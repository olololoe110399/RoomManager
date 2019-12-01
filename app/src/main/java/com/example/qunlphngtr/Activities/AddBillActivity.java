package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Database.BillDAO;
import com.example.qunlphngtr.Model.Bill;
import com.example.qunlphngtr.Model.Contract;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddBillActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText edtdatebegin, edtdateend, edtroomprice, edtbillroomprice, edtbillcustomername, edtnumberwater, edtnumberelectric;
    private Button btnadd;
    private TextView tvbillservice;
    private SimpleDateFormat simpleDateFormat;
    NumberFormat formatter;
    private Room room = RoomActivity.room;
    private int billroomprice = 0;
    private BillDAO billDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        initView();
        initToolbar();
        initObject();
        checkBilllist();
    }


    private void initObject() {
        billDAO=new BillDAO(this);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        formatter = new DecimalFormat("#,###");
    }

    private void checkBilllist() {
        int sumnumberwater = 0;
        int sumnumberelectric = 0;
        Contract contract = new Contract();
        for (int i = 0; i < BillActivity.contractList.size(); i++) {
            if (BillActivity.contractList.get(i).getContractstatus() == 0) {
                contract = BillActivity.contractList.get(i);
            }
        }


        List<Bill> billLists = new ArrayList<>();
        for (int j = 0; j < BillActivity.billList.size(); j++) {
            if (BillActivity.billList.get(j).getContractID() == contract.getContractID()) {
                billLists.add(BillActivity.billList.get(j));
            }
        }
        if (billLists.size() > 0) {

            Bill bill = billLists.get(billLists.size() - 1);
            settextNewbill(bill.getBillDateEnd(), contract.getContractDateTerm(), contract.getContractMonthPeriodic(), contract.getCustomer().getCustomerName());
            edtbillroomprice.setText(formatter.format(contract.getRoom().getRoomPrice()) + " VND");

            for (int i = 0; i < billLists.size(); i++) {
                sumnumberwater = sumnumberwater + billLists.get(i).getBillWaterNumber();
                sumnumberelectric = sumnumberelectric + billLists.get(i).getBillElectricNumber();
            }
            addBill(contract.getRoom().getRoomPrice(), contract.getContracNumberWaterBegin(), sumnumberwater, contract.getContracNumberElectricBegin(), sumnumberelectric, contract.getContractID());

        } else {
            settextNewbill(contract.getContractDateBegin(), contract.getContractDateTerm(), contract.getContractMonthPeriodic(), contract.getCustomer().getCustomerName());
            addBill(billroomprice, contract.getContracNumberWaterBegin(), sumnumberwater, contract.getContracNumberElectricBegin(), sumnumberelectric, contract.getContractID());

        }


    }

    private void addBill(final int billroomprice, final int numberwaterbegin, final int numberwatersum, final int numberelectricbegin, final int numberelctricsum, final String contractID) {

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int e = numberelectricbegin + numberelctricsum;
                int w = numberwaterbegin + numberwatersum;
                if (edtbillcustomername.getText().toString().trim().equals("") || edtnumberwater.getText().toString().equals("") || edtnumberelectric.getText().toString().equals("")) {
                    Toast.makeText(AddBillActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (Integer.parseInt(edtnumberwater.getText().toString()) <= w) {
                        Toast.makeText(AddBillActivity.this, "Chỉ số cuối nước không được nhỏ hơn hoặc bằng chỉ số hiện tại: " + w, Toast.LENGTH_SHORT).show();
                        edtnumberwater.setText(null);
                    } else {
                        if (Integer.parseInt(edtnumberelectric.getText().toString()) <= e) {
                            Toast.makeText(AddBillActivity.this, "Chỉ số cuối điện không được nhỏ hơn hoặc bằng chỉ số hiện tại: " + e, Toast.LENGTH_SHORT).show();
                            edtnumberelectric.setText(null);

                        } else {
                            int bw = Integer.parseInt(edtnumberwater.getText().toString().trim()) - w;
                            int be = Integer.parseInt(edtnumberelectric.getText().toString().trim()) - e;
                            int Toatal = billroomprice + bw * room.getRoomWaterPrice() + be * room.getRoomElectricPrice();
                            dialogTotal(new Bill( room, edtbillcustomername.getText().toString(), edtdatebegin.getText().toString(), edtdateend.getText().toString(), contractID, be, bw, Calendar.getInstance().getTime(), Toatal));
                        }

                    }
                }
            }


        });


    }

    private void dialogTotal(final Bill bill) {
        new AlertDialog.Builder(this)
                .setTitle("Total")
                .setCancelable(false)
                .setMessage("Tổng số tiền của hóa đơn là: " + formatter.format(bill.getBIllTotal()) + " VND")
                .setPositiveButton("Thanh toán", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        billDAO.addBill(bill);
                        BillActivity.billList.add(bill);
                        BillActivity.adapter.notifyDataSetChanged();
                        BillActivity.checkbill();
                        finish();
                        Animatoo.animateDiagonal(AddBillActivity.this);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void settextNewbill(String datebegin, int dateterm, int monthperiodic, String name) {
        edtdatebegin.setText(datebegin);
        try {
            Calendar calendar = Calendar.getInstance();
            Date billdatebegin = simpleDateFormat.parse(datebegin);
            calendar.setTime(billdatebegin);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            int date = calendar.get(Calendar.DATE);
            calendar.set(year, month, dateterm);
            edtdateend.setText(simpleDateFormat.format(addMonth(calendar.getTime(), monthperiodic)));
            edtroomprice.setText(formatter.format(room.getRoomPrice()) + " VND");
            if (date == dateterm) {
                edtbillroomprice.setText(formatter.format(room.getRoomPrice()) + " VND");
            } else {

                int roompriceofdate = room.getRoomPrice() / getlenthmonth(calendar.getTime());
                Log.i("Addbill", "lenth: " + getlenthmonth(calendar.getTime()));
                Date billdateend = addMonth(calendar.getTime(), monthperiodic);
                long day = (billdateend.getTime() - billdatebegin.getTime()) / (24 * 60 * 60 * 1000) + 1;
                billroomprice = (int) (roompriceofdate * day);
                edtbillroomprice.setText(formatter.format(billroomprice) + " VND");
                Log.i("Addbill", "settextNewbill: " + roompriceofdate + "," + day);
            }
            edtbillcustomername.setText(name);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static Date addMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        return cal.getTime();
    }

    public static int getlenthmonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.getActualMaximum(Calendar.DATE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsave:

                break;
        }
    }


    private void initView() {
        edtdatebegin = findViewById(R.id.edtdatebegin);
        edtdateend = findViewById(R.id.edtdateend);
        edtroomprice = findViewById(R.id.edtroomprice);
        edtbillroomprice = findViewById(R.id.edtbillroomprice);
        edtbillcustomername = findViewById(R.id.edtbillcustomername);
        edtnumberwater = findViewById(R.id.edtnumberwater);
        edtnumberelectric = findViewById(R.id.edtnumberelectric);
        tvbillservice = findViewById(R.id.tvbillservice);
        btnadd = findViewById(R.id.btnsave);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tạo hóa đơn mới");
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
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }
}
