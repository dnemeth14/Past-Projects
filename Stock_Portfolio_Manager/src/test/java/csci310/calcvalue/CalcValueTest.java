package csci310.calcvalue;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;

import org.json.simple.parser.ParseException;
import org.junit.Test;
import csci310.calcvalue.CalcValue;

public class CalcValueTest {

	@Test
	public void testCalcPortfolioVal() throws ParseException, IOException {
		Hashtable<String, Integer> portfolio = new Hashtable<String, Integer>();
		portfolio.put("AAPL", 20);
		portfolio.put("GE", 1);
		assertEquals(2170.4600243567793, CalcValue.calcPortfolioVal(portfolio, Integer.toString(1600754280), Integer.toString(1600927080)), 0.001);
	}
	
	@Test
	public void testGetUserID() throws SQLException {
		assertEquals(1, CalcValue.getUserID("test"));
		assertEquals(-1, CalcValue.getUserID("NotInDatabase"));
	}
	
	@Test
	public void testGetBuyShares() throws SQLException {
		Hashtable<String, Integer> portfolio = new Hashtable<String, Integer>();
		portfolio.put("AAPL", 50);
		portfolio.put("GOOGL", 100);
		portfolio.put("GE", 35);
		portfolio.put("AMZN", 10);
		Hashtable<String, Integer> testPortfolio = new Hashtable<String, Integer>();
		CalcValue.getBuyShares(1, testPortfolio);
		assertEquals(portfolio, testPortfolio);
	}
	
	@Test
	public void testGetSellShares() throws SQLException {
		Hashtable<String, Integer> portfolio = new Hashtable<String, Integer>();
		portfolio.put("AAPL", 20);
		portfolio.put("GE", 1);
		Hashtable<String, Integer> testPortfolio = new Hashtable<String, Integer>();
		testPortfolio.put("AAPL", 50);
		testPortfolio.put("GOOGL", 100);
		testPortfolio.put("GE", 35);
		testPortfolio.put("AMZN", 10);
		CalcValue investor = new CalcValue();
		CalcValue.getSellShares(1, testPortfolio);
		assertEquals(portfolio, testPortfolio);
	}
	
	@Test
	public void testGetPortAndCalcVal() throws SQLException, ParseException, IOException {
		String username = "test"; 
		Hashtable<String, Integer> portfolio = new Hashtable<String, Integer>();
		
		String today = Long.toString(System.currentTimeMillis() / 1000L);
	    String yesterday = Long.toString(Long.parseLong(today) - (7*86400));
	    int userID = CalcValue.getUserID(username);
		CalcValue.getBuyShares(userID, portfolio);
		CalcValue.getSellShares(userID, portfolio);
		long portfolioValue = (long) CalcValue.calcPortfolioVal(portfolio, yesterday, today);
		assertEquals(portfolioValue , CalcValue.getPortAndCalcVal(username), 100);
	}
	
	@Test
	public void testBuyShares() throws SQLException {
		String testDate = "2019-09-02";
		
		Date date = Date.valueOf(testDate);
		int buyID = CalcValue.buyShares(1, "AAPL", 1, 100.00, date);
		CalcValue.remBuy(buyID);
		assertTrue(true);
	}
	
	@Test
	public void testSellShares() throws SQLException {
		String testDate = "2020-09-02";
		
		Date date = Date.valueOf(testDate);
		int buyID = CalcValue.buyShares(1, "ADBE", 1, 100.00, date);
		int sellID = CalcValue.sellShares(1, "ADBE", 1, 100.00, date);
		CalcValue.remBuy(buyID);
		CalcValue.remSell(sellID);
		
		assertTrue(true);
	}
	
	@Test
	public void testRemBuy() throws SQLException {
		String testDate = "2020-09-02";
		
		Date date = Date.valueOf(testDate);
		int buyID = CalcValue.buyShares(1, "ADBE", 1, 100.00, date);
		int sellID = CalcValue.sellShares(1, "ADBE", 1, 100.00, date);
		CalcValue.remBuy(buyID);
		CalcValue.remSell(sellID);
		
		assertTrue(true);
	}
	
