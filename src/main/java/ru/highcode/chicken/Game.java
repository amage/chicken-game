package ru.highcode.chicken;

import javafx.application.Application;
import javafx.scene.Scene;
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
        // primaryStage.setFullScreen(true);

        final Scene scene = SceneFactory.gameScene();
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
