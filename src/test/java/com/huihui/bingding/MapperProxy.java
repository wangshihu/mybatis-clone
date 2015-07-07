package com.huihui.bingding;

import com.huihui.io.Resources;
import com.huihui.mapper.dao.BlogMapper;
import com.huihui.session.SqlSessionFactory;
import com.huihui.session.SqlSession;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hadoop on 2015/7/5 0005.
 */
public class MapperProxy {
    @Test
    public void testMapperProxy() throws IOException {
        InputStream resource = Resources.getResourceAsStream("com/huihui/config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactory(resource);
        SqlSession session  = sessionFactory.openSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);
        System.out.println(mapper);
    }
}
