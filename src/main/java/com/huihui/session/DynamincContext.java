package com.huihui.session;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2015/7/15 0015.
 */
public class DynamincContext {

    private final ContextMap bindings;



    public DynamincContext(Object parameterObject) {
        this.bindings = new ContextMap(parameterObject);
    }

    private StringBuilder sqlBuffer =new StringBuilder();

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void binding(String key,Object value){
        bindings.put(key, value);
    }

    public void appendSql(String content) {
        sqlBuffer.append(" ");
        sqlBuffer.append(content);
    }

    public StringBuilder getSqlBuffer() {
        return sqlBuffer;
    }



    static class ContextMap extends HashMap<String, Object> {
        private static final long serialVersionUID = 2977601501966151582L;

        private Object parameterObject;
        public ContextMap(Object parameterObject) {
            this.parameterObject = parameterObject;
        }

        @Override
        public Object get(Object key) {
            String strKey = (String) key;
            if (super.containsKey(strKey)) {
                return super.get(strKey);
            }

            if (parameterObject != null&&parameterObject instanceof Map) {
                return ((Map) parameterObject).get(key);
            }
            return null;
        }
    }
}
