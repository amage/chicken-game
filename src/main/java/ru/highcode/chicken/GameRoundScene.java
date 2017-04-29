package ru.highcode.chicken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import ru.highcode.chicken.data.Experiment;
import ru.highcode.chicken.data.Round;

public class GameRoundScene {
    // TODO clean layout
    // TODO tune and restrict car speed and max round time
    // TODO refactor logic
    // TODO log writer
    private final Scene scene;
    private final double roundTime;
    private final Properties settings = new Properties();
    private final Round round;
    /**
     * @param experiment
     * @param roundTime
     *            round time in seconds.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public GameRoundScene(String gameName, Experiment experiment, ISceneSwitcher switcher)
            throws FileNotFoundException, IOException {
        settings.load(new FileReader("game.cfg"));
        this.round = experiment.getRound(gameName);
        this.roundTime = Long.parseLong(settings.getProperty(gameName + ".roundTime"));
        final VBox pane = new VBox();

        pane.setAlignment(Pos.CENTER);
        final GridPane scorePane = new GridPane();

        final Text roundTimeText = new Text("0");
        final Text currentScoreText = new Text("0");
        final Text totalScoreText = new Text("0");
        final Button nextButton = new Button("Продолжить");
        nextButton.setOnAction(e -> {
            switcher.nextScene();
        });
        nextButton.setVisible(false);

        scorePane.add(new Text("Total Bank Account: "), 0, 0);
        scorePane.add(totalScoreText, 1, 0);
        scorePane.add(new Text("Total Points Earned: "), 0, 1);
        scorePane.add(currentScoreText, 1, 1);
        scorePane.add(new Text("Time: "), 0, 2);
        scorePane.add(roundTimeText, 1, 2);

        pane.getChildren().add(scorePane);
        final ImageView trafficLight = new ImageView(TrafficLightState.GREEN.getImage());
        pane.getChildren().add(trafficLight);


        final CarWay carWay = new CarWay(settings, round);
        carWay.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.DASHED, null, null)));

        pane.getChildren().add(carWay);
        pane.getChildren().add(nextButton);

        final AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                carWay.update(currentNanoTime);
                if (isRoundStarted(carWay, currentNanoTime)) {
                    if (trafficLight.getImage() != TrafficLightState.YELLOW.getImage()) {
                        trafficLight.setImage(TrafficLightState.YELLOW.getImage());
                    }
                    final double time = (currentNanoTime - carWay.getRoundStartNanoTime()) / 1000000000.0;

                    currentScoreText.setText(String.valueOf(round.getTotalScore()));
                    totalScoreText.setText(String.valueOf(experiment.getTotalScore()));
                    ;
                    roundTimeText.setText(new DecimalFormat("#.#").format(time));
                }
                if (isRoundEnded(carWay, currentNanoTime)) {
                    if (trafficLight.getImage() != TrafficLightState.RED.getImage()) {
                        trafficLight.setImage(TrafficLightState.RED.getImage());
                    }
                    carWay.stop();
                    this.stop();
                    nextButton.setVisible(true);
                    nextButton.setFocusTraversable(false);
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
            if (time <= roundTime) {
                return true;
            }
        }
        return false;
    }

    private boolean isRoundEnded(final CarWay carWay, long currentNanoTime) {
        if (carWay.getRoundStartNanoTime() > 0) {
            final double time = (currentNanoTime - carWay.getRoundStartNanoTime()) / 1000000000.0;
            if (time > roundTime) {
                return true;
            }
        }
        return false;
    }

    public Scene getScene() {
        return scene;
    }
}
