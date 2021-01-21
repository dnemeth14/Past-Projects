package csci310.utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class FinnhubGraphingTest {
    String weekResponse = "{\"c\":[125.85749816895,124.82499694824,126.52249908447,125.01000213623,124.80750274658],\"h\":[128.78500366211,125.17929840088,126.99250030518,127.48500061035,126.44249725342],\"l\":[123.9362487793,123.05249786377,125.08249664307,123.83249664307,124.57749938965],\"o\":[128.69749450684,124.69750213623,126.1791229248,127.14250183105,126.01249694824],\"s\":\"ok\",\"t\":[1598227200,1598313600,1598400000,1598486400,1598572800],\"v\":[345937984,211496000,163022000,155552000,187630000]}";
    String dayResponse = "{\"c\":[507.22,496.4879,508.11,504.805,506.015,501.73,503.79,503.5,504.64,504.25,506.1],\"h\":[515.14,507.4799,508.63,509.8,508,507.08,504.515,503.81,505,504.75,506.1],\"l\":[502.24,495.745,495.75,504.24,503.12,501.1,498.62,501.4647,503.43,503.9,504.05],\"o\":[514.79,507.37,496.7865,508.1068,504.725,506.07,501.71,503.68,504.9,504.6,504.21],\"s\":\"ok\",\"t\":[1598274000,1598277600,1598281200,1598284800,1598288400,1598292000,1598295600,1598299200,1598302800,1598306400,1598310000],\"v\":[20054981,17803534,11624378,8916326,6506171,6952637,11649315,1430007,288289,45501,71395]}";
    String monthResponse = "{\"c\":[77.377502441406,68.339996337891,63.572498321533,73.449996948242,79.485000610352,91.199996948242],\"h\":[81.962501525879,81.805000305176,76,73.632499694824,81.059997558594,93.095001220703],\"l\":[73.1875,64.092498779297,53.152500152588,59.224998474121,71.462501525879,79.30249786377],\"o\":[74.059997558594,76.074996948242,70.569999694824,61.625,71.5625,79.4375],\"s\":\"ok\",\"t\":[1578009600,1580688000,1583193600,1585872000,1588464000,1591142400],\"v\":[2940100096,3020900096,6281299968,3266099968,2806599936,3243599872]}";

    String weekApple = "{\"c\":[125.85749816895,124.82499694824,126.52249908447,125.01000213623,124.80750274658],\"h\":[128.78500366211,125.17929840088,126.99250030518,127.48500061035,126.44249725342],\"l\":[123.9362487793,123.05249786377,125.08249664307,123.83249664307,124.57749938965],\"o\":[128.69749450684,124.69750213623,126.1791229248,127.14250183105,126.01249694824],\"s\":\"ok\",\"t\":[1598227200,1598313600,1598400000,1598486400,1598572800],\"v\":[345937984,211496000,163022000,155552000,187630000]}";
    String weekGoogle = "{\"c\":[1585.1500244141,1605.8499755859,1644.1300048828,1628.5200195312,1639.4300537109],\"h\":[1608.5300292969,1608.5899658203,1651.75,1647.9899902344,1641.3499755859],\"l\":[1576.2900390625,1579.7800292969,1601.4899902344,1621.0400390625,1625.6049804688],\"o\":[1591.7800292969,1579.7800292969,1606,1647.9899902344,1629.4699707031],\"s\":\"ok\",\"t\":[1598227200,1598313600,1598400000,1598486400,1598572800],\"v\":[1281893,1257463,2609363,1567503,1121195]}";
    String expectedAAPLandGOOGL = "{\"datasets\":[{\"data\":[0,1.1363982,2.2577858,-0.97645533,0.60692054],\"label\":\"PORTFOLIO\"},{\"data\":[0,-0.827159,1.3416603,-1.2099007,-0.16224937],\"label\":\"AAPL\"},{\"data\":[0,1.289034,2.3282847,-0.9585382,0.6654772],\"label\":\"GOOGL\"}],\"labels\":[\"8\\/24\",\"8\\/25\",\"8\\/26\",\"8\\/27\",\"8\\/28\"]}";
    JSONParser parser = new JSONParser();

    private JSONObject getJson(String apiResponse) throws ParseException {
        Object obj;
        obj = parser.parse(apiResponse);
        return (JSONObject)obj;
    }

    @Test
    public void testGetIntervals() {
        FinnhubGraphing x = new FinnhubGraphing();

        List<String> expectedDayLabels = new ArrayList<>();
        List<String> expectedWeekLabels = new ArrayList<>();
        List<String> expectedMonthLabels = new ArrayList<>();
        List<String> invalidResLabels = new ArrayList<>();
        
        String[] expWeek = new String[]{"8/24", "8/25", "8/26", "8/27", "8/28"};
        String[] expMon = new String[]{"1/3", "2/3", "3/3", "4/3", "5/3", "6/3"};

        for (int i = 13; i <= 23; i++) expectedDayLabels.add(Integer.toString(i));
        for (String i : expWeek) expectedWeekLabels.add(i);
        for (String i : expMon) expectedMonthLabels.add(i);
        for (int i = 0; i < 11; i++) invalidResLabels.add(expWeek[0]);

        JSONObject dayJson = null;
        JSONObject weekJson = null;
        JSONObject monthJson = null;
        try {
            dayJson = getJson(dayResponse);
            weekJson = getJson(weekResponse);
            monthJson = getJson(monthResponse);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }

        List<String> dayLabels = FinnhubGraphing.getIntervals(dayJson, Resolution.hour);
        List<String> dayLabelsInvalidres = FinnhubGraphing.getIntervals(dayJson, Resolution.week);
        List<String> weekLabels = FinnhubGraphing.getIntervals(weekJson, Resolution.day);
        List<String> monthLabels = FinnhubGraphing.getIntervals(monthJson, Resolution.month);

        Assert.assertEquals(expectedDayLabels, dayLabels);
        Assert.assertEquals(invalidResLabels, dayLabelsInvalidres);
        Assert.assertEquals(expectedWeekLabels, weekLabels);
        Assert.assertEquals(expectedMonthLabels, monthLabels);
    }

    @Test
    public void testCalculatePercentChange() {
        JSONObject data;
        JSONObject inputData;
        String inputString = "{\"datasets\":[{\"data\":[1711.0076,1730.6749,1770.6525,1753.53,1764.2375],\"label\":\"PORTFOLIO\"},{\"data\":[125.85749816895,124.82499694824,126.52249908447,125.01000213623,124.80750274658],\"label\":\"AAPL\"},{\"data\":[1585.1500244141,1605.8499755859,1644.1300048828,1628.5200195312,1639.4300537109],\"label\":\"GOOGL\"}],\"labels\":[\"8\\/24\",\"8\\/25\",\"8\\/26\",\"8\\/27\",\"8\\/28\"]}";
        try {
            data = getJson(expectedAAPLandGOOGL);
            inputData = getJson(inputString);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
            return;
        }
        JSONArray expected = (JSONArray) data.get("datasets");

        List<Map<String, Object>> datasets = (List<Map<String, Object>>) inputData.get("datasets");
        List<Map<String, Object>> transformedData = FinnhubGraphing.calculatePercentChange(datasets);
        String transformedString = JSONArray.toJSONString(transformedData);
        JSONArray response;
        try {
            response = (JSONArray) parser.parse(transformedString);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
            return;
        }
        Assert.assertEquals(expected, response);
    }

    @Test
    public void testResponsesToGraph() {
        List<String> userStocks = new ArrayList<>();
        userStocks.add("AAPL");
        userStocks.add("GOOGL");

        String emptyData = "{\"datasets\":[],\"labels\":[]}";

        JSONObject data;
        JSONObject dataEmpty;
        JSONObject weekAppleJson;
        JSONObject weekGoogleJson;
        try {
            weekAppleJson = getJson(weekApple);
            weekGoogleJson = getJson(weekGoogle);
            data = getJson(expectedAAPLandGOOGL);
            dataEmpty = getJson(emptyData);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
            return;
        }
        List<JSONObject> apiResponses = new ArrayList<>();
        apiResponses.add(weekAppleJson);
        apiResponses.add(weekGoogleJson);
        JSONObject response = FinnhubGraphing.responsesToGraph(apiResponses, userStocks,Resolution.day);

        Assert.assertEquals(data.toJSONString(), response.toJSONString());

        // empty response
        JSONObject responseEmpty = FinnhubGraphing.responsesToGraph(new ArrayList<JSONObject>(), userStocks,Resolution.day);
        Assert.assertEquals(dataEmpty.toJSONString(), responseEmpty.toJSONString());
    }

}
