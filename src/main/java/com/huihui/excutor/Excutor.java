package com.huihui.excutor;

import com.huihui.mapping.ResultMapping;
import com.huihui.mapping.ResultSetWrapper;

/**
 * Created by hadoop on 2015/7/12 0012.
 */
public interface Excutor {
     Object getSimpleProperty(ResultSetWrapper wrapper, ResultMapping mapping);
}
