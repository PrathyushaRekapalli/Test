package test;

import java.sql.*;

import org.json.simple.JSONObject;
import java.sql.Timestamp;  
import java.time.Instant; 

public class Vitals {
    
    Double temperature,bloodPressure,respiratoryRate,heartRate,spO2;
    String patientId,name,doctorId;
    Driver driver;
    
    
    public Vitals(Vitals v){
        this.temperature = v.temperature;
        this.bloodPressure = v.bloodPressure;
        this.respiratoryRate = v.respiratoryRate;
        this.heartRate = v.heartRate;
        this.spO2 = v.spO2;
        driver = new Driver();
    }
    
    public Vitals(JSONObject obj) {
        this.temperature = Double.parseDouble((String)obj.get("temperature"));
        this.bloodPressure = Double.parseDouble((String)obj.get("bloodPressure"));
        this.respiratoryRate = Double.parseDouble((String)obj.get("respiratoryRate"));
        this.heartRate =  Double.parseDouble((String)obj.get("heartRate"));
        this.spO2 = Double.parseDouble((String)obj.get("spO2"));
        this.patientId = (String) obj.get("patientId");
        this.name = (String) obj.get("name");
        this.doctorId = (String) obj.get("doctorId");
        this.driver = new Driver();
    }
    
    public void store() throws Exception{
    	
    	
    	String c = critical();
    	int isCritical=0;
    	String message = "";
    	
    	if(!c.equals("")) {
    		isCritical=1;
    		message = c;
    	}
    	
    	Timestamp instant= Timestamp.from(Instant.now()); 

    	try {
    		Connection con = driver.connectDb();
    		
    		
    		if(isCritical==1) {
        		
    			String notif_query = "INSERT INTO NOTIFICATION (patientId,name,doctorId,message,isRead,time)"+
						 " VALUES (?,?,?,?,?,?)";
        		
    			PreparedStatement notif_ps = con.prepareStatement(notif_query);
        		
        		notif_ps.setString (1, patientId);
        		notif_ps.setString (2, name);
        		notif_ps.setString (3, doctorId);
        		notif_ps.setString (4, message);
        		notif_ps.setInt(5, 0);
        		notif_ps.setString (6, instant.toString());
        		
        		notif_ps.execute();
    		}
    		
    		String sql_query = "INSERT INTO VITALS (patientId,name,time,temperature,bloodPressure,respiratoryRate,heartRate,spO2,isCritical,message)" + 
            		" VALUES (?,?,?,?,?,?,?,?,?,?)";
    		PreparedStatement preparedStmt = con.prepareStatement(sql_query);
    		
    		preparedStmt.setString (1, patientId);
    		preparedStmt.setString (2, name);
    		preparedStmt.setString (3, instant.toString());
    		preparedStmt.setDouble(4, temperature);
    		preparedStmt.setDouble(5, bloodPressure);
    		preparedStmt.setDouble (6, respiratoryRate);
    		preparedStmt.setDouble (7, heartRate);
    		preparedStmt.setDouble (8, spO2);
    		preparedStmt.setInt(9, isCritical);
    		preparedStmt.setString (10, message);
    		
    		preparedStmt.execute();
    		con.close();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		throw(e);
    	}
    }
    
    String toJSON() {
    	JSONObject obj = new JSONObject();
    	
    	obj.put("patientId",patientId);
    	obj.put("temperature",temperature);
    	obj.put("bloodPressure",bloodPressure);
    	obj.put("respiratoryRate",respiratoryRate);
    	obj.put("heartRate",heartRate);
    	obj.put("spO2",spO2);
    	
    	return obj.toString();
    }
    
    String critical(){
        //critical message = "" when vitals are normal
        String criticalMessage="";
        
        if(this.temperature < 96.98 || this.temperature > 100.22){
            criticalMessage += "Abnormal Temperature: "+this.temperature+" , ";
        }
        if(this.bloodPressure < 60 || this.bloodPressure > 160){
            criticalMessage += "Abnormal Blood Pressure: "+this.bloodPressure+" , ";
        }
        if(this.respiratoryRate < 9 || this.respiratoryRate > 20){
            criticalMessage += "Abnormal Respiratory Rate: "+this.respiratoryRate+" , ";
        }
        if(this.heartRate < 50 || this.heartRate > 100){
            criticalMessage += "Abnormal Heart Rate: "+this.heartRate+" , ";
        }
        if(this.spO2 < 93){
            criticalMessage += "Abnormal SpO2: "+this.spO2;
        }
        
        return criticalMessage;
    }
}
