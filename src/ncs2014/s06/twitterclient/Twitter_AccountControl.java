package ncs2014.s06.twitterclient;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_AccountControl extends Activity {

	private Twitter mTwitter;
	private DBAdapter dbAdapter;
	private accountAdapter aAdapter;
	private Cursor cursor;

	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_lists);

		list = (ListView) findViewById(R.id.twitter_lists_list);

		aAdapter = new accountAdapter(this);
		list.setAdapter(aAdapter);

		mTwitter = TwitterUtils.getTwitterInstance(this);

		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		cursor = dbAdapter.getAllAccount();

		if(cursor.moveToFirst()){
			final Long id = cursor.getLong(0);
			do{
				AsyncTask<Void, Void, User> task = new AsyncTask<Void, Void, User>(){
					@Override
					protected User doInBackground(Void... params) {
						User user = null;
						try {
							user = mTwitter.showUser(id);
						} catch (TwitterException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
						return user;
					}

					@Override
					protected void onPostExecute(User result) {
						aAdapter.add(result);
					}
				};
				task.execute();

			}while(cursor.moveToNext());
			cursor.close();

		}else{
			showToast("アカウントが登録されていません");
		}


	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	//表示定義クラス
	public class accountAdapter extends ArrayAdapter<User> {

		private LayoutInflater mInflater;

		public accountAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1);
			mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		}

		//表示定義
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自動生成されたメソッド・スタブ

			if(convertView == null){
				convertView = mInflater.inflate(R.layout.list_item_account, null);
			}

			User item = getItem(position);
			SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.list_item_account_icon);
			icon.setImageUrl(item.getProfileImageURL());
			TextView name = (TextView) convertView.findViewById(R.id.list_item_account_name);
			name.setText(item.getName());
			TextView screenName = (TextView) convertView.findViewById(R.id.list_item_account_screen_name);
			screenName.setText("@"+ item.getScreenName());
			return convertView;
		}
	}//accountAdapter
}
