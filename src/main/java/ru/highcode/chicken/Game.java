package ru.highcode.chicken;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application {
    private static GameData DATA;

    static {
        try {
            DATA = GameData.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Game() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chicken Game");
//        primaryStage.setFullScreen(true);

        final Scene scene = gameScene();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Scene textScene(String text) {
        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        Text t = new Text(text);
        t.setWrappingWidth(800);
        t.setTextAlignment(TextAlignment.JUSTIFY);
        pane.setSpacing(50);
        pane.getChildren().add(t);
        pane.getChildren().add(new Button("Продолжить"));
        return new Scene(pane);
    }

    private Scene loginScene() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(20);
        pane.setVgap(20);
        pane.add(new Label("Данные об испытуемом"), 0, 0, 2, 1);
        pane.add(new Label("Номер"), 0, 1);
        pane.add(new TextField(), 1, 1);
        HBox bbox = new HBox();
        Button button = new Button("Старт");
        bbox.getChildren().add(button);
        bbox.setAlignment(Pos.BOTTOM_RIGHT);
        pane.add(bbox, 0, 2, 2, 1);
        return new Scene(pane);
    }

    private Scene gameScene() {
        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);

        GridPane scorePane = new GridPane();

        scorePane.add(new Text("Total Bank Account: "), 0, 0);
        scorePane.add(new Text("0"), 1, 0);
        scorePane.add(new Text("Total Points Earned: "), 0, 1);
        scorePane.add(new Text("0"), 1, 1);

        pane.getChildren().add(scorePane);
        pane.getChildren().add(new Text("Светофор"));

        CarWay carWay = new CarWay();
        carWay.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.DASHED, null, null)));

        pane.getChildren().add(carWay);

        long startNanoTime = System.nanoTime();
        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                double v = 50;
                double x = -200;
                carWay.setCarPos(x + v * t);
            }
        };
        at.start();

        return new Scene(pane, 800, 600);
    }
}
