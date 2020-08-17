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


@Path("/patient_vitals")

public class Patient {
	
	Driver driver = new Driver();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public String getPatientVitals(@QueryParam("p_id") String p_id) {
    	
    	
    	JSONObject vital_obj = new JSONObject();
    	
    	try {
    		
    		Connection con = driver.connectDb();
    		
    		Statement stmt = con.createStatement();
    		Statement stmt1 = con.createStatement();
    		
    		ResultSet doctor_rs = stmt.executeQuery("SELECT * FROM VITALS WHERE patientId ='"+p_id+"'");
    		ResultSet p_rs = stmt1.executeQuery("SELECT * FROM PATIENT WHERE patientId ='"+p_id+"'");
    		
    		boolean flag = true;
    		int index=1;
    		
    		if(p_rs.next()) {
	    		while(doctor_rs.next()) {
	    			
	    			flag = false;
	    			JSONObject obj = new JSONObject();
	    			
		    		obj.put("patientId",doctor_rs.getString("patientId"));
		    		obj.put("time",doctor_rs.getString("time"));
		        	obj.put("temperature",doctor_rs.getString("temperature"));
		        	obj.put("bloodPressure",doctor_rs.getString("bloodPressure"));
		        	obj.put("respiratoryRate",doctor_rs.getString("respiratoryRate"));
		        	obj.put("heartRate",doctor_rs.getString("heartRate"));
		        	obj.put("spO2",doctor_rs.getString("spO2"));
		        	obj.put("isCritical",doctor_rs.getString("isCritical"));
		        	obj.put("message",doctor_rs.getString("message"));
		        	
		        	vital_obj.put(index++, obj);
	    		}
	    		
	    		if(flag) {
	    			vital_obj.clear();
	    			vital_obj.put("error","NO VITALS TO SHOW (or) Check Patient ID");
	    		}
	    		else {
	    			vital_obj.put("error","none");
	    		}
	        	
    		}
    		else {
    			vital_obj.put("error","Invalid PatientId");
    		}
    		con.close();
    	}
    	catch(Exception e) {
    		vital_obj.clear();
    		vital_obj.put("error", e.toString()+" (or) Check Patient ID");
    		e.printStackTrace();
    	}
    	
    	return vital_obj.toString();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String storeVitals(JSONObject obj) {
    	
    	JSONObject status = new JSONObject();
    	
    	try {
    		Connection con = driver.connectDb();
    		Statement stmt = con.createStatement();
    		String p_id = (String)obj.get("patientId");
    		ResultSet p_rs = stmt.executeQuery("SELECT * FROM PATIENT WHERE patientId ='"+p_id+"'");
            
    		if(p_rs.next()) {
    			
    			obj.put("name", p_rs.getString("name"));
    			obj.put("doctorId", p_rs.getString("doctorId"));
    			
    			Vitals v = new Vitals(obj);  
    			v.store();
    			status.put("error","none");
    		}
    		else {
    			status.put("error","Invalid Patient ID");
    		}
    	}
    	catch(Exception e) {
    		status.clear();
    		status.put("error", e.toString()+" (or) Check Patient ID");
    		e.printStackTrace();
    	}
    	return status.toString();
    }
}
