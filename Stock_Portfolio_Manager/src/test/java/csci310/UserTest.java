package csci310;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class UserTest {
    @Test
    public void testGetIdByUsername() {
        int id = 0;
        User user = new User();
        try {
            id = User.getIdByUsername("test");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            Assert.fail();
            return;
        }
        Assert.assertEquals(1, id);

        int badId = 0;
        try {
            badId = User.getIdByUsername("hdajsdhfakjsdhakjs");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            Assert.fail();
            return;
        }
        Assert.assertEquals(-1, badId);
    }
}
