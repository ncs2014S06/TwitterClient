package ncs2014.s06.twitterclient;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyUser extends AsyncTask<Void, Void, User>{

	private Twitter mTwitter;
	private User myUser;
	private Handler mHandler;

	public MyUser(Twitter mTwitter,Handler mHandler) {
		this.mTwitter = mTwitter;
		this.mHandler = mHandler;
	}

	@Override
	protected User doInBackground(Void... params) {
		try {
			myUser = mTwitter.verifyCredentials();
			Log.d("Async", "myUser取得完了");
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return myUser;
	}


	@Override
	protected void onPostExecute(User result) {
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putSerializable("myUser", myUser);
		msg.setData(b);
		mHandler.sendMessage(msg);
	}

}
