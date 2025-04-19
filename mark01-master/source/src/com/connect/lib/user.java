package com.connect.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class user extends dbcon {
    private String user;
    private String email;
    private String userid;
    private String password;
    private int status;


    public user(String user) throws Exception {
        boolean res=false;
        try (Connection con= getConnection()){
            PreparedStatement ps=con.prepareStatement("select * from users where username=?");
            ps.setString(1,user);
            ResultSet rs=ps.executeQuery();
            while(rs.next()) {
                this.user=rs.getString("username");
                this.email=rs.getString("email");
                this.userid=rs.getString("userid");
                this.password=rs.getString("password");
                this.status=rs.getInt("status");
                res=true;
            }
            if(res==false){
                throw new Exception("invalid_user"+getuser());
            }
        }catch (Exception e){
            throw new Exception(e);
        }

    }
    public String getuid() {
        return userid;
    }
    public String getuser() {
        return user;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public int getStatus() {
        return status;
    }


}
