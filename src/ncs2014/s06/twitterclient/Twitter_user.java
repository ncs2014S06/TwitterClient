package ncs2014.s06.twitterclient;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
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
	private long cursor = -1L;
	private long[] idid;
	private Context mContext;
	private Twitter mTwitter;
	private Twitter TwitterUser;
	private User user;
	private User otherUser;
	private AsyncTask<Void, Void, IDs> IDTask;
	private AsyncTask<Void, Void, User> followTask;
	private IDs getFriendsIDs;
	private GestureDetector mGestureDetector;
	private int flingFlag = 0;
	private static final int FLAG_TRUE = 1;
	private static final int FLAG_FALSE = 0;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

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
	private Button createFollowBt;
	private TextView text_username;
	private TextView text_userfrom;
	private TextView text_context;
	private LinearLayout layout1;
	private TweetAdapter tAdapter;
	private drawable followImage;
	private boolean followFlag;

	//intent
	Intent intent = new Intent();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.twitter_user_status);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_user);

		//findview
		myTweet = (Button) findViewById(R.id.myTweet);
		follow = (Button) findViewById(R.id.follow);
		follower = (Button) findViewById(R.id.follower);
		fav = (Button) findViewById(R.id.fav);
		createFollowBt = (Button) findViewById(R.id.create_follow_bt);
		//bt_menu_time = (Button) findViewById(R.id.bt_menu_time);

		text_username = (TextView) findViewById(R.id.text_username);
		text_userfrom = (TextView) findViewById(R.id.text_userfrom);
		text_context = (TextView) findViewById(R.id.text_context);
		layout1 = (LinearLayout) findViewById(R.id.linearLayout3);
		myImage = (SmartImageView) findViewById(R.id.tweet_detail_usericon);
		backImage = (SmartImageView) findViewById(R.id.backimage);

		//リスナー
		//bt_menu_time.setOnClickListener(this);

		myTweet.setOnClickListener(this);
		follow.setOnClickListener(this);
		follower.setOnClickListener(this);
		fav.setOnClickListener(this);
		createFollowBt.setOnClickListener(this);
		intent = getIntent();
		mTwitter = TwitterUtils.getTwitterInstance(this);
		user = (User) intent.getSerializableExtra("myUser");
		otherUser = (User) intent.getSerializableExtra("otherUser");
		tAdapter = new TweetAdapter(this);
		followFlag = true;
		final ImageGet ig = new ImageGet(mTwitter);
		TwitterUser = mTwitter;
		intent.putExtra("TwitterUser",TwitterUser);
		intent.putExtra("otherUserId", otherUser);
		//フォローリスト取得
		friendListGet();

				if(otherUser != null){
					user = otherUser;
				}

				ig.setImage(myImage,user.getScreenName());

				//ヘッダーの画像変更
				backImageStr = user.getProfileBannerURL();
				backImage.setImageUrl(backImageStr);

				//ボタンに各種値をセット
				userStatusGet();
			}


	private void friendListGet(){
		IDTask = new AsyncTask<Void, Void, IDs>(){
			@Override
			protected IDs doInBackground(Void... params) {
				//フォローしているユーザーID取得
				try {
					getFriendsIDs = mTwitter.getFriendsIDs(cursor);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				return getFriendsIDs;
			}

			@Override
			protected void onPostExecute(IDs result) {
				super.onPostExecute(result);
				//ユーザーIDをリストに突っ込む
				idid = result.getIDs();

				//フォローしてるかチェック
				for(long a:idid){
					if(user.getId() == a){
						imageChange();
						followFlag = false;
					}
				}
			}
		};
		IDTask.execute();
	}//friendListGet

	private void imageChange(){
		if(followFlag){
			createFollowBt.setText("フォロー中\n");

			createFollowBt.setBackgroundColor(Color.rgb(159, 217, 246));
		//	doFollow.setBackgroundResource(R.drawable.ic_assignment_ind_black_48dp);
			followFlag = false;
		}else{
			createFollowBt.setText("フォローする\n");
			createFollowBt.setBackgroundColor(Color.rgb(170, 170, 170));
		//	doFollow.setBackgroundResource(R.drawable.ic_assignment_ind_grey600_48dp);
			followFlag = true;
		}
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
			intent.putExtra("otherUser", otherUser);
			startActivity(intent);
		}//if

		if(v == createFollowBt){
			if(followFlag){
				followTask = new AsyncTask<Void, Void, User>(){

					@Override
					protected User doInBackground(Void... params) {
						try {
							mTwitter.createFriendship(user.getId());
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
						imageChange();
					}
				};
				followTask.execute();
			}else{
				followTask = new AsyncTask<Void, Void, User>(){
					@Override
					protected User doInBackground(Void... params) {
						try {
							mTwitter.destroyFriendship(user.getId());
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
						imageChange();
					}
				};
				followTask.execute();
			}
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
		/*
		if(v == bt_menu_time){
			intent.setClass(getApplicationContext(), Twitter_home.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if
		 */
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


	//横スワイプ処理
	private final SimpleOnGestureListener mOnGestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
			flingFlag = FLAG_TRUE;
			try {

				if (Math.abs(event1.getY() - event2.getY()) > SWIPE_MAX_OFF_PATH) {
					// 縦の移動距離が大きすぎる場合は無視
					return false;
				}

				if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// 開始位置から終了位置の移動距離が指定値より大きい
					// X軸の移動速度が指定値より大きい
			//		intent.setClass(mContext, Twitter_Streaming.class);
			//		startActivity(intent);
			//		overridePendingTransition(R.anim.right_in, R.anim.left_out);

				} else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// 終了位置から開始位置の移動距離が指定値より大きい
					// X軸の移動速度が指定値より大きい
			//		intent.setClass(mContext, TwitterSearch.class);
			//		startActivity(intent);
			//		overridePendingTransition(R.anim.left_in, R.anim.right_out);
				}

			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	};
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

}
