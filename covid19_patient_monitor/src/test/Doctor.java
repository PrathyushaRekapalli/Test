package test;

import java.sql.*;

import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Application;

@Path("/doctor")

public class Doctor {

    Driver driver = new Driver();
    
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public String getDoctorInfo(@QueryParam("d_id") String d_id) {
    	
    	JSONObject obj = new JSONObject();
    	
    	
    	try {
    		
    		Connection con = driver.connectDb();
    		
    		Statement stmt = con.createStatement();
    		
    		ResultSet doctor_rs = stmt.executeQuery("SELECT * FROM DOCTOR WHERE doctorId ='"+d_id+"'");
    		
    		doctor_rs.next();
    		
    		obj.put("doctorId",doctor_rs.getString("doctorId"));
    		obj.put("name",doctor_rs.getString("name"));
    		obj.put("hospitalName",doctor_rs.getString("hospitalName"));
    		obj.put("phone",doctor_rs.getString("phone"));
    		obj.put("hospitalInfo",doctor_rs.getString("hospitalInfo"));
    		
    		JSONObject p_obj = new JSONObject();
    		
    		ResultSet patient_rs = stmt.executeQuery("SELECT * FROM PATIENT WHERE doctorId ='"+d_id+"'");
    	
    		while(patient_rs.next()) {    			
    			p_obj.put(patient_rs.getString("patientId"),patient_rs.getString("name"));
    		}
    		con.close();
    		obj.put("patients",p_obj);
    	}
    	catch(Exception e) {
    		obj.clear();
    		obj.put("error", e.toString()+" (or) Check Doctor ID");
    		System.out.println(e.toString());
    	}
    	
    	return obj.toString();
    }
    

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String storePatientInfo(JSONObject obj) {
    	
    	JSONObject status = new JSONObject();
    	
    	try {
    		
    		Connection con = driver.connectDb();
    		
    		Statement stmt = con.createStatement();
    		Statement stmt1 = con.createStatement();
    		
    		//String patientId = (String) obj.get("patientId");
            String name = (String) obj.get("name");
            String address = (String) obj.get("address");
            String email = (String) obj.get("email");
            String phone = (String) obj.get("phone");
            String sex = (String) obj.get("sex");
            String guardianName = (String) obj.get("guardianName");
            String age = (String) obj.get("age");
            String height = (String) obj.get("height");
            String weight = (String) obj.get("weight");
            
            String doctorId = (String)obj.get("doctorId");
            
            ResultSet p_rs = stmt.executeQuery("SELECT * FROM PATIENT WHERE email ='"+email+"'");
            ResultSet u_rs = stmt1.executeQuery("SELECT * FROM USERS WHERE email ='"+email+"'");
            
            if(u_rs.next()) {
                if(!p_rs.next()) { 
                	
                	
                	String patientId = u_rs.getString("id");
                	//to ensure patientId is not taken     
    	            String sql_query = "INSERT INTO PATIENT (patientId,name,address,email,phone,sex,guardianName,age,height,weight,doctorId)" + 
    	            		" VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    	           
    	            PreparedStatement preparedStmt = con.prepareStatement(sql_query);
    	            preparedStmt.setString (1, patientId);
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
    	            
    	            
    	            status.put("error","none");
                }
                else {
                	status.put("error","Patient Has been Registered Already");
                }
            }
            else {
            	status.put("error","Patient Has Not Been Signed Up");
            }

            con.close();
    	}
    	catch(Exception e) {
    		status.clear();
    		status.put("error", e.toString()+" (or) Check Patient ID");
    		e.printStackTrace();
    	}
    	
    	return status.toString();
    }
    

}