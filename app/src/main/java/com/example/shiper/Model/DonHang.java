package com.example.shiper.Model;

public class DonHang {
    String ma, tenDH, trangThai, tenCH, diaChiCH, diaChiNN,tongDon,sdtCH;
    int anh;

    public DonHang() {
    }

    public DonHang(String ma, String tenDH, String trangThai, String tenCH, String diaChiCH, String diaChiNN, String tongDon, String sdtCH, int anh) {
        this.ma = ma;
        this.tenDH = tenDH;
        this.trangThai = trangThai;
        this.tenCH = tenCH;
        this.diaChiCH = diaChiCH;
        this.diaChiNN = diaChiNN;
        this.tongDon = tongDon;
        this.sdtCH = sdtCH;
        this.anh = anh;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTenDH() {
        return tenDH;
    }

    public void setTenDH(String tenDH) {
        this.tenDH = tenDH;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenCH() {
        return tenCH;
    }

    public void setTenCH(String tenCH) {
        this.tenCH = tenCH;
    }

    public String getDiaChiCH() {
        return diaChiCH;
    }

    public void setDiaChiCH(String diaChiCH) {
        this.diaChiCH = diaChiCH;
    }

    public String getDiaChiNN() {
        return diaChiNN;
    }

    public void setDiaChiNN(String diaChiNN) {
        this.diaChiNN = diaChiNN;
    }

    public String getTongDon() {
        return tongDon;
    }

    public void setTongDon(String tongDon) {
        this.tongDon = tongDon;
    }

    public String getSdtCH() {
        return sdtCH;
    }

    public void setSdtCH(String sdtCH) {
        this.sdtCH = sdtCH;
    }

    public int getAnh() {
        return anh;
    }

    public void setAnh(int anh) {
        this.anh = anh;
    }
}