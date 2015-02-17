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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Twitter_Streaming extends Activity implements OnClickListener, OnItemClickListener, OnTouchListener {

	private Intent intent;
	private Context mContext;
	private Handler mHandler;
	private Twitter mTwitter;
	private TwitterStream mTStream;
	private TweetAdapter tAdapter;
	private int streamBtFlag;
	private GestureDetector mGestureDetector;
	private int flingFlag = 0;
	private static final int FLAG_TRUE = 1;
	private static final int FLAG_FALSE = 0;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private final static int STREAMSTART = 0;
	private final static int STREAMSTOP = 1;
	private final static int TWEET_DETAIL = 1000;

	private ListView list;
	private Button streamingBt;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.twitter_lists);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_streaming);
		View view = this.getLayoutInflater().inflate(R.layout.twitter_streaming_botans, null);
		RelativeLayout.LayoutParams lllp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		addContentView(view, lllp);

		list = (ListView) findViewById(R.id.twitter_lists_list);
		list.setOnTouchListener(this);
		streamingBt = (Button) findViewById(R.id.streaming_streamingbt);

		list.setOnItemClickListener(this);
		streamingBt.setOnClickListener(this);

		intent = getIntent();
		mContext = getApplicationContext();
		mGestureDetector = new GestureDetector(mContext,mOnGestureListener);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(flingFlag == FLAG_FALSE){
			Log.d("tweet_detail","view:" + parent + "  position:" + position + "  id:" + id);
			Status item = (Status) list.getItemAtPosition(position);
			intent.setClass(getApplicationContext(), Twitter_tweet_detail.class);
			intent.putExtra("TweetStatus", item);
			intent.putExtra("position", position);
			startActivityForResult(intent,TWEET_DETAIL);
			}
		flingFlag = FLAG_FALSE;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	//横スワイプ処理
	private final SimpleOnGestureListener mOnGestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
			flingFlag = FLAG_TRUE;
			try {

				if (Math.abs(event1.getY() - event2.getY()) > SWIPE_MAX_OFF_PATH) {
					// 縦の移動距離が大きすぎる場合は無視
					return false;
				}

				if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// 開始位置から終了位置の移動距離が指定値より大きい
					// X軸の移動速度が指定値より大きい
			//		intent.setClass(mContext, Twitter_Streaming.class);
			//		startActivity(intent);
			//		finish();
			//		overridePendingTransition(R.anim.right_in, R.anim.left_out);

				} else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// 終了位置から開始位置の移動距離が指定値より大きい
					// X軸の移動速度が指定値より大きい
			//		intent.setClass(mContext, TwitterSearch.class);
			//		startActivity(intent);
			//		overridePendingTransition(R.anim.left_in, R.anim.right_out);
					finish();
					overridePendingTransition(R.anim.left_in, R.anim.right_out);
				}

			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	};
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}
}
