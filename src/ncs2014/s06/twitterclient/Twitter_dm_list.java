package ncs2014.s06.twitterclient;

import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_dm_list extends FragmentActivity implements OnScrollListener,OnItemClickListener, OnClickListener{

	private Twitter mTwitter;
	private userAdapter uAdapter;
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
	private ArrayList<User> arrayUser;

	private ListView list;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		myUser = (User) intent.getSerializableExtra("myUser");
		Log.d("test",myUser.toString());
		setContentView(R.layout.twitter_lists);
		//findViewById
		list = (ListView) findViewById(R.id.twitter_lists_list);
		//リスナー
		list.setOnItemClickListener(this);



		//認証確認
		if (!TwitterUtils.hasAccessToken(this)) {
			intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}else{
			paging = new Paging(1);
			uAdapter = new userAdapter(this);
			mTwitter = TwitterUtils.getTwitterInstance(this);
			list.setOnScrollListener(this);
			reloadDM();
			list.setAdapter(uAdapter);



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
				arrayUser = new ArrayList<User>();
				if(result != null){
					for(DirectMessage status:result){
						User sender = status.getSender();
						Log.d("dmList","a " + sender.getName());
						if(arrayUser.indexOf(sender) == -1){
							arrayUser.add(sender);
						}
					}
					for(User u:arrayUser){
						Log.d("dmList",u.getName());
						uAdapter.add(u);
					}
				}
			}
		};
		task.execute();
	}


	private void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}


	public void onScroll(AbsListView view, int iTop,int iVisible, int iTotal) {
		boolean bLast = iTotal == iTop + iVisible;
		if(bLast){
			if(list.getCount() != 0){
				if(messages.size() != 0 && mode == 1){
					Log.d("client_dm",list.getCount()+" "+messages.size() +" 受信" );
					reloadDM();
				}else if(messages.size() != 0 && mode == 2){
					Log.d("client_dm",list.getCount()+" "+messages.size() +" 送信" );
					//sentDM();
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

		String str = uAdapter.getContext().toString();
		Log.d("test List",str);

		//DirectMessage selectDirectMessage = (DirectMessage)list.getItemAtPosition(position);
/*
		intent.setClass(getApplicationContext(), Twitter_createDM.class);
		intent.putExtra("selectDirectMessage", selectDirectMessage);
		intent.putExtra("mode", reply);
		Log.d("client_dm",selectDirectMessage.getId() + "");
		startActivity(intent);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
*/
		return;
	}



	//表示定義クラス
	public class userAdapter extends ArrayAdapter<User> {

		private LayoutInflater mInflater;

		public userAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1);
			mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自動生成されたメソッド・スタブ

			if(convertView == null){
				convertView = mInflater.inflate(R.layout.list_item_user, null);
			}

			User item = getItem(position);
			SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.icon);
			icon.setImageUrl(item.getProfileImageURL());
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(item.getName());
			TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
			screenName.setText("@"+ item.getScreenName());
			TextView text = (TextView) convertView.findViewById(R.id.text);
			text.setText("本文");
			return convertView;
		}

	}//TweetAdapter



	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
