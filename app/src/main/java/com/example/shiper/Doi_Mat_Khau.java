package com.example.shiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shiper.Model.Shipper;
import com.example.shiper.databinding.ActivityDoiMatKhauBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Doi_Mat_Khau extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference database;
    Shipper nguoiDungHienTai = new Shipper();
    ActivityDoiMatKhauBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoiMatKhauBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        setEvent();
    }

    private void setEvent() {
        // lấy tài khoản người dùng hiện tại
        binding.edttenDangNhap.setText(auth.getCurrentUser().getEmail());
        binding.btnDoimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KiemTra()){
                    ChangePass();
                }
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Home.class);
                startActivity(intent);
            }
        });
    }

    public void ChangePass(){

        FirebaseUser user = auth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(binding.edttenDangNhap.getText().toString(),binding.edtMatKhau.getText().toString());
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(binding.edtmatKhauMoi.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Doi_Mat_Khau.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        nguoiDungHienTai.setMatKhau(binding.edtmatKhauMoi.getText().toString());
                                        database.child("Shipper").child(auth.getUid()).setValue(nguoiDungHienTai);
                                    } else {
                                        Toast.makeText(Doi_Mat_Khau.this, "Đổi mật khẩu Không thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Doi_Mat_Khau.this, "Mật khẩu cũ của bạn không đúng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public boolean KiemTra(){
        boolean res = true;
        if(binding.edttenDangNhap.getText().toString().equals("")){
            binding.edttenDangNhap.setError("Vui Lòng Nhập Tên Tài Khoản Của Bạn");
            res = false;
        }if(binding.edtMatKhau.getText().toString().equals("")){
            binding.edtMatKhau.setError("Vui Lòng Mật Khẩu Của Bạn");
            res = false;
        }if(binding.edtmatKhauMoi.getText().toString().equals("")){
            binding.edtmatKhauMoi.setError("Vui Lòng Nhập Mật Khẩu Mà Bạn Muốn Đổi");
            res = false;
        }if(binding.edtnhapLai.getText().toString().equals("")){
            binding.edtnhapLai.setError("Vui Lòng Nhập Nhập Lại Mật Khẩu Mới Của Bạn");
            res = false;
        }else {
            if (!binding.edtnhapLai.getText().toString().equals(binding.edtmatKhauMoi.getText().toString())){
                binding.edtnhapLai.setError("Mật Khẩu Không Trùng Khớp");
            }
        }
        return res;
    }
}