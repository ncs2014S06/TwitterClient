package ncs2014.s06.twitterclient;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class TwitterSearch extends Activity implements OnClickListener,OnScrollListener{

	private Twitter mTwitter;
	private EditText searchText;
	private Button searchButton;
	private ListView searchList;
	private TweetAdapter tAdapter;
	private Paging paging;
	private QueryResult queryResult = null;
	private Query query;
	private String searchWord;
	private List<twitter4j.Status> arrayStatus ;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		mTwitter = TwitterUtils.getTwitterInstance(this);

		setContentView(R.layout.twitter_search);
		searchText = (EditText)findViewById(R.id.searchtext);
		searchButton = (Button)findViewById(R.id.searchbutton);
		searchList = (ListView)findViewById(R.id.search_list);
		searchButton.setOnClickListener(this);
		tAdapter = new TweetAdapter(this);
		searchList.setAdapter(tAdapter);
		searchList.setOnScrollListener(this);
		paging = new Paging(1);
		query = new Query( );
    }


	private void search(){

	    AsyncTask<Void, Void, QueryResult> task = new AsyncTask<Void, Void, QueryResult>(){
			@Override
			protected QueryResult doInBackground(Void... params) {

				if(queryResult == null){
					query.setCount(150);
					query.setQuery(searchText.getText().toString());
				}
					try {
						Log.d("test","ｷﾚﾃﾙｳｳｳｳｳ");
						queryResult = mTwitter.search(query);

				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				return queryResult;
			}

			@Override
			protected void onPostExecute(QueryResult result) {
				// TODO 自動生成されたメソッド・スタブ
				queryResult = result;
				arrayStatus = null;

				if(queryResult != null){
					for(int i = 1; i < 150 ; i++){
						 arrayStatus = queryResult.getTweets();
						 Log.d("test","queryResult");
					};

					for(twitter4j.Status item: arrayStatus){
						tAdapter.add(item);
						Log.d("test","okokokokokokokokokko");
					}


				}
			}
		};
		task.execute();
	}

	@Override
	public void onClick(View v) {
		tAdapter.clear();
		queryResult = null;
		search();
		// TODO 自動生成されたメソッド・スタブ

	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void onScroll(AbsListView view, int iTop,
			int iVisible, int iTotal) {
		boolean bLast = iTotal == iTop + iVisible;
		if(bLast){
			//Log.d("test","あああああああああああああああああああああああああああああああ");
			//Log.d("test","ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
			//query = queryResult.nextQuery();
			//search();
		}
	}
}