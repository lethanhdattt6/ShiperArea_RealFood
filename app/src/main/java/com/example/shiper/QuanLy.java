package com.example.shiper;

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
}
