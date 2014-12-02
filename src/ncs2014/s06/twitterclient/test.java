package ncs2014.s06.twitterclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class test extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_user_status);


		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
			}
		}

}