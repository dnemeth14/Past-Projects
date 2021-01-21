package csci310;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.TimeZone;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import csci310.calcvalue.CalcValue;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class Validate {

	
	/*
	 * String must be in form yyyy-mm-dd
	 */
	public Date stringToDate(String date)
	{
		//hello
		return Date.valueOf(date);
	}
	
	
	public String getCurrentDate() {
		
		TimeZone zone = TimeZone.getTimeZone("EST");
		
		Calendar time = Calendar.getInstance(zone);
		String sDate = "" + time.get(Calendar.YEAR) + "-" + time.get(Calendar.MONTH) + "-" + time.get(Calendar.DAY_OF_MONTH); 
		
		return sDate;
	}
	
	public boolean notFutureDate(Date testDate) {
		
		return !testDate.after(stringToDate(getCurrentDate()));
	}
	
	//buyDate before sell date
	//not sure about equals yet
	public boolean compareTwoDates(Date buyDate, Date sellDate) {
		
		return buyDate.before(sellDate);
	}
	
	//stock exists on a date
	public boolean validIndexOnDate(String index, Date date) throws IOException, ParseException {
		
		long start_time = date.getTime() / 1000;  //convert milliseconds to seconds
		
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder()
				.url("https://finnhub.io/api/v1/stock/candle?token=btjq1kf48v6vivbngtk0&"
						+"symbol="+index
						+"&resolution="+"D"
						+"&from="+start_time
						+"&to="+start_time+1)
				.method("GET", null)
				.addHeader("Cookie", "__cfduid=d0a804546b94383d32aff9c130ca89da51600715190")
				.build();
		Response response = null;

		response = client.newCall(request).execute();

		JSONParser parser = new JSONParser();
		Object obj;

		obj = parser.parse(response.body().string());

        JSONObject stockData = (JSONObject)obj;
        
        String comp = stockData.get("s").toString();
        
        if(comp.equals("no_data")) {
        	return false;
        }
		return true;
	}
	
	//iffy if this is needed
	//make sure not negative
	// can a user buy this many shares, take current shares + desired shares and make sure less than total, or not
	public boolean validBuyShares(int shares)
	{
		if(shares <= 0) {
			return false;
		}
		return true;
	}
	
	//maybe combine this with buy/sell shares to avoid double operations
	//make sure not negative
	//or call this and have it call buy/sell
	//based on how many shares a user currently has, check if they can sell this many
	public boolean validSellSharesOnDate(String index, Date date, int shares, int userID) throws SQLException {
		
		if(shares <= 0) {
			return false;
		}
		
		Hashtable<String, Integer> portfolio = new Hashtable<String, Integer>();
		

		CalcValue.getBuySharesOnDate(userID, date, portfolio);
		CalcValue.getSellSharesOnDate(userID, date, portfolio);

		if(!portfolio.containsKey(index)){
			return false;
		}
		if(portfolio.get(index) < shares) {
			return false;
		}
		
		return true;
	}
	
	
	/*
	 * Still assumming that userID is valid
	 * Can also return an error specific to each case
	 */
	public boolean ovrValidBuyOnly(String index, Date date, int shares, int userID) throws IOException, ParseException
	{
		// test dates first
		if(!(notFutureDate(date) && validIndexOnDate(index, date))) {
			return false;
		}
		
		//test if number of shares is valid
		if(!validBuyShares(shares)) {
			return false;
		}
		
		return true;
	}
	
	public boolean ovrValidSellOnly(String index, Date date, int shares, int userID) throws SQLException, IOException, ParseException {
		//call a bunch of other functions here
		
		if(!notFutureDate(date)) {
			return false;
		}
		
		if(!validIndexOnDate(index, date)){
			return false;
		}
		if(!(validSellSharesOnDate(index, date, shares, userID))) {
			return false;
		}
		
		return true;
	}
	
	public boolean ovrValidBuySell(String index, Date buyDate, Date sellDate, int buyShares, int sellShares, int userID) throws SQLException, IOException, ParseException
	{
		//compare the dates
		if(!compareTwoDates(buyDate, sellDate)) {
			return false;
		}
		
		//check if just the buy is ok
		if(!ovrValidBuyOnly(index, buyDate, buyShares, userID)) {
			return false;
		}
		
		
		// check sell date is not future
		if(!notFutureDate(sellDate)) {
			return false;
		}
		
		// cannot sell <= 0 shares
		if(sellShares <= 0) {
			return false;
		}
		
		//get shares at sellDate
		Hashtable<String, Integer> portfolio = new Hashtable<String, Integer>();
		
		CalcValue.getBuySharesOnDate(userID, sellDate, portfolio);
		CalcValue.getSellSharesOnDate(userID, sellDate, portfolio);

		
		//get shares of index not from this trade
		int sharesAlreadyThere = 0;
		if(portfolio.containsKey(index)){
			sharesAlreadyThere = portfolio.get(index);
		}
		
		//check if you are trying to sell too much
		int sharesAvailableToSell = sharesAlreadyThere + buyShares;
		if(sharesAvailableToSell < sellShares) {
			return false;
		}
		else {
			return true;
		}
	}
}
