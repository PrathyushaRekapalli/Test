package test;

import java.sql.*;

import org.json.simple.JSONObject;

import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@Path("/delete_patient")

public class DeletePatient {
	
	Driver driver = new Driver();
    
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public String deletePatient(@QueryParam("p_id") String p_id, @QueryParam("a_id") String a_id) {
		
		JSONObject obj = new JSONObject();
		
		try {
			
			Connection con = driver.connectDb();
			
			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();
			
			ResultSet a_rs = stmt.executeQuery("SELECT * FROM USERS WHERE id='"+a_id+"' AND type='admin'");
			
			if(a_rs.next()) {
				
				ResultSet p_rs = stmt3.executeQuery("SELECT * FROM PATIENT WHERE patientId='"+p_id+"'");
				
				if(p_rs.next()) {
					stmt1.executeUpdate("DELETE FROM PATIENT WHERE patientId='"+p_id+"'");
					stmt2.executeUpdate("DELETE FROM USERS WHERE id='"+p_id+"'");
				
					obj.put("error", "none");
				}
				else {
					obj.put("error", "Invalid Patient Id");
				}
			}
			else {
				obj.put("error","Admin Privileges Not Granted");
			}
			
			con.close();
		}
		catch(Exception e) {
			obj.clear();
			obj.put("error", e.toString()+" (or) Check Patient Id (or) Admin Id");
			e.printStackTrace();
		}
		
		return obj.toString();
	}
	
}
