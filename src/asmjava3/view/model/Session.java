/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asmjava3.view.model;

public class Session {
    private static Session instance;

    private String username;
    private String password;
    private String fullname;
    private String email;

    private Session() {
        // Private constructor to prevent direct instantiation
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUserInformation(String fullname, String email) {
        this.fullname = fullname;
        this.email = email;
    }

    public void clearCredentials() {
        this.username = null;
        this.password = null;
        this.fullname = null;
        this.email = null;
    }

    public boolean isLoggedIn() {
        return username != null && password != null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }
}
