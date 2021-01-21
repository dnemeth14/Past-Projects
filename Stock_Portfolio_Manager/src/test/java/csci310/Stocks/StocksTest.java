package csci310.Stocks;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StocksTest {

    @Test
    public void testGetUserStocks() {
        Stocks s = new Stocks();
        List<String> stocksUser1Expected = new ArrayList<>();
        stocksUser1Expected.add("AAPL");
        stocksUser1Expected.add("GE");

        List<String> stocksUser1;
        try {
            stocksUser1 = Stocks.getUserStocks("1");
        } catch (SQLException sqlException) {
            Assert.fail(sqlException.getMessage());
            return;
        }
        Assert.assertEquals(stocksUser1Expected, stocksUser1);
    }

}
