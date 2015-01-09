package ncs2014.s06.twitterclient;


import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

public class Twitter_user extends Activity implements OnClickListener {

	private SmartImageView view;

	//カウント用変数
	private String userName;
	private String userFrom;
	private String userContext;
	private int Count_tweet;
	private int Count_follow ;
	private int Count_follower;
	private int Count_fav;
	private String mytweet_c;
	private String follow_c;
	private String follower_c;
	private String fav_c;
	private String backImageStr;

	private SmartImageView myImage;
	private Twitter mTwitter;
	private ImageButton bt_update;
	private ImageButton bt_tuito;
	private ImageButton bt_user;
	private ImageButton bt_dm;
	private User user;

	//intent
	Intent intent = new Intent();
	private Button myTweet;
	private Button follow;
	private Button follower;
	private Button fav;
	private Button bt_menu_time;
	private Button bt_menu_user;
	private TextView text_username;
	private TextView text_userfrom;
	private TextView text_context;
	private LinearLayout layout1;
	private TweetAdapter tAdapter;
	private ListView list;

	private String followerName;
	private SmartImageView followerImg;
	private SmartImageView backImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		ImageGet ig = new ImageGet(mTwitter);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.twitter_user_status);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_user);

		//findview
		bt_update = (ImageButton) findViewById(R.id.bt_update);
		bt_tuito = (ImageButton) findViewById(R.id.bt_tuito);
		bt_user = (ImageButton) findViewById(R.id.bt_user);
		bt_dm = (ImageButton) findViewById(R.id.bt_dm);

		myTweet = (Button) findViewById(R.id.myTweet);
		follow = (Button) findViewById(R.id.follow);
		follower = (Button) findViewById(R.id.follower);
		fav = (Button) findViewById(R.id.fav);
		bt_menu_time = (Button) findViewById(R.id.bt_menu_time);

		text_username = (TextView) findViewById(R.id.text_username);
		text_userfrom = (TextView) findViewById(R.id.text_userfrom);
		text_context = (TextView) findViewById(R.id.text_context);

		layout1 = (LinearLayout) findViewById(R.id.linearLayout3);

		myImage = (SmartImageView) findViewById(R.id.image_user);
		backImage = (SmartImageView) findViewById(R.id.backimage);

		//リスナー
		bt_update.setOnClickListener(this);
		bt_tuito.setOnClickListener(this);
		bt_user.setOnClickListener(this);
		bt_dm.setOnClickListener(this);
		bt_menu_time.setOnClickListener(this);

		myTweet.setOnClickListener(this);
		follow.setOnClickListener(this);
		follower.setOnClickListener(this);
		fav.setOnClickListener(this);

		mTwitter = TwitterUtils.getTwitterInstance(this);
		tAdapter = new TweetAdapter(this);

		AsyncTask<Void, Void, twitter4j.User> task = new AsyncTask<Void, Void, twitter4j.User>() {
			@Override
			protected User doInBackground(Void... params) {
				try {
					user = mTwitter.verifyCredentials();
					return user;
				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(User result) {
				// TODO 自動生成されたメソッド・スタブ
				super.onPostExecute(result);
				//ヘッダーの画像変更
				backImageStr = result.getProfileBannerURL();
				backImage.setImageUrl(backImageStr);
				backImage.setScaleType(SmartImageView.ScaleType.FIT_XY);
				userName = user.getName();
				userFrom = user.getScreenName();
				userContext = user.getDescription();
				Count_tweet = user.getStatusesCount();
				Count_follow =user.getFriendsCount();
				Count_follower =user.getFollowersCount();
				Count_fav = user.getFavouritesCount();
			}

        };
        task.execute();

		//プロフィール画像変更
		ig.setImage(myImage);

		tAdapter = new TweetAdapter(this);

		//ボタンに各種値をセット
		userStatusGet();
	}

	@Override
	public void onClick(View v) {
		if(v == myTweet){
			startActivity(new Intent(Twitter_user.this,TweetGet.class));
		}

		if(v == follow){
			startActivity(new Intent(Twitter_user.this,FollowGet.class));
		}

		if(v == follower){
			startActivity(new Intent(Twitter_user.this,FollowerGet.class));
		}

		if(v == fav){
			startActivity(new Intent(Twitter_user.this,FavoriteGet.class));
		}

		if(v == bt_update){
		}//if

		//ツイート画面
		if(v == bt_tuito){
			intent.setClass(getApplicationContext(), Twitter_tuito.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if


		if(v == bt_user){
			intent.setClass(getApplicationContext(), Twitter_user.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if

		if(v == bt_dm){
			intent.setClass(getApplicationContext(), Twitter_Client_DM.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if

		if(v == bt_menu_time){
			intent.setClass(getApplicationContext(), Twitter_home.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if
	}

	public void userStatusGet(){
		//変数宣言
		mytweet_c = String.valueOf(Count_tweet);
		follow_c= String.valueOf(Count_follow);
		follower_c = String.valueOf(Count_follower);
		fav_c = String.valueOf(Count_fav);

		//文字セット
		text_username.setText(userName);
		text_userfrom.setText("@" + userFrom);
		text_context.setText(userContext);
		myTweet.setText("ツイート\n" + mytweet_c);
		follow.setText("フォロー\n" + follow_c);
		follower.setText("フォロワー\n" + follower_c);
		fav.setText("お気に入り\n" + fav_c);
	}//userStatus

}
