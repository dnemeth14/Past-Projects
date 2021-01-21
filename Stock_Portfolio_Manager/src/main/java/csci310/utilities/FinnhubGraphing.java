package csci310.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.*;

public class FinnhubGraphing {
    /**
     * Takes api response and returns formatted labeled units
     * @param response
     * @param resolution
     */
    public static List<String> getIntervals(JSONObject response, Resolution resolution) {
        JSONArray intervals = (JSONArray)response.get("t");
        List<String> result = new ArrayList<String>();
        String format = "M/d";
        switch (resolution) {
            case hour:
                format = "HH";
                break;
            case day:
            case month:
                format = "M/d";
                break;
            default:
                break;
        }

        DateFormat out = new SimpleDateFormat(format);
        out.setTimeZone(TimeZone.getTimeZone("GMT"));

        for (int i = 0; i < intervals.size(); i++) {
            Long timestamp = (Long)intervals.get(i);
            Date d = new Date(timestamp*1000L);
            result.add(out.format(d));
        }
        return result;
    }

    /**
     * Takes an api response and returns array of closing values
     * @param apiResponse
     */
    private static List<Number> responseToGraph(JSONObject apiResponse) {
        JSONArray out = (JSONArray)apiResponse.get("c");
        List<Number> result = new ArrayList<>();
        for (Object num : out) {
            result.add((Number) num);
        }
        return result;
    }

    /**
     * Takes in list of maps with props: data and label
     * @param datasets
     * @return
     */
    public static List<Map<String, Object>> calculatePercentChange(List<Map<String, Object>> datasets) {
        List<Map<String, Object>> output = new ArrayList<>();
        for (Map<String, Object> dataset : datasets) {
            List<Number> data = (List<Number>) dataset.get("data");

            Map<String, Object> newDataset = new HashMap<>();
            List<Number> newData = new ArrayList<>();
            newData.add(0);

            Number previous = data.get(0);
            for (int i = 1; i < data.size(); ++i) {
                Number curr = data.get(i);
                newData.add(
                        ((curr.floatValue() - previous.floatValue()) / curr.floatValue()) * 100
                );
                previous = curr;
            }

            newDataset.put("data", newData);
            newDataset.put("label", dataset.get("label"));
            output.add(newDataset);
        }
        return output;
    }

    /**
     * Turns multiple api responses into an output json object that is ready to be
     * sent to the client
     * @param apiResponses
     * @param resolution
     */
    public static JSONObject responsesToGraph(List<JSONObject> apiResponses, List<String> userStocks, Resolution resolution) {
        JSONObject data = new JSONObject();
        List<Map<String, Object>> datasets = new ArrayList<>();

        if (apiResponses.size() == 0) {
            data.put("datasets", datasets);
            data.put("labels", new ArrayList<String>());
            return data;
        }

        List<String> labels = getIntervals(apiResponses.get(0), resolution);
        data.put("labels", labels);

        Map<String, Object> portfolio =  new HashMap<>();
        List<Number> portfolioPrices = new ArrayList<>();
        portfolio.put("label", "PORTFOLIO");
        portfolio.put("data", portfolioPrices);
        datasets.add(portfolio);

        for (int i = 0; i < userStocks.size(); i++) {
            JSONObject res = apiResponses.get(i);
            Map<String, Object> m = new HashMap<>();
            List<Number> prices = responseToGraph(res);

            // add sum to portfolio
            for (int j = 0; j < prices.size(); j++) {
                if (portfolioPrices.size() < j+1) {
                    portfolioPrices.add(0);
                }
                Number newSum = portfolioPrices.get(j).floatValue() + prices.get(j).floatValue();
                portfolioPrices.set(j, newSum);
            }

            m.put("data", prices);
            m.put("label", userStocks.get(i));
            datasets.add(m);
        }

        // get percent change instead
        datasets = calculatePercentChange(datasets);
        data.put("datasets", datasets);
        return data;
    }
}
