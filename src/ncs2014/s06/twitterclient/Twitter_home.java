package ncs2014.s06.twitterclient;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

public class Twitter_home extends Activity implements OnItemClickListener,OnClickListener, OnRefreshListener,OnScrollListener{


	//変数
	private TimeLine timeLine;
	private ImageButton bt_update;
	private ImageButton bt_tuito;
	private ImageButton bt_user;
	private ImageButton bt_dm;
	private ImageButton bt_menu;
	private Button bt_menu_user;
	private Button bt_menu_tweet;
	private TextView title;
	private TweetAdapter tAdapter;
	private Twitter mTwitter;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView list;
	private Menu me;
	private Paging paging;
	private int page;

	//intent
	Intent intent = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//タイトルバーのカスタマイズ
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.twitter_home);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		//findview
		bt_update = (ImageButton) findViewById(R.id.bt_reply);
		bt_tuito = (ImageButton) findViewById(R.id.bt_retweet);
		bt_user = (ImageButton) findViewById(R.id.bt_fav);
		bt_dm = (ImageButton) findViewById(R.id.bt_more);
		bt_menu_user = (Button) findViewById(R.id.bt_menu_user);
		bt_menu_tweet = (Button) findViewById(R.id.bt_menu_tweet);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		list = (ListView) findViewById(R.id.tllist);

		//リスナー
		bt_update.setOnClickListener(this);
		bt_tuito.setOnClickListener(this);
		bt_user.setOnClickListener(this);
		bt_dm.setOnClickListener(this);
		bt_menu_user.setOnClickListener(this);
		bt_menu_tweet.setOnClickListener(this);
		list.setOnItemClickListener(this);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}else{
			tAdapter = new TweetAdapter(this);
			list.setAdapter(tAdapter);
			paging = new Paging(1);
			mTwitter = TwitterUtils.getTwitterInstance(this);
			timeLine = new TimeLine(this, mTwitter, tAdapter, swipeRefreshLayout);
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
		if(item == bt_tuito){
			startActivity(new Intent(Twitter_home.this,Twitter_user.class));
			intent.setClass(getApplicationContext(), Twitter_tuito.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}
		return false;
	}//select

	/**
	 * 画面がタッチされた時の動き
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		 return true;
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
			intent.putExtra("mTwitter", mTwitter);
			startActivity(intent);
			//startActivity(new Intent(Twitter_home.this,Twitter_user.class));
		}//if

		if(v == bt_dm){
			intent.setClass(getApplicationContext(), Twitter_Client_DM.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if

		if(v == bt_menu){
			openOptionsMenu();
		}//if

		if(v == bt_menu_user){
			startActivity(new Intent(Twitter_home.this,Twitter_user.class));
			Log.d("menu user", "menu_user");
		}//if

		if(v == bt_menu_tweet){
			intent.setClass(getApplicationContext(), Twitter_tuito.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if

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
		Status item = (Status) list.getItemAtPosition(position);
		Intent intent = new Intent(getApplication(),Twitter_tweet_detail.class);
		intent.putExtra("mTwitter", mTwitter);
		intent.putExtra("TweetId", item);
		startActivity(intent);
	}
}
