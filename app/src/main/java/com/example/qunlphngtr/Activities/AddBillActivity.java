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
import com.example.qunlphngtr.Database.BillServiceDAO;
import com.example.qunlphngtr.Database.ContractDAO;
import com.example.qunlphngtr.Model.Bill;
import com.example.qunlphngtr.Model.Contract;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.Model.Service;
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
import java.util.Stack;

public class AddBillActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText edtdatebegin, edtdateend, edtroomprice, edtbillroomprice, edtbillcustomername, edtnumberwater, edtnumberelectric;
    private Button btnadd;
    private TextView tvbillservice;
    private SimpleDateFormat simpleDateFormat;
    NumberFormat formatter;
    private Room room = RoomActivity.room;
    private double billroomprice = 0;
    private int totalBillService = 0;
    private BillDAO billDAO;
    private String TAG = "a";
    private List<Bill> billLists;
    private Contract contract;
    private List<Service> billServiceList;
    private BillServiceDAO billServiceDAO;
    private String item = "";
    private ContractDAO contractDAO;

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
        contractDAO = new ContractDAO(this);
        billLists = new ArrayList<>();
        billServiceList = new ArrayList<>();
        billServiceDAO = new BillServiceDAO(this);
        billDAO = new BillDAO(this);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        formatter = new DecimalFormat("#,###");
        contract = new Contract();
    }

    private void checkBilllist() {
        int sumnumberwater = 0;
        int sumnumberelectric = 0;
        for (int i = 0; i < BillActivity.contractList.size(); i++) {
            if (BillActivity.contractList.get(i).getContractstatus() == 0) {
                contract = BillActivity.contractList.get(i);
            }
        }
        billLists = billDAO.getBillByContractID(contract.getContractID());
        for (int i = 0; i < billLists.size(); i++) {
            sumnumberwater = sumnumberwater + billLists.get(i).getBillWaterNumber();
            sumnumberelectric = sumnumberelectric + billLists.get(i).getBillElectricNumber();
        }
        if (billLists.size() > 0) {
            Bill bill = billLists.get(billLists.size() - 1);
            Date billDateEnd = null;
            Date contractDateEnd = null;
            try {
                billDateEnd = simpleDateFormat.parse(bill.getBillDateEnd());
                contractDateEnd = simpleDateFormat.parse(contract.getContractDateEnd());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(billDateEnd);
            cal.add(Calendar.MONTH, contract.getContractMonthPeriodic());
            if (cal.getTime().before(contractDateEnd)) {
                settextNewbill(bill.getBillDateEnd(), contract.getContractDateTerm(), contract.getContractMonthPeriodic(), contract.getCustomer().getCustomerName());
                edtbillroomprice.setText(formatter.format(contract.getRoom().getRoomPrice() * contract.getContractMonthPeriodic()) + " VND");
                addBill(contract.getRoom().getRoomPrice() * contract.getContractMonthPeriodic(), contract.getContracNumberWaterBegin(), sumnumberwater, contract.getContracNumberElectricBegin(), sumnumberelectric, contract.getContractID());
            } else {
                cal.add(Calendar.MONTH, -contract.getContractMonthPeriodic());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(contractDateEnd);
                settextNewbill(bill.getBillDateEnd(), calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) - cal.get(Calendar.MONTH), contract.getCustomer().getCustomerName());
                addBill(billroomprice, contract.getContracNumberWaterBegin(), sumnumberwater, contract.getContracNumberElectricBegin(), sumnumberelectric, contract.getContractID());
            }

        } else {
            settextNewbill(contract.getContractDateBegin(), contract.getContractDateTerm(), contract.getContractMonthPeriodic(), contract.getCustomer().getCustomerName());
            addBill(billroomprice, contract.getContracNumberWaterBegin(), sumnumberwater, contract.getContracNumberElectricBegin(), sumnumberelectric, contract.getContractID());

        }


    }


    private void addBill(final double billroomprice, final int numberwaterbegin, final int numberwatersum, final int numberelectricbegin, final int numberelctricsum, final int contractID) {

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
                            Log.i(TAG, "Total Bill service: " + totalBillService);
                            double Toatal = billroomprice + bw * room.getRoomWaterPrice() + be * room.getRoomElectricPrice() + totalBillService;
                            dialogTotal(new Bill(room, edtbillcustomername.getText().toString(), edtdatebegin.getText().toString(), edtdateend.getText().toString(), contractID, be, bw, Calendar.getInstance().getTime(), Toatal));
                        }

                    }
                }
            }


        });


    }

    private String StringServiceItem(List<Service> services) {
        if (services.size() > 0) {


            for (int j = 0; j < services.size(); j++) {
                item = item + services.get(j).getServiceName();
                if (j != services.size() - 1) {
                    item = item + ", ";
                }
            }
        } else {
            item = "Không có dịch vụ nào khác";
        }
        return item;
    }


    private void dialogTotal(final Bill bill) {
        String mes = "";
        Date billDateEnd = null;
        Date contractDateEnd = null;
        try {
            billDateEnd = simpleDateFormat.parse(bill.getBillDateEnd());
            contractDateEnd = simpleDateFormat.parse(contract.getContractDateEnd());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (billDateEnd.compareTo(contractDateEnd) == 0) {
            String mes1 = "";
            if ((bill.getBIllTotal() - contract.getContractDeposits()) > 0) {
                mes1 = "\nNên tổng tiền thanh toán là:" + formatter.format(bill.getBIllTotal() - contract.getContractDeposits()) + " VND";

            } else if ((bill.getBIllTotal() - contract.getContractDeposits()) == 0) {
                mes1 = "\nNên tổng tiền thanh toán là:" + formatter.format(bill.getBIllTotal() - contract.getContractDeposits()) + " VND";

            } else {
                mes1 = "\nNên tổng tiền trả lại khách là:" + formatter.format(contract.getContractDeposits() - bill.getBIllTotal()) + " VND";
            }
            mes = "\nVì đây là hóa đơn cuối của hợp đồng" +
                    "\n Tiền cọc trả lại khách là:" + formatter.format(contract.getContractDeposits()) + " VND" +
                    mes1;

        }
        Date finalBillDateEnd = billDateEnd;
        Date finalContractDateEnd = contractDateEnd;
        new AlertDialog.Builder(this)
                .setTitle("Total")
                .setCancelable(false)
                .setMessage("Tổng số tiền của hóa đơn là: " + formatter.format(bill.getBIllTotal()) + " VND" + mes)
                .setPositiveButton("Thanh toán", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        bill.setBIllTotal(bill.getBIllTotal());
                        if (finalBillDateEnd.compareTo(finalContractDateEnd) == 0) {
                            contractDAO.updateContractStatus(contract.getContractID());
                            contractDAO.updateContractDeposits(contract.getContractID());
                            bill.setBIllTotal(bill.getBIllTotal() - contract.getContractDeposits());
                        }

                        bill.setBillDebtsToPay(0);
                        billDAO.addBill(bill);
                        BillActivity.p = 1;
                        BillActivity.spnBillFilter.setSelection(1);
                        BillActivity.checkBill2Null();

                        finish();
                        Animatoo.animateSlideRight(AddBillActivity.this);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Nợ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bill.setBillDebtsToPay(bill.getBIllTotal());
                        if (finalBillDateEnd.compareTo(finalContractDateEnd) == 0) {
                            contractDAO.updateContractStatus(contract.getContractID());
                            contractDAO.updateContractDeposits(contract.getContractID());
                            bill.setBillDebtsToPay(bill.getBIllTotal() - contract.getContractDeposits());
                        }
                        bill.setBIllTotal(0);
                        billDAO.addBill(bill);
                        BillActivity.p = 2;
                        BillActivity.spnBillFilter.setSelection(2);
                        BillActivity.checkBill2Null();
                        finish();
                        Animatoo.animateSlideRight(AddBillActivity.this);


                    }
                })
                .show();
    }

    private void settextNewbill(String Date, int dateterm, int monthperiodic, String name) {
        edtdatebegin.setText(Date);
        try {
            Calendar calendar = Calendar.getInstance();
            Date billdatebegin = simpleDateFormat.parse(Date);
            calendar.setTime(billdatebegin);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            int date = calendar.get(Calendar.DATE);
            calendar.set(year, month, dateterm);
            edtdateend.setText(simpleDateFormat.format(addMonth(calendar.getTime(), monthperiodic)));
            edtroomprice.setText(formatter.format(room.getRoomPrice()) + " VND");
            if (date == dateterm) {
                if (monthperiodic == 0) {
                    billroomprice = contract.getRoom().getRoomPrice();
                } else
                    billroomprice = contract.getRoom().getRoomPrice() * monthperiodic;
                edtbillroomprice.setText(formatter.format(billroomprice) + " VND");
            } else {
                if (monthperiodic == 0) {
                    billroomprice = RoomPricenull(billdatebegin, dateterm, room.getRoomPrice());
                } else
                    billroomprice = RoomPrice(billdatebegin, monthperiodic, dateterm, room.getRoomPrice());
                edtbillroomprice.setText(formatter.format(billroomprice) + " VND");
            }
            edtbillcustomername.setText(name);
            billServiceList = billServiceDAO.getsServiceBillByID(contract.getContractID());
            for (int i = 0; i < billServiceList.size(); i++) {
                if (monthperiodic == 0) {
                    totalBillService = totalBillService + billServiceList.get(i).getServicePrice();
                } else {
                    totalBillService = totalBillService + billServiceList.get(i).getServicePrice() * monthperiodic;
                }
            }
            tvbillservice.setText(StringServiceItem(billServiceList));


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private double RoomPrice(Date start, int monthperiodic, int dayterm, double roomPrice) {
        double price = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        for (int i = 1; i <= monthperiodic; i++) {
            if (i == monthperiodic) {
                cal.add(Calendar.MONTH, i - 1);
                Date date1 = cal.getTime();
                cal.add(Calendar.MONTH, 1);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                cal.set(year, month, dayterm);
                Date date2 = cal.getTime();
                long day = (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
                double roomprice = roomPrice / getlenthmonth(date1);
                price = price + roomprice * day;
                Log.i("Day", "RoomPrice: " + simpleDateFormat.format(date2) + " " + simpleDateFormat.format(date1) + " " + getlenthmonth(date1) + " " + day + " " + roomprice);
            } else {
                price = price + roomPrice;
            }

        }
        return price;
    }

    private double RoomPricenull(Date start, int dayterm, double roomPrice) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        cal.set(year, month, dayterm);
        Date date2 = cal.getTime();
        long day = (date2.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);
        double roomprice = roomPrice / getlenthmonth(start);
        double price = roomprice * day;
        Log.i("Day", "RoomPrice: " + simpleDateFormat.format(date2) + " " + simpleDateFormat.format(start) + " " + getlenthmonth(start) + " " + day + " " + roomprice);

        return price;
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
        tvbillservice = findViewById(R.id.tvbillservice);
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
