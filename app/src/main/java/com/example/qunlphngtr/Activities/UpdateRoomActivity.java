package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Database.RoomDAO;
import com.example.qunlphngtr.Fragment.FragmentRoom;
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
    private Button btnUpdate;
    private RoomDAO roomDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_room);
        initToolbar();
        ShowRoomDetails();
        UpdateRoom();
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
    public void UpdateRoom(){
        roomDAO = new RoomDAO(this);
        btnUpdate = findViewById(R.id.btnSave);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtRoomPrice.getText().toString().trim().equals("") || edtRoomElectric.getText().toString().trim().equals("")
                || edtRoomWater.getText().toString().equals("")){
                    Toast.makeText(UpdateRoomActivity.this, "Vui lòng không để trống!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Room room1=new Room();
                    room1.setRoomID(room.getRoomID());
                    room1.setRoomName(room.getRoomName());
                    room1.setRoomPrice(Integer.parseInt(edtRoomPrice.getText().toString()));
                    room1.setRoomAcreage(Integer.parseInt(edtRoomAceage.getText().toString()));
                    room1.setRoomElectricPrice(Integer.parseInt(edtRoomElectric.getText().toString()));
                    room1.setRoomWaterPrice(Integer.parseInt(edtRoomWater.getText().toString()));
                    roomDAO.updateRoomByID(room1);
                    FragmentRoom.LoadRecyclerview();
                    RoomActivity.room=FragmentRoom.roomList.get(RoomActivity.pos);
                    Toast.makeText(UpdateRoomActivity.this, "Thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
