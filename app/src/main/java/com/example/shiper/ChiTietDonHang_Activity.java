package com.example.shiper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.shiper.Model.CuaHang;
import com.example.shiper.Model.DonHang;
import com.example.shiper.Model.DonHangInfo;
import com.example.shiper.Model.KhachHang;
import com.example.shiper.Model.TrangThaiShipper;
import com.example.shiper.adapter.AdapterDonHang;
import com.example.shiper.adapter.AdapterSoLuong;
import com.example.shiper.databinding.ActivityChiTietDonHangBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    ActivityChiTietDonHangBinding binding;
    DonHang donHang ;
    FirebaseAuth auth;
    DatabaseReference reference;
    StorageReference storageRef;
    FirebaseStorage storage;
    ArrayList<DonHangInfo> hangInfos = new ArrayList<>();
    AdapterSoLuong adapterSoLuong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChiTietDonHangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        auth = FirebaseAuth.getInstance();


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
        if(donHang.getTrangThai()==TrangThaiDonHang.Shipper_DangGiaoHang){
            binding.tvdonhang.setText("Đang giao hàng");
            binding.btnGiaoThanhCong.setVisibility(View.VISIBLE);
            binding.btnGiaoThatBai.setVisibility(View.VISIBLE);
        }else {
            binding.btnGiaoThanhCong.setVisibility(View.GONE);
            binding.btnGiaoThatBai.setVisibility(View.GONE);
        }
        if(donHang.getTrangThai().toString().equals("Shipper_DaLayHang")){
            binding.tvdonhang.setText("Chờ đi giao");
            binding.btnDiGiao.setVisibility(View.VISIBLE);
        }else {
            binding.btnDiGiao.setVisibility(View.GONE);
        }

        if(donHang.getTrangThai().toString().equals("SHOP_ChoShipperLayHang")){
            binding.tvdonhang.setText("Chờ lấy hàng");
            binding.btnDaLayHang.setVisibility(View.VISIBLE);
        }else {
            binding.btnDaLayHang.setVisibility(View.GONE);
        }
        if(donHang.getTrangThai().toString().equals("Shipper_GiaoThanhCong")){
            binding.tvdonhang.setText("Chuyển tiền cho cửa hàng");
            binding.btnDaTraTien.setVisibility(View.VISIBLE);
        }else {
            binding.btnDaTraTien.setVisibility(View.GONE);
        }
        if(donHang.getTrangThai().toString().equals("Shipper_GiaoKhongThanhCong")){
            binding.tvdonhang.setText("Trả hàng cho cửa hàng");
            binding.btnDaTraHang.setVisibility(View.VISIBLE);
        }else {
            binding.btnDaTraHang.setVisibility(View.GONE);
        }
        if(donHang.getTrangThai().toString().equals("ChoShopXacNhan_Tien")){
            binding.tvdonhang.setText("Đang chờ xác nhận");
            binding.btnDaTraTien.setVisibility(View.GONE);
            binding.tvxacnhan.setVisibility(View.VISIBLE);
            binding.tvxacnhan.setText("Đang chờ xác nhận");
        }
        if(donHang.getTrangThai().toString().equals("ChoShopXacNhan_TraHang")){
            binding.tvxacnhan.setVisibility(View.VISIBLE);
            binding.tvxacnhan.setText("Đang chờ xác nhận");
            binding.tvdonhang.setText("Đang chờ xác nhận");
            binding.btnDaTraHang.setVisibility(View.GONE);
        }
        if(donHang.getTrangThai().toString().equals("Shipper_DaChuyenTien")){
            binding.tvxacnhan.setVisibility(View.VISIBLE);
            binding.tvxacnhan.setText("Đã chuyển tiền cho cửa hàng");
        }
        if(donHang.getTrangThai().toString().equals("Shipper_DaTraHang")){
            binding.tvxacnhan.setText("Đã trả hàng cho cửa hàng");
            binding.tvxacnhan.setVisibility(View.VISIBLE);
        }
        if(donHang.getTrangThai().toString().equals("SHOP_ChoXacNhanGiaoHangChoShipper")){
            binding.tvdonhang.setText("Đang chờ cửa hàng xác nhận");
            binding.tvxacnhan.setVisibility(View.VISIBLE);
            binding.tvxacnhan.setText("Đang chờ xác nhận");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadBtn();
    }
    public String GetStringTrangThaiDonHang(TrangThaiDonHang trangThaiDonHang){
        String res ="";
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_HuyDonHang)
        {
            res ="Đã hủy";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoXacNhanChuyenTien)
        {
            res ="Chờ xác nhận chuyển tiền cọc";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DaGiaoChoBep)
        {
            res ="Đã giao đơn hàng cho bếp";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DangChuanBihang)
        {
            res ="Đang chuẩn bị hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DaChuanBiXong)
        {
            res ="Đã chuẩn bị xong";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DangGiaoShipper)
        {
            res ="Đang giao shipper đi phát";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoShipperLayHang)
        {
            res ="Chờ shipper lấy hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoXacNhanGiaoHangChoShipper)
        {
            res ="Chờ Shop xác nhận giao hàng cho Shipper";
        }
        if (trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_Tien)
        {
            res ="Chờ Shop xác nhận đã nhận tiền hàng từ Shipper";
        }
        if (trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_TraHang)
        {
            res ="Chờ Shop xác nhận đã nhận hàng trả về từ Shipper";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaLayHang)
        {
            res ="Shipper đã lấy hàng đi giao";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_KhongNhanGiaoHang)
        {
            res ="Shipper không nhận giao hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaTraHang)
        {
            res ="Đơn hàng đã được hoàn về";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaChuyenTien)
        {
            res ="Thanh toán thành công";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoKhongThanhCong)
        {
            res ="Giao hàng không thành công";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DangGiaoHang)
        {
            res ="Shipper đang giao hàng";
        }if (trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoThanhCong)
        {
            res ="Shipper giao hàng thành công";
        }
        if (trangThaiDonHang == TrangThaiDonHang.KhachHang_HuyDon)
        {
            res ="Khách hàng hủy đơn hàng";
        }if (trangThaiDonHang == TrangThaiDonHang.Bep_DaHuyDonHang)
        {
            res ="Bếp đã hủy đơn";
        }
        return res;
    }

    private void LoadDataDonHang(){
        reference.child("DonHang").child(donHang.getIDDonHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DonHang temp = snapshot.getValue(DonHang .class);
                binding.tvmaDH.setText(donHang.getIDDonHang().substring(0,7));
                binding.tvTongDon.setText("Tổng đơn : " +temp.getTongTien()+"");
                binding.tvtrangThai.setText("Trạng thái : "+ GetStringTrangThaiDonHang(temp.getTrangThai()));
                binding.edtGhiChu.setText("Ghi chú : " +temp.getGhiChu_KhachHang());
                donHang = temp;
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
                new KAlertDialog(ChiTietDonHang_Activity.this, KAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Xác nhận!!!")
                        .setContentText("Bạn có chắn chắn muốn nhận đơn hàng này??")
                        .setCancelText("CANCEL")
                        .setConfirmText("OK")
                        .setConfirmClickListener(kAlertDialog -> {
                            donHang.setTrangThai(TrangThaiDonHang.SHOP_ChoShipperLayHang);
                            reference.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(v.getContext(), "Đã nhận thành công đƠn Hàng", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            kAlertDialog.dismiss();
                        }).show();

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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String data = gson.toJson(donHang);
                Intent intent = new Intent(ChiTietDonHang_Activity.this, LyDo_Activity.class);
                intent.putExtra("DonHang",data);
                startActivity(intent);
                KiemTra();
                LoadBtn();

            }
        });
        binding.btnGiaoThanhCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                donHang.setTrangThai(TrangThaiDonHang.Shipper_GiaoThanhCong);
                reference.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(v.getContext(), "Giao hàng thành công", Toast.LENGTH_SHORT).show();
                            KiemTra();
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
        binding.btnDaTraHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donHang.setTrangThai(TrangThaiDonHang.ChoShopXacNhan_TraHang);
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
    boolean res = true;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void KiemTra(){
        reference.child("DonHang").orderByChild("idshipper").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                boolean res = true;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (donHang.getTrangThai().toString().equals("Shipper_DangGiaoHang")){
                       //reference.child("Shipper").child(auth.getUid()).child("trangThaiHoatDong").setValue("Đang giao hàng");
                        res = false;
                        break;
                    }
                }
                if (res == false){
                    reference.child("Shipper").child(auth.getUid()).child("trangThaiShipper").setValue(TrangThaiShipper.DangGiaoHang);
                }else {
                    reference.child("Shipper").child(auth.getUid()).child("trangThaiShipper").setValue(TrangThaiShipper.DangHoatDong);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    public String GetStringTrangThaiShipper(TrangThaiShipper trangThaiShipper){
        String res ="";
        if (trangThaiShipper == TrangThaiShipper.KhongHoatDong)
        {
            res ="Offline";
        }
        if (trangThaiShipper == TrangThaiShipper.DangHoatDong)
        {
            res ="Đang hoạt động";
        }
        if (trangThaiShipper == TrangThaiShipper.DangGiaoHang)
        {
            res ="Đang giao hàng";
        }
        return res;
    }
}