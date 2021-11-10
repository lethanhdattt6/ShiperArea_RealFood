package com.example.shiper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiper.ChiTietDonHang_Activity;
import com.example.shiper.Model.CuaHang;
import com.example.shiper.Model.DonHang;
import com.example.shiper.Model.DonHangInfo;
import com.example.shiper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterSoLuong extends RecyclerView.Adapter<AdapterSoLuong.SoLuongViewHolder> {
    Context context;
    ArrayList<DonHangInfo> donHangs;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    @NonNull
    @Override
    public AdapterSoLuong.SoLuongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_soluongvatensanpham,parent, false);
        return new SoLuongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoLuongViewHolder holder, int position) {
        DonHangInfo donHangInfo = donHangs.get(position);
        if (donHangInfo == null) {
            return;
        }
        holder.tenSanPham.setText( donHangInfo.getSanPham().getTenSanPham());
        holder.soLuong.setText(" : "+ donHangInfo.getSoLuong());

    }

    public  AdapterSoLuong(Context context)
    {
        this.context = context;
    }
    @Override
    public int getItemCount() {
        return donHangs.size();
    }
    public void setData(ArrayList<DonHangInfo> data){
        this.donHangs = data;
        notifyDataSetChanged();
    }

    public class SoLuongViewHolder extends RecyclerView.ViewHolder {
        TextView soLuong, tenSanPham;
        public SoLuongViewHolder(@NonNull View itemView) {
            super(itemView);
            soLuong = itemView.findViewById(R.id.tvsoluong);
            tenSanPham = itemView.findViewById(R.id.tvtensanpham);
        }
    }
}
