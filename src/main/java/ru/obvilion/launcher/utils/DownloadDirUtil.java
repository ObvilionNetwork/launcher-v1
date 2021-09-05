package ru.obvilion.launcher.utils;

import ru.obvilion.json.JSONArray;
import ru.obvilion.json.JSONObject;
import ru.obvilion.launcher.api.Request;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class DownloadDirUtil {
    public DownloadDirUtil(String request, File path) {
        Request req = new Request(request);
        JSONObject obj = req.connectAndGetJSON();

        JSONArray arr = obj.getJSONArray("files");

        for(File file : path.listFiles()) {
            AtomicBoolean ok = new AtomicBoolean(false);

            arr.forEach(o -> {
                JSONObject object = new JSONObject(o.toString());
                if(file.getName().equals(object.getString("name"))) ok.set(true);
            });

            if(!ok.get()) file.delete();
        }

        arr.forEach(o -> {
            JSONObject object = new JSONObject(o.toString());
            File resource = new File(path, object.getString("name"));

            if(!object.getBoolean("isFile")) {
                resource.mkdir();

                new DownloadDirUtil(
                    request + "/" + object.getString("name"),
                    resource
                );
            } else {
                boolean exists = false;

                for(File file : path.listFiles()) {
                    if(file.getName().equals(object.getString("name")) && file.length() == object.getLong("size")) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) FileUtil.downloadFromURL(
                    request + "/" + object.getString("name"),
                    resource.toPath()
                );
            }
        });
    }
}