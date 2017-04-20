package ru.highcode.chicken;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Game extends Application {
    public Game() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chicken Game");

        final Image car1 = new Image("file:car.png");
        final Image car2 = new Image("file:car2.png");
        final ImageView imgView1 = new ImageView(car1);
        final ImageView imgView2 = new ImageView(car2);
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

        final Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
