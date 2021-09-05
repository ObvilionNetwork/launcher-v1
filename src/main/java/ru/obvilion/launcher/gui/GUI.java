package ru.obvilion.launcher.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.obvilion.json.JSONObject;
import ru.obvilion.launcher.api.API;
import ru.obvilion.launcher.config.Config;
import ru.obvilion.launcher.config.Vars;
import ru.obvilion.launcher.utils.Lang;

import java.io.IOException;

public class GUI extends Application {
    private static double x, y;
    public static Stage stage;

    public static void main(String[] args) {
        Platform.setImplicitExit(false);
        launch(args);
    }

    public static void loadMainEx() {
        loadNext("main");
    }

    public static void loadNext(String loadName) {
        if(stage != null)
        stage.close();

        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("fxmls/" + loadName + ".fxml"));
        Parent root;
        
        try {
            root = loader.load();

            stage.setScene(new Scene(root));

            root.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });

            root.getStylesheets().add((GUI.class.getResource("style.css")).toExternalForm());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMain() {
        if(stage != null)
        stage.close();
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("fxmls/Main.fxml"));
        Parent root;

        try {
            root = loader.load();
            Vars.mainController = loader.getController();

            stage.setScene(new Scene(root));

            root.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });

            root.getStylesheets().add((GUI.class.getResource("style.css")).toExternalForm());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setOnCloseRequest(event -> {
            Vars.presence.dispose();
            System.out.println("Stage is closing");
            Platform.exit();
        });

        String name = "Login";

        JSONObject obj = API.auth(Config.getValue("login"), Config.getValue("password"));
        if (obj.getBoolean("ok")) {
            Config.setValue("token", obj.getJSONObject("result").getString("token"));
            Config.setValue("uuid", obj.getJSONObject("result").getString("uuid"));

            name = "Main";
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/" + name + ".fxml"));
        Parent root = loader.load();

        if (name.equals("Login")) {
            Vars.loginController = loader.getController();
        } else {
            Vars.mainController = loader.getController();
            Vars.presence.updateDescription(Lang.get("player").replace("{0}", Config.getValue("login")));
            Vars.presence.updateState(Lang.get("inLauncher"));
        }

        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("ObvilionNetwork.ru | Minecraft project");
        primaryStage.getIcons().add(new Image(GUI.class.getResourceAsStream("images/logo.png")));

        primaryStage.sizeToScene();
        primaryStage.setResizable(true);

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

        root.getStylesheets().add((GUI.class.getResource("style.css")).toExternalForm());

        primaryStage.show();
    }
}
