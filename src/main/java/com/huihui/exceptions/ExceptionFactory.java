package com.huihui.exceptions;

/**
 * Created by hadoop on 2015/7/8 0008.
 */
public class ExceptionFactory {
    public static RuntimeException wrapException(String message, Exception e) {
        return new PersistenceException(e);
    }
}
