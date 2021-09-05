package ru.obvilion.launcher.api;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import ru.obvilion.json.JSONException;
import ru.obvilion.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Request {
    public RequestType requestType;
    public String link;

    public ArrayList<String> headerNames = new ArrayList<>();
    public ArrayList<String> headerValues = new ArrayList<>();

    public JSONObject body;

    public Request(String link) {
        this(RequestType.GET, link);
    }

    public Request(RequestType type, String link) {
        this.requestType = type;
        this.link = link;
    }

    public void addHeader(String type, String value) {
        headerNames.add(type);
        headerValues.add(value);
    }

    public void setBody(JSONObject json) {
        this.body = json;
    }

    public JSONObject connectAndGetJSON() {
        String json;

        if(requestType == RequestType.GET) {
            json = this.createGetRequest();
        } else {
            json = this.createPostRequest();
        }

        if (json == null) {
            return null;
        }

        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
    }

    public String connect() {
        try {
            URL obj = new URL(this.link);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod(this.requestType.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private String createGetRequest() {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet request = new HttpGet(this.link);

            request.addHeader("content-type", "application/json; charset=utf-8");

            try (CloseableHttpResponse response = httpClient.execute(request)) {

                // Get HttpResponse Status
                // System.out.println(response.getStatusLine().toString());

                HttpEntity entity = response.getEntity();
                Header headers = entity.getContentType();
                // System.out.println(headers);

                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    return result;
                }

            }

            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    private String createPostRequest() {
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost request = new HttpPost(this.link);

            StringEntity params = new StringEntity(this.body.toString());
            request.addHeader("content-type", "application/json; charset=utf-8");
            request.setEntity(params);
            HttpResponse resp = httpClient.execute(request);

            BufferedReader in = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception ex) {
            return null;
        }
    }
}
