package com.connect.auth;

import com.connect.lib.user;
import org.json.JSONObject;

public class authl {
    private String password;
    private String username;
    private boolean istoken=false;
    private JSONObject logintoken;
    private String token;
    private String session_name;
    private String session_token;

    public authl(String username, String password) throws Exception {
        if (password == null) {
            this.token = username;
            istoken = true;
        } else {
            this.username = username;
            this.password = password;
        }
        if (this.istoken) {
            oauth ah=new oauth(token);
            String[] session=ah.authenticate();
            if(session!=null){
                setSession_name(session[0]);
                setSession_token(session[1]);
            }
        } else {
            user us = new user(username);
            this.username=us.getuser();
            if (us.getuser().equals(username) && us.getPassword().equals(password)) {
                    if (us.getStatus() == 1) {
                        this.logintoken=this.addsession();
                    } else {
                        throw new Exception("Activate your account!");
                    }
                } else {
                throw new Exception("invalid_input");
                }
            }
        }
    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }
        public JSONObject getToken(){
            return this.logintoken;
        }
        public JSONObject addsession() throws Exception {
            oauth at=new oauth();
            at.setUser(this.username);
            return at.newSession("auth_grant");
        }
    }

