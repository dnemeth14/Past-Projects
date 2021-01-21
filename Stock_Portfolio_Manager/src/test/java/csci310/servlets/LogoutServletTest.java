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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)

public class LogoutServletTest extends Mockito{
	@Mock
	private ServletConfig config = null;
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private RequestDispatcher dispatch = null;
	private LogoutServlet logoutServlet = null;
	private HttpSession httpSession = null;
	
	@Before
	public void createServletVars() {
		logoutServlet = new LogoutServlet();
		config = mock(ServletConfig.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		dispatch = mock(RequestDispatcher.class);
		httpSession = Mockito.mock(HttpSession.class);
        Mockito.when((request.getSession())).thenReturn(httpSession);
	}

	@Test
	public void testLogout() throws ServletException, IOException {
		try {
			logoutServlet.service(request, response);
			((RequestDispatcher) verify(request)).forward(request, response);
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
	}
}