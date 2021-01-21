package csci310.servlets;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.mockito.Mock;
import org.mockito.Mockito;

import csci310.Stocks.Stocks;
import csci310.loginsignup.Signup;
import csci310.utilities.FinnhubHistory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@PrepareForTest({Signup.class})
@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)

public class CreateAccountServletTest extends Mockito{
	@Mock
	private ServletConfig config = null;
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private RequestDispatcher dispatch = null;
	private CreateAccountServlet cas = null;
	private static String dbUser = "root";
	private static String dbPassword = "";
	
	@Before
	public void createServletVars() {
		cas = new CreateAccountServlet();
		config = mock(ServletConfig.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		dispatch = mock(RequestDispatcher.class);
	}

	@Before 
	public void clearEntry() throws SQLException, ClassNotFoundException {
		Connection connection = null;
		PreparedStatement st = null;
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("DELETE FROM Users WHERE username=?");
		st.setString(1, "memes");
		st.execute();
		
		st.close();
		connection.close();
	}
	
	@Test//(expected = RuntimeException.class)
	public void testDoGet() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("memes");
		when(request.getParameter("password")).thenReturn("dreams");
		when(request.getParameter("confPass")).thenReturn("dreams");
		
		try {
			cas.doGet(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
        verify(request).setAttribute("usernameError", "\tAccount successfully created!");
	}
	
	@Test//(expected = RuntimeException.class)
	public void testDuplicateUser() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("test");
		when(request.getParameter("password")).thenReturn("test");
		when(request.getParameter("confPass")).thenReturn("test");
		
		try {
			cas.doGet(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
        verify(request).setAttribute("usernameError", "\tUsername already taken, please sign up with a new one.");
	}
	
	@Test//(expected = RuntimeException.class)
	public void testUserEntry() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("");
		when(request.getParameter("password")).thenReturn("");
		when(request.getParameter("confPass")).thenReturn("");
		
		try {
			cas.doGet(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
		verify(request).setAttribute("usernameError", "\tPlease fill out the username section.");
	}
	
	@Test//(expected = RuntimeException.class)
	public void testPassEntry() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("sad");
		when(request.getParameter("password")).thenReturn("");
		when(request.getParameter("confPass")).thenReturn("lad");
		
		try {
			cas.doGet(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
		verify(request).setAttribute("passwordError", "\tPlease fill out the password section.");
	}
	
	@Test//(expected = RuntimeException.class)
	public void testConfEntry() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("sad");
		when(request.getParameter("password")).thenReturn("lad");
		when(request.getParameter("confPass")).thenReturn("");
		
		try {
			cas.doGet(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
		verify(request).setAttribute("confpassError", "\tPlease fill out the password confirmation section.");
	}
	
	@Test//(expected = RuntimeException.class)
	public void testLongUser() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeet");
		when(request.getParameter("password")).thenReturn("lad");
		when(request.getParameter("confPass")).thenReturn("lad");
		
		try {
			cas.doGet(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
		verify(request).setAttribute("usernameError", "\tPlease enter a username that's less than 45 characters");
	}
	
	@Test//(expected = RuntimeException.class)
	public void testLongPass() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("yeet");
		when(request.getParameter("password")).thenReturn("yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeet");
		when(request.getParameter("confPass")).thenReturn("yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeet");
		
		try {
			cas.doGet(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
        verify(request).setAttribute("passwordError", "\tPlease enter a password that's less than 45 characters");
	}
	
	@Test//(expected = RuntimeException.class)
	public void testMismatchPass() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("yeet");
		when(request.getParameter("password")).thenReturn("yeeeeeet");
		when(request.getParameter("confPass")).thenReturn("yeeeeeeeeet");
		
		try {
			cas.doGet(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
        verify(request).setAttribute("confpassError", "\tYour passwords do not match.");
	}
	
	@Test
	public void testException() throws ServletException, IOException, ClassNotFoundException, SQLException {
		String username = "yeet";
		String password = "yeeeeeet";
		String confPass = "yessir";
		
		//Case where create account works
		when(request.getParameter("username")).thenReturn(username);
		when(request.getParameter("password")).thenReturn(password);
		when(request.getParameter("confPass")).thenReturn(confPass);
		
		//Get writer to mock the output
		PowerMockito.mockStatic(Signup.class);
	    PowerMockito.when(Signup.CreateAccount(username, password, confPass)).thenThrow(SQLException.class);
	        
	    //Create the servlet instance
		try {
			cas.doGet(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
	}
}
