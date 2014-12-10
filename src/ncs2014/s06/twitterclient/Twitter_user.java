package ncs2014.s06.twitterclient;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.loopj.android.image.SmartImageView;


public class Twitter_user extends Activity implements OnClickListener {

	private SmartImageView view;
	private Twitter mTwitter;
	private ImageButton bt_update;
	private ImageButton bt_tuito;
	private ImageButton bt_user;
	private ImageButton bt_dm;

	//intent
	Intent intent = new Intent();
	private Button myTweet;
	private Button follow;
	private Button follower;
	private Button fav;
	private TweetAdapter tAdapter;
	private ListView list;
	private String followerName;
	private SmartImageView followerImg;
	private User u;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.twitter_user_status);

		//findview
		bt_update = (ImageButton) findViewById(R.id.bt_update);
		bt_tuito = (ImageButton) findViewById(R.id.bt_tuito);
		bt_user = (ImageButton) findViewById(R.id.bt_user);
		bt_dm = (ImageButton) findViewById(R.id.bt_dm);
		list = (ListView) findViewById(R.id.user_list);

		myTweet = (Button) findViewById(R.id.myTweet);
		follow = (Button) findViewById(R.id.follow);
		follower = (Button) findViewById(R.id.follower);

		//リスナー

		bt_update.setOnClickListener(this);
		bt_tuito.setOnClickListener(this);
		bt_user.setOnClickListener(this);
		bt_dm.setOnClickListener(this);
		myTweet.setOnClickListener(this);
		follow.setOnClickListener(this);
		follower.setOnClickListener(this);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_acount);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		ImageGet ig = new ImageGet(mTwitter);
		Log.d("test","test");

		view = (SmartImageView) findViewById(R.id.image_user);
		ig.setImage(view);

		//ボタンに各種値をセット
		tweetGet();
		followGet();
		followerGet();


		tAdapter = new TweetAdapter(this);


	}



	@Override
	public void onClick(View v) {
		if(v == myTweet){
			Log.d("tweet","tddddddd");
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

		if(v == bt_update){

		}//if

		//ツイート画面
		if(v == bt_tuito){
			intent.setClass(getApplicationContext(), Twitter_tuito.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if

		if(v == bt_user){
			intent.setClass(getApplicationContext(), Twitter_user.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if

		if(v == bt_dm){
			intent.setClass(getApplicationContext(), Twitter_Client_DM.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}//if
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

	try {
		User user = mTwitter.verifyCredentials();
		int count =user.getStatusesCount();
		String a = String.valueOf(count);
		myTweet.setText("ツイート\n" + a);
	} catch (TwitterException e) {
		e.printStackTrace();
	}

    }//tweetGet

	public void followGet(){
		try {
			User user = mTwitter.verifyCredentials();
			int count =user.getFriendsCount();
			String a = String.valueOf(count);
			follow.setText("フォロー\n" + a);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	public void followerGet(){
		try {
			User user = mTwitter.verifyCredentials();
			int count =user.getFollowersCount();
			String a = String.valueOf(count);
			follower.setText("フォロワー\n" + a);
			Log.d("aa",a);
		} catch (TwitterException e) {
			e.printStackTrace();
		}


	}

	public void favGet(){
	}

}
