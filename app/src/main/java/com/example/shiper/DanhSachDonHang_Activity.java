package com.example.shiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.shiper.Model.DonHang;
import com.example.shiper.adapter.AdapterDonHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DanhSachDonHang_Activity extends AppCompatActivity {
    RecyclerView recyclerDanhSach;
    ArrayList<DonHang>hangs = new ArrayList<>();
    AdapterDonHang adapterDonHang;
    DatabaseReference reference;
    ImageView imgBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_don_hang);
        reference = FirebaseDatabase.getInstance().getReference();
        LoadData();

        setConTrol();
        setEvent();
    }

    private void LoadData() {
        reference.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                //hangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    hangs.add(donHang);
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


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    private ArrayList<DonHang> getDataDonHang(){
//        ArrayList<DonHang>hangs = new ArrayList<>();
//        hangs.add(new DonHang("Mã ĐH : DH126", "", "","Tên cửa hàng: Bánh mì anh Hòa","Địa chỉ cửa hàng : 124 Võ Văn Ngân, Thủ Đức","Địa chỉ người nhận : 44 Võ Văn Ngân, Thủ đức","Tổng đơn : 123000đ","Số điện thoại cửa hàng : 0123456789",R.drawable.anhuser));
//        hangs.add(new DonHang("Mã ĐH : DH125", "", "","Tên cửa hàng: Bánh mì anh Hòa","Địa chỉ cửa hàng : 124 Võ Văn Ngân, Thủ Đức","Địa chỉ người nhận : 98 Võ Văn Ngân, Thủ đức","Tổng đơn : 123000đ","Số điện thoại cửa hàng : 0123456789",R.drawable.anhuser));
//        hangs.add(new DonHang("Mã ĐH : DH124", "", "","Tên cửa hàng: Bánh mì anh Hòa","Địa chỉ cửa hàng : 124 Võ Văn Ngân, Thủ Đức","Địa chỉ người nhận : 37 Võ Văn Ngân, Thủ đức","Tổng đơn : 123000đ","Số điện thoại cửa hàng : 0123456789",R.drawable.anhuser));
//        hangs.add(new DonHang("Mã ĐH : DH123", "", "","Tên cửa hàng: Bánh mì anh Hòa","Địa chỉ cửa hàng : 124 Võ Văn Ngân, Thủ Đức","Địa chỉ người nhận : 150 Võ Văn Ngân, Thủ đức","Tổng đơn : 123000đ","Số điện thoại cửa hàng : 0123456789",R.drawable.anhuser));
//        hangs.add(new DonHang("", "", "","123","124","1234","123","123",R.drawable.anhuser));
//        return hangs;
//
//    }

    private void setConTrol() {
        imgBack = findViewById(R.id.imgBack);
        recyclerDanhSach = findViewById(R.id.recycleDanhSach);
    }
}