package ncs2014.s06.twitterclient;
import java.io.File;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_tuito extends FragmentActivity {


	private  static int REQUEST_PICK = 1;
	private static String CONSUMER_KEY = "rnEXcIylpdaiO91qS8xQbV1J";
	private static String CONSUMER_SECRET = "1aFTk1YLNASB3lRcENcJZQca5T1PCv5nvKdyDgNmZfmWZ8104r";
	private static String ACCESS_TOKEN = "2882966317-gRwWjwwR1W3W09eKLrISoPKLmabNvsYIa98m0l";
	private static String ACCESS_TOKEN_SECRET = "t85YjUlwu6PRUqSMr91C3aeC3oBdN00h9x7HyTe5jupd0";


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
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

				if (requestCode == REQUEST_PICK && resultCode == RESULT_OK) {
					Uri uri = data.getData();
					ContentResolver cr = getContentResolver();
					String[] columns = { MediaStore.Images.Media.DATA };
					Cursor c = cr.query(uri, columns, null, null, null);

					c.moveToFirst();
					File path = new File(c.getString(0));

					//パスがない場合
					if (!path.exists())
						return;


					ConfigurationBuilder builder = new ConfigurationBuilder();
					builder.setOAuthConsumerKey(CONSUMER_KEY);
					builder.setOAuthConsumerSecret(CONSUMER_SECRET);
					builder.setOAuthAccessToken(ACCESS_TOKEN);
					builder.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

					// ここでMediaProviderをTWITTERにする
					builder.setMediaProvider("TWITTER");

					Configuration conf = builder.build();
					ImageUpload imageUpload = new ImageUploadFactory(conf).getInstance();
					EditText textTweet = (EditText) findViewById(R.id.input_text);
					String tweet = textTweet.getText().toString();
					//ImageUp(imageUpload, path, tweet);
					c.close();

	//			return null;
	//		}
	//			return null;



/*
			@Override
			protected void onPostExecute(String tweet) {
				try {
					imageUpload.upload(path, tweet);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				if(result != null){
				}else{
				}
			}
*/
	//	};

		};
	}

	private void ImageUp(ImageUpload imageUpload,File path,String tweet){
		final ImageUpload imageUpload1 = imageUpload;
		final File path1 = path;
		final String tweet1 = tweet;

		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				// TODO 自動生成されたメソッド・スタブ
				try {
					imageUpload1.upload(path1, tweet1);
				} catch (TwitterException e) {
					// TODO 自動生成された catch ブロック
					Log.d("test","e");
					e.printStackTrace();
				}
				return null;
			}

		};
		task.execute();
    }






    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}