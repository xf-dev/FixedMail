package xf.fixedmail;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, "comicnote.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS MAIL_INFO (");
		sql.append("id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
		sql.append("name TEXT NOT NULL,");
		sql.append("address TEXT NOT NULL,");
		sql.append("title TEXT NOT NULL,");
		sql.append("body TEXT NOT NULL");
		sql.append(")");
		db.execSQL(sql.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

}
