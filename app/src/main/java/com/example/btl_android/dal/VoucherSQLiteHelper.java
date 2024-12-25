package com.example.btl_android.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.btl_android.model.Voucher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoucherSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "voucher.db";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "voucher";

    public VoucherSQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE voucher (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + "code TEXT," + "title TEXT," + "percentage TEXT," + "start TEXT," + "ends TEXT)";
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

    public List<Voucher> getAllVoucher() {
        List<Voucher> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String order = "start DESC";
        Cursor rs = st.query(TABLE_NAME, null, null, null, null, null, order);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String code = rs.getString(1);
            String title = rs.getString(2);
            String percentage = rs.getString(3);
            String start = rs.getString(4);
            String ends = rs.getString(5);
            if (isAvailable(ends)) {
                list.add(new Voucher(id, code, title, percentage, start, ends));
            }
        }
        if (rs != null) {
            rs.close();
        }
        return list;
    }

    public Voucher getVoucherByCode(String code) {
        SQLiteDatabase st = getReadableDatabase();

        Cursor rs = st.query(TABLE_NAME, null, "code = ?", new String[]{code}, null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String title = rs.getString(2);
            String percentage = rs.getString(3);
            String start = rs.getString(4);
            String ends = rs.getString(5);
            return new Voucher(id, code, title, percentage, start, ends);
        }
        if (rs != null) {
            rs.close();
        }
        return null;
    }

    public void addVoucher(String code, String title, String percentage, String start, String ends) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("code", code);
        values.put("title", title);
        values.put("percentage", percentage);
        values.put("start", start);
        values.put("ends", ends);
        db.insert(TABLE_NAME, null, values);
    }

    public void deleteVoucher(int id) {
        SQLiteDatabase st = getWritableDatabase();
        st.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }

    private boolean isAvailable(String s2) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            s2 = "23:59 " + s2;
            Date currentDate = new Date();
            Date date2 = format.parse(s2);
            return currentDate.compareTo(date2) <= 0;
        } catch (ParseException e) {

        }
        return false;
    }
}
