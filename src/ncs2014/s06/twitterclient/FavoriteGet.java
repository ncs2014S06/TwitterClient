package ncs2014.s06.twitterclient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavoriteGet extends Activity implements OnScrollListener{

	private Twitter mTwitter;
	private TweetAdapter tAdapter;
	private ListView list;
	private AsyncTask<Void, Void, List<twitter4j.Status>> task;
	private Paging paging;
	private User user;
	private String userScrean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		Intent intent = new Intent();
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.twitter_home);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_default);
		TextView titleName = (TextView) findViewById(R.id.title_default_title);
		titleName.setText("お気に入り");
		user = (User) intent.getSerializableExtra("otherUser");

		userScrean = user.getScreenName();



		paging = new Paging(1);
		tAdapter = new TweetAdapter(this);
		list = (ListView) findViewById(R.id.tllist);
		favoriteGet(paging,0);
		list.setAdapter(tAdapter);
		list.setOnScrollListener(this);

	}

	public void favoriteGet(final Paging paging,final int mode) {
		task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
		@Override
		protected List<twitter4j.Status> doInBackground(Void... params) {
			try {

				if(mode == 1){
					paging.setPage(paging.getPage() + 1);
				}

				return mTwitter.getFavorites(userScrean);

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
					favoriteGet(paging,1);
				}
			}
		}
	}
}

