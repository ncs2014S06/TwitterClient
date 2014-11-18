package ncs2014.s06.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class Twitter_home extends ActionBarActivity {

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





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitterhome);
	}//onCreate

	/**
	 * XMLとの関連付け
	 */
	protected void findView() {
		ViewFlipper filipper = (ViewFlipper)findViewById(R.id.flipper);
	}

	/**
	 * リスナーセット
	 */
	protected void setListener() {

	}//setListener

	/**
	 * アニメーションセット
	 */
	protected void setAnimations() {
		inFromRightAnimation=AnimationUtils.loadAnimation(this,R.anim.right_in);
		inFromLeftAnimation=AnimationUtils.loadAnimation(this,R.anim.left_in);
		outToRightAnimation=AnimationUtils.loadAnimation(this,R.anim.right_out);





	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		//メニューアイテム追加
		menu.add(Menu.NONE,MENU_A,Menu.NONE,"まっさｎ");
		menu.add(Menu.NONE,MENU_B,Menu.NONE,"ユーザ画面");
		menu.add(Menu.NONE,MENU_C,Menu.NONE,"設定");
		return true;
	}

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
		ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper);

		switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				Animation slideInFromLeft = AnimationUtils.loadAnimation(this,R.anim.left_in);
			    Animation slideInFromRight = AnimationUtils.loadAnimation(this,R.anim.right_out);
			    flipper.setInAnimation(slideInFromLeft);
			    flipper.setInAnimation(slideInFromRight);
			    flipper.showNext(); //次のページへ
			break;

			default:
				break;
		}


	    //Log.d("TouchEvent", "X:" + event.getX() + ",Y:" + event.getY());
		return true;
    }




}
