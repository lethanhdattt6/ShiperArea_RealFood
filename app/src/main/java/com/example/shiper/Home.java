package com.example.shiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.shiper.Model.Shipper;
import com.example.shiper.fragment.DanhSachDonHang;
import com.example.shiper.fragment.DoiMatKhau;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//    Shipper shipper;
//    TextView edtTenuser, edtEmail;


    private static final int FRAMENT_DANHSACHDONHANG = 0;
    private static final int FRAMENT_DOIMATKHAU= 1;

    private int mCurrentFragment = FRAMENT_DANHSACHDONHANG;

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,R.string.nav_close,R.string.nav_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //
//        Intent intent = getIntent();
//        String dataDonHang = intent.getStringExtra("Shipper");
//        Gson gson = new Gson();
//        shipper = gson.fromJson(dataDonHang, Shipper.class);
//        setConTrol();
//        Loaddata();


        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

    }

//    private void setConTrol() {
//        edtTenuser = drawerLayout.findViewById(R.id.tenuser);
//        edtEmail = drawerLayout.findViewById(R.id.email);
//    }

//    private void Loaddata() {
//        edtEmail.setText(shipper.getTaiKhoan());
//        edtTenuser.setText(shipper.getHoVaTen());
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();
        if(id == R.id.nav_danhsachdonhang){
            if(mCurrentFragment != FRAMENT_DANHSACHDONHANG){
                replaceFragment(new DanhSachDonHang());
                mCurrentFragment = FRAMENT_DANHSACHDONHANG;
            }
        }else if(id == R.id.lichsugiaohang){

        }else if(id == R.id.doanhthu){

        }else if(id == R.id.thongtincanhan){

        }else if(id == R.id.dangxuat){

        }else if(id == R.id.doimatkhau){
            Intent intent = new Intent(getApplicationContext(),Doi_Mat_Khau.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentfram, fragment);
        fragmentTransaction.commit();
    }
}