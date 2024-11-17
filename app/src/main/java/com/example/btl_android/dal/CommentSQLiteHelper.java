package com.example.btl_android.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.btl_android.model.Comment;
import com.example.btl_android.model.Order;
import com.example.btl_android.model.Product;
import com.example.btl_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommentSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "comment.db";
    private static int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "comment";
    private ProductSQLiteHelper productSQLiteHelper;
    private UserSQLiteHelper userSQLiteHelper;
    private Context context;

    public CommentSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE comment (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id TEXT," +
                "product_id TEXT," +
                "rating TEXT," +
                "comment TEXT," +
                "date TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Handle database upgrade as needed
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    public List<Comment> getCommentByProductId(String product_id){
        List<Comment> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String order = "date DESC";
        Cursor rs = st.query(TABLE_NAME, null, "product_id = ?", new String[]{product_id}, null, null, order);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String user_id = rs.getString(1);
            String rating = rs.getString(3);
            String comment = rs.getString(4);
            String date = rs.getString(5);
            userSQLiteHelper = new UserSQLiteHelper(context);
            productSQLiteHelper = new ProductSQLiteHelper(context);
            User user = userSQLiteHelper.getUserById(user_id);
            Product product = productSQLiteHelper.getProductById(Integer.parseInt(product_id));
            list.add(new Comment(id, user, product, Integer.parseInt(rating), comment, date));
        }
        if (rs != null) {
            rs.close();
        }
        return list;
    }
    public List<Comment> getAll(){
        List<Comment> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query(TABLE_NAME, null, null, null, null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String user_id = rs.getString(1);
            String product_id = rs.getString(2);
            String rating = rs.getString(3);
            String comment = rs.getString(4);
            String date = rs.getString(5);
            userSQLiteHelper = new UserSQLiteHelper(context);
            productSQLiteHelper = new ProductSQLiteHelper(context);
            User user = userSQLiteHelper.getUserById(user_id);
            Product product = productSQLiteHelper.getProductById(Integer.parseInt(product_id));
            list.add(new Comment(id, user, product, Integer.parseInt(rating), comment, date));
        }
        if (rs != null) {
            rs.close();
        }
        return list;
    }
    public boolean commented(String product_id, String user_id){
        List<Comment> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String order = "date DESC";
        Cursor rs = st.query(TABLE_NAME, null, "product_id = ? and user_id = ?", new String[]{product_id, user_id}, null, null, null);
        if(rs != null && rs.moveToNext()){
            return true;
        }
        return false;
    }
    public void addComment(String user_id, String product_id, String rating, String comment, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", user_id);
        values.put("product_id", product_id);
        values.put("rating", rating);
        values.put("comment", comment);
        values.put("date", date);
        db.insert(TABLE_NAME, null, values);
    }
    public void deleteComment(int id) {
        SQLiteDatabase st = getWritableDatabase();
        st.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }

}
