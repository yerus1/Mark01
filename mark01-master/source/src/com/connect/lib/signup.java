package com.connect.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class signup {
    public boolean signup(String user, String email, String phone, String pass) throws Exception {
        Random rand = new Random();
        dbcon d=new dbcon();
        lib lb=new lib();
        int i=0;
        try (Connection con = d.getConnection()) {
            PreparedStatement ps=con.prepareStatement("insert into users(userid,username,email,phone,password,status,otp) values(?,?,?,?,?,DEFAULT,?);");
            int otp = 100000 + rand.nextInt((999999 - 100000) + 1);
            ps.setString(1,lb.getRandomhash(8));
            ps.setString(2,user);
            ps.setString(3,email);
            ps.setString(4,phone);
            ps.setString(5,pass);
            ps.setInt(6,otp);
            lb.sendMail(email,otp);
            i=ps.executeUpdate();
        }catch (SQLException e) {
            throw new Exception("SQL problem"+e);
        }
        if(i>0){
            return true;
        }else{
            return false;
        }
    }

}
