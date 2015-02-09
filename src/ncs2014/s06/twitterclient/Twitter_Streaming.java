package ncs2014.s06.twitterclient;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Twitter_Streaming extends Activity implements OnClickListener {

	private Context mContext;
	private Handler mHandler;
	private Twitter mTwitter;
	private TwitterStream mTStream;
	private TweetAdapter tAdapter;
	private int streamBtFlag;
	private final static int STREAMSTART = 0;
	private final static int STREAMSTOP = 1;

	private ListView list;
	private Button streamingBt;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_lists);
		View view = this.getLayoutInflater().inflate(R.layout.twitter_streaming_botans, null);
		RelativeLayout.LayoutParams lllp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		addContentView(view, lllp);

		list = (ListView) findViewById(R.id.twitter_lists_list);
		streamingBt = (Button) findViewById(R.id.streaming_streamingbt);

		streamingBt.setOnClickListener(this);

		mContext = getApplicationContext();
		mHandler = new Handler();
		AccessToken at = TwitterUtils.loadAccessToken(this);
		Configuration configuration = new ConfigurationBuilder().setOAuthConsumerKey(getString(R.string.twitter_consumer_key))
				.setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret))
				.setOAuthAccessToken(at.getToken())
				.setOAuthAccessTokenSecret(at.getTokenSecret()).build();
		mTwitter = TwitterUtils.getTwitterInstance(this);
		mTStream = new TwitterStreamFactory(configuration).getInstance();

		mTStream.addListener(new Listener(mContext,list));
		streamBtFlag = STREAMSTOP;

	}

	private class Listener extends UserStreamAdapter{

		private ListView list;

		public Listener(Context mContext,ListView list) {
			this.list = list;
			tAdapter = new TweetAdapter(mContext);
			this.list.setAdapter(tAdapter);
		}

		@Override
		public void onStatus(final Status status) {
			Log.d("test",status.getText());
			mHandler.post(new Runnable(){
				@Override
				public void run() {
					tAdapter.insert(status,0);
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		if(streamBtFlag == STREAMSTOP){
			mTStream.user();
			showToast("ストリーミング開始");
			streamBtFlag = STREAMSTART;
			streamingBt.setText("ストリーミング停止");
		}else	if(streamBtFlag == STREAMSTART){
			mTStream.cleanUp();
			showToast("ストリーミング停止");
			streamBtFlag = STREAMSTOP;
			streamingBt.setText("ストリーミング開始");
		}
	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
