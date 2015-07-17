package com.huihui.session;

import com.huihui.exceptions.ExceptionFactory;
import com.huihui.exceptions.PersistenceException;
import com.huihui.excutor.DefaultExcutor;
import com.huihui.excutor.Excutor;
import com.huihui.mapping.MappedStatement;
import com.huihui.mapping.ResultMap;
import com.huihui.mapping.ResultMapping;
import com.huihui.mapping.ResultSetWrapper;
import com.huihui.mapping.sql.BoundSql;
import com.huihui.reflection.OgnlCache;
import com.huihui.type.TypeHandler;
import org.apache.commons.beanutils.PropertyUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 2015/7/4 0004.
 */
public class SqlSession {
    Configuration configuration;
    private Excutor excutor;

    public SqlSession(Configuration configuration) {
        this.configuration = configuration;
        this.excutor = new DefaultExcutor(configuration);
    }

    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public PreparedStatement getPreparedStatement(Connection connection, Object[] args, MappedStatement mappedStatement) {
        DynamincContext context = new DynamincContext(args[0]);
        BoundSql boundSql = mappedStatement.getBoudSQL(context);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(boundSql.getSql());
            if (!boundSql.getParameterList().isEmpty()) {//如果parameterList不为空填充参数
                if(args.length!=1)//@TODO 多参数方法。
                    throw new PersistenceException("do not support for args.length>1 method");
                int i=1;
                for (String parameter : boundSql.getParameterList()) {
                    Object parameterValue = OgnlCache.getValue(parameter,context.getBindings());

                    TypeHandler handler = configuration.getTypeHandler(parameterValue.getClass());
                    handler.setParameter(pstmt, i, parameterValue, null);
                    i++;
                }
            }
        } catch (SQLException e) {
            throw ExceptionFactory.wrapException("", e);
        }
        return pstmt;
    }

    /**
     * 查询单个
     */
    public Object selectOne(Connection connection, Object[] args, MappedStatement mappedStatement) {

        PreparedStatement pstmt = getPreparedStatement(connection, args, mappedStatement);
        ResultMap resultMap = mappedStatement.getResultMap();
        Object result = null;
        try {
            ResultSet resultSet = pstmt.executeQuery();
            if (mappedStatement.isUseResultMap()) {
                while (resultSet.next()) {
                    result = wrapResultMap(resultMap, resultSet);
                }
            }
        } catch (SQLException e) {
            ExceptionFactory.wrapException(" ", e);
        }
        return result;
    }

    public Object selectMany(Connection connection, Object[] args, MappedStatement mappedStatement) {
        List<Object> resultList = new ArrayList<>();
        PreparedStatement pstmt = getPreparedStatement(connection, args, mappedStatement);
        ResultMap resultMap = mappedStatement.getResultMap();
        try {
            ResultSet resultSet = pstmt.executeQuery();
            if (mappedStatement.isUseResultMap()) {
                while (resultSet.next()) {
                    Object result = wrapResultMap(resultMap, resultSet);
                    resultList.add(result);
                }
            }
        } catch (SQLException e) {
            ExceptionFactory.wrapException(" ", e);
        }

        return resultList;
    }

    public Object insert(Connection connection, Object[] args, MappedStatement mappedStatement) {
        PreparedStatement pstmt = getPreparedStatement(connection, args, mappedStatement);
        int result = 0;
        try {
            result = pstmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Object wrapResultMap(ResultMap resultMap, ResultSet resultSet) {
        Object result = null;
        try {
            result = resultMap.getType().newInstance();
            wrapResultMap(resultMap, resultSet, result);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void wrapResultMap(ResultMap resultMap, ResultSet resultSet, Object result) {
        ResultSetWrapper wrapper = new ResultSetWrapper(resultSet);
        for (ResultMapping mapping : resultMap.getResultMappings()) {
            Object value = null;
            if (mapping.getNestedMapId() == null) {//如果没有nestedMap简单设值
                value = excutor.getSimpleProperty(wrapper, mapping);
            } else {//如果有nestedMap
                ResultMap nestedMap = configuration.getResultMap(mapping.getNestedMapId());
                value = wrapResultMap(nestedMap, resultSet);
            }
            setProperty(result, mapping.getProperty(), value);
        }
    }

    private void setProperty(Object result, String property, Object value) {
        try {
            PropertyUtils.setSimpleProperty(result, property, value);
        } catch (Exception e) {
            ExceptionFactory.wrapException("connot found property ", e);
        }
    }


    public Object delete(Connection connection, Object[] args, MappedStatement statement) {
        return null;
    }

    public Object update(Connection connection, Object[] args, MappedStatement statement) {

        return null;
    }
}
