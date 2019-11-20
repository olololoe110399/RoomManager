package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;


public class RoomActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    String roomid;
    Intent i;
    RelativeLayout rlupdate, rlcontract, rlbill;
    public static Room room = new Room();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        initView();
        initToolbar();
        setupOnclick();
    }

    public void initView() {
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
        finish();
        Animatoo.animateSlideRight(this);

        return true;
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.rlbill:
                i =new Intent(this,BillActivity.class);
                startActivity(i);
                Animatoo.animateSlideLeft(this);

                break;
            case R.id.rlcontract:
                i =new Intent(this,ContractActivity.class);
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
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }
}
