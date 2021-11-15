package com.example.shiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shiper.Model.CuaHang;
import com.example.shiper.Model.DonHang;
import com.example.shiper.Model.DonHangInfo;
import com.example.shiper.Model.KhachHang;
import com.example.shiper.adapter.AdapterDonHang;
import com.example.shiper.adapter.AdapterSoLuong;
import com.example.shiper.databinding.ActivityChiTietDonHangBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ChiTietDonHang_Activity extends AppCompatActivity {
    Context context;
    ActivityChiTietDonHangBinding binding;
    DonHang donHang ;
    DatabaseReference reference;
    StorageReference storageRef;
    FirebaseStorage storage;
    ArrayList<DonHangInfo> hangInfos = new ArrayList<>();
    AdapterSoLuong adapterSoLuong;
    RecyclerView rcySoluong;

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
        setEvent();

    }

    private void LoadBtn() {
        if(donHang.getTrangThai() == TrangThaiDonHang.SHOP_DangGiaoShipper){
            binding.btnNhanDon.setVisibility(View.VISIBLE);
            binding.btnTuChoiDon.setVisibility(View.VISIBLE);
        }
        else {
            binding.btnNhanDon.setVisibility(View.GONE);
            binding.btnTuChoiDon.setVisibility(View.GONE);
        }
        if(donHang.getTrangThai() == TrangThaiDonHang.Shipper_KhongNhanGiaoHang){
            binding.btnNhanDon.setVisibility(View.GONE);
            binding.btnTuChoiDon.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "trạng thái", Toast.LENGTH_LONG).show();
        }
        if(donHang.getTrangThai().toString().equals("SHOP_ChoShipperLayHang")){
            binding.btnDaLayHang.setVisibility(View.VISIBLE);
            binding.btnHuyDon.setVisibility(View.VISIBLE);
        }else {
            binding.btnDaLayHang.setVisibility(View.GONE);
            binding.btnHuyDon.setVisibility(View.GONE);
        }
        if(donHang.getTrangThai().toString().equals("Shipper_DangGiaoHang")){
            binding.btnGiaoThanhCong.setVisibility(View.VISIBLE);
            binding.btnGiaoThatBai.setVisibility(View.VISIBLE);
        }else {
            binding.btnGiaoThanhCong.setVisibility(View.GONE);
            binding.btnGiaoThatBai.setVisibility(View.GONE);
        }
        if(donHang.getTrangThai().toString().equals("Shipper_DaLayHang")){
            binding.btnDiGiao.setVisibility(View.VISIBLE);
        }else {
            binding.btnDiGiao.setVisibility(View.GONE);
        }

        if(donHang.getTrangThai().toString().equals("Shipper_GiaoThanhCong")){
            binding.btnDaTraTien.setVisibility(View.VISIBLE);
        }else {
            binding.btnDaTraTien.setVisibility(View.GONE);

        }
        if(donHang.getTrangThai().toString().equals("Shipper_GiaoKhongThanhCong")){
            binding.btnDaTraHang.setVisibility(View.VISIBLE);
        }else {
            binding.btnDaTraHang.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadBtn();
    }

    private void LoadDataDonHang(){
        reference.child("DonHang").child(donHang.getIDDonHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DonHang donHang = snapshot.getValue(DonHang .class);
                binding.tvmaDH.setText(donHang.getIDDonHang().substring(0,7));
                binding.tvTongDon.setText("Tổng đơn : " +donHang.getTongTien()+"");
                binding.tvtrangThai.setText("Trạng thái : "+ donHang.getTrangThai().toString());
                binding.edtGhiChu.setText("Ghi chú : " +donHang.getGhiChu_KhachHang());
                if(donHang.getTrangThai() == TrangThaiDonHang.Shipper_KhongNhanGiaoHang){
                    binding.btnNhanDon.setVisibility(View.GONE);
                    binding.btnTuChoiDon.setVisibility(View.INVISIBLE);
                    binding.btnTuChoiDon.setText("đc đi mà");
                    binding.lndcdima.setVisibility(View.GONE);
                    Log.d("qq", donHang.getTrangThai() + " - " +  TrangThaiDonHang.Shipper_KhongNhanGiaoHang);
                }
                //Toast.makeText(getApplicationContext(), donHang.getTrangThai()+"", Toast.LENGTH_SHORT).show();
                LoadBtn();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                binding.sdtNguoiNhan.setText("SĐT người nhận : "+khachHang.getSoDienThoai());
                binding.tvDiaChiNguoiNhan.setText("Địa chỉ người nhận :" +khachHang.getDiaChi());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("DonHangInfo").orderByChild("iddonHang").equalTo(donHang.getIDDonHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    hangInfos.add(donHangInfo);
                    storageRef.child("SanPham").child(donHangInfo.getSanPham()
                            .getIDCuaHang()).child(donHangInfo.getSanPham().getIDSanPham())
                            .child(donHangInfo.getSanPham().getImages().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Glide.with(ChiTietDonHang_Activity.this)
                                    .load(task.getResult().toString())
                                    .into(binding.anhsanpham);
                        }
                    });
                    //Toast.makeText(ChiTietDonHang_Activity.this, hangInfos.size()+"", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapterSoLuong = new AdapterSoLuong(ChiTietDonHang_Activity.this);
        adapterSoLuong.setData(hangInfos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChiTietDonHang_Activity.this, RecyclerView.VERTICAL,false);
        binding.ryctenvasoluong.setLayoutManager(linearLayoutManager);
        binding.ryctenvasoluong.setAdapter(adapterSoLuong);





    }
    private void setEvent() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnNhanDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donHang.setTrangThai(TrangThaiDonHang.SHOP_ChoShipperLayHang);
                reference.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(v.getContext(), "Đã nhận thành công đƠn Hàng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        binding.btnTuChoiDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String data = gson.toJson(donHang);
                Intent intent = new Intent(ChiTietDonHang_Activity.this, LyDo_Activity.class);
                intent.putExtra("DonHang",data);
                startActivity(intent);
                LoadBtn();

            }
        });
        binding.btnDaLayHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donHang.setTrangThai(TrangThaiDonHang.SHOP_ChoXacNhanGiaoHangChoShipper);
                reference.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(v.getContext(), "Lấy hàng thành công! Chờ xác nhận từ cửa hàng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        binding.btnDiGiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donHang.setTrangThai(TrangThaiDonHang.Shipper_DangGiaoHang);
                reference.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(v.getContext(), "Xác nhận đi giao thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                LoadBtn();
                LoadDataDonHang();
            }
        });
        binding.btnGiaoThatBai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String data = gson.toJson(donHang);
                Intent intent = new Intent(ChiTietDonHang_Activity.this, LyDo_Activity.class);
                intent.putExtra("DonHang",data);
                startActivity(intent);


            }
        });
        binding.btnGiaoThanhCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donHang.setTrangThai(TrangThaiDonHang.Shipper_GiaoThanhCong);
                reference.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(v.getContext(), "Giao hàng thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        binding.btnDaTraTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donHang.setTrangThai(TrangThaiDonHang.ChoShopXacNhan_Tien);
                reference.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(v.getContext(), "Giao hàng thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}