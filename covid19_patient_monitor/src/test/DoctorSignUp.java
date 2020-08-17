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

@Path("/doctor_signup")
public class DoctorSignUp {
	
	Driver driver = new Driver();
	
	public String createUniqueId() throws Exception{
		
		String id;

		UniqueId uid = new UniqueId();
		
		try {
			Connection con = driver.connectDb();
			
			Statement stmt = con.createStatement();
			

			
			while(true) {
				id = uid.generateId(10);
				ResultSet rs = stmt.executeQuery("SELECT * FROM USERS WHERE id='"+id+"'");
				
				if(!rs.next()) {
					break;
				}
			}
			
			con.close();
		}
		catch(Exception e) {
			throw(e);
		}
		
		return id;
	}
	
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String signUp(JSONObject obj) {
    	
    	JSONObject r_obj = new JSONObject();
    	
    	String adminId = (String) obj.get("adminId");
    	String email = (String) obj.get("email");
    	String password = (String) obj.get("password");
    	String name = (String) obj.get("name");
    	String phone = (String) obj.get("phone");
    	String hospitalInfo = (String) obj.get("hospitalInfo");
    	String hospitalName = (String) obj.get("hospitalName");
    	
    	
    	try {
    		
    		String id = createUniqueId();
    		
    		Connection con = driver.connectDb();
    		
    		Statement stmt = con.createStatement();
    		Statement stmt1 = con.createStatement();
    		
    		ResultSet doctor_rs = stmt.executeQuery("SELECT * FROM DOCTOR WHERE email ='"+email+"' or phone = '"+phone+"'");
    		
    		ResultSet a_rs = stmt1.executeQuery("SELECT * FROM USERS WHERE id ='"+adminId+"' AND "+"type='admin'");
    		
    		if(a_rs.next()) {
				if(!doctor_rs.next()) {
					
		            String sql_query = "INSERT INTO USERS(id,email,name,password,type)" + 
		    	            		" VALUES (?,?,?,?,?)";
		           
		            PreparedStatement preparedStmt = con.prepareStatement(sql_query);
		            preparedStmt.setString (1, id);
		            preparedStmt.setString (2, email);
		            preparedStmt.setString (3, name);
		            preparedStmt.setString (4, password);
		            preparedStmt.setString (5, "doctor");
		
		            preparedStmt.execute();
		            
		            preparedStmt.clearBatch();
		            preparedStmt.clearParameters();
		            
		            sql_query = "INSERT INTO DOCTOR(doctorId,name,hospitalName,email,hospitalInfo,phone)" + 
		            		" VALUES (?,?,?,?,?,?)";	            
		            
		            preparedStmt = con.prepareStatement(sql_query);
		            preparedStmt.setString (1, id);
		            preparedStmt.setString (2, name);
		            preparedStmt.setString (3, hospitalName);
		            preparedStmt.setString (4, email);
		            preparedStmt.setString (5, hospitalInfo);
		            preparedStmt.setString (6, phone);
		            
		            preparedStmt.execute();
		            
		            r_obj.put("doctorId", id);
		            r_obj.put("error","none");
				}
				else {
					r_obj.put("error","Email (or) Phone Number Taken");			
				}
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
   // 	System.out.println(obj.toString());
    	return r_obj.toString();
    }
}