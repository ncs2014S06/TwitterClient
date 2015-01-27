package ncs2014.s06.twitterclient;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_createDM extends FragmentActivity {

	private EditText mInputText;
	private TextView from;
	private TextView text2;
	private String from2;
	private Twitter mTwitter;
	private SmartImageView view;
	private DirectMessage message;
	private DirectMessage msg;
	private String to;
	private String text;
	private String img;
	private long id;
	private Status item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		mTwitter = TwitterUtils.getTwitterInstance(this);
		setContentView(R.layout.twitter_createdm);
		//ImageGet ig = new ImageGet(mTwitter);

		msg = (DirectMessage) intent.getSerializableExtra("msg");

		Log.d("test","test");
		from2 = intent.getStringExtra("id");
		Log.d("test222",from2);
		text =  intent.getStringExtra("text");
		img = intent.getStringExtra("img");
		from = (TextView) findViewById(R.id.from);
		from.setText(from2);
		text2 = (TextView)findViewById(R.id.honbun);
		text2.setText(text);
		view = (SmartImageView)findViewById(R.id.DM_MyImage25);
		mInputText = (EditText) findViewById(R.id.input_text);





		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void,String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ
				Log.d("test222",msg.getSenderScreenName() + "sd");
				String s = msg.getSender().getProfileImageURL();
				return s;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO 自動生成されたメソッド・スタブ
				super.onPostExecute(result);
				view.setImageUrl(result);
			}

		};
		task.execute();



		findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				sendDM();
			}
		});
	}




	private void sendDM(){
		AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void,Boolean>(){

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ

				try {
					message = mTwitter.sendDirectMessage(from2,mInputText.getText().toString());
					Log.d("test","Direct message successfully sent to " + message.getRecipientScreenName());
					return true;
					//System.exit(0);
				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					return false;
				}
			}

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    showToast("送信完了");
                    finish();
                } else {
                    showToast("送信失敗");
                }
            }
        };
        task.execute();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}



