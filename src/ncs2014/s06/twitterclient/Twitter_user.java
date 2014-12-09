package ncs2014.s06.twitterclient;

import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.image.SmartImageView;

public class Twitter_user extends Activity implements OnClickListener {

	private SmartImageView view;
	private Twitter mTwitter;
	private Button myTweet;
	private Button follow;
	private Button follower;
	private Button fav;
	private TweetAdapter tAdapter;
	private ListView list;
	private PagableResponseList<twitter4j.User> rawData;
	private long cursor = -1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		rawData = null;
		ImageGet ig = new ImageGet(mTwitter);
		Log.d("test","test");
		setContentView(R.layout.twitter_user_status);
		view = (SmartImageView) findViewById(R.id.user_image);
		ig.setImage(view);


		tAdapter = new TweetAdapter(this);
		myTweet = (Button) findViewById(R.id.bt_tweet);
		follow = (Button) findViewById(R.id.bt_follow);
		follower = (Button) findViewById(R.id.bt_follower);
		fav = (Button) findViewById(R.id.bt_favorite);

		myTweet.setOnClickListener(this);
		follow.setOnClickListener(this);
		follower.setOnClickListener(this);
		fav.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == myTweet){
			setContentView(R.layout.twitter_mytweet);
			list = (ListView) findViewById(R.id.tllist);
			tweetGet();
			list.setAdapter(tAdapter);
		}

		if(v == follow){
			setContentView(R.layout.twitter_follow);
			list = (ListView) findViewById(R.id.tllist);
			followGet();
			list.setAdapter(tAdapter);
		}

		if(v == follower){
			followerGet();
		}

		if(v == fav){
			favGet();
		}
	}

	private void tweetGet() {
		AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
		@Override
		protected List<twitter4j.Status> doInBackground(Void... params) {
			try {
				return mTwitter.getUserTimeline(mTwitter.getId());
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
			}
		}
	};
	task.execute();

    }

	public void followGet(){
		AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
			@Override
			protected List<twitter4j.Status> doInBackground(Void... params) {
				try {
					rawData = mTwitter.getFriendsList(mTwitter.getScreenName(),cursor);

					//ツイート数カウント用変数
					int i = mTwitter.showUser(mTwitter.getId()).getStatusesCount();

					return mTwitter.getUserTimeline(mTwitter.getId());
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
				}
			}
		};
		task.execute();

	}

	public void followerGet(){
	}

	public void favGet(){
	}

}