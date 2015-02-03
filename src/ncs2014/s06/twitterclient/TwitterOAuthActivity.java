package ncs2014.s06.twitterclient;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TwitterOAuthActivity extends Activity {

	private String mCallbackURL;
	private Twitter mTwitter;
	private RequestToken mRequestToken;
	private DBAdapter dbAdapter;
	private Context mContext;






	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_oauth);
		mContext = getApplicationContext();

		//DB作成
		dbAdapter = new DBAdapter(this);


		mCallbackURL = getString(R.string.twitter_callback_url);
		mTwitter = TwitterUtils.getTwitterInstance(this);

		findViewById(R.id.action_start_oauth).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("OnClick", "Button was clicked.");
				startAuthorize();
			}
		});
	}

	/**
	 * OAuth認証（厳密には認可）を開始します。
	 *
	 * @param listener
	 */
	private void startAuthorize() {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				try {
					mRequestToken = mTwitter.getOAuthRequestToken(mCallbackURL);
					return mRequestToken.getAuthorizationURL();
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(String url) {
				if (url != null) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
				} else {
					// 失敗。。。
				}
			}
		};
		task.execute();
	}

	@Override
	public void onNewIntent(Intent intent) {
		if (intent == null
				|| intent.getData() == null
				|| !intent.getData().toString().startsWith(mCallbackURL)) {
			return;
		}
		String verifier = intent.getData().getQueryParameter("oauth_verifier");

		AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>() {
			@Override
			protected AccessToken doInBackground(String... params) {
				try {
					return mTwitter.getOAuthAccessToken(mRequestToken, params[0]);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(AccessToken accessToken) {
				if (accessToken != null) {
					// 認証成功！
					showToast("認証成功！");
					successOAuth(accessToken);
				} else {
					// 認証失敗。。。
					showToast("認証失敗。。。");
				}
			}
		};
		task.execute(verifier);
	}

	private void successOAuth(AccessToken accessToken) {
		TwitterUtils.storeAccessToken(this, accessToken);

		Long id = accessToken.getUserId();
		String strAccessToken = accessToken.getToken();
		String strAccessTokenSecret = accessToken.getTokenSecret();
		dbAdapter.open();
		dbAdapter.saveAccount(id,strAccessToken, strAccessTokenSecret);
		Cursor c = dbAdapter.getAllAccount();
		c.moveToFirst();
		Log.d("test", c.getColumnName(0));
		Log.d("test", c.getColumnName(2));
		Log.d("test", c.getLong(0) + "");

		dbAdapter.close();
		Intent intent = new Intent(mContext, Twitter_home.class);
		startActivity(intent);
		finish();
	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
