package com.huihui.binding;

import com.huihui.exceptions.BindingException;
import com.huihui.mapping.BoundSQLSource;
import com.huihui.mapping.MappedStatement;
import com.huihui.mapping.SqlCommandType;
import com.huihui.session.SqlSession;
import com.huihui.type.TypeHandler;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by hadoop on 2015/7/4 0004.
 */
public class MapperMethod {
    Method method;
    SqlConmmand conmmand;
    SqlSession sqlSession;

    public MapperMethod(Method method,Class<?>mapperInterface,SqlSession sqlSession){
        this.sqlSession = sqlSession;
        this.method = method;
        conmmand  = new SqlConmmand(method,mapperInterface);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {

        Object result = null;
        if(conmmand.statement.getType()== SqlCommandType.INSERT){
            result = doInsert(args);
        }else if(conmmand.statement.getType()==SqlCommandType.SELECT){
            result = doSelect(args);
        }
        return result;
    }

    private Object doSelect(Object[] args) {
        DataSource dataSource = sqlSession.getConfiguration().getDataSource();
        Connection connection = null;

        Map<String,Object> map = (Map<String, Object>) args[0];
        BoundSQLSource boundSQL = conmmand.statement.getBoudSQL();

        Class<?> returnType = method.getReturnType();
        Object result = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement(boundSQL.getSql()) ;
            int i=1;
            for(String parameter:boundSQL.getParameterList()){
                Object parameterValue = map.get(parameter);
                TypeHandler handler = sqlSession.getConfiguration().getTypeHandler(parameterValue.getClass());
                handler.setParameter(pstmt,i,parameterValue,null);
                i++;
            }
            pstmt.execute();
            ResultSet resultSet = pstmt.getResultSet();

            result = returnType.newInstance();

            

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e){

        }
        return result;

    }


    private Object doInsert(Object[] args) {
        DataSource dataSource = sqlSession.getConfiguration().getDataSource();
        Connection connection = null;

        Map<String,Object> map = (Map<String, Object>) args[0];
        BoundSQLSource boundSQL = conmmand.statement.getBoudSQL();
        try {
            connection = dataSource.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(boundSQL.getSql()) ;
            int i=1;
            for(String parameter:boundSQL.getParameterList()){
                Object parameterValue = map.get(parameter);
                TypeHandler handler = sqlSession.getConfiguration().getTypeHandler(parameterValue.getClass());
                handler.setParameter(pstmt,i,parameterValue,null);
                i++;
            }
            pstmt.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private class SqlConmmand{
        String name;
        MappedStatement statement;
        public SqlConmmand(Method method,Class<?>mapperInterface){
            name = mapperInterface.getName()+"."+method.getName();
            MappedStatement statement = sqlSession.getConfiguration().getMapperStatement(name);
            if(statement==null){
                throw new BindingException("Connot binding "+name+" for mapperStatement");
            }
            this.statement = statement;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public MappedStatement getStatement() {
            return statement;
        }

        public void setStatement(MappedStatement statement) {
            this.statement = statement;
        }
    }
}
