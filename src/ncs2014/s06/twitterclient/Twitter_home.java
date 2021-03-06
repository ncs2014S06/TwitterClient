package ncs2014.s06.twitterclient;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Twitter_home extends Activity implements OnItemClickListener,OnClickListener, OnRefreshListener,OnScrollListener, OnTouchListener{

	//変数
	private Intent intent; //intent
	private TimeLine timeLine;
	private TweetAdapter tAdapter;
	private Twitter mTwitter;
	private Menu me;
	private Paging paging;
	private int page;
	private User myUser;
	private Handler mHandler;
	private Context mContext;
	private GestureDetector mGestureDetector;
	private int flingFlag = 0;
	private static final int FLAG_TRUE = 1;
	private static final int FLAG_FALSE = 0;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private final static int TWEET_DETAIL = 1000;
	private final static int ACCOUNT_CONTROL = 1010;



	private SwipeRefreshLayout swipeRefreshLayout;
	private ImageButton bt_update;
	private ImageButton bt_tuito;
	private ImageButton bt_user;
	private ImageButton bt_dm;
	private ImageButton bt_menu;
	private Button bt_menu_user;
	private Button bt_menu_tweet;
	private Button bt_title_menu;
	private TextView title;
	private ListView list;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//タイトルバーのカスタマイズ
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.twitter_home);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		mContext = getApplicationContext();
		mHandler = new Handler();
		mGestureDetector = new GestureDetector(mContext,mOnGestureListener);

		//findview
		bt_update = (ImageButton) findViewById(R.id.bt_reply);
		bt_tuito = (ImageButton) findViewById(R.id.bt_retweet);
		bt_user = (ImageButton) findViewById(R.id.bt_fav);
		bt_dm = (ImageButton) findViewById(R.id.bt_more);
		bt_title_menu = (Button)findViewById(R.id.bt_title);
//		bt_menu_user = (Button) findViewById(R.id.bt_menu_user);
//		bt_menu_tweet = (Button) findViewById(R.id.bt_menu_tweet);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		list = (ListView) findViewById(R.id.tllist);

		//リスナー
		bt_update.setOnClickListener(this);
		bt_tuito.setOnClickListener(this);
		bt_user.setOnClickListener(this);
		bt_dm.setOnClickListener(this);
		bt_title_menu.setOnClickListener(this);
