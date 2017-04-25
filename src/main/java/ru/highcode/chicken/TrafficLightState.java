package ru.highcode.chicken;

import javafx.scene.image.Image;

public enum TrafficLightState {
    RED("file:red.png"),
    YELLOW("file:yellow.png"),
    GREEN("file:green.png");

    private final Image image;

    private TrafficLightState(String imageUrl) {
        image = new Image(imageUrl);
    }

    public Image getImage() {
        return image;
    }
}
