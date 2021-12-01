package com.example.shiper.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiper.Model.ThongBao;
import com.example.shiper.R;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;

public class AdapterThongBao extends RecyclerView.Adapter<AdapterThongBao.MyViewHolder> {
    Activity context;
    int resources;
    ArrayList<ThongBao> thongBaos;

    public AdapterThongBao(Activity context, int resources, ArrayList<ThongBao> thongBaos) {
        this.context = context;
        this.resources = resources;
        this.thongBaos = thongBaos;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        ThongBao thongBao = thongBaos.get(position);
        if (thongBao == null) {
            return;
        }
        holder.tvTieuDe.setText(thongBao.getTieuDe());
        holder.tvNoiDung.setText(thongBao.getNoiDung());
        String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(thongBao.getDate());
        holder.tvNgayThongBao.setText(date);
    }

    @Override
    public int getItemViewType(int position) {
        return resources;
    }

    @Override
    public int getItemCount() {
        return thongBaos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTieuDe, tvNoiDung, tvNgayThongBao;
        ImageView ivNotification;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTieuDe = itemView.findViewById(R.id.tv_TieuDe_ThongBao);
            tvNoiDung = itemView.findViewById(R.id.tv_NoiDung_ThongBao);
            tvNgayThongBao = itemView.findViewById(R.id.tv_ThoiGian_ThongBao);
            ivNotification = itemView.findViewById(R.id.image_notification);
        }
    }
}
