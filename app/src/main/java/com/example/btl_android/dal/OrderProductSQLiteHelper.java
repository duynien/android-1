package com.example.btl_android.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.btl_android.model.Order;
import com.example.btl_android.model.OrderProduct;
import com.example.btl_android.model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderProductSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "order_product.db";
    private static int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "order_product";
    private ProductSQLiteHelper productSQLiteHelper;
    private Context context;

    public OrderProductSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE order_product (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id TEXT," +
                "product_id TEXT," +
                "quantity TEXT)";
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

    public List<OrderProduct> getByOrderId(String order_id){
        List<OrderProduct> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query(TABLE_NAME, null, "order_id = ?", new String[]{order_id}, null, null, null);
        while (rs != null && rs.moveToNext()) {
            String product_id = rs.getString(2);
            String quantity = rs.getString(3);
            productSQLiteHelper = new ProductSQLiteHelper(context);
            Product product = productSQLiteHelper.getProductById(Integer.parseInt(product_id));
            list.add(new OrderProduct(product, Integer.parseInt(quantity)));
        }
        if (rs != null) {
            rs.close();
        }
        return list;
    }
    public void addOrderProduct(String order_id, String product_id, String quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_id", order_id);
        values.put("product_id", product_id);
        values.put("quantity", quantity);
        db.insert(TABLE_NAME, null, values);
    }
}
