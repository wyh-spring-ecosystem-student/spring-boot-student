package com.xiaolyuh.jdbc;

import com.alibaba.fastjson.JSON;
import com.xiaolyuh.BaseTest;
import com.xiaolyuh.domain.model.Person;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yuhao.wang3
 * @since 2019/10/12 17:23
 */
public class JdbcDemoTest extends BaseTest {

    @Test
    public void testJdbcDemo() {
        String sql = "select `id`, `name`, `age`, `address` from person where name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {
            // 注册 JDBC 驱动
            Class.forName("org.h2.Driver");

            // 打开连接
            conn = DriverManager.getConnection("jdbc:h2:mem:ssb_test", "root", "root");
            initH2db(conn);

            // 创建 Statement
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "wyf");

            // 执行查询
            resultSet = stmt.executeQuery();

            // 处理结果
            // 展开结果集数据库
            List<Person> peoples = new ArrayList<>();
            while (resultSet.next()) {
                Person person = new Person();
                // 通过字段检索
                person.setId(resultSet.getLong("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setAddress(resultSet.getString("address"));
                peoples.add(person);
            }
            System.out.println(JSON.toJSONString(peoples));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (Objects.nonNull(resultSet)) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (Objects.nonNull(stmt)) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (Objects.nonNull(conn)) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
