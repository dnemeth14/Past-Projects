package csci310.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FinnhubHistory {
	
	/**
     * Takes stock info and returns a JSONObject response
     * @param stock_index
     * @param resolution_in
     * @param start_time
     * @param end_time
	 * @throws IOException 
	 * @throws ParseException 
     */
	public static JSONObject getHistory(String stock_index, Resolution resolution_in, String start_time, String end_time) throws ParseException, IOException {
		String resolution;
        switch (resolution_in) {
        	case week:
                resolution = "W";
                break;
            case month:
                resolution = "M";
                break;
			case day:
            default:
                resolution = "D";
                break;
        }
		
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder()
				.url("https://finnhub.io/api/v1/stock/candle?token=btjq1kf48v6vivbngtk0&"
						+"symbol="+stock_index
						+"&resolution="+resolution
						+"&from="+start_time
						+"&to="+end_time)
				.method("GET", null)
				.addHeader("Cookie", "__cfduid=d0a804546b94383d32aff9c130ca89da51600715190")
				.build();
		Response response = null;

		response = client.newCall(request).execute();

		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(response.body().string());
   
        return (JSONObject)obj;
    }

	public static List<JSONObject> getHistories(List<String> stock_index, Resolution resolution, String start_time, String end_time) throws ParseException, IOException {
		List<JSONObject> apiResponses = new ArrayList<>();
		for (String index : stock_index) {
			apiResponses.add(FinnhubHistory.getHistory(index, resolution, start_time, end_time));
		}
		return apiResponses;
	}
}
