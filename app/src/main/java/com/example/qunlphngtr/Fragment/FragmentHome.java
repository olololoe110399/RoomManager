package com.example.qunlphngtr.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qunlphngtr.Database.BillDAO;
import com.example.qunlphngtr.Database.ContractDAO;
import com.example.qunlphngtr.Database.RoomDAO;
import com.example.qunlphngtr.Model.Room;
import com.example.qunlphngtr.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentHome extends Fragment {
    private View view;
    private TextView tvDate, tvRoomnull, tvRoomnotnull, tvBill1, tvBill0, tvRevenue, tvDeposits, tvAllElectricNumber, tvAllWaterNumber, tvContract0, tvContract1;
    private Spinner spinner;
    private List<Room> roomList;
    private RoomDAO roomDAO;
    private SimpleDateFormat simpleDateFormat;
    private String stDatebegin = "all", stDateend = "all";
    private ContractDAO contractDAO;
    private BillDAO billDAO;
    private NumberFormat formatter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initObject();
        setSpinner();
        eventSelectedItemSpinner();
        eventOnclickChoseDate();
        setStatistics(0, stDatebegin, stDateend);
        return view;
    }

    private void eventOnclickChoseDate() {
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChoseDate();
            }
        });
    }

    private void dialogChoseDate() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_add_date);
        dialog.getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        dialog.setCancelable(false);
        EditText edtdatebegin, edtdateend;
        Button btnadd, btnback;
        btnadd = dialog.findViewById(R.id.btnadd);
        btnback = dialog.findViewById(R.id.btnback);
        edtdatebegin = dialog.findViewById(R.id.edtdatebegin);
        edtdateend = dialog.findViewById(R.id.edtdateend);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        edtdatebegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdatePickerDialog(edtdatebegin);
            }
        });
        edtdateend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdatePickerDialog(edtdateend);
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtdatebegin.getText().toString().equals("") || edtdateend.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    long date = -1;
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(simpleDateFormat.parse(edtdatebegin.getText().toString()));
                        Date datebegin = calendar.getTime();
                        stDatebegin = new SimpleDateFormat("yyyy/MM").format(datebegin);
                        calendar.setTime(simpleDateFormat.parse(edtdateend.getText().toString()));
                        Date dateend = calendar.getTime();
                        stDateend = new SimpleDateFormat("yyyy/MM").format(dateend);
                        date = dateend.getTime() - datebegin.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date >= 0) {
                        tvDate.setText(stDatebegin + "-" + stDateend);
                        setStatistics(getPosSpinner(spinner.getSelectedItem().toString()), stDatebegin, stDateend);
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();
    }

    private void eventSelectedItemSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setStatistics(position, stDatebegin, stDateend);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinner() {
        roomList.clear();
        roomList.add(new Room("Tất cả phòng"));
        roomList.addAll(roomDAO.getAllRoom());
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, roomList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void initObject() {
        formatter = new DecimalFormat("#,###");
        billDAO = new BillDAO(getActivity());
        contractDAO = new ContractDAO(getActivity());
        simpleDateFormat = new SimpleDateFormat("MM/yyyy");
        roomList = new ArrayList<>();
        roomDAO = new RoomDAO(getActivity());
    }

    private void setStatistics(int spn, String datebegin, String dateend) {
        if (spn == 0) {
            if (datebegin.equals("all") || dateend.equals("all")) {
                tvAllElectricNumber.setText(billDAO.getSumNumberElectric() + "");
                tvAllWaterNumber.setText(billDAO.getSumNumberWater() + "");
                tvBill1.setText(formatter.format(billDAO.getAllSumbillTotal()) + " VND");
                tvRevenue.setText(formatter.format(billDAO.getAllSumbillTotal()) + "VND");
                tvBill0.setText(formatter.format(billDAO.getAllSumbillDebtsToPay()) + " VND");
            } else {
                tvAllElectricNumber.setText(billDAO.getSumNumberElectricbyDate(datebegin, dateend) + "");
                tvAllWaterNumber.setText(billDAO.getSumNumberWatericbyDate(datebegin, dateend) + "");
                tvBill1.setText(formatter.format(billDAO.getAllSumbillTotalbyDate(datebegin, dateend)) + " VND");
                tvRevenue.setText(formatter.format(billDAO.getAllSumbillTotalbyDate(datebegin, dateend)) + " VND");
                tvBill0.setText(formatter.format(billDAO.getAllSumbillDebtsToPaybyDate(datebegin, dateend)) + " VND");
            }
            tvRoomnull.setText((roomList.size() - 1 - contractDAO.getallStatusRoom()) + "");
            tvRoomnotnull.setText(contractDAO.getallStatusRoom() + "");
            tvContract0.setText(contractDAO.getallNumberRoomNotNull() + "");
            tvContract1.setText("" + contractDAO.getallNumberRoom());
            tvDeposits.setText(formatter.format(contractDAO.getAllSumDeposits()) + " VND");
        } else {
            if (datebegin.equals("all") || dateend.equals("all")) {
                tvAllElectricNumber.setText(billDAO.getSumNumberElectricbyroomID(roomList.get(spn).getRoomID()) + "");
                tvAllWaterNumber.setText(billDAO.getSumNumberWaterbyroomID(roomList.get(spn).getRoomID()) + "");
                tvBill1.setText(formatter.format(billDAO.getAllSumbillTotalbyRoomID(roomList.get(spn).getRoomID())) + " VND");
                tvRevenue.setText(formatter.format(billDAO.getAllSumbillTotalbyRoomID(roomList.get(spn).getRoomID())) + "VND");
                tvBill0.setText(formatter.format(billDAO.getAllSumbillDebtsToPaybyRoomID(roomList.get(spn).getRoomID())) + " VND");
            } else {
                tvAllElectricNumber.setText(billDAO.getSumNumberElectricbyDateandRoomID(datebegin, dateend, roomList.get(spn).getRoomID()) + "");
                tvAllWaterNumber.setText(billDAO.getSumNumberWaterbyDateandRoomID(datebegin, dateend, roomList.get(spn).getRoomID()) + "");
                tvBill1.setText(formatter.format(billDAO.getAllSumbillTotalbyDateandRoomID(datebegin, dateend, roomList.get(spn).getRoomID())) + " VND");
                tvRevenue.setText(formatter.format(billDAO.getAllSumbillTotalbyDateandRoomID(datebegin, dateend, roomList.get(spn).getRoomID())) + " VND");
                tvBill0.setText(formatter.format(billDAO.getAllSumbillDebtsToPaybyDateandRoomID(datebegin, dateend, roomList.get(spn).getRoomID())) + " VND");
            }
            tvRoomnull.setText((roomList.size() - 1 - contractDAO.getallStatusRoom()) + "");
            tvRoomnotnull.setText(contractDAO.getallStatusRoom() + "");
            tvContract0.setText(contractDAO.getallNumberRoomNotNullbyIDroom(roomList.get(spn).getRoomID()) + "");
            tvContract1.setText("" + contractDAO.getallNumberRoombyRoomID(roomList.get(spn).getRoomID()));
            tvDeposits.setText(formatter.format(contractDAO.getAllSumDepositsbyRoomID(roomList.get(spn).getRoomID())) + " VND");
        }

    }

    private void initView() {
        spinner = view.findViewById(R.id.spnRoom);
        tvDate = view.findViewById(R.id.tvDate);
        tvRoomnull = view.findViewById(R.id.tvRoomnull);
        tvRoomnotnull = view.findViewById(R.id.tvRoomnotnull);
        tvBill1 = view.findViewById(R.id.tvBill1);
        tvBill0 = view.findViewById(R.id.tvBill0);
        tvRevenue = view.findViewById(R.id.tvRevenue);
        tvDeposits = view.findViewById(R.id.tvDeposits);
        tvAllElectricNumber = view.findViewById(R.id.tvAllElectricNumber);
        tvAllWaterNumber = view.findViewById(R.id.tvAllWaterNumber);
        tvContract0 = view.findViewById(R.id.tvContract0);
        tvContract1 = view.findViewById(R.id.tvContract1);
    }

    private void setdatePickerDialog(EditText editText) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(year, month, dayOfMonth);
                editText.setText(simpleDateFormat.format(cal.getTime()));
            }
        },
                year, month, day) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            }
        };
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private int getPosSpinner(String a) {
        int i = 0;
        for (int j = 0; j < roomList.size(); j++) {
            if (a.equals(roomList.get(j).getRoomName())) {
                i = j;
            }
        }
        return i;
    }


}


