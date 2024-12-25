package com.example.btl_android.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.btl_android.model.User;
import com.example.btl_android.util.HashUtil;

import java.util.ArrayList;
import java.util.List;

public class UserSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    public UserSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table with columns for user data
        String sql = "CREATE TABLE user (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, " + "username TEXT, " + "password TEXT, " + "role TEXT, " + "email TEXT, " + "phone TEXT, " + "salt TEXT)";
        db.execSQL(sql);

        // Generate a random salt for the admin user
        String password = "123456aA@"; // admin password
        String salt = HashUtil.generateSalt();
        try {
            String hashedPassword = HashUtil.hashPasswordWithSalt(password, salt);
            // Insert admin record with hashed password and salt
            String insertAdminSQL = "INSERT INTO user (name, username, password, role, email, phone, salt) " + "VALUES ('Admin', 'admin', ?, 'admin', 'duynien01668611460@gmail.com', '1234567890', ?)";

            // Use parameterized queries to avoid SQL injection
            SQLiteStatement statement = db.compileStatement(insertAdminSQL);
            statement.bindString(1, hashedPassword); // admin password (hashed)
            statement.bindString(2, salt); // generated salt
            statement.executeInsert();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("user", null, null, null, null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String username = rs.getString(2);
            String password = rs.getString(3);
            String role = rs.getString(4);
            String email = rs.getString(5);
            String phone = rs.getString(6);
            String salt = rs.getString(7);
            list.add(new User(id, name, username, password, role, email, phone, salt));
        }
        if (rs != null) {
            rs.close();
        }
        return list;
    }

    public User getAccount(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "username = ?";
        String[] selectionArgs = {username};
        Cursor rs = db.query("user", null, selection, selectionArgs, null, null, null);

        if (rs != null && rs.moveToFirst()) {
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String user = rs.getString(2);
            String storedPassword = rs.getString(3); // stored hashed password
            String salt = rs.getString(7);  // retrieve salt
            String role = rs.getString(4);
            String email = rs.getString(5);
            String phone = rs.getString(6);
            rs.close();

            // Hash the input password with the retrieved salt
            try {
                // Hash the input password with the stored salt
                String hashedInputPassword = HashUtil.hashPasswordWithSalt(password, salt);

                // Compare the hashed input password with the stored password
                if (hashedInputPassword.equals(storedPassword)) {
                    return new User(id, name, user, storedPassword, role, email, phone, salt);
                } else {
                    Log.d("password input: ", password);
                    Log.d("Login", "Password mismatch");
                    Log.d("Login", "Stored Salt: " + salt);
                    Log.d("Login", "Hashed Input Password: " + hashedInputPassword);
                    Log.d("Login", "Stored Hashed Password: " + storedPassword);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (rs != null) {
            rs.close();
        }
        return null;
    }


    public User getUserById(String user_id) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "id = ?";
        String[] selectionArgs = {user_id};
        Cursor rs = db.query("user", null, selection, selectionArgs, null, null, null);

        if (rs != null && rs.moveToFirst()) {
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String user = rs.getString(2);
            String role = rs.getString(4);
            String email = rs.getString(5);
            String phone = rs.getString(6);
            String salt = rs.getString(7);
            rs.close();
            return new User(id, name, user, null, role, email, phone, salt);
        }

        if (rs != null) {
            rs.close();
        }
        return null;
    }

    public boolean isExisted(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "username = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query("user", null, selection, selectionArgs, null, null, null);
        boolean exists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "email = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query("user", null, selection, selectionArgs, null, null, null);
        boolean exists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "email = ?";
        String[] selectionArgs = {email};
        Cursor rs = db.query("user", null, selection, selectionArgs, null, null, null);

        if (rs != null && rs.moveToFirst()) {
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String user = rs.getString(2);
            String role = rs.getString(4);
            String phone = rs.getString(6);
            String salt = rs.getString(7);
            rs.close();
            return new User(id, name, user, null, role, email, phone, salt);
        }

        if (rs != null) {
            rs.close();
        }
        return null;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "username = ?";
        String[] selectionArgs = {username};
        Cursor rs = db.query("user", null, selection, selectionArgs, null, null, null);

        if (rs != null && rs.moveToFirst()) {
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String user = rs.getString(2);
            String role = rs.getString(4);
            String email = rs.getString(5);
            String phone = rs.getString(6);
            String salt = rs.getString(7);
            rs.close();
            return new User(id, name, user, null, role, email, phone, salt);
        }

        if (rs != null) {
            rs.close();
        }
        return null;
    }

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());  // Store hashed password
        values.put("salt", user.getSalt());  // Store salt
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        values.put("role", user.getRole());
        db.insert("user", null, values);
        db.close();
    }

    public void updatePassword(String email, String s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", s);
        db.update("user", values, "email = ?", new String[]{email});
        db.close();
    }
    public void updateEmailUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        db.update("users", values, "username = ?", new String[]{user.getUsername()});
        db.close();
    }

    public void updatePhoneUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", user.getPhone());
        db.update("users", values, "username = ?", new String[]{user.getUsername()});
        db.close();
    }

    public void updateEmailAndPhoneUser(String email, String phone, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("phone", phone);
        db.update("users", values, "username = ?", new String[]{username});
        db.close();
    }
}
