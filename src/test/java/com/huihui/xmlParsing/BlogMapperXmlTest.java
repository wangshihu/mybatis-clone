package com.huihui.xmlParsing;

import com.huihui.io.Resources;
import com.huihui.io.Resourse;
import com.huihui.parser.MapperParser;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class BlogMapperXmlTest {
    @Test
    public void testMapper() throws IOException {
        String resourceName = "com/huihui/xmlParsing/blogMapper.xml";
        Resourse resourse = new Resourse(Resources.getResourceAsStream(resourceName),resourceName);
        MapperParser parser = new MapperParser(resourse);
        parser.parsing();
    }
}
