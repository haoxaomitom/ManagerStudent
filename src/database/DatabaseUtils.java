/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author soaic
 */
public class DatabaseUtils {

    public static final String connectionUrl = "jdbc:sqlserver://localhost;" + "databaseName=DB_ASS_Java3;user=sa;password=1;IntegratedSecurity=true;encrypt=true;trustServerCertificate=true";

    public static Connection getDBConnect() {

        try{
            Connection con = null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            return con;
        }catch (ClassNotFoundException e ){
            System.out.println("Not have driver yet !"+e.toString());
            System.out.println("Error: "+e.toString());
        }catch(Exception e){
            System.out.println("Erorr to connect: "+e.toString());
        }
        return null;
    }
}
