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
import javax.servlet.http.HttpSession;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.mockito.Mock;
import org.mockito.Mockito;

import csci310.loginsignup.Login;
import csci310.loginsignup.Signup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@PrepareForTest({Login.class})
@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)

public class LoginServletTest extends Mockito{
	@Mock
	private ServletConfig config = null;
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private RequestDispatcher dispatch = null;
	private LoginServlet loginServlet = null;
	private HttpSession httpSession = null;
	
	@Before
	public void createServletVars() {
		loginServlet = new LoginServlet();
		config = mock(ServletConfig.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		dispatch = mock(RequestDispatcher.class);
		httpSession = Mockito.mock(HttpSession.class);
        Mockito.when((request.getSession())).thenReturn(httpSession);
	}

	@Before 
	public void clearEntry() throws SQLException, ClassNotFoundException {
		Connection connection = null;
		PreparedStatement st = null;
		String dbUser = "root";
		String dbPassword = "";
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("DELETE FROM Users WHERE username=?");
		st.setString(1, "memes");
		st.execute();
		
		st.close();
		connection.close();
	}
	
	@Test//(expected = RuntimeException.class)
	public void testWrongPassword() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("trojan");
		when(request.getParameter("password")).thenReturn("123");

		try {
			loginServlet.service(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
		String error_msg = "Error: Username or Password does not exist";
        verify(request).setAttribute("error", error_msg);
	}
	
	@Test//(expected = RuntimeException.class)
	public void testNoUsernameNoPasswordInput() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("");
		when(request.getParameter("password")).thenReturn("");

		try {
			loginServlet.service(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
		String error_msg = "Error: Please fill out all fields";
        verify(request).setAttribute("error", error_msg);
	}
	
	@Test//(expected = RuntimeException.class)
	public void testLogin() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("trojan");
		when(request.getParameter("password")).thenReturn("test");

		try {
			loginServlet.service(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
		String error_msg = "";
		String username = "trojan";
        verify(request).setAttribute("error", error_msg);
        verify(httpSession).setAttribute("username", username);
	}
	
	@Test//(expected = RuntimeException.class)
	public void testNoUsernameInput() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("");
		when(request.getParameter("password")).thenReturn("test");

		try {
			loginServlet.service(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
		String error_msg = "Error: Please fill out all fields";
        verify(request).setAttribute("error", error_msg);
	}
	
	@Test//(expected = RuntimeException.class)
	public void testNoPasswordInput() throws ServletException, IOException {
		//Case where create account works
		when(request.getParameter("username")).thenReturn("123");
		when(request.getParameter("password")).thenReturn("");

		try {
			loginServlet.service(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		
		String error_msg = "Error: Please fill out all fields";
        verify(request).setAttribute("error", error_msg);
	}
	
	@Test
	public void testException() throws ServletException, IOException, ClassNotFoundException, SQLException {
		String username = "test123";
		String password = "123";
		
		//Case where create account works
		when(request.getParameter("username")).thenReturn(username);
		when(request.getParameter("password")).thenReturn(password);
		
		//Get writer to mock the output
		PowerMockito.mockStatic(Login.class);
	    PowerMockito.when(Login.Authenticate(username, password)).thenThrow(SQLException.class);
	        
	    //Create the servlet instance
	    try {
			loginServlet.service(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDispatcher() throws ServletException, IOException, ClassNotFoundException, SQLException {
		String username = "test123";
		String password = "123";
		
		//Case where create account works
		when(request.getParameter("username")).thenReturn(username);
		when(request.getParameter("password")).thenReturn(password);
		
		//Get writer to mock the output
		PowerMockito.mockStatic(Signup.class);
//	    PowerMockito.when(Login.Authenticate(username, password)).thenThrow(SQLException.class);
//	        
	    //Create the servlet instance
	    try {
			loginServlet.service(request, response);
			((RequestDispatcher) verify(request)).forward(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
}