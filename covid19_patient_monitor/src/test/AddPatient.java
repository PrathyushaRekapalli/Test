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

@Path("/add_patient")
public class AddPatient {
	
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
    public String addPatient(JSONObject obj) {
    	
    	JSONObject r_obj = new JSONObject();
    	
    	String adminId = (String) obj.get("adminId");
    	String email = (String) obj.get("email");
    	String password = (String) obj.get("password");
    	String name = (String) obj.get("name");
    	String address = (String) obj.get("address");
    	String phone = (String) obj.get("phone");
    	String sex = (String) obj.get("sex");
    	String guardianName = (String) obj.get("guardianName");
    	String age = (String) obj.get("age");
    	String height = (String) obj.get("height");
    	String weight = (String) obj.get("weight");
    	String doctorId = (String) obj.get("doctorId");
    	
    	try {
    		
    		String id;
    		
    		Connection con = driver.connectDb();
    		
    		Statement stmt = con.createStatement();
    		Statement stmt1 = con.createStatement();
    		Statement stmt2 = con.createStatement();
    		Statement stmt3 = con.createStatement();
    		
    		ResultSet p_rs = stmt.executeQuery("SELECT * FROM PATIENT WHERE email ='"+email+"'");
    		ResultSet a_rs = stmt1.executeQuery("SELECT * FROM USERS WHERE id ='"+adminId+"' AND "+"type='admin'");
    		ResultSet d_rs = stmt2.executeQuery("SELECT * FROM DOCTOR WHERE doctorId ='"+doctorId+"'");
    		ResultSet u_rs = stmt3.executeQuery("SELECT * FROM USERS WHERE email = '"+email+"'");
    		
    		if(a_rs.next()) {
    			if(u_rs.next()) {
	    			if(d_rs.next()) {
			    		if(!p_rs.next()) {
			    			
				            String sql_query = "INSERT INTO USERS(id,email,name,password,type)" + 
				    	            		" VALUES (?,?,?,?,?)";
				           
				            id = u_rs.getString("id"); 
				            PreparedStatement preparedStmt = con.prepareStatement(sql_query);
				            preparedStmt.setString (1, id);
				            preparedStmt.setString (2, email);
				            preparedStmt.setString (3, name);
				            preparedStmt.setString (4, password);
				            preparedStmt.setString (5, "patient");
			
				            preparedStmt.execute();
				            
				            preparedStmt.clearBatch();
				            preparedStmt.clearParameters();
				            
				            sql_query = "INSERT INTO PATIENT (patientId,name,address,email,phone,sex,guardianName,age,height,weight,doctorId)" + 
				            		" VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				           
				            preparedStmt = con.prepareStatement(sql_query);
				            preparedStmt.setString (1, id);
				            preparedStmt.setString (2, name);
				            preparedStmt.setString (3, address);
				            preparedStmt.setString (4, email);
				            preparedStmt.setString (5, phone);
				            preparedStmt.setString (6, sex);
				            preparedStmt.setString (7, guardianName);
				            preparedStmt.setString (8, age);
				            preparedStmt.setString (9, height);
				            preparedStmt.setString (10, weight);
				            preparedStmt.setString (11, doctorId);
				            
				            preparedStmt.execute();
				            
				            r_obj.put("patientId", id);
				            r_obj.put("error","none");
			    		}
			    		else {
			    			r_obj.put("error","Email  Taken");
			    		}
	    			}
	    			else {
	    				r_obj.put("error","Invalid Doctor Id");
	    			}
    			}
    			else {
    				r_obj.put("error","Patient Has Not Been Signed Up");
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
    	
    	return r_obj.toString();
    }
}
