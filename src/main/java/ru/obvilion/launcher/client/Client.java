package ru.obvilion.launcher.client;

import javafx.application.Platform;
import ru.obvilion.json.JSONObject;
import ru.obvilion.launcher.config.Config;
import ru.obvilion.launcher.config.Global;
import ru.obvilion.launcher.config.Vars;
import ru.obvilion.launcher.gui.GUI;
import ru.obvilion.launcher.utils.Lang;
import ru.obvilion.launcher.utils.StreamGobbler;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    public String name;
    public String version;
    public String core;
    public File clientDir;

    public Client(String name, JSONObject info) {
        this.name = name;
        this.version = info.getString("version");
        this.core = info.getString("core");
        clientDir = new File(Global.LAUNCHER_CLIENTS, name);
    }

    public String getCmd() {
        String cmd = "java ";
        cmd += "-Xms300m "; // Минимальное колл-во озу
        cmd += "-Xmx" + Config.getIntValue("ram") + "m "; // Максимальное колл-во озу
        cmd += "-Djava.library.path=" + new File(clientDir, "natives").getPath() + " ";
        cmd += "-cp " + new Classpath(new File(clientDir, "libraries")).getCmd() + new File(clientDir, "forge.jar").getPath() + ";" + new File(clientDir, "minecraft.jar").getPath() + " ";
        cmd += "-Duser.language=en ";

        cmd += "net.minecraft.launchwrapper.Launch ";

        cmd += "--username " + Config.getValue("login") + " ";
        cmd += "--version " + this.core + " " + this.version + " ";
        cmd += "--gameDir " + clientDir.getPath() + " ";
        cmd += "--assetsDir " + new File(Global.LAUNCHER_CLIENTS, "asset" + this.version).getPath() + " ";
        cmd += "--assetIndex " + this.version + " ";
        cmd += "--uuid " + Config.getValue("uuid") + " "; // Айди, выдается при авторизации
        cmd += "--accessToken " + Config.getValue("token") + " "; // Токен доступа, выдается при авторизации
        cmd += "--userProperties [] ";
        cmd += "--userType legacy ";
        cmd += "--tweakClass cpw.mods.fml.common.launcher.FMLTweaker ";
        return cmd;
    }

    public void run() {
        final String cmd = getCmd();
        final AtomicInteger exit = new AtomicInteger(-1);

        Vars.presence.updateDescription(Lang.get("player").replace("{0}", Config.getValue("login")));
        Vars.presence.updateState(Lang.get("server1").replace("{0}", name));

        Process ps = null;
        System.out.println(cmd);

        Platform.runLater(() -> {
            GUI.stage.close();
        });

        try {
            ps = Runtime.getRuntime().exec(cmd, null, clientDir);
        } catch (IOException e) {
            e.printStackTrace();
        }


        StreamGobbler errorGobbler = new StreamGobbler(ps.getErrorStream(), "MC");
        StreamGobbler outputGobbler = new StreamGobbler(ps.getInputStream(), "MC");

        errorGobbler.start();
        outputGobbler.start();

        try {
            exit.set(ps.waitFor()); // Ждем когда майн закроется

            errorGobbler.stop();
            outputGobbler.stop();

            Platform.runLater(GUI::loadMain);
            if (!System.getProperty("java.version").startsWith("1.8")) {
                System.out.println("This java version is not supported on minecraft!");
                Platform.runLater(() -> {
                    Vars.mainController.news_label.setText(Lang.get("javaNotSupported").replace("{0}", System.getProperty("java.version")));
                    Vars.mainController.news.setText("");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Classpath {
    public File dir;

    public Classpath(File dir) {
        this.dir = dir;
    }

    public String getCmd() {
        String cmd = "";

        for(File f : dir.listFiles()) {
            if(f.isDirectory()) {
                cmd += new Classpath(f).getCmd();
            } else {
                cmd += f.getPath() + ";";
            }
        }

        return cmd;
    }
}
