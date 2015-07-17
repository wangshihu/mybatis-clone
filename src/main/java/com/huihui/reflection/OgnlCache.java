package com.huihui.reflection;

import com.huihui.exceptions.BuilderException;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2015/7/16 0016.
 */
public class OgnlCache {
    private static final Map<String, Object> cache = new HashMap<>();

    public static Object getValue(String expression, Object parameterObject) {
        try {
            OgnlContext context = new OgnlContext();
            Object expressionObj = getExpression(expression);
            return Ognl.getValue(expressionObj, context, parameterObject);
        } catch (OgnlException e) {
            throw new BuilderException("Error evaluating expression '" + expression + "'. Cause: " + e, e);
        }


    }

    public static Object getExpression(String expression) throws OgnlException {
        Object result = cache.get(expression);
        if (result == null) {
            result = Ognl.parseExpression(expression);
        }
        return result;
    }


}
