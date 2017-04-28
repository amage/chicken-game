package ru.highcode.chicken;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class SceneFactory {
    public static Scene textScene(String text, ISceneSwitcher switcher) {
        final VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        final Text t = new Text(text);
        t.setWrappingWidth(800);
        t.setTextAlignment(TextAlignment.JUSTIFY);
        pane.setSpacing(50);
        pane.getChildren().add(t);
        final Button nextButton = new Button("Продолжить");
        pane.getChildren().add(nextButton);
        nextButton.setOnAction(e -> {
            switcher.nextScene();
        });
        return new Scene(pane);
    }

    public static Scene loginScene(ISceneSwitcher switcher) {
        final GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(20);
        pane.setVgap(20);
        pane.add(new Label("Данные об испытуемом"), 0, 0, 2, 1);
        pane.add(new Label("Номер"), 0, 1);
        pane.add(new TextField(), 1, 1);
        final HBox bbox = new HBox();
        final Button button = new Button("Старт");
        button.setOnAction(e -> {
            switcher.nextScene();
        });
        bbox.getChildren().add(button);
        bbox.setAlignment(Pos.BOTTOM_RIGHT);
        pane.add(bbox, 0, 2, 2, 1);
        return new Scene(pane);
    }

    public static Scene gameScene(ISceneSwitcher switcher) {
        final GameRoundScene gscene = new GameRoundScene(4, switcher);
        return gscene.getScene();
    }
}
