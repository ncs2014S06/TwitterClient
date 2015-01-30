package ncs2014.s06.twitterclient;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.R.drawable;
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
	private String backImageStr;
	private Twitter mTwitter;
	private Twitter TwitterUser;
	private User user;
	private User otherUser;
	private AsyncTask<Void, Void, User> task;
	private IDs getFriendsIDs;

	private SmartImageView myImage;
	private SmartImageView backImage;
	private ImageButton bt_update;
	private ImageButton bt_tuito;
	private ImageButton bt_user;
	private ImageButton bt_dm;
	private Button myTweet;
	private Button follow;
	private Button follower;
	private Button fav;
	private Button bt_menu_time;
	private ImageButton doFollow;
	private TextView text_username;
	private TextView text_userfrom;
	private TextView text_context;
	private LinearLayout layout1;
	private TweetAdapter tAdapter;
	private drawable followImage;

	//intent
	Intent intent = new Intent();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.twitter_user_status);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_user);

		//findview
		bt_update = (ImageButton) findViewById(R.id.bt_reply);
		bt_tuito = (ImageButton) findViewById(R.id.bt_retweet);
		bt_user = (ImageButton) findViewById(R.id.bt_fav);
		bt_dm = (ImageButton) findViewById(R.id.bt_more);

		myTweet = (Button) findViewById(R.id.myTweet);
		follow = (Button) findViewById(R.id.follow);
		follower = (Button) findViewById(R.id.follower);
		fav = (Button) findViewById(R.id.fav);
		doFollow = (ImageButton) findViewById(R.id.doFollow);
		bt_menu_time = (Button) findViewById(R.id.bt_menu_time);

		text_username = (TextView) findViewById(R.id.text_username);
		text_userfrom = (TextView) findViewById(R.id.text_userfrom);
		text_context = (TextView) findViewById(R.id.text_context);
		layout1 = (LinearLayout) findViewById(R.id.linearLayout3);
		myImage = (SmartImageView) findViewById(R.id.tweet_detail_usericon);
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
		doFollow.setOnClickListener(this);
		intent = getIntent();
		mTwitter = TwitterUtils.getTwitterInstance(this);
		user = (User) intent.getSerializableExtra("myUser");
		otherUser = (User) intent.getSerializableExtra("otherUser");
		tAdapter = new TweetAdapter(this);
		final ImageGet ig = new ImageGet(mTwitter);
		TwitterUser = mTwitter;
		intent.putExtra("TwitterUser",TwitterUser);
		intent.putExtra("otherUserId", otherUser);
				try {
					if(otherUser != null){
						user = otherUser;
						long cursor = -1L;
						long[] idid;

						//フォローしているユーザーID取得
						getFriendsIDs = mTwitter.getFriendsIDs(cursor);

						//ユーザーIDをリストに突っ込む
						idid = getFriendsIDs.getIDs();

						//フォローしてるかチェック
						for(long a:idid){
							if(user.getId() == a){
								doFollow.setBackgroundResource(R.drawable.ic_assignment_ind_black_48dp);
							}
						}

					}//else
				} catch (TwitterException e) {
					e.printStackTrace();
				}//try

				ig.setImage(myImage,user.getScreenName());

				//ヘッダーの画像変更
				backImageStr = user.getProfileBannerURL();
				backImage.setImageUrl(backImageStr);

				//ボタンに各種値をセット
				userStatusGet();
			}

	@Override
	public void onClick(View v) {
		if(v == myTweet){
			intent.setClass(getApplicationContext(), TweetGet.class);
			startActivity(intent);
		}//if

		if(v == follow){
			intent.setClass(getApplicationContext(), FollowGet.class);
			startActivity(intent);
		}//if

		if(v == follower){
			intent.setClass(getApplicationContext(), FollowerGet.class);
			startActivity(intent);
		}//if

		if(v == fav){
			intent.setClass(getApplicationContext(), FavoriteGet.class);
			startActivity(intent);
		}//if

		if(v == doFollow){

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

		try{
			//数字を文字に変換
			mytweet_c = String.valueOf(user.getStatusesCount());
			follow_c= String.valueOf(user.getFriendsCount());
			follower_c = String.valueOf(user.getFollowersCount());
			fav_c = String.valueOf(user.getFavouritesCount());
			//文字セット
			text_username.setText(user.getName());
			text_userfrom.setText("@" + user.getScreenName());
			text_context.setText(user.getDescription());
			myTweet.setText("ツイート\n" + mytweet_c);
			follow.setText("フォロー\n" + follow_c);
			follower.setText("フォロワー\n" + follower_c);
			fav.setText("お気に入り\n" + fav_c);

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
		}//catch

	}//userStatus

}
