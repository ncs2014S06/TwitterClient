package ncs2014.s06.twitterclient;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_Client_DM extends FragmentActivity {

	 private Twitter mTwitter;
	private TweetAdapter tAdapter;
	private ImageGet ig;
	private ListView list;
	private DirectMessage item;
	private TextView name;

	Intent intent = new Intent();



	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_dmlist);

		list = (ListView) findViewById(R.id.dmlist);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}else{
			tAdapter = new TweetAdapter(this);
			list.setAdapter(tAdapter);
			mTwitter = TwitterUtils.getTwitterInstance(this);
			ig = new ImageGet(mTwitter);
		}


		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	        	DirectMessage msg = (DirectMessage)list.getItemAtPosition(position);

				intent.setClass(getApplicationContext(), Twitter_createDM.class);
				String msg2 = msg.getSenderScreenName();

					Log.d("test2", msg2 + "");

				intent.putExtra("id", msg2);
				startActivity(intent);
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
				return false;
	        }
	    });



		//受信
		findViewById(R.id.bt_tweet).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reloadDM();
				list.setAdapter(tAdapter);
			}
		});

		//送信
		findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sentDM();
				list.setAdapter(tAdapter);
			}
		});

		//新規作成
		findViewById(R.id.bt_follower).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.setClass(getApplicationContext(), Twitter_createDM.class);
				intent.putExtra("id", "");
				startActivity(intent);
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		});
	}

	//送信
	private void sentDM(){
		AsyncTask<Void, Void, ResponseList<DirectMessage>> task = new AsyncTask<Void, Void, ResponseList<DirectMessage>>(){
			@Override
			protected ResponseList<DirectMessage> doInBackground(Void... params) {
					// TODO 自動生成されたメソッド・スタブ
					try {
						Paging paging = new Paging(1);
						return mTwitter.getDirectMessages(paging);
					} catch (TwitterException te) {
						te.printStackTrace();
					}
					return null;
				}


			@Override
			protected void onPostExecute(ResponseList<DirectMessage> result) {
				// TODO 自動生成されたメソッド・スタブ
				if(result != null){
					tAdapter.clear();
					for(DirectMessage status:result){
						tAdapter.add(status);
					}
				}else{
					showToast("DMの取得に失敗しました");
				}
			}
		};
		task.execute();
	}

	private void reloadDM(){
		AsyncTask<Void, Void, ResponseList<DirectMessage>> task = new AsyncTask<Void, Void, ResponseList<DirectMessage>>(){
			@Override
			protected ResponseList<DirectMessage> doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ

				try {
					Paging paging = new Paging(1);
					return mTwitter.getDirectMessages(paging);
				} catch (TwitterException te) {
					te.printStackTrace();
				}
			return null;
			}
			@Override
			protected void onPostExecute(ResponseList<DirectMessage> result) {
				// TODO 自動生成されたメソッド・スタブ
				if(result != null){
					tAdapter.clear();
					for(DirectMessage status:result){
						tAdapter.add(status);
					}
				}else{
					ApiLimit api = new ApiLimit(mTwitter);
					api.execute();
					Map<String,RateLimitStatus> map = null;
					try {
						map = api.get();
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
					RateLimitStatus DMlimit = map.get("/direct_messages/show");
					int i = DMlimit.getSecondsUntilReset();
					String m = i/60 + "分";
					String s = i % 60 + "秒";
					showToast("DMの取得に失敗しました\n" + DMlimit.getRemaining() + "回\nAPIリセットまで" + m + s);
				}
			}
		};
		task.execute();
	}



	private void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}


	//表示定義クラス
	public class TweetAdapter extends ArrayAdapter<DirectMessage> {

		private LayoutInflater mInflater;

		public TweetAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1);
			mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自動生成されたメソッド・スタブ

			if(convertView == null){
				convertView = mInflater.inflate(R.layout.list_item_tweet, null);
			}

			DirectMessage item = getItem(position);
			SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.icon);
			icon.setImageUrl(item.getSender().getProfileImageURL());
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(item.getSender().getName());
			TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
			screenName.setText("@" + item.getSender().getScreenName());
			TextView text = (TextView) convertView.findViewById(R.id.text);
			text.setText(item.getText());
			return convertView;
		}

	}//TweetAdapter

	private void getName(){

		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ
				try {
					return mTwitter.showUser(item.getSenderScreenName()).getName();
				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO 自動生成されたメソッド・スタブ
				name.setText(result);
			}

		};
		task.execute();

	}



}
