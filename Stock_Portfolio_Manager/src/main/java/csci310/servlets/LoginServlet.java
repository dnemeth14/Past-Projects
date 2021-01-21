package csci310.servlets;

import csci310.loginsignup.Login;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name="LoginServlet", urlPatterns={"/LoginServlet"})
public class LoginServlet extends HttpServlet {
	
private static final long serialVersionUID = 1L;
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("in login servlet");
		String errmsg = "";

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if (username.trim().length() == 0 || password.trim().length() == 0) 
		{
			errmsg = "Error: Please fill out all fields";
		} 
		else 
		{
			try {
				errmsg = Login.Authenticate(username, password);
				System.out.println("error message: " + errmsg);
				if(errmsg.equalsIgnoreCase("Logged in")) {
					errmsg = "";
				}
				else {
					errmsg = "Error: Username or Password does not exist";
				}
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(!errmsg.trim().contentEquals("")) 
		{
			request.setAttribute("error", errmsg); 
			getServletContext().getRequestDispatcher("/index.jsp").forward(request, response); }
		else 
		{ 
			// Successful
			System.out.println("log in success");
			request.setAttribute("error", errmsg); 
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			response.sendRedirect("/homepage.jsp");					
		}
	}
}