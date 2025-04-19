import com.connect.auth.authl;
import com.connect.auth.oauth;
import com.connect.lib.dbcon;
import com.connect.lib.signup;
import com.connect.lib.verify;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.EOFException;
import java.io.IOException;
import java.sql.SQLException;

public class postm extends Main {

    JSONObject json=new JSONObject();
    private authl auth;

    public void signup(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String user = req.getParameter("user");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String pass = req.getParameter("pass");
        if (!user.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !pass.isEmpty()) {
            try {
                signup sp=new signup();
                if (sp.signup(user,email,phone,pass)) {

                    json.put("Status","inserted");
                    response(json, 201, resp);
                } else {
                    json.put("Status","unable to insert");
                    response(json, 500, resp);
                }
            } catch (SQLException | IOException e) {
                json.put("Error",e);
                response(json, 500, resp);
            } catch (Exception e) {
                json.put("Error",e);
                response(json, 500, resp);
            }
        } else {
            json.put("Error","expectation_failed");
            response(json, 417, resp);
        }
    }

    public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String pass = req.getParameter("pass");
        if (!email.isEmpty() && !pass.isEmpty()) {
            try {
                    authl db=new authl(email,pass);
                    json.put("Status","Login success");
                    json.put("Token",db.getToken());
                    response(json, 200, resp);

            } catch (Exception e) {
                json.put("Expectation Failed",e);
                response(json, 406, resp);
            }
        } else {
            json.put("Error","input your input");
            response(json, 417, resp);
        }
    }

    public void verify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username=req.getParameter("username");
        int otp= Integer.parseInt(req.getParameter("otp"));
        try {
            verify vf=new verify();
            if(vf.verify(username,otp)){
                json.put("Status","Verified success");
                response(json, 200, resp);
            }else{
                json.put("Status","invalid input");
                response(json, 500, resp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void refresh(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String token=req.getParameter("token");
        if (!token.isEmpty()) {
            try {
                oauth db=new oauth(token);
                    json.put("Status","refresh success");
                    json.put("Token",db.refreshAccess());
                    response(json, 200, resp);
            } catch (Exception e) {
                json.put("Expectation Failed",e);
                response(json, 406, resp);
            }
        } else {
            json.put("Error","input your input");
            response(json, 417, resp);
        }
    }

    public void current(HttpServletRequest req, HttpServletResponse resp) throws IOException{
                HttpSession session = req.getSession();
                String username = (String) session.getAttribute("username");
                if(username!=null) {
                    json.put("Token", username);
                    response(json, 200, resp);
                }else{
                    json.put("Status","no session");
                    response(json, 406, resp);
                }
    }
    public boolean bearer_auth(String token,HttpServletRequest req, HttpServletResponse resp) throws IOException{
           boolean res=false;
            try{
                this.auth=new authl(token,null);
                HttpSession session = req.getSession();
                session.setAttribute("username", this.auth.getSession_name());
                session.setAttribute("token", this.auth.getSession_token());
                res=true;
            }catch(Exception e){
                json.put("Expectation Failed",e);
                response(json, 406, resp);
                res=false;
            }
        return res;
    }

}
