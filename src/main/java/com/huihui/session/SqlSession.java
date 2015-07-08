package com.huihui.session;

import com.huihui.exceptions.ExceptionFactory;
import com.huihui.exceptions.ResultMapException;
import com.huihui.mapping.*;
import com.huihui.type.JdbcType;
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

    public SqlSession(Configuration configuration) {
        this.configuration = configuration;
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
                Map<String, Object> map = (Map<String, Object>) args[0];
                int i = 1;
                for (String parameter : boundSQL.getParameterList()) {
                    Object parameterValue = map.get(parameter);
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

    public Object selectOne(Connection connection, Object[] args, MappedStatement mappedStatement) {
        PreparedStatement pstmt = getPreparedStatement(connection, args, mappedStatement.getBoudSQL());
        ResultMap resultMap = mappedStatement.getResultMap();
        Object result = null;
        try {
            ResultSet resultSet = pstmt.executeQuery();
            result = resultMap.getType().newInstance();
            if (mappedStatement.isUseResultMap()) {
                while (resultSet.next()) {
                    wrapResultForResultMap(resultMap, resultSet, result);
                }
            }
        } catch (SQLException e) {
            ExceptionFactory.wrapException(" ", e);
        } catch (Exception e) {
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
                    Object result = resultMap.getType().newInstance();
                    wrapResultForResultMap(resultMap, resultSet, result);
                    resultList.add(result);
                }
            }
        } catch (SQLException e) {
            ExceptionFactory.wrapException(" ", e);
        } catch (Exception e) {
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


    public void wrapResultForResultMap(ResultMap resultMap, ResultSet resultSet, Object result) {
        try {
            ResultSetWrapper wrapper = new ResultSetWrapper(resultSet);

            for (ResultMapping mapping : resultMap.getResultMappings()) {
                int columnIndex = wrapper.getResultColumns().indexOf(mapping.getColum());
                if (columnIndex == -1)
                    throw new ResultMapException("connot found column");

                JdbcType jdbcType = wrapper.getJdbcTypes().get(columnIndex);
                Class<?> typeClass = configuration.getJdbcTypeClass(jdbcType);
                TypeHandler handler = configuration.getTypeHandler(typeClass);

                Object value = handler.getResult(wrapper.getResultSet(), mapping.getColum());
                setProperty(result, mapping.getProperty(), value);
            }

        } catch (SQLException e) {
            ExceptionFactory.wrapException(" ", e);
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
