package com.example.shiper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shiper.ChiTietDonHang_Activity;
import com.example.shiper.Model.CuaHang;
import com.example.shiper.Model.DonHang;
import com.example.shiper.Model.DonHangInfo;
import com.example.shiper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterLichSuGiaoHang extends RecyclerView.Adapter<AdapterLichSuGiaoHang.LichSuViewHolder> {
    Context context;
    ArrayList<DonHang> donHangs;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @NonNull
    @Override
    public LichSuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlichsu,parent, false);
        return new LichSuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLichSuGiaoHang.LichSuViewHolder holder, int position) {
        DonHang donHang = donHangs.get(position);
        if (donHang == null) {
            return;
        }
        //
        holder.maDH.setText("Mã ĐH : " + donHang.getIDDonHang().substring(0,7));
        holder.tvDiaChiNN.setText("Địa chỉ người nhận : "+ donHang.getDiaChi());
        holder.tvTongDon.setText("Tổng đơn : "+ donHang.getTongTien());
        holder.tvTrangThai.setText(donHang.getTrangThai()+"");
        reference.child("CuaHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CuaHang cuaHang = dataSnapshot.getValue(CuaHang.class);
                    if(cuaHang.getIDCuaHang().equals(donHang.getIDCuaHang())){
                        holder.tvDiaChiCuaHang.setText("Địa chỉ cửa hàng : "+ cuaHang.getDiaChi());
                        holder.tvTenCuaHang.setText("Cửa hàng : " + cuaHang.getTenCuaHang());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        reference.child("DonHangInfo").orderByChild("iddonHang").equalTo(donHang.getIDDonHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.tvTenSanPham.setText("");
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    holder.tvTenSanPham.setText(holder.tvTenSanPham.getText() +
                            donHangInfo.getSanPham().getTenSanPham()+" , ");
                    storageRef.child("SanPham").child(donHangInfo.getSanPham()
                            .getIDCuaHang()).child(donHangInfo.getSanPham().getIDSanPham())
                            .child(donHangInfo.getSanPham().getImages().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            try{
                                Glide.with(context)
                                        .load(task.getResult().toString())
                                        .into(holder.imganhDH);
                            }catch (Exception e){

                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        holder.lineardonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String data = gson.toJson(donHangs.get(position));
                Intent intent = new Intent(context, ChiTietDonHang_Activity.class);
                intent.putExtra("DataDonHang",data);
                context.startActivity(intent);
            }
        });
    }
    public  AdapterLichSuGiaoHang(Context context)
    {
        this.context = context;
    }
    @Override
    public int getItemCount() {
        return donHangs.size();
    }
    public void setData(ArrayList<DonHang> data){
        this.donHangs = data;
        notifyDataSetChanged();
    }

    public class LichSuViewHolder extends RecyclerView.ViewHolder {
        ImageView imganhDH;
        TextView tvTenCuaHang, tvDiaChiCuaHang, tvDiaChiNN, tvTongDon, maDH, tvTenSanPham,tvTrangThai;
        LinearLayout lineardonhang;
        public LichSuViewHolder(View view){
            super(view);
            maDH = view.findViewById(R.id.maDonHang);
            imganhDH = view.findViewById(R.id.productimg);
            tvDiaChiCuaHang = view.findViewById(R.id.diaChiCuaHang);
            tvDiaChiNN = view.findViewById(R.id.diaChiNguoiNhan);
            tvTongDon = view.findViewById(R.id.tvTongDon);
            lineardonhang = view.findViewById(R.id.lnlichsu);
            tvTenSanPham = view.findViewById(R.id.tvTenSanPham);
            tvTenCuaHang = view.findViewById(R.id.tvTenCuaHang);
            tvTrangThai = view.findViewById(R.id.trangthai);

        }
    }
}
