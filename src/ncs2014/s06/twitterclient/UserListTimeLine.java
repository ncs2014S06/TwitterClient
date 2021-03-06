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
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

public class UserListTimeLine{

	private Twitter mTwitter;
	private TweetAdapter tAdapter;
	private SwipeRefreshLayout swipeRefreshLayout;
	private Context mContext;
	private Handler mHandler;
	private Paging paging;
	private Long listId;
	private AsyncTask<Void, Void, List<twitter4j.Status>> task;

	public UserListTimeLine(Context mContext,Handler mHandler, TweetAdapter tAdapter,SwipeRefreshLayout swipeRefreshLayout,Long listId) {
		this.mContext = mContext;
		this.mHandler = mHandler;
		this.mTwitter = TwitterUtils.getTwitterInstance(mContext);
		this.tAdapter = tAdapter;
		this.swipeRefreshLayout = swipeRefreshLayout;
		this.listId = listId;
	}
	/**
	 *
	 * @param paging1
	 * @param mode 新規 0 追加 1
	 */
	public synchronized void reloadUserList( Paging paging1,final int mode){
		this.paging = paging1;
		if(mode == 1){
			paging.setPage(paging.getPage() + 1);
		}
		task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
			@Override
			protected List<twitter4j.Status> doInBackground(Void... params){
				try {
					if(mode == 1){
						Log.d("scroll","addTL呼び出し！");
						return mTwitter.getUserListStatuses(listId,paging);
					}
					paging = new Paging(1);
					Log.d("scroll","TL呼び出し！");
					return mTwitter.getUserListStatuses(listId,paging);
				} catch (TwitterException e) {
					e.printStackTrace();
					final Message msg = new Message();
					msg.obj = e;
					mHandler.sendMessage(msg);
					cancel(true);
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<twitter4j.Status> result) {
				if (result != null) {
					if(mode == 0){
						tAdapter.clear();
					}
					int i = 0;
					for (twitter4j.Status status : result) {
						tAdapter.add(status);
					}
					showToast("リストの取得に成功しました");

					//la.getListView().setSelection(0);
				} else {
					showToast("リストの取得に失敗しました");
				}
				swipeRefreshLayout.setRefreshing(false);
				ApiLimit api = new ApiLimit(mTwitter);
				api.execute();
				Map<String, RateLimitStatus> map = null;
				try {
					map = api.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				RateLimitStatus TLlimit = map.get("/lists/statuses");
				int i = TLlimit.getSecondsUntilReset();
				String m = i / 60 + "分";
				String S = i % 60 + "秒";
				showToast("残り読み込み回数" + TLlimit.getRemaining() + "回\nAPIリセットまで" + m + S);
			}
		};
		Log.d("task","name " + task.getStatus().name());
		task.execute();
	}

	private void showToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	public boolean taskRunning(){
		if(task.getStatus().name().equals("RUNNING")){
			return true;
		}
		return false;
	}
}
