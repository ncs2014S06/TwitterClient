package ncs2014.s06.twitterclient;

import twitter4j.Twitter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.image.SmartImageView;

public class Twitter_user extends Activity {

	private SmartImageView view;
	private Twitter mTwitter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		ImageGet ig = new ImageGet(mTwitter);
		Log.d("test","test");
		setContentView(R.layout.twitter_user);
		ListView listView = (ListView) findViewById(R.id.aaa);
		view = (SmartImageView) findViewById(R.id.imageView1);

		ig.setImage(view);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

		 adapter.add("a");
		 adapter.add("b");
		 adapter.add("c");

		 listView.setAdapter(adapter);
	}


}