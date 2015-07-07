package com.huihui.dataSource.unpooled;

import com.huihui.io.Resources;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by hadoop on 2015/6/29 0029.
 */
public class UnpooledDataSource implements DataSource {

    private static Map<String, Driver> registeredDrivers = new ConcurrentHashMap<String, Driver>();
    private String driver;
    private String url;
    private String username;
    private String password;

    private boolean autoCommit = false;

    public UnpooledDataSource(){}

    public UnpooledDataSource(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public Connection getConnection()  {
        return doGetConnnection(username, password);
    }

    public Connection getConnection(String username, String password)  {
        return doGetConnnection(username, password);
    }

    public Connection doGetConnnection(String username, String password)  {
        Connection connection = null;
        try {
            initDriver();
            connection = DriverManager.getConnection(url, username, password);
            if(connection!=null){
                connection.setAutoCommit(autoCommit);
            }else {
                throw new SQLException("UnKown Error for connetion is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    private void initDriver() throws SQLException {
        if(!registeredDrivers.containsKey(driver)){
            try {
                Class<?> clazz = Resources.classForName(driver);
                Driver driverInstance = (Driver) clazz.newInstance();
                registeredDrivers.put(driver,driverInstance);
            } catch (Exception e) {
                e.printStackTrace();
               throw new SQLException("Cannot found driver class");
            }
        }
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setLoginTimeout(int loginTimeout) throws SQLException {
        DriverManager.setLoginTimeout(loginTimeout);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        DriverManager.setLogWriter(logWriter);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
