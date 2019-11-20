package com.example.qunlphngtr.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.Model.Service;
import com.example.qunlphngtr.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AdapterBillService extends BaseAdapter {
    List<Service> serviceList;
    Context context;
    int layout;

    public AdapterBillService(List<Service> serviceList, Context context, int layout) {
        this.serviceList = serviceList;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return serviceList.size();
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
        NumberFormat formatter = new DecimalFormat("#,###");
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(layout,null);
        TextView textView=convertView.findViewById(R.id.tvservicename);
        TextView textView1=convertView.findViewById(R.id.tvserviceprice);
        textView.setText(serviceList.get(position).getServiceName());
        textView1.setText(formatter.format(serviceList.get(position).getServicePrice())+ "VND");
        return convertView;
    }
}
