package ru.highcode.chicken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ru.highcode.chicken.data.Experiment;
import ru.highcode.chicken.data.Round;

public class GameRoundScene {
    // TODO clean layout
    // TODO log writer
    private final static Font BIG_FONT = new Font(26);
    private final Scene scene;
    private final double roundTime;
    private final Properties settings = new Properties();
    private final Round round;
    // private final Image winImage = new Image("file:win.png");
    // private final Image failImage = new Image("file:fail.png");
    private final Image emptyImage = new Image("file:empty.png");

    private long switchSceneDelay;
    /**
     * @param experiment
     * @param roundTime
     *            round time in seconds.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public GameRoundScene(String gameName, Experiment experiment, IGame switcher)
            throws FileNotFoundException, IOException {
        settings.load(new FileReader("game.cfg"));
        this.round = experiment.getRound(gameName);
        this.roundTime = Long.parseLong(settings.getProperty(gameName + ".roundTime"));

        final VBox pane = new VBox();

        final BorderPane dataLinePane = new BorderPane();
        dataLinePane.setPadding(new Insets(200, 0, 200, 0));

        final ImageView resultImage = new ImageView(emptyImage);
        resultImage.minHeight(256);
        dataLinePane.setCenter(resultImage);

        final VBox wrapper = new VBox();
        final ImageView trafficLight = new ImageView(TrafficLightState.GREEN.getImage());
        wrapper.getChildren().add(trafficLight);
        wrapper.setPadding(new Insets(50, 100, 0, 200));
        // wrapper.setBorder(new Border(new BorderStroke(Color.BLUE,
        // BorderStrokeStyle.DASHED, null, null)));
        wrapper.setAlignment(Pos.CENTER_RIGHT);
        dataLinePane.setRight(wrapper);

        final CarWay carWay = new CarWay(settings, round);


        final Text currentScoreText = new Text("0");
        currentScoreText.setFont(BIG_FONT);
        final Text totalScoreText = new Text("0");
        totalScoreText.setFont(BIG_FONT);

        dataLinePane.setLeft(createScorePane(currentScoreText, totalScoreText));


        carWay.setPadding(new Insets(0, 0, 200, 0));
        final VBox bottom = new VBox();
        bottom.getChildren().add(carWay);

        pane.getChildren().add(dataLinePane);
        pane.getChildren().add(bottom);

        final AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                carWay.update(currentNanoTime);
                if (isRoundStarted(carWay, currentNanoTime)) {
                    if (trafficLight.getImage() != TrafficLightState.YELLOW.getImage()) {
                        trafficLight.setImage(TrafficLightState.YELLOW.getImage());
                    }

                    currentScoreText.setText(String.valueOf(round.getTotalScore()));
                    totalScoreText.setText(String.valueOf(experiment.getTotalScore()));
                }
                if (isRoundEnded(carWay, currentNanoTime)) {
                    if (trafficLight.getImage() != TrafficLightState.RED.getImage()) {
                        trafficLight.setImage(TrafficLightState.RED.getImage());
                    }
                    carWay.stop();

                    // if (carWay.isWin()) {
                    // resultImage.setImage(winImage);
                    // } else {
                    // resultImage.setImage(failImage);
                    // }
                    if(switchSceneDelay == 0) {
                        switchSceneDelay = System.nanoTime();
                    } else {
                        // 3 sec
                        if(System.nanoTime() - switchSceneDelay >   3 * 1000000000l) {
                            this.stop();
                            switcher.nextScene();
                        }
                    }
                }
            }

        };
        at.start();

        carWay.setPadding(new Insets(100, 0, 0, 0));
        pane.getChildren().add(carWay);

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

    private GridPane createScorePane(final Text currentScoreText, final Text totalScoreText) {
        final GridPane scorePane = new GridPane();
        if (!round.isPractics()) {
            final Text totalScoreLabel = new Text("Очки за всю игру: ");
            totalScoreLabel.setFont(BIG_FONT);
            scorePane.add(totalScoreLabel, 0, 1);
            scorePane.add(totalScoreText, 1, 1);
        }

        final Text roundScoreLabel = new Text("Очки за раунд: ");
        roundScoreLabel.setFont(BIG_FONT);
        scorePane.add(roundScoreLabel, 0, 0);
        scorePane.add(currentScoreText, 1, 0);
        scorePane.setPadding(new Insets(60, 20, 0, 20));
        // scorePane.setBorder(new Border(new BorderStroke(Color.BLUE,
        // BorderStrokeStyle.DASHED, null, null)));
        return scorePane;
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
