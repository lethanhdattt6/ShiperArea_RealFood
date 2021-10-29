package com.example.shiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.developer.kalert.KAlertDialog;
import com.example.shiper.databinding.ActivityQuenMatKhauBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class QuenMatKhau_Activity extends AppCompatActivity {
    ActivityQuenMatKhauBinding binding;
    KAlertDialog kAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuenMatKhauBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEvent();
    }

    private void setEvent() {
        Context context = this;
        binding.btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KiemTra()) {
                    kAlertDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
                    kAlertDialog.setTitleText("Đang Gửi ...");
                    kAlertDialog.show();
                    kAlertDialog.setCancelable(false);
                    kAlertDialog.showConfirmButton(false);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(binding.email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                        kAlertDialog.setTitleText("HỆ THỐNG ĐÃ GỬI CHO BẠN MỘT EMAIL ĐỂ THAY ĐỔI MẬT KHẨU");
                                        kAlertDialog.setConfirmText("Vui lòng kiểm tra email và thay đổi mật khẩu!");
                                        kAlertDialog.setCancelable(true);
                                        kAlertDialog.showConfirmButton(false);
                                    } else {
                                        kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                                        kAlertDialog.setTitleText(" Xin lỗi! Email này không tồn tại trong hệ thống của chúng tôi");
                                        kAlertDialog.setCancelable(true);
                                        kAlertDialog.showConfirmButton(false);
                                    }
                                }
                            });
                }
            }
        });
        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(intent);
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean KiemTra() {
        boolean res = true;
        if (binding.email.getText().toString() == "") {
            res = false;
        } else {
            String emailAddress = binding.email.getText().toString().trim();
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                res = true;
            } else {
                binding.email.setError("Vui lòng nhập đúng định dạng email");
                res = false;
            }
        }
        return res;
    }
}

