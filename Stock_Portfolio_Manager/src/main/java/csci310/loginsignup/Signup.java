package csci310.loginsignup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import csci310.ForEmily;

public class Signup {
    
	private static String accountUsername = "";
	private static String accountPassword = "";
	private static String confirmPassword = "";

	private static String dbUser = "root";
	private static String dbPassword = ForEmily.dbPassword;

	
	public static boolean validUsername(String username) {
		boolean valid = username.length() < 45 ? true : false;
		return valid;
	}
	
	public static boolean validPassword(String password) {
		boolean valid = password.length() < 45 ? true : false;
		return valid;
	}
	
	public static String hash(String password) {
		//Break the string into a list of chars
		String[] scrambled = password.split("");
        List<String> letters = Arrays.asList(scrambled);
        
        //Shuffle the list with a consistent seed
        Collections.shuffle(letters, new Random(23));
        
        //Concatenate the chars back into a string
        String shuffledPassword = "";
        for (String c : letters) {
            shuffledPassword += c;
        }
        
        //Return the scrambled password
        return shuffledPassword;
    }
	
	public static String CreateAccount(String username, String password, String passwordTwo) throws SQLException, ClassNotFoundException {
		//Ensure that username and password aren't too long
		if(!validUsername(username)) {
			return("Please enter a username that's less than 45 characters");
		}
		
		if(!validPassword(password)) {
			return("Please enter a password that's less than 45 characters");
		}
		
		//Make sure passwords match
		if(!password.equals(passwordTwo)) return "Passwords don't match";
		
		//Connecting to SQL database
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		//Using prepared statement to see if the username password combination returns a user
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("SELECT * FROM Users WHERE username=?");
		st.setString(1, username);
		rs = st.executeQuery();

		if(rs.next()) {
			st.close();
			connection.close();
			rs.close();
			return("Username already taken");
		}
		else {
			//Hash the password
			password = hash(password);
			
			//Register user in the database

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
			st = connection.prepareStatement("INSERT INTO Users (username, hashPass) VALUES (?, ?)");
			st.setString(1, username);
			st.setString(2, password);
			st.execute();
			
			st.close();
			connection.close();
			rs.close();
			
			//Print success
			return("New account successfully created!");
		}		
	}
}
