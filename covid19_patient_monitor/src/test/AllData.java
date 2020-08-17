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


@Path("/get_info")
public class AllData {
	
	Driver driver = new Driver();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public String getInfo(@QueryParam("a_id") String a_id) {
		
		JSONObject obj = new JSONObject();
		JSONObject pwd = new JSONObject();
	
		try {
			Connection con = driver.connectDb();
			
			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();
			Statement stmt2 = con.createStatement();
			Statement stmt3 = con.createStatement();
			
			
			
			
			
			ResultSet a_rs = stmt.executeQuery("SELECT * FROM USERS WHERE id='"+a_id+"' AND type='admin'");
			
			if(a_rs.next()) {
				
				JSONObject obj1 = new JSONObject();
				
				ResultSet u_rs = stmt1.executeQuery("SELECT * FROM USERS");
				int i=1;
				while(u_rs.next()) {
					
					JSONObject temp = new JSONObject();
					
					temp.put("id",u_rs.getString("id"));
					temp.put("email",u_rs.getString("email"));
					temp.put("name",u_rs.getString("name"));
					temp.put("password",u_rs.getString("password"));
					temp.put("type",u_rs.getString("type"));
					
					pwd.put(u_rs.getString("id"),u_rs.getString("password"));
					obj1.put(i++,temp);
				}
				
				JSONObject obj2 = new JSONObject();
				
				ResultSet p_rs = stmt2.executeQuery("SELECT * FROM PATIENT");
				i=1;
				while(p_rs.next()) {
					
					JSONObject temp = new JSONObject();
					
					String p="";
					
					if(pwd.containsKey(p_rs.getString("patientId"))) {
						p = (String) pwd.get(p_rs.getString("patientId"));
					}
					
					temp.put("patientId",p_rs.getString("patientId"));
					temp.put("name",p_rs.getString("name"));
					temp.put("email",p_rs.getString("email"));
					temp.put("password",p);
					temp.put("phone",p_rs.getString("phone"));
					temp.put("address",p_rs.getString("address"));
					temp.put("sex",p_rs.getString("sex"));
					temp.put("guardianName",p_rs.getString("guardianName"));
					temp.put("age",p_rs.getString("age"));
					temp.put("height",p_rs.getString("height"));
					temp.put("weight",p_rs.getString("weight"));
					temp.put("doctorId",p_rs.getString("doctorId"));

					obj2.put(i++,temp);
				}
				
				JSONObject obj3 = new JSONObject();
				
				ResultSet d_rs = stmt3.executeQuery("SELECT * FROM DOCTOR");
				i=1;
				while(d_rs.next()) {
					
					JSONObject temp = new JSONObject();
					
					String p="";
					
					if(pwd.containsKey(d_rs.getString("doctorId"))) {
						p = (String) pwd.get(d_rs.getString("doctorId"));
					}
					
					temp.put("doctorId",d_rs.getString("doctorId"));
					temp.put("name",d_rs.getString("name"));
					temp.put("password",p);
					temp.put("email",d_rs.getString("email"));
					temp.put("phone",d_rs.getString("phone"));
					temp.put("hospitalName",d_rs.getString("hospitalName"));
					temp.put("hospitalInfo",d_rs.getString("hospitalInfo"));
					
					obj3.put(i++,temp);
				}
				
				obj.put("users",obj1);
				obj.put("patients",obj2);
				obj.put("doctors",obj3);
				obj.put("error", "none");
			}
			else {
				System.out.println(a_id);
				obj.put("error", "Admin Privileges Not Granted");
			}
			con.close();
		}
		catch(Exception e) {
			obj.clear();
			obj.put("error", e.toString()+ " (OR) check Admin Id");
			e.printStackTrace();
		}
		
		return obj.toString();
	}
}
