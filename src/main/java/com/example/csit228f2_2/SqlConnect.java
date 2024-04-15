package com.example.csit228f2_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnect {
    public static final String URL = "jdbc:mysql://localhost:3306/delapenajavadb";
    public static final String username = "ZyleGeraldedelaPena";
    public static final String password = "123456";
    public static Connection getConnection(){
        Connection c = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL,username,password);
            System.out.println("Connected to the database");
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

        return c;
    }

    public static void main(String[] args) {
        getConnection();
    }
}
