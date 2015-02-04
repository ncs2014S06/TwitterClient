package ncs2014.s06.twitterclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	static final String DATABASE_NAME = "oauth_account.db";
	static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "account";

	public static final String ID = "ID";
	public static final String ACCESSTOKEN = "ACCESSTOKEN";
	public static final String ACCESSTOKENSECRET = "ACCESSTOKENSECRET";


	protected final Context context;
	protected DatabaseHelper dbHelper;
	protected SQLiteDatabase db;

	public DBAdapter(Context context){
		this.context = context;
		dbHelper = new DatabaseHelper(this.context);
	}

	//
	// SQLiteOpenHelper
	//

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(
				"CREATE TABLE " + TABLE_NAME + " ("
				+ ID + " INTEGER PRIMARY KEY,"
				+ ACCESSTOKEN + " TEXT NOT NULL,"
				+ ACCESSTOKENSECRET + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(
			SQLiteDatabase db,
			int oldVersion,
			int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}

	//
	// Adapter Methods
	//

	public DBAdapter open() {
		db = dbHelper.getWritableDatabase();
		return this;
	}


	public void close(){
		dbHelper.close();
	}


	//
	// App Methods
	//


	public boolean deleteAllAccount(){
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	public boolean deleteAccount(int id){
		return db.delete(TABLE_NAME, ID + "=" + id, null) > 0;
	}

	public Cursor getAllAccount(){
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}

	public Cursor getAccount(Long id){
		return db.query(TABLE_NAME, null, "ID = " + id, null, null, null, null);
	}

	public void saveAccount(Long id,String accessToken,String accessTokenSecret){
		ContentValues values = new ContentValues();
		values.put(ID, id);
		Log.d("db",id + "");
		values.put(ACCESSTOKEN, accessToken);
		Log.d("db",accessToken);
		values.put(ACCESSTOKENSECRET, accessTokenSecret);
		Log.d("db",accessTokenSecret);
		Log.d("db", db.insertOrThrow(TABLE_NAME, null, values) + "");
	}
}