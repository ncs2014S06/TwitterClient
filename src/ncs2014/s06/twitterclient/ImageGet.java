package ncs2014.s06.twitterclient;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.image.SmartImageView;

public class ImageGet extends Activity{
	private Twitter mTwitter;
	private User user;
	private String name;
	private SmartImageView view;


	public ImageGet(Twitter mTwitter) {
		this.mTwitter = mTwitter;
	}

	/**
	 * ログインユーザーのアイコンをview1にセットする
	 * @param view1
	 */
	public void setImage(SmartImageView view1){
		this.view = view1;
		user = null;
		AsyncTask<Void, Void, User> task = new AsyncTask<Void,Void,User>(){

			@Override
			protected User doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ
				try {
					Log.d("test",mTwitter.getScreenName());
					User test = mTwitter.showUser(mTwitter.getScreenName());
					return test;
				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Log.d("test","error");
				}
				return null;
			}

			@Override
			protected void onPostExecute(User result) {
				// TODO 自動生成されたメソッド・スタブ
				if(result != null){
					user = result;
					view.setImageUrl(user.getProfileImageURL());
					Log.d("test",user.getScreenName());
				}else{
					Log.d("test","null1");
				}
			}
		};
		task.execute();
	}

	/**
	 * name1のアイコンをview1にセットする
	 * @param view1
	 * @param name1
	 */
	public void setImage(SmartImageView view1,String name1){
		this.view = view1;
		this.name = name1;
		user = null;
		AsyncTask<Void, Void, User> task = new AsyncTask<Void,Void,User>(){

			@Override
			protected User doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ
				try {
					User test = mTwitter.showUser(name);
					return test;
				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Log.d("test","error");
				}
				return null;
			}

			@Override
			protected void onPostExecute(User result) {
				// TODO 自動生成されたメソッド・スタブ
				if(result != null){
					user = result;
					view.setImageUrl(user.getProfileImageURL());
					Log.d("test",user.getScreenName());
				}else{
					Log.d("test","null1");
				}
			}
		};
		task.execute();
	}


}
