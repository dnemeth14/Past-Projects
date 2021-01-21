package csci310.utilities;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ListMultimap;

public class ParseCsvTest {

	@Test
	public final void testParsecsv() throws IOException {
		
		// uncomment to check the current file reading location
//		File file = new File(".");
//		for(String fileNames : file.list()) System.out.println(fileNames);
		
		ParseCsv parser = new ParseCsv("sql_code/sample_stock_data.csv");
		ListMultimap<String, ListMultimap<String, Float>> stock_data = parser.parsecsv();
		
		
		// uncomment the following lines to check all stock info
//		System.out.println("All stock info: "); 
//	    for (String name: stock_data.keySet()){
//            String key = name.toString();
//            String value = stock_data.get(name).toString();  
//            System.out.println(key + " " + value);  
//	    } 
		
		// test case 1
		Assert.assertEquals(
				stock_data.get("AAPL").toString(),
    		    "[{1598227200=[10.0]}, {1598659200=[100.0]}, {1578614400=[1000.0]}]");
		// test case 2
		Assert.assertEquals(
				stock_data.get("GOOGL").toString(),
    		    "[{1578614400=[10.0]}, {1578614400=[100.0]}, {1578614500=[1000.0]}]");
		
	}
	
	@Test
	public final void testGetShares() throws IOException {
		
		ParseCsv parser = new ParseCsv("sql_code/sample_stock_data.csv");
		
		// test case 1
		List<Float> shares = parser.getShares("GOOGL", "1578614400");
		Assert.assertEquals(
				shares.toString(),
    		    "[10.0, 100.0]");
		// test case 2
		shares = parser.getShares("AAPL", "1598227200");
		Assert.assertEquals(
				shares.toString(),
    		    "[10.0]");
	}

}
