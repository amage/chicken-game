package ru.highcode.chicken;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class CarWay extends Pane {
    // TODO: Game stop timer
    // TODO: Game stop length
    // TODO: history

    private static final Image car = new Image("file:car.png");
    private static final double CAR_HEIGHT = car.getHeight();
    private static final double CAR_WIDTH = car.getWidth();
    private final Canvas canvas = new Canvas();
    private double carPos = 0;
    private final double velocity;
    private long startNanoTime;
    private double startPos;

    private boolean run = false;

    public CarWay(double velocity) {
        this.velocity = velocity;
        setCarPos(-CAR_WIDTH);
        getChildren().add(canvas);
        setMinHeight(CAR_HEIGHT);
    }

    public double getCarPos() {
        return carPos;
    }

    public void startEngine() {
        if (run) {
            return;
        }
        startNanoTime = System.nanoTime();
        startPos = getCarPos();
        run = true;
    }

    public void stopEngine() {
        run = false;
    }

    public void setCarPos(double carPos) {
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(this.carPos, 0, CAR_WIDTH, CAR_HEIGHT);
        this.carPos = carPos;
        gc.drawImage(car, carPos, 0);
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

    public void update(long currentNanoTime) {
        if (!run) {
            return;
        }
        final double dt = (currentNanoTime - startNanoTime) / 1000000000.0;
        setCarPos(startPos + velocity * dt);
    }
}
