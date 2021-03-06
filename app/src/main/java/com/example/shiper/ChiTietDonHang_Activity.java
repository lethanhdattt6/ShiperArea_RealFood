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
            binding.tvdonhang.setText("??ang giao h??ng");
            binding.btnGiaoThanhCong.setVisibility(View.VISIBLE);
            binding.btnGiaoThatBai.setVisibility(View.VISIBLE);
            binding.btnGotoMap.setVisibility(View.VISIBLE);
        } else {
            binding.btnGiaoThanhCong.setVisibility(View.GONE);
            binding.btnGiaoThatBai.setVisibility(View.GONE);
            binding.btnGotoMap.setVisibility(View.GONE);
        }
        if (donHang.getTrangThai().toString().equals("Shipper_DaLayHang")) {
            binding.tvdonhang.setText("Ch??? ??i giao");
            binding.btnDiGiao.setVisibility(View.VISIBLE);
        } else {
            binding.btnDiGiao.setVisibility(View.GONE);
        }

        if (donHang.getTrangThai().toString().equals("SHOP_ChoShipperLayHang")) {
            binding.tvdonhang.setText("Ch??? l???y h??ng");
            binding.btnDaLayHang.setVisibility(View.VISIBLE);
        } else {
            binding.btnDaLayHang.setVisibility(View.GONE);
        }
        if (donHang.getTrangThai().toString().equals("Shipper_GiaoThanhCong")) {
            binding.tvdonhang.setText("Chuy???n ti???n cho c???a h??ng");
            binding.btnDaTraTien.setVisibility(View.VISIBLE);
        } else {
            binding.btnDaTraTien.setVisibility(View.GONE);
        }
        if (donHang.getTrangThai().toString().equals("Shipper_GiaoKhongThanhCong")) {
            binding.tvdonhang.setText("Tr??? h??ng cho c???a h??ng");
            binding.btnDaTraHang.setVisibility(View.VISIBLE);
        } else {
            binding.btnDaTraHang.setVisibility(View.GONE);
        }
        if (donHang.getTrangThai().toString().equals("ChoShopXacNhan_Tien")) {
            binding.tvdonhang.setText("??ang ch??? x??c nh???n");
            binding.btnDaTraTien.setVisibility(View.GONE);
            binding.tvxacnhan.setVisibility(View.VISIBLE);
            binding.tvxacnhan.setText("??ang ch??? x??c nh???n");
        }
        if (donHang.getTrangThai().toString().equals("ChoShopXacNhan_TraHang")) {
            binding.tvxacnhan.setVisibility(View.VISIBLE);
            binding.tvxacnhan.setText("??ang ch??? x??c nh???n");
            binding.tvdonhang.setText("??ang ch??? x??c nh???n");
            binding.btnDaTraHang.setVisibility(View.GONE);
        }
        if (donHang.getTrangThai().toString().equals("Shipper_DaChuyenTien")) {
            binding.tvxacnhan.setVisibility(View.VISIBLE);
            binding.tvxacnhan.setText("???? chuy???n ti???n cho c???a h??ng");
        }
        if (donHang.getTrangThai().toString().equals("Shipper_DaTraHang")) {
            binding.tvxacnhan.setText("???? tr??? h??ng cho c???a h??ng");
            binding.tvxacnhan.setVisibility(View.VISIBLE);
        }
        if (donHang.getTrangThai().toString().equals("SHOP_ChoXacNhanGiaoHangChoShipper")) {
            binding.tvdonhang.setText("??ang ch??? c???a h??ng x??c nh???n");
            binding.tvxacnhan.setVisibility(View.VISIBLE);
            binding.tvxacnhan.setText("??ang ch??? x??c nh???n");
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
                binding.tvTongDon.setText("T???ng ????n : " + temp.getTongTien() + "VN??");
                binding.tvtrangThai.setText("Tr???ng th??i : " + quanLy.GetStringTrangThaiDonHang(temp.getTrangThai()));
                binding.edtGhiChu.setText("Ghi ch?? : " + temp.getGhiChu_KhachHang());
                donHang = temp;
                binding.sdtNguoiNhan.setText("S??T ng?????i nh???n : " + donHang.getSoDienThoai());
                binding.tvDiaChiNguoiNhan.setText("?????a ch??? ng?????i nh???n :" + donHang.getDiaChi());
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
                binding.tvtenCuaHang.setText("T??n c???a h??ng : " + cuaHang.getTenCuaHang());
                binding.tvDiaChiCuaHang.setText("?????a ch??? c???a h??ng : " + cuaHang.getDiaChi());
                binding.tvsdtCuaHang.setText("S??T c???a h??ng : " + cuaHang.getSoDienThoai());
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
                binding.tvtenNguoiNhan.setText("T??n ng?????i nh???n : " + khachHang.getTenKhachHang());

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
                        .setTitleText("X??c nh???n!!!")
                        .setContentText("B???n c?? ch???n ch???n mu???n nh???n ????n h??ng n??y??")
                        .setCancelText("CANCEL")
                        .setConfirmText("OK")
                        .setConfirmClickListener(kAlertDialog -> {
                            donHang.setTrangThai(TrangThaiDonHang.SHOP_ChoShipperLayHang);
                            reference.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Alerter.create(ChiTietDonHang_Activity.this)
                                                .setTitle("Th??ng b??o")
                                                .setText("B???n ???? nh???n giao ??on h??ng n??y")
                                                .setDuration(5000)
                                                .setBackgroundColorRes(R.color.success_stroke_color)
                                                .show();
                                        String IDThongBao = UUID.randomUUID().toString();
                                        ThongBao thongBao = new ThongBao(IDThongBao, "???? nh???n ????n h??ng th??nh c??ng! H??y ?????n c???a h??ng ????? l???y h??ng!! " + donHang.getIDDonHang().substring(0, 15), "Th??ng B??o", "", donHang.getIDCuaHang(), "", TrangThaiThongBao.ChuaXem, new Date());
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
                                    .setTitle("Th??ng b??o")
                                    .setText("???? l???y h??ng th??nh c??ng - Ch??? shop x??c nh???n")
                                    .setDuration(5000)
                                    .setBackgroundColorRes(R.color.success_stroke_color)
                                    .show();
                        }
                    }
                });
                String IDThongBao = UUID.randomUUID().toString();
                ThongBao thongBao = new ThongBao(IDThongBao, "???? x??c nh???n l???y h??ng! Ch??? c???a h??ng x??c nh???n " + donHang.getIDDonHang().substring(0, 15), "Th??ng B??o", "", donHang.getIDCuaHang(), "", TrangThaiThongBao.ChuaXem, new Date());
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
                                    .setTitle("Th??ng b??o")
                                    .setText("X??c nh???n ??i giao")
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
                                    .setTitle("Th??ng b??o")
                                    .setText("Giao h??ng th??nh c??ng")
                                    .setDuration(5000)
                                    .setBackgroundColorRes(R.color.success_stroke_color)
                                    .show();
                            KiemTra();
                        }
                    }
                });
                String IDThongBao = UUID.randomUUID().toString();
                ThongBao thongBao = new ThongBao(IDThongBao, "???? giao h??ng th??nh c??ng ????n h??ng " + donHang.getIDDonHang().substring(0, 15), "Th??ng B??o", "", donHang.getIDCuaHang(), "", TrangThaiThongBao.ChuaXem, new Date());
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
                                    .setTitle("Th??ng b??o")
                                    .setText("???? tr??? ti???n - ch??? shop x??c nh???n")
                                    .setDuration(5000)
                                    .setBackgroundColorRes(R.color.success_stroke_color)
                                    .show();
                            // Toast.makeText(v.getContext(), "Giao h??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                String IDThongBao = UUID.randomUUID().toString();
                ThongBao thongBao = new ThongBao(IDThongBao, "???? x??c nh???n tr??? ti???n cho c???a h??ng! Ch??? c???a h??ng x??c nh???n " + donHang.getIDDonHang().substring(0, 15), "Th??ng B??o", "", donHang.getIDCuaHang(), "", TrangThaiThongBao.ChuaXem, new Date());
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
                                    .setTitle("Th??ng b??o")
                                    .setText("???? tr??? h??ng - Ch??? shop x??c nh???n")
                                    .setDuration(5000)
                                    .setBackgroundColorRes(R.color.success_stroke_color)
                                    .show();
                        }
                    }
                });
                String IDThongBao = UUID.randomUUID().toString();
                ThongBao thongBao = new ThongBao(IDThongBao, "???? x??c nh???n tr??? h??ng cho c???a h??ng! Ch??? c???a h??ng x??c nh???n " + donHang.getIDDonHang().substring(0, 15), "Th??ng B??o", "", donHang.getIDCuaHang(), "", TrangThaiThongBao.ChuaXem, new Date());
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
                        if (addresses.size() != 0) {

                            Address address = addresses.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?daddr=" + latLng.latitude + "," + latLng.longitude));
                            startActivity(intent);
                        } else {
                            Alerter.create(ChiTietDonHang_Activity.this)
                                    .setTitle("Th??ng b??o")
                                    .setText("Kh??ng t??m th???y ?????a ch??? : " + donHang.getDiaChi())
                                    .setDuration(5000)
                                    .setBackgroundColorRes(R.color.error_stroke_color)
                                    .show();
                        }

                    } catch (Exception e) {

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
                        //reference.child("Shipper").child(auth.getUid()).child("trangThaiHoatDong").setValue("??ang giao h??ng");
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
//            res ="??ang ho???t ?????ng";
//        }
//        if (trangThaiShipper == TrangThaiShipper.DangGiaoHang)
//        {
//            res ="??ang giao h??ng";
//        }
//        return res;
//    }
}