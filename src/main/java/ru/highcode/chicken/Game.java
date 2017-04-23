package ru.highcode.chicken;

import java.io.IOException;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.scene.paint.Paint;
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
        primaryStage.setFullScreen(true);

         final Scene scene = gameScene();
//        final Scene scene = textScene(DATA.getTexts().get("1"));
//        final ImageView imgView2 = new ImageView(car2);

        final Path path = new Path();
        path.getElements().add(new MoveTo(20, 20));
        final PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
//        pathTransition.setNode(imgView1);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();

        final VBox vBox = new VBox();
        vBox.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        final Text text = new Text(DATA.getTexts().get("1"));
        text.setWrappingWidth(600);
        text.setFont(new Font(20));
        text.setFill(Color.AQUA);
        text.setTextAlignment(TextAlignment.JUSTIFY);
//        vBox.getChildren().add(imgView1);
        vBox.getChildren().add(text);
//        vBox.getChildren().add(imgView2);

        // final Scene scene = new Scene(vBox);
        // primaryStage.setScene(scene);
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
//        scorePane.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.DASHED, null, null)));

        pane.getChildren().add(scorePane);
        pane.getChildren().add(new Text("Светофор"));

        final Image car = new Image("file:car.png");
        ImageView carView = new ImageView(car);
//        carView.setFitHeight(100);
//        carView.setFitWidth(300);
        pane.getChildren().add(carView);
        return new Scene(pane);
    }
}
