package com.example.qunlphngtr.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Activities.AddBillActivity;
import com.example.qunlphngtr.Activities.BillActivity;
import com.example.qunlphngtr.Database.BillDAO;
import com.example.qunlphngtr.Model.Bill;
import com.example.qunlphngtr.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterBill extends RecyclerView.Adapter<AdapterBill.ViewHolder> {
    List<Bill> billList;
    Context context;
    BillDAO billDAO;

    public AdapterBill() {
    }

    public AdapterBill(List<Bill> billList, Context context) {
        this.billList = billList;
        this.context = context;
        billDAO = new BillDAO(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false);
        return new AdapterBill.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (billList.get(position).getBillDebtsToPay() == 0) {
            holder.tvPaymentStatus.setText("Đã thanh toán");
            holder.tvPaymentStatus.setTextColor(ContextCompat.getColor(context, R.color.Color2));
            holder.btnPayment.setVisibility(View.GONE);
        } else {
            holder.tvPaymentStatus.setText("Chưa thanh toán");
            holder.tvPaymentStatus.setTextColor(ContextCompat.getColor(context, R.color.Color4));
            holder.btnPayment.setVisibility(View.VISIBLE);
        }
        holder.tvbill.setText(billList.get(position).getBillDateBegin() + "->" + billList.get(position).getBillDateEnd());
        holder.tvbillcustomername.setText(billList.get(position).getBillCustomerName());
        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tvtotal.setText(formatter.format(billList.get(position).getBIllTotal()) + " VND");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTotal(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvbillcustomername, tvbill, tvtotal, tvPaymentStatus;
        private Button btnPayment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            btnPayment = itemView.findViewById(R.id.btnPayment);
            tvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
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
                BillActivity.checkBillNull();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void dialogBillDetail(int pos) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_customer_detail);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
    }

    private void dialogTotal(final int pos) {
        NumberFormat formatter = new DecimalFormat("#,###");
        new AlertDialog.Builder(context)
                .setTitle("Total")
                .setCancelable(false)
                .setMessage("Tổng số tiền phải trả là: " + formatter.format(billList.get(pos).getBillDebtsToPay()) + " VND")
                .setPositiveButton("Thanh toán", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Bill bill = billList.get(pos);
                        bill.setBIllTotal(bill.getBillDebtsToPay());
                        bill.setBillDebtsToPay(0);
                        bill.setBillPaymentDate(Calendar.getInstance().getTime());
                        billDAO.updateBill(bill);
                        notifyItemRangeChanged(pos, billList.size());
                        notifyItemInserted(pos);
                        notifyItemChanged(pos);
                        BillActivity.spnBillFilter.setSelection(1);
                    }
                })
                .setNegativeButton("Nợ lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
