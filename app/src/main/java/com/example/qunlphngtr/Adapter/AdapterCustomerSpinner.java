package com.example.qunlphngtr.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.R;

import java.util.List;

public class AdapterCustomerSpinner extends BaseAdapter {
    List<Customer> customerList;
    Context context;
    int layout;

    public AdapterCustomerSpinner(List<Customer> customerList, Context context, int layout) {
        this.customerList = customerList;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return customerList.size();
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
        TextView textView=convertView.findViewById(R.id.tvnamecustomer);
        ImageView imageView=convertView.findViewById(R.id.imgcustomer);
        textView.setText(customerList.get(position).getCustomerName());
        imageView.setImageBitmap(LoadingImg(customerList.get(position).getCustomerImage()));
        return convertView;
    }
    private Bitmap LoadingImg(byte[] arrimg){
        return BitmapFactory.decodeByteArray(arrimg, 0, arrimg.length);
    }
}
