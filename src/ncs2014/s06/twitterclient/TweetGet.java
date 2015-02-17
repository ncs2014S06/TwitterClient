package ncs2014.s06.twitterclient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TweetGet extends Activity implements OnScrollListener, OnItemClickListener{

	private Twitter mTwitter;
	private User otherUserId;
	private TweetAdapter tAdapter;
	private ListView list;
	private AsyncTask<Void, Void, List<twitter4j.Status>> task;
	private Paging paging;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.twitter_home);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_default);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		intent = getIntent();
		this.mTwitter = (Twitter) intent.getSerializableExtra("TwitterUser");
		this.otherUserId = (User) intent.getSerializableExtra("otherUserId");
		paging = new Paging(1);
		tAdapter = new TweetAdapter(this);
		list = (ListView) findViewById(R.id.tllist);
		tweetGet(paging,0);
		list.setAdapter(tAdapter);
		list.setOnItemClickListener(this);
		list.setOnScrollListener(this);
	}

	public void tweetGet(final Paging paging,final int mode) {
		task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
		@Override
		protected List<twitter4j.Status> doInBackground(Void... params) {
			try {
				if(mode == 1){
					paging.setPage(paging.getPage() + 1);
				}
				if(otherUserId == null){
					return mTwitter.getUserTimeline(mTwitter.getId(),paging);
				}else{
					return mTwitter.getUserTimeline(otherUserId.getId(),paging);
				}

			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<twitter4j.Status> result) {
			if (result != null) {
				if(mode == 0){
					tAdapter.clear();
				}
				for (twitter4j.Status status : result) {
					tAdapter.add(status);
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
			RateLimitStatus TLlimit = map.get("/statuses/user_timeline");
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

	public boolean taskRunning(){
		if(task.getStatus().name().equals("RUNNING")){
			return true;
		}
		return false;
	}


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
					tweetGet(paging,1);
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Status item = (Status) list.getItemAtPosition(position);
		Intent intent = new Intent(getApplication(),Twitter_tweet_detail.class);
		//intent.putExtra("mTwitter", mTwitter);
		//intent.putExtra("TweetId", item);
		intent.putExtra("TweetStatus", item);
		intent.putExtra("position", position);
		startActivity(intent);

	}
}

