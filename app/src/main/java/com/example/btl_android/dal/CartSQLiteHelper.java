package com.example.btl_android.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.btl_android.model.Cart;
import com.example.btl_android.model.Product;
import com.example.btl_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class CartSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cart.db";
    private static int DATABASE_VERSION = 1;
    private ProductSQLiteHelper productSQLiteHelper;
    private UserSQLiteHelper userSQLiteHelper;
    private Context context;

    public CartSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product TEXT," +
                "user TEXT," +
                "quantity TEXT)";
        db.execSQL(sql);
        System.out.println("dsaj");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Handle database upgrade as needed
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public List<Cart> getCart(String user_id){
        List<Cart> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("cart", null, "user = ?", new String[]{user_id}, null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String product_id = rs.getString(1);
            String quantity = rs.getString(3);
            productSQLiteHelper = new ProductSQLiteHelper(context);
            userSQLiteHelper = new UserSQLiteHelper(context);
            Product product = productSQLiteHelper.getProductById(Integer.parseInt(product_id));
            User user = userSQLiteHelper.getUserById(user_id);
            list.add(new Cart(id, product, user, Integer.parseInt(quantity)));
        }
        if (rs != null) {
            rs.close();
        }
        return list;
    }

    public Cart getCartProduct(String user_id, String product_id){
        List<Cart> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("cart", null, "user = ? and product = ?", new String[]{user_id, product_id}, null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String quantity = rs.getString(3);
            productSQLiteHelper = new ProductSQLiteHelper(context);
            userSQLiteHelper = new UserSQLiteHelper(context);
            Product product = productSQLiteHelper.getProductById(Integer.parseInt(product_id));
            User user = userSQLiteHelper.getUserById(user_id);
            return new Cart(id, product, user, Integer.parseInt(quantity));
        }
        if (rs != null) {
            rs.close();
        }
        return null;
    }
    public void addCart(String product_id, String user_id, String quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product", product_id);
        values.put("user", user_id);
        values.put("quantity", quantity);
        db.insert("cart", null, values);
    }
    public void updateCart(String cart_id, String product_id, String user_id, String quantity) {
        SQLiteDatabase st = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product", product_id);
        values.put("user", user_id);
        values.put("quantity", quantity);
        st.update("cart", values, "id = ?", new String[]{cart_id});
    }

    public void deleteCart(String user_id) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "user = ?";
        String[] whereArgs = {user_id};
        db.delete("cart", whereClause, whereArgs);
    }

    public void deleteCartById(int id) {
        SQLiteDatabase st = getWritableDatabase();
        st.delete("cart", "id = ?", new String[]{String.valueOf(id)});
    }

}
