package ru.highcode.chicken;

import javafx.scene.Scene;

public interface IChickenScene {
    void activated();

    Scene getScene();

    static IChickenScene wrap(Scene scene) {
        return new IChickenScene() {
            @Override
            public Scene getScene() {
                return scene;
            }

            @Override
            public void activated() {
            }
        };
    }
}
