package com.huihui.session;

import com.huihui.parser.ConfigurationParser;

import java.io.InputStream;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class SqlSessionFactory {
    private Configuration configuration;
    private InputStream resourece;
    public SqlSessionFactory(InputStream resourece){
        this.resourece = resourece;
        configuration = new Configuration();
        initConfiguration();
    }

    private void initConfiguration() {
        new ConfigurationParser(resourece,configuration).parsing();
    }

    public SqlSession openSession(){
        return new SqlSession(configuration);
    }

}
