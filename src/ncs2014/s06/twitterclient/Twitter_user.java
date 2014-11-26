package ncs2014.s06.twitterclient;

import twitter4j.Twitter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class Twitter_user extends Activity {

	private ImageView view;
	private Twitter mTwitter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTwitter = TwitterUtils.getTwitterInstance(this);

		String name = null;

		name = "ncs06_sotuken";

		ImageGet ig = new ImageGet(view, R.id.imageView1, name, mTwitter);
		ig.imageSet();

		setContentView(R.layout.twitteruser);
		ListView listView = (ListView) findViewById(R.id.aaa);


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

		 adapter.add("a");
		 adapter.add("b");
		 adapter.add("c");

		 listView.setAdapter(adapter);



	}
}