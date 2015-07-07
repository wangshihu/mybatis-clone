package com.huihui.dataSource.pooled;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 2015/6/29 0029.
 */
public class PooledDataSourceTest {
    @Test
    public void testPoolConnection() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/mybatis";
        String username = "root";
        String password = "root";
        String driver = "com.mysql.jdbc.Driver";
        PooledDataSource dataSource = new PooledDataSource(driver, url,username,password);
        int size = 10;
        List<Connection> list = new ArrayList<>(10);
        for(int i=0;i<size;i++){
            list.add(dataSource.getConnection());
        }
        for(int i=0;i<size;i++){
            Assert.assertEquals(list.get(i),dataSource.getConnection());
        }
    }
}
