package com.example.shiper.Model;

public class Shipper {
    String iDShipper, eMail, matKhau, hoVaTen, diaChi, khuVucHoatDong, ngaySinh, maSoXe, soDienThoai, loaiShipper;
    TrangThaiShipper trangThaiShipper;

    public String getiDShipper() {
        return iDShipper;
    }

    public void setiDShipper(String iDShipper) {
        this.iDShipper = iDShipper;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
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

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getLoaiShipper() {
        return loaiShipper;
    }

    public void setLoaiShipper(String loaiShipper) {
        this.loaiShipper = loaiShipper;
    }

    public TrangThaiShipper getTrangThaiShipper() {
        return trangThaiShipper;
    }

    public void setTrangThaiShipper(TrangThaiShipper trangThaiShipper) {
        this.trangThaiShipper = trangThaiShipper;
    }

    public Shipper() {
    }

    public Shipper(String iDShipper, String eMail, String matKhau, String hoVaTen, String diaChi, String khuVucHoatDong, String ngaySinh, String maSoXe, String soDienThoai, String loaiShipper, TrangThaiShipper trangThaiShipper) {
        this.iDShipper = iDShipper;
        this.eMail = eMail;
        this.matKhau = matKhau;
        this.hoVaTen = hoVaTen;
        this.diaChi = diaChi;
        this.khuVucHoatDong = khuVucHoatDong;
        this.ngaySinh = ngaySinh;
        this.maSoXe = maSoXe;
        this.soDienThoai = soDienThoai;
        this.loaiShipper = loaiShipper;
        this.trangThaiShipper = trangThaiShipper;
    }
}