package ru.highcode.chicken;

import java.util.Properties;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ru.highcode.chicken.data.Round;
import ru.highcode.chicken.data.StepHistory;

public class CarWay extends Pane {
    // TODO: history

    private static final Image car = new Image("file:car.png");
    private static final double CAR_HEIGHT = car.getHeight();
    private static final double CAR_WIDTH = car.getWidth();
    private final Canvas canvas = new Canvas();
    private double carPos = 0;
    private double velocity;
    private long startNanoTime;
    private long roundStartNanoTime;
    private double startPos;

    private boolean run = false;
    private Boolean win;
    private final Properties settings;
    private final Round round;

    public CarWay(Properties settings, Round round) {
        this.settings = settings;
        this.round = round;
        velocity = calculateCarSpeed();
        getChildren().add(canvas);
        setMinHeight(CAR_HEIGHT);
        setCarPos(-CAR_WIDTH / 2);
    }

    private double calculateCarSpeed() {
        final String speedProperty = settings.getProperty("car.speed");
        if (!speedProperty.endsWith("s")) {
            return Double.parseDouble(speedProperty);
        }

        final double screenTime = Double.parseDouble(speedProperty.substring(0, speedProperty.length() - 1));
        return canvas.getWidth() / screenTime;
    }

    public double getCarPos() {
        return carPos;
    }

    public void startEngine() {
        if (run || win != null) {
            return;
        }
        startNanoTime = System.nanoTime();
        if (getRoundStartNanoTime() == 0) {
            roundStartNanoTime = startNanoTime;
        }
        startPos = getCarPos();
        run = true;
        round.addStep(new StepHistory(startNanoTime));
    }

    public void stopEngine() {
        if (run == false) {
            return;
        }
        run = false;
        round.getLastStep().setStopTime(System.nanoTime());
    }

    public void setCarPos(double carPos) {
        this.carPos = carPos;
        paint(canvas.getGraphicsContext2D());
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
            paint(canvas.getGraphicsContext2D());
            velocity = calculateCarSpeed();
        }
    }

    private void paint(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(car, carPos, 0);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(0, CAR_HEIGHT, canvas.getWidth(), CAR_HEIGHT);
    }

    public void update(long currentNanoTime) {
        if (!run) {
            return;
        }
        final double dt = (currentNanoTime - startNanoTime) / 1000000000.0;
        setCarPos(startPos + velocity * dt);
    }

    public long getRoundStartNanoTime() {
        return roundStartNanoTime;
    }

    public void stop() {
        if (win != null) {
            return;
        }
        if (run) {
            stopEngine();
            win = false;
        } else {
            win = true;
        }
    }

    public boolean isWin() {
        return win;
    }
}
