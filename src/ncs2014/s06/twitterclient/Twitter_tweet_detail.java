package ncs2014.s06.twitterclient;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ncs2014.s06.twitterclient.TextLinker.OnLinkClickListener;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_tweet_detail extends Activity implements OnClickListener{
	private Twitter mTwitter;
	private Status tweetStatus;
	private long tweetId;
	private int position;
	private SparseArray<String> links;
	private int flag;
	private boolean favFlag;
	private boolean retweetFlag;
	private int favColor = Color.rgb(243, 213, 26);
	private int retweetColor = Color.rgb(71, 234, 126);

	private LinearLayout tweet_detail_lineLayout;
	private SmartImageView tweet_detail_userIcon;
	private TextView tweet_detail_userName;
	private TextView tweet_detail_userId;
	private TextView tweet_detail_absoluteTime;
	private TextView tweet_detail_tweet;
	//button
	private ImageButton bt_reply;
	private ImageButton bt_retweet;
	private ImageButton bt_fav;
	private ImageButton bt_more;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_tweet_detail);
		View view = this.getLayoutInflater().inflate(R.layout.twitter_twwet_detail_botans, null);
		RelativeLayout.LayoutParams lllp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		addContentView(view, lllp);
		//findview
		tweet_detail_lineLayout = (LinearLayout) findViewById(R.id.tweet_detail_LinearLayout);
		tweet_detail_userIcon = (SmartImageView) findViewById(R.id.tweet_detail_usericon);
		tweet_detail_userName = (TextView) findViewById(R.id.tweet_detail_username);
		tweet_detail_userId = (TextView) findViewById(R.id.tweet_detail_userid);
		tweet_detail_absoluteTime = (TextView) findViewById(R.id.tweet_detail_absolutetime);
		tweet_detail_tweet = (TextView) findViewById(R.id.tweet_detail_tweet);
		bt_reply = (ImageButton) findViewById(R.id.bt_reply);
		bt_retweet = (ImageButton) findViewById(R.id.bt_retweet);
		bt_fav = (ImageButton) findViewById(R.id.bt_fav);
		bt_more = (ImageButton) findViewById(R.id.bt_more);

		//リスナー
		bt_reply.setOnClickListener(this);
		bt_retweet.setOnClickListener(this);
		bt_fav.setOnClickListener(this);
		bt_more.setOnClickListener(this);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}

		mTwitter = TwitterUtils.getTwitterInstance(this);
		Intent intent = getIntent();
	//	this.mTwitter = (Twitter) intent.getSerializableExtra("mTwitter");
		this.tweetStatus = (Status) intent.getSerializableExtra("TweetStatus");
		this.position = intent.getIntExtra("position", 0);

		showTweet();

		//ファボ・リツイートボタン変色
		favFlag = tweetStatus.isFavorited();
		retweetFlag = tweetStatus.isRetweeted();
		if(favFlag){
			bt_fav.setColorFilter(favColor);
		}
		if(retweetFlag){
			bt_retweet.setColorFilter(retweetColor);
		}
	}

	private void showTweet(){

		tweet_detail_userIcon.setImageUrl(tweetStatus.getUser().getProfileImageURL());
		tweet_detail_userName.setText(tweetStatus.getUser().getName());
		tweet_detail_userId.setText("@"+ tweetStatus.getUser().getScreenName());
		tweet_detail_absoluteTime.setText(tweetStatus.getCreatedAt().toString());
		String tweet = tweetStatus.getText();


		ArrayList<String> LinkArray = new ArrayList<String>(); //リンク変更予定URL配列

		//URL展開
		URLEntity[] uEntities = tweetStatus.getURLEntities();
		for(URLEntity entity : uEntities){
			String ex_url = entity.getExpandedURL(); //展開済みURL
			String tco = entity.getURL(); //展開前URL
			Log.d("url","展開前URL:" + tco);
			Log.d("url","展開後URL:" + ex_url);
			Pattern p = Pattern.compile(tco);
			Matcher m = p.matcher(tweet);
			tweet = m.replaceAll(ex_url);
			LinkArray.add(ex_url); //リンク変更予定URL配列に入れる
		}//URL展開

		//メディア展開
		MediaEntity[] mEntities = tweetStatus.getExtendedMediaEntities();
		ArrayList<SmartImageView> arraySiv = new ArrayList<SmartImageView>();
		for(MediaEntity entity : mEntities){
			String displayUrl = "http://" + entity.getDisplayURL(); //表示用URL
			String tco = entity.getURL(); //展開前URL
			Log.d("url","メディア展開前URL:" + tco);
			Log.d("url","メディア展開後URL:" + displayUrl);
			Pattern p = Pattern.compile(tco);
			Matcher m = p.matcher(tweet);
			tweet = m.replaceAll(displayUrl);
			LinkArray.add(displayUrl); //リンク変更予定URL配列に入れる

			SmartImageView iv = new SmartImageView(this);
			iv.setImageUrl(entity.getMediaURL());
			arraySiv.add(iv);
		}
		int mediaNo = 1;
		for(SmartImageView iv: arraySiv){
			tweet_detail_lineLayout.addView(iv);
			Log.d("media","addView");
			Log.d("media",mediaNo + "枚目");
			mediaNo++;
		}

		links = new SparseArray<String>();
		int LinkArrayKey = 0;
		for(String link:LinkArray){
			links.append(LinkArrayKey, link);
			Log.d("tweet_detail",link + ":");
			LinkArrayKey++;
		}
		OnLinkClickListener onLinkClickListener = new OnLinkClickListener() {
			@Override
			public void onLinkClick(int textId) {
				Uri uri = Uri.parse(links.get(textId));
				Log.d("tweet_detail","links:" + links.get(textId));
				Intent InternetIntent = new Intent(Intent.ACTION_VIEW,uri);
				startActivity(InternetIntent);
			}
		};




		tweet_detail_tweet.setText(TextLinker.getLinkableText(tweet, links, onLinkClickListener));
		tweet_detail_tweet.setMovementMethod(LinkMovementMethod.getInstance());
	}


	@Override
	public void onClick(View v) {
		if(v == bt_reply){
			String screenName = tweetStatus.getUser().getScreenName();
			Intent intent = new Intent(getApplication(),Twitter_tuito.class);
			intent.putExtra("screenName", screenName);
			startActivity(intent);
		}
		//リツートボタン
		if(v == bt_retweet){
			AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>(){
						@Override
						protected Integer doInBackground(Void... params) {
							flag = 0;
							try {
								mTwitter.retweetStatus(tweetStatus.getId());
							} catch (TwitterException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
								flag = 1;
							}
							return flag;
						}
						@Override
						protected void onPostExecute(Integer result) {
							// TODO 自動生成されたメソッド・スタブ
							super.onPostExecute(result);
							if(flag != 1){
								bt_retweet.setColorFilter(retweetColor);
								showToast("リツイートしました");
							}else{
								showToast("同じ内容のツイートを連続で投稿することはできません");
							}
						}
				};
			task.execute();
		}

		//お気に入りボタン
		if(v == bt_fav){
			AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>(){
				@Override
				protected Integer doInBackground(Void... params) {
					flag = 0;
					try {
						tweetId = tweetStatus.getId();
						Log.d("fav","tweetId:" + tweetId);
						//お気に入り登録
						tweetStatus = mTwitter.createFavorite(tweetId);
						Log.d("fav","afterTweetId:" + tweetStatus.getId());
						if(tweetStatus.isFavorited()){
							Log.d("fav","favされてるよ");
						}else{
							Log.d("fav","favされてないよ");
						}
					} catch (TwitterException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
						flag = 1;
					}
					return flag;
				}
				@Override
				protected void onPostExecute(Integer result) {
					// TODO 自動生成されたメソッド・スタブ
					super.onPostExecute(result);
					if(flag != 1){
						bt_fav.setColorFilter(favColor);
						showToast("お気に入り登録しました");
					}else{
						showToast("すでにお気に入り登録されています");
					}
				}
		};
	task.execute();

		}
		if(v == bt_more){

		}
	}

	private void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			// intentの作成
			Intent intent = new Intent();

			// intentへ添え字付で値を保持させる
			intent.putExtra( "position", position );
			intent.putExtra("tweetStatus", tweetStatus);

			// 返却したい結果ステータスをセットする
			setResult( Activity.RESULT_OK, intent );

			// アクティビティを終了させる
			finish();
			return true;
		}
			return false;
	}

}