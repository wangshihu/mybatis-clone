package com.huihui.mapping.sql;

import com.huihui.domain.Author;
import com.huihui.io.Resources;
import com.huihui.io.Resourse;
import com.huihui.mapping.MappedStatement;
import com.huihui.parser.MapperParser;
import com.huihui.session.Configuration;
import com.huihui.session.DynamincContext;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadoop on 2015/7/15 0015.
 */
public class DynamicSqlTest {
    Configuration configuration ;
    String namespace = "com.huihui.mapping.sql.DynamicSqlTest";
    DynamincContext context;
    @Before
    public void Init() throws IOException {
        configuration = new Configuration();
        String path = "com\\huihui\\mapping\\sql\\dynamic.xml";
        Resourse resource = new Resourse(Resources.getResourceAsStream(path),path);
        MapperParser parser = new MapperParser(resource,configuration);
        parser.parsing();

    }



    @Test
    public void testSetIf()  {
        String suffer = ".testSetIf";
        Map<String,Object> map = new HashMap<>();
        map.put("username","dmh123");
        map.put("password","1234");
        context = new DynamincContext(map);

        MappedStatement mappedStatement = configuration.getMapperStatement(namespace+suffer);

        BoundSql boundSql = mappedStatement.getBoudSQL(context);
        System.out.println(boundSql.getSql());
    }
    @Test
    public void testIfSetWhere()  {
        String suffer = ".testIfSetWhere";
        Map<String,Object> map = new HashMap<>();
        map.put("username","dmh123");
        map.put("password","1234");
        map.put("tf","1");
        map.put("tw","2.0");
        map.put("id","2.0");
        context = new DynamincContext(map);
        MappedStatement mappedStatement = configuration.getMapperStatement(namespace+suffer);
        BoundSql boundSql = mappedStatement.getBoudSQL(context);
        System.out.println(boundSql.getSql());
    }
    @Test
    public void testForEach(){
        String suffer = ".testForEach";
        List<Author> authors = new ArrayList<>();
        for(int i=0;i<10;i++){
            authors.add(new Author(String.valueOf(i),"d"+i,i));
        }
        Map<String,Object> map = new HashMap<>();
        map.put("authors",authors);
        context = new DynamincContext(map);
        MappedStatement mappedStatement = configuration.getMapperStatement(namespace+suffer);
        BoundSql boundSql = mappedStatement.getBoudSQL(context);
        System.out.println(boundSql.getSql());
    }

}
