package com.example.btl_android.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.btl_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static int DATABASE_VERSION = 1;
    public UserSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "username TEXT," +
                "password TEXT," +
                "role TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    public List<User> getAll(){
        List<User> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("user", null, null, null, null, null, null);
        while (rs!=null && rs.moveToNext()){
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String username = rs.getString(2);
            String password = rs.getString(3);
            String role = rs.getString(4);
            list.add(new User(id, name, username, password, role));
        }
        return list;
    }
    public User getAccount(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = { username, password };
        Cursor rs = db.query("user", null, selection, selectionArgs, null, null, null);

        if (rs != null && rs.moveToFirst()) {
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String user = rs.getString(2);
            String pass = rs.getString(3);
            String role = rs.getString(4);
            rs.close();
            return new User(id, name, user, pass, role);
        }

        if (rs != null) {
            rs.close();
        }
        return null;
    }
    public User getUserById(String user_id) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "id = ?";
        String[] selectionArgs = { user_id };
        Cursor rs = db.query("user", null, selection, selectionArgs, null, null, null);

        if (rs != null && rs.moveToFirst()) {
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String user = rs.getString(2);
            String pass = rs.getString(3);
            String role = rs.getString(4);
            rs.close();
            return new User(id, name, user, null, role);
        }

        if (rs != null) {
            rs.close();
        }
        return null;
    }
    public boolean isExisted(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "username = ?";
        String[] selectionArgs = { username };
        Cursor rs = db.query("user", null, selection, selectionArgs, null, null, null);
        List<User> list = new ArrayList<>();
        if (rs != null && rs.moveToFirst()) {
            return true;
        }

        if (rs != null) {
            rs.close();
        }
        return false;
    }
    public long addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("role", user.getRole());

        long id = db.insert("user", null, values);
        db.close();
        return id;
    }
}
