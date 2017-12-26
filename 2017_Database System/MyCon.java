
package javatermproject;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class MyCon {
    public static Connection con = null;
    
    public static Connection makeCon(){
        // return the column of the function
        // 1. load and register the sql driver
        
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            JOptionPane.showMessageDialog(null, "Driver is loaded and register");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Driver is not loaded........");
        }
        // 2. Establish a Connection
        String url = "jdbc:sqlserver://HP150801\\SQLEXPRESS:1433;databaseName=Market";
        try{
            con = DriverManager.getConnection(url, "*******", "######");  // Establish connection
            JOptionPane.showMessageDialog(null, "Connection is Established.");
            return con;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Connection is not Established.");
            return null;
        }
    }
}
