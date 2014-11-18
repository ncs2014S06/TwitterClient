package ncs2014.s06.twitterclient;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Twitter_home extends Activity implements OnClickListener{

	//メニューアイテム識別ID
	private static final int MENU_A = 0;
	private static final int MENU_B = 1;
	private static final int MENU_C = 2;
	//変数
	private Button bt1;
	private Button bt2;
	//intent
	Intent intent = new Intent();


		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.twitter_home);


			//findview
			bt1 = (Button) findViewById(R.id.bt1);
			bt2 = (Button) findViewById(R.id.bt2);

			//リスナー
			bt1.setOnClickListener(this);
			bt2.setOnClickListener(this);

		}//onCreate

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			//押したときの処理
			switch (item.getItemId()) {
				case MENU_A:
				return true;

				case MENU_B:
					startActivity(new Intent(
							Twitter_home.this,
							Twitter_user.class)
					);
				return true;

				case MENU_C:
				return true;

				default:
				break;
			}
			return false;
		}//select


		/**
		 * 画面がタッチされた時の動き
		 */
		@Override
	    public boolean onTouchEvent(MotionEvent event) {
			return true;
	    }

		@Override
		public void onClick(View v) {
			//ユーザ画面
			if(v == bt1){
				intent.setClass(getApplicationContext(), Twitter_user.class);
				startActivity(intent);
				overridePendingTransition(R.anim.right_in, R.anim.left_in);
			}//if

			//ツイート画面
			if(v == bt2){
				intent.setClass(getApplicationContext(), Twitter_tuito.class);
				startActivity(intent);
				overridePendingTransition(R.anim.right_in, R.anim.left_in);

			}//if



		}





}
