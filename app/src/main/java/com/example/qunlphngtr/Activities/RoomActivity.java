package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Database.ContractDAO;
import com.example.qunlphngtr.Fragment.FragmentRoom;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;


public class RoomActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    Intent i;
    RelativeLayout rlupdate, rlcontract, rlbill;
    public static Room room = new Room();
    private ContractDAO contractDAO;
    private TextView tvpeolpeNumberRoom, tvstatusRoom, tvvehicalNumberRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        initView();
        initToolbar();
        setupOnclick();
        setTextRoom();
    }

    private void setTextRoom() {
        if(contractDAO.getStatusRoom(room.getRoomID())>0){
            tvstatusRoom.setText("Đang ở");
        }else {
            tvstatusRoom.setText("Trống");
        }
            tvpeolpeNumberRoom.setText(contractDAO.getpeopleNumberRoom(room.getRoomID())+"");
            tvvehicalNumberRoom.setText(contractDAO.getvehicleNumberRoom(room.getRoomID())+"");
    }

    public void initView() {
        tvpeolpeNumberRoom = findViewById(R.id.tvpeolpeNumberRoom);
        tvvehicalNumberRoom = findViewById(R.id.tvvehicalNumberRoom);
        tvstatusRoom = findViewById(R.id.tvstatusRoom);
        contractDAO = new ContractDAO(this);
        toolbar = findViewById(R.id.tool_bar);
        i = getIntent();
        rlcontract = findViewById(R.id.rlcontract);
        rlbill = findViewById(R.id.rlbill);
        rlupdate = findViewById(R.id.rlupdate);
    }

    private void setupOnclick() {
        rlcontract.setOnClickListener(this);
        rlupdate.setOnClickListener(this);
        rlbill.setOnClickListener(this);
    }


    public void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(i.getStringExtra("RoomName"));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        FragmentRoom.LoadRecyclerview();
        finish();
        Animatoo.animateSlideRight(this);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.rlbill:
                i = new Intent(this, BillActivity.class);
                startActivity(i);
                Animatoo.animateSlideLeft(this);

                break;
            case R.id.rlcontract:
                i = new Intent(this, ContractActivity.class);
                startActivity(i);
                Animatoo.animateSlideLeft(this);

                break;
            case R.id.rlupdate:
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        FragmentRoom.LoadRecyclerview();
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTextRoom();
    }
}
