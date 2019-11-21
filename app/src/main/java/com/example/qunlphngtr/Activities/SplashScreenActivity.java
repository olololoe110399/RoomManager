package com.example.qunlphngtr.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qunlphngtr.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

public class SplashScreenActivity extends AppCompatActivity {
    static int TIMEOUT_MILLIS = 4000;
    String strUserName, strPassword;
    AccessToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        token = AccessToken.getCurrentAccessToken();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (checkLoginShap() < 0) {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();


                } else {
                    if (isOnline(SplashScreenActivity.this)) {
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (token == null) {
                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            logout();
                            LoginManager.getInstance().logOut();
                            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }


                }


            }
        }, TIMEOUT_MILLIS);
    }

    public int checkLoginShap() {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        boolean chk = pref.getBoolean("REMEMBER", false);
        if (chk) {
            strUserName = pref.getString("USERNAME", "");
            strPassword = pref.getString("PASSWORD", "");
            return 1;
        }
        return -1;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void logout() {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.commit();
    }
}
