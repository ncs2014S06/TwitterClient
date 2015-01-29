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
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_createDM2 extends FragmentActivity {

	private EditText mInputText;
	private EditText mInputText1;
	private Twitter mTwitter;
	private SmartImageView view;
	private DirectMessage message;
	private String to;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_dm_newmail);

		mTwitter = TwitterUtils.getTwitterInstance(this);

		ImageGet ig = new ImageGet(mTwitter);
	//	view = (SmartImageView) findViewById(R.id.DM_MyImage);
		ig.setImage(view);

		mInputText = (EditText) findViewById(R.id.dm_sendmessage);
		mInputText1 =   (EditText)findViewById(R.id.dm_toname);


		findViewById(R.id.dm_messagesendbutton).setOnClickListener(new View.OnClickListener() {
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
					message = mTwitter.sendDirectMessage(mInputText1.getText().toString(),mInputText.getText().toString());
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



