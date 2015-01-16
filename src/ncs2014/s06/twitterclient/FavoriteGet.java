package ncs2014.s06.twitterclient;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class FavoriteGet extends Activity implements OnScrollListener{

	private Twitter mTwitter;
	private ListView list;
	private ArrayAdapter<User> uAdapter;
	private ArrayList<User> arrayList;
	private ResponseList<Status> user;
	private AsyncTask<Void, Void, ResponseList<Status>> task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_follow);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		uAdapter = new userAdapter(this);
		list = (ListView) findViewById(R.id.tllist);
		//list.setOnScrollListener(this);
		favoriteGet(0);
		list.setAdapter(uAdapter);
		list.setOnScrollListener(this);


	}

	/**
	 *
	 * @param mode 0 新規:1 追加
	 */
	public void favoriteGet(final int mode){
		arrayList = new ArrayList<User>();
		task = new AsyncTask<Void, Void, ResponseList<twitter4j.Status>>(){

			@Override
			protected ResponseList<twitter4j.Status> doInBackground(Void... params) {

						try {
							user = mTwitter.getFavorites();
						} catch (TwitterException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}

				return user;
			}

			@Override
			protected void onPostExecute(ResponseList<twitter4j.Status> result) {
				// TODO 自動生成されたメソッド・スタブ
				super.onPostExecute(result);
				if (result != null) {
					if(mode == 0){
					uAdapter.clear();
					}
					for (twitter4j.Status u: result) {
						uAdapter.add(result);
					}
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
		task.execute();
	}

	public boolean taskRunning(){
		if(task.getStatus().name().equals("RUNNING")){
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
		//	text.setText(item.getText());
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
					Log.d("scroll","Folloeget最後尾だよ");
				}
			}
		}
	}
}
