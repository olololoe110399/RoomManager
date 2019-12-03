package com.example.qunlphngtr.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunlphngtr.Database.CustomerDAO;
import com.example.qunlphngtr.Fragment.FragmentCustomer;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.R;

import java.util.List;

public class AdapterCustomer extends RecyclerView.Adapter<AdapterCustomer.ViewHolder> {
    List<Customer> customerList;
    Context context;
    CustomerDAO customerDAO;

    public AdapterCustomer(List<Customer> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;
        this.customerDAO = new CustomerDAO(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.Name.setText(customerList.get(position).getCustomerName());
        holder.Phone.setText(customerList.get(position).getCustomerPhone());
        if (!(customerList.get(position).getCustomerImage() == null)) {
            startNewAsyncTask(customerList.get(position).getCustomerImage(), holder);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // dialogdetail(position);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Lưu ý");
                builder.setMessage("Bạn có muốn xóa khách thuê \""+customerList.get(position).getCustomerName()+"\" này?");
                builder.setCancelable(false);
                builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        customerDAO.deleteCustomer(customerList.get(position).getCustomerID());
                        customerList.remove(position);
                        notifyDataSetChanged();
                        FragmentCustomer.checkCustomernull();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    private void dialogdetail(final int position) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_customer_detail);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        RelativeLayout btncancel;
        TextView id;
        EditText cmnd, phone, name;
        ImageView avatar;
        avatar = dialog.findViewById(R.id.avatar);
        id = dialog.findViewById(R.id.tvid);
        cmnd = dialog.findViewById(R.id.edtcmnd);
        phone = dialog.findViewById(R.id.edtphone);
        name = dialog.findViewById(R.id.edtname);
        cmnd.setText(customerList.get(position).getCustomerCMND() + "");
        id.setText(customerList.get(position).getCustomerID());
        phone.setText(customerList.get(position).getCustomerPhone());
        name.setText(customerList.get(position).getCustomerName());
        cmnd.setEnabled(false);
        phone.setEnabled(false);
        name.setEnabled(false);
        byte[] hinh = customerList.get(position).getCustomerImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
        avatar.setImageBitmap(bitmap);
        btncancel = dialog.findViewById(R.id.back);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Name, Phone;
        private ImageView Img, imgDelete;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.tvcustomername);
            Phone = itemView.findViewById(R.id.tvcustomerphone);
            Img = itemView.findViewById(R.id.imgcustomer);
            cardView = itemView.findViewById(R.id.cardView);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

    private class ImageAsynctask extends AsyncTask<Bitmap, Void, Bitmap> {
        private byte[] img;
        private ViewHolder holder;

        private ImageAsynctask(byte[] img, ViewHolder holder) {
            this.img = img;
            this.holder = holder;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            return BitmapFactory.decodeByteArray(this.img, 0, img.length);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            {
                holder.Img.setImageBitmap(bitmap);
            }
        }
    }

    public void startNewAsyncTask(byte[] image, ViewHolder
            holder) {
        ImageAsynctask asyncTask = new ImageAsynctask(image, holder);
        asyncTask.execute();
    }

}