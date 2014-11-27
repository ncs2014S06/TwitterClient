package ncs2014.s06.twitterclient;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class Twitter_tuito extends FragmentActivity {

    private EditText mInputText;
    private Twitter mTwitter;
    private SmartImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_tweet);

        mTwitter = TwitterUtils.getTwitterInstance(this);
        mInputText = (EditText) findViewById(R.id.input_text);

        ImageGet ig = new ImageGet(mTwitter);
        view = (SmartImageView) findViewById(R.id.icon);
        ig.setImage(view);

        findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener() {

        	@Override
            public void onClick(View v) {
                tweet();
            }
        });
    }

    private void tweet() {
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
            		//view = (SmartImageView) findViewById(R.id.imageView1);
            		//ig.setImage(view);

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

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}