package ru.highcode.chicken;

import java.text.DecimalFormat;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameRoundScene {
    // TODO clean layout
    // TODO tune and restrict car speed and max round time
    // TODO refactor logic
    // TODO log writer
    private final Scene scene;
    private final double totalTime;

    /**
     * @param totalTime
     *            round time in seconds.
     */
    public GameRoundScene(double totalTime) {
        this.totalTime =totalTime;
        final VBox pane = new VBox();

        pane.setAlignment(Pos.CENTER);
        final GridPane scorePane = new GridPane();

        final Text roundTimeText = new Text("0");
        final Text currentScoreText = new Text("0");
        final Text totalScoreText = new Text("0");

        scorePane.add(new Text("Total Bank Account: "), 0, 0);
        scorePane.add(totalScoreText, 1, 0);
        scorePane.add(new Text("Total Points Earned: "), 0, 1);
        scorePane.add(currentScoreText, 1, 1);
        scorePane.add(new Text("Time: "), 0, 2);
        scorePane.add(roundTimeText, 1, 2);

        pane.getChildren().add(scorePane);
        final ImageView trafficLight = new ImageView(TrafficLightState.GREEN.getImage());
        pane.getChildren().add(trafficLight);

        final CarWay carWay = new CarWay(10);
        carWay.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.DASHED, null, null)));

        pane.getChildren().add(carWay);

        final AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                carWay.update(currentNanoTime);
                if (isRoundStarted(carWay, currentNanoTime)) {
                    if (trafficLight.getImage() != TrafficLightState.YELLOW.getImage()) {
                        trafficLight.setImage(TrafficLightState.YELLOW.getImage());
                    }
                    final double time = (currentNanoTime - carWay.getRoundStartNanoTime()) / 1000000000.0;
                    roundTimeText.setText(new DecimalFormat("#.#").format(time));
                }
                if (isRoundEnded(carWay, currentNanoTime)) {
                    if (trafficLight.getImage() != TrafficLightState.RED.getImage()) {
                        trafficLight.setImage(TrafficLightState.RED.getImage());
                    }
                    carWay.stop();
                    this.stop();
                    if (carWay.isWin()) {
                        roundTimeText.setText("WIN!!!");
                    } else {
                        roundTimeText.setText("LOOSE!!!");
                    }
                }
            }

        };
        at.start();

        scene = new Scene(pane, 800, 600);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                carWay.startEngine();
            }
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                carWay.stopEngine();
            }
        });

    }

    private boolean isRoundStarted(final CarWay carWay, long currentNanoTime) {
        if (carWay.getRoundStartNanoTime() > 0) {
            final double time = (currentNanoTime - carWay.getRoundStartNanoTime()) / 1000000000.0;
            if (time <= totalTime) {
                return true;
            }
        }
        return false;
    }

    private boolean isRoundEnded(final CarWay carWay, long currentNanoTime) {
        if (carWay.getRoundStartNanoTime() > 0) {
            final double time = (currentNanoTime - carWay.getRoundStartNanoTime()) / 1000000000.0;
            if (time > totalTime) {
                return true;
            }
        }
        return false;
    }

    public Scene getScene() {
        return scene;
    }
}