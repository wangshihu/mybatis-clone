package com.huihui.xmlParsing;

import com.huihui.io.Resources;
import com.huihui.parser.MapperParser;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class BlogMapperXmlTest {
    @Test
    public void testMapper() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("com/huihui/xmlParsing/blogMapper.xml");
        MapperParser parser = new MapperParser(inputStream);
        parser.parsing();
    }
}
