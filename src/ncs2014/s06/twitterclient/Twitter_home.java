package ncs2014.s06.twitterclient;

import java.util.ArrayList;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Twitter_home extends Activity implements OnClickListener{

	//メニューアイテム識別ID
	private static final int MENU_A = 0;
	private static final int MENU_B = 1;
	private static final int MENU_C = 2;
	//変数
	private Button bt1;
	private Button bt2;
	private TweetAdapter tAdapter;
	private Twitter mTwitter;
	private ListView list;

	//intent
	Intent intent = new Intent();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_home);

		//findview
		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		list = (ListView) findViewById(R.id.tllist);

		//リスナー
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}else{
			tAdapter = new TweetAdapter(this);
			mTwitter = TwitterUtils.getTwitterInstance(this);
			reloadTimeLine();
			list.setAdapter(tAdapter);
		}
	}//onCreate


	private void reloadTimeLine() {
		AsyncTask<Void, Void, List<String>> task = new AsyncTask<Void, Void, List<String>>() {
			@Override
			protected List<String> doInBackground(Void... params) {
				try {
					ResponseList<twitter4j.Status> timeline = mTwitter.getHomeTimeline();
					ArrayList<String> list = new ArrayList<String>();
					for (twitter4j.Status status : timeline) {
						list.add(status.getText());
					}
					return list;
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<String> result) {
				if (result != null) {
					tAdapter.clear();
					for (String status : result) {
						tAdapter.add(status);
					}
					//la.getListView().setSelection(0);
				} else {
					showToast("タイムラインの取得に失敗しました。。。");
				}
			}
		};
		task.execute();
	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//押したときの処理
		switch (item.getItemId()) {
			case MENU_A:
			return true;

			case MENU_B:
					startActivity(new Intent(
						Twitter_home.this,
						Twitter_user.class)
					);
			return true;

			case MENU_C:
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
		//ユーザ画面
		if(v == bt1){
			intent.setClass(getApplicationContext(), Twitter_user.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if

		//ツイート画面
		if(v == bt2){
			intent.setClass(getApplicationContext(), Twitter_Client_DM.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);

		}//if



	}
	//表示定義クラス
	private class TweetAdapter extends ArrayAdapter<String> {

		 public TweetAdapter(Context context) {
			  super(context, android.R.layout.simple_list_item_1);
		 }
	}//TweetAdapter


}
