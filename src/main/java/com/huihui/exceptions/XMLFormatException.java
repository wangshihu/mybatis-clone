package com.huihui.exceptions;

/**
 * Created by hadoop on 2015/7/1 0001.
 */
public class XMLFormatException extends RuntimeException {
    private static final long serialVersionUID = -7537392564877271L;

    public XMLFormatException() {
        super();
    }

    public XMLFormatException(String message) {
        super(message);
    }

    public XMLFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLFormatException(Throwable cause) {
        super(cause);
    }
}
