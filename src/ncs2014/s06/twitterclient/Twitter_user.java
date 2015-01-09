package ncs2014.s06.twitterclient;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;


public class Twitter_user extends Activity implements OnClickListener {

	//カウント用変数
	private String mytweet_c;
	private String follow_c;
	private String follower_c;
	private String fav_c;
	private String myList_c;
	private String backImageStr;

	private SmartImageView myImage;
	private Twitter mTwitter;
	private ImageButton bt_update;
	private ImageButton bt_tuito;
	private ImageButton bt_user;
	private ImageButton bt_dm;
	private User user;

	private Button myTweet;
	private Button follow;
	private Button follower;
	private Button fav;
	private Button myList;
	private Button bt_menu_time;
	private TextView text_username;
	private TextView text_userfrom;
	private TextView text_context;
	private LinearLayout layout1;
	private TweetAdapter tAdapter;
	private String followerName;
	private SmartImageView followerImg;
	private SmartImageView backImage;

	//intent
	Intent intent = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		myList = (Button) findViewById(R.id.mylist);
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
		myList.setOnClickListener(this);

		mTwitter = TwitterUtils.getTwitterInstance(this);
		tAdapter = new TweetAdapter(this);
		ImageGet ig = new ImageGet(mTwitter);

		try {
			user = mTwitter.verifyCredentials();
		} catch (TwitterException e) {
			e.printStackTrace();
		}//try

		//プロフィール画像変更
		//myImage.setScaleType(SmartImageView.ScaleType.FIT_XY);
		ig.setImage(myImage);

		//ヘッダーの画像変更
		backImageStr = user.getProfileBannerURL();
		backImage.setImageUrl(backImageStr);
		backImage.setScaleType(SmartImageView.ScaleType.FIT_XY);

		//ボタンに各種値をセット
		userStatusGet();

	}



	@Override
	public void onClick(View v) {
		if(v == myTweet){
			Log.d("tweet","tddddddd");
			tweetGet();
		}//if

		if(v == follow){
			setContentView(R.layout.twitter_follow);
			followGet();
		}//if

		if(v == follower){
			followerGet();
		}//if

		if(v == fav){
			favGet();
		}//if

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

	private void tweetGet() {
		AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
		@Override
		protected List<twitter4j.Status> doInBackground(Void... params) {
			try {
				return mTwitter.getUserTimeline(mTwitter.getId());
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<twitter4j.Status> result) {
			if (result != null) {
				tAdapter.clear();
				for (twitter4j.Status status : result) {
					tAdapter.add(status);
				}
			}
		}
	};
	task.execute();


    }//tweetGet

	public void followGet(){
		int count =user.getFriendsCount();
		String b= String.valueOf(count);
		follow.setText("フォロー\n" + b);
	}

	public void followerGet(){

	}

	public void favGet(){
	}

	public void userStatusGet(){

		try{
			//数字を文字に変換
			mytweet_c = String.valueOf(user.getStatusesCount());
			follow_c= String.valueOf(user.getFriendsCount());
			follower_c = String.valueOf(user.getFollowersCount());
			fav_c = String.valueOf(user.getFavouritesCount());
			myList_c = String.valueOf(user.getListedCount());

			//文字セット
			text_username.setText(user.getName());
			text_userfrom.setText("@" + user.getScreenName());
			text_context.setText(user.getDescription());
			myTweet.setText("ツイート\n" + mytweet_c);
			follow.setText("フォロー\n" + follow_c);
			follower.setText("フォロワー\n" + follower_c);
			fav.setText("お気に入り\n" + fav_c);
			myList.setText("リスト\n" + myList_c);

		}catch(NullPointerException e){
			Log.d("nullです","ヌルに入りました");
			//文字セット
			text_username.setText("null");
			text_userfrom.setText("@" + "null");
			text_context.setText("null");
			myTweet.setText("null");
			follow.setText("null");
			follower.setText("null");
			fav.setText("null");
			myList.setText("null");
		}//catch

	}//userStatus

}
