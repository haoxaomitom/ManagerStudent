/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asmjava3.view.model;

import java.util.Date;

/**
 *
 * @author soaic
 */
public class SinhVien {
    private String Masv, tenSV, diaChi, hinhAnh;
    private boolean gioiTinh;
    private Date ngaySinh;

    public SinhVien() {
    }

    public SinhVien(String Masv, String tenSV, String diaChi, String hinhAnh, boolean gioiTinh, Date ngaySinh) {
        this.Masv = Masv;
        this.tenSV = tenSV;
        this.diaChi = diaChi;
        this.hinhAnh = hinhAnh;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
    }

    public String getMasv() {
        return Masv;
    }

    public SinhVien(String Masv, String tenSV) {
        this.Masv = Masv;
        this.tenSV = tenSV;
    }

    
    public void setMasv(String Masv) {
        this.Masv = Masv;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
    
}
