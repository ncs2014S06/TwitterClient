package ncs2014.s06.twitterclient;

import com.loopj.android.image.SmartImageView;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class TwitterSearch extends Activity {

	private Twitter mTwitter;
	private EditText searchText;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		setContentView(R.layout.twitter_follow);
		searchText = (SmartImageView)findViewById(R.id.);


    }


	private void search(){

	    Query query = new Query("source:twitter4j yusukey");
	    QueryResult result = null;
		try {
			Twitter twitter = TwitterFactory.getSingleton();
			result = mTwitter.search(query);
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	    for (Status status : result.getTweets()) {
	        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
	    }



	}

}
