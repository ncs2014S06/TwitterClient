package ncs2014.s06.twitterclient;
import java.util.ArrayList;
import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Twitter_Client_DM extends FragmentActivity {

	 private Twitter mTwitter;
	private TweetAdapter tAdapter;
	private ListView list;



	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_dmlist);

		list = (ListView) findViewById(R.id.dmlist);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		}else{
			tAdapter = new TweetAdapter(this);
			mTwitter = TwitterUtils.getTwitterInstance(this);
		}


		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reloadDM();
				list.setAdapter(tAdapter);
			}
		});
	}

	private void reloadDM(){
		AsyncTask<Void, Void, List<String>> task = new AsyncTask<Void, Void, List<String>>(){
			@Override
			protected List<String> doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ
				try {
					Paging paging = new Paging(1);
					ResponseList<DirectMessage> messages;
					ArrayList<String> list = new ArrayList<String>();
					do {
						messages = mTwitter.getDirectMessages(paging);
						for (DirectMessage message : messages) {
							Log.d("dm", message.getText());
							list.add(message.getText());
						}
						paging.setPage(paging.getPage() + 1);
					} while (messages.size() > 0 && paging.getPage() < 10);
					return list;
				} catch (TwitterException te) {
					te.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(List<String> result) {
				// TODO 自動生成されたメソッド・スタブ
				if(result != null){
					tAdapter.clear();
					for(String status:result){
						tAdapter.add(status);
					}
				}else{
					showToast("DMの取得に失敗しました");
				}
			}
		};
		task.execute();
	}

	private void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}


	private class TweetAdapter extends ArrayAdapter<String>{
		public TweetAdapter(Context context){
			super(context,android.R.layout.simple_list_item_1);
		}
	}
}