package com.xiaolyuh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.Date;

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
