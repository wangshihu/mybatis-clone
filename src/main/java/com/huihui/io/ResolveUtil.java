package com.huihui.io;

import com.huihui.exceptions.ExceptionFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 2015/7/9 0009.
 */
public class ResolveUtil {
    /**
     * 解析package 获得Package下面所有Class
     * @param packageName
     * @return
     */
    public static List<Class>  resolvePackage(String packageName) {
        List<Class> list=null;
        try {
            String path = packageName.replace('.', '/');
            File file = Resources.getResourceAsFile(path);
            list = findClasses(file,packageName);
        } catch (IOException e) {
            ExceptionFactory.wrapException("may not found package "+ packageName,e);
        } catch (ClassNotFoundException e) {
            ExceptionFactory.wrapException("may not found Class " , e);
        }
        return list;
    }
    private static List<Class> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 递归查找文件夹【即对应的包】下面的所有文件
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + '.'
                        + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName
                        + "."
                        + file.getName().substring(0,
                        file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
