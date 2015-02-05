package ncs2014.s06.twitterclient;

import twitter4j.UserList;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


//表示定義クラス
public class ListAdapter extends ArrayAdapter<UserList> {

	private LayoutInflater mInflater;

	public ListAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1);
		mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_list, null);
		}



		UserList item = getItem(position);
		TextView listName = (TextView) convertView.findViewById(R.id.list_name);
		listName.setText(item.getName());
		Log.d("list", item.getName());
		TextView listText = (TextView) convertView.findViewById(R.id.list_text);
		listText.setText(item.getDescription());
		Log.d("list", item.getDescription());
		return convertView;
	}

}//TweetAdapter
