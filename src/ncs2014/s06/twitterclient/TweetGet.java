package ncs2014.s06.twitterclient;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class TweetGet extends Activity{

	private Twitter mTwitter;
	private TweetAdapter tAdapter;
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_mytweet);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		tAdapter = new TweetAdapter(this);
		list = (ListView) findViewById(R.id.tllist);
		tweetGet(mTwitter,tAdapter);
		list.setAdapter(tAdapter);


	}




	public void tweetGet(final Twitter mTwitte,final TweetAdapter tAdapte) {

		AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
		@Override
		protected List<twitter4j.Status> doInBackground(Void... params) {
			try {
				return mTwitte.getUserTimeline(mTwitte.getId());
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<twitter4j.Status> result) {
			if (result != null) {
				tAdapte.clear();
				for (twitter4j.Status status : result) {
					tAdapte.add(status);
				}
			}
		}
	};
	task.execute();

    }
}

