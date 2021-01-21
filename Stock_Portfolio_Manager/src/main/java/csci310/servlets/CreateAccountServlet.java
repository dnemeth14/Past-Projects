package csci310.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import csci310.loginsignup.Signup;

/**
 * Servlet implementation class LogInServlet
 */
@WebServlet("/CreateAccountServlet")
public class CreateAccountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateAccountServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get data from jsp
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confPass = request.getParameter("confPass");
		
		// Return jsp
		String next = "/CreateAccount.jsp";
		System.out.println("username: " + username + " " + "password: " + password + " " + "confPass: " + confPass);
		
		// If any field is left black return an error
		System.out.println(username.equals(""));
		if(username.equals("") || password.equals("") || confPass.equals("")) {
			if(username.equals("")) {request.setAttribute("usernameError", "\tPlease fill out the username section.");}
			if(password.equals("")) {request.setAttribute("passwordError", "\tPlease fill out the password section.");}
			if(confPass.equals("")) {request.setAttribute("confpassError", "\tPlease fill out the password confirmation section.");}
		}
		else {
			String receipt = "";
			try {
				receipt = Signup.CreateAccount(username, password, confPass);
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			System.out.println(receipt);
			if(receipt.equals("Please enter a username that's less than 45 characters")) {
				request.setAttribute("usernameError", "\tPlease enter a username that's less than 45 characters");
			}
			else if(receipt.equals("Please enter a password that's less than 45 characters")) {
				request.setAttribute("passwordError", "\tPlease enter a password that's less than 45 characters");
			}
			else if(receipt.equals("Passwords don't match")) {
				request.setAttribute("confpassError", "\tYour passwords do not match.");
			}
			else if(receipt.equals("Username already taken")) {
				request.setAttribute("usernameError", "\tUsername already taken, please sign up with a new one.");
			}
			else if(receipt.equals("New account successfully created!")) {
				request.setAttribute("usernameError", "\tAccount successfully created!");
				next = "/index.jsp";
			}
		}
			
		//Send dispatch
		getServletContext().getRequestDispatcher(next).forward(request, response);}
}
