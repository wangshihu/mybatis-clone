package com.huihui.session;

/**
 * Created by hadoop on 2015/7/4 0004.
 */
public class SqlSession {
    Configuration configuration;

    public SqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    public <T> T getMapper(Class<T> type){
        return configuration.getMapper(type,this);
    }

    public Configuration getConfiguration() {
        return configuration;
    }


}
