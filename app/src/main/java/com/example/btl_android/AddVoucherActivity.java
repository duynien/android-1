package com.example.btl_android;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.dal.VoucherSQLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddVoucherActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText startTime, startDate, endDate, title, code, percentage;
    private Button btnAdd, btnCancel;
    private VoucherSQLiteHelper voucherSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher);
        initView();
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        startTime.setOnClickListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
    }

    private void initView() {
        startTime = findViewById(R.id.start_time);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        title = findViewById(R.id.title);
        code = findViewById(R.id.code);
        percentage = findViewById(R.id.percentage);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        voucherSQLiteHelper = new VoucherSQLiteHelper(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        if (view == startTime) {
            final Calendar c = Calendar.getInstance();
            int gio = c.get(Calendar.HOUR_OF_DAY);
            int phut = c.get(Calendar.MINUTE);
            TimePickerDialog dialog = new TimePickerDialog(AddVoucherActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String time = hourOfDay + ":" + minute;
                            if (time.charAt(1) == ':') time = "0" + time;
                            if (time.length() < 5) {
                                time = time.substring(0, 3) + "0" + time.substring(3);
                            }
                            startTime.setText(time);
                        }

                    }, gio, phut, true);
            dialog.show();
        }
        if (view == startDate) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(AddVoucherActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String date = "";
                            if (month > 8) {
                                date = dayOfMonth + "/" + (month + 1) + "/" + year;
                            } else {
                                date = dayOfMonth + "/0" + (month + 1) + "/" + year;
                            }
                            if (date.length() < 10) date = "0" + date;
                            startDate.setText(date);
                        }
                    }, year, month, day);
            dialog.show();
        }
        if (view == endDate) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(AddVoucherActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String date = "";
                            if (month > 8) {
                                date = dayOfMonth + "/" + (month + 1) + "/" + year;
                            } else {
                                date = dayOfMonth + "/0" + (month + 1) + "/" + year;
                            }
                            if (date.length() < 10) date = "0" + date;
                            endDate.setText(date);
                        }
                    }, year, month, day);
            dialog.show();
        }
        if (view == btnCancel) {
            finish();
        }
        if (view == btnAdd) {
            String code1 = code.getText().toString();
            String title1 = title.getText().toString();
            String percentage1 = percentage.getText().toString();
            String start = startTime.getText().toString() + " " + startDate.getText().toString();
            String end = endDate.getText().toString();
            if (code1.isEmpty() || title1.isEmpty() || startTime.getText().toString()
                    .isEmpty() || startDate.getText().toString().isEmpty() || end.isEmpty()) {
                Toast.makeText(getApplicationContext(), "All fields are required!",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (!isGreaterToday(
                        startTime.getText().toString() + " " + startDate.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Date required after today!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (isGreater(startDate.getText().toString(), end)) {
                        if (voucherSQLiteHelper.getVoucherByCode(code1.trim()) != null) {
                            Toast.makeText(getApplicationContext(), "Voucher is already existed!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (Integer.parseInt(percentage1) < 1 || Integer.parseInt(
                                    percentage1) > 50) {
                                Toast.makeText(getApplicationContext(),
                                                "Percentage must be in range 0 to 50!", Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                voucherSQLiteHelper.addVoucher(code1, title1, percentage1, start,
                                        end);
                                Toast.makeText(getApplicationContext(), "Voucher added!",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "End date and start date are invalid!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private boolean isGreaterToday(String s2) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            Date currentDate = new Date();
            Date date = format.parse(s2);
            return date.compareTo(currentDate) >= 0;
        } catch (ParseException e) {

        }
        return false;
    }

    private boolean isGreater(String s1, String s2) {
        String[] ss1 = s1.split("/");
        String[] ss2 = s2.split("/");
        if (Integer.parseInt(ss2[2]) > Integer.parseInt(ss1[2])) {
            return true;
        } else if (Integer.parseInt(ss2[2]) < Integer.parseInt(ss1[2])) {
            return false;
        } else {
            if (Integer.parseInt(ss2[1]) > Integer.parseInt(ss1[1])) {
                return true;
            } else if (Integer.parseInt(ss2[1]) < Integer.parseInt(ss1[1])) {
                return false;
            } else {
                return Integer.parseInt(ss2[0]) > Integer.parseInt(ss1[0]);
            }
        }
    }
}