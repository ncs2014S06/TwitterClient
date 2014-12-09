package ncs2014.s06.twitterclient;

import twitter4j.Twitter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.loopj.android.image.SmartImageView;

public class Twitter_user extends Activity implements OnClickListener{

	private SmartImageView view;
	private Twitter mTwitter;
	private ImageButton bt_update;
	private ImageButton bt_tuito;
	private ImageButton bt_user;
	private ImageButton bt_dm;

	//intent
	Intent intent = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.twitter_user_status);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_acount);

		//findview
		bt_update = (ImageButton) findViewById(R.id.bt_update);
		bt_tuito = (ImageButton) findViewById(R.id.bt_tuito);
		bt_user = (ImageButton) findViewById(R.id.bt_user);
		bt_dm = (ImageButton) findViewById(R.id.bt_dm);

		//リスナー
		bt_update.setOnClickListener(this);
		bt_tuito.setOnClickListener(this);
		bt_user.setOnClickListener(this);
		bt_dm.setOnClickListener(this);






		mTwitter = TwitterUtils.getTwitterInstance(this);
		ImageGet ig = new ImageGet(mTwitter);
		Log.d("test","test");
		//ListView listView = (ListView) findViewById(R.id.aaa);
		view = (SmartImageView) findViewById(R.id.image_user);

		ig.setImage(view);

		/**ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

		 adapter.add("a");
		 adapter.add("b");
		 adapter.add("c");

		 listView.setAdapter(adapter);
		 */
	}

	@Override
	public void onClick(View v) {
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