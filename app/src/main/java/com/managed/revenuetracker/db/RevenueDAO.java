package com.managed.revenuetracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.managed.revenuetracker.to.Revenue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

//implementation of database operations
public class RevenueDAO extends RevenueDBDAO {

	private static final String WHERE_ID_EQUALS = DataBaseHelper.ID_COLUMN
			+ " =?";
	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.ENGLISH);

	public RevenueDAO(Context context) {
		super(context);
	}

	//	input and save fields into db
	public long save(Revenue revenue) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.PLATFORM_COLUMN, revenue.getPlatform());
		Log.d("date", revenue.getDate().getTime() + "");
		values.put(DataBaseHelper.REV_DATE, formatter.format(revenue.getDate()));
		values.put(DataBaseHelper.REV_AMT, revenue.getAmt());

		return database.insert(DataBaseHelper.REV_TABLE, null, values);
	}

	// update an existing db entry
	public long update(Revenue revenue) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.PLATFORM_COLUMN, revenue.getPlatform());
		values.put(DataBaseHelper.REV_DATE, formatter.format(revenue.getDate()));
		values.put(DataBaseHelper.REV_AMT, revenue.getAmt());

		long result = database.update(DataBaseHelper.REV_TABLE, values,
				WHERE_ID_EQUALS,
				new String[] { String.valueOf(revenue.getId()) });
		Log.d("Update Result:", "=" + result);
		return result;

	}

	// delete db entry currently implemented by a long click on the list view entry in activity_main/fragment_rev_list, used in
	public static int delete(Revenue revenue) {
		return database.delete(DataBaseHelper.REV_TABLE, WHERE_ID_EQUALS,
				new String[] { revenue.getId() + "" });
	}


	//USING query() method to move cursor through db entries
	public ArrayList<Revenue> getRevenues() {
		ArrayList<Revenue> revenues = new ArrayList<Revenue>();

		Cursor cursor = database.query(DataBaseHelper.REV_TABLE,
				new String[] { DataBaseHelper.ID_COLUMN,
						DataBaseHelper.PLATFORM_COLUMN,
						DataBaseHelper.REV_DATE,
						DataBaseHelper.REV_AMT }, null, null, null,
				null, null);

		while (cursor.moveToNext()) {
			Revenue revenue = new Revenue();
			revenue.setId(cursor.getInt(0));
			revenue.setPlatform(cursor.getString(1));
			try {
				revenue.setDate(formatter.parse(cursor.getString(2)));
			} catch (ParseException e) {
				revenue.setDate(null);
			}
			revenue.setAmt(cursor.getDouble(3));

			revenues.add(revenue);
		}
		return revenues;
	}
	
	//USING rawQuery() method
	/*public ArrayList<Employee> getEmployees() {
		ArrayList<Employee> employees = new ArrayList<Employee>();

		String sql = "SELECT " + DataBaseHelper.ID_COLUMN + ","
				+ DataBaseHelper.NAME_COLUMN + ","
				+ DataBaseHelper.EMPLOYEE_DOB + ","
				+ DataBaseHelper.EMPLOYEE_SALARY + " FROM "
				+ DataBaseHelper.EMPLOYEE_TABLE;

		Cursor cursor = database.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			Employee employee = new Employee();
			employee.setId(cursor.getInt(0));
			employee.setName(cursor.getString(1));
			try {
				employee.setDateOfBirth(formatter.parse(cursor.getString(2)));
			} catch (ParseException e) {
				employee.setDateOfBirth(null);
			}
			employee.setSalary(cursor.getDouble(3));

			employees.add(employee);
		}
		return employees;
	}*/
	
	//Retrieves a single employee record with the given id
	public Revenue getRevenue(long id) {
		Revenue revenue = null;

		String sql = "SELECT * FROM " + DataBaseHelper.REV_TABLE
				+ " WHERE " + DataBaseHelper.ID_COLUMN + " = ?";

		Cursor cursor = database.rawQuery(sql, new String[] { id + "" });

		if (cursor.moveToNext()) {
			revenue = new Revenue();
			revenue.setId(cursor.getInt(0));
			revenue.setPlatform(cursor.getString(1));
			try {
				revenue.setDate(formatter.parse(cursor.getString(2)));
			} catch (ParseException e) {
				revenue.setDate(null);
			}
			revenue.setAmt(cursor.getDouble(3));
		}
		return revenue;
	}
}
