package ncs2014.s06.twitterclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageGet extends Activity{
	private ImageView view;
	private int id;
	private String name;
	private Twitter mTwitter;


	public ImageGet(ImageView view,int id,String name,Twitter mTwitter) {
		this.view =view;
		this.id = id;
		this.name = name;
		this.mTwitter = mTwitter;
	}

	public void imageSet() {

		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					User user = mTwitter.showUser(name);
					URL imageURL = new URL(user.getProfileImageURL());
					Bitmap profile = null;
					view = (ImageView) findViewById(id);
					try {
						profile = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
						if(profile != null){
							view.setImageBitmap(profile);
						}
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						view.setId(R.drawable.ic_launcher);
					}
				} catch (MalformedURLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
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
