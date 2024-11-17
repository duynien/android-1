package com.example.btl_android.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.btl_android.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class ProductSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "product.db";
    private static int DATABASE_VERSION = 1;

    public ProductSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE product (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "image TEXT," +
                "name TEXT," +
                "price TEXT," +
                "description TEXT," +
                "category TEXT)";
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

    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("product", null, null, null, null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String image = rs.getString(1);
            String name = rs.getString(2);
            String price = rs.getString(3);
            String description = rs.getString(4);
            String category = rs.getString(5);
            list.add(new Product(id, image, name, price, description, category));
        }
        Collections.reverse(list);
        return list;
    }

    public List<Product> getAllIncre() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String order = "CAST(price AS REAL) ASC";
        Cursor rs = st.query("product", null, null, null, null, null, order);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String image = rs.getString(1);
            String name = rs.getString(2);
            String price = rs.getString(3);
            String description = rs.getString(4);
            String category = rs.getString(5);
            list.add(new Product(id, image, name, price, description, category));
        }
        return list;
    }

    public List<Product> getAllDesc() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String order = "CAST(price AS REAL) DESC";
        Cursor rs = st.query("product", null, null, null, null, null, order);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String image = rs.getString(1);
            String name = rs.getString(2);
            String price = rs.getString(3);
            String description = rs.getString(4);
            String category = rs.getString(5);
            list.add(new Product(id, image, name, price, description, category));
        }
        return list;
    }

    public List<Product> getProductByName(String productName) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("product", null, "name LIKE ?", new String[]{"%" + productName + "%"}, null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String image = rs.getString(1);
            String name = rs.getString(2);
            String price = rs.getString(3);
            String description = rs.getString(4);
            String category = rs.getString(5);
            list.add(new Product(id, image, name, price, description, category));
        }
        if (rs != null) {
            rs.close();
        }
        return list;
    }
    public Product getProductById(int id) {
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("product", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while (rs != null && rs.moveToNext()) {
            int productId = rs.getInt(0);
            String image = rs.getString(1);
            String name = rs.getString(2);
            String price = rs.getString(3);
            String description = rs.getString(4);
            String category = rs.getString(5);
            return new Product(productId, image, name, price, description, category);
        }
        if (rs != null) {
            rs.close();
        }
        return null;
    }

    public List<Product> getProductByCategory(String category) {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("product", null, "category LIKE ?", new String[]{"%" + category + "%"}, null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String image = rs.getString(1);
            String name = rs.getString(2);
            String price = rs.getString(3);
            String description = rs.getString(4);
            String categoryText = rs.getString(5);
            list.add(new Product(id, image, name, price, description, categoryText));
        }
        if (rs != null) {
            rs.close();
        }
        return list;
    }

    public void addProduct(Product product) {
        SQLiteDatabase st = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", product.getImage());
        values.put("name", product.getName());
        values.put("price", product.getPrice());
        values.put("description", product.getDescription());
        values.put("category", product.getCategory());
        st.insert("product", null, values);
    }

    public void updateProduct(Product product) {
        SQLiteDatabase st = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", product.getImage());
        values.put("name", product.getName());
        values.put("price", product.getPrice());
        values.put("description", product.getDescription());
        values.put("category", product.getCategory());
        st.update("product", values, "id = ?", new String[]{String.valueOf(product.getId())});
    }

    public void deleteProduct(int id) {
        SQLiteDatabase st = getWritableDatabase();
        st.delete("product", "id = ?", new String[]{String.valueOf(id)});
    }
}
