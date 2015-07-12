package com.huihui.excutor;

import com.huihui.exceptions.ExceptionFactory;
import com.huihui.exceptions.ResultMapException;
import com.huihui.mapping.ResultMapping;
import com.huihui.mapping.ResultSetWrapper;
import com.huihui.session.Configuration;
import com.huihui.type.JdbcType;
import com.huihui.type.TypeHandler;

import java.sql.SQLException;

/**
 * Created by hadoop on 2015/7/12 0012.
 */
public class DefaultExcutor implements Excutor {
    private Configuration configuration;

    public DefaultExcutor(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public Object getSimpleProperty(ResultSetWrapper wrapper, ResultMapping mapping) {
        Object value = null;
        try {
            int columnIndex = wrapper.getResultColumns().indexOf(mapping.getColum());
            if (columnIndex == -1)
                throw new ResultMapException("connot found column");
            JdbcType jdbcType = wrapper.getJdbcTypes().get(columnIndex);
            Class<?> typeClass = configuration.getJdbcTypeClass(jdbcType);
            TypeHandler handler = configuration.getTypeHandler(typeClass);
            value = handler.getResult(wrapper.getResultSet(), mapping.getColum());

        } catch (SQLException e) {
            ExceptionFactory.wrapException(" ", e);
        }
        return value;
    }
}
