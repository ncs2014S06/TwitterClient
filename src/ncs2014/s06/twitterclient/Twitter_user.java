package ncs2014.s06.twitterclient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Twitter_user extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitteruser);

		ListView listView = (ListView) findViewById(R.id.aaa);


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

		 adapter.add("a");
		 adapter.add("b");
		 adapter.add("c");

		 listView.setAdapter(adapter);



	}
}