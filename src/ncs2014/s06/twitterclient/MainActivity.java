package ncs2014.s06.twitterclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

	String s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_home);


		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
			}
		}

}