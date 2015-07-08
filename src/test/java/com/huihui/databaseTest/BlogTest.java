package com.huihui.databaseTest;


import com.huihui.domain.Blog;
import com.huihui.io.Resources;
import com.huihui.mapper.dao.BlogMapper;
import com.huihui.session.SqlSession;
import com.huihui.session.SqlSessionFactory;
import com.huihui.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadoop on 2015/6/28 0028.
 */
public class BlogTest {

    SqlSession session;
    BlogMapper mapper ;
    @Before
    public void init() throws IOException {
        String resource = "com/huihui/config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        mapper = session.getMapper(BlogMapper.class);
    }
    @Test
    public void testAdd(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("title","年华5");
        map.put("author_id", 5);
        mapper.insert(map);
    }
    @Test
    public void testSelect(){
        List<Blog> list = mapper.findAll();
        System.out.println(list);
    }
    @Test
    public void testSelectOne(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",2);
        Blog blog = mapper.findById(map);
        System.out.println(blog);
    }


}
