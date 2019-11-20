package com.example.qunlphngtr.Adapter;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Activities.BillActivity;
import com.example.qunlphngtr.Activities.BillDetailActiviy;
import com.example.qunlphngtr.Model.Bill;
import com.example.qunlphngtr.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AdapterBill extends RecyclerView.Adapter<AdapterBill.ViewHolder> {
    List<Bill> billList;
    Context context;

    public AdapterBill() {
    }

    public AdapterBill(List<Bill> billList, Context context) {
        this.billList = billList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false);
        return new AdapterBill.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tvbill.setText(billList.get(position).getBillDateBegin()+"->"+billList.get(position).getBillDateEnd());
        holder.tvbillcustomername.setText(billList.get(position).getBillCustomerName());
        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tvtotal.setText(formatter.format(billList.get(position).getBIllTotal()) + " VND");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, BillDetailActiviy.class);
                context.startActivity(i);
                Animatoo.animateSlideLeft(context);
            }
        });

    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvbillcustomername, tvbill, tvtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardView);
            tvbill = itemView.findViewById(R.id.tvbilldatebeginandend);
            tvbillcustomername = itemView.findViewById(R.id.tvbillnamecustomer);
            tvtotal = itemView.findViewById(R.id.tvTotal);
        }
    }
    private void dialogdelete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure delete?");
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                billList.remove(position);
                notifyItemRangeChanged(position, billList.size());
                notifyItemRemoved(position);
                notifyItemChanged(position);
                notifyDataSetChanged();
                BillActivity.checkbill();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
