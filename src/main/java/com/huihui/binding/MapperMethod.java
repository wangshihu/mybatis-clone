package com.huihui.binding;

import com.huihui.exceptions.BindingException;
import com.huihui.exceptions.ExceptionFactory;
import com.huihui.mapping.MappedStatement;
import com.huihui.mapping.sql.SqlCommandType;
import com.huihui.session.Configuration;
import com.huihui.session.ErrorContext;
import com.huihui.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by hadoop on 2015/7/4 0004.
 */
public class MapperMethod {

    MethodSignature method;
    SqlConmmand conmmand;
    SqlSession sqlSession;
    Configuration configuration;

    public MapperMethod(Method method,Class<?>mapperInterface,SqlSession sqlSession){
        ErrorContext.instance().resource(mapperInterface.getName()+"excute mapperMethod");
        this.sqlSession = sqlSession;
        this.method = new MethodSignature(method);
        this.configuration = sqlSession.getConfiguration();
        conmmand  = new SqlConmmand(method,mapperInterface);
    }
    public Connection getConnection(){
        try {
            Connection connection = configuration.getDataSource().getConnection();
            return connection;
        } catch (SQLException e) {
            throw ExceptionFactory.wrapException("connot get connection", e);
        }
    }
    public Object execute(SqlSession sqlSession, Object[] args) {
        Connection connection = getConnection();
        Object result = null;
        if(conmmand.statement.getType()== SqlCommandType.INSERT){
            result = sqlSession.insert(connection,args,conmmand.statement);
        }else if(conmmand.statement.getType()==SqlCommandType.DELETE){
            result = sqlSession.delete(connection,args,conmmand.statement);
        }else if(conmmand.statement.getType()==SqlCommandType.UPDATE){
            result = sqlSession.update(connection,args,conmmand.statement);
        }else if(conmmand.statement.getType()==SqlCommandType.SELECT){
            if(method.returnsMany){
                result = sqlSession.selectMany(connection,args,conmmand.statement);
            }else {
                result = sqlSession.selectOne(connection,args,conmmand.statement);
            }
        }
        return result;
    }


    private class SqlConmmand{
        String name;
        MappedStatement statement;
        public SqlConmmand(Method method,Class<?>mapperInterface){
            name = mapperInterface.getName()+"."+method.getName();
            MappedStatement statement = configuration.getMapperStatement(name);
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

    private class MethodSignature{
        private final Method method;
        private final boolean returnsMany;
        private final boolean returnsVoid;
        private final Class<?> returnType;

        public MethodSignature(Method method) {
            this.method = method;
            this.returnType = method.getReturnType();
            this.returnsVoid = Void.class.isAssignableFrom(returnType);
            this.returnsMany = Collection.class.isAssignableFrom(returnType);
        }

        public Method getMethod() {
            return method;
        }

        public boolean isReturnsMany() {
            return returnsMany;
        }

        public boolean isReturnsVoid() {
            return returnsVoid;
        }

        public Class<?> getReturnType() {
            return returnType;
        }
    }
}
