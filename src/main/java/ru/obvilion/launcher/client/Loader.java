package ru.obvilion.launcher.client;

import ru.obvilion.json.JSONArray;
import ru.obvilion.json.JSONObject;
import ru.obvilion.launcher.api.API;
import ru.obvilion.launcher.api.Request;
import ru.obvilion.launcher.config.Global;
import ru.obvilion.launcher.config.Vars;
import ru.obvilion.launcher.utils.DownloadDirUtil;
import ru.obvilion.launcher.utils.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicLong;

public class Loader {
    public String name;
    public int id;
    public File clientPath;

    public Loader(String name, int id) {
        this.name = name;
        this.id = id;
        this.clientPath = new File(Global.LAUNCHER_CLIENTS, name);

        clientPath.mkdir();
    }

    public void getPercent() {

    }

    public void load() {
        JSONObject client = API.getClientFile(name);
        if(client.has("error")) {
            Vars.mainController.title_o.setText(client.getString("error"));
            return;
        }

        JSONArray resArr = client.getJSONArray("files");
        resArr.forEach(obj -> {
            JSONObject file = new JSONObject(obj.toString());

            String name = file.getString("name");
            if(name.endsWith(".jar") || name.endsWith(".dat") || name.endsWith(".json")) {
                File clientFile = new File(clientPath, name);

                if(!clientFile.exists() || clientFile.length() != file.getLong("size")) {
                    FileUtil.downloadFromURL(
                            API.APILink + "files/clients/" + this.name + "/" + name,
                            new File(clientPath, name).toPath()
                    );
                }
                return;
            }

            File dir = new File(clientPath, name.replace(".zip", ""));
            dir.mkdir();

            if(name.equals("config.zip")) {
                if(!dir.exists() || new File(clientPath, name.replace(".zip", "/temp.zip")).length() != file.getLong("size")) {
                    FileUtil.downloadAndUnpackFromURL(
                        API.APILink + "files/clients/" + this.name + "/" + name,
                        new File(clientPath, name.replace(".zip", "")).toPath()
                    );
                }
            }

            if(name.equals("mods") || name.equals("libraries") || name.equals("natives")) {
                new DownloadDirUtil(
                    API.APILink + "files/clients/" + this.name + "/" + name,
                    new File(clientPath, name)
                );
            }

        });

        try {
            String text = new String(Files.readAllBytes(new File(clientPath, "client.json").toPath()), StandardCharsets.UTF_8);
            JSONObject info = new JSONObject(text);

            File assets = new File(Global.LAUNCHER_CLIENTS, "asset" + info.getString("version"));

            AtomicLong size = new AtomicLong();

            Request req = new Request(API.APILink + "files/assets");
            JSONObject res = req.connectAndGetJSON();
            JSONArray zip = res == null ? new JSONArray() : res.getJSONArray("files");

            zip.forEach(o -> {
                JSONObject file = new JSONObject(o.toString());
                if(file.getString("name").equals("asset" + info.getString("version") + ".zip")) {
                    size.set(file.getLong("size"));
                }
            });

            if(!assets.exists() || new File(assets, "temp.zip").length() != size.get()) {
                assets.mkdir();

                FileUtil.downloadAndUnpackFromURL(
                        API.APILink + "files/assets/asset" + info.getString("version") + ".zip",
                        new File(Global.LAUNCHER_CLIENTS, "asset" + info.getString("version")).toPath()
                );
            }

            new Client(this.name, info).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
