package com.huihui.mapper.dao;

import com.huihui.domain.Blog;

import java.util.List;
import java.util.Map;

/**
 * Created by hadoop on 2015/6/28 0028.
 */
public interface BlogMapper {
    public void insert(Map<String,Object> map);

    public List<Blog> findAll();

    public Blog findById(Map<String, Object> map);
}
