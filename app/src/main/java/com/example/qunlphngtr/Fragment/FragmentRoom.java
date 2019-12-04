package com.example.qunlphngtr.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Activities.AddRoomActivity;
import com.example.qunlphngtr.Adapter.AdapterRoom;
import com.example.qunlphngtr.Database.ContractDAO;
import com.example.qunlphngtr.Database.RoomDAO;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentRoom extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ProgressBar myProgress;
    public static List<Room> roomList;
    public static AdapterRoom adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabroom;
    public static RoomDAO roomDAO;
    public static TextView tvroomNull;
    public static TextView tvsizeRoom, tvNumberRoomNull, tvpeolpeNumberRoom;
    public static ContractDAO contractDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_room, container, false);
        initView();
        new Loading().execute();
        return view;
    }

    public static void setTextRoomManager() {
        tvsizeRoom.setText(roomList.size()+"");
        int PeopleNumberRoom=contractDAO.getallPeopleNumberRoom();
        tvpeolpeNumberRoom.setText(PeopleNumberRoom+"");
        int sum= (roomList.size()-contractDAO.getallNumberRoomNotNull());
        tvNumberRoomNull.setText(sum+"");


    }

    private void initView() {
        contractDAO=new ContractDAO(getActivity());
        tvroomNull = view.findViewById(R.id.tvroomNull);
        tvsizeRoom = view.findViewById(R.id.tvsizeRoom);
        tvNumberRoomNull = view.findViewById(R.id.tvNumberRoomNull);
        tvpeolpeNumberRoom = view.findViewById(R.id.tvpeolpeNumberRoom);
        fabroom = view.findViewById(R.id.fbroom);
        fabroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddRoomActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(getActivity());
            }
        });
        roomDAO = new RoomDAO(getActivity());
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

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new AdapterRoom(roomList, getActivity());
        recyclerView.setAdapter(adapter);

    }

    public static void checkRoomListNull() {
        if (roomList.size() > 0) {
            tvroomNull.setVisibility(View.GONE);
        } else {
            tvroomNull.setVisibility(View.VISIBLE);
        }
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
            roomList.addAll(roomDAO.getAllRoom());
            return roomList;
        }

        @Override
        protected void onPostExecute(List<Room> Rooms) {
            adapter.notifyDataSetChanged();
            checkRoomListNull();
            setTextRoomManager();
            myProgress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);// set swipe refreshing
            super.onPostExecute(Rooms);
        }
    }

    private void refreshRecyclerView() {
        new Loading().execute();

    }

    public static void LoadRecyclerview() {
        roomList.clear();
        roomList.addAll(roomDAO.getAllRoom());
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        setTextRoomManager();
    }
}
