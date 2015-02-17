package ncs2014.s06.twitterclient;
import java.io.File;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_tuito extends FragmentActivity implements OnClickListener{


	private  static int REQUEST_PICK = 1;

	private Status status;
	private User myUser;

	private EditText tweetText;
	private Twitter mTwitter;
	private SmartImageView userIcon;
	private Button tweet;
	private Button imageTweet;
	private Button bt_menu_time;
	private TextView tv_username;
	private String replyScreenName;

	//intent
	private Intent intent;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.twitter_tweet);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_default);
		TextView titleName = (TextView) findViewById(R.id.title_default_title);
		titleName.setText("ツイート");


		//findView
		userIcon = (SmartImageView) findViewById(R.id.icon);
		tweetText = (EditText) findViewById(R.id.tweet_tweet_text);
		tweet = (Button) findViewById(R.id.action_tweet);
		imageTweet = (Button) findViewById(R.id.add_image);
		tv_username = (TextView) findViewById(R.id.tv_usename);

		//リスナーセット
		tweet.setOnClickListener(this);
		imageTweet.setOnClickListener(this);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}

		mTwitter = TwitterUtils.getTwitterInstance(this);
		intent = getIntent();
		myUser = (User) intent.getSerializableExtra("myUser");
		
		status = (Status) intent.getSerializableExtra("rtStatus");
		replyScreenName = intent.getStringExtra("screenName");
		
		userIcon.setImageUrl(myUser.getProfileImageURL());
		tv_username.setText("@" + myUser.getScreenName());
		//リプライ
		if(replyScreenName != null){
			Log.d("test",replyScreenName);
			replyScreenName = "@" + replyScreenName + " ";
			tweetText.setText(replyScreenName);
			tweetText.setSelection(tweetText.length());
		}else{
			Log.d("test","replyScreenName = null");
		}
		//非公式RT
		if(status != null){
			tweetText.setText("RT @" + status.getUser().getScreenName() + ": " + status.getText());
			Log.d("null",status.getText());
		}else{
			Log.d("null", "status null");
		}
	}

	private void tweet() {
		AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				try {
					mTwitter.updateStatus(params[0]);
					return true;
				} catch (TwitterException e) {
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					showToast("ツイートが完了しました！");
					finish();
				} else {
					showToast("ツイートに失敗しました。。。");
				}
			}
		};
		task.execute(tweetText.getText().toString());
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				if (requestCode == REQUEST_PICK && resultCode == RESULT_OK) {
					Boolean result = false;

					Uri uri = data.getData();
					ContentResolver cr = getContentResolver();
					String[] columns = { MediaStore.Images.Media.DATA };
					Cursor c = cr.query(uri, columns, null, null, null);

					c.moveToFirst();
					File path = new File(c.getString(0));

					EditText textTweet = (EditText) findViewById(R.id.tweet_tweet_text);
					String tweet = textTweet.getText().toString();

					try {
						twitter4j.Status status = mTwitter.updateStatus(
								new StatusUpdate(tweet).media(path));
						result = true;
					} catch (Exception e) {
						result = false;
						e.printStackTrace();
					}finally{
						c.close();
					}

				if (result) {
					//showToast("ツイートが完了しました！");
					finish();
				} else {
					//showToast("ツイートに失敗しました。。。");
				}
				return true;
			}else{
				return false;
			}
	}
		};
	task.execute(tweetText.getText().toString());
	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		if(v == tweet){
			tweet();
			intent.removeExtra("rtStatus");
		}else{
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, REQUEST_PICK);
		}

		if(v == bt_menu_time){
			intent.setClass(getApplicationContext(), Twitter_home.class);
			startActivity(intent);
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
		}//if

	}
}
