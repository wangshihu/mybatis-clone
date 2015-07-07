package com.huihui.dataSource.pooled;

import com.huihui.dataSource.unpooled.UnpooledDataSourceFactory;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory{
    public PooledDataSourceFactory(){
        this.dataSource = new PooledDataSource();
    }

}
