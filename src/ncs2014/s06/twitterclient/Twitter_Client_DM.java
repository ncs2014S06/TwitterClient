package ncs2014.s06.twitterclient;
import java.util.ArrayList;
import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Twitter_Client_DM extends FragmentActivity {

	 private Twitter mTwitter;
    private ListView lv ;
    private ArrayAdapter<String> adapter;
    private TwitterAdapter mAdapter;
    List<String> list = new ArrayList<String>();



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_dmlist);

        mTwitter = TwitterUtils.getTwitterInstance(this);
        lv = (ListView) findViewById(R.id.dmlist);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        }

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updata();
            }
        });
    }


	private void updata() {
    	//Twitter twitter = new TwitterFactory().getInstance();
        try {
            Paging paging = new Paging(1);
            List<DirectMessage> messages;
            do {
                //messages = twitter.getDirectMessages(paging);
                messages = mTwitter.getDirectMessages(paging);
                for (DirectMessage message : messages) {
                   /* System.out.println("From: @" + message.getSenderScreenName() + " id:" + message.getId() + " - "
                            + message.getText()); */

               adapter.add(message.getText());
                }

                lv.setAdapter(adapter);

                paging.setPage(paging.getPage() + 1);
            } while (messages.size() > 0 && paging.getPage() < 10);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.exit(-1);
            }


    }

}