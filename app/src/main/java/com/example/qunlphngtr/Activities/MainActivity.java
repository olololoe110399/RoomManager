package com.example.qunlphngtr.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.qunlphngtr.Fragment.FragmentCustomer;
import com.example.qunlphngtr.Fragment.FragmentHome;
import com.example.qunlphngtr.Fragment.FragmentRoom;
import com.example.qunlphngtr.Fragment.FragmentService;
import com.example.qunlphngtr.Fragment.FragmentUser;
import com.example.qunlphngtr.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int startingPosition;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomnavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initToolbar();
        setupNavigation();

    }

    public void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setTitle("Thống kê");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initView() {
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

            switch (menuItem.getItemId()) {
                case R.id.Home:
                    menuItem.setCheckable(true);
                    uncheckednavigation();
                    getSupportActionBar().setTitle("Thống kê");
                    newPositon = 1;
                    fragment = new FragmentHome();
                    loadFragment(fragment, newPositon);

                    return true;
                case R.id.Room:
                    menuItem.setCheckable(true);
                    uncheckednavigation();
                    getSupportActionBar().setTitle("Quản lý phòng trọ");
                    newPositon = 2;
                    fragment = new FragmentRoom();
                    loadFragment(fragment, newPositon);

                    return true;
                case R.id.Customer:
                    menuItem.setCheckable(true);
                    uncheckednavigation();
                    getSupportActionBar().setTitle("Quản lý khách trọ");
                    newPositon = 3;
                    fragment = new FragmentCustomer();
                    loadFragment(fragment, newPositon);

                    return true;
                case R.id.Service:
                    menuItem.setCheckable(true);
                    uncheckednavigation();
                    getSupportActionBar().setTitle("Quản lý dịch vụ");
                    newPositon = 4;
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

    public void setupNavigation() {
        navigationView.setNavigationItemSelectedListener(this);
        loadFragment(new FragmentHome(), 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_setting:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getCheckedItem(navigationView) > -1) {
                bottomnavigation.setSelectedItemId(R.id.Home);
            } else {
                if (bottomnavigation.getSelectedItemId() == R.id.Home) {
                    dialogexit();

                } else {
                    bottomnavigation.setSelectedItemId(R.id.Home);
                }
            }


        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int newPositon = 0;
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_user:
                menuItem.setChecked(true);
                uncheckedbottomnavigation();
                getSupportActionBar().setTitle("Người dùng");
                newPositon=5;
                fragment=new FragmentUser();
                loadFragment(fragment ,newPositon);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_bill:
                menuItem.setChecked(true);
                uncheckedbottomnavigation();
                getSupportActionBar().setTitle("Hóa đơn");
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_message:
                menuItem.setChecked(true);
                uncheckedbottomnavigation();
                getSupportActionBar().setTitle("Hộp thư");
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_share:
                menuItem.setChecked(true);
                uncheckedbottomnavigation();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_changePass:
                menuItem.setChecked(true);
                uncheckedbottomnavigation();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_logout:
                menuItem.setChecked(true);
                uncheckedbottomnavigation();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_exit:
                menuItem.setChecked(true);
                uncheckedbottomnavigation();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

        }

        return false;
    }

    private void uncheckednavigation() {
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    private void uncheckedbottomnavigation() {
        int size = bottomnavigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            bottomnavigation.getMenu().getItem(i).setCheckable(false);
        }
    }

    private int getCheckedItem(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isChecked()) {
                return i;
            }
        }

        return -1;
    }

    private void dialogexit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc chắn muốn thoát khỏi chương trình?");
        builder.setCancelable(false);
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
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

}
