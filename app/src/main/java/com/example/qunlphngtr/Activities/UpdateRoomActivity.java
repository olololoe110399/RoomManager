package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateRoomActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvRoomName;
    private EditText edtRoomPrice, edtRoomAceage, edtRoomWater, edtRoomElectric;
    private Room room;
    private List<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_room);
        initToolbar();
        ShowRoomDetails();
    }
    private void initToolbar() {
        room = RoomActivity.room;
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin phòng");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Animatoo.animateSlideRight(this);
        return true;
    }
    public void ShowRoomDetails(){
        tvRoomName = findViewById(R.id.tvRoomName);
        edtRoomPrice = findViewById(R.id.edtRoomPrice);
        edtRoomAceage = findViewById(R.id.edtRoomAcreage);
        edtRoomWater = findViewById(R.id.edtRoomWaterPrice);
        edtRoomElectric = findViewById(R.id.edtRoomElectricPrice);
        tvRoomName.setText(room.getRoomName());
        edtRoomPrice.setText(room.getRoomPrice()+"");
        edtRoomWater.setText(room.getRoomWaterPrice()+"");
        edtRoomElectric.setText(room.getRoomElectricPrice()+"");
        edtRoomAceage.setText(room.getRoomAcreage()+"");
    }

}
