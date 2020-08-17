package test;

import org.json.simple.JSONObject;

import test.Driver;

import java.sql.*;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.UUID;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Application;

@Path("/patient_signup")
public class PatientSignUp {
	
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
    	
    	String email = (String) obj.get("email");
    	String password = (String) obj.get("password");
    	String name = (String) obj.get("name");

    	
    	
    	try {
    		
    		String id = createUniqueId();
    		
    		Connection con = driver.connectDb();
    		
    		Statement stmt = con.createStatement();
    		
    		ResultSet p_rs = stmt.executeQuery("SELECT * FROM USERS WHERE email ='"+email+"'");
    		
    		if(!p_rs.next()) {
    			
	            String sql_query = "INSERT INTO USERS(id,email,name,password,type)" + 
	    	            		" VALUES (?,?,?,?,?)";
	           
	            PreparedStatement preparedStmt = con.prepareStatement(sql_query);
	            preparedStmt.setString (1, id);
	            preparedStmt.setString (2, email);
	            preparedStmt.setString (3, name);
	            preparedStmt.setString (4, password);
	            preparedStmt.setString (5, "patient");

	            preparedStmt.execute();
	            
	            r_obj.put("patientId", id);
	            r_obj.put("error","none");
    		}
    		else {
    			r_obj.put("error","Email  Taken");
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
