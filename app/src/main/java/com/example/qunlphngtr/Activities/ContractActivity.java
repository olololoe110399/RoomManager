package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Adapter.AdapterContract;

import com.example.qunlphngtr.Database.ContractDAO;
import com.example.qunlphngtr.Model.Contract;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ContractActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    public static List<Contract> contractList;
    public static AdapterContract adapter;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static RelativeLayout relativeLayout;
    private Room room = RoomActivity.room;
    public static FloatingActionButton fbcontract;
    private ContractDAO contractDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        initView();
        initToolbar();
        checkContractlistNull();
        checkContractHideAndShowFAB();
        fbcontract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContractActivity.this, AddContractActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(ContractActivity.this);
            }
        });
    }


    public static void checkContractlistNull() {
        if (contractList.size() > 0) {
            relativeLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }


    }

    private void initView() {
        contractDAO = new ContractDAO(this);
        fbcontract = findViewById(R.id.fbcontract);
        relativeLayout = findViewById(R.id.rlcontractnull);
        toolbar = findViewById(R.id.tool_bar);
        recyclerView = findViewById(R.id.rvcontract);
        swipeRefreshLayout = findViewById(R.id.srlcontract);
        contractList = new ArrayList<>();
        contractList = contractDAO.getAllContract(room.getRoomID());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecycerview();
                checkContractlistNull();
                checkContractHideAndShowFAB();
                swipeRefreshLayout.setRefreshing(false);// set swipe refreshing
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new AdapterContract(contractList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hợp đồng");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Animatoo.animateSlideRight(this);
        return true;
    }


    @SuppressLint("RestrictedApi")
    public static void checkContractHideAndShowFAB() {
        if (contractList.size() > 0) {
            for (int i = 0; i < contractList.size(); i++) {
                if (contractList.get(i).getContractstatus() == 0) {
                    fbcontract.setVisibility(View.GONE);
                    break;
                } else {
                    fbcontract.setVisibility(View.VISIBLE);
                }
            }

        } else {
            fbcontract.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecycerview();
        checkContractlistNull();
        checkContractHideAndShowFAB();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }
    private void loadRecycerview(){
        contractList.clear();
        contractList.addAll(contractDAO.getAllContract(room.getRoomID()));
        adapter.notifyDataSetChanged();
    }

}
