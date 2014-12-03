package ncs2014.s06.twitterclient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Twitter_home extends Activity implements OnClickListener, OnRefreshListener{

	//メニューアイテム識別ID
	private static final int menu_tuito = 0;
	private static final int menu_user = 1;
	private static final int menu_dm = 2;
	private static final int menu_update = 3;

	//変数
	private ImageButton bt_update;
	private ImageButton bt_tuito;
	private ImageButton bt_user;
	private ImageButton bt_dm;
	private ImageButton bt_menu;
	private TextView title;
	private TweetAdapter tAdapter;
	private Twitter mTwitter;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView list;
	private Menu me;

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
		bt_update = (ImageButton) findViewById(R.id.bt_update);
		bt_tuito = (ImageButton) findViewById(R.id.bt_tuito);
		bt_user = (ImageButton) findViewById(R.id.bt_user);
		bt_dm = (ImageButton) findViewById(R.id.bt_dm);
		bt_menu = (ImageButton) findViewById(R.id.menu_bt);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		list = (ListView) findViewById(R.id.tllist);

		//リスナー
		bt_update.setOnClickListener(this);
		bt_tuito.setOnClickListener(this);
		bt_user.setOnClickListener(this);
		bt_dm.setOnClickListener(this);
		bt_menu.setOnClickListener(this);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}else{
			tAdapter = new TweetAdapter(this);
			mTwitter = TwitterUtils.getTwitterInstance(this);
			createSwipeRefreshLayout();
			reloadTimeLine();
			list.setAdapter(tAdapter);
		}
	}//onCreate

	public void createSwipeRefreshLayout(){
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setOnRefreshListener(this);
	}

	private void reloadTimeLine() {
		AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
			@Override
			protected List<twitter4j.Status> doInBackground(Void... params) {
				try {
					return mTwitter.getHomeTimeline();
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
					showToast("タイムラインの取得に成功しました");

					//la.getListView().setSelection(0);
				} else {
					showToast("タイムラインの取得に失敗しました");
				}
				swipeRefreshLayout.setRefreshing(false);
				ApiLimit api = new ApiLimit(mTwitter);
				api.execute();
				Map<String, RateLimitStatus> map = null;
				try {
					map = api.get();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				RateLimitStatus TLlimit = map.get("/statuses/home_timeline");
				int i = TLlimit.getSecondsUntilReset();
				String m = i / 60 + "分";
				String S = i % 60 + "秒";
				showToast("残り読み込み回数" + TLlimit.getRemaining() + "回\nAPIリセットまで" + m + S);
			}
		};
		task.execute();
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
		//押したときの処理
		switch (item.getItemId()) {
			case R.id.menu_tuito:
					startActivity(new Intent(
						Twitter_home.this,
						Twitter_tuito.class)
					);
			return true;

			case R.id.menu_user:
					startActivity(new Intent(
						Twitter_home.this,
						Twitter_user.class)
					);
			return true;

			case R.id.menu_dm:
					startActivity(new Intent(
						Twitter_home.this,
						Twitter_Client_DM.class)
					);
			return true;

			case menu_update:
				reloadTimeLine();
			return true;

			default:
			break;
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
			//reloadTimeLine();
			intent.setClass(getApplicationContext(), test.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
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

		if(v == bt_menu){
			openOptionsMenu();
		}//if

	}//on

	@Override
	public void onRefresh() {
		reloadTimeLine();
	}


}
