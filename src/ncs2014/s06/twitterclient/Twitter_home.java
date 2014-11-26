package ncs2014.s06.twitterclient;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_home extends Activity implements OnClickListener{

	//メニューアイテム識別ID
	private static final int tuito = 0;
	private static final int user = 1;
	private static final int DM = 2;
	private static final int update = 3;
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



	/**
	 *  メニュー
	 */
	public boolean onCreateOptionsMenu(Menu menu){

		menu.add(0, update, 0, "更新");
		menu.add(0, tuito, 0, "ツイート画面");
		menu.add(0, user, 0, "ユーザ画面");
		menu.add(0, DM, 0, "DM画面");

		return true;
	}




	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//押したときの処理
		switch (item.getItemId()) {
			case tuito:
					startActivity(new Intent(
						Twitter_home.this,
						Twitter_tuito.class)
					);
			return true;

			case user:
					startActivity(new Intent(
						Twitter_home.this,
						Twitter_user.class)
					);
			return true;

			case DM:
					startActivity(new Intent(
						Twitter_home.this,
						Twitter_Client_DM.class)
					);
			return true;

			case update:
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
		//ユーザ画面
		if(v == bt1){
			intent.setClass(getApplicationContext(), Twitter_user.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if

		//ツイート画面
		if(v == bt2){
			intent.setClass(getApplicationContext(), Twitter_tuito.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);

		}//if



	}
	//表示定義クラス
	private class TweetAdapter extends ArrayAdapter<twitter4j.Status> {

		private LayoutInflater mInflater;

		public TweetAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1);
			mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自動生成されたメソッド・スタブ

			if(convertView == null){
				convertView = mInflater.inflate(R.layout.list_item_tweet, null);
			}

			Status item = getItem(position);
			SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.icon);
			icon.setImageUrl(item.getUser().getProfileImageURL());
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(item.getUser().getName());
			TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
			screenName.setText("@" + item.getUser().getScreenName());
			TextView text = (TextView) convertView.findViewById(R.id.text);
			text.setText(item.getText());
			return convertView;
		}
	}//TweetAdapter


}
