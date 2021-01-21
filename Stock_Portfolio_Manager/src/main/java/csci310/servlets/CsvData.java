package csci310.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ListMultimap;

import csci310.utilities.ParseCsv;


@WebServlet(name = "CsvData")
public class CsvData extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String stock_index = request.getParameter("index");
    	ParseCsv parser = new ParseCsv("sql_code/sample_stock_data.csv");
    	ListMultimap<String, ListMultimap<String, Float>> stock_data = parser.parsecsv();

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
       
        out.print(stock_data.get(stock_index).toString());
        out.flush();
    }    
}
