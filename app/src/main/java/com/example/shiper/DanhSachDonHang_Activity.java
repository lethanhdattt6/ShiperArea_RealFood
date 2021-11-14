package com.example.shiper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiper.Model.DonHang;
import com.example.shiper.adapter.AdapterDonHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DanhSachDonHang_Activity extends AppCompatActivity {
    RecyclerView recyclerDanhSach;
    ArrayList<DonHang>hangs = new ArrayList<>();
    AdapterDonHang adapterDonHang;
    private FirebaseAuth auth;
    DatabaseReference reference;
    ImageView imgBack;
    Spinner spdanhsach;
    TextView tvDanhSach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_don_hang);
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        LoadData();
        setConTrol();
        setEvent();
    }

    private void LoadData() {
        reference.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                hangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("SHOP_DangGiaoShipper")
                            && spdanhsach.getSelectedItem().toString().equals("Danh sách đơn hàng")){
                            hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("Shipper_DangGiaoHang")
                            && spdanhsach.getSelectedItem().toString().equals("Danh sách đơn hàng đang giao")){
                        hangs.add(donHang);
                        Toast.makeText(DanhSachDonHang_Activity.this, "Danh sách đơn hàng đang giao", Toast.LENGTH_SHORT).show();

                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("SHOP_ChoShipperLayHang")
                            && spdanhsach.getSelectedItem().toString().equals("Danh sách đơn hàng đã nhận")){
                       // Toast.makeText(DanhSachDonHang_Activity.this, "Danh sách đơn hàng đã nhận", Toast.LENGTH_SHORT).show();
                        hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("Shipper_DaLayHang")
                            && spdanhsach.getSelectedItem().toString().equals("Danh sách đơn hàng chờ đi giao")){
                        // Toast.makeText(DanhSachDonHang_Activity.this, "Danh sách đơn hàng đã nhận", Toast.LENGTH_SHORT).show();
                        hangs.add(donHang);
                    }
                }
                adapterDonHang = new AdapterDonHang(DanhSachDonHang_Activity.this);
                adapterDonHang.setData(hangs);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DanhSachDonHang_Activity.this, RecyclerView.VERTICAL,false);
                recyclerDanhSach.setLayoutManager(linearLayoutManager);
                recyclerDanhSach.setAdapter(adapterDonHang);

            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        Context context = this;

        spdanhsach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spdanhsach.getSelectedItem().toString().equals("Danh sách đơn hàng")){
                    tvDanhSach.setText("Danh sách đơn hàng");
                }
                if(spdanhsach.getSelectedItem().toString().equals("Danh sách đơn hàng đã nhận")){
                    tvDanhSach.setText("Danh sách đơn hàng đã nhận");
                }
                if(spdanhsach.getSelectedItem().toString().equals("Danh sách đơn hàng đang giao")){
                    tvDanhSach.setText("Danh sách đơn hàng đang giao");
                }
                if(spdanhsach.getSelectedItem().toString().equals("Danh sách đơn hàng chờ đi giao")){
                    tvDanhSach.setText("Danh sách đơn hàng chờ đi giao");
                }
                LoadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

//

    private void setConTrol() {
        imgBack = findViewById(R.id.imgBack);
        recyclerDanhSach = findViewById(R.id.recycleDanhSach);
        spdanhsach = findViewById(R.id.spdanhSach);
        tvDanhSach = findViewById(R.id.tvDanhSach);
    }
}