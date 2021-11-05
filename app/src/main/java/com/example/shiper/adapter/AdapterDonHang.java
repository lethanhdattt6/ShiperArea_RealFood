package com.example.shiper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiper.Model.CuaHang;
import com.example.shiper.Model.DonHang;
import com.example.shiper.Model.DonHangInfo;
import com.example.shiper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterDonHang extends RecyclerView.Adapter<AdapterDonHang.DonHangViewHolder> {
    Context context;
    ArrayList<DonHang> donHangs;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemdonhang,parent, false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDonHang.DonHangViewHolder holder, int position) {

        DonHang donHang = donHangs.get(position);
        if (donHang == null) {
            return;
        }
        holder.maDH.setText("Mã ĐH : " + donHang.getIDDonHang());
        holder.tvDiaChiNN.setText("Địa chỉ người nhận : "+ donHang.getDiaChi());

        reference.child("CuaHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CuaHang cuaHang = dataSnapshot.getValue(CuaHang.class);
                    if(cuaHang.getIDCuaHang().equals(donHang.getIDCuaHang())){
                        holder.tvDiaChiCuaHang.setText("Địa chỉ cửa hàng : "+ cuaHang.getDiaChi());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    public  AdapterDonHang(Context context)
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

    public class DonHangViewHolder extends RecyclerView.ViewHolder {
        ImageView imganhDH;
        TextView tvsdtCH, tvDiaChiCuaHang, tvDiaChiNN, tvTongDon, maDH;
        public DonHangViewHolder(View view){
            super(view);
            maDH = view.findViewById(R.id.maDonHang);
            imganhDH = view.findViewById(R.id.imganhdonhang);
            tvDiaChiCuaHang = view.findViewById(R.id.tvDiaChiCuaHang);
            tvDiaChiNN = view.findViewById(R.id.tvDiaChiNguoiNhan);
            tvsdtCH = view.findViewById(R.id.tvsdtCuaHang);
            tvTongDon = view.findViewById(R.id.tvTongDon);
        }
    }
}
