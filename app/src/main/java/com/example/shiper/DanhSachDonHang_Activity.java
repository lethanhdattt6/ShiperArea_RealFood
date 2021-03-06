package com.example.shiper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
    ArrayList<DonHang> hangs = new ArrayList<>();
    AdapterDonHang adapterDonHang;
    private FirebaseAuth auth;
    DatabaseReference reference;
    ImageView imgBack;
    Spinner spdanhsach;
    TextView tvDanhSach;
    androidx.appcompat.widget.SearchView searchView;
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                hangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);

                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("SHOP_DangGiaoShipper")
                            && spdanhsach.getSelectedItem().toString().equals("Danh s??ch ????n h??ng")){
                        hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("Shipper_DangGiaoHang")
                            && spdanhsach.getSelectedItem().toString().equals("Danh s??ch ????n h??ng ??ang giao")){
                        hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("SHOP_ChoShipperLayHang")
                            && spdanhsach.getSelectedItem().toString().equals("Danh s??ch ????n h??ng ???? nh???n")){
                        hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("Shipper_DaLayHang")
                            && spdanhsach.getSelectedItem().toString().equals("Danh s??ch ????n h??ng ch??? ??i giao")){
                        hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("Shipper_GiaoKhongThanhCong")
                            && spdanhsach.getSelectedItem().toString().equals("????n h??ng giao kh??ng th??nh c??ng")){
                        hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("Shipper_GiaoThanhCong")
                            && spdanhsach.getSelectedItem().toString().equals("????n h??ng giao th??nh c??ng")){
                        hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("SHOP_ChoXacNhanGiaoHangChoShipper")
                            && spdanhsach.getSelectedItem().toString().equals("Danh s??ch ????n ch??? x??c nh???n")){
                        hangs.add(donHang);
                    }
                }
                adapterDonHang = new AdapterDonHang(DanhSachDonHang_Activity.this,R.layout.itemdonhang,hangs);
                adapterDonHang.setData(hangs);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                        DanhSachDonHang_Activity.this, RecyclerView.VERTICAL,false);
                recyclerDanhSach.setLayoutManager(linearLayoutManager);
                recyclerDanhSach.setAdapter(adapterDonHang);

            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
    }


    private void setEvent() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    adapterDonHang.getFilter().filter(query);
                }catch (Exception e)
                {

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    adapterDonHang.getFilter().filter(newText);
                }catch (Exception e)
                {

                }

                return true;
            }
        });
        Context context = this;

        spdanhsach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        adapterDonHang.getFilter().filter(TrangThaiDonHang.SHOP_DangGiaoShipper.toString());
                        break;
                    case 1:
                        adapterDonHang.getFilter().filter(TrangThaiDonHang.SHOP_ChoShipperLayHang.toString());
                        break;
                    case 2:
                        adapterDonHang.getFilter().filter(TrangThaiDonHang.SHOP_ChoXacNhanGiaoHangChoShipper.toString());
                        break;
                    case 3:
                        adapterDonHang.getFilter().filter(TrangThaiDonHang.Shipper_DangGiaoHang.toString());
                        break;
                    case 4:
                        adapterDonHang.getFilter().filter(TrangThaiDonHang.Shipper_DaLayHang.toString());
                        break;
                    case 5:
                        adapterDonHang.getFilter().filter(TrangThaiDonHang.Shipper_GiaoKhongThanhCong.toString());
                        break;
                    case 6:
                        adapterDonHang.getFilter().filter(TrangThaiDonHang.Shipper_GiaoThanhCong.toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spdanhsach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spdanhsach.getSelectedItem().toString().equals("Danh s??ch ????n h??ng")){
                    tvDanhSach.setText("Danh s??ch ????n h??ng");
                }
                if(spdanhsach.getSelectedItem().toString().equals("Danh s??ch ????n h??ng ???? nh???n")){
                    tvDanhSach.setText("Danh s??ch ????n h??ng ???? nh???n");
                }
                if(spdanhsach.getSelectedItem().toString().equals("Danh s??ch ????n h??ng ??ang giao")){
                    tvDanhSach.setText("Danh s??ch ????n h??ng ??ang giao");
                }
                if(spdanhsach.getSelectedItem().toString().equals("Danh s??ch ????n h??ng ch??? ??i giao")){
                    tvDanhSach.setText("Danh s??ch ????n h??ng ch??? ??i giao");
                }
                if(spdanhsach.getSelectedItem().toString().equals("????n h??ng giao kh??ng th??nh c??ng")){
                    tvDanhSach.setText("????n h??ng giao kh??ng th??nh c??ng");
                }
                if(spdanhsach.getSelectedItem().toString().equals("????n h??ng giao th??nh c??ng")){
                    tvDanhSach.setText("????n h??ng giao th??nh c??ng");
                }
                if(spdanhsach.getSelectedItem().toString().equals("Danh s??ch ????n ch??? x??c nh???n")){
                    tvDanhSach.setText("Danh s??ch ????n ch??? x??c nh???n");
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
        searchView = findViewById(R.id.searchView);
    }
}