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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qunlphngtr.Adapter.AdapterRoom;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentRoom extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ProgressBar myProgress;
    private List<Room> roomList;
    private AdapterRoom adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_room, container, false);
        initView();
        new Loading().execute();
        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.rvroom);
        myProgress = view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.srlroom);
        roomList = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecyclerView();
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new AdapterRoom(roomList, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
    }

    private class Loading extends AsyncTask<Void, Void, List<Room>> {
        @Override
        protected void onPreExecute() {
            myProgress.setVisibility(View.VISIBLE);
            roomList.clear();
            super.onPreExecute();
        }

        @Override
        protected List<Room> doInBackground(Void... voids) {
            roomList.add(new Room(1, "Phòng 1", 2000000, 12, 5000, 10000));
            roomList.add(new Room(2, "Phòng 2", 1000000, 12, 5000, 10000));
            roomList.add(new Room(3, "Phòng 3", 800000, 12, 5000, 10000));
            roomList.add(new Room(4, "Phòng 4", 1500000, 12, 5000, 10000));
            return roomList;
        }

        @Override
        protected void onPostExecute(List<Room> Rooms) {
            adapter.notifyDataSetChanged();
            myProgress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);// set swipe refreshing
            super.onPostExecute(Rooms);
        }
    }

    private void refreshRecyclerView() {
        new Loading().execute();

    }

}
