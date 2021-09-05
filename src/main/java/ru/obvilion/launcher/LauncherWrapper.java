package ru.obvilion.launcher;

import ru.obvilion.launcher.api.API;
import ru.obvilion.launcher.config.Global;
import ru.obvilion.launcher.config.Vars;
import ru.obvilion.launcher.gui.GUI;
import ru.obvilion.launcher.utils.ClassPathHacker;
import ru.obvilion.launcher.utils.FileUtil;
import ru.obvilion.launcher.utils.RichPresence;

import java.io.File;

public class LauncherWrapper {
    public static void main(String[] args) throws Exception {
        System.setProperty("console.encoding","Cp866");
        System.setProperty("prism.lcdtext", "false");

        try {
            Class.forName("javafx.application.Platform").getClass();
        } catch( ClassNotFoundException e ) {
            System.out.println("JavaFX not found!");

            File javaFXPath = new File(Global.LAUNCHER_HOME, "javafx");
            javaFXPath.mkdir();

            String[] libraries = { "graphics", "base", "controls", "fxml"  };
            for (String libName : libraries) {
                File lib = new File(javaFXPath, "javafx." + libName + ".jar");

                if (!lib.exists()) {
                    System.out.println("Downloading " + libName);
                    FileUtil.downloadFromURL2(API.APILink + "files/javafx/" + libName + ".jar", lib.toPath());
                    System.out.println("Downloaded! Size: " + lib.length());
                }
                ClassPathHacker.addFile(lib);
            }
        }

        new Thread(() -> {
            Vars.presence = new RichPresence();
            Vars.presence.updateTimestamp();
        }).start();

        GUI.main(args);

        System.out.println("Bye!");
    }
}
