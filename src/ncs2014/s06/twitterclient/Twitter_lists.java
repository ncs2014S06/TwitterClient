package ncs2014.s06.twitterclient;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Twitter_lists extends Activity implements OnItemClickListener {

	private Intent intent;
	private Twitter mTwitter;
	private User myUser;
	private ResponseList<UserList> lists;
	private ListAdapter adapter;

	private ListView lists_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_lists);

		lists_list = (ListView) findViewById(R.id.twitter_lists_list);

		lists_list.setOnItemClickListener(this);

		adapter = new ListAdapter(this);
		lists_list.setAdapter(adapter);


		intent = getIntent();
		mTwitter = TwitterUtils.getTwitterInstance(this);
		myUser = (User) intent.getSerializableExtra("myUser");

		searchLists();
	}

	public void searchLists(){
		AsyncTask<Void, Void, ResponseList<UserList>> task = new AsyncTask<Void, Void, ResponseList<UserList>>(){
			@Override
			protected ResponseList<UserList> doInBackground(Void... params) {
				try {
					lists = mTwitter.getUserLists(myUser.getId());
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				return lists;
			}

			@Override
			protected void onPostExecute(ResponseList<UserList> result) {
				for(UserList item :result){
					adapter.add(item);
				}
			//	lists_list.setAdapter(adapter);
			}
		};
		task.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		UserList item = (UserList) lists_list.getItemAtPosition(position);
		Long listId = item.getId();
		intent.setClass(getApplicationContext(), Twitter_list.class);
		intent.putExtra("ListId", listId);
		startActivity(intent);
	}



}