//		bt_menu_user.setOnClickListener(this);
//		bt_menu_tweet.setOnClickListener(this);
		list.setOnTouchListener(this);
		list.setOnItemClickListener(this);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}else{
			if(intent == null){
				intent = new Intent();
			}
			tAdapter = new TweetAdapter(this);
			list.setAdapter(tAdapter);
			paging = new Paging(1);
			mTwitter = TwitterUtils.getTwitterInstance(this);
			mHandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					myUser = (User) msg.getData().getSerializable("myUser");
					intent.putExtra("myUser", myUser);
				}
			};
			new MyUser(mTwitter,mHandler).execute();
			mHandler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					Throwable e = (Throwable)msg.obj;
					showToast("Twitterに接続できません\n接続を確認してください");
				}
			};
			timeLine = new TimeLine(mContext,mHandler, tAdapter, swipeRefreshLayout);
			createSwipeRefreshLayout();
			timeLine.reloadTimeLine(paging,0);
			list.setOnScrollListener(this);
		}
	}//onCreate

	public void createSwipeRefreshLayout(){
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setOnRefreshListener(this);
	}


	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}


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
					intent.setClass(mContext, Twitter_Streaming.class);
					startActivity(intent);
					overridePendingTransition(R.anim.right_in, R.anim.left_out);

				} else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// 終了位置から開始位置の移動距離が指定値より大きい
					// X軸の移動速度が指定値より大きい
					intent.setClass(mContext, TwitterSearch.class);
					startActivity(intent);
					overridePendingTransition(R.anim.left_in, R.anim.right_out);
				}

			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	};


	/**
	 *  メニュー
	 */
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_tuito:
			intent.setClass(mContext, Twitter_tuito.class);
			startActivity(intent);
			return true;

		case R.id.menu_dm:

			intent.setClass(mContext, Twitter_Client_DM.class);
			startActivity(intent);
			return true;

		case R.id.menu_search:

			intent.setClass(mContext, TwitterSearch.class);
			startActivity(intent);
			return true;

		case R.id.menu_user:

			intent.setClass(mContext, Twitter_user.class);
			Log.d("test", intent.getSerializableExtra("myUser").toString());
			startActivity(intent);
			return true;
		case R.id.menu_update:

			timeLine.reloadTimeLine(paging,0);
			return true;

		case R.id.menu_list:
			intent.setClass(mContext, Twitter_lists.class);
			startActivity(intent);
			return true;

		case R.id.menu_account:
			intent.setClass(mContext, Twitter_AccountControl.class);
			startActivityForResult(intent,ACCOUNT_CONTROL);
			return true;

		case R.id.menu_streaming:
			intent.setClass(mContext, Twitter_Streaming.class);
			startActivity(intent);
			return true;

		default:
			return false;
		}
	}//select

	/**
	 * 画面がタッチされた時の動き
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		if(v == bt_update){
			timeLine.reloadTimeLine(paging,0);
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
		}//if

		if(v == bt_dm){
			intent.setClass(getApplicationContext(), Twitter_Client_DM.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if

		/*if(v == bt_menu){
			openOptionsMenu();
		}//if
		*/

		if(v == bt_title_menu){
			openOptionsMenu();
		}//if

		/**
		if(v == bt_menu_user){
			intent.setClass(getApplicationContext(), Twitter_user.class);
			startActivity(intent);
		}//if

		if(v == bt_menu_tweet){
			intent.setClass(getApplicationContext(), Twitter_tuito.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if
		**/

	}//on

	@Override
	public void onRefresh() {
		timeLine.reloadTimeLine(paging,0);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onScroll(AbsListView view, int iTop,
			int iVisible, int iTotal) {
		boolean bLast = iTotal == iTop + iVisible;
		if(bLast){
			if(list.getCount() != 0){
				if(!timeLine.taskRunning()){
					Log.d("scroll","最後尾だよ");
					timeLine.reloadTimeLine(paging,1);
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(flingFlag == FLAG_FALSE){
			Log.d("tweet_detail","view:" + parent + "  position:" + position + "  id:" + id);
			Status item = (Status) list.getItemAtPosition(position);
			intent.setClass(getApplicationContext(), Twitter_tweet_detail.class);
			intent.putExtra("TweetStatus", item);
			intent.putExtra("position", position);
			startActivityForResult(intent,TWEET_DETAIL);
		}
		flingFlag = FLAG_FALSE;

	}

	public void exchangeListItem(int position,Status status){
		tAdapter.remove(tAdapter.getItem(position));
		tAdapter.insert(status, position);
		tAdapter.notifyDataSetChanged();
	}
	public void deleteListItem(int position,Status status){
		tAdapter.remove(tAdapter.getItem(position));
		//tAdapter.insert(status, position);
		tAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(requestCode, resultCode, data);
		this.intent = data;
		if(data == null){
			Log.d("null", "data null");
		}
		// startActivityForResult()の際に指定した識別コードとの比較
		if( requestCode == TWEET_DETAIL ){
			// 返却結果ステータスとの比較
			if( resultCode == Activity.RESULT_OK ){
				// 返却されてきたintentから値を取り出す
				int position = intent.getIntExtra( "position", 0 );
				myUser = (User) intent.getSerializableExtra("myUser");
				Status tweetStatus = (Status) intent.getSerializableExtra("tweetStatus");
				exchangeListItem(position, tweetStatus);
			}

			if( resultCode == 3){
				// 返却されてきたintentから値を取り出す
				Log.d("test","adsasdasdasdgfythht");
				int position = intent.getIntExtra( "position", 0 );
				myUser = (User) intent.getSerializableExtra("myUser");
				Status tweetStatus = (Status) intent.getSerializableExtra("tweetStatus");
				deleteListItem(position, tweetStatus);
			}

		}
		if(requestCode == ACCOUNT_CONTROL){
			Log.d("AccountControl","return AccountControl");
			if(intent == null){
				Log.d("null", "intent null");
			}
			if(intent.getIntExtra("changeFlag", 0) != 0){
				mTwitter = TwitterUtils.getTwitterInstance(mContext);
				timeLine = new TimeLine(mContext,mHandler, tAdapter, swipeRefreshLayout);
				timeLine.reloadTimeLine(paging,0);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

}
