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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.shiper.Model.DonHang;
import com.example.shiper.Model.Shipper;
import com.example.shiper.Model.ThongBao;
import com.example.shiper.Model.TrangThaiShipper;
import com.example.shiper.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tapadoo.alerter.Alerter;

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.nav_close, R.string.nav_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // du lieu firebase
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        GetData();
        setEvent();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        reference.child("DonHang").orderByChild("idshipper").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int duocGiao=0,canGiao=0,daNhan=0,dangGiao=0,thanhCong=0,thatBai=0;

                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (donHang.getTrangThai() == TrangThaiDonHang.SHOP_ChoShipperLayHang)
                    {
                        duocGiao ++;
                    }
                    if (donHang.getTrangThai() == TrangThaiDonHang.SHOP_ChoXacNhanGiaoHangChoShipper)
                    {
                        daNhan ++;
                    }
                    if (donHang.getTrangThai() == TrangThaiDonHang.Shipper_DaLayHang)
                    {
                        canGiao ++;
                    }
                    if (donHang.getTrangThai() == TrangThaiDonHang.Shipper_ChoDiGiao||donHang.getTrangThai() == TrangThaiDonHang.Shipper_DangGiaoHang)
                    {
                        dangGiao ++;
                    }
                    if (donHang.getTrangThai() == TrangThaiDonHang.Shipper_GiaoThanhCong||donHang.getTrangThai() == TrangThaiDonHang.Shipper_DaChuyenTien)
                    {
                        thanhCong ++;
                    }
                    if (donHang.getTrangThai() == TrangThaiDonHang.Shipper_GiaoKhongThanhCong||donHang.getTrangThai() == TrangThaiDonHang.Shipper_DaTraHang)
                    {
                        thatBai ++;
                    }
                }
                binding.tvCanGiao.setText(canGiao+"");
                binding.tvDangGiao.setText(dangGiao+"");
                binding.tvGiaoThatBai.setText(thatBai+"");
                binding.tvThanhCong.setText(thanhCong+"");
                binding.tvDangGiao.setText(dangGiao+"");
                binding.tvDonHangDuocGiao.setText(duocGiao+"");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        reference.child("Shipper").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                nguoidunghientai = snapshot.getValue(Shipper.class);
                binding.tvTenShipper.setText("Hi, "+ nguoidunghientai.getHoVaTen());
                if (nguoidunghientai.getTrangThaiShipper() == (TrangThaiShipper.DangHoatDong)) {
                    binding.btnssdigiao.setVisibility(View.GONE);
                }
                if (nguoidunghientai.getTrangThaiShipper() == (TrangThaiShipper.KhongHoatDong)) {
                    binding.btnssdigiao.setVisibility(View.VISIBLE);
                }
                Alerter.create(Home_Activity.this)
                        .setTitle("Xin Ch??o")
                        .setText(nguoidunghientai.getHoVaTen() + ",Ch??c b???n 1 ng??y l??m vi???c an to??n v?? vui v???")
                        .setDuration(5000)
                        .setBackgroundColorRes(R.color.teal_200)
                        .show();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void setEvent() {
        binding.btnssdigiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alerter.create(Home_Activity.this)
                        .setTitle("Xin Ch??o")
                        .setText(nguoidunghientai.getHoVaTen() + ",Ch??c b???n 1 ng??y l??m vi???c may m???n")
                        .setDuration(5000)
                        .setBackgroundColorRes(R.color.teal_200)
                        .show();
                reference.child("Shipper").child(auth.getUid()).child("trangThaiShipper").setValue(TrangThaiShipper.DangHoatDong);

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
                Intent intent = new Intent(getApplicationContext(), ThongKe_activity.class);
                startActivity(intent);
            }
        });
        binding.lnLichsudonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Lichsugiaohang_Activity.class);
                startActivity(intent);
            }
        });

    }

    public String GetStringTrangThaiShipper(TrangThaiShipper trangThaiShipper) {
        String res = "";
        if (trangThaiShipper == TrangThaiShipper.KhongHoatDong) {
            res = "Offline";
        }
        if (trangThaiShipper == TrangThaiShipper.DangHoatDong) {
            res = "??ang ho???t ?????ng";
        }
        if (trangThaiShipper == TrangThaiShipper.DangGiaoHang) {
            res = "??ang giao h??ng";
        }
        return res;
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
                TextView tvTrangThai = (TextView) headerView.findViewById(R.id.tvTrangThai);
                tvTen.setText(shipper.getHoVaTen().toString());
                tvemail.setText(shipper.geteMail().toString());
                tvTrangThai.setText(GetStringTrangThaiShipper(shipper.getTrangThaiShipper()));
                if (shipper.getTrangThaiShipper() == (TrangThaiShipper.DangHoatDong)) {
                    tvTrangThai.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                }
                if (shipper.getTrangThaiShipper() == (TrangThaiShipper.DangGiaoHang)) {
                    tvTrangThai.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };

        reference.child("Shipper").child(auth.getUid()).addValueEventListener(postListener);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        reference.child("ThongBao").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int i =0;
                for (DataSnapshot dataSnapshot:snapshot.getChildren()
                     ) {
                    ThongBao thongBao = dataSnapshot.getValue(ThongBao.class);
                    if (thongBao.getTrangThaiThongBao()==TrangThaiThongBao.ChuaXem)
                    {
                        i++;
                    }
                }
                TextView view = (TextView) navigationView.getMenu().findItem(R.id.thongbao).getActionView();
                if (i==0)
                {
                    view.setVisibility(View.GONE);
                }
                else {
                    view.setText(i+"");

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        View headerView = navigationView.getHeaderView(0);
        CircleImageView imvavatar = (CircleImageView) headerView.findViewById(R.id.imvavatar);
        storageRef.child("Shipper").child(auth.getUid()).child("avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri.toString())
                        .into(imvavatar);
                Glide.with(getApplicationContext())
                        .load(uri.toString())
                        .into(binding.imageAvatar);
            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_danhsachdonhang) {
//            if(mCurrentFragment != FRAMENT_DANHSACHDONHANG){
//                replaceFragment(new DanhSachDonHang_Activity());
//                mCurrentFragment = FRAMENT_DANHSACHDONHANG;
//            }
            Intent intent = new Intent(getApplicationContext(), DanhSachDonHang_Activity.class);
            startActivity(intent);
        } else if (id == R.id.lichsugiaohang) {
            Intent intent = new Intent(getApplicationContext(), Lichsugiaohang_Activity.class);
            startActivity(intent);
        } else if (id == R.id.doanhthu) {
            Intent intent = new Intent(getApplicationContext(), ThongKe_activity.class);
            startActivity(intent);
        } else if (id == R.id.thongtincanhan) {
            Intent intent = new Intent(getApplicationContext(), Thongtincanhan_Activity.class);
            startActivity(intent);
        } else if (id == R.id.dangxuat) {
            new KAlertDialog(this, KAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("????ng xu???t!!!")
                    .setContentText("B???n c?? ch???n ch???n mu???n ????ng xu???t??")
                    .setCustomImage(R.drawable.dangxuat, getApplicationContext())
                    .setConfirmText("OK")
                    .setCancelText("CANCEL").setConfirmClickListener(kAlertDialog -> {

                reference.child("Shipper").child(auth.getUid()).child("trangThaiShipper").setValue(TrangThaiShipper.KhongHoatDong);
                FirebaseAuth.getInstance().signOut();
                finish();
                kAlertDialog.dismiss();
            })
                    .show();


        } else if (id == R.id.doimatkhau) {
            Intent intent = new Intent(getApplicationContext(), Doi_Mat_Khau_Activity.class);
            startActivity(intent);
        }
        else if(id == R.id.thongbao){
            Intent intent = new Intent(getApplicationContext(), ThongBao_Activity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentfram, fragment);
        fragmentTransaction.commit();
    }

}