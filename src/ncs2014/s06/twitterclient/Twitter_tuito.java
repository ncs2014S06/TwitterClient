package ncs2014.s06.twitterclient;
import java.io.File;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_tuito extends FragmentActivity {


	private final static int REQUEST_PICK = 1;
	private final static String CONSUMER_KEY = "(書き換えてください)";
	private final static String CONSUMER_SECRET = "(書き換えてください)";
	private final static String ACCESS_TOKEN = "(書き換えてください)";
	private final static String ACCESS_TOKEN_SECRET = "(書き換えてください)";


    private EditText mInputText;
    private Twitter mTwitter;
    private SmartImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_tweet);

        mTwitter = TwitterUtils.getTwitterInstance(this);
     //mInputText = (EditText) findViewById(R.id.input_text);
        ImageGet ig = new ImageGet(mTwitter);
        view = (SmartImageView) findViewById(R.id.icon);
        ig.setImage(view);

		findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener() {

        	@Override
            public void onClick(View v) {


        		Intent intent = new Intent(Intent.ACTION_PICK);
        		intent.setType("image/*");
        		startActivityForResult(intent, REQUEST_PICK);


                //tweet();
            }
        });
    }

    private void tweet() {
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {

                    mTwitter.updateStatus(params[0]);
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    showToast("ツイートが完了しました！");
                    finish();
                } else {
                    showToast("ツイートに失敗しました。。。");
                }
            }
        };
        task.execute(mInputText.getText().toString());
    }






    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_PICK && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			ContentResolver cr = getContentResolver();
			String[] columns = { MediaStore.Images.Media.DATA };
			Cursor c = cr.query(uri, columns, null, null, null);

			c.moveToFirst();
			File path = new File(c.getString(0));
			if (!path.exists())
				return;

/*
			twitter4j.conf.ConfigurationBuilder cb = new twitter4j.conf.ConfigurationBuilder();
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(CONSUMER_KEY);
			builder.setOAuthConsumerSecret(CONSUMER_SECRET);
			builder.setOAuthAccessToken(ACCESS_TOKEN);
			builder.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
			// ここでMediaProviderをTWITTERにする
			cb.setMediaProvider("TWITTER");
*/
			Configuration conf = mTwitter.getConfiguration();

			ImageUpload imageUpload = new ImageUploadFactory(conf).getInstance();

			EditText textTweet = (EditText) findViewById(R.id.input_text);
			String tweet = textTweet.getText().toString();

			try {
				imageUpload.upload(path, tweet);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}










    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}