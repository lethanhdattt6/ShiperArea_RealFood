package com.example.shiper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
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
import com.example.shiper.QuanLy;
import com.example.shiper.R;
import com.example.shiper.TrangThaiDonHang;
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

public class AdapterDonHang extends RecyclerView.Adapter<AdapterDonHang.DonHangViewHolder> {
    Context context;
    private int resource;
    private ArrayList<DonHang> display;
    //chính này
    ArrayList<DonHang> donHangs;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseStorage storage;
    private StorageReference storageRef;
    QuanLy quanLy;

    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        quanLy = new QuanLy();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemdonhang,parent, false);
        return new DonHangViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDonHang.DonHangViewHolder holder, int position) {
        DonHang donHang = display.get(position);
        if (donHang == null) {
            return;
        }
        //
        quanLy.SetColorOfStatus(donHang.getTrangThai(),holder.lnTrangThai);
        holder.trangThai.setText(quanLy.GetStringTrangThaiDonHang(donHang.getTrangThai()));
        holder.maDH.setText("Mã ĐH : " + donHang.getIDDonHang().substring(0,10));
        holder.tvDiaChiNN.setText("Địa chỉ người nhận : "+ donHang.getDiaChi());
        holder.tvTongDon.setText("Tổng đơn : "+ donHang.getTongTien() + "VNĐ");
        reference.child("CuaHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CuaHang cuaHang = dataSnapshot.getValue(CuaHang.class);
                    if(cuaHang.getIDCuaHang().equals(donHang.getIDCuaHang())){
                        holder.tvDiaChiCuaHang.setText("Địa chỉ cửa hàng : "+ cuaHang.getDiaChi());
                        holder.tvsdtCH.setText("SĐT cửa hàng" + cuaHang.getSoDienThoai());
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
                                try {
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
                String data = gson.toJson(donHang);
                Intent intent = new Intent(context, ChiTietDonHang_Activity.class);
                intent.putExtra("DataDonHang",data);
                context.startActivity(intent);
            }
        });
    }
    public  AdapterDonHang(Context context ,int resource , ArrayList<DonHang>donHangs)
    {
        this.context = context;
        this.resource = resource;
        this.donHangs = donHangs;
        this.display = donHangs;
    }
    @Override
    public int getItemCount() {
        return display.size();
    }
    @Override
    public int getItemViewType(int position) {
        return resource;
    }
    public void setData(ArrayList<DonHang> data){
        this.donHangs = data;
        this.display = data;
        notifyDataSetChanged();
    }

    public class DonHangViewHolder extends RecyclerView.ViewHolder {
        ImageView imganhDH;
        TextView tvsdtCH, tvDiaChiCuaHang, tvDiaChiNN, tvTongDon, maDH, tvTenSanPham ,trangThai;
        LinearLayout lineardonhang, lnTrangThai;
        public DonHangViewHolder(View view){
            super(view);
            maDH = view.findViewById(R.id.maDonHang);
            imganhDH = view.findViewById(R.id.imganhdonhang);
            tvDiaChiCuaHang = view.findViewById(R.id.tvDiaChiCuaHang);
            tvDiaChiNN = view.findViewById(R.id.tvDiaChiNguoiNhan);
            tvsdtCH = view.findViewById(R.id.tvsdtCuaHang);
            tvTongDon = view.findViewById(R.id.tvTongDon);
            lineardonhang = view.findViewById(R.id.lndonhang);
            tvTenSanPham = view.findViewById(R.id.tvtensanpham);
            trangThai = view.findViewById(R.id.trangthai);
            lnTrangThai = view.findViewById(R.id.lntrangthai);

        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    display = donHangs;
                } else {
                    ArrayList<DonHang> list = new ArrayList<>();
                    for (DonHang donHang: donHangs) {
                        if (donHang.getTrangThai().toString().equals(strSearch)
                                ||donHang.getIDDonHang().contains(strSearch)
                                ||donHang.getSoDienThoai().contains(strSearch)
                                ||donHang.getDiaChi().contains(strSearch)) {
                            list.add(donHang);
                        }
                    }
                    display = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = display;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                display = (ArrayList<DonHang>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
