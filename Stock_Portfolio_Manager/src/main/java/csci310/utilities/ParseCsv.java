package csci310.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;


public class ParseCsv {
	
	private String filename;

	public ParseCsv(String filename) {
		this.filename = filename;
	}
	
	/**
     * Parse a .csv file (SYMBOL, BUY-DATE, #-SHARES) and return a HashMap of HashMaps
     * @param csv_filename
	 * @throws IOException 
     */
	public ListMultimap<String, ListMultimap<String, Float>> parsecsv() throws IOException{
		
		String row;
		BufferedReader csvReader = new BufferedReader(new FileReader(filename));
		
		ListMultimap<String, ListMultimap<String, Float>> stock_data = ArrayListMultimap.create();
		
		// skip header
		csvReader.readLine();
		
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    
		    ListMultimap<String, Float> date_shares = ArrayListMultimap.create();
		    
		    float f = Float.parseFloat(data[2]);
		    
		    date_shares.put(data[1],f);
		    stock_data.put(data[0], date_shares);
		    
		}
		
		csvReader.close();
		return stock_data;
	}
	
	/**
     * Given stock index and buy-date, return the list of number of shares purchased on that date
     * @param index
     * @param date
	 * @throws IOException 
     */
	public List<Float> getShares(String index, String date) throws IOException {
		
		ParseCsv parser = new ParseCsv(filename);
		ListMultimap<String, ListMultimap<String, Float>> stock_data = parser.parsecsv();
		List<Float> target_shares = new ArrayList<Float>();
		
		
		List<ListMultimap<String, Float>> date_shares = stock_data.get(index);
		for (ListMultimap<String, Float> map : date_shares) {
			// System.out.println(map.get(date));
			if (!map.get(date).isEmpty()) {
				// System.out.println("found");
				target_shares.add(map.get(date).get(0));
			}
		}
		return target_shares;
	}
	
}
