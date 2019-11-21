package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Fragment.FragmentService;
import com.example.qunlphngtr.Model.Contract;
import com.example.qunlphngtr.Model.Service;
import com.example.qunlphngtr.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ContractDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static Contract contract = new Contract();
    private TextView tvdatebegin, tvdateend, tvroomprice, tvmonth, tvcustomername, tvnumberwater, tvnumberelectric, tvdateterm, tvdeposits, tvcontractservice;
    private ImageView imgcustomer;
    private NumberFormat formatter = new DecimalFormat("#,###");
    private List<Service> serviceList;
    private String[] array;
    private String item = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);
        initView();
        toolBar();
        getDetailmodel();
    }

    private void getDetailmodel() {
        tvdeposits.setText(formatter.format(contract.getContractDeposits()) + " VND");
        tvdateterm.setText(contract.getContractDateTerm() + "");
        tvdatebegin.setText(contract.getContractDateBegin());
        tvdateend.setText(contract.getContractDateEnd());
        tvroomprice.setText(formatter.format(contract.getRoom().getRoomPrice()) + " VND");
        tvmonth.setText(contract.getContractMonthPeriodic() + " tháng");
        tvcustomername.setText(contract.getCustomer().getCustomerName() + " ");
        tvnumberwater.setText(contract.getContracNumberWaterBegin() + "");
        tvnumberelectric.setText(contract.getContracNumberElectricBegin() + "");
        imgcustomer.setImageBitmap(LoadingImg(contract.getCustomer().getCustomerImage()));
        serviceList = new ArrayList<>();
        serviceList = FragmentService.serviceList;
        ConvertListtoArrayString(serviceList);
        StringServiceItem(serviceList);
        tvcontractservice.setText(item);
        tvcontractservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceList.size() > 0) {
                    dialogService(array);
                }
            }
        });


    }

    private void ConvertListtoArrayString(List<Service> services) {
        if (services.size() > 0) {
            array = new String[services.size()];
            int index = 0;
            for (Object value : services) {
                array[index] = String.valueOf(value);
                index++;
            }
        }

    }

    private void StringServiceItem(List<Service> services) {
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

    }

    private void dialogService(String[] arrays) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Dịch vụ");
        mBuilder.setItems(arrays, null);
        mBuilder.setCancelable(false);
        mBuilder.setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    private void initView() {
        tvdeposits = findViewById(R.id.tvdeposits);
        tvdateterm = findViewById(R.id.tvdateterm);
        tvcontractservice = findViewById(R.id.tvcontractservice);
        tvdatebegin = findViewById(R.id.tvdatebegin);
        tvdateend = findViewById(R.id.tvdateend);
        tvroomprice = findViewById(R.id.tvpriceroom);
        tvmonth = findViewById(R.id.tvmonthperiodic);
        tvcustomername = findViewById(R.id.tvnamecustomer);
        tvnumberwater = findViewById(R.id.tvcontractwater);
        tvnumberelectric = findViewById(R.id.tvcontractelectric);
        imgcustomer = findViewById(R.id.imgcustomer);

    }

    private void toolBar() {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hợp đồng chi tiết");
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

    private Bitmap LoadingImg(byte[] img) {
        return BitmapFactory.decodeByteArray(img, 0, img.length);
    }

}
