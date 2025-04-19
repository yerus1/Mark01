package com.connect.auth;

import com.connect.lib.dbcon;
import com.connect.lib.lib;
import com.connect.lib.user;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class oauth extends dbcon {
    private String user=null;
    private String access_token=null;
    private String refresh_token=null;

    public oauth(String token) throws Exception {
        if (token != null) {
            if(token.startsWith("a")){
                this.access_token = token;
            } else if (token.startsWith("r")) {
                this.refresh_token = token;
            }else{
                throw new Exception("Invalid token");
            }
        }
    }

    public oauth(){

    }

    public void setUser(String user) throws Exception {
        user us=new user(user);
        this.user = us.getuser();
    }

    public JSONObject newSession(String token) throws Exception {
        JSONObject jt = new JSONObject();
        lib lb = new lib();
        if(this.user==null){
            throw new Exception("User not set for oauth");
        }
        this.access_token = "a"+lb.getRandomhash(31);    //need to change the logic here
        if(token.equals("auth_grant")){
            this.refresh_token = "r"+lb.getRandomhash(31); //need to change the logic here
        }else {
            this.refresh_token = "d" + lb.getRandomhash(31);   //need to change the logic here
        }
        try (Connection con = getConnection()) {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO session (username, access_token, created_at, valid, refresh_token, valid_for, refer_token) VALUES (?, ?,?,DEFAULT,?, 7200, ?);");
            String refer_token="auth_grant";
            stmt.setString(1, this.user);
            stmt.setString(2, access_token);
            stmt.setLong(3,current_timestamp());
            stmt.setString(4, refresh_token);
            stmt.setString(5,refer_token);
            int i = stmt.executeUpdate();
            if (i > 0) {
                jt.put("Accesstoken", this.access_token);
                jt.put("valid_for", 7200); //2 hours
                jt.put("refreshtoken", this.refresh_token);
                jt.put("refer_token",refer_token);
                jt.put("type", "api");
                return jt;
            } else {
                throw new Exception("session not inserted");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("SQL problem"+e);
        }
    }

    public JSONObject refreshAccess() throws Exception {
        if (this.refresh_token != null && !this.refresh_token.startsWith("d")) {
            dbcon db=new dbcon();
            try (Connection con =db.getConnection()) {
                PreparedStatement ps = con.prepareStatement("select * from session where refresh_token=?");
                ps.setString(1, this.refresh_token);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    setUser(rs.getString("username"));
                    if (rs.getInt("valid") == 0) {
                            return this.newSession(this.refresh_token);
                    } else {
                        throw new Exception("Expired Token");
                    }
                }
            } catch (Exception e) {
                throw new SQLException("refresh sql error"+e);
            }
        } else {
            throw new Exception("invalid request");
        }
        return null;
    }

    public String[] authenticate() throws Exception {
        if (this.access_token != null) {
         try(Connection con =getConnection()){
             PreparedStatement ps = con.prepareStatement("select * from session where access_token=?");
             ps.setString(1, access_token);
             ResultSet rs = ps.executeQuery();
                     if(rs.next()){
                         long expires_at = rs.getLong("created_at") + rs.getInt("valid_for");
                         if (current_timestamp() <= expires_at) {
                             String[] session=new String[2];
                             session[0]=rs.getString("username");
                             session[1]=rs.getString("access_token");
                             return session;
                         }else {
                             throw new Exception("Expired token");
                         }
                     }else {
                         throw new Exception("invalid token");
                     }
         }catch (SQLException e){
             throw new Exception("SQL "+e);
         }
        }
        return null;
    }


//    Local TimeZone
    public long current_timestamp(){
        ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        String formattedTimestamp = currentDateTime.format(formatter);
        LocalDateTime parsedDateTime = LocalDateTime.parse(formattedTimestamp, formatter);
        long unixTimestamp = parsedDateTime.atZone(java.time.ZoneId.systemDefault()).toEpochSecond();
        return unixTimestamp;
    }

}
