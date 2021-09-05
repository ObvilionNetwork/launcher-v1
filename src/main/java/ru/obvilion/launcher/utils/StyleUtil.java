package ru.obvilion.launcher.utils;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Вспомогательный класс для красивых и плавных анимаций чего-либо
 * @author KeviTV
 */
public class StyleUtil {

    /**
     * Создаёт анимацию измененеия прозрачности на определенный промежуток времени
     *
     * @param node Сам обьект для изменения прозрачности
     * @param fadeDuration Длительноть анимации изменения прозрачности
     * @param to До какого значения будет изменяться прозрачность (от 0.0 до 1.0)
     */
    public static void createFadeAnimation(Node node, int fadeDuration, float to) {
        FadeTransition ft = new FadeTransition(Duration.millis(fadeDuration), node);

        ft.setFromValue(node.getOpacity());
        ft.setToValue(to);
        ft.play();
    }

    /**
     * изменяет цвет изображения, применяет эффект изменения цвета
     *
     * @param imageView Сам обьект для изменения цвета
     * @param color Цвет, в который необходимо изменить изображение
     */
    public static void changeColorImage(ImageView imageView, Color color) {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(0.0);
        lighting.setSpecularExponent(0.0);
        lighting.setSurfaceScale(0.0);
        lighting.setLight(new Light.Distant(45, 45, color));

        imageView.setEffect(lighting);
    }

    /**
     * Плавно изменяет цвет изображения.
     *
     * @param image Сам обьект для изменения цвета
     * @param front изначальный цвет изображения
     * @param to Цвет, в который необходимо изменить изображение
     * @param durationAnimation Длительность анимации
     */
    public static void changeColorImage(ImageView image, Color front, Color to, int durationAnimation) {
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(durationAnimation));
            }

            protected void interpolate(double f) {
                float r = (float) (to.getRed() * f + front.getRed() * (1 - f));
                float g = (float) (to.getGreen() * f + front.getGreen() * (1 - f));
                float b = (float) (to.getBlue() * f + front.getBlue() * (1 - f));

                changeColorImage(image, Color.color(r, g, b));
            }
        };
        animation.play();
    }

    /**
     * Плавно изменяет цвет текста.
     *
     * @param text Сам обьект для изменения цвета
     * @param to Цвет, в который необходимо изменить изображение
     * @param durationAnimation Длительность анимации
     */
    public static void changeColorText(Text text, Color to, int durationAnimation) {
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(durationAnimation));
            }

            final Color front = (Color) text.getFill();
            protected void interpolate(double f) {
                float r = (float) (to.getRed() * f + front.getRed() * (1 - f));
                float g = (float) (to.getGreen() * f + front.getGreen() * (1 - f));
                float b = (float) (to.getBlue() * f + front.getBlue() * (1 - f));

                text.setFill(Color.color(r, g, b));
            }
        };
        animation.play();
    }

    /**
     * Плавно изменяет позицию обьекта
     *
     * @param element Элемент для перемещения
     * @param toX До какой координаты по X
     * @param toY До какой координаты по Y
     * @param durationAnimation Длительность анимации
     */
    public static void changePosition(Node element, double toX, double  toY, int durationAnimation) {
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(durationAnimation));
            }

            final float ansLayoutX = (float) element.getLayoutX();
            final float ansLayoutY = (float) element.getLayoutY();
            protected void interpolate(double f) {
                float x = (float) (toX * f + ansLayoutX * (1 - f));
                float y = (float) (toY * f + ansLayoutY * (1 - f));

                element.setLayoutX(x);
                element.setLayoutY(y);
            }
        };
        animation.play();
    }

    public static void changeBorderColor(Pane obj, Color front, Color to, int durationAnimation) {
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(durationAnimation));
            }

            protected void interpolate(double f) {
                float r = (float) (to.getRed() * f + front.getRed() * (1 - f));
                float g = (float) (to.getGreen() * f + front.getGreen() * (1 - f));
                float b = (float) (to.getBlue() * f + front.getBlue() * (1 - f));

                obj.setStyle("-fx-border-color: #" + Color.color(r,g,b).toString().replace("0x", "") + ";");
            }
        };
        animation.play();
    }
}
