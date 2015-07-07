package com.huihui.session;

import com.huihui.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hadoop on 2015/7/1 0001.
 */
public class SessionFactoryTest {
    @Test
    public void testInit() throws IOException {
        InputStream resource = Resources.getResourceAsStream("com/huihui/config.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactory(resource);
        System.out.println(sessionFactory);
    }
}
