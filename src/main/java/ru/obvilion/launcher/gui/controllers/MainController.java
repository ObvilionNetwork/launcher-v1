package ru.obvilion.launcher.gui.controllers;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import ru.obvilion.json.JSONArray;
import ru.obvilion.json.JSONObject;
import ru.obvilion.launcher.api.API;
import ru.obvilion.launcher.config.Config;
import ru.obvilion.launcher.config.Global;
import ru.obvilion.launcher.config.Vars;
import ru.obvilion.launcher.gui.GUI;
import ru.obvilion.launcher.utils.FileUtil;
import ru.obvilion.launcher.utils.Lang;
import ru.obvilion.launcher.utils.MathUtil;
import ru.obvilion.launcher.utils.StyleUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;

public class MainController implements Initializable  {
    //region Панель слева
    public Pane left_pane;

    public ImageView logo;
    public ImageView youtube;
    public ImageView discord;
    public ImageView vk;

    public ImageView main_page;
    public ImageView options;
    public ImageView to_site;
    //endregion

    //region Управление окном
    public ImageView close_image;
    public ImageView minimize_image;
    public Button close;
    public Button minimize;
    //endregion

    //region Средняя панель настроек клиента
    public Pane average_pane;

    public ImageView icon_description;
    public ImageView icon_mods;
    public ImageView icon_client_mods;
    public ImageView icon_texturepacks;
    public ImageView icon_shaders;

    public ImageView selected1;
    public ImageView selected2;
    public ImageView selected3;
    public ImageView selected4;
    public ImageView selected5;

    public Button button1;
    public Button button2;
    public Button button3;
    public Button button4;
    public Button button5;

    public Label label_description;
    public Label label_mods;
    public Label label_client_mods;
    public Label label_texturepacks;
    public Label label_shaders;

    public Pane vip_bg;
    public Pane vip_button;
    //endregion

    /* Текущая выбранная категория */
    public Label currentLabel;
    public ImageView currentImage;
    public ImageView currentSelected;

    public List<Node> buttons;
    public List<Node> labels;
    public List<Node> images;
    public List<Node> selected;

    
    public ScrollPane servers;
    public Pane selectedServer;

    public ImageView image;
    public ImageView loading;
    public Pane image_border;

    public Label server_name;
    public Label server_description;
    public ImageView play;

    /* Главная */
    public AnchorPane main;
    public Label title_list;
    public Label title_servers;
    public Label title_o;
    public Label title_server;
    public ImageView player;
    public Label news;
    public Label news_label;
    public Pane news_pane;
    public Label title_options;
    public Label title_launcher;

    public Pane autologin;
    public Pane fullscreen;
    public Pane savePass;
    public Pane debug;

    public Label path;
    public Pane changePath;
    public Circle autologinY;
    public Circle fullscreenY;
    public Circle savePassY;
    public Circle debugY;
    public Slider ram;
    public Label ramCount;
    public Pane openFolder;
    public Pane optionsPane;
    public Pane removeAll;


