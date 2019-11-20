package com.example.qunlphngtr.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunlphngtr.Model.Service;
import com.example.qunlphngtr.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AdapterService extends RecyclerView.Adapter<AdapterService.ViewHolder> {
    private Context context;
    private List<Service> serviceList;
    private int layout;

    public AdapterService() {
    }

    public AdapterService(Context context, List<Service> serviceList,int layout) {
        this.context = context;
        this.serviceList = serviceList;
        this.layout=layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tvname.setText(serviceList.get(position).getServiceName());
        holder.tvprice.setText(formatter.format(serviceList.get(position).getServicePrice()) + " VND");
    }

    @Override
    public int getItemCount() {

        return serviceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvname, tvprice;
        private ImageView btndelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.tvservicename);
            tvprice = itemView.findViewById(R.id.tvserviceprice);
            btndelete = itemView.findViewById(R.id.btndelete);
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
                serviceList.remove(position);
                notifyItemRangeChanged(position, serviceList.size());
                notifyItemRemoved(position);
                notifyItemChanged(position);
                notifyDataSetChanged();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
