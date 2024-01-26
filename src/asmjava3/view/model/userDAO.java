/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asmjava3.view.model;

import database.DatabaseUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 *
 * @author soaic
 */
public class userDAO {
    public user getUserByID(String username) {
        Connection conn = null;
        PreparedStatement sttm = null;
        ResultSet rs = null;
        user ee = new user();
        try {
            String sSQL = "Select * from Users where username = ?";
            conn = DatabaseUtils.getDBConnect();
            sttm = conn.prepareStatement(sSQL);
            sttm.setString(1, username);
            rs = sttm.executeQuery();
            while (rs.next()) {
                ee.setUsername(rs.getString(1));
                ee.setPass(rs.getString(2));
                ee.setRole(rs.getBoolean(3));
                ee.setFullname(rs.getString(4));
                ee.setEmail(rs.getString(5));
                return ee;
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

    public boolean checkLogin(String username, String pass) {
        user user = getUserByID(username);
        if (user != null) {
            if (user.getPass().equals(pass)) {
                return true;
            }
        }
        return false;
    }
    public int getRole(String username, String pass) {
        user user = getUserByID(username);
        if (user != null) {
            if (user.getPass().equals(pass)) {
                return user.getRole() ? 1 : 0; // Chuyển đổi giá trị boolean (true/false) thành (1/0)
            }
        }
        return -1; // -1 để thể hiện rằng thông tin người dùng không hợp lệ hoặc không tìm thấy người dùng
    }
    
    public String getFullname(String username) {
        user user = getUserByID(username);
        if (user != null) {
            return user.getFullname();
        }
        return null;
    }

    public String getEmail(String username) {
        user user = getUserByID(username);
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }
}

