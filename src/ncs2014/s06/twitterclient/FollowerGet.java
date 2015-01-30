package ncs2014.s06.twitterclient;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import twitter4j.Friendship;
import twitter4j.PagableResponseList;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class FollowerGet extends Activity implements OnScrollListener,OnItemClickListener, OnClickListener{

	private Twitter mTwitter;
	private User otherUserId;
	private ListView list;
	private Status followStatus;
	private ArrayAdapter<User> uAdapter;
	private ArrayList<User> arrayList;
	private PagableResponseList<User> user;
	private AsyncTask<Void, Void, ArrayList<User>> taskFollow;
	private AsyncTask<Void, Void, Twitter> taskFollowButton;
	private AsyncTask<Void, Void, ResponseList<Friendship>> lookTask;
	private Intent intent;
	private boolean followFlag;
	private Button follow;
	private ResponseList<Friendship> aa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_follow);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		intent = getIntent();
		this.mTwitter = (Twitter) intent.getSerializableExtra("TwitterUser");
		this.otherUserId = (User) intent.getSerializableExtra("otherUserId");
		uAdapter = new userAdapter(this);
		list = (ListView) findViewById(R.id.tllist);
		intent = getIntent();
		followGet(0);
		list.setAdapter(uAdapter);
		list.setOnItemClickListener(this);
		list.setOnScrollListener(this);
	}

	/**
	 *
	 * @param mode 0 新規:1 追加
	 */
	public void followGet(final int mode){
		arrayList = new ArrayList<User>();
		taskFollow = new AsyncTask<Void, Void, ArrayList<User>>(){

			@Override
			protected ArrayList<User> doInBackground(Void... params) {
				long i = -1;

				try {
					if(mode == 0)
						if(otherUserId == null){
							user = mTwitter.getFollowersList(mTwitter.getId(), i);
						}else{
							user = mTwitter.getFollowersList(otherUserId.getId(),i);
					}

				//	do{
						for(User u :user){
							arrayList.add(u);
						}
						if(otherUserId == null){
							user = mTwitter.getFollowersList(mTwitter.getId(), user.getNextCursor());
						}else{
							user = mTwitter.getFollowersList(otherUserId.getId(), user.getNextCursor());
						}
				} catch (IllegalStateException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

				return arrayList;
			}

			@Override
			protected void onPostExecute(ArrayList<User> result) {
				// TODO 自動生成されたメソッド・スタブ
				super.onPostExecute(result);
				if(mode == 0){
					uAdapter.clear();
				}
				for(User u:result){
					uAdapter.add(u);
				}


				//API取得
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
				RateLimitStatus TLlimit = map.get("/followers/list");
				int i = TLlimit.getSecondsUntilReset();
				String m = i / 60 + "分";
				String S = i % 60 + "秒";
				showToast("残り読み込み回数" + TLlimit.getRemaining() + "回\nAPIリセットまで" + m + S);
			}
		};
		taskFollow.execute();
	}

	public boolean taskRunning(){
		if(taskFollow.getStatus().name().equals("RUNNING")){
			return true;
		}
		return false;
	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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
			//text.setText(item.get());
			return convertView;
		}
	}//TweetAdapter

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
				if(!taskRunning()){
					followGet(1);
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		User otherUser = (User) list.getItemAtPosition(position);
		intent = new Intent(getApplication(),Twitter_user.class);
		intent.putExtra("otherUser",otherUser);
		startActivity(intent);
	}

	//フォローボタン
	@Override
	public void onClick(View v) {
		if(v == follow){
			taskFollowButton = new AsyncTask<Void, Void, Twitter>(){
				@Override
				protected Twitter doInBackground(Void... params) {
					// TODO 自動生成されたメソッド・スタブ
					return null;
				}
			};
		}
	}
}
