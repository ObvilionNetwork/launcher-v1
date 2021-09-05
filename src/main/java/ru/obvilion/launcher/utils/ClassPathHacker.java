package ru.obvilion.launcher.utils;

import java.io.IOException;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.net.URL;

public class ClassPathHacker {
    public static void addFile(File f) throws IOException {
        addURL(f.toURL());
    }

    public static void addURL(URL u) {
        try {
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, u);
        } catch (Exception e) {
            System.out.println("Error adding FX lib");
        }
    }
}