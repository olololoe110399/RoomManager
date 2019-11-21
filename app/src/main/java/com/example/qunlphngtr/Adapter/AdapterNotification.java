package com.example.qunlphngtr.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qunlphngtr.Model.Notification;
import com.example.qunlphngtr.Model.Service;
import com.example.qunlphngtr.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AdapterNotification extends BaseAdapter {
    List<Notification> notificationList;
    Context context;
    int layout;

    public AdapterNotification(List<Notification> notificationList, Context context, int layout) {
        this.notificationList = notificationList;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(layout,null);
        TextView textView=convertView.findViewById(R.id.tvmessage);
        textView.setText(notificationList.get(position).getMessage());
        return convertView;
    }
}
