package ncs2014.s06.twitterclient;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_AccountControl extends Activity implements OnItemClickListener, OnClickListener {

	private Intent intent;
	private Context mContext;
	private Twitter mTwitter;
	private DBAdapter dbAdapter;
	private accountAdapter aAdapter;
	private Cursor cursor;
	private final static int OAUTH_ACTIVITY = 1001;

	private ListView list;
	private Button addButon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		intent = getIntent();
		setContentView(R.layout.twitter_lists);
		View view = this.getLayoutInflater().inflate(R.layout.twitter_account_control_botans, null);
		RelativeLayout.LayoutParams lllp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		addContentView(view, lllp);
		//findViewById
		list = (ListView) findViewById(R.id.twitter_lists_list);
		addButon = (Button) findViewById(R.id.account_control_add_accountbt);
		//リスナー
		list.setOnItemClickListener(this);
		addButon.setOnClickListener(this);

		aAdapter = new accountAdapter(this);
		list.setAdapter(aAdapter);

		mTwitter = TwitterUtils.getTwitterInstance(this);

		dbAdapter = new DBAdapter(this);

		showAccount();

	}

	private void showAccount(){
		dbAdapter.open();
		cursor = dbAdapter.getAllAccount();
		if(cursor.moveToFirst()){
			do{
				final Long id = cursor.getLong(0);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(requestCode, resultCode, data);
		this.intent = data;
		// startActivityForResult()の際に指定した識別コードとの比較
		if( requestCode == OAUTH_ACTIVITY ){
			// 返却結果ステータスとの比較
			if( resultCode == Activity.RESULT_OK ){
				aAdapter.clear();
				showAccount();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		User user = (User) list.getItemAtPosition(position);
		Long userId = user.getId();
		dbAdapter.open();
		Cursor c = dbAdapter.getAccount(userId);
		if(c.moveToFirst()){
			final String accessToken = c.getString(1);
			final String accessTokenSecret =c.getString(2);
			Log.d("AccountControl", accessToken);
			Log.d("AccountControl", accessTokenSecret);

			//ダイアログ
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("アカウント変更");
			adb.setMessage("アカウントを切り替えます。\nよろしいですか？");
			adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自動生成されたメソッド・スタブ
					AccessToken at = new AccessToken(accessToken, accessTokenSecret);
					TwitterUtils.storeAccessToken(mContext, at);
					mTwitter = TwitterUtils.getTwitterInstance(mContext);
					setResult( Activity.RESULT_OK, intent );
					finish();
				}
			});
			adb.setNegativeButton("キャンセル", null);
			adb.show();
		}
	}

	@Override
	public void onClick(View v) {
		intent.setClass(this, TwitterOAuthActivity.class);
		startActivityForResult(intent,OAUTH_ACTIVITY);
	}
}
