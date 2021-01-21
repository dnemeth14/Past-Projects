package csci310.calcvalue;


import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import csci310.ForEmily;
import csci310.utilities.FinnhubHistory;
import csci310.utilities.Resolution;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalcValue {
	
	private static String today = Long.toString(System.currentTimeMillis() / 1000L);
	private static String threeDaysAgo = Long.toString(Long.parseLong(today) - (3*86400));
	private static int userID = 0;
	private static double portfolioValue = 0;
	private static Hashtable<String, Integer> portfolio = new Hashtable<String, Integer>();
	//private static DecimalFormat money = new DecimalFormat("#.##");

	private static String dbUser = "root";
	private static String dbPassword = ForEmily.dbPassword;

	
	/**public static void main(String[] args) {
		try {
			List<Double> fforrespects = portfolioValOverTime("test", "2020-09-01 09:00:00", "2020-09-23 09:00:00");
			for(int i = 0; i < fforrespects.size(); i++) {
				System.out.println(fforrespects.get(i));
			}
			//System.out.println(isValidDate("202222"));
		} catch (NumberFormatException | SQLException | java.text.ParseException | ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}**/
	
	/**
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ParseException **/
	public static double getPortAndCalcVal(String username) throws SQLException, ParseException, IOException {
		Hashtable<String, Integer> portfolio2 = new Hashtable<String, Integer>();
		
		getUserID(username);
		getBuyShares(userID, portfolio2);
		getSellShares(userID, portfolio2);
		String fiveDaysAgo = Long.toString(Long.parseLong(today) - (5*86400));
		portfolioValue = calcPortfolioVal(portfolio2, fiveDaysAgo, today);
		return portfolioValue;
	}
	
	/**public static boolean isValidDate(String start_date) {
		//Make sure start date format is YYYY-MM-DD
		String format = "YYYY-MM-DD";
		Locale locale = Locale.ENGLISH;
	    DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);
	    LocalDateTime ldt = LocalDateTime.parse(start_date, fomatter);
        String result = ldt.format(fomatter);
        return result.equals(start_date);
	}
	 * @throws java.text.ParseException 
	 * @throws IOException **/
	
	public static List<Double> portfolioValOverTime(String username, String start_date, String end_date) throws SQLException, java.text.ParseException, ParseException, IOException {
		//Create list of doubles to store portfolio value
		List<Double> portfolioValue = new ArrayList<Double>();
		
		//Get user id from username
		int userID = getUserID(username);
		
		//Get a list of shares of each stock for each day from the start date to the current day
		//Get a list of portfolios, every time a stock is added or sold make a new portfolio
		TreeMap<Date, List<Hashtable<String, Integer>>> longTermPortfolio = new TreeMap<Date, List<Hashtable<String, Integer>>>();
		getBuySharesOverTime(userID, longTermPortfolio);
		getSellSharesOverTime(userID, longTermPortfolio);
		
		//Iterate from starting date until today
		//Convert start date to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = sdf.parse(start_date);
		long millis = date.getTime();
		
		//Get a list of all the closing prices for each stock
		Hashtable<String, JSONArray> stockData = new Hashtable<String, JSONArray>();
		Hashtable<String, Integer> portfolioNoDate = new Hashtable<String, Integer>();
		getBuyShares(userID, portfolioNoDate);
		Set<Entry<String, Integer>> entries = portfolioNoDate.entrySet();
		 
		//iterate using the for loop
		//Parse the json object and get the opening stock price
		for(Entry<String, Integer> entry : entries ){
			JSONObject stockPrice = FinnhubHistory.getHistory(entry.getKey(), Resolution.day, Long.toString(millis/1000L), today);
			JSONArray closingPrices = (JSONArray) stockPrice.get("c");
			stockData.put(entry.getKey(), closingPrices);
		}
		
		//Get rid of all transactions that occur outside of the time range
		java.util.Date endDate = sdf.parse(end_date);
		long endDateMillis = date.getTime();
		
		 
		//iterate using the for loop
		//Parse the json object and get the opening stock price
		while(sdf.parse(longTermPortfolio.firstKey() + " 09:00:00").getTime() < (millis - 3600000)) {
			longTermPortfolio.pollFirstEntry();
		}
		
		Set<Entry<Date, List<Hashtable<String, Integer>>>> entriess = longTermPortfolio.entrySet();
		
		for(Entry<Date, List<Hashtable<String, Integer>>> entry : entriess){
		    System.out.println(entry.getKey() + " => " + entry.getValue());
		}
		
		//Instantiate portfolio to show given time's portfolio
		Hashtable<String, Integer> currentPortfolio = new Hashtable<String, Integer>();
		
		int counter = 0, dayOfWeek = 0, numSuccesses = 0;
		for(long time = millis; time < sdf.parse(end_date).getTime(); time += 86400000) {
			if(longTermPortfolio.size() > 0) { 
				//Check to see if any transaction was made
				String firstDate = longTermPortfolio.firstKey() + " 09:00:00";
				date = sdf.parse(firstDate);
				long firstDateTime = date.getTime();
				
				//System.out.println("firstDateTime: " + Long.toString(firstDateTime) + " time: " + Long.toString(time));
				if(Math.abs(firstDateTime - time) <= 3600000) {
					//Account for daylight savings time
					//Check if the date of the next portfolio crosses daylight time
					time = firstDateTime;
					++numSuccesses;
					
					//Add the transaction to current portfolio and remove it from long term portfolio
					List<Hashtable<String, Integer>> purchasesThatDay = longTermPortfolio.firstEntry().getValue();
					for(int i = 0; i < purchasesThatDay.size(); i++) {
						Set<Entry<String, Integer>> entrySet = purchasesThatDay.get(i).entrySet();
						for(Entry<String, Integer> entry: entrySet) {
							String ticker = entry.getKey();
							int shares = entry.getValue();
							
							if(shares > 0) currentPortfolio.put(ticker, shares);
							else {
								if(currentPortfolio.get(ticker) == (-1*shares)) currentPortfolio.remove(ticker);
								else currentPortfolio.put(ticker, currentPortfolio.get(ticker) + shares);
							}
						}
					}
					
					longTermPortfolio.pollFirstEntry();
				}
			}
			//Add that day's portfolio value to list
			Set<Entry<String, Integer>> entriesTwo = currentPortfolio.entrySet();
			 
			//iterate using the for loop
			//Calculate the portfolio value for each day
		    double dailyPortfolioVal = 0;
		    //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
			for(Entry<String, Integer> entry : entriesTwo){            
				Object stockPriceValue = stockData.get(entry.getKey()).get(counter);
				if(stockPriceValue instanceof Double) dailyPortfolioVal += entry.getValue()*(Double) stockPriceValue;
				if(stockPriceValue instanceof Long) dailyPortfolioVal += entry.getValue()*(Long) stockPriceValue;
				//System.out.println(entry.getKey() + " => " + entry.getValue());
			}

			//Round value to the nearest hundreth
			dailyPortfolioVal *= 100;
			dailyPortfolioVal = Math.round(dailyPortfolioVal);
			dailyPortfolioVal /= 100;
			
			System.out.println(dailyPortfolioVal);
			portfolioValue.add(dailyPortfolioVal);
			
			//Increment counter which is used to get closing price
			if(((dayOfWeek % 7 == 0) || (dayOfWeek % 7 == 1) || (dayOfWeek % 7 == 2)) == false){
				//Don't increment counter on weekends
				++counter;
			}
			++dayOfWeek;
		}
		
		return portfolioValue;
	}
	
	public static double calcPortfolioVal(Hashtable<String, Integer> portfolio, String start_date, String end_date) throws ParseException, IOException {
		//Value to increment portfolio value by
		double value = 0;
		
		//Iterate through hashtable
		Set<Entry<String, Integer>> entries = portfolio.entrySet();
		 
		//iterate using the for loop
		for(Entry<String, Integer> entry : entries ){            
		    System.out.println(entry.getKey() + " => " + entry.getValue());
		    
		    //Call function that gets stock price
			JSONObject stockPrice = FinnhubHistory.getHistory(entry.getKey(), Resolution.day, start_date, end_date);
			//System.out.println(stockPrice.toJSONString());

			//Parse the json object and get the opening stock price
			JSONArray closingPrices = (JSONArray) stockPrice.get("c");
			value += entry.getValue() * (Double) closingPrices.get(closingPrices.size() - 1);
		}
		
		return value;
	}
	
	public static int getUserID(String username) throws SQLException {
		/**
		//Get username
		System.out.print("Enter the name of the person whose portfolio you want the value of: ");	
		Scanner scan = new Scanner(System.in);
		String username = scan.nextLine();
		scan.close(); **/
		
		//Find corresponding user id for that username
		//Connect to database
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("SELECT userID FROM Users WHERE username=?");
		st.setString(1, username);
		rs = st.executeQuery();
		if(rs.next()) {
			//If user exists, get the corresponding id
			userID = rs.getInt(1);
		}
		else {
			//If user doesn't exist, set an error flag
			userID = -1;
		}

		//Close the connection
		connection.close();
		st.close();
		rs.close();
		
		return userID;
	}
	
	public static void getBuyShares(int userID, Hashtable<String, Integer> portfolio) throws SQLException {
		//Connect to database
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("SELECT * FROM UserToBuys WHERE userID=?");
		st.setInt(1, userID);
		rs = st.executeQuery();
		
		//Array list used to store buy ids for a user
		List<Integer> buyIDs = new ArrayList<Integer>();
		
		while(rs.next()) {
			//Add all the buy ids for that user into an array list
			buyIDs.add(rs.getInt(1));
		}
		
		//Use the buy id list to fill out a dictionary with stock ticker as key and number of shares as value
		//For each buy id, get the corresponding stock and shares
		for(int i = 0; i < buyIDs.size(); i++) {
			st = connection.prepareStatement("SELECT * FROM Buys WHERE buyID=?");
			st.setInt(1, buyIDs.get(i));
			rs = st.executeQuery();
			rs.next();
			
			String ticker = rs.getString(2);
			int shares = rs.getInt(3);

			//Add to hashtable
			portfolio.put(ticker, shares);
		}

		//Close the connection
		connection.close();
		st.close();
		rs.close();
	}

	public static void getBuySharesOverTime(int userID, TreeMap<Date, List<Hashtable<String, Integer>>> longTermPortfolio) throws SQLException {
		//Connect to database
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("SELECT * FROM UserToBuys WHERE userID=?");
		st.setInt(1, userID);
		rs = st.executeQuery();
		
		//Array list used to store buy ids for a user
		List<Integer> buyIDs = new ArrayList<Integer>();
		
		while(rs.next()) {
			//Add all the buy ids for that user into an array list
			buyIDs.add(rs.getInt(1));
		}
		
		//Use the buy id list to fill out a dictionary with stock ticker as key and number of shares as value
		//For each buy id, get the corresponding stock and shares

		for(int i = 0; i < buyIDs.size(); i++) {
			st = connection.prepareStatement("SELECT * FROM Buys WHERE buyID=?");
			st.setInt(1, buyIDs.get(i));
			rs = st.executeQuery();
			rs.next();
			
			String ticker = rs.getString(2);
			int shares = rs.getInt(3);
			Date date = rs.getDate(4);

			//Add to hashtable
			Hashtable<String, Integer> portfolio = new Hashtable<String, Integer>();
			portfolio.put(ticker, shares);
			
			//Check if the date is contained in the map
			if(longTermPortfolio.containsKey(date)) {
				//If contained, then add the purchase to the list
				List<Hashtable<String, Integer>> oldDatePurchase = longTermPortfolio.get(date);
				oldDatePurchase.add(portfolio);
				longTermPortfolio.put(date, oldDatePurchase);
				
			}
			else {
				//Otherwise create a new purchase
				List<Hashtable<String, Integer>> newDatePurchase = new ArrayList<Hashtable<String, Integer>>();
				newDatePurchase.add(portfolio);
				longTermPortfolio.put(date, newDatePurchase);
			}
		}

		//Close the connection
		connection.close();
		st.close();
		rs.close();		
	}

	
	public static void getSellShares(int userID, Hashtable<String, Integer> portfolio) throws SQLException {
		//Connect to database
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("SELECT * FROM UserToSells WHERE userID=?");
		st.setInt(1, userID);
		rs = st.executeQuery();
		
		//Array list used to store buy ids for a user
		List<Integer> sellIDs = new ArrayList<Integer>();
		
		while(rs.next()) {
			//Add all the buy ids for that user into an array list
			sellIDs.add(rs.getInt(1));
		}
		
		//Use the buy id list to fill out a dictionary with stock ticker as key and number of shares as value
		//For each buy id, get the corresponding stock and shares
		for(int i = 0; i < sellIDs.size(); i++) {
			st = connection.prepareStatement("SELECT * FROM Sells WHERE sellID=?");
			st.setInt(1, sellIDs.get(i));
			rs = st.executeQuery();
			rs.next();
			
			String ticker = rs.getString(2);
			int shares = rs.getInt(3);
				
			//Delete the shares from the hashtable
			if(portfolio.get(ticker) == shares) portfolio.remove(ticker);
			else portfolio.put(ticker, portfolio.get(ticker) - shares);
		}

		//Close the connection
		connection.close();
		st.close();
		rs.close();

	}
	
	public static void getSellSharesOverTime(int userID, TreeMap<Date, List<Hashtable<String, Integer>>> longTermPortfolio) throws SQLException {
		//Connect to database
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("SELECT * FROM UserToSells WHERE userID=?");
		st.setInt(1, userID);
		rs = st.executeQuery();
		
		//Array list used to store buy ids for a user
		List<Integer> sellIDs = new ArrayList<Integer>();
		
		while(rs.next()) {
			//Add all the buy ids for that user into an array list
			sellIDs.add(rs.getInt(1));
		}
		
		//Use the buy id list to fill out a dictionary with stock ticker as key and number of shares as value
		//For each buy id, get the corresponding stock and shares
		for(int i = 0; i < sellIDs.size(); i++) {
			st = connection.prepareStatement("SELECT * FROM Sells WHERE sellID=?");
			st.setInt(1, sellIDs.get(i));
			rs = st.executeQuery();
			rs.next();
			
			String ticker = rs.getString(2);
			int shares = -1*rs.getInt(3);
			Date date = rs.getDate(4);

			//Add to hashtable
			Hashtable<String, Integer> portfolio = new Hashtable<String, Integer>();
			portfolio.put(ticker, shares);
			
			//Check if the date is contained in the map
			if(longTermPortfolio.containsKey(date)) {
				//If contained, then add the purchase to the list
				List<Hashtable<String, Integer>> oldDatePurchase = longTermPortfolio.get(date);
				oldDatePurchase.add(portfolio);
				longTermPortfolio.put(date, oldDatePurchase);
				
			}
			else {
				//Otherwise create a new purchase
				List<Hashtable<String, Integer>> newDatePurchase = new ArrayList<Hashtable<String, Integer>>();
				newDatePurchase.add(portfolio);
				longTermPortfolio.put(date, newDatePurchase);
			}
		}

		//Close the connection
		connection.close();
		st.close();
		rs.close();

	}
	
	public static void getBuySharesOnDate(int userID, Date date, Hashtable<String, Integer> portfolio) throws SQLException {
		//Connect to database
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("SELECT * FROM UserToBuys WHERE userID=?");
		st.setInt(1, userID);
		rs = st.executeQuery();
		
		//Array list used to store buy ids for a user
		List<Integer> buyIDs = new ArrayList<Integer>();
		
		while(rs.next()) {
			//Add all the buy ids for that user into an array list
			buyIDs.add(rs.getInt(1));
		}
		
		//Use the buy id list to fill out a dictionary with stock ticker as key and number of shares as value
		//For each buy id, get the corresponding stock and shares
		for(int i = 0; i < buyIDs.size(); i++) {
			st = connection.prepareStatement("SELECT * FROM Buys WHERE buyID=?");
			st.setInt(1, buyIDs.get(i));
			rs = st.executeQuery();
			rs.next();
			
			String ticker = rs.getString(2);
			int shares = rs.getInt(3);
			Date thisDate = rs.getDate(4);
			if(!thisDate.after(date)) { // checks if purchase date is not after desired date
				portfolio.put(ticker, shares);
			}

			//Add to hashtable
		}

		//Close the connection
		connection.close();
		st.close();
		rs.close();
	}
	

	public static void getSellSharesOnDate(int userID, Date date, Hashtable<String, Integer> portfolio) throws SQLException {
		//Connect to database
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("SELECT * FROM UserToSells WHERE userID=?");
		st.setInt(1, userID);
		rs = st.executeQuery();
		
		//Array list used to store buy ids for a user
		List<Integer> sellIDs = new ArrayList<Integer>();
		
		while(rs.next()) {
			//Add all the sell ids for that user into an array list
			sellIDs.add(rs.getInt(1));
		}
		
		//Use the buy id list to fill out a dictionary with stock ticker as key and number of shares as value
		//For each buy id, get the corresponding stock and shares
		for(int i = 0; i < sellIDs.size(); i++) {
			st = connection.prepareStatement("SELECT * FROM Sells WHERE sellID=?");
			st.setInt(1, sellIDs.get(i));
			rs = st.executeQuery();
			rs.next();
			
			String ticker = rs.getString(2);
			int shares = rs.getInt(3);
			Date thisDate = rs.getDate(4);
			if(!thisDate.after(date)) { // ensures sell date is not after desired date
				//Delete the shares from the hashtable
				if(portfolio.get(ticker) == shares) portfolio.remove(ticker);
				else portfolio.put(ticker, portfolio.get(ticker) - shares);
			}
			
		}

		//Close the connection
		connection.close();
		st.close();
		rs.close();

	}
	
	/*
	 * Assumes all error checking has been done beforehand
	 */
	public static Integer buyShares(int userID, String index, int shares, Double price, Date date) throws SQLException {
		//Connect to database
		Connection connection = null;
		PreparedStatement st = null;
		PreparedStatement st2 = null;
		
		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("INSERT INTO Buys(symbol, shares, buyDate, price) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		st.setString(1, index.toUpperCase());
		st.setInt(2, shares);
		st.setDate(3, date);
		st.setDouble(4, price);
		

		//symbol shares buydate price
		st.executeUpdate();
		int buyID = 0;
		
		ResultSet rs = st.getGeneratedKeys();
		rs.next();
	
		buyID = rs.getInt(1);
		System.out.println("BuyID is: " + buyID);
		
		st2 = connection.prepareStatement("INSERT INTO UserToBuys(buyID, userID) VALUES(?, ?)");
		st2.setInt(1, buyID);
		st2.setInt(2, userID);

		st2.executeUpdate();

		connection.close();
		st.close();
		st2.close();
		
		return buyID;
	}
	
	/*
	 * Assumes all error checking has been done beforehand
	 */
	public static Integer sellShares(int userID, String index, int shares, Double price, Date date) throws SQLException {
		//Connect to database
		Connection connection = null;
		PreparedStatement st = null;
		PreparedStatement st2 = null;
		
		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("INSERT INTO Sells(symbol, shares, sellDate, price) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		st.setString(1, index);
		st.setInt(2, shares);
		st.setDate(3, date);
		st.setDouble(4, price);
		

		//symbol shares buydate price
		st.executeUpdate();
		ResultSet rs = st.getGeneratedKeys();
		rs.next();
		int sellID = rs.getInt(1);
		
		System.out.println("SellID is: " + sellID);
		
		st2 = connection.prepareStatement("INSERT INTO UserToSells(sellID, userID) VALUES(?, ?)");
		st2.setInt(1, sellID);
		st2.setInt(2, userID);

		st2.executeUpdate();

		connection.close();
		st.close();
		st2.close();
		return sellID;
	}
	
	public static void remBuy(int buyID) throws SQLException {
		//Connect to database
		Connection connection = null;
		PreparedStatement st = null;
		PreparedStatement st2 = null;
		
		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("DELETE FROM UserToBuys WHERE buyID=?");
		st.setInt(1, buyID);

		//symbol shares buydate price
		st.executeUpdate();
				
		st2 = connection.prepareStatement("DELETE FROM Buys WHERE buyID=?");
		st2.setInt(1, buyID);

		st2.executeUpdate();

		connection.close();
		st.close();
		st2.close();
	}
	
	public static void remSell(int sellID) throws SQLException {
		//Connect to database
		Connection connection = null;
		PreparedStatement st = null;
		PreparedStatement st2 = null;
		
		//Get database connection
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
		st = connection.prepareStatement("DELETE FROM UserToSells WHERE sellID=?");
		st.setInt(1, sellID);

		//symbol shares buydate price
		st.executeUpdate();
				
		st2 = connection.prepareStatement("DELETE FROM Sells WHERE sellID=?");
		st2.setInt(1, sellID);

		st2.executeUpdate();

		connection.close();
		st.close();
		st2.close();
	}
	
	//assumes index name is correct
	//find price for past 4 days, and take the last non-null value
	public static Double getPriceOnDate(String index, Date date) throws IOException, ParseException {
		
		int secondsInDay = 86400;
		
		Long seconds = date.getTime() / 1000;
		
		Long fiveDaysBefore = seconds - (5*secondsInDay);
		
		String format = "D";
		
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder()
				.url("https://finnhub.io/api/v1/stock/candle?token=btjq1kf48v6vivbngtk0&"
						+"symbol="+index
						+"&resolution="+format
						+"&from="+fiveDaysBefore
						+"&to="+seconds)
				.method("GET", null)
				.addHeader("Cookie", "__cfduid=d0a804546b94383d32aff9c130ca89da51600715190")
				.build();
		Response response = null;

		response = client.newCall(request).execute();

		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(response.body().string());
   
        JSONObject responseObject = (JSONObject)obj;
        
        JSONArray out = (JSONArray)responseObject.get("c");
        List<Double> result = new ArrayList<>();
        
        for (Object num : out) {
            result.add((Double) num);
        }
        
        Double curPrice = result.get(0);
        
        for (Double num : result) {
        	curPrice = num;
        }
        return curPrice; // yee
	}
	
	/*
	 * @Return list of percent change in portfolio value, then dollar amount
	 * If the returned percent change string is empty, infinite percent change
	 * [percent change (ex: 89.4), dollar amount]
	 */
	public static List<String> yestPortChange(String username) throws SQLException, ParseException, IOException{
		
		Double currentValue = CalcValue.getPortAndCalcVal(username);
		
		List<String> percentAndVal = new ArrayList<String>();
		Hashtable<String, Integer> portfolio = new Hashtable<String, Integer>();
		
		// now find yesterday's value
		int userID = CalcValue.getUserID(username);

		
		//find current date in milliseconds, then substract by milliseconds in a day
		Long curMilli = System.currentTimeMillis() - 86400000;
		
		Long start_date = (curMilli / 1000) - (86400 * 5);
		Long end_date = curMilli / 1000;
		
		String start_string = start_date.toString();
		String end_string = end_date.toString();
		
		Date curDate = new Date(curMilli);
		CalcValue.getBuySharesOnDate(userID, curDate, portfolio);
		CalcValue.getSellSharesOnDate(userID, curDate, portfolio);
		Double yesterdayValue = CalcValue.calcPortfolioVal(portfolio, start_string, end_string);

		
		String percent_change_string = "";
		String value_change_string = "";
		
		Double change_in_value = currentValue - yesterdayValue;
		
		// percent change will be invalid if we divide by zero
		if(yesterdayValue != 0.0) {
			Double percent_change = (change_in_value / yesterdayValue) * 100;
			percent_change_string = percent_change.toString();
		}
		
		
		value_change_string = change_in_value.toString();
		percentAndVal.add(percent_change_string);
		percentAndVal.add(value_change_string);
		
		return percentAndVal;
	}
		
	
}
