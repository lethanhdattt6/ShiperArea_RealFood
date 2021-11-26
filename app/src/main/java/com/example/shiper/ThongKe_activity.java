package com.example.shiper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shiper.Model.DonHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

public class ThongKe_activity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference reference;

    int choXacNhanCoc = 0, dangXuLy = 0, dangGiaoHang = 0, giaoHangThanhCong = 0, giaoHangThatBai = 0;
    String[] info = {"Đã nhận đơn", "Đang xử lý", "Đang giao hàng", "Giao hàng thành công", "Giao hàng thất bạt"};
    TextView txtTongSoDonHang, txtTongDoanhThu, txtDaGiaoThanhCong, txtChoChuyenCoc, txtDangXuLy, txtDangGiaoHang, txtGiaoHangKhongThanhCong;
    Button btnNgayBatDau, btnNgayKetThuc;
    ProgressBar progressBar;
    LinearLayout lnLayout;
    ArrayList<DonHang> donHangs = new ArrayList<>();
    ArrayList<DonHang> display = new ArrayList<>();
    Date dateBatDau = new Date(), dateKetThuc=new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        txtTongSoDonHang = findViewById(R.id.txtTongDonHang);
        txtDaGiaoThanhCong = findViewById(R.id.txtDaGiaoThanhCong);
        txtTongDoanhThu = findViewById(R.id.txtTongDoanhThu);
        txtDangGiaoHang = findViewById(R.id.txtDangGiaoHang);
        txtGiaoHangKhongThanhCong = findViewById(R.id.txtGiaoHangKhongThanhCong);
        lnLayout = findViewById(R.id.lnLayout);
        progressBar = findViewById(R.id.progessbar);
        btnNgayBatDau = findViewById(R.id.btnNgayBatDau);
        btnNgayKetThuc = findViewById(R.id.btnNgayKetThuc);
        setEvent();
        LoadData();
        lnLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefesh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadData();
                pullToRefresh.setRefreshing(false);
            }
        });

    }

    private void LoadData() {
        reference.child("DonHang").orderByChild("idshipper").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangs.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DonHang donHang = postSnapshot.getValue(DonHang.class);
                    donHangs.add(donHang);
                }
                display = donHangs;
                GetThongKe_DonHang(donHangs);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        btnNgayKetThuc.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateKetThuc);

                DatePickerDialog dpd = new DatePickerDialog(ThongKe_activity.this,
                        (view1, year, month, dayOfMonth) -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, dayOfMonth);
                            DateFormat formatter = new SimpleDateFormat("dd / MM /yyyy");
                            Date date = calendar.getTime();
                            dateKetThuc = date;
                            btnNgayKetThuc.setText(formatter.format(date));
                            display = (ArrayList<DonHang>) donHangs.stream().filter(donHang -> donHang.getNgayTao().compareTo(date) < 0).collect(Collectors.toList());
                            display = (ArrayList<DonHang>) display.stream().filter(donHang -> donHang.getNgayTao().compareTo(dateBatDau) >0).collect(Collectors.toList());

                            GetThongKe_DonHang(display);
                            LoadPieChart();
                        }
                        , cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dpd.show();
            }
        });

        btnNgayBatDau.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateBatDau);
                DatePickerDialog dpd = new DatePickerDialog(ThongKe_activity.this,
                        (view1, year, month, dayOfMonth) -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, dayOfMonth);
                            DateFormat formatter = new SimpleDateFormat("dd / MM /yyyy");
                            Date date = calendar.getTime();
                            dateBatDau = date;
                            btnNgayBatDau.setText(formatter.format(date));
                            display = (ArrayList<DonHang>) donHangs.stream().filter(donHang -> donHang.getNgayTao().compareTo(date) > 0).collect(Collectors.toList());
                            display = (ArrayList<DonHang>) display.stream().filter(donHang -> donHang.getNgayTao().compareTo(dateKetThuc) <0).collect(Collectors.toList());
                            GetThongKe_DonHang(display);
                            LoadPieChart();

                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dpd.show();
            }
        });
    }

    public void GetThongKe_DonHang(ArrayList<DonHang> donHangs) {
        dangGiaoHang = 0;
        giaoHangThanhCong = 0;
        int tong = 0;
        giaoHangThatBai = 0;
        lnLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        for (DonHang donHang : donHangs) {
            if (donHang.getTrangThai() == TrangThaiDonHang.Shipper_DaLayHang ||
                    donHang.getTrangThai() == TrangThaiDonHang.Shipper_DangGiaoHang) {
                dangGiaoHang++;
            }
            if (donHang.getTrangThai() == TrangThaiDonHang.Shipper_GiaoKhongThanhCong ||
                    donHang.getTrangThai() == TrangThaiDonHang.ChoShopXacNhan_TraHang ||
                    donHang.getTrangThai() == TrangThaiDonHang.Shipper_DaTraHang ||
                    donHang.getTrangThai() == TrangThaiDonHang.KhachHang_HuyDon) {
                giaoHangThatBai++;
            }
            if (donHang.getTrangThai() == TrangThaiDonHang.Shipper_GiaoThanhCong ||
                    donHang.getTrangThai() == TrangThaiDonHang.ChoShopXacNhan_Tien ||
                    donHang.getTrangThai() == TrangThaiDonHang.Shipper_DaChuyenTien) {
                giaoHangThanhCong++;
                tong += donHang.getTongTien();
            }

        }
        LoadPieChart();
        txtTongDoanhThu.setText(tong + " VND");
        txtDaGiaoThanhCong.setText(giaoHangThanhCong + "");
        txtDangGiaoHang.setText(dangGiaoHang + "");
        txtGiaoHangKhongThanhCong.setText(giaoHangThatBai + "");
        lnLayout.setVisibility(View.VISIBLE);
        txtTongSoDonHang.setText(display.size() + "");
        progressBar.setVisibility(View.GONE);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void LoadPieChart() {
        loadFragment(new PieChart_Fragment(choXacNhanCoc, dangXuLy, dangGiaoHang, giaoHangThanhCong, giaoHangThatBai));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
