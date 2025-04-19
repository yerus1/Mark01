package com.connect.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class verify {

    public boolean verify(String username, int otp) throws Exception {
        dbcon d=new dbcon();
        boolean res=false;
        try(Connection con =d.getConnection()){
            PreparedStatement ps=con.prepareStatement("select * from users where username=? AND otp=?");
            ps.setString(1,username);
            ps.setInt(2,otp);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    res=true;
                    empty(username);
                }
            }
        }catch (Exception e){
            throw new Exception("SQL Error ->"+e);
        }
        return res;
    }

    private void empty(String username) throws Exception {
        dbcon d=new dbcon();
        try(Connection con =d.getConnection()){
         PreparedStatement ps= con.prepareStatement("update users SET status=1 where username=?;");
         ps.setString(1,username);
         ps.executeUpdate();
        }catch (Exception e){
            throw new Exception("SQL Error ->"+e);
        }
    }


}
