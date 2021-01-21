package csci310.utilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FinnhubHistoryTest {

    String dayApple = "{\"c\":[125.86000061035,124.83000183105,126.51999664307,125.01000213623,124.80999755859],\"s\":\"ok\",\"t\":[1598227200,1598313600,1598400000,1598486400,1598572800],\"v\":[345937760,211495792,163022272,155552384,187629920],\"h\":[128.7799987793,125.18000030518,126.98999786377,127.48000335693,126.44000244141],\"l\":[123.94000244141,123.05000305176,125.08000183105,123.83000183105,124.58000183105],\"o\":[128.69999694824,124.69999694824,126.18000030518,127.13999938965,126.01000213623]}";
    String dayGoogle = "{\"c\":[1585.1500244141,1605.8499755859,1644.1300048828,1628.5200195312,1639.4300537109],\"h\":[1608.5300292969,1608.5899658203,1651.75,1647.9899902344,1641.3499755859],\"l\":[1576.2900390625,1579.7800292969,1601.4899902344,1621.0400390625,1625.6049804688],\"o\":[1591.7800292969,1579.7800292969,1606,1647.9899902344,1629.4699707031],\"s\":\"ok\",\"t\":[1598227200,1598313600,1598400000,1598486400,1598572800],\"v\":[1281893,1257463,2609363,1567503,1121195]}";
    String weekGoogle = "{\"c\":[1428.9599609375],\"h\":[1434.9399414062],\"l\":[1351],\"o\":[1351.6300048828],\"s\":\"ok\",\"t\":[1578182400],\"v\":[8806689]}";
    String monthGoogle = "{\"c\":[1432.7800292969,1339.25],\"h\":[1500.5799560547,1530.7399902344],\"l\":[1346.4899902344,1268.2099609375],\"o\":[1348.4100341797,1461.6500244141],\"s\":\"ok\",\"t\":[1578009600,1580688000],\"v\":[33700320,41565156]}";

    JSONParser parser = new JSONParser();

    private JSONObject getJson(String apiResponse) throws ParseException {
        Object obj;
        obj = parser.parse(apiResponse);
        return (JSONObject)obj;
    }
    
    @Test
    public void testgetHistory() throws ParseException, IOException {
    	
    	String stock_index = null;
    	Resolution r = null;
    	String start_time = null;
    	String end_time = null;

    	JSONObject hourGoogleJson;
    	JSONObject dayGoogleJson;
        JSONObject weekGoogleJson;
        JSONObject monthGoogleJson;
        JSONObject dayAppleJson;
        try {
        	dayGoogleJson = getJson(dayGoogle);
        	weekGoogleJson = getJson(weekGoogle);
        	monthGoogleJson = getJson(monthGoogle);
        	dayAppleJson = getJson(dayApple);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
            return;
        }
    	
    	// test case 1
    	stock_index = "GOOGL";
    	r = Resolution.day;
    	start_time = "1598227200";
    	end_time = "1598659200";
    	
    	JSONObject obj_1 = FinnhubHistory.getHistory(stock_index, r, start_time, end_time);
    	Assert.assertEquals(
    		    dayGoogleJson.toJSONString(),
    		    obj_1.toJSONString());
		
    	// test case 2
    	stock_index = "GOOGL";
    	r = Resolution.week;
    	start_time = "1577836800";
    	end_time = "1578614400";
    	
    	JSONObject obj_2 = FinnhubHistory.getHistory(stock_index, r, start_time, end_time);
    	Assert.assertEquals(
    		    weekGoogleJson.toJSONString(),
    		    obj_2.toJSONString());
    	
    	// test case 3
    	stock_index = "GOOGL";
    	r = Resolution.month;
    	start_time = "1577836800";
    	end_time = "1580515200";
    	
    	JSONObject obj_3 = FinnhubHistory.getHistory(stock_index, r, start_time, end_time);
    	Assert.assertEquals(
    		    monthGoogleJson.toJSONString(),
    		    obj_3.toJSONString());
    	
    	// test case 4
    	stock_index = "AAPL";
    	r = Resolution.day;
    	start_time = "1598227200";
    	end_time = "1598659200";
    	
    	JSONObject obj_4 = FinnhubHistory.getHistory(stock_index, r, start_time, end_time);
    	Assert.assertEquals(
    		    dayAppleJson.toJSONString(),
    		    obj_4.toJSONString());
    	
    	// test case 5
    	stock_index = "AAPL";
    	r = Resolution.minute;
    	start_time = "1598227200";
    	end_time = "1598659200";
    	
    	FinnhubHistory pullStock = new FinnhubHistory();
    	JSONObject obj_5 = pullStock.getHistory(stock_index, r, start_time, end_time);
    	Assert.assertEquals(
    		    dayAppleJson.toJSONString(),
    		    obj_5.toJSONString());
    }

	@Test
	public void testGetHistories() throws ParseException, IOException {

		String stock_index = null;
		Resolution r = null;
		String start_time = null;
		String end_time = null;

		JSONObject dayGoogleJson = null;
		try {
			dayGoogleJson = getJson(dayGoogle);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
			return;
		}

		List<JSONObject> expected = new ArrayList<>();
		expected.add(dayGoogleJson);

		// test case 1
		stock_index = "GOOGL";
		r = Resolution.day;
		start_time = "1598227200";
		end_time = "1598659200";

		List<String> stocks = new ArrayList<>();
		stocks.add(stock_index);

		List<JSONObject> obj_1 = FinnhubHistory.getHistories(stocks, r, start_time, end_time);

		Assert.assertEquals(expected, obj_1);
	}
}
