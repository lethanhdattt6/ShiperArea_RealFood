package com.example.shiper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Thongtincanhan extends AppCompatActivity {
    Button save;
    EditText edthoVaTen, edtemail, edtsdt, edtdiachi, edtngaysinh, edtcmnd, edtbienso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtincanhan);
        setConTrol();
        setEvent();
    }

    private void setEvent() {

    }

    private void setConTrol() {
        save = findViewById(R.id.btnsave);
        edthoVaTen = findViewById(R.id.hovaten);
        edtemail = findViewById(R.id.email);
        edtsdt = findViewById(R.id.sodienthoai);
        edtngaysinh = findViewById(R.id.ngaysinh);
        edtbienso = findViewById(R.id.bienso);
        edtdiachi = findViewById(R.id.diachi);
        edtcmnd = findViewById(R.id.cmnd);

    }
}