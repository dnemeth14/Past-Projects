package csci310.servlets;

import csci310.Stocks.Stocks;
import csci310.User;
import csci310.utilities.FinnhubGraphing;
import csci310.utilities.FinnhubHistory;
import csci310.utilities.Resolution;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "GraphData")
public class GraphData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Sister we made it");
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
            response.sendError(400, errorMessage);
            return;
        }


        List<String> userStocks;
        try {
            int userId = User.getIdByUsername(username);
            userStocks = Stocks.getUserStocks(Integer.toString(userId));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            response.sendError(500, sqlException.getMessage());
            return;
        }

        List<JSONObject> apiResponses;
        try {
            apiResponses = FinnhubHistory.getHistories(userStocks, Resolution.day, from, to);
        } catch (ParseException e) {
            e.printStackTrace();
            response.sendError(500, e.getMessage());
            return;
        }

        JSONObject responseObject = FinnhubGraphing.responsesToGraph(apiResponses, userStocks, Resolution.month);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseObject);
        out.flush();
    }
}
