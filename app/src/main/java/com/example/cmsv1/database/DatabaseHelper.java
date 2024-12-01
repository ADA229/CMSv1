package com.example.cmsv1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cmsv1.db";
    private static final int DATABASE_VERSION = 1; // Ensure this value hasn't changed

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, phone TEXT, password TEXT)";
        db.execSQL(CREATE_USERS_TABLE);
        
        String CREATE_CREDIT_TABLE = "CREATE TABLE credit (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, amount REAL, description TEXT, date TEXT, FOREIGN KEY(user_id) REFERENCES users(id))";
        db.execSQL(CREATE_CREDIT_TABLE);

        // Insert hardcoded shopkeeper entry
        String INSERT_SHOPKEEPER = "INSERT INTO users (id, name, phone, password) VALUES (777, 'shopkeeper', '777', 'pass')";
        db.execSQL(INSERT_SHOPKEEPER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Comment out the lines that drop tables to prevent data loss
        // db.execSQL("DROP TABLE IF EXISTS users");
        // db.execSQL("DROP TABLE IF EXISTS credit");
        // onCreate(db);
    }
}

// schema for SQLite Database referenced in the code snippet above
// -- Schema for SQLite Database

// -- Users Table
// CREATE TABLE users (
//     id INTEGER PRIMARY KEY,
//     name TEXT NOT NULL,
//     phone TEXT NOT NULL,
//     password TEXT NOT NULL
// );

// -- Credit Table
// CREATE TABLE credit (
//     id INTEGER PRIMARY KEY AUTOINCREMENT,
//     user_id INTEGER NOT NULL,
//     amount REAL NOT NULL,
//     description TEXT,
//     date TEXT NOT NULL,
//     FOREIGN KEY(user_id) REFERENCES users(id)
// );
