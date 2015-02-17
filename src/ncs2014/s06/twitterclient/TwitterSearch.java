package ncs2014.s06.twitterclient;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class TwitterSearch extends Activity implements OnClickListener,OnScrollListener,OnItemClickListener, OnTouchListener{

	private Twitter mTwitter;
	private Context mContext;
	private TweetAdapter tAdapter;
	private Paging paging;
	private User myUser;
	private QueryResult queryResult = null;
	private Query query;
	private String searchWord;
	private List<twitter4j.Status> arrayStatus ;
	private Intent intent = new Intent();
	private GestureDetector mGestureDetector;
	private int flingFlag = 0;
	private static final int FLAG_TRUE = 1;
	private static final int FLAG_FALSE = 0;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private final static int TWEET_DETAIL = 1000;


	private EditText searchText;
	private Button searchButton;
	private ListView searchList;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		mTwitter = TwitterUtils.getTwitterInstance(this);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.twitter_search);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_search);
		searchText = (EditText)findViewById(R.id.searchtext);
		searchButton = (Button)findViewById(R.id.searchbutton);
		searchList = (ListView)findViewById(R.id.search_list);
		searchButton.setOnClickListener(this);
		searchList.setOnTouchListener(this);

		mContext = getApplicationContext();
		mGestureDetector = new GestureDetector(mContext,mOnGestureListener);

		tAdapter = new TweetAdapter(this);
		searchList.setAdapter(tAdapter);
		searchList.setOnScrollListener(this);
		paging = new Paging(1);
		query = new Query( );
		searchList.setOnItemClickListener(this);


    }


	private void search(){

	    AsyncTask<Void, Void, QueryResult> task = new AsyncTask<Void, Void, QueryResult>(){
			@Override
			protected QueryResult doInBackground(Void... params) {

				if(queryResult == null){
					query.setCount(150);
					query.setQuery(searchText.getText().toString());
				}
					try {
						Log.d("test","ｷﾚﾃﾙｳｳｳｳｳ");
						queryResult = mTwitter.search(query);

				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				return queryResult;
			}

			@Override
			protected void onPostExecute(QueryResult result) {
				// TODO 自動生成されたメソッド・スタブ
				queryResult = result;
				arrayStatus = null;

				if(queryResult != null){
					for(int i = 1; i < 150 ; i++){
						 arrayStatus = queryResult.getTweets();
						 Log.d("test","queryResult");
					};

					for(twitter4j.Status item: arrayStatus){
						tAdapter.add(item);
						Log.d("test","okokokokokokokokokko");
					}


				}
			}
		};
		task.execute();
	}

	@Override
	public void onClick(View v) {
		tAdapter.clear();
		queryResult = null;
		search();
		// TODO 自動生成されたメソッド・スタブ

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
			//Log.d("test","あああああああああああああああああああああああああああああああ");
			//Log.d("test","ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
			//query = queryResult.nextQuery();
			//search();
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(flingFlag == FLAG_FALSE){
			Log.d("tweet_detail","view:" + parent + "  position:" + position + "  id:" + id);
			Status item = (Status) searchList.getItemAtPosition(position);
			intent.setClass(getApplicationContext(), Twitter_tweet_detail.class);
			intent.putExtra("TweetStatus", item);
			intent.putExtra("position", position);
			startActivityForResult(intent,TWEET_DETAIL);
		}
		flingFlag = FLAG_FALSE;
	}

	public void exchangeListItem(int position,Status status){
		tAdapter.remove(tAdapter.getItem(position));
		tAdapter.insert(status, position);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(requestCode, resultCode, data);
		this.intent = data;
		// startActivityForResult()の際に指定した識別コードとの比較
		if( requestCode == TWEET_DETAIL ){
			// 返却結果ステータスとの比較
			if( resultCode == Activity.RESULT_OK ){
				// 返却されてきたintentから値を取り出す
				int position = intent.getIntExtra( "position", 0 );
				myUser = (User) intent.getSerializableExtra("myUser");
				Status tweetStatus = (Status) intent.getSerializableExtra("tweetStatus");
				exchangeListItem(position, tweetStatus);
			}
		}
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
					finish();
					overridePendingTransition(R.anim.right_in, R.anim.left_out);

				} else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// 終了位置から開始位置の移動距離が指定値より大きい
					// X軸の移動速度が指定値より大きい
			//		intent.setClass(mContext, TwitterSearch.class);
			//		startActivity(intent);
			//		overridePendingTransition(R.anim.left_in, R.anim.right_out);
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