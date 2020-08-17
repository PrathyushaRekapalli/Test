package test;

import java.sql.*;

public class Driver {
    
    final String userId = "root";
    final String password = "";
    final String dbName = "covid19_patient_monitor";
    
    public Connection connectDb() throws Exception{
        
               
        try{
            //connect to database
        	Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/"+dbName,userId,password);
            
            return con;

        }
        catch(Exception e){
            throw(e);
        } 
             
    }
    
}