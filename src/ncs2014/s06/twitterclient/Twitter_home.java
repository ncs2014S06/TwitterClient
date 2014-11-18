package ncs2014.s06.twitterclient;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

public class Twitter_home extends Activity implements OnClickListener{

	//メニューアイテム識別ID
		private static final int MENU_A = 0;
		private static final int MENU_B = 1;
		private static final int MENU_C = 2;
		//ViewFliper
		ViewFlipper viewFlipper;
		//アニメーション
		Animation inFromRightAnimation;
		Animation inFromLeftAnimation;
		Animation outToRightAnimation;
		Animation outToLeftAnimation;
		//Button
		Button bt1_to2;
		Button bt2_to3;
		Button bt3_to1;





		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.twitterhome);

			//findView
			ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper);
			bt1_to2 = (Button) findViewById(R.id.bt1_to2);
			bt2_to3 = (Button) findViewById(R.id.bt2_to3);
			bt3_to1 = (Button) findViewById(R.id.bt3_to1);

			//リスナー追加
			bt1_to2.setOnClickListener(this);
			bt2_to3.setOnClickListener(this);
			bt3_to1.setOnClickListener(this);


			//animation
			inFromRightAnimation=AnimationUtils.loadAnimation(this,R.anim.right_in);
			inFromLeftAnimation=AnimationUtils.loadAnimation(this,R.anim.left_in);
			outToRightAnimation=AnimationUtils.loadAnimation(this,R.anim.right_out);
			outToLeftAnimation=AnimationUtils.loadAnimation(this,R.anim.left_out);

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
			ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper);
			Animation slideInFromLeft = AnimationUtils.loadAnimation(this,R.anim.left_in);
		    Animation slideInFromRight = AnimationUtils.loadAnimation(this,R.anim.right_out);
		    flipper.setInAnimation(slideInFromLeft);
		    flipper.setInAnimation(slideInFromRight);

			if(v == bt1_to2){
			    flipper.showNext(); //次のページへ
			}//if

			if(v == bt2_to3){
			    flipper.showNext(); //次のページへ
			}//if

			if(v == bt3_to1){
			    flipper.showNext(); //次のページへ
			}//if




		}





}
