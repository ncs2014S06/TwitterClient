package ncs2014.s06.twitterclient;


import twitter4j.Twitter;
import twitter4j.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.loopj.android.image.SmartImageView;


public class Twitter_user extends Activity implements OnClickListener {

	private TweetGet tg;
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
		TweetGet tg = new TweetGet();
		super.onCreate(savedInstanceState);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		ImageGet ig = new ImageGet(mTwitter);
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
	//	ImageGet ig = new ImageGet(mTwitter);

		view = (SmartImageView) findViewById(R.id.image_user);
		ig.setImage(view);



		tAdapter = new TweetAdapter(this);


	}


	@Override
	public void onClick(View v) {
		if(v == myTweet){
			startActivity(new Intent(Twitter_user.this,TweetGet.class));
		}

		if(v == follow){
		}

		if(v == follower){
		}

		if(v == fav){
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


}
