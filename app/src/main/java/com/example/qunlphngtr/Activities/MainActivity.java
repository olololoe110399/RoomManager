package com.example.qunlphngtr.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.qunlphngtr.Database.ManagerUsers;
import com.example.qunlphngtr.Fragment.FragmentBill;
import com.example.qunlphngtr.Fragment.FragmentCustomer;
import com.example.qunlphngtr.Fragment.FragmentHome;
import com.example.qunlphngtr.Fragment.FragmentRoom;
import com.example.qunlphngtr.Fragment.FragmentService;
import com.example.qunlphngtr.Fragment.FragmentUser;
import com.example.qunlphngtr.Model.Notification;
import com.example.qunlphngtr.Model.Users;
import com.example.qunlphngtr.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomnavigation;
    public static View headerView;
    private AccessToken token;
    public static ManagerUsers managerUsers;
    public static Users users;
    private SharedPreferences pref;
    private String messnotification;
    public static String UserNameSp;
    private TextView textNotificationItemCount;
    int menuitemid, startingPosition;
    public static int mNotificationItemCount;
    private SimpleDateFormat simpleDateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initObject();
        initToolbar();
        setProfileNavigationView();
        setupNavigation();
        addNotificationUpdateInformationUser();
        CheckcountNotification();


    }

    private void addNotificationUpdateInformationUser() {
        if (managerUsers.checkInformatioNull(UserNameSp) > 0) {
            if (checkmessNotification() < 0) {
                messnotification = "Xin chào " + UserNameSp + "! Vui lòng cập nhật đầy đủ thông tin của bạn ! ";
                NotificationActivity.notificationList.add(new Notification(messnotification, simpleDateFormat.format(Calendar.getInstance().getTime()), true));
            }
        }
    }

    public static void CheckcountNotification() {
        if (checkstatusNotification() < 0) {
            mNotificationItemCount = 0;
        } else {
            for (int i = 0; i < NotificationActivity.notificationList.size(); i++) {
                if (NotificationActivity.notificationList.get(i).isStatus() == true) {
                    mNotificationItemCount = mNotificationItemCount + 1;
                }
            }
        }

    }

    public static int checkstatusNotification() {
        int i = -1;
        for (int j = 0; j < NotificationActivity.notificationList.size(); j++) {
            if (NotificationActivity.notificationList.get(j).isStatus() == true) {
                i = 1;
            }
        }
        return i;
    }

    private int checkmessNotification() {
        int i = -1;
        for (int j = 0; j < NotificationActivity.notificationList.size(); j++) {
            if (NotificationActivity.notificationList.get(j).getMessage().equals(messnotification)) {
                i = 1;
            }
        }
        return i;
    }


    private void initObject() {

        managerUsers = new ManagerUsers(this);
        users = new Users();
        pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        UserNameSp = pref.getString("USERNAME", "");
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");

    }

    public void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setTitle("Thống kê");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initView() {
        token = AccessToken.getCurrentAccessToken();
        drawerLayout = findViewById(R.id.main_drawer);
        toolbar = findViewById(R.id.tool_bar);
        navigationView = findViewById(R.id.navigation_view);
        bottomnavigation = findViewById(R.id.navigation);
        bottomnavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int newPositon = 0;
            Fragment fragment = null;
            menuItem.setCheckable(true);
            switch (menuItem.getItemId()) {
                case R.id.Home:

                    uncheckednavigation();
                    getSupportActionBar().setTitle("Thống kê");
                    newPositon = 5;
                    fragment = new FragmentHome();
                    loadFragment(fragment, newPositon);

                    return true;
                case R.id.Room:
                    uncheckednavigation();
                    getSupportActionBar().setTitle("Quản lý phòng trọ");
                    newPositon = 6;
                    fragment = new FragmentRoom();
                    loadFragment(fragment, newPositon);

                    return true;
                case R.id.Customer:
                    uncheckednavigation();
                    getSupportActionBar().setTitle("Quản lý khách trọ");
                    newPositon = 7;
                    fragment = new FragmentCustomer();
                    loadFragment(fragment, newPositon);

                    return true;
                case R.id.Service:
                    uncheckednavigation();
                    getSupportActionBar().setTitle("Quản lý dịch vụ");
                    newPositon = 8;
                    fragment = new FragmentService();
                    loadFragment(fragment, newPositon);
                    return true;
            }
            return false;
        }


    };

    private boolean loadFragment(Fragment fragment, int position) {
        if (fragment != null) {
            if (startingPosition > position) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.left_to_right, R.anim.exit_left_to_right, R.anim.right_to_left, R.anim.exit_right_to_left).replace(R.id.container_main, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            }
            if (startingPosition < position) {

                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.right_to_left, R.anim.exit_right_to_left, R.anim.left_to_right, R.anim.exit_left_to_right).replace(R.id.container_main, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            }
            startingPosition = position;
            return true;
        }
        return false;
    }

    private void addFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container_main, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }
    }

    public void setupNavigation() {
        navigationView.setNavigationItemSelectedListener(this);
        addFragment(new FragmentHome());
        startingPosition = 5;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_notification);
        View actionView = menuItem.getActionView();
        textNotificationItemCount = actionView.findViewById(R.id.notification_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(menuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupBadge() {

        if (textNotificationItemCount != null) {
            if (mNotificationItemCount == 0) {
                if (textNotificationItemCount.getVisibility() != View.GONE) {
                    textNotificationItemCount.setVisibility(View.GONE);
                }
            } else {
                textNotificationItemCount.setText(String.valueOf(Math.min(mNotificationItemCount, 99)));
                if (textNotificationItemCount.getVisibility() != View.VISIBLE) {
                    textNotificationItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                Animatoo.animateSlideLeft(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (menuitemid == R.id.menu_none) {
                if (bottomnavigation.getSelectedItemId() == R.id.Home) {
                    dialogexit();
                } else {
                    bottomnavigation.setSelectedItemId(R.id.Home);
                }
            } else {
                bottomnavigation.setSelectedItemId(R.id.Home);
            }


        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int newPositon = 0;
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_user:
                menuitemid = menuItem.getItemId();
                uncheckedbottomnavigation();
                getSupportActionBar().setTitle("Trang cá nhân");
                newPositon = 1;
                fragment = new FragmentUser();
                loadFragment(fragment, newPositon);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_bill:
                menuitemid = menuItem.getItemId();
                uncheckedbottomnavigation();
                getSupportActionBar().setTitle("Tất cả hóa đơn");
                newPositon = 2;
                fragment = new FragmentBill();
                loadFragment(fragment, newPositon);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_message:
                menuitemid = menuItem.getItemId();
                uncheckedbottomnavigation();
                getSupportActionBar().setTitle("Hộp thư");
                newPositon = 3;
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_share:
                menuitemid = menuItem.getItemId();
                uncheckedbottomnavigation();
                getSupportActionBar().setTitle("Chia sẻ");
                newPositon = 4;
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_changePass:
                menuitemid = menuItem.getItemId();
                uncheckedbottomnavigation();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_logout:
                menuitemid = menuItem.getItemId();
                dialoglogout();
                return true;
            case R.id.nav_exit:
                menuitemid = menuItem.getItemId();
                dialogexit();
                return true;

        }

        return false;
    }

    private void dialoglogout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
        builder.setCancelable(false);
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                uncheckednavigation();
                dialogInterface.dismiss();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        builder.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (token == null) {
                } else {
                    LoginManager.getInstance().logOut();
                }
                logout();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Animatoo.animateSlideRight(MainActivity.this);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void uncheckednavigation() {
        navigationView.setCheckedItem(R.id.menu_none);
        menuitemid = R.id.menu_none;
    }

    private void uncheckedbottomnavigation() {
        int size = bottomnavigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            bottomnavigation.getMenu().getItem(i).setCheckable(false);
        }
    }


    private void dialogexit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc chắn muốn thoát khỏi chương trình?");
        builder.setCancelable(false);
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                uncheckednavigation();
                dialogInterface.dismiss();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logout() {
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.commit();
    }

    public void showImage(String profilePicUrl) {
        ImageView imgAvater = headerView.findViewById(R.id.imgAvatar);
        Picasso.get().load(profilePicUrl).placeholder(R.drawable.avatar)// Place holder image from drawable folder
                .error(R.drawable.ic_launcher_background).resize(200, 200).centerCrop()
                .into(imgAvater);
    }

    private void setProfileFb() {
        TextView tvUserFullName = headerView.findViewById(R.id.tvUserFullName);
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("JSON1", response.getJSONObject().toString());

                try {
                    showImage(response.getJSONObject().getJSONObject("picture")
                            .getJSONObject("data").getString("url"));
                    tvUserFullName.setText(object.getString("name"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,birthday,location,picture.width(200).height(200)");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    private void setProfileNavigationView() {
        headerView = navigationView.inflateHeaderView(R.layout.navigation_header);

        if (token == null) {
            if (managerUsers.checkInformatioNull(UserNameSp) < 0) {
                setProfileSqlite();
            }

        } else {
            if (isOnline(this)) {
                setProfileFb();
            } else {
                Toast.makeText(this, R.string.error_internet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void setProfileSqlite() {
        users = managerUsers.getUserById(UserNameSp);
        TextView tvUserFullName = headerView.findViewById(R.id.tvUserFullName);
        ImageView imgAvater = headerView.findViewById(R.id.imgAvatar);
        tvUserFullName.setText(users.getUserFullName());
        imgAvater.setImageBitmap(LoadingImgArayByte(users.getUserAvater()));

    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Bitmap LoadingImgArayByte(byte[] img) {
        return BitmapFactory.decodeByteArray(img, 0, img.length);
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }
}
