import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Main extends HttpServlet {
    JSONObject json=new JSONObject();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method=req.getMethod();
        String url=clean(req.getPathInfo());
        auth(req,resp);
        switch (method){
            case "GET":
                doGet(req,resp);
                break;
            case "POST":
                doPost(req,resp,url);
                break;
            default:
                json.put("Error","Method Not Found");
                response(json,405,resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp,String url) throws IOException{
        postm pm=new postm();
        switch (url){
            case "login":
                pm.login(req, resp);
                break;
            case "signup":
                pm.signup(req, resp);
                break;
            case "verify":
                pm.verify(req, resp);
                break;
            case "refresh":
                pm.refresh(req, resp);
                break;
            case "current":
                pm.current(req, resp);
                break;
            default:
                json.put("Error","Page Not Found");
                response(json,404,resp);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    public void auth(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            postm pm=new postm();
            try{
                pm.bearer_auth(token,request,response);
            }catch(Exception e){
                json.put("Error","Unauthorized");
                response(json, 401, response);
            }

        } else {
            json.put("Error","Unauthorized");
            response(json, 401, response);
        }
    }

    public void response(JSONObject js,int code,HttpServletResponse resp) throws IOException {
        int statusCode = (code != 0) ? code : 200;
        header(resp, statusCode);
        resp.getOutputStream().print(js.toString());
    }


    private String clean(String url){
        String pathInfo = url;
        String[] pathParts = pathInfo.split("/");
        return pathParts[1];
    }

    private void header(HttpServletResponse resp,int code) {
        resp.setStatus(code);
        resp.setHeader("Content-Type", "application/json");
    }
}