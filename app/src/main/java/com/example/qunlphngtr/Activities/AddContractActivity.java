package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Adapter.AdapterCustomerSpinner;
import com.example.qunlphngtr.Database.ContractDAO;
import com.example.qunlphngtr.Fragment.FragmentService;
import com.example.qunlphngtr.Model.Contract;
import com.example.qunlphngtr.Model.Customer;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.Model.Service;
import com.example.qunlphngtr.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddContractActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Spinner spinner;
    private SimpleDateFormat simpleDateFormat;
    private List<Service> serviceList;
    private TextView tvbillservice;
    private EditText edtdatebegin, edtdateend, edtmonthperiodic, edtdateterm, edtnumberelectric, edtnumberwater, edtpeople, edtvehical, edtdeposits;
    private boolean[] checkedItems;
    private ArrayList<String> mServiceItems;
    private int monthperiodic, idate, imonth, iyear, dateterm;
    private Calendar calendar;
    private String[] array;
    private Customer customer;
    private Room room = RoomActivity.room;
    private List<Customer> customerList;
    private AdapterCustomerSpinner adapterCustomerSpinner;
    private ContractDAO contractDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contract);
        initView();
        initObject();
        initToolbar();
        getDatenow();
        spinnercustomer();
        setupOnclick();
        setupText();

    }

    private void setupText() {
        edtmonthperiodic.setText("1 tháng");
        monthperiodic = 1;
        edtdateterm.setText("1");
        dateterm = 1;
    }

    private void getDatenow() {
        calendar = Calendar.getInstance();
        idate = calendar.get(Calendar.DATE);
        imonth = calendar.get(Calendar.MONTH);
        iyear = calendar.get(Calendar.YEAR);
        calendar.set(iyear, imonth, idate);
        edtdatebegin.setText(simpleDateFormat.format(calendar.getTime()));
        calendar.set(iyear + 1, imonth, idate);
        edtdateend.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private void initObject() {
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        serviceList = new ArrayList<>();
        ConvertListtoArrayString();
        mServiceItems = new ArrayList<>();
        contractDAO = new ContractDAO(this);

    }

    private void ConvertListtoArrayString() {
        array = new String[FragmentService.serviceList.size()];
        int index = 0;
        for (Object value : FragmentService.serviceList) {
            array[index] = String.valueOf(value);
            index++;
        }
        checkedItems = new boolean[array.length];
    }

    private void initView() {
        edtdeposits = findViewById(R.id.edtdeposits);
        edtpeople = findViewById(R.id.edtpeople);
        edtvehical = findViewById(R.id.edtvehical);
        edtnumberelectric = findViewById(R.id.edtnumberelectric);
        edtnumberwater = findViewById(R.id.edtnumberwater);
        edtdateterm = findViewById(R.id.edtdateterm);
        edtmonthperiodic = findViewById(R.id.edtmonthperiodic);
        tvbillservice = findViewById(R.id.tvbillservice);
        edtdatebegin = findViewById(R.id.edtdatebegin);
        edtdateend = findViewById(R.id.edtdateend);
    }

    private void setupOnclick() {
        edtdateterm.setOnClickListener(this);
        edtmonthperiodic.setOnClickListener(this);
        tvbillservice.setOnClickListener(this);
        edtdatebegin.setOnClickListener(this);
        edtdateend.setOnClickListener(this);
    }

    private void spinnercustomer() {
        spinner = findViewById(R.id.spncustomer);
        customerList = new ArrayList<>();
        adapterCustomerSpinner = new AdapterCustomerSpinner(customerList, this, R.layout.item_spinner_customer);
        spinner.setAdapter(adapterCustomerSpinner);
        new spinnercustomer().execute();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                customer = customerList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private class spinnercustomer extends AsyncTask<Void, Void, List<Customer>> {

        @Override
        protected List<Customer> doInBackground(Void... voids) {

            customerList.add(new Customer("1", LoadingImg(R.drawable.avatar), "0793333648", "Nguyễn Ngọc Duy", 201729145));
            customerList.add(new Customer("2", LoadingImg(R.drawable.avatar), "0983164856", "Bùi Nguyễn Quế Anh", 201729145));
            customerList.add(new Customer("3", LoadingImg(R.drawable.avatar), "0326243624", "Nguyễn Quốc Việt", 201729145));
            customerList.add(new Customer("4", LoadingImg(R.drawable.avatar), "0904194212", "Trần Phương Nam", 201729145));
            return customerList;
        }

        @Override
        protected void onPostExecute(List<Customer> customers) {
            super.onPostExecute(customers);
            adapterCustomerSpinner.notifyDataSetChanged();

        }
    }

    private byte[] LoadingImg(int IDImg) {
        Drawable drawable = getDrawable(IDImg);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void dialogchoosedayterm() {
        int checkedItem = -1;
        if (dateterm > 0) {
            checkedItem = dateterm - 1;
        }

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Chọn chu kỳ trả tiền");
        mBuilder.setSingleChoiceItems(getResources().getStringArray(R.array.dayterm), checkedItem, null);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dateterm = ((AlertDialog) dialog).getListView().getCheckedItemPosition() + 1;
                edtdateterm.setText(dateterm + "");
            }
        });
        mBuilder.setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();

        mDialog.show();
    }

    private void dialogchoosemonthperiodic() {
        int checkeditem = -1;
        switch (monthperiodic) {
            case 1:
                checkeditem = 0;
                break;
            case 2:
                checkeditem = 1;
                break;
            case 3:
                checkeditem = 2;
                break;
            case 6:
                checkeditem = 3;
                break;
            case 12:
                checkeditem = 4;
                break;
        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Chọn ngày chốt tiền");
        mBuilder.setSingleChoiceItems(getResources().getStringArray(R.array.MonthPeriodic), checkeditem, null);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (((AlertDialog) dialog).getListView().getCheckedItemPosition()) {
                    case 0:
                        monthperiodic = 1;
                        break;
                    case 1:
                        monthperiodic = 2;
                        break;
                    case 2:
                        monthperiodic = 3;
                        break;
                    case 3:
                        monthperiodic = 6;
                        break;
                    case 4:
                        monthperiodic = 12;
                        break;
                }
                edtmonthperiodic.setText(getResources().getStringArray(R.array.MonthPeriodic)[((AlertDialog) dialog).getListView().getCheckedItemPosition()]);
            }
        });
        mBuilder.setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ký hợp đồng mới");
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
        switch (v.getId()) {
            case R.id.tvbillservice:
                addservice(array);
                break;
            case R.id.edtdatebegin:
                dialogdatebegin();
                break;
            case R.id.edtdateend:
                dialogdateend();
                break;
            case R.id.edtmonthperiodic:
                dialogchoosemonthperiodic();
                break;
            case R.id.edtdateterm:
                dialogchoosedayterm();
                break;
            case R.id.btnsave:
                addContract();
                break;

        }
    }

    private void addContract() {
        if (edtdatebegin.getText().toString().equals("")
                || edtdateend.getText().toString().equals("")
                || edtdateterm.getText().toString().equals("")
                || edtmonthperiodic.getText().toString().equals("")
                || edtnumberelectric.getText().toString().equals("")
                || edtnumberwater.getText().toString().equals("")
                || edtpeople.getText().toString().equals("")
                || edtvehical.getText().toString().equals("")
                || edtdeposits.getText().toString().equals("")
        ) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            Contract contract = new Contract();
            contract.setContractID("HD" + room.getRoomID() + customer.getCustomerID() + idate + iyear);
            contract.setContractDateBegin(edtdatebegin.getText().toString());
            contract.setContractDateEnd(edtdateend.getText().toString());
            contract.setContractMonthPeriodic(monthperiodic);
            contract.setContractDateTerm(dateterm);
            contract.setContracNumberElectricBegin(Integer.parseInt(edtnumberelectric.getText().toString()));
            contract.setContracNumberWaterBegin(Integer.parseInt(edtnumberwater.getText().toString()));
            contract.setRoom(room);
            contract.setCustomer(customer);
            contract.setContractPeopleNumber(Integer.parseInt(edtnumberwater.getText().toString()));
            contract.setContractVehicleNumber(Integer.parseInt(edtvehical.getText().toString()));
            contract.setContractstatus(0);
            contract.setContractDeposits(Double.parseDouble(edtdeposits.getText().toString()));
            if (contractDAO.addContract(contract) > 0) {
                ContractActivity.contractList.add(contract);
                ContractActivity.adapter.notifyDataSetChanged();
                ContractActivity.checkcontract();
                addbillservice(contract.getContractID());
            } else {
                Toast.makeText(this, "Chưa thêm được!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void addbillservice(String contractID) {
        try {
            for (Service service : serviceList) {
                service.setContracID(contractID);
                //lưu hoá đơn dịch vụ
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());

        }
        dialogSuccessfully();

    }

    private void dialogSuccessfully() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Thành công");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Animatoo.animateDiagonal(AddContractActivity.this);
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();


    }

    private void checkmServiceText(boolean check, int position) {
        String a = FragmentService.serviceList.get(position).getServiceName();
        if (check == true) {
            int pos = -1;
            for (int i = 0; i < mServiceItems.size(); i++) {
                if (mServiceItems.get(i) == a) {
                    pos = 1;
                    break;
                }
            }
            if (pos == -1) {
                mServiceItems.add(FragmentService.serviceList.get(position).getServiceName());
            }

        } else {
            for (int i = 0; i < mServiceItems.size(); i++) {
                if (mServiceItems.get(i) == a) {
                    mServiceItems.remove(i);
                }

            }

        }
    }

    private void checkServiceItems(boolean check, int position) {
        Service service = FragmentService.serviceList.get(position);
        if (check == true) {
            int pos = -1;
            for (int i = 0; i < serviceList.size(); i++) {
                if (serviceList.get(i).getServiceID() == service.getServiceID()) {
                    pos = 1;
                    break;
                }
            }
            if (pos == -1) {
                serviceList.add(new Service(service.getServiceID(), service.getServiceName(), service.getServicePrice()));
            }

        } else {
            for (int i = 0; i < serviceList.size(); i++) {
                if (serviceList.get(i).getServiceID() == service.getServiceID()) {
                    serviceList.remove(i);
                }

            }

        }
    }

    private void addservice(String[] array) {
        if (serviceList.size() > 0) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("Dịch vụ");
            mBuilder.setMultiChoiceItems(array, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                    checkmServiceText(isChecked, position);
                    checkServiceItems(isChecked, position);
                }
            });

            mBuilder.setCancelable(false);
            mBuilder.setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    String item = "";
                    for (int i = 0; i < mServiceItems.size(); i++) {
                        item = item + mServiceItems.get(i);
                        if (i != mServiceItems.size() - 1) {
                            item = item + ", ";
                        }
                    }
                    tvbillservice.setText(item);
                }
            });

            mBuilder.setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });


            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        } else {
            Toast.makeText(this, "Bạn chưa tạo dịch vụ nào khác", Toast.LENGTH_SHORT).show();
        }

    }

    private void dialogdatebegin() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                edtdatebegin.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, iyear, imonth, idate);
        datePickerDialog.show();

    }

    private void dialogdateend() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                edtdateend.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, iyear + 1, imonth, idate);
        datePickerDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }

}
