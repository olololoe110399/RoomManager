package com.example.qunlphngtr.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qunlphngtr.Adapter.AdapterService;
import com.example.qunlphngtr.Database.ServiceDAO;
import com.example.qunlphngtr.Model.Service;
import com.example.qunlphngtr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentService extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ProgressBar myProgress;
    public static List<Service> serviceList;
    private AdapterService adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton button;
    private ServiceDAO serviceDAO;
    public static TextView tvServiceNull;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service, container, false);
        initView();

        new Loading().execute();
        return view;
    }

    private void initView() {
        tvServiceNull=view.findViewById(R.id.tvServiceNull);
        recyclerView = view.findViewById(R.id.rvservice);
        myProgress = view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.srlservice);
        button = view.findViewById(R.id.fbservice);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddService();
            }
        });
        serviceList = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecyclerView();
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new AdapterService(getActivity(), serviceList, R.layout.item_service);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        serviceDAO = new ServiceDAO(getActivity());
    }

    private void refreshRecyclerView() {
        new Loading().execute();

    }
    public  static void checkServicelistNull(){
        if(serviceList.size()>0){
            tvServiceNull.setVisibility(View.GONE);
        }else {
            tvServiceNull.setVisibility(View.VISIBLE);
        }
    }

    private class Loading extends AsyncTask<Void, Void, List<Service>> {
        @Override
        protected void onPreExecute() {
            myProgress.setVisibility(View.VISIBLE);
            serviceList.clear();
            super.onPreExecute();
        }

        @Override
        protected List<Service> doInBackground(Void... voids) {
            serviceList.addAll(serviceDAO.getAllService());
            return serviceList;
        }

        @Override
        protected void onPostExecute(List<Service> services) {
            adapter.notifyDataSetChanged();
            myProgress.setVisibility(View.GONE);
            checkServicelistNull();
            swipeRefreshLayout.setRefreshing(false);// set swipe refreshing
            super.onPostExecute(services);
        }
    }

    private void dialogAddService() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_add_service);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        dialog.setCancelable(false);
        EditText edtServiceName, edtServicePrice;
        Button btnadd, btnback;
        edtServiceName = dialog.findViewById(R.id.edtServiceName);
        edtServicePrice = dialog.findViewById(R.id.edtServicePrice);
        btnadd = dialog.findViewById(R.id.btnadd);
        btnback = dialog.findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtServiceName.getText().toString().trim().equals("") || edtServicePrice.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    Service service = new Service();
                    service.setServiceName(edtServiceName.getText().toString());
                    service.setServicePrice(Integer.parseInt(edtServicePrice.getText().toString()));
                    if (serviceDAO.addService(service) > 0) {
                        Toast.makeText(getActivity(), R.string.successfully, Toast.LENGTH_SHORT).show();
                        refreshRecyclerView();
                        checkServicelistNull();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog.show();
    }
    private void LoadRecyclerview(){
        serviceList.clear();
        serviceList.addAll(serviceDAO.getAllService());
        adapter.notifyDataSetChanged();
    }
}
