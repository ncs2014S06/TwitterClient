package ncs2014.s06.twitterclient;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_Client_DM extends FragmentActivity implements OnScrollListener,OnItemClickListener, OnClickListener{

	private Twitter mTwitter;
	private TweetAdapter tAdapter;
	private User myUser;
	private Paging paging;
	private ResponseList<DirectMessage> messages;
	private DirectMessage selectDirectMessage;
	private Intent intent;
	private int mode = 0;
	public static final int get = 1;		//送信リスト
	public static final int send = 2;		//受信リスト
	private final static int reply = 0; //返信
	private final static int newMail = 1; //新規

	private ListView list;
	private Button dm_receive_button;
	private Button dm_send_button;
	private Button dm_new_button;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		myUser = (User) intent.getSerializableExtra("myUser");
		Log.d("test",myUser.toString());
		setContentView(R.layout.twitter_dmlist);
		//findViewById
		list = (ListView) findViewById(R.id.dmlist);
		dm_receive_button = (Button) findViewById(R.id.dm_receive_button);
		dm_send_button = (Button) findViewById(R.id.dm_send_button);
		dm_new_button = (Button) findViewById(R.id.dm_new_button);
		//リスナー
		dm_new_button.setOnClickListener(this);
		dm_receive_button.setOnClickListener(this);
		dm_send_button.setOnClickListener(this);
		list.setOnItemClickListener(this);


		//認証確認
		if (!TwitterUtils.hasAccessToken(this)) {
			intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}else{
			paging = new Paging(1);
			tAdapter = new TweetAdapter(this);
			list.setAdapter(tAdapter);
			mTwitter = TwitterUtils.getTwitterInstance(this);
			list.setOnScrollListener(this);
		}
	}

	//受信
	private void reloadDM(){
		AsyncTask<Void, Void, ResponseList<DirectMessage>> task = new AsyncTask<Void, Void, ResponseList<DirectMessage>>(){
			@Override
			protected ResponseList<DirectMessage> doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ
				try {
					messages = mTwitter.getDirectMessages(paging);
					Log.d("client_dm",messages.size() + " 受信");
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

	//送信
	private void sentDM(){
		AsyncTask<Void, Void, ResponseList<DirectMessage>> task = new AsyncTask<Void, Void, ResponseList<DirectMessage>>(){
			@Override
			protected ResponseList<DirectMessage> doInBackground(Void... params) {
					try {
						messages = mTwitter.getSentDirectMessages(paging);
						paging.setPage(paging.getPage() + 1);
						Log.d("client_dm",list.getCount()+" 送信" );
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
			User senderUser = item.getSender();

			SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.icon);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
			TextView text = (TextView) convertView.findViewById(R.id.text);

			icon.setImageUrl(senderUser.getProfileImageURL());
			name.setText(senderUser.getName());
			screenName.setText("@" + senderUser.getScreenName());
			text.setText(item.getText());
			return convertView;
		}

	}//TweetAdapter

	public void onScroll(AbsListView view, int iTop,int iVisible, int iTotal) {
		boolean bLast = iTotal == iTop + iVisible;
		if(bLast){
			if(list.getCount() != 0){
				if(messages.size() != 0 && mode == 1){
					Log.d("client_dm",list.getCount()+" "+messages.size() +" 受信" );
					reloadDM();
				}else if(messages.size() != 0 && mode == 2){
					Log.d("client_dm",list.getCount()+" "+messages.size() +" 送信" );
					sentDM();
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Log.d("dm_detail","view:" + parent + "  position:" + position + "  id:" + id);
		DirectMessage selectDirectMessage = (DirectMessage)list.getItemAtPosition(position);

		intent.setClass(getApplicationContext(), Twitter_createDM.class);
		intent.putExtra("selectDirectMessage", selectDirectMessage);
		intent.putExtra("mode", reply);
		Log.d("client_dm",selectDirectMessage.getId() + "");
		startActivity(intent);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);

		return;
	}

	@Override
	public void onClick(View v) {
		//受信
		if(v == dm_receive_button){
			mode = get;
			paging.setPage(1);
			tAdapter.clear();
			reloadDM();
			list.setAdapter(tAdapter);
		}
		//送信
		if(v == dm_send_button){
			mode = send;
			paging.setPage(1);
			tAdapter.clear();
			sentDM();
			list.setAdapter(tAdapter);
		}
		//新規作成
		if(v == dm_new_button){
			intent.setClass(getApplicationContext(), Twitter_createDM.class);
			intent.putExtra("mode", newMail);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}
	}
}
