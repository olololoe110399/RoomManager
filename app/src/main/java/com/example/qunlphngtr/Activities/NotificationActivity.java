package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Adapter.AdapterNotification;
import com.example.qunlphngtr.Model.Notification;
import com.example.qunlphngtr.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView lvnotification;

    public static List<Notification> notificationList=new ArrayList<>();
    public static AdapterNotification adapterNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initView();
        initToolbar();
        initObject();
        updatestatus();
        MainActivity.CheckcountNotification();

    }

    private void updatestatus() {
        for (int i=0;i<notificationList.size();i++){
            notificationList.get(i).setStatus(false);
            adapterNotification.notifyDataSetChanged();
        }

    }

    private void initObject() {
        adapterNotification=new AdapterNotification(notificationList,this,R.layout.item_notification);
        lvnotification.setAdapter(adapterNotification);
        lvnotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(NotificationActivity.this, ""+notificationList.get(i).isStatus(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        lvnotification=findViewById(R.id.lvnotification);
    }

    public void initToolbar() {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông báo");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Animatoo.animateSlideRight(this);
        return super.onSupportNavigateUp();

    }
}
