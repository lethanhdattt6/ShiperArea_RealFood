package com.example.shiper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
                            spdanhsach.getSelectedItem().toString().equals("???? giao th??nh c??ng")){
                        hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("Shipper_DaTraHang")
                            && spdanhsach.getSelectedItem().toString().equals("???? giao th???t b???i")){
                        hangs.add(donHang);
                    }
                    if(donHang.getIDShipper().equals(auth.getUid())
                            && donHang.getTrangThai().toString().equals("Shipper_DaTraHang")
                            || donHang.getTrangThai().toString().equals("Shipper_DaChuyenTien")
                            && spdanhsach.getSelectedItem().toString().equals("T???t c??? l???ch s??? giao h??ng")){
                        hangs.add(donHang);
                    }
                }
                adapterLichSuGiaoHang = new AdapterLichSuGiaoHang(Lichsugiaohang_Activity.this , R.layout.itemlichsu,hangs);
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
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterLichSuGiaoHang.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterLichSuGiaoHang.getFilter().filter(newText);
                return true;
            }
        });
        spdanhsach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spdanhsach.getSelectedItem().toString().equals("???? giao th??nh c??ng")){
                    tvDanhSach.setText("Dang s??ch giao th??nh c??ng");
                }
                if (spdanhsach.getSelectedItem().toString().equals("???? giao th???t b???i")){
                    tvDanhSach.setText("Danh s??ch giao th??t b???i");
                }
                if (spdanhsach.getSelectedItem().toString().equals("T???t c??? l???ch s??? giao h??ng")){
                    tvDanhSach.setText("L???ch s??? giao h??ng");
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

    private void setConTrol() {
        recyclerDanhSach = findViewById(R.id.recycleLichSu);
        spdanhsach = findViewById(R.id.lichsu);
        tvDanhSach = findViewById(R.id.tvDanhSach);
        imgBack = findViewById(R.id.imgBack);
    }
}