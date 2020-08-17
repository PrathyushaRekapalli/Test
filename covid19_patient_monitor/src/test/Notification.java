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


@Path("/notification")
public class Notification {

	Driver driver = new Driver();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public String getNotification(@QueryParam("d_id") String d_id) {
		
		JSONObject d_obj = new JSONObject();
		
		try {
			Connection con = driver.connectDb();
			
			Statement stmt = con.createStatement();
			
			String query = "SELECT * FROM NOTIFICATION WHERE doctorId='"+d_id+"'";
			
			ResultSet n_rs = stmt.executeQuery(query);
			
			boolean flag = false;
			int index = 1;
			
			while(n_rs.next()) {
				
				flag=true;
				
				JSONObject obj = new JSONObject();
    			
	    		obj.put("patientId",n_rs.getString("patientId"));
	    		obj.put("name",n_rs.getString("name"));
	    		obj.put("message",n_rs.getString("message"));
	    		obj.put("isRead",n_rs.getString("isRead"));
	    		obj.put("time",n_rs.getString("time"));
	    		
	    		d_obj.put(index++,obj);
			}
			
			if(flag) {
				query = "UPDATE NOTIFICATION SET isRead='1' WHERE doctorId='"+d_id+"'";
				stmt.executeUpdate(query);
				d_obj.put("error","none");
			}
			else {
	    		d_obj.clear();
	    		d_obj.put("error", "No Notifications available (or) Check Doctor ID");
			}
		}
		catch(Exception e) {
    		d_obj.clear();
    		d_obj.put("error", e.toString()+" (or) Check Doctor ID");
    		e.printStackTrace();
		}
		
		return d_obj.toString();
	}
}
