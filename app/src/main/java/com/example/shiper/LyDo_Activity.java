package com.example.shiper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.shiper.Model.DonHang;
import com.example.shiper.databinding.ActivityLyDoBinding;
import com.google.gson.Gson;

public class LyDo_Activity extends AppCompatActivity {

    ActivityLyDoBinding binding;
    DonHang donHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLyDoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getIntent()!= null ){
            Intent intent =  getIntent();
            String DataDonHang = intent.getStringExtra("DataChiTietDonHang");
            Gson gson = new Gson();
            donHang = gson.fromJson(DataDonHang, DonHang.class);
        }

        Toast.makeText(this, donHang.getTongTien()+"", Toast.LENGTH_SHORT).show();
    }
}