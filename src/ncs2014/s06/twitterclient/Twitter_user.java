package ncs2014.s06.twitterclient;

import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.image.SmartImageView;

public class Twitter_user extends Activity implements OnClickListener {

	private TweetGet tg;
	private SmartImageView view;
	private static Twitter mTwitter;
	private Button myTweet;
	private Button follow;
	private Button follower;
	private Button fav;
	private TweetAdapter tAdapter;
	private ListView list;
	private PagableResponseList<twitter4j.User> rawData;
	private static long cursor = -1;
	private IDs ids;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TweetGet tg = new TweetGet();
		super.onCreate(savedInstanceState);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		rawData = null;
		ImageGet ig = new ImageGet(mTwitter);
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


	private void followGet(){


	}


	public void followerGet(){
	}

	public void favGet(){
	}

	@Override
	public void onClick(View v) {
		if(v == myTweet){
			startActivity(new Intent(Twitter_user.this,TweetGet.class));
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

}