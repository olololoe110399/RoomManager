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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        initView();
        initToolbar();
        checkbill();
        demoAddContract();
        fbbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkContract();
            }
        });
    }


    private void initView() {
        fbbill=findViewById(R.id.fbbill);
        relativeLayout = findViewById(R.id.rlbillnull);
        recyclerView = findViewById(R.id.rvbill);
        swipeRefreshLayout = findViewById(R.id.srlbill);
        billList = new ArrayList<>();
//        try {
//            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2019");
//            billList.add(new Bill("HD0331",room,"Nguyễn Ngọc Duy","11/11/2019","01/12/2019","HD01212019",10,20,date1,150000000));
//            billList.add(new Bill("HD0332",room,"Bùi Nguyễn Quế Anh","01/12/2019","01/01/2020","HD01212019",10,20,date1,150000000));
//            billList.add(new Bill("HD0333",room,"Phan Ngọc Hải","01/01/2020","01/02/2020","HD01212019",10,20,date1,150000000));
//            billList.add(new Bill("HD0334",room,"Bùi Quý Thảo","01/02/2019","01/03/2020","HD01212019",10,20,date1,150000000));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                billList.clear();
                try {
                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2019");
                    billList.add(new Bill("HD0331", room, "KH0114", "11/11/2019", "11/12/2019", "HD16102019", 10, 20, date1, 150000000));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
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

    public static void checkbill() {
        if (billList.size() > 0) {
            relativeLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {

            relativeLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }


    }


    private void demoAddContract() {
        Drawable drawable = getDrawable(R.drawable.avatar);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        contractList.add(new Contract("HD01212019", "18/11/2019", "11/11/2020", 3, 2, room, new Customer("1", image, "0793333648", "Bùi Nguyễn Quế Anh", 201729145), 1, 0, 0, 1, 0,400000));
        contractList.add(new Contract("HD01212019", "18/11/2019", "11/11/2020", 3, 2, room, new Customer("1", image, "0793333648", "Bùi Nguyễn Quế Anh", 201729145), 1, 0, 0, 1, 1,400000));
        contractList.add(new Contract("HD01212019", "18/11/2019", "11/11/2020", 3, 2, room, new Customer("1", image, "0793333648", "Bùi Nguyễn Quế Anh", 201729145), 1, 0, 0, 1, 1,400000));
        contractList.add(new Contract("HD01212019", "18/11/2019", "11/11/2020", 3, 2, room, new Customer("1", image, "0793333648", "Bùi Nguyễn Quế Anh", 201729145), 1, 0, 0, 1, 1,400000));

    }

    private void checkContract() {
        if (contractList.size() > 0) {
            for (int i = 0; i < contractList.size(); i++) {
                if (contractList.get(i).getContractstatus() == 0) {
                    Intent intent = new Intent(this, AddBillActivity.class);
                    startActivity(intent);
                    Animatoo.animateSlideLeft(this);
                    break;
                }
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

}
