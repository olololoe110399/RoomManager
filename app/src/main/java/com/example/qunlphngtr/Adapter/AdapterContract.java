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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import com.example.qunlphngtr.Activities.ContractActivity;
import com.example.qunlphngtr.Activities.ContractDetailActivity;
import com.example.qunlphngtr.Database.BillDAO;
import com.example.qunlphngtr.Database.BillServiceDAO;
import com.example.qunlphngtr.Database.ContractDAO;
import com.example.qunlphngtr.Model.Bill;
import com.example.qunlphngtr.Model.Contract;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.Model.Service;
import com.example.qunlphngtr.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AdapterContract extends RecyclerView.Adapter<AdapterContract.ViewHolder> implements View.OnClickListener {
    List<Contract> contractList;
    Context context;
    BottomSheetDialog mBottomDialogNotificationAction;
    ContractDAO contractDAO;
    BillServiceDAO billServiceDAO;
    List<Service> serviceList;
    List<Bill> billList;
    BillDAO billDAO;
    NumberFormat formatter;

    public AdapterContract(List<Contract> contractList, Context context) {
        this.contractList = contractList;
        this.context = context;
        contractDAO = new ContractDAO(context);
        billServiceDAO = new BillServiceDAO(context);
        serviceList = new ArrayList<>();
        billList = new ArrayList<>();
        billDAO = new BillDAO(context);
        formatter = new DecimalFormat("#,###");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_contract, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if (contractList.get(position).getContractstatus() == 0) {
            holder.Id.setText("Còn hiệu lực");

            holder.Id.setTextColor(ContextCompat.getColor(context, R.color.Color2));
        } else {
            holder.Id.setText("Đã thanh lý");
            holder.Id.setTextColor(ContextCompat.getColor(context, R.color.colorBlack2));
        }
        holder.date.setText(contractList.get(position).getContractDateBegin() + " - " + contractList.get(position).getContractDateEnd());
        holder.name.setText(contractList.get(position).getCustomer().getCustomerName());
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogNotificationAction(position);
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContractDetailActivity.contract = contractList.get(position);
                Intent i = new Intent(context, ContractDetailActivity.class);
                context.startActivity(i);
                Animatoo.animateSlideLeft(context);

            }
        });

    }


    @Override
    public int getItemCount() {
        return contractList.size();
    }

    @Override
    public void onClick(View v) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Id, date, name;
        private ImageButton btndelete;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Id = itemView.findViewById(R.id.tvstatus);
            date = itemView.findViewById(R.id.tvcontractdate);
            name = itemView.findViewById(R.id.tvcontractcustomer);
            btndelete = itemView.findViewById(R.id.btnmenu);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    private void dialogdelete(final int position) {
        String tvTotal = "";
        billList = billDAO.getBillDebtsByContractID(contractList.get(position).getContractID());
        double A = TotalDebsToPay() - contractList.get(position).getContractDeposits();
        if (A > 0) {
            tvTotal = "Tổng nợ chưa thanh toán:" + formatter.format(TotalDebsToPay()) + " VND" +
                    "\nTiền cọc của khách là: " + formatter.format(contractList.get(position).getContractDeposits()) + " VND" +
                    "\nTổng tiền khách phải thanh toán là:" + formatter.format(A) + " VND\n";
        } else {
            if (contractList.get(position).getContractDeposits() > 0) {
                tvTotal = "Tiền cọc phải trả lại cho khách là: " + formatter.format(contractList.get(position).getContractDeposits()) + " VND\n";
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Lưu ý");
        builder.setMessage(tvTotal +
                "Bạn chắc chắn muốn xóa hợp đồng này?");
        builder.setCancelable(false);
        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Thanh toán và xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!(billList == null)) UpdateBillDebsToPay(position);
                serviceList = billServiceDAO.getsServiceBillByID(contractList.get(position).getContractID());
                for (int j = 0; j < serviceList.size(); j++) {
                    billServiceDAO.deleteBillServiceByID(serviceList.get(j).getServiceID());
                }
                contractDAO.deleteContractByID(contractList.get(position).getContractID());
                contractList.remove(position);
                notifyItemRangeChanged(position, contractList.size());
                notifyItemRemoved(position);
                notifyItemChanged(position);
                ContractActivity.checkContractHideAndShowFAB();
                ContractActivity.checkContractlistNull();
                mBottomDialogNotificationAction.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void dialogiquidation(final int position) {
        String tvTotal = "";
        billList = billDAO.getBillDebtsByContractID(contractList.get(position).getContractID());
        double A = TotalDebsToPay() - contractList.get(position).getContractDeposits();
        if (A > 0) {
            tvTotal = "Tổng nợ chưa thanh toán:" + formatter.format(TotalDebsToPay()) + " VND" +
                    "\nTiền cọc của khách là: " + formatter.format(contractList.get(position).getContractDeposits()) + " VND" +
                    "\nTổng tiền khách phải thanh toán là:" + formatter.format(A) + " VND";
        } else {
            tvTotal = "Tiền cọc phải trả lại cho khách là: " + formatter.format(contractList.get(position).getContractDeposits()) + " VND";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Lưu ý");
        builder.setMessage(tvTotal +
                "\nBạn chắc chắn muốn thanh lý hợp đồng này?"
        );
        builder.setCancelable(false);
        builder.setPositiveButton("trở về", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Thanh toán và thanh lý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!(billList == null)) UpdateBillDebsToPay(position);
                contractDAO.updateContractStatus(contractList.get(position).getContractID());
                contractDAO.updateContractDeposits(contractList.get(position).getContractID());
                contractList.get(position).setContractstatus(1);
                contractList.get(position).setContractDeposits(0);
                notifyItemRangeChanged(position, contractList.size());
                notifyItemInserted(position);
                notifyItemChanged(position);
                ContractActivity.checkContractHideAndShowFAB();
                ContractActivity.checkContractlistNull();
                mBottomDialogNotificationAction.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialogNotificationAction(final int pos) {
        try {
            View view = ((ContractActivity) context).getLayoutInflater().inflate(R.layout.dialog_menu_bottomsheet, null);
            mBottomDialogNotificationAction = new BottomSheetDialog(context);
            mBottomDialogNotificationAction.setContentView(view);
            mBottomDialogNotificationAction.show();

            // Remove default white color background
            FrameLayout bottomSheet = (FrameLayout) mBottomDialogNotificationAction.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            bottomSheet.setBackground(null);
            // init
            LinearLayout lnliquidation, lndismiss, lndelete;
            lnliquidation = view.findViewById(R.id.lnliquidation);
            lndismiss = view.findViewById(R.id.lndismiss);
            lndelete = view.findViewById(R.id.lndelete);
            lnliquidation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomDialogNotificationAction.dismiss();
                    if (contractList.get(pos).getContractstatus() == 0) {
                        dialogiquidation(pos);
                    } else {
                        Toast.makeText(context, "Hợp đồng đã thanh lý", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            lndismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomDialogNotificationAction.dismiss();
                }
            });
            lndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomDialogNotificationAction.dismiss();
                    dialogdelete(pos);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double TotalDebsToPay() {
        double TotalDebsToPay = 0;
        for (int i = 0; i < billList.size(); i++) {
            TotalDebsToPay += billList.get(i).getBillDebtsToPay();
        }
        return TotalDebsToPay;
    }

    private void UpdateBillDebsToPay(int p) {
        int id=-1;
        if (billList.size() > 0) {
             id= billList.get(billList.size() - 1).getBillID();
        }

        for (Bill bill : billList) {
            if (id == bill.getBillID()&&billList.size()>0) {
                bill.setBIllTotal(bill.getBillDebtsToPay() - contractList.get(p).getContractDeposits());
            } else {
                bill.setBIllTotal(bill.getBillDebtsToPay());
            }
            bill.setBillPaymentDate(Calendar.getInstance().getTime());
            bill.setBillDebtsToPay(0);
            billDAO.updateBill(bill);
        }
    }


}