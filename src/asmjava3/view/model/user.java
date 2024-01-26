/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asmjava3.view.model;

/**
 *
 * @author soaic
 */
public class user {
    private String username, pass, fullname, email;
    private boolean role;

    public user() {
    }

    public user(String username, String pass, String fullname, String email, boolean role) {
        this.username = username;
        this.pass = pass;
        this.fullname = fullname;
        this.email = email;
        this.role = role;
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }
    public boolean getRole() {
        return role;
    }
}

    