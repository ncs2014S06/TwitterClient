package ncs2014.s06.twitterclient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

public class TimeLine {

	private Twitter mTwitter;
	private TweetAdapter tAdapter;
	private SwipeRefreshLayout swipeRefreshLayout;
	private Context context;
	private Paging paging;

	public TimeLine(Context context,Twitter mTwitter,TweetAdapter tAdapter,SwipeRefreshLayout swipeRefreshLayout) {
		this.context = context;
		this.mTwitter = mTwitter;
		this.tAdapter = tAdapter;
		this.swipeRefreshLayout = swipeRefreshLayout;
	}

	public synchronized void reloadTimeLine() {
		AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
			@Override
			protected List<twitter4j.Status> doInBackground(Void... params) {
				try {
					Log.d("scroll","TL呼び出し！");
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
					showToast("タイムラインの取得に成功しました");

					//la.getListView().setSelection(0);
				} else {
					showToast("タイムラインの取得に失敗しました");
				}
				swipeRefreshLayout.setRefreshing(false);
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
				RateLimitStatus TLlimit = map.get("/statuses/home_timeline");
				int i = TLlimit.getSecondsUntilReset();
				String m = i / 60 + "分";
				String S = i % 60 + "秒";
				showToast("残り読み込み回数" + TLlimit.getRemaining() + "回\nAPIリセットまで" + m + S);
			}
		};
		task.execute();
	}

	/**
	 *
	 * @param paging1
	 * @param mode 新規 0 追加 1
	 */
	public synchronized void reloadTimeLine(Paging paging1,final int mode) {
		this.paging = paging1;
		if(mode == 1){
			paging.setPage(paging.getPage() + 1);
		}
		AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
			@Override
			protected List<twitter4j.Status> doInBackground(Void... params) {
				try {
					if(mode == 1){
						Log.d("scroll","addTL呼び出し！");
						return mTwitter.getHomeTimeline(paging);
					}
					Log.d("scroll","TL呼び出し！");
					return mTwitter.getHomeTimeline();

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
					showToast("タイムラインの取得に成功しました");

					//la.getListView().setSelection(0);
				} else {
					showToast("タイムラインの取得に失敗しました");
				}
				swipeRefreshLayout.setRefreshing(false);
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
				RateLimitStatus TLlimit = map.get("/statuses/home_timeline");
				int i = TLlimit.getSecondsUntilReset();
				String m = i / 60 + "分";
				String S = i % 60 + "秒";
				showToast("残り読み込み回数" + TLlimit.getRemaining() + "回\nAPIリセットまで" + m + S);
			}
		};
		task.execute();
	}

	private void showToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}


}
