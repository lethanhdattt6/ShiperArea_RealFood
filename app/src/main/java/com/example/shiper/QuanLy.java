package com.example.shiper;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.shiper.Model.TrangThai;
import com.example.shiper.Model.TrangThaiShipper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class QuanLy {
    public DatabaseReference mDatabase ;
    public StorageReference storageRef ;
    public FirebaseAuth auth;

    public QuanLy() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        auth= FirebaseAuth.getInstance();
    }

    //Thay đổi trạng thái của đơn hàng
    public String GetStringTrangThaiDonHang(TrangThaiDonHang trangThaiDonHang) {
        String res = "";
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DangGiaoShipper) {
            res = "Đơn hàng có thể nhận";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoShipperLayHang) {
            res = "Chờ shipper lấy hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoXacNhanGiaoHangChoShipper) {
            res = "Chờ Shop xác nhận giao hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_Tien) {
            res = "Chờ Shop xác nhận đã nhận tiền hàng từ Shipper";
        }
        if (trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_TraHang) {
            res = "Chờ Shop xác nhận đã nhận hàng trả về từ Shipper";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaLayHang) {
            res = "Đã lấy hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_KhongNhanGiaoHang) {
            res = "Đã từ chối đơn hàng này";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaTraHang) {
            res = "Đã trả hàng cho cửa hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaChuyenTien) {
            res = "Đã chuyển tiền cho cửa hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoKhongThanhCong) {
            res = "Giao hàng không thành công";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DangGiaoHang) {
            res = "Đang giao hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoThanhCong) {
            res = "Giao hàng thành công";
        }
        return res;
    }
    //set màu khi trạng thái đơn hàng thay đổi
    public void SetColorOfStatus(TrangThaiDonHang trangThaiDonHang, View view){
        //textView.setTextColor(Color.WHITE);
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoKhongThanhCong)
        {
            view.setBackgroundColor(Color.parseColor("#54D6BA"));
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DangGiaoHang)
        {
            view.setBackgroundColor(Color.parseColor("#F0DE38"));
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DangGiaoShipper)
        {
            view.setBackgroundColor(Color.parseColor("#31BAF0"));
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoThanhCong)
        {
            view.setBackgroundColor(Color.parseColor("#D65F5E"));
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoShipperLayHang)
        {
            view.setBackgroundColor(Color.parseColor("#2CFA6B"));
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_ChoDiGiao)
        {
            view.setBackgroundColor(Color.parseColor("#05E6C6"));
        }
    }
    public void SetColor(TrangThaiShipper trangThai, TextView textView){
        textView.setTextColor(Color.WHITE);
        if (trangThai == TrangThaiShipper.KhongHoatDong||trangThai==TrangThaiShipper.BiKhoa)
        {
            textView.setTextColor(Color.parseColor("#F0290E"));
        }
        if (trangThai== TrangThaiShipper.DangHoatDong)
        {
            textView.setTextColor(Color.parseColor("#4CAF50"));
        }
        if (trangThai== TrangThaiShipper.DangGiaoHang)
        {
            textView.setTextColor(Color.parseColor("#E8DF3C"));
        }
    }
    public void SetLichSuColor(TrangThaiDonHang trangThai, TextView textView){
        textView.setTextColor(Color.WHITE);
        if (trangThai == TrangThaiDonHang.Shipper_DaTraHang)
        {
            textView.setTextColor(Color.parseColor("#F0290E"));
        }
//        if (trangThai== TrangThaiShipper.DangHoatDong)
//        {
//            textView.setTextColor(Color.parseColor("#4CAF50"));
//        }
//        if (trangThai== TrangThaiShipper.DangGiaoHang)
//        {
//            textView.setTextColor(Color.parseColor("#E8DF3C"));
//        }
    }
}
