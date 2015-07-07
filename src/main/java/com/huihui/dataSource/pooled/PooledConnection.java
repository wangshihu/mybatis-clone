package com.huihui.dataSource.pooled;

import java.sql.Connection;

/**
 * Created by hadoop on 2015/6/29 0029.
 */
public class PooledConnection {
    private Connection realConnection;
    private long connectTimeStamp;
    private boolean isVaild;

    public boolean isVaild() {
        return isVaild;
    }

    public void unVaild() {
        this.isVaild = false;
    }

    public PooledConnection(Connection realConnection) {
        this.realConnection = realConnection;
        this.isVaild = true;
        this.connectTimeStamp = System.currentTimeMillis();
    }

    public long getConnectTime(){
        return System.currentTimeMillis()-connectTimeStamp;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public void setRealConnection(Connection realConnection) {
        this.realConnection = realConnection;
    }
}
