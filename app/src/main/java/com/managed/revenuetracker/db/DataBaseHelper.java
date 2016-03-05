package com.managed.revenuetracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// main database helper to set up sqlite db table columns and datatypes. manages creation and version management
public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "revenuedb";
	private static final int DATABASE_VERSION = 1;

	public static final String REV_TABLE = "revenue";

	public static final String ID_COLUMN = "id";
	public static final String PLATFORM_COLUMN = "platform";
	public static final String REV_DATE = "date";
	public static final String REV_AMT = "amount";

	public static final String CREATE_REV_TABLE = "CREATE TABLE "
			+ REV_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY, "
			+ PLATFORM_COLUMN + " TEXT, " + REV_AMT + " DOUBLE, "
			+ REV_DATE + " DATE" + ")";

	private static DataBaseHelper instance;

	public static synchronized DataBaseHelper getHelper(Context context) {
		if (instance == null)
			instance = new DataBaseHelper(context);
		return instance;
	}

	private DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_REV_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
