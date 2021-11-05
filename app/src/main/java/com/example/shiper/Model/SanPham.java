package com.example.shiper.Model;

import java.util.ArrayList;

public class SanPham {
    String IDSanPham, TenSanPham, IDLoai, IDDanhMuc, Gia, ChiTietSanPham, IDCuaHang;
    Float Rating;
    ArrayList<String> images;

    public SanPham() {
    }



    public SanPham(String IDSanPham, String tenSanPham, String IDLoai, String IDDanhMuc, String gia, String chiTietSanPham, String IDCuaHang, Float rating, ArrayList<String> images) {
        this.IDSanPham = IDSanPham;
        TenSanPham = tenSanPham;
        this.IDLoai = IDLoai;
        this.IDDanhMuc = IDDanhMuc;
        this.Gia = gia;
        this.ChiTietSanPham = chiTietSanPham;
        this.IDCuaHang = IDCuaHang;
        this.Rating = rating;
        this.images = images;
    }

    public String getIDSanPham() {
        return IDSanPham;
    }

    public void setIDSanPham(String IDSanPham) {
        this.IDSanPham = IDSanPham;
    }

    public String getTenSanPham() {
        return TenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        TenSanPham = tenSanPham;
    }

    public String getIDLoai() {
        return IDLoai;
    }

    public void setIDLoai(String IDLoai) {
        this.IDLoai = IDLoai;
    }

    public String getIDDanhMuc() {
        return IDDanhMuc;
    }

    public void setIDDanhMuc(String IDDanhMuc) {
        this.IDDanhMuc = IDDanhMuc;
    }

    public String getGia() {
        return Gia;
    }

    public void setGia(String gia) {
        Gia = gia;
    }

    public String getChiTietSanPham() {
        return ChiTietSanPham;
    }

    public void setChiTietSanPham(String chiTietSanPham) {
        ChiTietSanPham = chiTietSanPham;
    }

    public String getIDCuaHang() {
        return IDCuaHang;
    }

    public void setIDCuaHang(String IDCuaHang) {
        this.IDCuaHang = IDCuaHang;
    }

    public Float getRating() {
        return Rating;
    }

    public void setRating(Float rating) {
        Rating = rating;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }


    @Override
    public String toString() {
        return "SanPham{" +
                "IDSanPham='" + IDSanPham + '\'' +
                ", TenSanPham='" + TenSanPham + '\'' +
                ", IDLoai='" + IDLoai + '\'' +
                ", IDDanhMuc='" + IDDanhMuc + '\'' +
                ", Gia='" + Gia + '\'' +
                ", ChiTietSanPham='" + ChiTietSanPham + '\'' +
                ", IDCuaHang='" + IDCuaHang + '\'' +
                ", Rating=" + Rating +
                ", images=" + images +
                '}';
    }
}