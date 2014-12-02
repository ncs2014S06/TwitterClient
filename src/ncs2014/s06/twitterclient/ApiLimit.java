package ncs2014.s06.twitterclient;

import java.util.Map;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.os.AsyncTask;

public class ApiLimit extends AsyncTask<Void, Void, Map<String, RateLimitStatus>>{
	private Twitter mTwitter;
	private Map<String, RateLimitStatus> ratelimit;

	public ApiLimit(Twitter mTwitter) {
		this.mTwitter = mTwitter;
	}
	@Override
	protected Map<String, RateLimitStatus> doInBackground(Void... params) {
		try {
			return mTwitter.getRateLimitStatus();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return null;
	}


	/*
	public void getApi(){
		ratelimit = null;
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Log.d("api", "ratelimitにセット");
					ratelimit = mTwitter.getRateLimitStatus();
					if(ratelimit != null){
						for(String key:ratelimit.keySet()){
							Log.d("api",key + " : " + ratelimit.get(key));
						}
					}else{
						Log.d("api", "ratelimit = null");
					}
				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				return null;
			}
		};
		task.execute();
	}

	public void getTLApi() {
		ratelimit = null;
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				String result = null;
				try {
					ratelimit = mTwitter.getRateLimitStatus();

					if(ratelimit != null){
						RateLimitStatus TLlimit = ratelimit.get("/statuses/home_timeline");
						result = "残り読み込み回数" + TLlimit.getRemaining() + "回";
						Log.d("api",result);
						for(String key:ratelimit.keySet()){
							Log.d("api",key + " : " + ratelimit.get(key));
						}
					}else{
						Log.d("api", "ratelimit = null");
					}
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				return result;
			}
		};
		task.execute();
	}

*/

}
