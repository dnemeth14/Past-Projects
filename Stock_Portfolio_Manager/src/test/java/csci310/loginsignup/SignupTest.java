package csci310.loginsignup;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.Test;

public class SignupTest {

	private static String dbUser = "root";
	private static String dbPassword = "";


	//Testing successful account creation
	@Test
	public void testCreateAccount() throws SQLException, ClassNotFoundException {
		//Empty the database
		
		//Connecting to SQL database
		Connection connection = null;
		PreparedStatement st = null;
		
		try {
			//Using prepared statement to clear the contents of the table
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
			st = connection.prepareStatement("DELETE FROM Users WHERE username=?");
			st.setString(1, "username");
			st.execute();					
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
		Signup signin = new Signup();
		assertEquals(signin.CreateAccount("username", "password", "password"), "New account successfully created!");
	}
	
	@Test
	public void testAlreadyInTable() throws SQLException, ClassNotFoundException {
		//Insert into SQL table
		assertEquals(Signup.CreateAccount("username", "password", "password"), "Username already taken");
	}
	
	@Test
	public void testMismatchedPasswords() throws SQLException, ClassNotFoundException {
		//Insert into SQL table a password combo that doesn't match
		assertEquals(Signup.CreateAccount("username", "password", "password+1"), "Passwords don't match");
	}
	
	@Test
	public void testLongUsername() throws SQLException, ClassNotFoundException {
		//Insert into SQL table a username that's too long
		String username = "owaeifjwaoefijawoeifjwaoeifjawoeifjawoeifjawoeifjaweoifjawoeifjwaoeifjawoeif";
		assertEquals(Signup.CreateAccount(username, "password", "password"), "Please enter a username that's less than 45 characters");
	}
	
	@Test
	public void testLongPassword() throws SQLException, ClassNotFoundException {
		//Insert into SQL table a username that's too long
		String password = "owaeifjwaoefijawoeifjwaoeifjawoeifjawoeifjawoeifjaweoifjawoeifjwaoeifjawoeif";
		assertEquals(Signup.CreateAccount("username", password, password), "Please enter a password that's less than 45 characters");			
	}
	
	@Test
	public void testHash() {
		String password = "password";
		
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
        
        assertEquals(shuffledPassword, Signup.hash(password));
	}
}