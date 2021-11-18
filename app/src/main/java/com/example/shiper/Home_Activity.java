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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.shiper.Model.Shipper;
import com.example.shiper.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class Home_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageRef;
    DatabaseReference reference;
    Shipper shipper = new Shipper();
    Shipper nguoidunghientai;
    ActivityHomeBinding binding;

//    private static final int FRAMENT_DANHSACHDONHANG = 0;
//    private static final int FRAMENT_DOIMATKHAU= 1;

//    private int mCurrentFragment = FRAMENT_DANHSACHDONHANG;

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        //
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,R.string.nav_close,R.string.nav_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // du lieu firebase
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        GetData();
        setEvent();


        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);


    }


    private void setEvent() {
        boolean flag = true;
        reference.child("Shipper").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (flag == true){
                    nguoidunghientai = snapshot.getValue(Shipper.class);
                    nguoidunghientai.setTrangThaiHoatDong("Đang hoạt ");
                    //reference.child("Shipper").child(auth.getUid()).setValue(nguoidunghientai);
                }else {
                    nguoidunghientai = snapshot.getValue(Shipper.class);
                    nguoidunghientai.setTrangThaiHoatDong("Đang  ");
                }
                reference.child("Shipper").child(auth.getUid()).setValue(nguoidunghientai);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.danhsach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DanhSachDonHang_Activity.class);
                startActivity(intent);
            }
        });
        binding.thongtincanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Thongtincanhan_Activity.class);
                startActivity(intent);
            }
        });

        binding.doanhthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void GetData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shipper = dataSnapshot.getValue(Shipper.class);
                NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
                View headerView = navigationView.getHeaderView(0);
                TextView tvTen = (TextView) headerView.findViewById(R.id.tvtenuser);
                TextView tvemail = (TextView) headerView.findViewById(R.id.tvemail);
                tvTen.setText(shipper.getHoVaTen().toString());
                tvemail.setText(shipper.geteMail().toString());

                //Log.d("okok", shipper.getHoVaTen());
               // CircleImageView imvavatar = (CircleImageView) headerView.findViewById(R.id.imvavatar);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };

        reference.child("Shipper").child(auth.getUid()).addValueEventListener(postListener);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView imvavatar = (CircleImageView) headerView.findViewById(R.id.imvavatar);
        storageRef.child("Shipper").child(auth.getUid()).child("avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri.toString())
                        .into(imvavatar);
            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id  = item.getItemId();
        if(id == R.id.nav_danhsachdonhang){
//            if(mCurrentFragment != FRAMENT_DANHSACHDONHANG){
//                replaceFragment(new DanhSachDonHang_Activity());
//                mCurrentFragment = FRAMENT_DANHSACHDONHANG;
//            }
            Intent intent = new Intent(getApplicationContext(), DanhSachDonHang_Activity.class);
            startActivity(intent);
        }else if(id == R.id.lichsugiaohang){

        }else if(id == R.id.doanhthu){

        }else if(id == R.id.thongtincanhan){
            Intent intent = new Intent(getApplicationContext(), Thongtincanhan_Activity.class);
            startActivity(intent);
        }else if(id == R.id.dangxuat){
            new KAlertDialog(this, KAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Đăng xuất!!!")
                    .setContentText("Bạn có chắn chắn muốn đăng xuất??")
                    .setCustomImage(R.drawable.dangxuat,getApplicationContext())
                    .setConfirmText("OK")
                    .setCancelText("CANCEL").setConfirmClickListener(kAlertDialog -> {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        kAlertDialog.dismiss();
                    })
                    .show();



        }
        else if(id == R.id.doimatkhau){
            Intent intent = new Intent(getApplicationContext(), Doi_Mat_Khau_Activity.class);
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