package ru.highcode.chicken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game extends Application implements ISceneSwitcher {
    private final List<Scene> scenario = new ArrayList<>();
    private Stage primaryStage;
    private int currentScene = 0;
    final Map<String, String> texts = GameData.load().getTexts();
    private ExperimentHistory experiment = new ExperimentHistory();

    public Game() throws IOException {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Chicken Game");
        // primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);

        addScene(SceneFactory.loginScene(experiment, this));
        // TODO intro texts
        addScene(SceneFactory.textScene(texts.get("1"), this));
        addScene(SceneFactory.gameScene("1", this));
        addScene(SceneFactory.textScene(texts.get("1"), this));

        primaryStage.setScene(scenario.get(currentScene));
        primaryStage.show();
    }

    private void addScene(Scene scene) {
        scenario.add(scene);
    }

    @Override
    public void nextScene() {
        currentScene = currentScene + 1;
        if (currentScene == scenario.size()) {
            currentScene = 0;
            experiment = new ExperimentHistory();
        }
        final Scene scene = scenario.get(currentScene);
        primaryStage.hide();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
