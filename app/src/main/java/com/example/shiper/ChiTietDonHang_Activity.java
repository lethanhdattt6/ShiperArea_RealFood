package com.example.shiper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.shiper.databinding.ActivityChiTietDonHangBinding;
import com.example.shiper.databinding.ActivityQuenMatKhauBinding;

public class ChiTietDonHang_Activity extends AppCompatActivity {
    ActivityChiTietDonHangBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChiTietDonHangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}