package com.example.shiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.shiper.Model.DonHang;
import com.example.shiper.databinding.ActivityLyDoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class LyDo_Activity extends AppCompatActivity {

    DatabaseReference reference;
    ActivityLyDoBinding binding;
    DonHang donHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLyDoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference();

        if(getIntent()!= null ){
            Intent intent =  getIntent();
            String DonHang = intent.getStringExtra("DonHang");
            Gson gson = new Gson();
            donHang = gson.fromJson(DonHang, DonHang.class);
        }

        setEvent();

    }

    private void setEvent() {
        binding.btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(LyDo_Activity.this, KAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Từ chối!!!")
                        .setContentText("Bạn có chắn chắn muốn từ chối đơn hàng này??")
                        .setCancelText("CANCEL")
                        .setConfirmText("OK")
                        .setConfirmClickListener(kAlertDialog -> {
                            donHang.setGhiChu_Shipper(binding.edtLyDo.getText().toString());
                            donHang.setIDShipper("");
                            donHang.setTrangThai(TrangThaiDonHang.Shipper_KhongNhanGiaoHang);
                            reference.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                            finish();
                            kAlertDialog.dismiss();
                        }).show();

            }
        });
        if(donHang.getTrangThai().toString().equals("Shipper_DangGiaoHang")){
            binding.tvlyDo.setText("Lý do giao không thành công");
            binding.btnGui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new KAlertDialog(LyDo_Activity.this, KAlertDialog.CUSTOM_IMAGE_TYPE)
                            .setTitleText("Từ chối!!!")
                            .setContentText("Bạn có chắn chắn muốn từ chối đơn hàng này??")
                            .setCancelText("CANCEL")
                            .setConfirmText("OK")
                            .setConfirmClickListener(kAlertDialog -> {
                                donHang.setGhiChu_Shipper(binding.edtLyDo.getText().toString());
                                donHang.setTrangThai(TrangThaiDonHang.Shipper_GiaoKhongThanhCong);
                                reference.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        }
                                    }
                                });
                                finish();
                                kAlertDialog.dismiss();
                            }).show();
                }


            });
        }
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
