package ncs2014.s06.twitterclient;
import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Twitter_createDM extends FragmentActivity {

	private EditText mInputText;
	private EditText mInputText1;
	private Twitter mTwitter;
	private DirectMessage message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_createdm);

		mTwitter = TwitterUtils.getTwitterInstance(this);

		mInputText = (EditText) findViewById(R.id.input_text);

		findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				sendDM();
			}
		});
	}


	private void sendDM(){
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ
				try {
					message = mTwitter.sendDirectMessage("yume_askr","null/////fgsdf/////sdsfsd");
					Log.d("test","Direct message successfully sent to " + message.getRecipientScreenName());
					System.exit(0);
				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

				return null;
			}

		};
		task.execute();
	}
}