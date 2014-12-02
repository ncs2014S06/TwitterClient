package ncs2014.s06.twitterclient;

import twitter4j.Status;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;


//表示定義クラス
public class TweetAdapter extends ArrayAdapter<twitter4j.Status> {

	private LayoutInflater mInflater;

	public TweetAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1);
		mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自動生成されたメソッド・スタブ

		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_tweet, null);
		}

		Status item = getItem(position);
		SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.icon);
		icon.setImageUrl(item.getUser().getProfileImageURL());
		TextView name = (TextView) convertView.findViewById(R.id.name);
		name.setText(item.getUser().getName());
		TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
		screenName.setText("@" + item.getUser().getScreenName());
		TextView text = (TextView) convertView.findViewById(R.id.text);
		text.setText(item.getText());
		return convertView;
	}

}//TweetAdapter
