package ncs2014.s06.twitterclient;
import java.io.File;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_tuito extends FragmentActivity implements OnClickListener{


	private  static int REQUEST_PICK = 1;
	private static String CONSUMER_KEY = "rnEXcIylpdaiO91qS8xQbV1J";
	private static String CONSUMER_SECRET = "1aFTk1YLNASB3lRcENcJZQca5T1PCv5nvKdyDgNmZfmWZ8104r";
	private static String ACCESS_TOKEN = "2882966317-gRwWjwwR1W3W09eKLrISoPKLmabNvsYIa98m0l";
	private static String ACCESS_TOKEN_SECRET = "t85YjUlwu6PRUqSMr91C3aeC3oBdN00h9x7HyTe5jupd0";


    private EditText mInputText;
    private Twitter mTwitter;
    private SmartImageView view;
    private Button tweet;
    private Button imageTweet;

  //intent
  	Intent intent = new Intent();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.twitter_tweet);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_tuito);


        mTwitter = TwitterUtils.getTwitterInstance(this);
        ImageGet ig = new ImageGet(mTwitter);
        view = (SmartImageView) findViewById(R.id.icon);
        ig.setImage(view);

        mTwitter = TwitterUtils.getTwitterInstance(this);
        mInputText = (EditText) findViewById(R.id.input_text);

        tweet = (Button) findViewById(R.id.action_tweet);
        imageTweet = (Button) findViewById(R.id.image_plus);

        tweet.setOnClickListener(this);
        imageTweet.setOnClickListener(this);
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
        task.execute(mInputText.getText().toString());
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

					EditText textTweet = (EditText) findViewById(R.id.input_text);
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
	task.execute(mInputText.getText().toString());
	}

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

	@Override
	public void onClick(View v) {
		if(v == tweet){
			tweet();
		}else{
			Intent intent = new Intent(Intent.ACTION_PICK);
    		intent.setType("image/*");
    		startActivityForResult(intent, REQUEST_PICK);
		}

	}
}
