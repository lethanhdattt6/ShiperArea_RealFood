package com.example.shiper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shiper.Model.Shipper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


public class Login extends AppCompatActivity {
    EditText tendangnhap,matkhau;
    Button login;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        reference = FirebaseDatabase.getInstance().getReference();
        setConTrol();
        setEvent();
        DangNhap();
    }

    private void DangNhap() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = reference.child("Shipper").orderByChild("taiKhoan").equalTo(tendangnhap.getText().toString().trim());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                Shipper shiper = user.getValue(Shipper.class);
                                Toast.makeText(Login.this,shiper.getTaiKhoan(), Toast.LENGTH_SHORT).show();
                                if (shiper.getMatKhau().equals(matkhau.getText().toString().trim())) {

                                    Intent intent = new Intent(getApplicationContext(), Home.class);
//                                    Gson gson = new Gson();
//                                    String data = gson.toJson(shiper);
//                                    intent.putExtra("Shipper", data);
                                    startActivity(intent);
                                    //


                                } else {
                                    Toast.makeText(Login.this," Password is wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void setEvent() {

    }

    private void setConTrol() {
        login = findViewById(R.id.btnLogin);
        tendangnhap = findViewById(R.id.tendangnhap);
        matkhau = findViewById(R.id.matkhau);
    }
}