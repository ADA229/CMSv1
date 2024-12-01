package com.example.cmsv1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cmsv1.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT, password TEXT)";
        db.execSQL(CREATE_USERS_TABLE);
        
        String CREATE_CREDIT_TABLE = "CREATE TABLE credit (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, amount REAL, description TEXT, date TEXT, FOREIGN KEY(user_id) REFERENCES users(id))";
        db.execSQL(CREATE_CREDIT_TABLE);

    
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS credit");
        onCreate(db);
    }
}