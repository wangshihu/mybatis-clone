package com.huihui.io;

import java.io.InputStream;

/**
 * Created by hadoop on 2015/7/7 0007.
 */
public class Resourse {
    InputStream stream;
    String name;

    public Resourse(InputStream stream, String name) {
        this.stream = stream;
        this.name = name;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return name+" ";
    }
}
