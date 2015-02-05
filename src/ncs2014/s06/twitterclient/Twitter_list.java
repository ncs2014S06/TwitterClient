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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Twitter_list extends Activity implements OnItemClickListener,OnRefreshListener,OnScrollListener{

	//変数
	private Intent intent; //intent
	private UserListTimeLine userList;
	private TweetAdapter tAdapter;
	private Twitter mTwitter;
	private final static int TWEET_DETAIL = 0;
	private User myUser;
	private Paging paging;
	private Handler mHandler;
	private Context mContext;
	private Long listId;



	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView list;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//タイトルバーのカスタマイズ
		setContentView(R.layout.twitter_home);
		mContext = getApplicationContext();
		mHandler = new Handler();

		//findview
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		list = (ListView) findViewById(R.id.tllist);

		//リスナー
		list.setOnItemClickListener(this);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}else{
			intent = getIntent();
			listId = intent.getLongExtra("ListId", 0);
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
			userList = new UserListTimeLine(mContext,mHandler, tAdapter, swipeRefreshLayout,listId);
			createSwipeRefreshLayout();
			userList.reloadUserList(paging,0);
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

		switch (item.getItemId()) {

		case R.id.menu_update:

			userList.reloadUserList(paging,0);
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
		 return true;
	}

	@Override
	public void onRefresh() {
		userList.reloadUserList(paging,0);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int iTop,
			int iVisible, int iTotal) {
		boolean bLast = iTotal == iTop + iVisible;
		if(bLast){
			if(list.getCount() != 0){
				if(!userList.taskRunning()){
					Log.d("scroll","最後尾だよ");
					userList.reloadUserList(paging,1);
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Log.d("tweet_detail","view:" + parent + "  position:" + position + "  id:" + id);
		Status item = (Status) list.getItemAtPosition(position);
		intent.setClass(getApplicationContext(), Twitter_tweet_detail.class);
		intent.putExtra("TweetStatus", item);
		intent.putExtra("position", position);
		startActivityForResult(intent,TWEET_DETAIL);
	}

	public void exchangeListItem(int position,Status status){
		tAdapter.remove(tAdapter.getItem(position));
		tAdapter.insert(status, position);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(requestCode, resultCode, data);
		this.intent = data;
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
		}
	}
}
