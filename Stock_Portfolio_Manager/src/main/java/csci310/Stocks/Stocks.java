package csci310.Stocks;

import csci310.ForEmily;
import csci310.calcvalue.CalcValue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class Stocks {
    private static String dbUser = "root";
    private static String dbPassword = ForEmily.dbPassword;

    public static List<String> getUserStocks(String userId) throws SQLException {
        int userIdInt = Integer.parseInt(userId);
        Hashtable<String, Integer> portfolio = new Hashtable<>();

        CalcValue.getBuyShares(userIdInt, portfolio);
        CalcValue.getSellShares(userIdInt, portfolio);

        return Collections.list(portfolio.keys());
    }
}
