package com.huihui.dataSource.pooled;

import com.huihui.dataSource.unpooled.UnpooledDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Created by hadoop on 2015/6/29 0029.
 */
public class PooledDataSource implements DataSource {

    private UnpooledDataSource dataSource;
    private PooledState state = new PooledState();

    private int maxActiveConnections = 10;
    private int maxIdleConnections = 5;
    private long maxLongConnectTime = 20000;
    private int waitTime = 20000;

    public PooledDataSource(){
        this.dataSource = new UnpooledDataSource();
    }
    public PooledDataSource(UnpooledDataSource dataSourece){
        this.dataSource = dataSourece;
    }

    public PooledDataSource(String driver,String url,String username,String password){
        this.dataSource = new UnpooledDataSource(driver, url,username,password);
    }


    public PooledConnection pushConnection(String username,String password) {
        PooledConnection connection = null;
        try {
            while (connection == null) {
                synchronized (state){
                    if (!state.idlePool.isEmpty()) {//如果idle池中不为空0
                        PooledConnection oldConnection = state.idlePool.remove(0);
                        connection = refreshOldConnetion(oldConnection);
                    } else {
                        if (state.activePool.size() < maxActiveConnections) {//如果active池没达到最大,新建一个connection
                            connection = new PooledConnection(dataSource.getConnection(username,password));
                        } else {
                            PooledConnection observerConnection = state.activePool.get(0);
                            if(observerConnection.getConnectTime()<maxLongConnectTime){//如果active池中第一个连接超时了。
                                connection = refreshOldConnetion(observerConnection);
                            }else {
                                state.wait(waitTime);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(connection!=null){
            state.activePool.add(connection);
        }
        return connection;
    }

    private PooledConnection refreshOldConnetion(PooledConnection oldConnetion) throws SQLException {
        oldConnetion.getRealConnection().rollback();
        state.activePool.remove(0);
        oldConnetion.unVaild();
        return new PooledConnection(oldConnetion.getRealConnection());
    }

    public void popConnection(PooledConnection connection) throws SQLException {
        synchronized (state){
            state.activePool.remove(connection);
            connection.unVaild();
            connection.getRealConnection().rollback();
            if(state.idlePool.size()<maxIdleConnections){//存放入idle池中
                state.idlePool.add(connection);
                state.notifyAll();
            }else{//销毁
                connection.getRealConnection().close();
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(getUsername(),getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return pushConnection(username, password).getRealConnection();
    }

    public String getDriver() {
        return this.dataSource.getDriver();
    }

    public void setDriver(String driver) {
        this.dataSource.setDriver(driver);
    }

    public String getUrl() {
        return this.dataSource.getUrl();
    }

    public void setUrl(String url) {
        this.dataSource.setUrl(url);
    }

    public String getUsername() {
        return this.dataSource.getUsername();
    }

    public void setUsername(String username) {
        this.dataSource.setUsername(username);
    }

    public String getPassword() {
        return this.dataSource.getPassword();
    }

    public void setPassword(String password) {
        this.dataSource.setPassword(password);
    }

    public PooledState getState() {
        return state;
    }

    public void setState(PooledState state) {
        this.state = state;
    }

    public UnpooledDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(UnpooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    public int getMaxActiveConnections() {
        return maxActiveConnections;
    }

    public void setMaxActiveConnections(int maxActiveConnections) {
        this.maxActiveConnections = maxActiveConnections;
    }

    public long getMaxLongConnectTime() {
        return maxLongConnectTime;
    }

    public void setMaxLongConnectTime(long maxLongConnectTime) {
        this.maxLongConnectTime = maxLongConnectTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
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
