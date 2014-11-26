package ncs2014.s06.twitterclient;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.image.SmartImageView;

public class Twitter_user extends Activity {

	private SmartImageView view;
	private Twitter mTwitter;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		Log.d("test","test");
		setContentView(R.layout.twitteruser);
		ListView listView = (ListView) findViewById(R.id.aaa);
		view = (SmartImageView) findViewById(R.id.imageView1);

		new ImageGet(mTwitter).setImage(view);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

		 adapter.add("a");
		 adapter.add("b");
		 adapter.add("c");

		 listView.setAdapter(adapter);
	}

	private void setImage(){
		AsyncTask<Void, Void, User> task = new AsyncTask<Void,Void,User>(){

			@Override
			protected User doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ
				try {
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


}