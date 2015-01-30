package ncs2014.s06.twitterclient;
import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_createDM extends FragmentActivity implements OnClickListener {

	private DirectMessage selectDirectMessage;
	private String senderScreenName;
	private Twitter mTwitter;
	private DirectMessage message;
	private User senderUser;
	private int mode;
	private final static int reply = 0; //返信
	private final static int newMail = 1; //新規

	private SmartImageView userIcon;
	private TextView fromName;
	private TextView toName;
	private TextView receivMessage;
	private EditText sendMessage;
	private Button messageSendButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		Intent intent = getIntent();
		mode = intent.getIntExtra("mode", 0);
		if(mode == reply){ //返信
			setContentView(R.layout.twitter_dm_reply);
			fromName = (TextView) findViewById(R.id.dm_fromname);
			receivMessage = (TextView) findViewById(R.id.dm_receivemessage);
		}
		if(mode == newMail){ //新規
			setContentView(R.layout.twitter_dm_newmail);
			toName = (TextView) findViewById(R.id.dm_toname);
		}

		//共通部
		userIcon = (SmartImageView) findViewById(R.id.DM_usericon);
		sendMessage = (EditText) findViewById(R.id.dm_sendmessage);
		messageSendButton = (Button) findViewById(R.id.dm_messagesendbutton);
		//リスナー
		messageSendButton.setOnClickListener(this);


		if(mode == reply){ //返信
			selectDirectMessage = (DirectMessage) intent.getSerializableExtra("selectDirectMessage");
			senderUser = selectDirectMessage.getSender();
			senderScreenName = senderUser.getScreenName();

			fromName.setText(senderUser.getName() + "(" + senderScreenName + ")");
			receivMessage.setText(selectDirectMessage.getText());
		}

		if(mode == newMail){ //新規
			senderUser = (User) intent.getSerializableExtra("myUser");
			Log.d("test", "新規 " + senderUser.toString());
		}

		//共通部
		userIcon.setImageUrl(senderUser.getProfileImageURL());
	}


	private void sendDM(){
		AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void,Boolean>(){

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ
				try {
					message = mTwitter.sendDirectMessage(senderScreenName,sendMessage.getText().toString());
					Log.d("client_dm","Direct message successfully sent to " + message.getRecipientScreenName());
					return true;
				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if(result){
					showToast("送信完了");
					finish();
				}else{
					showToast("送信失敗");
				}
			}

		};
		task.execute();
	}

	@Override
	public void onClick(View v) {
		if(mode == newMail){
			senderScreenName = toName.getText().toString();
		}
		sendDM();
	}


	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}

