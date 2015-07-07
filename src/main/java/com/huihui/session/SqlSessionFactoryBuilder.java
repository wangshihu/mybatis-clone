package com.huihui.session;

import java.io.InputStream;

/**
 * Created by hadoop on 2015/7/6 0006.
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream inputStream) {
        return new SqlSessionFactory(inputStream);
    }
}
