package com.example.shiper.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shiper.ChiTietDonHang_Activity;
import com.example.shiper.Model.ThongBao;
import com.example.shiper.R;
import com.example.shiper.TrangThaiThongBao;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterThongBao extends RecyclerView.Adapter<AdapterThongBao.MyViewHolder> {
    Activity context;
    int resources;
    ArrayList<ThongBao> thongBaos;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
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
        if (thongBao.getTrangThaiThongBao()==TrangThaiThongBao.DaXem)
        {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }
        else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#CAC9C9"));

        }
        if (thongBao == null) {
            return;
        }
        if (thongBao.getTrangThaiThongBao()== TrangThaiThongBao.ChuaXem)
        {
            holder.lottieAnimationView.setVisibility(View.VISIBLE);
            holder.ivNotification.setVisibility(View.GONE);
        }
        else {
            holder.lottieAnimationView.setVisibility(View.GONE);
            holder.ivNotification.setVisibility(View.VISIBLE);
        }
        holder.tvTieuDe.setText(thongBao.getTieuDe());
        holder.tvNoiDung.setText(thongBao.getNoiDung());
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        String strDate= formatter.format(thongBao.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (thongBao.getLoaiThongBao())
                {
                    case DonHangShipper:
                        if (thongBao.getDonHang()!=null)
                        {
                            thongBao.setTrangThaiThongBao(TrangThaiThongBao.DaXem);
                            reference.child("ThongBao").child(auth.getUid()).child(thongBao.getIDThongBao()).setValue(thongBao).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Gson gson = new Gson();
                                    String data = gson.toJson(thongBao.getDonHang());
                                    Intent intent = new Intent(context, ChiTietDonHang_Activity.class);
                                    intent.putExtra("DataDonHang",data);
                                    context.startActivity(intent);
                                }
                            });
                        }
                        break;
                    case NORMAL:
                        AlertDialog.Builder b = new AlertDialog.Builder(context);

                        b.setTitle("Thông báo");
                        b.setMessage(thongBao.getNoiDung());

                        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog al = b.create();
                        reference.child("ThongBao").child(thongBao.getIDUSer()).child(thongBao.getIDThongBao()).child("trangThaiThongBao").setValue(TrangThaiThongBao.DaXem);
                        al.show();
                        break;

                }

            }
        });
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
        LottieAnimationView lottieAnimationView;
        LinearLayout linearLayout ;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTieuDe = itemView.findViewById(R.id.tv_TieuDe_ThongBao);
            tvNoiDung = itemView.findViewById(R.id.tv_NoiDung_ThongBao);
            tvNgayThongBao = itemView.findViewById(R.id.tv_ThoiGian_ThongBao);
            ivNotification = itemView.findViewById(R.id.image_notification);
            lottieAnimationView = itemView.findViewById(R.id.image_notification_new);
            linearLayout = itemView.findViewById(R.id.lnLayout);
        }
    }
}
