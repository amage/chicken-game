package ru.highcode.chicken;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class CarWay extends Pane {
    private static final Image car = new Image("file:car.png");
    private static final double CAR_HEIGHT = car.getHeight();
    private static final double CAR_WIDTH = car.getWidth();
    private final Canvas canvas = new Canvas();
    private double carPos = 0;

    public CarWay() {
        getChildren().add(canvas);
        setMinHeight(CAR_HEIGHT);
    }

    public double getCarPos() {
        return carPos;
    }

    public void setCarPos(double carPos) {
        canvas.getGraphicsContext2D().clearRect(this.carPos, 0, CAR_WIDTH, CAR_HEIGHT);
        this.carPos = carPos;
        canvas.getGraphicsContext2D().drawImage(car, carPos, 0);
    }

    @Override
    protected void layoutChildren() {
        final int top = (int) snappedTopInset();
        final int right = (int) snappedRightInset();
        final int bottom = (int) snappedBottomInset();
        final int left = (int) snappedLeftInset();
        final int w = (int) getWidth() - left - right;
        final int h = (int) getHeight() - top - bottom;
        canvas.setLayoutX(left);
        canvas.setLayoutY(top);
        if (w != canvas.getWidth() || h != canvas.getHeight()) {
            canvas.setWidth(w);
            canvas.setHeight(CAR_HEIGHT);
            setMinWidth(w);
        }
    }
}
