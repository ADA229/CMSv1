package com.example.cmsv1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;
import java.util.ArrayList; // Add this import
import java.util.List; // Add this import

public class UserDao {
    private static final String TAG = "UserDao";
    private DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean registerUser(String name, String phone, String password) {
        Log.d(TAG, "Registering user: Name = " + name + ", Phone = " + phone);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("password", password);
        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    public Pair<Boolean, Integer> authenticateUser(String phone, String password) {
        Log.d(TAG, "Authenticating user with Phone = " + phone);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"id"}, "phone=? AND password=?", new String[]{phone, password}, null, null, null);
        boolean isAuthenticated = cursor.getCount() > 0;
        int userId = -1;
        if (isAuthenticated && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        db.close();
        return new Pair<>(isAuthenticated, userId);
    }

    public String[] getCreditDetails(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] creditDetails = new String[2];
        Cursor cursor = null;
        try {
            cursor = db.query("credit", new String[]{"amount", "description"}, "user_id=?", new String[]{String.valueOf(userId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                creditDetails[0] = cursor.getString(cursor.getColumnIndexOrThrow("amount"));
                creditDetails[1] = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return creditDetails;
    }

    public double getTotalAmount(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) as total FROM credit WHERE id=?", new String[]{String.valueOf(userId)});
        double totalAmount = 0;
        if (cursor != null && cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            cursor.close();
        }
        db.close();
        return totalAmount;
    }

    public Cursor getUserTransactions(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query("credit", new String[]{"amount", "description", "date"}, "id=?", new String[]{String.valueOf(userId)}, null, null, "date DESC");
    }

    public String getUserName(int userId) {
        if (userId == -1) {
            Log.e(TAG, "Invalid user ID: " + userId);
            return "userid -1";
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        String userName = null;

        try {
            cursor = db.query("users", new String[]{"name"}, "id=?", new String[]{String.valueOf(userId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                userName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                Log.d(TAG, "User name retrieved: " + userName);
            } else {
                Log.e(TAG, "No user found with ID: " + userId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving user name", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return userName;
    }

    public List<String> searchCustomers(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> customers = new ArrayList<>();
        Cursor cursor = db.query("users", new String[]{"name", "phone"}, "(name LIKE ? OR phone LIKE ?) AND id != 777", new String[]{"%" + query + "%", "%" + query + "%"}, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            customers.add(name + " (" + phone + ")");
        }
        cursor.close();
        db.close();
        return customers;
    }
}