package ru.obvilion.launcher.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import ru.obvilion.json.JSONObject;
import ru.obvilion.launcher.api.API;
import ru.obvilion.launcher.config.Config;
import ru.obvilion.launcher.config.Vars;
import ru.obvilion.launcher.gui.GUI;
import ru.obvilion.launcher.utils.Lang;
import ru.obvilion.launcher.utils.PasswordFieldSkin;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public Button go;
    public TextField login;
    public PasswordField pass;
    public ImageView close;
    public Label text_no;
    public CheckBox save;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.setText(Config.getValue("login"));
        pass.setText(Config.getValue("password"));

        login.setFocusTraversable(false);
        pass.setFocusTraversable(false);
        pass.setSkin(new PasswordFieldSkin(pass));

        close.setOnMouseClicked(event -> {
            System.exit(0);
        });
        text_no.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://obvilionnetwork.ru/auth/signup"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        go.setOnMouseClicked(event -> {
            JSONObject obj1 = API.auth(login.getText(), pass.getText());

            if (obj1.getBoolean("ok")) {
                Config.setValue("login", login.getText());
                if (save.isSelected()) Config.setValue("password", pass.getText());
                Config.setValue("token", obj1.getJSONObject("result").getString("token"));
                Config.setValue("uuid", obj1.getJSONObject("result").getString("uuid"));

                Vars.presence.updateDescription(Lang.get("inLauncher"));
                GUI.loadMain();
            }
        });
    }
}
