package com.example.btl_android.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.MyReceiver;
import com.example.btl_android.R;
import com.example.btl_android.dal.VoucherSQLiteHelper;
import com.example.btl_android.model.Voucher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecycleVoucherAdapter extends RecyclerView.Adapter<RecycleVoucherAdapter.VoucherViewHolder> {
    private List<Voucher> list;
    private Context context;

    public RecycleVoucherAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = list.get(position);
        holder.name.setText(voucher.getTitle());
        holder.percentage.setText("Giam: " + voucher.getPercentage() + "%");
        holder.code.setText(voucher.getCode());
        holder.start.setText("Start: " + voucher.getStart());
        holder.end.setText("End: " + voucher.getEnd());
        holder.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipboard(voucher.getCode());
                Toast.makeText(context, "Voucher code copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        if(isGreaterToday(voucher.getStart())){
            holder.btnAlarm.setVisibility(View.VISIBLE);
        } else {
            holder.btnAlarm.setVisibility(View.GONE);
        }
        holder.btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start1 = voucher.getStart();
                String[] a1 = start1.split("\\s+");
                if(isToday(a1[1])){
                    String[] data = a1[0].split(":");
                    int gio = Integer.parseInt(data[0]);
                    int phut = Integer.parseInt(data[1]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY,gio);
                    calendar.set(Calendar.MINUTE,phut);
                    calendar.set(Calendar.SECOND,0);

                    AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(context.ALARM_SERVICE);

                    Intent intent = new Intent(context, MyReceiver.class);
                    intent.setAction("myAction");
                    intent.putExtra("Title","Voucher " + voucher.getTitle() + " đã sẵn sàng!");
                    intent.putExtra("Description", "Giảm ngay " + voucher.getPercentage() + " %, đừng bỏ lỡ!");
                    intent.putExtra("time",gio+":"+phut);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            0, intent, PendingIntent.FLAG_IMMUTABLE);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    Toast.makeText(context, "You've add notification about this voucher!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void setList(List<Voucher> list) {
        this.list = list;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VoucherViewHolder extends RecyclerView.ViewHolder {
        private TextView name, percentage, code, start, end;
        private Button btnCopy, btnAlarm;

        public VoucherViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);
            percentage = view.findViewById(R.id.percentage);
            code = view.findViewById(R.id.code);
            start = view.findViewById(R.id.start);
            end = view.findViewById(R.id.end);
            btnCopy = view.findViewById(R.id.btnCopy);
            btnAlarm = view.findViewById(R.id.btnAlarm);
        }
    }
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("code", text);
        clipboard.setPrimaryClip(clip);
    }
    private boolean isToday(String s2) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date currentDate = new Date();
            String s1 = format.format(currentDate);
            return s1.equalsIgnoreCase(s2);
        } catch (Exception e){

        }
        return false;
    }
    private boolean isGreaterToday(String s2) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            Date currentDate = new Date();
            Date date = format.parse(s2);
            return date.compareTo(currentDate) >= 0;
        } catch (ParseException e){

        }
        return false;
    }
}