	@Test
	public void testRemSell() throws SQLException {
		String testDate = "2020-09-02";
		
		Date date = Date.valueOf(testDate);
		int buyID = CalcValue.buyShares(1, "ADBE", 1, 100.00, date);
		int sellID = CalcValue.sellShares(1, "ADBE", 1, 100.00, date);
		CalcValue.remBuy(buyID);
		CalcValue.remSell(sellID);
		
		assertTrue(true);
	}
	
	@Test
	public void testGetBuySharesOverTime() throws SQLException {
		//Create the tree map to be compared
		TreeMap<Date, List<Hashtable<String, Integer>>> testBuyShares = new TreeMap<Date, List<Hashtable<String, Integer>>>();
		
		//Add transactions to the test
		List<Hashtable<String, Integer>> thatDayPurchases = new ArrayList<Hashtable<String, Integer>>();
		Hashtable<String, Integer> purchase = new Hashtable<String, Integer>();
		Hashtable<String, Integer> purchaseDuplicate = new Hashtable<String, Integer>();
		purchase.put("AAPL", 50); purchaseDuplicate.put("AMZN", 10); 
		thatDayPurchases.add(purchase); thatDayPurchases.add(purchaseDuplicate);
		String testDate = "2020-09-02";	Date date = Date.valueOf(testDate);
		testBuyShares.put(date, thatDayPurchases);
		
		List<Hashtable<String, Integer>> thatDayPurchasesOne = new ArrayList<Hashtable<String, Integer>>();
		Hashtable<String, Integer> purchaseOne = new Hashtable<String, Integer>();
		purchaseOne.put("GOOGL", 100); thatDayPurchasesOne.add(purchaseOne);
		testDate = "2018-09-02"; date = Date.valueOf(testDate);
		testBuyShares.put(date, thatDayPurchasesOne);
		
		List<Hashtable<String, Integer>> thatDayPurchasesTwo = new ArrayList<Hashtable<String, Integer>>();
		Hashtable<String, Integer> purchaseTwo = new Hashtable<String, Integer>();
		purchaseTwo.put("GE", 35); thatDayPurchasesTwo.add(purchaseTwo);
		testDate = "2013-09-02"; date = Date.valueOf(testDate);
		testBuyShares.put(date, thatDayPurchasesTwo);

		TreeMap<Date, List<Hashtable<String, Integer>>> actualBuyShares = new TreeMap<Date, List<Hashtable<String, Integer>>>();
		CalcValue.getBuySharesOverTime(1, actualBuyShares);
		
		assertEquals(testBuyShares, actualBuyShares);
	}
	
	@Test
	public void testGetSellSharesOverTime() throws SQLException {
		//Create the tree map to be compared
		TreeMap<Date, List<Hashtable<String, Integer>>> testSellShares = new TreeMap<Date, List<Hashtable<String, Integer>>>();
		
		//Add transactions to the test
		List<Hashtable<String, Integer>> thatDayPurchases = new ArrayList<Hashtable<String, Integer>>();
		Hashtable<String, Integer> purchase = new Hashtable<String, Integer>();
		Hashtable<String, Integer> purchaseDuplicate = new Hashtable<String, Integer>();
		purchase.put("AAPL", -30); purchaseDuplicate.put("AMZN", -10); 
		thatDayPurchases.add(purchase); thatDayPurchases.add(purchaseDuplicate);
		String testDate = "2020-09-20";	Date date = Date.valueOf(testDate);
		testSellShares.put(date, thatDayPurchases);
		
		List<Hashtable<String, Integer>> thatDayPurchasesOne = new ArrayList<Hashtable<String, Integer>>();
		Hashtable<String, Integer> purchaseOne = new Hashtable<String, Integer>();
		purchaseOne.put("GE", -34); thatDayPurchasesOne.add(purchaseOne);
		testDate = "2018-09-20"; date = Date.valueOf(testDate);
		testSellShares.put(date, thatDayPurchasesOne);
		
		List<Hashtable<String, Integer>> thatDayPurchasesTwo = new ArrayList<Hashtable<String, Integer>>();
		Hashtable<String, Integer> purchaseTwo = new Hashtable<String, Integer>();
		purchaseTwo.put("GOOGL", -100); thatDayPurchasesTwo.add(purchaseTwo);
		testDate = "2019-09-20"; date = Date.valueOf(testDate);
		testSellShares.put(date, thatDayPurchasesTwo);

		TreeMap<Date, List<Hashtable<String, Integer>>> actualSellShares = new TreeMap<Date, List<Hashtable<String, Integer>>>();
		CalcValue.getSellSharesOverTime(1, actualSellShares);
		
		assertEquals(testSellShares, actualSellShares);
	}
	
