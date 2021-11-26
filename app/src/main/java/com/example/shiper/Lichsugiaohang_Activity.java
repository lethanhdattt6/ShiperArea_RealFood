package com.example.shiper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shiper.Model.DonHang;
import com.example.shiper.adapter.AdapterLichSuGiaoHang;
import com.example.shiper.databinding.ActivityLichsugiaohangBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Lichsugiaohang_Activity extends AppCompatActivity {
    RecyclerView recyclerDanhSach;
    ArrayList<DonHang> hangs = new ArrayList<>();
    AdapterLichSuGiaoHang adapterLichSuGiaoHang;
    private FirebaseAuth auth;
    DatabaseReference reference;
    ImageView imgBack;
    Spinner spdanhsach;
    TextView tvDanhSach;
    ActivityLichsugiaohangBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLichsugiaohangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        setConTrol();
        LoadData();
        setEvent();
    }



    private void LoadData() {
        reference.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                hangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if(donHang.getIDShipper().equals(auth.getUid())&&
                            donHang.getTrangThai().toString().equals("Shipper_DaChuyenTien")&&
                            spdanhsach.getSelectedItem().toString().equals("Đã giao thành công")){
                        hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("Shipper_DaTraHang")
                            && spdanhsach.getSelectedItem().toString().equals("Đã giao thất bại")){
                        hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("Shipper_DaTraHang")
                            || donHang.getTrangThai().toString().equals("Shipper_DaChuyenTien")
                            && spdanhsach.getSelectedItem().toString().equals("Tất cả lịch sử giao hàng")){
                        hangs.add(donHang);
                    }
                }
                adapterLichSuGiaoHang = new AdapterLichSuGiaoHang(Lichsugiaohang_Activity.this);
                adapterLichSuGiaoHang.setData(hangs);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Lichsugiaohang_Activity.this, RecyclerView.VERTICAL,false);
                recyclerDanhSach.setLayoutManager(linearLayoutManager);
                recyclerDanhSach.setAdapter(adapterLichSuGiaoHang);

            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
     }
    private void setEvent() {
        spdanhsach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spdanhsach.getSelectedItem().toString().equals("Đã giao thành công")){
                    tvDanhSach.setText("Dang sách giao thành công");
                }
                if (spdanhsach.getSelectedItem().toString().equals("Đã giao thất bại")){
                    tvDanhSach.setText("Danh sách giao thât bại");
                }
                if (spdanhsach.getSelectedItem().toString().equals("Tất cả lịch sử giao hàng")){
                    tvDanhSach.setText("Lịch sử giao hàng");
                }
                    LoadData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setConTrol() {
        recyclerDanhSach = findViewById(R.id.recycleLichSu);
        spdanhsach = findViewById(R.id.lichsu);
        tvDanhSach = findViewById(R.id.tvDanhSach);
    }
}