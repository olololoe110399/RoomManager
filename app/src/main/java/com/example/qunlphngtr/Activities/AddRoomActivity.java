package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Database.RoomDAO;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;

public class AddRoomActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edtroomName, edtroomPrice, edtroomAcrege, edtroomWaterNumber, edtroomElectricNumber;
    private Button btnsave, btncancel;
    private RoomDAO roomDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        initView();
        initToolbar();
        setBtnOnclick();
    }

    private void setBtnOnclick() {

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoom();
            }
        });
    }

    private void addRoom() {
        if (edtroomName.getText().toString().trim().equals("") || edtroomAcrege.getText().toString().trim().equals("") || edtroomPrice.getText().toString().trim().equals("") || edtroomWaterNumber.getText().toString().trim().equals("") || edtroomElectricNumber.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else {
            Room room = new Room();
            room.setRoomName(edtroomName.getText().toString().trim());
            room.setRoomAcreage(Integer.parseInt(edtroomAcrege.getText().toString().trim()));
            room.setRoomElectricPrice(Integer.parseInt(edtroomElectricNumber.getText().toString().trim()));
            room.setRoomWaterPrice(Integer.parseInt(edtroomWaterNumber.getText().toString().trim()));
            room.setRoomPrice(Integer.parseInt(edtroomPrice.getText().toString().trim()));
            if (roomDAO.addRoom(room) > 0) {
                finish();
            } else {
                Toast.makeText(this, "Không thêm được", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void initView() {
        roomDAO = new RoomDAO(this);
        edtroomName = findViewById(R.id.edtRoomName);
        edtroomAcrege = findViewById(R.id.edtRoomAcreage);
        edtroomPrice = findViewById(R.id.edtRoomPrice);
        edtroomWaterNumber = findViewById(R.id.edtRoomWaterPrice);
        edtroomElectricNumber = findViewById(R.id.edtRoomElectricPrice);
        btnsave = findViewById(R.id.btnSave);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm phòng");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Animatoo.animateSlideRight(this);
        return true;
    }
}
