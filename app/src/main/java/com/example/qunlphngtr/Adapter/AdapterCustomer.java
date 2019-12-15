package com.example.qunlphngtr.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Activities.UpdateCustomerActivity;
import com.example.qunlphngtr.Database.ContractDAO;
import com.example.qunlphngtr.Database.CustomerDAO;
import com.example.qunlphngtr.Fragment.FragmentCustomer;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.R;

import java.util.List;

public class AdapterCustomer extends RecyclerView.Adapter<AdapterCustomer.ViewHolder> {
    List<Customer> customerList;
    Context context;
    CustomerDAO customerDAO;
    ContractDAO contractDAO;

    public AdapterCustomer(List<Customer> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;
        contractDAO=new ContractDAO(context);
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
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateCustomerActivity.pos = position;
                Intent i = new Intent(context, UpdateCustomerActivity.class);
                context.startActivity(i);
                Animatoo.animateSlideLeft(context);
            }
        });

        holder.detailsCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsCustomer(position);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contractDAO.getStatusCustomer(customerList.get(position).getCustomerID())>0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Lưu ý");
                    builder.setMessage("Bạn phải xóa những hợp đồng liên quan đến khách \"" + customerList.get(position).getCustomerName() + "\" trước khi xóa!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Tôi biết rồi", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Lưu ý");
                    builder.setMessage("Bạn có muốn xóa khách thuê \"" + customerList.get(position).getCustomerName() + "\" này?");
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

            }
        });

    }

    private void detailsCustomer(final int position) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_customer_detail);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        dialog.show();
        //Todo back
        ImageView backIcon = dialog.findViewById(R.id.back_ic);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //Todo show details
        EditText edtCMND, edtPhone, edtName;
        edtCMND = dialog.findViewById(R.id.edtcmnd);
        edtName = dialog.findViewById(R.id.edtname);
        edtPhone = dialog.findViewById(R.id.edtphone);
        ImageView avatar = dialog.findViewById(R.id.avatar);
        ImageView imgCMNDB = dialog.findViewById(R.id.img_cmnd_before);
        ImageView imgCMNDA = dialog.findViewById(R.id.img_cmnd_after);

        edtCMND.setText(customerList.get(position).getCustomerCMND() + "");
        edtName.setText(customerList.get(position).getCustomerName());
        edtPhone.setText(customerList.get(position).getCustomerPhone());
        edtCMND.setEnabled(false);
        edtName.setEnabled(false);
        edtPhone.setEnabled(false);
        avatar.setImageBitmap(LoadImgDetail(customerList.get(position).getCustomerImage()));
        imgCMNDB.setImageBitmap(LoadImgDetail(customerList.get(position).getCustomerCMNDImgBefore()));
        imgCMNDA.setImageBitmap(LoadImgDetail(customerList.get(position).getCustomerCMNdImgAfter()));


    }


    @Override
    public int getItemCount() {
        return customerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Name, Phone;
        private ImageView Img, imgDelete, imgEdit;
        private CardView cardView;
        private RelativeLayout detailsCustomer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            Name = itemView.findViewById(R.id.tvcustomername);
            Phone = itemView.findViewById(R.id.tvcustomerphone);
            Img = itemView.findViewById(R.id.imgcustomer);
            cardView = itemView.findViewById(R.id.cardView);

            imgDelete = itemView.findViewById(R.id.imgDelete);
            detailsCustomer = itemView.findViewById(R.id.detailsCustomer);
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

    private Bitmap LoadImgDetail(byte[] img) {
        return BitmapFactory.decodeByteArray(img, 0, img.length);
    }
}