package com.huihui.exceptions;

import com.huihui.session.ErrorContext;

/**
 * Created by hadoop on 2015/7/8 0008.
 */
public class ExceptionFactory {
    public static RuntimeException wrapException(String message, Exception e) {
        throw  new PersistenceException(ErrorContext.instance().message(message).cause(e).toString(), e);
    }
}
