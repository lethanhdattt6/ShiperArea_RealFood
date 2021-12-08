package com.example.shiper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import com.example.shiper.Model.LoaiThongBao;
import com.example.shiper.Model.ThongBao;
import com.example.shiper.Model.TrangThaiShipper;
import com.example.shiper.adapter.AdapterDonHang;
import com.example.shiper.adapter.AdapterSoLuong;
import com.example.shiper.databinding.ActivityChiTietDonHangBinding;
import com.google.android.gms.maps.model.LatLng;
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
import com.tapadoo.alerter.Alerter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ChiTietDonHang_Activity extends AppCompatActivity {
    ActivityChiTietDonHangBinding binding;
    DonHang donHang;
    FirebaseAuth auth;
    DatabaseReference reference;
    StorageReference storageRef;
    FirebaseStorage storage;
    ArrayList<DonHangInfo> hangInfos = new ArrayList<>();
    AdapterSoLuong adapterSoLuong;
    private int REQUEST_CODE = 1;
    String sdtCuaHang = "";
    QuanLy quanLy;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChiTietDonHangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        auth = FirebaseAuth.getInstance();

        quanLy = new QuanLy();
        //Check and request permission if neeeded
        if (!checkPermission(Manifest.permission.CALL_PHONE)) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
        } else {
            callPerson();
        }
        if (getIntent() != null) {
            Intent intent = getIntent();
            String DataDonHang = intent.getStringExtra("DataDonHang");
            Gson gson = new Gson();
            donHang = gson.fromJson(DataDonHang, DonHang.class);
            LoadDataDonHang();
        }
        setEvent();
    }

    private void LoadBtn() {
        if (donHang.getTrangThai() == TrangThaiDonHang.SHOP_DangGiaoShipper) {
            binding.btnNhanDon.setVisibility(View.VISIBLE);
            binding.btnTuChoiDon.setVisibility(View.VISIBLE);
        } else {
            binding.btnNhanDon.setVisibility(View.GONE);
            binding.btnTuChoiDon.setVisibility(View.GONE);
        }
        if (donHang.getTrangThai() == TrangThaiDonHang.Shipper_DangGiaoHang) {
            binding.tvdonhang.setText("Đang giao hàng");
            binding.btnGiaoThanhCong.setVisibility(View.VISIBLE);
            binding.btnGiaoThatBai.setVisibility(View.VISIBLE);
            binding.btnGotoMap.setVisibility(View.VISIBLE);
        } else {
            binding.btnGiaoThanhCong.setVisibility(View.GONE);
            binding.btnGiaoThatBai.setVisibility(View.GONE);
            binding.btnGotoMap.setVisibility(View.GONE);
        }
        if (donHang.getTrangThai().toString().equals("Shipper_DaLayHang")) {
            binding.tvdonhang.setText("Chờ đi giao");
            binding.btnDiGiao.setVisibility(View.VISIBLE);
        } else {
            binding.btnDiGiao.setVisibility(View.GONE);
        }

        if (donHang.getTrangThai().toString().equals("SHOP_ChoShipperLayHang")) {
            binding.tvdonhang.setText("Chờ lấy hàng");
            binding.btnDaLayHang.setVisibility(View.VISIBLE);
        } else {
            binding.btnDaLayHang.setVisibility(View.GONE);
        }
        if (donHang.getTrangThai().toString().equals("Shipper_GiaoThanhCong")) {
            binding.tvdonhang.setText("Chuyển tiền cho cửa hàng");
            binding.btnDaTraTien.setVisibility(View.VISIBLE);
        } else {
            binding.btnDaTraTien.setVisibility(View.GONE);
        }
        if (donHang.getTrangThai().toString().equals("Shipper_GiaoKhongThanhCong")) {
            binding.tvdonhang.setText("Trả hàng cho cửa hàng");
            binding.btnDaTraHang.setVisibility(View.VISIBLE);
        } else {
            binding.btnDaTraHang.setVisibility(View.GONE);
        }
        if (donHang.getTrangThai().toString().equals("ChoShopXacNhan_Tien")) {
            binding.tvdonhang.setText("Đang chờ xác nhận");
            binding.btnDaTraTien.setVisibility(View.GONE);
            binding.tvxacnhan.setVisibility(View.VISIBLE);
            binding.tvxacnhan.setText("Đang chờ xác nhận");
        }
        if (donHang.getTrangThai().toString().equals("ChoShopXacNhan_TraHang")) {
            binding.tvxacnhan.setVisibility(View.VISIBLE);
            binding.tvxacnhan.setText("Đang chờ xác nhận");
            binding.tvdonhang.setText("Đang chờ xác nhận");
            binding.btnDaTraHang.setVisibility(View.GONE);
        }
        if (donHang.getTrangThai().toString().equals("Shipper_DaChuyenTien")) {
            binding.tvxacnhan.setVisibility(View.VISIBLE);
            binding.tvxacnhan.setText("Đã chuyển tiền cho cửa hàng");
        }
        if (donHang.getTrangThai().toString().equals("Shipper_DaTraHang")) {
            binding.tvxacnhan.setText("Đã trả hàng cho cửa hàng");
            binding.tvxacnhan.setVisibility(View.VISIBLE);
        }
        if (donHang.getTrangThai().toString().equals("SHOP_ChoXacNhanGiaoHangChoShipper")) {
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



    private void LoadDataDonHang() {
        reference.child("DonHang").child(donHang.getIDDonHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DonHang temp = snapshot.getValue(DonHang.class);
                binding.tvmaDH.setText(donHang.getIDDonHang().substring(0, 20));
                binding.tvTongDon.setText("Tổng đơn : " + temp.getTongTien() + "VNĐ");
                binding.tvtrangThai.setText("Trạng thái : " + quanLy.GetStringTrangThaiDonHang(temp.getTrangThai()));
                binding.edtGhiChu.setText("Ghi chú : " + temp.getGhiChu_KhachHang());
                donHang = temp;
                binding.sdtNguoiNhan.setText("SĐT người nhận : " + donHang.getSoDienThoai());
                binding.tvDiaChiNguoiNhan.setText("Địa chỉ người nhận :" + donHang.getDiaChi());
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
                binding.tvtenCuaHang.setText("Tên cửa hàng : " + cuaHang.getTenCuaHang());
                binding.tvDiaChiCuaHang.setText("Địa chỉ cửa hàng : " + cuaHang.getDiaChi());
                binding.tvsdtCuaHang.setText("SĐT cửa hàng : " + cuaHang.getSoDienThoai());
                sdtCuaHang = cuaHang.getSoDienThoai();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("KhachHang").child(donHang.getIDKhachHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                binding.tvtenNguoiNhan.setText("Tên người nhận : " + khachHang.getTenKhachHang());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("DonHangInfo").orderByChild("iddonHang").equalTo(donHang.getIDDonHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    hangInfos.add(donHangInfo);
                    storageRef.child("SanPham").child(donHangInfo.getSanPham()
                            .getIDCuaHang()).child(donHangInfo.getSanPham().getIDSanPham())
                            .child(donHangInfo.getSanPham().getImages().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            try {
                                Glide.with(ChiTietDonHang_Activity.this)
                                        .load(task.getResult().toString())
                                        .into(binding.anhsanpham);
                            }catch (Exception e)
                            {

                            }

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChiTietDonHang_Activity.this, RecyclerView.VERTICAL, false);
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
                                        Alerter.create(ChiTietDonHang_Activity.this)
                                                .setTitle("Thông báo")
                                                .setText("Bạn đã nhận giao đon hàng này")
                                                .setDuration(5000)
                                                .setBackgroundColorRes(R.color.success_stroke_color)
                                                .show();
                                        String IDThongBao = UUID.randomUUID().toString();
                                        ThongBao thongBao = new ThongBao(IDThongBao, "Đã nhận đơn hàng thành công! Hãy đến cửa hàng để lấy hàng!! " + donHang.getIDDonHang().substring(0, 15), "Thông Báo", "", donHang.getIDCuaHang(), "", TrangThaiThongBao.ChuaXem, new Date());
                                        thongBao.setDonHang(donHang);
                                        thongBao.setLoaiThongBao(LoaiThongBao.DONHANG_MOI);
                                        reference.child("ThongBao").child(donHang.getIDCuaHang()).child(IDThongBao).setValue(thongBao);
                                        thongBao.setLoaiThongBao(LoaiThongBao.DonHangShipper);
                                        thongBao.setIDUSer(auth.getUid());
                                        reference.child("ThongBao").child(auth.getUid()).child(IDThongBao).setValue(thongBao);
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
                intent.putExtra("DonHang", data);
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
                            Alerter.create(ChiTietDonHang_Activity.this)
                                    .setTitle("Thông báo")
                                    .setText("Đã lấy hàng thành công - Chờ shop xác nhận")
                                    .setDuration(5000)
                                    .setBackgroundColorRes(R.color.success_stroke_color)
                                    .show();
                        }
                    }
                });
                String IDThongBao = UUID.randomUUID().toString();
                ThongBao thongBao = new ThongBao(IDThongBao, "Đã xác nhận lấy hàng! Chờ cửa hàng xác nhận " + donHang.getIDDonHang().substring(0, 15), "Thông Báo", "", donHang.getIDCuaHang(), "", TrangThaiThongBao.ChuaXem, new Date());
                thongBao.setDonHang(donHang);
                thongBao.setLoaiThongBao(LoaiThongBao.DONHANG_MOI);
                reference.child("ThongBao").child(donHang.getIDCuaHang()).child(IDThongBao).setValue(thongBao);
                thongBao.setLoaiThongBao(LoaiThongBao.DonHangShipper);
                thongBao.setIDUSer(auth.getUid());
                reference.child("ThongBao").child(auth.getUid()).child(IDThongBao).setValue(thongBao);

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
                            Alerter.create(ChiTietDonHang_Activity.this)
                                    .setTitle("Thông báo")
                                    .setText("Xác nhận đi giao")
                                    .setDuration(5000)
                                    .setBackgroundColorRes(R.color.success_stroke_color)
                                    .show();
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
                intent.putExtra("DonHang", data);
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
                            Alerter.create(ChiTietDonHang_Activity.this)
                                    .setTitle("Thông báo")
                                    .setText("Giao hàng thành công")
                                    .setDuration(5000)
                                    .setBackgroundColorRes(R.color.success_stroke_color)
                                    .show();
                            KiemTra();
                        }
                    }
                });
                String IDThongBao = UUID.randomUUID().toString();
                ThongBao thongBao = new ThongBao(IDThongBao, "Đã giao hàng thành công đơn hàng " + donHang.getIDDonHang().substring(0, 15), "Thông Báo", "", donHang.getIDCuaHang(), "", TrangThaiThongBao.ChuaXem, new Date());
                thongBao.setDonHang(donHang);
                thongBao.setLoaiThongBao(LoaiThongBao.DONHANG_MOI);
                reference.child("ThongBao").child(donHang.getIDCuaHang()).child(IDThongBao).setValue(thongBao);
                thongBao.setLoaiThongBao(LoaiThongBao.DonHangShipper);
                thongBao.setIDUSer(auth.getUid());
                reference.child("ThongBao").child(auth.getUid()).child(IDThongBao).setValue(thongBao);
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
                            Alerter.create(ChiTietDonHang_Activity.this)
                                    .setTitle("Thông báo")
                                    .setText("Đã trả tiền - chờ shop xác nhận")
                                    .setDuration(5000)
                                    .setBackgroundColorRes(R.color.success_stroke_color)
                                    .show();
                            // Toast.makeText(v.getContext(), "Giao hàng thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                String IDThongBao = UUID.randomUUID().toString();
                ThongBao thongBao = new ThongBao(IDThongBao, "Đã xác nhận trả tiền cho cửa hàng! Chờ cửa hàng xác nhận " + donHang.getIDDonHang().substring(0, 15), "Thông Báo", "", donHang.getIDCuaHang(), "", TrangThaiThongBao.ChuaXem, new Date());
                thongBao.setDonHang(donHang);
                thongBao.setLoaiThongBao(LoaiThongBao.DONHANG_MOI);
                reference.child("ThongBao").child(donHang.getIDCuaHang()).child(IDThongBao).setValue(thongBao);
                thongBao.setLoaiThongBao(LoaiThongBao.DonHangShipper);
                thongBao.setIDUSer(auth.getUid());
                reference.child("ThongBao").child(auth.getUid()).child(IDThongBao).setValue(thongBao);
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
                            Alerter.create(ChiTietDonHang_Activity.this)
                                    .setTitle("Thông báo")
                                    .setText("Đã trả hàng - Chờ shop xác nhận")
                                    .setDuration(5000)
                                    .setBackgroundColorRes(R.color.success_stroke_color)
                                    .show();
                        }
                    }
                });
                String IDThongBao = UUID.randomUUID().toString();
                ThongBao thongBao = new ThongBao(IDThongBao, "Đã xác nhận trả hàng cho cửa hàng! Chờ cửa hàng xác nhận " + donHang.getIDDonHang().substring(0, 15), "Thông Báo", "", donHang.getIDCuaHang(), "", TrangThaiThongBao.ChuaXem, new Date());
                thongBao.setDonHang(donHang);
                thongBao.setLoaiThongBao(LoaiThongBao.DONHANG_MOI);
                reference.child("ThongBao").child(donHang.getIDCuaHang()).child(IDThongBao).setValue(thongBao);
                thongBao.setLoaiThongBao(LoaiThongBao.DonHangShipper);
                thongBao.setIDUSer(auth.getUid());
                reference.child("ThongBao").child(auth.getUid()).child(IDThongBao).setValue(thongBao);

            }
        });
        binding.btnGotoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Address> addresses = null;
                if (addresses == null) {
                    Geocoder geocoder = new Geocoder(ChiTietDonHang_Activity.this);
                    try {
                        addresses = geocoder.getFromLocationName(donHang.getDiaChi(), 1);

                    } catch (Exception e) {

                    }
                    if (addresses.size() != 0) {

                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=" + latLng.latitude + "," + latLng.longitude));
                        startActivity(intent);
                    } else {
                        Alerter.create(ChiTietDonHang_Activity.this)
                                .setTitle("Thông báo")
                                .setText("Không tìm thấy địa chỉ : " + donHang.getDiaChi())
                                .setDuration(5000)
                                .setBackgroundColorRes(R.color.error_stroke_color)
                                .show();
                    }
                }
            }
        });

    }

    boolean res = true;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void KiemTra() {
        reference.child("DonHang").orderByChild("idshipper").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                boolean res = true;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (donHang.getTrangThai().toString().equals("Shipper_DangGiaoHang")) {
                        //reference.child("Shipper").child(auth.getUid()).child("trangThaiHoatDong").setValue("Đang giao hàng");
                        res = false;
                        break;
                    }
                }
                if (res == false) {
                    reference.child("Shipper").child(auth.getUid()).child("trangThaiShipper").setValue(TrangThaiShipper.DangGiaoHang);
                } else {
                    reference.child("Shipper").child(auth.getUid()).child("trangThaiShipper").setValue(TrangThaiShipper.DangHoatDong);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    //Check Permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkPermission(String permission) {
        boolean ok = false;
        int check = checkSelfPermission(permission);
        if (check == PackageManager.PERMISSION_GRANTED) {
            ok = true;
        }
        return ok;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPerson();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void callPerson() {
        binding.tvCallCuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "tel:" + sdtCuaHang;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(text));
                startActivity(intent);
            }
        });
        binding.tvCallNguoiNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "tel:" + donHang.getSoDienThoai();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(text));
                startActivity(intent);
            }
        });
    }
//    public String GetStringTrangThaiShipper(TrangThaiShipper trangThaiShipper){
//        String res ="";
//        if (trangThaiShipper == TrangThaiShipper.KhongHoatDong)
//        {
//            res ="Offline";
//        }
//        if (trangThaiShipper == TrangThaiShipper.DangHoatDong)
//        {
//            res ="Đang hoạt động";
//        }
//        if (trangThaiShipper == TrangThaiShipper.DangGiaoHang)
//        {
//            res ="Đang giao hàng";
//        }
//        return res;
//    }
}