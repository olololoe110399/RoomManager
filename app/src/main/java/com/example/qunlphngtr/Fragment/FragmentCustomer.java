package com.example.qunlphngtr.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qunlphngtr.Adapter.AdapterCustomer;
import com.example.qunlphngtr.Database.CustomerDAO;
import com.example.qunlphngtr.Helper.SwipeController;
import com.example.qunlphngtr.Helper.SwipeControllerActions;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class
FragmentCustomer extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ProgressBar myProgress;
    private List<Customer> customerList;
    private AdapterCustomer adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    byte[] img;
    private CustomerDAO customerDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer, container, false);
        initView();
        innitObject();
        new Loading().execute();
        return view;
    }

    private void innitObject() {
        customerDAO = new CustomerDAO(getActivity());
    }

    private void dialogdelete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                customerList.remove(position);
                adapter.notifyItemRangeChanged(position, customerList.size());
                adapter.notifyItemRemoved(position);
                adapter.notifyItemChanged(position);

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void initView() {
        recyclerView = view.findViewById(R.id.rvcustomer);
        myProgress = view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.srlcustomer);
        customerList = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecyclerView();
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new AdapterCustomer(customerList, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        img = LoadingImg(R.drawable.avatar);
    }

    private void refreshRecyclerView() {
        new Loading().execute();

    }

    private class Loading extends AsyncTask<Void, Void, List<Customer>> {
        @Override
        protected void onPreExecute() {
            myProgress.setVisibility(View.VISIBLE);
            customerList.clear();
            super.onPreExecute();
        }

        @Override
        protected List<Customer> doInBackground(Void... voids) {
            customerList.addAll(customerDAO.getAllCustomer());
            return customerList;
        }

        @Override
        protected void onPostExecute(List<Customer> customers) {
            adapter.notifyDataSetChanged();
            myProgress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);// set swipe refreshing
            super.onPostExecute(customers);
        }
    }

    private byte[] LoadingImg(int IDImg) {
        Drawable drawable = getActivity().getDrawable(IDImg);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
