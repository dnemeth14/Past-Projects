package csci310.loginsignup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import csci310.ForEmily;

public class Login {

	private static String dbUser = "root";
	private static String dbPassword = ForEmily.dbPassword;


	private static String accountUsername = "";
	private static String accountPassword = "";
	
	public static String Authenticate(String username, String password) throws SQLException {
		//Ensure that username and password aren't too long
		if(!Signup.validUsername(username)) {
			return("Please enter a username that's less than 45 characters");
		}
		
		if(!Signup.validPassword(password)) {
			return("Please enter a password that's less than 45 characters");
		}
		
		//Connecting to SQL database
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		//Hash the password 
		password = Signup.hash(password);

		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("SELECT username FROM Users WHERE username=? AND hashPass=?");
		st.setString(1, username);
		st.setString(2, password);
		rs = st.executeQuery();
		if(rs.next()) {
			//Login
			connection.close();
			st.close();
			rs.close();
			return("Logged in");
		}
		else {
			connection.close();
			st.close();
			rs.close();
			return("Not in database");
		}
	}
}