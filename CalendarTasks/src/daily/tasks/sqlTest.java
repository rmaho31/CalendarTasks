package daily.tasks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class sqlTest {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public void writeDataBase(String[] a) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/calendar?useSSL=false", "root", "");

            // Sets up the preparedStatement for inputting the values into the DB from the input Array
            preparedStatement = connect
                    .prepareStatement(" insert into newEvents (date, event, description, time, length, location)" + 
                    		 " values (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, a[0]);
            preparedStatement.setString(2, a[1]);
            preparedStatement.setString(3, a[2]);
            preparedStatement.setString(4, a[3]);
            preparedStatement.setString(5, a[4]);
            preparedStatement.setString(6, a[5]);
            preparedStatement.executeUpdate();

           

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }
    
    public ResultSet readDataBase(String d1) throws Exception {
        try {
            // Loads the SQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Setup the connection
            connect = DriverManager.getConnection("jdbc:mysql://localhost/calendar?useSSL=false", "root", "");

            statement = connect.createStatement();
            resultSet = statement.executeQuery("select date, event, description, time, length, location from newEvents where date = " + "'" + d1 +"'");
            return resultSet;         
        } catch (Exception e) {
            throw e;
        }
    }
    
    public void deleteRowDatabase(String[] a) throws Exception{
    	try {
    		//Load MySQL driver
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		//Setup the connection
    		connect = DriverManager.getConnection("jdbc:mysql://localhost/calendar?useSSL=false", "root", "");
    		
    		//Deletes record matching the input array values
    		statement = connect.createStatement();
    		statement.executeUpdate("delete from newEvents where date = " + "'" + a[0] + "' and event = " + "'" + a[1] + "' and time = " + "'" + a[3] + "'");
    		
    	} catch (Exception e) {
    		throw e;
    	} finally {
    		close();
    	}
    }

    
    // Method to close the connections
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

}