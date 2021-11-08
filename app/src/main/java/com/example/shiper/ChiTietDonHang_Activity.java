package com.example.shiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.shiper.Model.CuaHang;
import com.example.shiper.Model.DonHang;
import com.example.shiper.Model.DonHangInfo;
import com.example.shiper.Model.KhachHang;
import com.example.shiper.databinding.ActivityChiTietDonHangBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;


public class ChiTietDonHang_Activity extends AppCompatActivity {
    ActivityChiTietDonHangBinding binding;
    DonHang donHang ;
    DonHangInfo hangInfo = new DonHangInfo();
    DatabaseReference reference;
    StorageReference storageRef;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChiTietDonHangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reference = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        if(getIntent()!= null ){
            Intent intent =  getIntent();
            String DataDonHang = intent.getStringExtra("DataDonHang");
            Gson gson = new Gson();
            donHang = gson.fromJson(DataDonHang, DonHang.class);
            LoadDataDonHang();
        }

    }

    private void LoadDataDonHang(){
        binding.tvmaDH.setText(donHang.getIDDonHang().substring(0,7));
        binding.tvTongDon.setText("Tổng đơn : " +donHang.getTongTien()+"");
        binding.sdtNguoiNhan.setText("SĐT người nhận : "+donHang.getSoDienThoai());
        binding.tvDiaChiNguoiNhan.setText("Địa chỉ người nhận :" +donHang.getDiaChi());
        binding.tvtrangThai.setText("Trạng thái : "+ donHang.getTrangThai().toString());
        binding.edtGhiChu.setText("Ghi chú : " +donHang.getGhiChu_KhachHang());
        reference.child("CuaHang").child(donHang.getIDCuaHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    CuaHang cuaHang = snapshot.getValue(CuaHang.class);
                    binding.tvtenCuaHang.setText("Tên cửa hàng : " +cuaHang.getTenCuaHang());
                    binding.tvDiaChiCuaHang.setText("Địa chỉ cửa hàng : "+ cuaHang.getDiaChi());
                    binding.tvsdtCuaHang.setText("SĐT cửa hàng : " + cuaHang.getSoDienThoai());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("KhachHang").child(donHang.getIDKhachHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                binding.tvtenNguoiNhan.setText("Tên người nhận : "+khachHang.getTenKhachHang());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("DonHangInfo").child(donHang.getIDDonHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DonHangInfo donHangInfo = snapshot.getValue(DonHangInfo.class);
                binding.tvsoluong.setText("Số lượng : "+donHangInfo.getSoLuong());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        storageRef.child("SanPham").child(donHang.getIDDonHang()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri.toString())
                        .into(binding.anhsanpham);
            }
        });

    }
}