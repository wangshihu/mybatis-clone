package com.huihui.dataSource.unpooled;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by hadoop on 2015/6/29 0029.
 */
public class UnpooledDataSourceTest {
    @Test
    public void testConnection() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/mybatis";
        String username = "root";
        String password = "root";
        String driver = "com.mysql.jdbc.Driver";
        UnpooledDataSource dataSource = new UnpooledDataSource(driver,url,username,password);
        Connection connection = dataSource.getConnection();
        Assert.assertNotNull(connection);
    }
}
