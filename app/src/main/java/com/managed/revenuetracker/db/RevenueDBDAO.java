package com.managed.revenuetracker.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

//creation of database object that can be extended by RevenueDAO for CRUD functionality
public class RevenueDBDAO {

	protected static SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	private Context mContext;

	public RevenueDBDAO(Context context) {
		this.mContext = context;
		dbHelper = DataBaseHelper.getHelper(mContext);
		open();
		
	}

	public void open() throws SQLException {
		if(dbHelper == null)
			dbHelper = DataBaseHelper.getHelper(mContext);
		database = dbHelper.getWritableDatabase();
	}
	
	/*public void close() {
		dbHelper.close();
		database = null;
	}*/
}
