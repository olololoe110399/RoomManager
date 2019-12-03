package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.View;
import android.widget.RelativeLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Adapter.AdapterBill;
import com.example.qunlphngtr.Database.BillDAO;
import com.example.qunlphngtr.Database.ContractDAO;
import com.example.qunlphngtr.Model.Bill;
import com.example.qunlphngtr.Model.Contract;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BillActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    public static List<Bill> billList;
    public static AdapterBill adapter;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static RelativeLayout relativeLayout;
    public static List<Contract> contractList = new ArrayList<>();
    private Room room = RoomActivity.room;
    private FloatingActionButton fbbill;
    private ContractDAO contractDAO;
    private BillDAO billDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        initView();
        initToolbar();
        checkBillNull();
        fbbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkContract();
            }
        });
    }


    private void initView() {
        billDAO = new BillDAO(this);
        contractDAO = new ContractDAO(this);
        contractList = contractDAO.getAllContract(room.getRoomID());
        fbbill = findViewById(R.id.fbbill);
        relativeLayout = findViewById(R.id.rlbillnull);
        recyclerView = findViewById(R.id.rvbill);
        swipeRefreshLayout = findViewById(R.id.srlbill);
        billList = new ArrayList<>();
        billList = billDAO.getBillByID(room.getRoomID());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecycerview();
                checkBillNull();
                swipeRefreshLayout.setRefreshing(false);// set swipe refreshing
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new AdapterBill(billList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hóa đơn");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Animatoo.animateSlideRight(this);
        return true;
    }

    public static void checkBillNull() {
        if (billList.size() > 0) {
            relativeLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {

            relativeLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }


    }

    private int checckContractlistStatus() {
        int status=-1;
        for (int i = 0; i < contractList.size(); i++) {
            if (contractList.get(i).getContractstatus() == 0) {
                status=1;
                break;
            }
        }
        return status;
    }

    private void checkContract() {
        if (contractList.size() > 0) {
            if(checckContractlistStatus()>0){
                Intent intent = new Intent(this, AddBillActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(this);

            }  else {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                mBuilder.setTitle("Thông báo");
                mBuilder.setMessage("Chưa có hợp đồng nào đang ở. Vui lòng một tạo hợp đồng trước khi tạo hóa đơn!");
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(BillActivity.this, ContractActivity.class);
                        startActivity(i);
                        Animatoo.animateDiagonal(BillActivity.this);
                        finish();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }

        } else {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("Thông báo");
            mBuilder.setMessage("Chưa có hợp đồng nào. Vui lòng tạo hợp đồng trước khi tạo hóa đơn!");
            mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(BillActivity.this, ContractActivity.class);
                    startActivity(i);
                    Animatoo.animateDiagonal(BillActivity.this);
                    finish();
                }
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecycerview();
        checkBillNull();
    }

    private void loadRecycerview() {
        billList.clear();
        billList.addAll(billDAO.getBillByID(room.getRoomID()));
        adapter.notifyDataSetChanged();
    }
}