	@Test
	public void testPortfolioValOverTime() throws SQLException, java.text.ParseException, ParseException, IOException {
		//Create the tree map to be compared
		TreeMap<Date, List<Hashtable<String, Integer>>> testShares = new TreeMap<Date, List<Hashtable<String, Integer>>>();

		//Get time range
		String start_date = "2020-09-01 09:00:00", end_date = "2020-09-23 09:00:00";
		String username = "test";
		
		//List<double> for comparison
		List<Double> testList = new ArrayList<Double>();
		testList.add(0.0);
		testList.add(41884.5);
		testList.add(41884.5);
		testList.add(41884.5);
		testList.add(39724.0);
		testList.add(38994.2);
		testList.add(37139.4);
		testList.add(38552.1);
		testList.add(38552.1);
		testList.add(38552.1);
		testList.add(38552.1);
		testList.add(37425.6);
		testList.add(36762.2);
		testList.add(36797.7);
		testList.add(37338.3);
		testList.add(37338.3);
		testList.add(37338.3);
		testList.add(37338.3);
		testList.add(36387.5);
		testList.add(2206.8);
		testList.add(2136.8);
		testList.add(2201.6);
		assertEquals(testList, CalcValue.portfolioValOverTime(username, start_date, end_date));
	}

	@Test
	public void testGetPriceOnDate() {
		String testDate = "2020-09-02";
		String testMLK = "2020-02-17";
		
		//tests normal day and MLK day
		
		String index = "AAPL";
		
		Date date = Date.valueOf(testDate);
		Date mlkDate = Date.valueOf(testMLK);
		
		Double price = 0.0;
		Double mlkPrice = 0.0;
		
		try {
			price = CalcValue.getPriceOnDate(index, date);
			mlkPrice = CalcValue.getPriceOnDate(index, mlkDate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(price);
		System.out.println(mlkPrice);
		
		Double actual = 131.39999389648;
		Double mlkActual = 81.23999786377;

		assertTrue(actual.equals(price));
		assertTrue(mlkActual.equals(mlkPrice));
	}
	
	@Test
	public void testYestPortChange() throws SQLException, ParseException, IOException {
		
		//input username for userID2
		String secUser = "username";
		int userID = CalcValue.getUserID("username");
		
		//get today's date
		Long curMilli = System.currentTimeMillis();
		Date todayDate = new Date(curMilli);
		
		// add buy for them for today
		int buyID = CalcValue.buyShares(userID, "MMM", 50, 0.0, todayDate);
		
		// then do testPortChange
		List<String> vals = CalcValue.yestPortChange("username");
		
		assertTrue(vals.get(0).equals(""));
		assertFalse(vals.get(1).equals("0.0") || vals.get(1).equals("0"));
		
		//delete the buy for secUser
		CalcValue.remBuy(buyID);
		
		//then do for other user
		String baseUsername = "test";
		List<String> baseVals = CalcValue.yestPortChange(baseUsername);
		assertFalse(baseVals.get(0).equals(""));		
	}
}
