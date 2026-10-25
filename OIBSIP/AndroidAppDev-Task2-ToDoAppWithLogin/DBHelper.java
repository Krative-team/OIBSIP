package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.security.MessageDigest;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "todo.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context ctx) { super(ctx, DB_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT)");
        db.execSQL("CREATE TABLE tasks(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, title TEXT, notes TEXT, is_done INTEGER DEFAULT 0, FOREIGN KEY(user_id) REFERENCES users(id))");
    }
    @Override public void onUpgrade(SQLiteDatabase db, int o, int n) {
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // Basic Hashing - SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { return password; }
    }

    public long registerUser(String name, String email, String pass) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("email", email);
        cv.put("password", hashPassword(pass));
        return db.insert("users", null, cv);
    }

    public int loginUser(String email, String pass) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM users WHERE email=? AND password=?", 
            new String[]{email, hashPassword(pass)});
        if (c.moveToFirst()) { int id = c.getInt(0); c.close(); return id; }
        c.close(); return -1;
    }

    public long addTask(int userId, String title, String notes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_id", userId); cv.put("title", title); cv.put("notes", notes);
        return db.insert("tasks", null, cv);
    }

    public ArrayList<Task> getTasks(int userId) {
        ArrayList<Task> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM tasks WHERE user_id=? ORDER BY id DESC", new String[]{String.valueOf(userId)});
        while (c.moveToNext()) {
            list.add(new Task(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getInt(4)==1));
        }
        c.close(); return list;
    }

    public void updateTaskStatus(int taskId, boolean done) {
        ContentValues cv = new ContentValues();
        cv.put("is_done", done ? 1 : 0);
        getWritableDatabase().update("tasks", cv, "id=?", new String[]{String.valueOf(taskId)});
    }

    public void deleteTask(int taskId) {
        getWritableDatabase().delete("tasks", "id=?", new String[]{String.valueOf(taskId)});
    }

    public static class Task {
        public int id, userId; public String title, notes; public boolean isDone;
        public Task(int id, int userId, String title, String notes, boolean isDone) {
            this.id=id; this.userId=userId; this.title=title; this.notes=notes; this.isDone=isDone;
        }
    }
}
