package csci310.servlets;


import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import csci310.calcvalue.CalcValue;
import csci310.User;
import csci310.Stocks.Stocks;

/**
 * Servlet implementation class PortfolioSummary
 */
@WebServlet("/PortfolioSummary")
public class PortfolioSummary extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PortfolioSummary() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Brother we made it");
		String userID = request.getParameter("userId");
		String username = request.getParameter("username");
        String from = request.getParameter("from");
        String to = request.getParameter("to");

        String errorMessage = null;
        if (username == null) {
            errorMessage = "username is missing from url param";
        } else if (from == null) {
            errorMessage = "from is missing from url param";
        } else if (to == null) {
            errorMessage = "to is missing from url param";
        }
        if (errorMessage != null) {
            System.out.println(errorMessage);
        }
        
        //Return a list containing the value amount, percent increase, and image location
        if(userID.equals("1")) username = "test";

        List<String> portfolioStats = new ArrayList<String>();
        try {
			portfolioStats = CalcValue.yestPortChange(username);
        } catch (SQLException | ParseException | IOException e){
            e.printStackTrace();
            response.sendError(500, e.getMessage());
            return;
        }
        
        for(int i = 0; i < portfolioStats.size(); i++) System.out.println(portfolioStats.get(i) + " ");
        request.setAttribute("portfolioValue", portfolioStats.get(1));
        request.setAttribute("percentChange", portfolioStats.get(0));
        request.getRequestDispatcher("/homepage.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
