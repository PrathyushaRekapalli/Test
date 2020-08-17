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

@Path("/update_patient")
public class UpdatePatient {
	
	Driver driver = new Driver();
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String updatePatient(JSONObject obj) {
		
    	JSONObject r_obj = new JSONObject();
    	
    	String patientId = (String) obj.get("patientId");
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
			Connection con = driver.connectDb();
			
			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			
		//	ResultSet p_rs = stmt.executeQuery("SELECT * FROM USERS WHERE email ='"+email+"'");
			ResultSet a_rs = stmt1.executeQuery("SELECT * FROM USERS WHERE id ='"+adminId+"' AND "+"type='admin'");
			ResultSet d_rs = stmt2.executeQuery("SELECT * FROM DOCTOR WHERE doctorId ='"+doctorId+"'");
			
			if(a_rs.next()) {
				if(d_rs.next()) {
		    			
			            String sql_query = "UPDATE USERS SET email=?,name=?,password=?,type=? WHERE id='"+patientId+"'";
			           
			            PreparedStatement preparedStmt = con.prepareStatement(sql_query);
			            preparedStmt.setString (1, email);
			            preparedStmt.setString (2, name);
			            preparedStmt.setString (3, password);
			            preparedStmt.setString (4, "patient");
		
			            preparedStmt.executeUpdate();
			            
			            preparedStmt.clearBatch();
			            preparedStmt.clearParameters();
			            
			            sql_query = "UPDATE PATIENT SET name=?,address=?,email=?,phone=?,sex=?,guardianName=?,age=?,height=?,weight=?,doctorId=? WHERE patientId='"+patientId+"'";
			           
			            preparedStmt = con.prepareStatement(sql_query);
			            preparedStmt.setString (1, name);
			            preparedStmt.setString (2, address);
			            preparedStmt.setString (3, email);
			            preparedStmt.setString (4, phone);
			            preparedStmt.setString (5, sex);
			            preparedStmt.setString (6, guardianName);
			            preparedStmt.setString (7, age);
			            preparedStmt.setString (8, height);
			            preparedStmt.setString (9, weight);
			            preparedStmt.setString (10, doctorId);
			            
			            preparedStmt.execute();

			            r_obj.put("error","none");
				}
				else {
					r_obj.put("error","Invalid Doctor Id");
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
