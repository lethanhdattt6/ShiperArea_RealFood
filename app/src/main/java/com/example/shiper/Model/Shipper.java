package com.example.shiper.Model;

public class Shipper {
    public Shipper() {
    }

    String iDShipper, taiKhoan, matKhau, hoVaTen, diaChu, khuVucHoatDong, ngaySinh, maSoXe, trangThaiHoatDong, soDienThoai;

    public String getiDShipper() {
        return iDShipper;
    }

    public void setiDShipper(String iDShipper) {
        this.iDShipper = iDShipper;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getDiaChu() {
        return diaChu;
    }

    public void setDiaChu(String diaChu) {
        this.diaChu = diaChu;
    }

    public String getKhuVucHoatDong() {
        return khuVucHoatDong;
    }

    public void setKhuVucHoatDong(String khuVucHoatDong) {
        this.khuVucHoatDong = khuVucHoatDong;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getMaSoXe() {
        return maSoXe;
    }

    public void setMaSoXe(String maSoXe) {
        this.maSoXe = maSoXe;
    }

    public String getTrangThaiHoatDong() {
        return trangThaiHoatDong;
    }

    public void setTrangThaiHoatDong(String trangThaiHoatDong) {
        this.trangThaiHoatDong = trangThaiHoatDong;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public Shipper(String iDShipper, String taiKhoan, String matKhau, String hoVaTen, String diaChu, String khuVucHoatDong, String ngaySinh, String maSoXe, String trangThaiHoatDong, String soDienThoai) {
        this.iDShipper = iDShipper;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.hoVaTen = hoVaTen;
        this.diaChu = diaChu;
        this.khuVucHoatDong = khuVucHoatDong;
        this.ngaySinh = ngaySinh;
        this.maSoXe = maSoXe;
        this.trangThaiHoatDong = trangThaiHoatDong;
        this.soDienThoai = soDienThoai;

    }
}
