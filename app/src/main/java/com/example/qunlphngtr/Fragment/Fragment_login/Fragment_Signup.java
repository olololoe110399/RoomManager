package com.example.qunlphngtr.Fragment.Fragment_login;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.qunlphngtr.Activities.LoginActivity;
import com.example.qunlphngtr.Database.ManagerUsers;
import com.example.qunlphngtr.Model.Users;
import com.example.qunlphngtr.R;

import java.io.ByteArrayOutputStream;


public class Fragment_Signup extends Fragment {
    Button btnreister;
    EditText edtuser, edtpass, edtrepass;
    View view;
    TextView tvsingn;
    ManagerUsers managerUsers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        initVieẉ();
        btnreister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        tvsingn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) getActivity()).backsignin();
                loadFragment(new Fragment_Signin());

            }
        });
        return view;
    }

    private void initVieẉ() {
        btnreister = view.findViewById(R.id.btnregisger);
        edtuser = view.findViewById(R.id.edtuser);
        edtpass = view.findViewById(R.id.edtpassword);
        edtrepass = view.findViewById(R.id.edtrepassword);
        managerUsers = new ManagerUsers(getActivity());
        tvsingn = view.findViewById(R.id.tvsignin);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container2, fragment);
        transaction.commit();
    }

    public void register() {
        if (edtuser.getText().toString().equals("") || edtpass.getText().toString().equals("") || edtrepass.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Không được bỏ trống!", Toast.LENGTH_SHORT).show();
        } else {
            if (!isValidEmail(edtuser.getText().toString())) {
                Toast.makeText(getActivity(), R.string.error_valid_email, Toast.LENGTH_SHORT).show();
            } else {
                if (!edtpass.getText().toString().equals(edtrepass.getText().toString())) {

                    Toast.makeText(getActivity(), R.string.error_password_match, Toast.LENGTH_SHORT).show();

                } else {

                    Users users = new Users();
                    users.setUserName(edtuser.getText().toString());
                    users.setUserPassword(edtpass.getText().toString());
                    if (managerUsers.inserUser(users) > 0) {
                        edtuser.setText(null);
                        edtpass.setText(null);
                        edtrepass.setText(null);
                        Toast.makeText(getActivity(), R.string.text_register_successful, Toast.LENGTH_SHORT).show();
                    } else {
                        edtuser.setText(null);
                        Toast.makeText(getActivity(), R.string.error_email_exists, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
