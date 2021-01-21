package csci310;

import csci310.loginsignup.Signup;

import java.sql.*;

public class User {
    private static String dbUser = "root";
    private static String dbPassword = ForEmily.dbPassword;

    public static int getIdByUsername(String username) throws SQLException {
        //Connecting to SQL database
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;


        //Get database connection
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/stocks?user=" + dbUser + "&password=" + dbPassword);
        st = connection.prepareStatement("SELECT userId FROM Users WHERE username=?");
        st.setString(1, username);
        rs = st.executeQuery();

        if(rs.next()) {
            //Login
            int ret = rs.getInt(1);
            connection.close();
            st.close();
            rs.close();
            return ret;
        }
        else {
            connection.close();
            st.close();
            rs.close();
            return -1;
        }
    }
}
