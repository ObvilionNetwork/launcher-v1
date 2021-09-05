package ru.obvilion.launcher.utils;

import ru.obvilion.launcher.gui.GUI;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Lang {
    public static String get(String nameStr) {
        String out;
        FileInputStream fileInputStream;
        Properties prop = new Properties();
        try {
            prop.load(GUI.class.getResource("ru.properties").openStream());
            out = prop.getProperty(nameStr);

            if (out != null) {
                out = new String(out.getBytes("ISO-8859-1"), "UTF-8");
            } else {
                out = "LANG_ERROR("+nameStr+")";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "LANG_ERROR";
        }
        return out;
    }
}
