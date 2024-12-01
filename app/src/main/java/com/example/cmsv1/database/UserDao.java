
package com.example.cmsv1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
    private DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean registerUser(String name, String phone, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("password", password);
        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    public boolean authenticateUser(String phone, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "phone=? AND password=?", new String[]{phone, password}, null, null, null);
        boolean isAuthenticated = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isAuthenticated;
    }
}