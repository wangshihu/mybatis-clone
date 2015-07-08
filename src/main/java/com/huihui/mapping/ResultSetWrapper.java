package com.huihui.mapping;

import com.huihui.type.JdbcType;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ResultSet的包装类
 * Created by hadoop on 2015/7/8 0008.
 */
public class ResultSetWrapper {
    private ResultSet resultSet;
    private List<JdbcType> jdbcTypes;
    private List<String> resultColumns;
    public ResultSetWrapper(ResultSet resultSet) {
        this.resultSet = resultSet;
        jdbcTypes = new ArrayList<>();
        resultColumns = new ArrayList<>();
        try {
            ResultSetMetaData metaData =  resultSet.getMetaData();
            int count = metaData.getColumnCount();
            for(int i=1;i<=count;i++){
                jdbcTypes.add(JdbcType.forCode(metaData.getColumnType(i)));
                resultColumns.add(metaData.getColumnName(i));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getResultColumns() {
        return resultColumns;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public List<JdbcType> getJdbcTypes() {
        return jdbcTypes;
    }
}
