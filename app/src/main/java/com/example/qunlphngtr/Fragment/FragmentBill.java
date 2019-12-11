package com.example.qunlphngtr.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qunlphngtr.Activities.BillActivity;
import com.example.qunlphngtr.Adapter.AdapterBill;
import com.example.qunlphngtr.Adapter.AdapterBill2;
import com.example.qunlphngtr.Database.BillDAO;
import com.example.qunlphngtr.Database.ContractDAO;
import com.example.qunlphngtr.Model.Bill;
import com.example.qunlphngtr.Model.Contract;
import com.example.qunlphngtr.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentBill extends Fragment {
    private RecyclerView recyclerView;
    private List<Bill> billList;
    private AdapterBill2 adapter;
    private RelativeLayout relativeLayout;
    private BillDAO billDAO;
    public static Spinner spnBillFilter;
    String[] categories = {"Tất cả", "Đã thanh toán", "Chưa thanh toán"};
    private List<Bill> billList2;
    private View view;
    public static boolean status=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bill, container, false);
        initView();
        checkBillNull();
        return view;
    }

    private void initView() {
        spnBillFilter = view.findViewById(R.id.spnBillFilter);
        spnBillFilter.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categories));
        spnBillFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < categories.length) {
                    getSelectedCategoryData(position);
                } else {
                    Toast.makeText(getActivity(), "Danh mục đã chọn không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        billDAO = new BillDAO(getActivity());
        relativeLayout = view.findViewById(R.id.rlbillnull);
        recyclerView = view.findViewById(R.id.rvbill);
        billList = new ArrayList<>();
        billList2 = new ArrayList<>();
        billList = billDAO.getAllBill();

        recyclerView.setNestedScrollingEnabled(false);
        adapter = new AdapterBill2(billList, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void getSelectedCategoryData(int position) {
        billList.clear();
        billList.addAll(billDAO.getAllBill());
        billList2.clear();
        if (position == 0) {

            adapter = new AdapterBill2(billList, getActivity());
            checkBillNull();
        } else if (position == 1) {
            for (Bill bill : billList) {
                if (bill.getBillDebtsToPay() == 0) {
                    billList2.add(bill);
                }
            }
            adapter = new AdapterBill2(billList2, getActivity());
            checkBill2Null();
        } else {
            for (Bill bill : billList) {
                if (bill.getBIllTotal() == 0) {
                    billList2.add(bill);
                }
            }
            adapter = new AdapterBill2(billList2, getActivity());
            checkBill2Null();
        }
        recyclerView.setAdapter(adapter);

    }

    private void checkBillNull() {
        if (billList.size() > 0) {
            relativeLayout.setVisibility(View.GONE);
        } else {

            relativeLayout.setVisibility(View.VISIBLE);
        }


    }

    private void checkBill2Null() {
        if (billList2.size() > 0) {
            relativeLayout.setVisibility(View.GONE);

        } else {

            relativeLayout.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        status=true;
    }
}
