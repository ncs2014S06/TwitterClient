package ncs2014.s06.twitterclient;

import twitter4j.DirectMessage;
import twitter4j.Paging;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_Client_DM extends FragmentActivity implements OnScrollListener,OnItemClickListener{

	private Twitter mTwitter;
	private TweetAdapter tAdapter;
	private ImageGet ig;
	private ListView list;
	private DirectMessage item;
	private TextView name;
	private Paging paging;
	private ResponseList<DirectMessage> messages;
	private ResponseList<DirectMessage> getmessages;
	private ResponseList<DirectMessage> sendmessages;
	Intent intent = new Intent();
	private int mode = 0;
	public static final int get = 1;		//送信リスト
    public static final int send = 2;		//受信リスト
    public static final int sendget = 3;	//送受信





	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_dmlist);

		list = (ListView) findViewById(R.id.dmlist);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}else{
            paging = new Paging(1);
			tAdapter = new TweetAdapter(this);
			list.setAdapter(tAdapter);
			mTwitter = TwitterUtils.getTwitterInstance(this);
			ig = new ImageGet(mTwitter);
			list.setOnScrollListener(this);
			list.setOnItemClickListener(this);
		}


	/*	list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

	    }*/;



		//受信
		findViewById(R.id.bt_tweet).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mode = get;
				paging.setPage(1);
				tAdapter.clear();
				reloadDM();
				list.setAdapter(tAdapter);
			}
		});

		//送信
		findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mode = send;
				paging.setPage(1);
				tAdapter.clear();
				sentDM();
				list.setAdapter(tAdapter);
			}
		});

		//送受信
		findViewById(R.id.sendget).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mode = sendget;
				paging.setPage(1);
				tAdapter.clear();
				sendgetDM();
				list.setAdapter(tAdapter);
			}
		});

		//新規作成
		findViewById(R.id.bt_follower).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.setClass(getApplicationContext(), Twitter_createDM2.class);
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

						messages = mTwitter.getSentDirectMessages(paging);;
			            paging.setPage(paging.getPage() + 1);
			            Log.d("scroll222",list.getCount()+" 送信" );
						return messages;
					} catch (TwitterException te) {
						te.printStackTrace();
					}
					return null;
				}


			@Override
			protected void onPostExecute(ResponseList<DirectMessage> result) {
				// TODO 自動生成されたメソッド・スタブ
				if(result != null){
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

	//受信
	private void reloadDM(){
		AsyncTask<Void, Void, ResponseList<DirectMessage>> task = new AsyncTask<Void, Void, ResponseList<DirectMessage>>(){
			@Override
			protected ResponseList<DirectMessage> doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ

				//tAdapter.clear();
				try {

					messages = mTwitter.getDirectMessages(paging);
					Log.d("size",messages.size() + " 受信");
					paging.setPage(paging.getPage() + 1);
					return messages;

				} catch (TwitterException te) {
					te.printStackTrace();
				}
			return null;
			}

			@Override
			protected void onPostExecute(ResponseList<DirectMessage> result) {
				// TODO 自動生成されたメソッド・スタブ
				if(result != null){
					for(DirectMessage status:result){
						tAdapter.add(status);
					}
				}
			}
		};
		task.execute();


	}



	//送受信
	private void sendgetDM(){
		AsyncTask<Void, Void, ResponseList<DirectMessage>> task = new AsyncTask<Void, Void, ResponseList<DirectMessage>>(){
			@Override
			protected ResponseList<DirectMessage> doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ

				//tAdapter.clear();
				try {

					getmessages = mTwitter.getDirectMessages(paging);
					sendmessages = mTwitter.getSentDirectMessages(paging);
					paging.setPage(paging.getPage() + 1);
					return messages;

				} catch (TwitterException te) {
					te.printStackTrace();
				}
			return null;
			}

			@Override
			protected void onPostExecute(ResponseList<DirectMessage> result) {
				// TODO 自動生成されたメソッド・スタブ
				if(result != null){
					for(DirectMessage status:getmessages){
						tAdapter.add(status);
						for(DirectMessage status1:sendmessages){
							tAdapter.add(status1);
					}

					}
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

	/*
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
	*/

	public void onScroll(AbsListView view, int iTop,
			int iVisible, int iTotal) {
		boolean bLast = iTotal == iTop + iVisible;
		int i = 0;
		if(bLast){
			if(list.getCount() != 0){
				if(messages.size() != 0 && mode == 1){
					Log.d("scroll222",list.getCount()+" "+messages.size() +" 受信" );
					reloadDM();
					list.setAdapter(tAdapter);

				}else if(messages.size() != 0 && mode == 2){
					Log.d("scroll222",list.getCount()+" "+messages.size() +" 送信" );
					sentDM();
					list.setAdapter(tAdapter);
				}
				}
			}
		}
		// TODO 自動生成されたメソッド・スタブ

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Log.d("dm_detail","view:" + parent + "  position:" + position + "  id:" + id);
		DirectMessage msg = (DirectMessage)list.getItemAtPosition(position);

		intent.setClass(getApplicationContext(), Twitter_createDM.class);
		String msg2 = msg.getSenderScreenName();
		String msg3 = msg.getText();
		String msg4 = msg.getSender().getProfileImageURL();

		Log.d("test2", msg2 + "");

		intent.putExtra("mTwitter", mTwitter);
		intent.putExtra("msg", msg);
		//intent.putExtra("TweetId", msg.getId());
		intent.putExtra("id", msg2);
		intent.putExtra("text", msg3);
		intent.putExtra("img",msg4);

		startActivity(intent);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);

		return;
	}
}




