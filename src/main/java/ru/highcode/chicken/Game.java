package ru.highcode.chicken;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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
    public Game() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chicken Game");
        primaryStage.setFullScreen(true);

        final Scene loginScene = loginScene();
        final Image car1 = new Image("file:car.png");
        final Image car2 = new Image("file:car2.png");
        final ImageView imgView1 = new ImageView(car1);
        final ImageView imgView2 = new ImageView(car2);

        final Path path = new Path();
        path.getElements().add(new MoveTo(20, 20));
        final PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(imgView1);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();

        final VBox vBox = new VBox();
        vBox.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        final Text text = new Text(GameData.load().getTexts().get("1"));
        text.setWrappingWidth(600);
        text.setFont(new Font(20));
        text.setFill(Color.AQUA);
        text.setTextAlignment(TextAlignment.JUSTIFY);
        vBox.getChildren().add(imgView1);
        vBox.getChildren().add(text);
        vBox.getChildren().add(imgView2);

//        final Scene scene = new Scene(vBox);
        // primaryStage.setScene(scene);
        primaryStage.setScene(loginScene);
        primaryStage.show();
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

}
