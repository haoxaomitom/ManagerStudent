/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asmjava3.view.model;

import database.DatabaseUtils;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class SinhVienDAO {

    SimpleDateFormat format_date = new SimpleDateFormat("yyyy/MM/dd");
    public static List<SinhVien> ls = new ArrayList<>();
    public int add(SinhVien sv) {
        Connection conn = null;
        PreparedStatement sttm = null;
        try {
            String sSQL = "insert into SinhVien(MaSV,TenSV,NgaySinh,GioiTinh,DiaChi,HinhAnh) values (?,?,?,?,?,?)";
            conn = DatabaseUtils.getDBConnect();
            sttm = conn.prepareStatement(sSQL);
            sttm.setString(1, sv.getMasv());
            sttm.setString(2, sv.getTenSV());
            sttm.setString(3, format_date.format(sv.getNgaySinh()));
            sttm.setBoolean(4, sv.isGioiTinh());
            sttm.setString(5, sv.getDiaChi());
            sttm.setString(6, sv.getHinhAnh());
            if (sttm.executeUpdate() > 0) {
                System.out.println("Insert Successfully");
                return 1;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } finally {
            try {
                sttm.close();
                conn.close();
            } catch (Exception e) {

            }
        }
        return -1;
    }

    public int updateSinhVienByID(SinhVien sv) {
        Connection conn = null;
        PreparedStatement sttm = null;
        try {
            String sSQL = "update sinhvien set tensv=?, ngaysinh=?,gioitinh=?,diachi=?,hinhanh=? where masv=?";
            conn = DatabaseUtils.getDBConnect();
            sttm = conn.prepareStatement(sSQL);
            sttm.setString(6, sv.getMasv());
            sttm.setString(1, sv.getTenSV());
            sttm.setString(2, format_date.format(sv.getNgaySinh()));
            sttm.setBoolean(3, sv.isGioiTinh());
            sttm.setString(4, sv.getDiaChi());
            sttm.setString(5, sv.getHinhAnh());
            if (sttm.executeUpdate() > 0) {
                System.out.println("Insert Successfully");
                return 1;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } finally {
            try {
                sttm.close();
                conn.close();
            } catch (Exception e) {

            }
        }
        return -1;
    }

//    public List<SinhVien> getAllSinhVien() {
//        return ls;
//    }
    public int delSinhVienByID(String sv) {
        Connection conn = null;
        PreparedStatement sttm = null;
        try {
            String sSQL = "delete sinhvien where masv = ?";
            conn = DatabaseUtils.getDBConnect();
            sttm = conn.prepareStatement(sSQL);
            sttm.setString(1, sv);

            if (sttm.executeUpdate() > 0) {
                System.out.println("Delete Successfully");
                return 1;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        } finally {
            try {
                sttm.close();
                conn.close();
            } catch (Exception e) {

            }
        }
        return -1;
    }

    public List<SinhVien> getAllSinhVien() {
        List<SinhVien> ls = new ArrayList<>();
        Connection conn = null;
        ResultSet rs = null;
        Statement sttm = null;
        try {
            String sSQL = "select * from sinhvien";
            conn = DatabaseUtils.getDBConnect();
            sttm = conn.createStatement();
            rs = sttm.executeQuery(sSQL);
            while (rs.next()) {
                SinhVien sv = new SinhVien();
                sv.setMasv(rs.getString(1));
                sv.setTenSV(rs.getString(2));
                sv.setNgaySinh(rs.getDate(3));
                sv.setGioiTinh(rs.getBoolean(4));
                sv.setDiaChi(rs.getString(5));
                sv.setHinhAnh(rs.getString(6));
                ls.add(sv);
            }
        } catch (Exception e) {
            System.out.println("Erorr: " + e.toString());
        } finally {
            try {
                rs.close();
                sttm.close();
                conn.close();
            } catch (Exception e) {

            }
        }
        return ls;
    }

    public SinhVien getSinhVienByID(String MaSV) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement sttm = null;
        try {
            String sSQL = "Select * from sinhvien where Masv = ?";
            conn = DatabaseUtils.getDBConnect();
            sttm = conn.prepareStatement(sSQL);
            sttm.setString(1, MaSV);
            rs = sttm.executeQuery();
            while (rs.next()) {
                SinhVien sv = new SinhVien();
                sv.setMasv(rs.getString(1));
                sv.setTenSV(rs.getString(2));
                sv.setNgaySinh(rs.getDate(3));
                sv.setGioiTinh(rs.getBoolean(4));
                sv.setDiaChi(rs.getString(5));
                sv.setHinhAnh(rs.getString(6));
                return sv;
            }
        } catch (Exception e) {
            System.out.println("Erorr: " + e.toString());
        } finally {
            try {
                rs.close();
                sttm.close();
                conn.close();
            } catch (Exception e) {

            }
        }
        return null;
    }
//    public int delSinhVienByID(String ma) {
//        for (SinhVien sv : ls) {
//            if (sv.getMasv().equalsIgnoreCase(ma)) {
//                ls.remove(sv);
//                return 1;
//            }
//        }
//        return -1;
//    }

//    public SinhVien getSinhVienByID(String ma) {
//        for (SinhVien sv : ls) {
//            if (sv.getMasv().equalsIgnoreCase(ma)) {
//                return sv;
//            }
//        }
//        return null;
//    }
//    public int updateSinhVienByID(SinhVien svNew) {
//        for (SinhVien sv : ls) {
//            if (sv.getMasv().equalsIgnoreCase(svNew.getMasv())) {
//                sv.setDiaChi(svNew.getDiaChi());
//                sv.setGioiTinh(svNew.isGioiTinh());
//                sv.setHinhAnh(svNew.getHinhAnh());
//                sv.setNgaySinh(svNew.getNgaySinh());
//                sv.setTenSV(svNew.getTenSV());
//                return 1;
//            }
//        }
//        return -1;
//    }
}
