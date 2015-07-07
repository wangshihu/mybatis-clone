package com.huihui.dataSource.unpooled;

import com.huihui.dataSource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {
    protected DataSource dataSource;
    private Properties properties;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }


    @Override
    public void setProperties(Properties props) {
        this.properties = props;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
