package ru.obvilion.launcher.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import ru.obvilion.json.JSONObject;
import ru.obvilion.launcher.api.API;
import ru.obvilion.launcher.api.Request;
import ru.obvilion.launcher.client.Loader;
import ru.obvilion.launcher.config.Global;
import ru.obvilion.launcher.config.Vars;
import ru.obvilion.launcher.utils.FileUtil;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LoadingController implements Initializable {
    public Label percent;
    public Pane loaded;
    public Label now;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Vars.loadingController = this;

        new Thread(() -> {
            // TODO: name fix
            new Loader(Vars.clientName, 1).load();
        }).start();

        update();
    }

    public static void update() {
        Request req = new Request(API.APILink + "files/clients");
        JSONObject obj = req.connectAndGetJSON();

        AtomicLong size = new AtomicLong();

        obj.getJSONArray("files").forEach(o -> {
            JSONObject file = new JSONObject(o.toString());
            String name = file.getString("name");
            if(name.equals(Vars.clientName)) {
                size.set(file.getLong("size"));
            }
        });

        new Thread(() -> {
            while (true) {
                int res = (int) ((float) FileUtil.folderSize(new File(Global.LAUNCHER_CLIENTS, Vars.clientName)) / size.get() * 100);

                updateProgress(res);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void updateProgress(int progress) {
        final AtomicInteger proc = new AtomicInteger(progress);
        if(progress>100) proc.set(100);
        final float length = 11.08f * proc.get();
        Platform.runLater(() -> {
            Vars.loadingController.percent.setText(proc.get() + "%");
            Vars.loadingController.loaded.setPrefWidth(length);
        });
    }
}
