package csci310.loginsignup;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.junit.Test;

import csci310.loginsignup.Login;
import csci310.loginsignup.Signup;

public class LoginTest {

	private static String dbUser = "root";
	private static String dbPassword = "";

	@Test
	public void testSuccessfulLogin() throws ClassNotFoundException {
		//Create an account and then try logging into it
		
		//Connecting to SQL database
		Connection connection = null;
		
		try {
			//Using prepared statement to clear the contents of the table
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);

			
			//Now insert a username password combo
			
			//Insert into SQL table
			Signup.CreateAccount("username", "password", "password");
					
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//Try logging in with those credentials
		try {
			Login log = new Login();
			assertEquals(log.Authenticate("username", "password"), "Logged in");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUnsuccessfulLogin() {
		//Get the 1st username and password in the table
		//Account info
		String username = "";
		String password = ""; 
		
		//Connecting to SQL database
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			//Using prepared statement to clear the contents of the table
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
			st = connection.prepareStatement("SELECT * FROM Users");
			rs = st.executeQuery();
			
			//Now insert a username password combo
			//Get username and password
			if(rs.next()) {
				username = rs.getString(2);
				password = rs.getString(3);
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				st.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//Insert into SQL table
		try {
			assertEquals(Login.Authenticate(username, password), "Not in database");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLongUsername() {
		//Insert into SQL table a username that's too long
		String username = "owaeifjwaoefijawoeifjwaoeifjawoeifjawoeifjawoeifjaweoifjawoeifjwaoeifjawoeif";
		try {
			assertEquals(Login.Authenticate(username, "password"), "Please enter a username that's less than 45 characters");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLongPassword() {
		//Insert into SQL table a username that's too long
		String password = "owaeifjwaoefijawoeifjwaoeifjawoeifjawoeifjawoeifjaweoifjawoeifjwaoeifjawoeif";
		try {
			assertEquals(Login.Authenticate("username", password), "Please enter a password that's less than 45 characters");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}