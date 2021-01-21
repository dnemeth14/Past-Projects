package csci310;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

import org.json.simple.parser.ParseException;
import org.junit.Test;

public class ValidateTest {

	
//	@Test
//	public void testNumSharesOnDate() {
//		Validate x = new Validate();
//		String index = "AAPL";
//		
//		assertTrue(x.numSharesOnDate(1, index, x.stringToDate("2020-09-02")) == 50);
//		assertTrue(x.numSharesOnDate(1, index, x.stringToDate("2020-09-02")) == 0);
//		assertTrue(x.numSharesOnDate(1, index, x.stringToDate("2020-09-19")) == 50);
//		assertTrue(x.numSharesOnDate(1, index, x.stringToDate("2020-09-20")) == 20);
//		assertTrue(x.numSharesOnDate(-1, index, x.stringToDate("2020-09-20")) == 0);
//
//	}
	
	
	
	@Test
	public void testStringToDate() {
		Validate x = new Validate();
		String testDate = "2019-09-02";
		int month = 8;
		int day = 2;
		int year = 2019 - 1900;
		assertTrue(x.stringToDate(testDate).getMonth() == month);
		assertTrue(x.stringToDate(testDate).getDate() == day);
		assertTrue(x.stringToDate(testDate).getYear() == year);
	}
	
	@Test
	public void testGetCurrentDate() {
		Validate x = new Validate();
		TimeZone zone = TimeZone.getTimeZone("EST");
		Calendar time = Calendar.getInstance(zone);
		String sDate = "" + time.get(Calendar.YEAR) + "-" + time.get(Calendar.MONTH) + "-" + time.get(Calendar.DAY_OF_MONTH); 
		assertTrue(sDate.equals(x.getCurrentDate()));
	}
	
//	@Test
//	public void testValidIndex() {
//		
//		Validate x = new Validate();
//		
//		
//		try {
//			assertFalse(x.validIndex("", x.stringToDate(x.getCurrentDate())));
//			assertTrue(x.validIndex("AAPL", x.stringToDate(x.getCurrentDate()))); // APPLE NASDAQ
//			assertTrue(x.validIndex("aaPl", x.stringToDate(x.getCurrentDate())));
//			assertFalse(x.validIndex("ASADFSF", x.stringToDate(x.getCurrentDate())));
//		} catch (ParseException | IOException e) {
//			// TODO Auto-generated catch block
//			assertTrue(false);
//		}
//		
//
//	}
	
	@Test
	public void testNotFutureDate() {
		//time for now
		Validate x = new Validate();

		TimeZone zone = TimeZone.getTimeZone("PST");
		
		Calendar time = Calendar.getInstance(zone);
		
		
		String sDate = "" + time.get(Calendar.YEAR) + "-" + time.get(Calendar.MONTH) + "-" + time.get(Calendar.DAY_OF_MONTH); 
		Date curDate = Date.valueOf(sDate);
		
		String sDate1 = "2020-09-02";
		Date pastDate = Date.valueOf(sDate1);
		
		Calendar future = Calendar.getInstance(zone);
		future.add(Calendar.DAY_OF_MONTH, 5);
		
		String fsDate = "" + future.get(Calendar.YEAR) + "-" + future.get(Calendar.MONTH) + "-" + future.get(Calendar.DAY_OF_MONTH); 
		Date futureDate = Date.valueOf(fsDate);
		
		// now test a past time, curDate, and future date
		assertTrue(x.notFutureDate(curDate));
		assertTrue(x.notFutureDate(pastDate));
		assertFalse(x.notFutureDate(futureDate));
	}

	@Test
	public void testCompareTwoDates() {
		
		Validate x = new Validate();
		
		String sDate1 = "2020-09-02";
		String sDate2 = "2020-09-03";
		
		Date first = Date.valueOf(sDate1);
		Date second = Date.valueOf(sDate2);
		
		
		assertTrue(x.compareTwoDates(first, second)); //valid
		assertFalse(x.compareTwoDates(second, first)); //invalid
		
		//if two dates are equal...
		assertFalse(x.compareTwoDates(first, first));
	}

