package com.huihui.session;

import com.huihui.exceptions.ExceptionFactory;
import com.huihui.exceptions.PersistenceException;
import com.huihui.excutor.DefaultExcutor;
import com.huihui.excutor.Excutor;
import com.huihui.mapping.*;
import com.huihui.type.TypeHandler;
import org.apache.commons.beanutils.PropertyUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public PreparedStatement getPreparedStatement(Connection connection, Object[] args, BoundSQLSource boundSQL) {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(boundSQL.getSql());
            if (!boundSQL.getParameterList().isEmpty()) {//如果parameterList不为空填充参数
                if(args.length!=1)//@TODO 多参数方法。
                    throw new PersistenceException("do not support for args.length>1 method");
                int i = 1;
                if(args[0] instanceof Map){//如果能转换成map,多参数
                    Map<String,Object> map = (Map<String, Object>) args[0];
                    for (String parameter : boundSQL.getParameterList()) {
                        Object parameterValue = map.get(parameter);
                        TypeHandler handler = configuration.getTypeHandler(parameterValue.getClass());
                        handler.setParameter(pstmt, i, parameterValue, null);
                        i++;
                    }
                }else {
                    if(boundSQL.getParameterList().size()==1){
                        Object parameterValue = args[0];
                        TypeHandler handler = configuration.getTypeHandler(parameterValue.getClass());
                        handler.setParameter(pstmt, i, parameterValue, null);
                    }else{//如果需要的parameter数量不等于提供的参数。
                        throw new PersistenceException("require parameters not equals parameters");
                    }
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
        PreparedStatement pstmt = getPreparedStatement(connection, args, mappedStatement.getBoudSQL());
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
        PreparedStatement pstmt = getPreparedStatement(connection, args, mappedStatement.getBoudSQL());
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
        PreparedStatement pstmt = getPreparedStatement(connection, args, mappedStatement.getBoudSQL());
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


}
