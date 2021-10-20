package com.example.shiper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.shiper.databinding.ActivityDoiMatKhauBinding;

public class Doi_Mat_Khau extends AppCompatActivity {
    ActivityDoiMatKhauBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoiMatKhauBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}