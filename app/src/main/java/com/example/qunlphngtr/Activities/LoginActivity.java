package com.example.qunlphngtr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.qunlphngtr.Fragment.Fragment_login.Fragment_Signin;
import com.example.qunlphngtr.Fragment.Fragment_login.Fragment_Signup;
import com.example.qunlphngtr.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    public static Button btnin, btnout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Fragment_Signin fragment_signin = new Fragment_Signin();
        loadFragment(fragment_signin);
        initView();

    }

    private void initView() {
        btnin = findViewById(R.id.btnSignin);
        btnout = findViewById(R.id.btnSingnup);


    }

    public void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.btnSignin:
                btnin.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ColorWhite));
                btnin.setBackgroundResource(R.drawable.btnsginin1);
                btnout.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Color4));
                btnout.setBackgroundResource(R.drawable.btnsignup2);
                fragment = new Fragment_Signin();
                loadFragment(fragment);
                break;
            case R.id.btnSingnup:
                btnin.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Color4));
                btnin.setBackgroundResource(R.drawable.btnsignin2);
                btnout.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ColorWhite));
                btnout.setBackgroundResource(R.drawable.btnsignup1);
                fragment = new Fragment_Signup();
                loadFragment(fragment);
                break;
        }
    }
    public void backsignin(){
        btnin.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ColorWhite));
        btnin.setBackgroundResource(R.drawable.btnsginin1);
        btnout.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Color4));
        btnout.setBackgroundResource(R.drawable.btnsignup2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container2);
        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container2, fragment);
        transaction.commit();
    }

//    @Override
//    public void onBackPressed() {
//        int stackCount = getSupportFragmentManager().getBackStackEntryCount();
//        if (stackCount == 0) {
//            finish();
//            super.onBackPressed();
//        } else {
//            btnin.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ColorWhite));
//            btnin.setBackgroundResource(R.drawable.btnsginin1);
//            btnout.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color));
//            btnout.setBackgroundResource(R.drawable.btnsignup2);
//            loadFragment(new Fragment_Signin());
//        }
//    }
// Lấy Hashkey của Facebook
    private void gethashkey() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.qunlphngtr", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

}
