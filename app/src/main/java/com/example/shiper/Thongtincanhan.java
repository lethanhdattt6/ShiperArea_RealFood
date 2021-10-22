package com.example.shiper;


import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.shiper.Model.Shipper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;


import de.hdodenhof.circleimageview.CircleImageView;

public class Thongtincanhan extends AppCompatActivity {

    Uri uriAvatar;
    Shipper nguoiDungHienTai = new Shipper();
    DatabaseReference reference ;
    private FirebaseAuth auth;
    private Uri uriImage ;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    Button save;
    EditText edthoVaTen, edtemail, edtsdt, edtdiachi, edtngaysinh, edtbienso;
    CircleImageView iv_photo, profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtincanhan);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        reference = FirebaseDatabase.getInstance().getReference();
        LoadUser();
        setConTrol();
        setEvent();
    }

    private void LoadUser() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                nguoiDungHienTai = dataSnapshot.getValue(Shipper.class);
                edthoVaTen.setText( nguoiDungHienTai.getHoVaTen().toString());
                edtdiachi.setText( nguoiDungHienTai.getDiaChi().toString());
                edtngaysinh.setText( nguoiDungHienTai.getNgaySinh().toString());
                edtsdt.setText( nguoiDungHienTai.getSoDienThoai().toString());
                edtemail.setText( nguoiDungHienTai.geteMail().toString());
                edtbienso.setText( nguoiDungHienTai.getMaSoXe().toString());
                // ..

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        reference.child("Shipper").child(auth.getUid()).addValueEventListener(postListener);
        storageRef.child("Shipper").child(auth.getUid()).child("avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri.toString())
                        .into(profile);
            }
        });
    }


    private void setEvent() {
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                profile.setImageBitmap(r.getBitmap());
                                uriAvatar = r.getUri();
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(Thongtincanhan.this);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KiemTra())
                {
                    nguoiDungHienTai.setHoVaTen(edthoVaTen.getText().toString());
                    nguoiDungHienTai.setMaSoXe(edtbienso.getText().toString());
                    nguoiDungHienTai.setDiaChi(edtdiachi.getText().toString());
                    nguoiDungHienTai.setNgaySinh(edtngaysinh.getText().toString());
                    nguoiDungHienTai.setSoDienThoai(edtsdt.getText().toString());
                    reference.child("Shipper").child(auth.getUid()).setValue(nguoiDungHienTai).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(Thongtincanhan.this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private boolean KiemTra() {
        boolean res = true;
        if (edthoVaTen.getText().toString().equals("")) {
            edthoVaTen.setError("Vui Lòng Nhập Tên Của Bạn");
            res = false;
        }

        if (edtemail.getText().toString().equals("")) {
            edtemail.setError("Vui Lòng Nhập Email");
            res = false;
        }else {
            String emailAddress = edtemail.getText().toString().trim();
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                res = true;
            } else {
                edtemail.setError("ĐỊng dạng email không hợp lệ");
                res = false;
            }
        }

        if (edtdiachi.getText().toString().equals("")) {
            edtdiachi.setError("Vui Lòng Nhập Địa Chỉ");
            res = false;
        }
        if (edtsdt.getText().toString().equals("")) {
            edtsdt.setError("Vui Lòng Nhập Số Điện Thoại");
        }
        if (edtbienso.getText().toString().equals("")) {
            edtbienso.setError("Vui Lòng Nhập Số Điện Thoại");
        }
        if (edtngaysinh.getText().toString().equals("")) {
            edtngaysinh.setError("Vui Lòng Nhập Số Điện Thoại");
        }
        if(uriAvatar != null){
            storageRef.child("Shipper").child(auth.getUid()).child("avatar").putFile(uriAvatar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    LoadUser();
                }
            });
        }
        return res;
    }

    private void setConTrol() {
        save = findViewById(R.id.btnsave);
        edthoVaTen = findViewById(R.id.hovaten);
        edtemail = findViewById(R.id.email);
        edtsdt = findViewById(R.id.sodienthoai);
        edtngaysinh = findViewById(R.id.ngaysinh);
        edtbienso = findViewById(R.id.bienso);
        edtdiachi = findViewById(R.id.diachi);
        profile = findViewById(R.id.profile);
        iv_photo = findViewById(R.id.iv_camera);
    }
}