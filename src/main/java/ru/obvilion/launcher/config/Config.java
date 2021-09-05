package ru.obvilion.launcher.config;

import java.io.*;
import java.util.Properties;

public class Config {
    public static Properties config;

    static {
        try {
            if(!Global.LAUNCHER_HOME.isDirectory()) {
                Global.LAUNCHER_HOME.mkdir();
            }

            config = new Properties();
            if(!Global.LAUNCHER_CONFIG.exists()) {
                if(Global.LAUNCHER_CONFIG.createNewFile()) {
                    System.out.println("Config created");
                    config.load(new FileInputStream(Global.LAUNCHER_CONFIG));

                    config.setProperty("login", "");
                    config.setProperty("password", "");
                    config.setProperty("token", "");
                    config.setProperty("autoLogin", "false");
                    config.setProperty("fullscreen", "false");
                    config.setProperty("savePass", "true");
                    config.setProperty("debug", "false");
                    config.setProperty("ram", "1024");
                    config.setProperty("customPath", "");
                }
            } else {
                config.load(new FileInputStream(Global.LAUNCHER_CONFIG));
            }

            OutputStream out = new FileOutputStream(Global.LAUNCHER_CONFIG);
            config.store(out, null);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setValue(String name, String desc) {
        config.setProperty(name, desc);
        try {
            OutputStream out = new FileOutputStream(Global.LAUNCHER_CONFIG);
            config.store(out, null);
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static String getValue(String name) {
        return config.getProperty(name);
    }
    public static int getIntValue(String name) {
        return Integer.parseInt(getValue(name));
    }
    public static boolean getBooleanValue(String name) {
        return Boolean.parseBoolean(getValue(name));
    }
}
