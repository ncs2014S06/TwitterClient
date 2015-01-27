package ncs2014.s06.twitterclient;

import java.util.ArrayList;

import ncs2014.s06.twitterclient.TextLinker.OnLinkClickListener;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

public class Twitter_dm_detail extends Activity {
	private Twitter mTwitter;
	private Long TweetId;
	private Status item;
	private SparseArray<String> links;

	private SmartImageView tweet_detail_userIcon;
	private TextView tweet_detail_userName;
	private TextView tweet_detail_userId;
	private TextView tweet_detail_absoluteTime;
	private TextView tweet_detail_tweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_dm_detail);
		//findview
		tweet_detail_userIcon = (SmartImageView) findViewById(R.id.DM_MyImage);
		tweet_detail_userName = (TextView) findViewById(R.id.tweet_detail_username_dm);
		tweet_detail_userId = (TextView) findViewById(R.id.tweet_detail_userid_dm);
		//tweet_detail_absoluteTime = (TextView) findViewById(R.id.tweet_detail_absolutetime_dm);
		//tweet_detail_tweet = (TextView) findViewById(R.id.tweet_detail_tweet_dm);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}
		mTwitter = TwitterUtils.getTwitterInstance(this);
		Intent intent = getIntent();
		this.mTwitter = (Twitter) intent.getSerializableExtra("mTwitter");
		//this.TweetId = intent.getLongExtra("TweetId", 0);
		showTweet();
	}

	private void showTweet(){
		AsyncTask<Void, Void, Status> task = new AsyncTask<Void, Void, Status>() {

			@Override
			protected twitter4j.Status doInBackground(Void... params) {
				try {
					item = mTwitter.showStatus(TweetId);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				return item;
			}

			@Override
			protected void onPostExecute(twitter4j.Status result) {
				tweet_detail_userIcon.setImageUrl(item.getUser().getProfileImageURL());
				tweet_detail_userName.setText(item.getUser().getName());
				tweet_detail_userId.setText("@"+ item.getUser().getScreenName());
				//tweet_detail_absoluteTime.setText(item.getCreatedAt().toString());
				String tweet = item.getText();


				//リンク検索
				ArrayList<String> LinkArray = new ArrayList<String>();
				int end = 0;
				while(tweet.indexOf("http",end) != -1){
					if(tweet.indexOf("http") != -1){
						int start = tweet.indexOf("http",end);
						if(tweet.indexOf("\n",start) != -1){
							end = tweet.indexOf("\n",start) - 1;
						}else if(tweet.indexOf(" ",start) != -1){
							end = tweet.indexOf(" ",start);
						}else{
							end = tweet.length();
						}
						String tweetLink = tweet.substring(start,end);
						LinkArray.add(tweetLink);
					}
				}
				links = new SparseArray<String>();
				int i = 0;
				for(String s:LinkArray){
					links.append(i, s);
					Log.d("tweet_detail",s + ":");
					i++;
				}
				OnLinkClickListener listener = new OnLinkClickListener() {
					@Override
					public void onLinkClick(int textId) {
						Uri uri = Uri.parse(links.get(textId));
						Log.d("tweet_detail","links:" + links.get(textId));
						Intent i = new Intent(Intent.ACTION_VIEW,uri);
						startActivity(i);
					}
				};




				Log.d("tweet_detail","--------------------------------");
				tweet_detail_tweet.setText(TextLinker.getLinkableText(tweet, links, listener));
				tweet_detail_tweet.setMovementMethod(LinkMovementMethod.getInstance());
			}

		};
		task.execute();

	}

}
