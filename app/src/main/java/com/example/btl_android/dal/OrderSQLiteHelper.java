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
import com.example.btl_android.model.User;

import java.util.ArrayList;
import java.util.List;

public class OrderSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "orders.db";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "orders";
    private OrderProductSQLiteHelper orderProductSQLiteHelper;
    private UserSQLiteHelper userSQLiteHelper;
    private final Context context;

    public OrderSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE orders (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "user_id TEXT," + "total_price TEXT," + "mobile TEXT," + "address TEXT," + "status TEXT," + "date TEXT)";
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

    public List<Order> getOrderByUserId(String user_id) {
        List<Order> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query(TABLE_NAME, null, "user_id = ?", new String[]{user_id}, null, null,
                null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String total_price = rs.getString(2);
            String mobile = rs.getString(3);
            String address = rs.getString(4);
            String status = rs.getString(5);
            String date = rs.getString(6);
            orderProductSQLiteHelper = new OrderProductSQLiteHelper(context);
            userSQLiteHelper = new UserSQLiteHelper(context);
            ProductSQLiteHelper productSQLiteHelper = new ProductSQLiteHelper(context);
            List<OrderProduct> orderProducts = orderProductSQLiteHelper.getByOrderId(
                    String.valueOf(id));

            List<OrderProduct> items = new ArrayList<>();

            for (OrderProduct orderProduct : orderProducts) {
                if (orderProduct.getProduct() == null) {
                    continue;
                }

                Product product = productSQLiteHelper.getProductById(
                        orderProduct.getProduct().getId());
                items.add(new OrderProduct(product, orderProduct.getQuantity()));
            }

            User user = userSQLiteHelper.getUserById(user_id);
            if (user == null) {
                continue;
            }
            if (items.isEmpty()) {
                continue;
            }
            list.add(new Order(id, user, Integer.parseInt(total_price), items, mobile, address,
                    status, date));
        }
        if (rs != null) {
            rs.close();
        }
        return list;
    }

    public List<Order> getAll() {
        List<Order> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query(TABLE_NAME, null, "status = ?", new String[]{"notDone"}, null, null,
                null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String user_id = rs.getString(1);
            String total_price = rs.getString(2);
            String mobile = rs.getString(3);
            String address = rs.getString(4);
            String status = rs.getString(5);
            String date = rs.getString(6);
            orderProductSQLiteHelper = new OrderProductSQLiteHelper(context);
            userSQLiteHelper = new UserSQLiteHelper(context);
            ProductSQLiteHelper productSQLiteHelper = new ProductSQLiteHelper(context);
            List<OrderProduct> orderProducts = orderProductSQLiteHelper.getByOrderId(
                    String.valueOf(id));
            List<OrderProduct> items = new ArrayList<>();

            for (OrderProduct orderProduct : orderProducts) {
                if (orderProduct.getProduct() == null) {
                    continue;
                }
                Product product = productSQLiteHelper.getProductById(
                        orderProduct.getProduct().getId());
                items.add(new OrderProduct(product, orderProduct.getQuantity()));
            }

            User user = userSQLiteHelper.getUserById(user_id);
            if (user == null) {
                continue;
            }
            if (items.isEmpty()) {
                continue;
            }
            list.add(new Order(id, user, Integer.parseInt(total_price), orderProducts, mobile,
                    address, status, date));
        }
        if (rs != null) {
            rs.close();
        }
        return list;
    }

    public long addOrder(String user_id, String total_price, String mobile, String address, String status, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", user_id);
        values.put("total_price", total_price);
        values.put("mobile", mobile);
        values.put("address", address);
        values.put("status", status);
        values.put("date", date);
        long newRowId = db.insert(TABLE_NAME, null, values);
        return newRowId;
    }

    public void updateOrder(String order_id, String user_id, String total_price, String mobile, String address, String status, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", user_id);
        values.put("total_price", total_price);
        values.put("mobile", mobile);
        values.put("address", address);
        values.put("status", status);
        values.put("date", date);
        db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(order_id)});
    }

    public void deleteOrder(int id) {
        SQLiteDatabase st = getWritableDatabase();
        st.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }
}
