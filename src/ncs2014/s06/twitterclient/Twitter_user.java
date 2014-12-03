package ncs2014.s06.twitterclient;

import twitter4j.Twitter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.loopj.android.image.SmartImageView;

public class Twitter_user extends Activity implements OnClickListener {

	private SmartImageView view;
	private Twitter mTwitter;
	private Button myTweet;
	private Button follow;
	private Button follower;
	private Button fav;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		ImageGet ig = new ImageGet(mTwitter);
		Log.d("test","test");
		setContentView(R.layout.twitter_user_status);
		view = (SmartImageView) findViewById(R.id.user_image);
		ig.setImage(view);

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
			tweetGet();
		}

		if(v == follow){
			followGet();
		}

		if(v == follower){
			followerGet();
		}

		if(v == fav){
			favGet();
		}

	}

	public void tweetGet(){




	}

	public void followGet(){

	}

	public void followerGet(){

	}

	public void favGet(){

	}




}