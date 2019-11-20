package com.example.qunlphngtr.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qunlphngtr.Adapter.AdapterService;
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service, container, false);
        initView();

        new Loading().execute();
        return view;
    }
    private void initView() {
        recyclerView = view.findViewById(R.id.rvservice);
        myProgress = view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.srlservice);
        button=view.findViewById(R.id.fbservice);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        adapter = new AdapterService(getActivity(), serviceList,R.layout.item_service);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
    private void refreshRecyclerView() {
        new Loading().execute();

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
            serviceList.add(new Service(0,  "Rác", 5000));
            serviceList.add(new Service(1, "Internet",  40000));
            serviceList.add(new Service(2,  "Tivi", 50000));
            serviceList.add(new Service(3,   "Tủ lạnh", 100000));
            return serviceList;
        }

        @Override
        protected void onPostExecute(List<Service> services) {
            adapter.notifyDataSetChanged();
            myProgress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);// set swipe refreshing
            super.onPostExecute(services);
        }
    }
}
