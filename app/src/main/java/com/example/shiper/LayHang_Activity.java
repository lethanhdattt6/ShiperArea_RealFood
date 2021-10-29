package com.example.shiper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.shiper.databinding.ActivityLayHangBinding;

public class LayHang_Activity extends AppCompatActivity {
    ActivityLayHangBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLayHangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}