    public void initLeftPane() {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

        main_page.setOnMouseClicked(e -> {
            optionsPane.setVisible(false);
            hideNews(true);
            hideServers(true);
            hideInfo(true);
        });
        options.setOnMouseClicked(e -> {
            optionsPane.setVisible(true);
            hideNews(false);
            hideServers(false);
            hideInfo(false);
        });
        to_site.setOnMouseClicked(e -> {
            try {
                desktop.browse(new URI("https://obvilionnetwork.ru/"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        logo.setOnMouseEntered(e -> {
            StyleUtil.createFadeAnimation(logo, 600, 0.6f);
        });
        logo.setOnMouseExited(e -> {
            StyleUtil.createFadeAnimation(logo, 600, 0.85f);
        });

        StyleUtil.changeColorImage(youtube, Color.web("#7289da"));
        youtube.setOnMouseEntered(e -> {
            StyleUtil.changeColorImage(youtube, Color.web("#7289da"), Color.web("#ff464d"), 400);
        });
        youtube.setOnMouseExited(e -> {
            StyleUtil.changeColorImage(youtube, Color.web("#ff464d"), Color.web("#7289da"), 400);
        });
        youtube.setOnMouseClicked(e -> {
            try {
                desktop.browse(new URI("https://www.youtube.com/channel/UCi-c4YwKOQUJ7Ep6lFqCRtg"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        StyleUtil.changeColorImage(discord, Color.web("#7289da"));
        discord.setOnMouseEntered(e -> {
            StyleUtil.changeColorImage(discord, Color.web("#7289da"), Color.web("#889cdf"), 300);
        });
        discord.setOnMouseExited(e -> {
            StyleUtil.changeColorImage(discord, Color.web("#889cdf"), Color.web("#7289da"), 300);
        });
        discord.setOnMouseClicked(e -> {
            try {
                desktop.browse(new URI("https://discord.gg/cg82mjh"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        StyleUtil.changeColorImage(vk, Color.web("#7289da"));
        vk.setOnMouseEntered(e -> {
            StyleUtil.changeColorImage(vk, Color.web("#7289da"), Color.web("#89b1e3"), 300);
        });
        vk.setOnMouseExited(e -> {
            StyleUtil.changeColorImage(vk, Color.web("#89b1e3"), Color.web("#7289da"), 300);
        });
        vk.setOnMouseClicked(e -> {
            try {
                desktop.browse(new URI("https://vk.com/obvilionmc"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    public void initAveragePane() {

        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }

        StyleUtil.changeColorImage(icon_mods, Color.web("#4b5568"));
        StyleUtil.changeColorImage(icon_client_mods, Color.web("#4b5568"));
        StyleUtil.changeColorImage(icon_texturepacks, Color.web("#4b5568"));
        StyleUtil.changeColorImage(icon_shaders, Color.web("#4b5568"));

        currentLabel = label_description;
        currentImage = icon_description;
        currentSelected = selected1;

        buttons = toArray(button1, button2, button3, button4, button5);
        labels = toArray(label_description, label_mods, label_client_mods, label_texturepacks, label_shaders);
        selected = toArray(selected1, selected2, selected3, selected4, selected5);
        images = toArray(icon_description, icon_mods, icon_client_mods, icon_texturepacks, icon_shaders);

        for (int i = 0; i < buttons.size(); i++) {
            int finalI = i;
            buttons.get(i).setOnMouseClicked(event -> {
                if (currentLabel == labels.get(finalI)) return;
                StyleUtil.changePosition(currentLabel, 39, currentLabel.getLayoutY(), 200);

                removeCurrent();
                currentLabel = (Label) labels.get(finalI);
                currentImage = (ImageView) images.get(finalI);
                currentSelected = (ImageView) selected.get(finalI);
                updateCurrent();
            });
            buttons.get(i).setOnMouseEntered(event -> {
                if(selected.get(finalI) != currentSelected) {

                    StyleUtil.createFadeAnimation(selected.get(finalI), 300, 0.2f);
                    StyleUtil.changeColorImage((ImageView) images.get(finalI), Color.web("#4b5568"), Color.GRAY, 300);
                    StyleUtil.createFadeAnimation(labels.get(finalI), 300, 0.5f);
                    StyleUtil.changePosition(labels.get(finalI), 43, labels.get(finalI).getLayoutY(), 200);
                }
            });
            buttons.get(i).setOnMouseExited(event -> {
                if(selected.get(finalI) != currentSelected) {
                    StyleUtil.createFadeAnimation(selected.get(finalI), 300, 0.0f);
                    StyleUtil.changeColorImage((ImageView) images.get(finalI), Color.GRAY, Color.web("#4b5568"), 300);
                    StyleUtil.createFadeAnimation(labels.get(finalI), 300, 0.3f);
                    StyleUtil.changePosition(labels.get(finalI), 39, labels.get(finalI).getLayoutY(), 200);
                }
            });
        }

        vip_button.setOnMouseEntered(event -> {
            StyleUtil.createFadeAnimation(vip_button, 400, 0.8f);
        });
        vip_button.setOnMouseExited(event -> {
            StyleUtil.createFadeAnimation(vip_button, 400, 1.0f);
        });
        vip_button.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://obvilionnetwork.ru/"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        path.setText(Global.LAUNCHER_CLIENTS.getPath());
        changePath.setOnMouseClicked(event -> {
            File file = new DirectoryChooser().showDialog(GUI.stage);
            if(file != null) {
                Global.LAUNCHER_CLIENTS = file;
                path.setText(Global.LAUNCHER_CLIENTS.getPath());
            }
        });

        if(Config.getBooleanValue("autoLogin")) {
            autologinY.setLayoutX(autologinY.getCenterX() + 43);
            autologinY.setFill(Color.web("7293bf"));
        }
        if(Config.getBooleanValue("fullscreen")) {
            fullscreenY.setLayoutX(fullscreenY.getCenterX() + 43);
            fullscreenY.setFill(Color.web("7293bf"));
        }
        if(Config.getBooleanValue("savePass")) {
            savePassY.setLayoutX(savePassY.getCenterX() + 43);
            savePassY.setFill(Color.web("7293bf"));
        }
        if(Config.getBooleanValue("debug")) {
            debugY.setLayoutX(debugY.getCenterX() + 43);
            debugY.setFill(Color.web("7293bf"));
        }
        autologin.setOnMouseClicked(event -> {
            if(Config.getBooleanValue("autoLogin")) {
                StyleUtil.changePosition(autologinY, 15, autologinY.getLayoutY(), 300);
                autologinY.setFill(Color.web("a4a4a4"));
            } else {
                StyleUtil.changePosition(autologinY, 40, autologinY.getLayoutY(), 300);
                autologinY.setFill(Color.web("7293bf"));
            }
            Config.setValue("autoLogin", !Config.getBooleanValue("autoLogin") + "");
        });
        fullscreen.setOnMouseClicked(event -> {
            if(Config.getBooleanValue("fullscreen")) {
                StyleUtil.changePosition(fullscreenY, 15, fullscreenY.getLayoutY(), 300);
                fullscreenY.setFill(Color.web("a4a4a4"));
            } else {
                StyleUtil.changePosition(fullscreenY, 40, fullscreenY.getLayoutY(), 300);
                fullscreenY.setFill(Color.web("7293bf"));
            }
            Config.setValue("fullscreen", !Config.getBooleanValue("fullscreen") + "");
        });
        savePass.setOnMouseClicked(event -> {
            if(Config.getBooleanValue("savePass")) {
                StyleUtil.changePosition(savePassY, 15, savePassY.getLayoutY(), 300);
                savePassY.setFill(Color.web("a4a4a4"));
            } else {
                StyleUtil.changePosition(savePassY, 40, savePassY.getLayoutY(), 300);
                savePassY.setFill(Color.web("7293bf"));
            }
            Config.setValue("savePass", !Config.getBooleanValue("savePass") + "");
        });
        debug.setOnMouseClicked(event -> {
            if(Config.getBooleanValue("debug")) {
                StyleUtil.changePosition(debugY, 15, debugY.getLayoutY(), 300);
                debugY.setFill(Color.web("a4a4a4"));
            } else {
                StyleUtil.changePosition(debugY, 40, debugY.getLayoutY(), 300);
                debugY.setFill(Color.web("7293bf"));
            }
            Config.setValue("debug", !Config.getBooleanValue("debug") + "");
        });

        ram.setShowTickMarks(true);
        ram.setShowTickLabels(true);

        ram.setMin(0.5f);
        ram.setMax(8.0f);

        ram.setMajorTickUnit(1.0);
        ram.setMinorTickCount(1);

        ram.valueProperty().addListener((obs, oldValue, newValue) -> {
            ramCount.setText(MathUtil.round(newValue.floatValue(), 2) + "GB");
            Config.setValue("ram", Math.round(ram.getValue() * 1024) + "");
        });

        ram.setValue(Config.getIntValue("ram") / 1024f);
        ramCount.setText(MathUtil.round(ram.getValue(), 2) + "GB");

        Desktop finalDesktop = desktop;
        openFolder.setOnMouseClicked(e -> {
            try {
               finalDesktop.open(Global.LAUNCHER_CLIENTS);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        removeAll.setOnMouseClicked(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle(Lang.get("alertType"));
            alert.setHeaderText(Lang.get("removeAll"));
            alert.setContentText(Lang.get("confirm"));

            alert.showAndWait();

            if (alert.getResult().getText().equals("OK")) {
                FileUtil.deleteDirectory(Global.LAUNCHER_CLIENTS);
            }
        });
    }
    public void initServers() {
        servers.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        news.setText(API.getNews());
    }

    public void removeCurrent() {
        StyleUtil.changeColorImage(currentImage, Color.WHITE, Color.web("#4b5568"), 300);
        StyleUtil.createFadeAnimation(currentSelected, 300, 0.0f);
        StyleUtil.createFadeAnimation(currentLabel, 300, 0.3f);
    }
    public void updateCurrent() {
        StyleUtil.createFadeAnimation(currentLabel, 300, 1.0f);
        StyleUtil.createFadeAnimation(currentSelected, 300, 1.0f);
        StyleUtil.changeColorImage(currentImage, Color.GRAY, Color.WHITE, 300);
    }

    public List<Node> toArray(Node... nodes) {
        return new ArrayList<>(Arrays.asList(nodes));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLeftPane();
        initAveragePane();
        initServers();

        play.setOnMouseClicked(event -> {
            Vars.clientName = server_name.getText();
            GUI.loadNext("Loading");
        });

       // hideNews(false);
       // hideServers(false);
       // hideInfo(false);

        close.setOnMouseClicked(event -> {
            System.exit(0);
        });
        minimize.setOnMouseClicked(event -> {
            GUI.stage.setIconified(true);
        });

        HBox root = (HBox) servers.getContent();
        JSONArray servers1 = API.getServers();

        // Максимальный размер скролл панели
        int hmax = 0;

        for (int i = 0; i < servers1.length(); i++) {
            JSONObject srv = (JSONObject) servers1.get(i);
            hmax += 160;

            Pane pane = addServer(srv);
            root.getChildren().add(pane);

            if(i == 0) {
                if(!srv.getBoolean("inDev")) {
                    image.setImage(new Image(srv.getString("image")));
                    selectedServer = pane;
                    pane.setStyle("-fx-border-color: #5f97ff; -fx-border-radius: 7; -fx-border-width: 2px;");
                    server_name.setText(srv.getString("name"));
                    server_description.setText(srv.getString("description"));
                }
            }
        }

        root.setPrefWidth(hmax);
        root.setSpacing(18);
    }
    public Pane addServer(final JSONObject server) {
        final Pane srv = new Pane();

        srv.setId(server.getInt("id") + "");
        srv.setPrefSize(161, 118);
        srv.setMinWidth(161);
        srv.setMinHeight(118);
        srv.setMaxHeight(118);

        /* Название сервера */
        Label name = new Label(server.getString("name"));
        name.getStyleClass().add("server_name");
        name.setLayoutX(9);
        name.setLayoutY(14);

        /* игроков сервера текст */
        Label title_players = new Label(server.getInt("players") == -1 ? Lang.get("server") : Lang.get("players"));
        title_players.getStyleClass().add("server_title_players");
        title_players.setLayoutX(56);
        title_players.setLayoutY(48);

        /* Онлайн сервера текст */
        Label title_online = new Label(server.getInt("players") == -1 ? Lang.get("offline") : Lang.get("online"));
        title_online.getStyleClass().add("server_title_online");
        title_online.setLayoutX(56);
        title_online.setLayoutY(64);

        /* Дата вайпа текст */
        Label wipe_text = new Label(Lang.get("wipe"));
        wipe_text.getStyleClass().add("wipe_text");
        wipe_text.setLayoutX(9);
        wipe_text.setLayoutY(95);

        /* Дата вайпа */
        Label wipe_date = new Label(server.isNull("wipeDate") ? Lang.get("notFound") : server.getString("wipeDate").substring(0, 10));
        wipe_date.getStyleClass().add("wipe_date");
        wipe_date.setLayoutX(89);
        wipe_date.setLayoutY(96);

        Circle circle_bg = new Circle(16, Color.web("#0e141a"));
        circle_bg.setLayoutX(29);
        circle_bg.setLayoutY(64);

        Circle circle_bg2 = new Circle(18.3, Color.web("#5f97ff"));
        circle_bg2.setLayoutX(29);
        circle_bg2.setLayoutY(64);

        Arc arc = new Arc();
        arc.setCenterX(29); arc.setRadiusX(18.7);
        arc.setCenterY(64); arc.setRadiusY(18.7);
        arc.setStartAngle(90.0f);

        if(server.getInt("maxPlayers") != 0) {
            int radius = 360 / server.getInt("maxPlayers") * server.getInt("players");
            radius = 360 - radius;
            arc.setLength(radius);
        } else {
            int radius = 0;
            arc.setLength(radius);
        }

        arc.setType(ArcType.ROUND);
        arc.setFill(Color.web("#202734"));

        Label online = new Label((server.getInt("players") == -1 ? 0 : server.getInt("players")) + "/" + server.getInt("maxPlayers"));
        online.getStyleClass().add("online");
        online.setLayoutX(9);
        online.setLayoutY(58);
        online.setPrefWidth(40);

        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(7);

        ImageView image1 = null;
        Label opt1 = null;
        Label opt2 = null;
        if(server.getBoolean("inDev")) {
            online.setEffect(gaussianBlur);
            arc.setEffect(gaussianBlur);
            circle_bg2.setEffect(gaussianBlur);
            circle_bg.setEffect(gaussianBlur);
            wipe_date.setEffect(gaussianBlur);
            wipe_text.setEffect(gaussianBlur);
            title_online.setEffect(gaussianBlur);
            title_players.setEffect(gaussianBlur);
            name.setEffect(gaussianBlur);

            image1 = new ImageView(new Image(GUI.class.getResourceAsStream("images/icons/icon_lock.png")));
            image1.setLayoutX(70); image1.setLayoutY(30);
            opt1 = new Label(Lang.get("development"));
            opt2 = new Label(Lang.get("development1"));
            opt1.getStyleClass().add("wipe_text");
            opt1.setLayoutX(16);
            opt1.setLayoutY(62);
            opt2.getStyleClass().add("wipe_text");
            opt2.setLayoutX(38);
            opt2.setLayoutY(78);

        } else {
            srv.setOnMouseClicked(event -> {
                if(selectedServer != null && selectedServer != srv) {
                    selectedServer.getStyleClass().remove("server_selected");
                    selectedServer.setStyle("-fx-border-width: 0;");

                    Thread load = new Thread(() -> {
                        StyleUtil.createFadeAnimation(loading, 200, 0.8f);
                        image.setImage(new Image(server.getString("image")));
                        StyleUtil.createFadeAnimation(loading, 200, 0);
                        Thread.currentThread().interrupt();
                    });
                    load.start();
                }
                if (selectedServer != srv) {
                    selectedServer = srv;
                    srv.getStyleClass().add("server_selected");
                    StyleUtil.changeBorderColor(srv, Color.web("#0e141a"), Color.web("#5f97ff"), 200);
                    server_name.setText(server.getString("name"));
                    server_description.setText(server.getString("description"));
                }
            });
        }

        srv.getStyleClass().add("server");

        srv.getChildren().addAll(wipe_date, wipe_text, title_players, title_online, name, circle_bg2, arc, circle_bg, online);
        if(image1 != null) {
            srv.getChildren().addAll(image1, opt1, opt2);
        }

        return srv;
    }

    public void hideNews(boolean visible) {
        news.setVisible(visible);
        news_label.setVisible(visible);
        news_pane.setVisible(visible);
    }
    public void hideServers(boolean visible) {
        servers.setVisible(visible);
        title_list.setVisible(visible);
        title_servers.setVisible(visible);
    }
    public void hideInfo(boolean visible) {
        title_o.setVisible(visible);
        title_server.setVisible(visible);
        server_description.setVisible(visible);
        server_name.setVisible(visible);
        image_border.setVisible(visible);
        image.setVisible(visible);
        play.setVisible(visible);
    }
}
