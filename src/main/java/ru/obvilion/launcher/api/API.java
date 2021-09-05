package ru.obvilion.launcher.api;

import ru.obvilion.json.JSONArray;

import ru.obvilion.json.JSONObject;
import ru.obvilion.launcher.utils.Lang;

public class API {

    public static String APILink = "https://obvilionnetwork.ru/api/";

    public static JSONArray getServers() {
        Request req = new Request(APILink + "servers");
        JSONObject res = req.connectAndGetJSON();

        if(res != null && !res.has("message")) {
            return res.getJSONArray("servers");
        }

        return new JSONArray();
    }

    public static String getNews() {
        Request req = new Request(APILink + "news/last");
        JSONObject res = req.connectAndGetJSON();

        if(res != null && !res.has("message")) {
            return res.getString("name");
        }

        return Lang.get("noNews");
    }

    public static JSONObject getClientFile(String name) {
        Request req = new Request(APILink + "files/clients/" + name);
        return req.connectAndGetJSON();
    }

    public static JSONObject auth(String login, String password) {
        JSONObject obj = new JSONObject();
        obj.put("name", login);
        obj.put("password", password);

        Request req = new Request(RequestType.POST, APILink + "auth/login");
        req.setBody(obj);

        JSONObject res = req.connectAndGetJSON();
        req.connect();

        System.out.println(res);

        if (res != null)
        if (res.has("token")) {

            return new JSONObject().put("ok", true).put("result", res);
        }

        return new JSONObject().put("ok", false);
    }
}
