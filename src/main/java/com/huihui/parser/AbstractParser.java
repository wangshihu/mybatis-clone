package com.huihui.parser;

import java.io.InputStream;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public abstract class AbstractParser {
    protected InputStream resource;

    public AbstractParser(InputStream resource) {
        this.resource = resource;
    }

    public InputStream getResource() {
        return resource;
    }

    public void setResource(InputStream resource) {
        this.resource = resource;
    }
    public abstract void parser();
}