	@Test
	public void testValidIndexOnDate() throws IOException, ParseException {
		
		Validate x = new Validate();
		String index = "AAPL";
		String sDate1 = "1965-09-02";
		String sDate2 = "2020-09-02";
		Date date = Date.valueOf(sDate1);
		Date date2 = Date.valueOf(sDate2);
		
		
		assertFalse(x.validIndexOnDate(index, date));
		assertTrue(x.validIndexOnDate(index, date2));
	}

	@Test
	public void testValidBuyShares() {
		
		//test if shares non-negative
		Validate x = new Validate();
		
		//test negative
		assertFalse(x.validBuyShares(-1));
		
		assertFalse(x.validBuyShares(0));
		
		assertTrue(x.validBuyShares(1));

	}

	@Test
	public void testValidSellSharesOnDate() throws SQLException {
		Validate x = new Validate();
		String index = "AAPL";
		int userID = 1;
		
		String sDate1 = "2020-09-01";
		Date date1 = Date.valueOf(sDate1);
		
		//not sure about this case, buying/selling on same date
		String sDate2 = "2020-09-02";
		Date date2 = Date.valueOf(sDate2);
		
		String sDate3 = "2020-09-03";
		Date date3 = Date.valueOf(sDate3);
		
		//selling when you've sold on same day, no
		String sDate4 = "2020-09-20";
		Date date4 = Date.valueOf(sDate4);
		
		//try negative shares again
		assertFalse(x.validSellSharesOnDate(index, date1, 1, userID));
		assertFalse(x.validSellSharesOnDate(index, date2, -1, userID));
		assertTrue(x.validSellSharesOnDate(index, date3, 50, userID));
		assertTrue(x.validSellSharesOnDate(index, date3, 49, userID));
		assertFalse(x.validSellSharesOnDate(index, date3, 51, userID));
		assertTrue(x.validSellSharesOnDate(index, date4, 20, userID));
		assertFalse(x.validSellSharesOnDate(index, date4, 21, userID));
	}

	@Test
	public void testOvrValidBuyOnly() throws IOException, ParseException {
		Validate x = new Validate();
		String gIndex = "AAPL";
		String bIndex = "APPLE";
		
		int userID = 1;
		
		String oldSDate = "1965-09-02";
		Date oldDate = Date.valueOf(oldSDate);

		
		String sDate1 = "2020-09-01";
		Date date1 = Date.valueOf(sDate1);
		
		//find current date
		TimeZone zone = TimeZone.getTimeZone("PST");
		Calendar curCal = Calendar.getInstance(zone);
		String curSDate = "" + curCal.get(Calendar.YEAR) + "-" + curCal.get(Calendar.MONTH) + "-" + curCal.get(Calendar.DAY_OF_MONTH); 
		Date curDate = Date.valueOf(curSDate);
		
		//bad index name
		assertFalse(x.ovrValidBuyOnly(bIndex, date1, 20, userID));
		
		// date before went public
		assertFalse(x.ovrValidBuyOnly(gIndex, oldDate, 20, userID));
		
		//buy negative shares
		assertFalse(x.ovrValidBuyOnly(gIndex, date1, -1, userID));
		
		//buy in past -- should work
		assertTrue(x.ovrValidBuyOnly(gIndex, date1, 20, userID));
		
		//buy today -- should work
		assertTrue(x.ovrValidBuyOnly(gIndex, curDate, 20, userID));
		
		//buy in future  -- should not work
		Calendar future = Calendar.getInstance(zone);
		future.add(Calendar.DAY_OF_MONTH, 5);
		String fsDate = "" + future.get(Calendar.YEAR) + "-" + future.get(Calendar.MONTH) + "-" + future.get(Calendar.DAY_OF_MONTH); 
		Date futureDate = Date.valueOf(fsDate);
		assertFalse(x.ovrValidBuyOnly(gIndex, futureDate, 50, userID));
	}

