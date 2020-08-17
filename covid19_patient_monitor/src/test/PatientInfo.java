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

@Path("/patient_info")
public class PatientInfo {
	
	Driver driver = new Driver();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public String getPatientInfo(@QueryParam("p_id") String p_id) {
    	
    	JSONObject obj = new JSONObject();
    	
    	
    	try {
    		
    		Connection con = driver.connectDb();
    		
    		Statement stmt = con.createStatement();
    		
    		ResultSet doctor_rs = stmt.executeQuery("SELECT * FROM PATIENT WHERE patientId ='"+p_id+"'");
    		
    		if(doctor_rs.next()) {
    		
	    		obj.put("patientId",doctor_rs.getString("patientId"));
	    		obj.put("name",doctor_rs.getString("name"));
	    		obj.put("address",doctor_rs.getString("address"));
	    		obj.put("email",doctor_rs.getString("email"));
	    		obj.put("phone",doctor_rs.getString("phone"));
	    		obj.put("sex",doctor_rs.getString("sex"));
	    		obj.put("guardian",doctor_rs.getString("guardianName"));
	    		obj.put("age",doctor_rs.getString("age"));
	    		obj.put("height",doctor_rs.getString("height"));
	        	obj.put("doctorId",doctor_rs.getString("doctorId"));
	        	
	        	obj.put("error","none");
	    		con.close();
    		}
    		else {
    			obj.put("error","Check Patient ID");
    		}
    		
    	}
    	catch(Exception e) {
    		obj.clear();
    		obj.put("error", e.toString()+" (or) Check Patient ID");
    		System.out.println(e.toString());
    	}
    	
    	return obj.toString();
    }
}
