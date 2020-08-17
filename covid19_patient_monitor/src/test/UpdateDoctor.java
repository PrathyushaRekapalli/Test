package test;

import org.json.simple.JSONObject;

import java.sql.*;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.UUID;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Application;

@Path("/update_doctor")
public class UpdateDoctor {
	
	Driver driver = new Driver();
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String signUp(JSONObject obj) {
    	
    	JSONObject r_obj = new JSONObject();
    	
    	String doctorId = (String) obj.get("doctorId");
    	String adminId = (String) obj.get("adminId");
    	String email = (String) obj.get("email");
    	String password = (String) obj.get("password");
    	String name = (String) obj.get("name");
    	String phone = (String) obj.get("phone");
    	String hospitalInfo = (String) obj.get("hospitalInfo");
    	String hospitalName = (String) obj.get("hospitalName");
    	
    	
    	try {
    		
    		
    		Connection con = driver.connectDb();
    		
    		Statement stmt = con.createStatement();
    		Statement stmt1 = con.createStatement();
    		
    	//	ResultSet doctor_rs = stmt.executeQuery("SELECT * FROM DOCTOR WHERE email ='"+email+"' or phone = '"+phone+"'");
    		
    		ResultSet a_rs = stmt1.executeQuery("SELECT * FROM USERS WHERE id ='"+adminId+"' AND "+"type='admin'");
    		
    		if(a_rs.next()) {
					
		            String sql_query = "UPDATE USERS SET email=?,name=?,password=?,type=? WHERE id='"+doctorId+"'";
		           
		            PreparedStatement preparedStmt = con.prepareStatement(sql_query);
		            
		            preparedStmt.setString (1, email);
		            preparedStmt.setString (2, name);
		            preparedStmt.setString (3, password);
		            preparedStmt.setString (4, "doctor");
		
		            preparedStmt.execute();
		            
		            preparedStmt.clearBatch();
		            preparedStmt.clearParameters();
		            
		            sql_query = "UPDATE DOCTOR SET name=?,hospitalName=?,email=?,hospitalInfo=?,phone=? WHERE doctorId='"+doctorId+"'";
		            
		            preparedStmt = con.prepareStatement(sql_query);
		            
		            preparedStmt.setString (1, name);
		            preparedStmt.setString (2, hospitalName);
		            preparedStmt.setString (3, email);
		            preparedStmt.setString (4, hospitalInfo);
		            preparedStmt.setString (5, phone);
		            
		            preparedStmt.execute();
		            
		            r_obj.put("error","none");

    		}
    		else {
    			r_obj.put("error","Admin Privileges Not Granted");
    		}
    		con.close();
    	}
    	catch(Exception e) {
    		r_obj.clear();
    		r_obj.put("error",e.toString()+" (or) Check Details");
    		e.printStackTrace();
    	}
    	
    	return r_obj.toString();
    }
}