	@Test
	public void testOvrValidSellOnly() throws SQLException, IOException, ParseException {
		Validate x = new Validate();
		String gIndex = "AAPL";
		String bIndex = "APPLE";
		
		int userID = 1;

		
		String sDate1 = "2020-09-01";
		Date date1 = Date.valueOf(sDate1);
		
		String sDate3 = "2020-09-03";
		Date date3 = Date.valueOf(sDate3);
		
		//find current date
		TimeZone zone = TimeZone.getTimeZone("PST");
		Calendar curCal = Calendar.getInstance(zone);
		String curSDate = "" + curCal.get(Calendar.YEAR) + "-" + curCal.get(Calendar.MONTH) + "-" + curCal.get(Calendar.DAY_OF_MONTH); 
		Date curDate = Date.valueOf(curSDate);
		
		//find future date
		Calendar future = Calendar.getInstance(zone);
		future.add(Calendar.DAY_OF_MONTH, 5);
		String fsDate = "" + future.get(Calendar.YEAR) + "-" + future.get(Calendar.MONTH) + "-" + future.get(Calendar.DAY_OF_MONTH); 
		Date futureDate = Date.valueOf(fsDate);
		
		// future date should not work
		assertFalse(x.ovrValidSellOnly(gIndex, futureDate, 1, userID));

		
		//should not sell negative
		assertFalse(x.ovrValidSellOnly(gIndex, date1, -1, userID));
		
		//invalid index
		assertFalse(x.ovrValidSellOnly(bIndex, date3, 1, userID));
		
		//before purchased
		assertFalse(x.ovrValidSellOnly(gIndex, date1, 1, userID));
		
		//after purchased, valid shares
		assertTrue(x.ovrValidSellOnly(gIndex, date3, 50, userID));
		
		//after purchased, too many shares
		assertFalse(x.ovrValidSellOnly(gIndex, date3, 51, userID));

		//sell today
		assertTrue(x.ovrValidSellOnly(gIndex, curDate, 10, userID));
		
		//don't need to worry about selling in the past
	}

	@Test
	public void testOvrValidBuySell() throws SQLException, IOException, ParseException {
		
		//only different thing should be testing buy date vs. sell date stuff, otherwise chill
		
		Validate x = new Validate();
		
		String gIndex = "AAPL";
		
		int userID = 1;

		
		String sDate1 = "2020-09-01";
		Date date1 = Date.valueOf(sDate1);
		
		String sDate3 = "2020-09-03";
		Date date3 = Date.valueOf(sDate3);
				
		assertTrue(x.ovrValidBuySell(gIndex, date1, date3, 50, 50, userID));
		assertFalse(x.ovrValidBuySell(gIndex, date3, date1, 50, 50, userID));
		assertTrue(x.ovrValidBuySell(gIndex, date1, date3, 60, 50, userID));
		assertFalse(x.ovrValidBuySell(gIndex, date1, date3, 1, 0, userID));
		assertFalse(x.ovrValidBuySell(gIndex, date1, date3, -1, -1, userID));
		
		TimeZone zone = TimeZone.getTimeZone("PST");

		Calendar future = Calendar.getInstance(zone);
		future.add(Calendar.DAY_OF_MONTH, 5);
		String fsDate = "" + future.get(Calendar.YEAR) + "-" + future.get(Calendar.MONTH) + "-" + future.get(Calendar.DAY_OF_MONTH); 
		Date futureDate = Date.valueOf(fsDate);
		
		assertFalse(x.ovrValidBuySell(gIndex, date1, futureDate, 1, 1, userID));
		
		//test unavilable shares to sell
		assertFalse(x.ovrValidBuySell(gIndex, date1, date3, 2, 500, userID));
		
		//test stock user hasn't bought yet
		assertTrue(x.ovrValidBuySell("ADBE", date1, date3, 50, 50, userID));	
	}

}
