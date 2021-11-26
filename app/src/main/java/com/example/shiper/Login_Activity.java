package com.example.shiper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.kalert.KAlertDialog;
import com.example.shiper.Model.Shipper;
import com.example.shiper.Model.TrangThaiShipper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

import org.jetbrains.annotations.NotNull;


public class Login_Activity extends AppCompatActivity {
    KAlertDialog kAlertDialog;
    TextView txtQuenMatKhau;
    EditText tendangnhap,matkhau;
    Button login;
    private FirebaseAuth auth;
    DatabaseReference reference;
    Shipper shipper ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        setConTrol();
        setEvent();
        if(auth.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), Home_Activity.class);
            tendangnhap.setText(auth.getCurrentUser().getEmail());
            startActivity(intent);
        }



    }


    private boolean KiemTra() {
        boolean res = true;
        if (tendangnhap.getText().toString().equals("")) {
            tendangnhap.setError("Vui Lòng Nhập Email Của Bạn");
            res = false;
        }else {
            String emailAddress = tendangnhap.getText().toString().trim();
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                res = true;
            } else {
                tendangnhap.setError("ĐỊng dạng email không hợp lệ");
                res = false;
            }
        }
        if (matkhau.getText().toString().equals("")) {
            matkhau.setError("Vui Lòng Nhập Mật Khẩu");
            res = false;
        } else {
            if (matkhau.getText().toString().length() < 6) {
                matkhau.setError("Mật Khẩu Quá Ngắn");
                res = false;
            }
        }
        return res;
    }

    private void setEvent() {
        Context context = this;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KiemTra()) {
                    auth.signInWithEmailAndPassword(tendangnhap.getText().toString(), matkhau.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    reference.child("Shipper").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            Shipper shipper1 = snapshot.getValue(Shipper.class);
                                            if (task.isSuccessful()) {
                                                if (shipper1.getTrangThaiShipper() == TrangThaiShipper.BiKhoa){
                                                    //Toast.makeText(Login_Activity.this, "Tài khoản của bạn bị khóa", Toast.LENGTH_SHORT).show();
                                                    Alerter.create(Login_Activity.this)
                                                            .setTitle("Thông báo")
                                                            .setText("Tài khoản của bạn đã bị khóa")
                                                            .setBackgroundColorRes(R.color.madh)
                                                            .show();
                                                    auth.signOut();
                                                }
                                                else {
                                                    Toast.makeText(Login_Activity.this, "Đăng Nhập Thành Công!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), Home_Activity.class);
                                                    startActivity(intent);
                                                    matkhau.setText("");
                                                }

                                            } else {
                                                Toast.makeText(Login_Activity.this, "Thông tin tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                                                Alerter.create(Login_Activity.this)
                                                        .setTitle("Thông báo")
                                                        .setText("Thông tin tài khoản hoặc mật khẩu không chính xác")
                                                        .setBackgroundColorRes(R.color.madh)
                                                        .show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });

                                }
                            });
                }
            }
        });
        txtQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuenMatKhau_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void setConTrol() {
        login = findViewById(R.id.btnLogin);
        tendangnhap = findViewById(R.id.tendangnhap);
        matkhau = findViewById(R.id.matkhau);
        txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);
    }
}