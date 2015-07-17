package com.huihui.mapper.dao;

import com.huihui.domain.Author;

import java.util.Map;

/**
 * Created by hadoop on 2015/7/9 0009.
 */
public interface AuthorMapper {
    Author testOneParameter(int id);
    void testInsertList(Map<String,Object> map);
}
