package com.xiaolyuh;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Test;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CreateConnectionTests {

    @Test
    public void testDataSource() throws Exception {
        String sql = "select * from person";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;

        long beforeTimeOffset = -1L; //创建Connection对象前时间
        long afterTimeOffset = -1L; //创建Connection对象后时间
        long executeTimeOffset = -1L; //创建Connection对象后时间

        try {
            Class.forName("com.mysql.jdbc.Driver");
            beforeTimeOffset = new Date().getTime();
            System.out.println("before:\t" + beforeTimeOffset);
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ssb_test", "root", "root");
            afterTimeOffset = new Date().getTime();
            System.out.println("after:\t\t" + afterTimeOffset);
            System.out.println("Create Costs:\t\t" + (afterTimeOffset - beforeTimeOffset) + " ms");

            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();
            executeTimeOffset = new Date().getTime();
            System.out.println("Exec Costs:\t\t" + (executeTimeOffset - afterTimeOffset) + " ms");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

    }

}
