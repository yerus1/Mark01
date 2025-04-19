package com.connect.lib;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class dbcon {

    private String username;
    private String password;

//    public dbcon() {
//        try (InputStream input = new FileInputStream("./WEB-INF/conf/conf.properties")) {
//            Properties prop = new Properties();
//            prop.load(input);
//            this.url = prop.getProperty("dburl");
//            this.user = prop.getProperty("dbuser");
//            this.pass = prop.getProperty("dbpass");
//            this.username=prop.getProperty("Musername");
//            this.password=prop.getProperty("Mpassword");
//        } catch (IOException ex) {
//             ex.printStackTrace();
//        }
//    }

//    public static void main(String[] args) {
//        dbcon db=new dbcon();
//        System.out.println(url);
//    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/mark01", "postgres", "password");
    }

}
