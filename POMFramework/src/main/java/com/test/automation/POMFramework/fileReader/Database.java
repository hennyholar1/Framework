package com.test.automation.POMFramework.fileReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.annotations.Test;

public class Database {
	public Connection connect;
	public Statement statement;

	public Statement getStatement() throws ClassNotFoundException, SQLException {
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String connection = "jdbc:mysql://localhost:3306/access?useSSL=false";
			String userName = "root";
			String password = "1Awolowo..";
			Class.forName(driver);
			connect = DriverManager.getConnection(connection, userName, password);
			statement = connect.createStatement();
			return statement;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return statement;
	}
	
	
	public void insertData(String query) {
		try {	
			getStatement().executeUpdate(query);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}


	public void updateData(String query) {
		try {
			getStatement().executeUpdate(query);
			
			
			
		} catch (Exception e) {	
			e.printStackTrace();	
		}	
	}
	
	
	public ResultSet getData(String query) {
			try {
				ResultSet data = getStatement().executeQuery(query);
				
				return data;
				
			} catch (Exception e) {	
				e.printStackTrace();
			}
			return null;	
			}	
	
	@Test
	public void retrieveData() throws ClassNotFoundException, SQLException {
		Database db = new Database();
		String query = "select * from access.login";	// Create an SQL query to manipulate data by calling the applicable method to update the database/table 
		String sqlQuery = "update login set Password = 'egin-lomo' where Password ='eginlomo'";
		
		db.updateData(sqlQuery);
		
		ResultSet data = getStatement().executeQuery(query);
		System.out.println("Database table record contains:   ");
		
		while (data.next()) {
		System.out.println( " ====> " + data.getString("UserName") + ", " + data.getString("Password") + ", " + data.getString("RunMode"));	
		}
	}	
}
	

