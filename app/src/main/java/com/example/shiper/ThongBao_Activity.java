package com.example.shiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.shiper.Model.ThongBao;
import com.example.shiper.adapter.AdapterThongBao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class ThongBao_Activity extends AppCompatActivity {
    RecyclerView rcvThongBao;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ThongBao> thongBaos;
    FirebaseAuth auth;
    DatabaseReference mDataBase;
    AdapterThongBao adapterThongBao;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);
        auth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        thongBaos = new ArrayList<>();
        adapterThongBao = new AdapterThongBao(this, R.layout.itemthongbao, thongBaos);
        setControl();
        setEvent();
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvThongBao.setLayoutManager(linearLayoutManager);
        rcvThongBao.setAdapter(adapterThongBao);
        LoadItemThongBao();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void LoadItemThongBao() {
        mDataBase.child("ThongBao").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                thongBaos.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ThongBao thongBao = dataSnapshot.getValue(ThongBao.class);
                    thongBaos.add(thongBao);
                    adapterThongBao.notifyDataSetChanged();
                }
                Collections.sort(thongBaos,(o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        imgBack = findViewById(R.id.imgBack);
        rcvThongBao = findViewById(R.id.rcvThongBao);
    }
}