package test;

import org.json.simple.JSONObject;

import java.sql.*;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Application;

@Path("/login")
public class Login {
	
	Driver driver = new Driver();
	
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(JSONObject obj) {
    	
    	JSONObject r_obj = new JSONObject();
    	
    	String email = (String) obj.get("email");
    	String password = (String) obj.get("password");
    	
    	try {
    		
    		Connection con = driver.connectDb();
    		
    		Statement stmt = con.createStatement();
    		
    		ResultSet doctor_rs = stmt.executeQuery("SELECT * FROM USERS WHERE email ='"+email+"'");
    		
    		if(doctor_rs.next()) {
    			
    			if(doctor_rs.getString("password").equals(password)) {
    				
    				r_obj.put("id",doctor_rs.getString("id"));
    				r_obj.put("type",doctor_rs.getString("type"));
    				r_obj.put("error","none");
    			}
    			else {
    				r_obj.put("error","Invalid Password");
    			}
    		}
    		else {
    			r_obj.put("error","Check Email");
    		}
    		con.close();
    	}
    	catch(Exception e) {
    		r_obj.clear();
    		r_obj.put("error",e.toString()+" (or) Check Email or Password");
    		e.printStackTrace();
    	}
    	
    	return r_obj.toString();
    }
}
