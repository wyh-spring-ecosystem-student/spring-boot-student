package com.xiaolyuh;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * @author yuhao.wang3
 * @since 2019/10/12 17:23
 */
public class BaseTest {

    protected void initH2db(Connection conn) throws SQLException, ClassNotFoundException, URISyntaxException {
        Statement st = null;
        try {
            String schema = getClass().getResource("/db/schema.sql").toURI().toString().substring(6);
            String data = getClass().getResource("/db/data.sql").toURI().toString().substring(6);
            // 注册 JDBC 驱动
            Class.forName("org.h2.Driver");

            // 打开连接
            conn = DriverManager.getConnection("jdbc:h2:mem:ssb_test", "root", "root");

            st = conn.createStatement();

            // 这一句可以不要
            st.execute("drop all objects;");

            // 执行初始化语句
            st.execute("runscript from '" + schema + "'");
            st.execute("runscript from '" + data + "'");
        } finally {
            if (Objects.nonNull(st)) {
                st.close();
            }
        }
    }

}
