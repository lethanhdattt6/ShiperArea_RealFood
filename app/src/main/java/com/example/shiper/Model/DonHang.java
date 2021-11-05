package com.example.shiper.Model;

import com.example.shiper.TrangThaiDonHang;

import java.util.Date;

public class DonHang {
    String  IDDonHang , IDKhachHang , IDShipper , diaChi, soDienThoai, ghiChu_KhachHang, ghiChu_Shipper,IDCuaHang;
    double tongTien;
    Date ngayTao;
    TrangThaiDonHang trangThai;

    public DonHang(String IDDonHang, String IDKhachHang, String IDShipper, String diaChi, String soDienThoai, String ghiChu_KhachHang, String ghiChu_Shipper, String IDCuaHang, double tongTien, Date ngayTao, TrangThaiDonHang trangThai) {
        this.IDDonHang = IDDonHang;
        this.IDKhachHang = IDKhachHang;
        this.IDShipper = IDShipper;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.ghiChu_KhachHang = ghiChu_KhachHang;
        this.ghiChu_Shipper = ghiChu_Shipper;
        this.IDCuaHang = IDCuaHang;
        this.tongTien = tongTien;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }

    public String getIDDonHang() {
        return IDDonHang;
    }

    public void setIDDonHang(String IDDonHang) {
        this.IDDonHang = IDDonHang;
    }

    public String getIDKhachHang() {
        return IDKhachHang;
    }

    public void setIDKhachHang(String IDKhachHang) {
        this.IDKhachHang = IDKhachHang;
    }

    public String getIDShipper() {
        return IDShipper;
    }

    public void setIDShipper(String IDShipper) {
        this.IDShipper = IDShipper;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getGhiChu_KhachHang() {
        return ghiChu_KhachHang;
    }

    public void setGhiChu_KhachHang(String ghiChu_KhachHang) {
        this.ghiChu_KhachHang = ghiChu_KhachHang;
    }

    public String getGhiChu_Shipper() {
        return ghiChu_Shipper;
    }

    public void setGhiChu_Shipper(String ghiChu_Shipper) {
        this.ghiChu_Shipper = ghiChu_Shipper;
    }

    public String getIDCuaHang() {
        return IDCuaHang;
    }

    public void setIDCuaHang(String IDCuaHang) {
        this.IDCuaHang = IDCuaHang;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public TrangThaiDonHang getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiDonHang trangThai) {
        this.trangThai = trangThai;
    }

    public DonHang() {
    }
}