package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Database.CustomerDAO;
import com.example.qunlphngtr.Database.RoomDAO;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;

public class AddCustomerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnsave, btncancel;
    private EditText edtCustomerPhone, edtCustomerName, edtCustomerCMND;
    private ImageView imgCustomer, imgCMNDBefore, imgCMNDAfter;
    private CustomerDAO customerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        initToolbar();
        initView();
        setBtnOnclick();
    }
    private void initToolbar() {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm khách hàng");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Animatoo.animateSlideRight(this);
        return true;
    }
    private void setBtnOnclick() {
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCustomer();
            }
        });
    }
    private void initView() {
        btnsave = findViewById(R.id.btnSave);
        btncancel = findViewById(R.id.btnCancel);

        customerDAO = new CustomerDAO(this);
        edtCustomerPhone = findViewById(R.id.edtPhone);
        edtCustomerName = findViewById(R.id.edtName);
        edtCustomerCMND = findViewById(R.id.edtCMND);
        imgCustomer = findViewById(R.id.imgCustomer);
        imgCMNDBefore = findViewById(R.id.imgCMNDBefore);
        imgCMNDAfter = findViewById(R.id.imgCMNDAfter);
    }
    public void AddCustomer(){
        if (edtCustomerPhone.getText().toString().trim().equals("") || edtCustomerName.getText().toString().trim().equals("") || edtCustomerCMND.getText().toString().trim().equals("") ) {
            Toast.makeText(this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else {
            Customer customer = new Customer();

            customer.setCustomerPhone(edtCustomerPhone.getText().toString().trim());
            customer.setCustomerName(edtCustomerName.getText().toString().trim());
            customer.setCustomerCMND(Integer.parseInt(edtCustomerCMND.getText().toString().trim()));

            if (customerDAO.addCustomer(customer) > 0) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Không thêm được", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
 ông