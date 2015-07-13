package com.huihui.databaseTest;

import com.huihui.domain.Author;
import com.huihui.io.Resources;
import com.huihui.mapper.dao.AuthorMapper;
import com.huihui.session.SqlSession;
import com.huihui.session.SqlSessionFactory;
import com.huihui.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hadoop on 2015/7/13 0013.
 */
public class AuthorTest {
    SqlSession session;
    AuthorMapper mapper ;
    @Before
    public void init() throws IOException {
        String resource = "com/huihui/config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(AuthorMapper.class);
    }
    @Test
    public void testOneParameter(){
        int id = 1;
        Author author = mapper.testOneParameter(id);
        Assert.assertEquals("id==1",author.getId(),1);

    }
}
