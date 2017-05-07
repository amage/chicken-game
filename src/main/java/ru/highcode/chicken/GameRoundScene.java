package ru.highcode.chicken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ru.highcode.chicken.data.Experiment;
import ru.highcode.chicken.data.Round;

public class GameRoundScene implements IChickenScene {
    private static final long EOG_DELAY = 1;
    private final static Font BIG_FONT = new Font(26);
    private static final double CARWAY_POT_PADDING = 0;
    private Scene scene;
    private double roundTime;
    private final Properties settings = new Properties();
    private Round round;

    private long switchSceneDelay;
    private final String gameName;
    private final IGame game;

    /**
     * @param experiment
     * @param roundTime
     *            round time in seconds.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public GameRoundScene(String gameName, IGame game) {
        this.gameName = gameName;
        this.game = game;
    }

    private Scene createNewScene() {
        try {
            settings.load(new FileReader("game.cfg"));
        } catch (final IOException e1) {
            e1.printStackTrace();
        }
        final Experiment experiment = game.getExperiment();
        this.round = experiment.getRound(gameName);
        this.roundTime = Long.parseLong(settings.getProperty(gameName + ".roundTime"));

        final VBox pane = new VBox();

        final BorderPane dataLinePane = new BorderPane();
        dataLinePane.setPadding(new Insets(100, 0, 200, 0));

        final VBox wrapper = new VBox();
        final ImageView trafficLight = new ImageView(TrafficLightState.GREEN.getImage());
        wrapper.getChildren().add(trafficLight);
        wrapper.setPadding(new Insets(50, 100, 0, 200));
        // wrapper.setBorder(new Border(new BorderStroke(Color.BLUE,
        // BorderStrokeStyle.DASHED, null, null)));
        wrapper.setAlignment(Pos.CENTER_RIGHT);
        dataLinePane.setRight(wrapper);

        final CarWay carWay = new CarWay(settings, round);

        final Text currentScoreText = new Text();
        currentScoreText.setFont(BIG_FONT);
        final Text totalScoreText = new Text();
        totalScoreText.setFont(BIG_FONT);
        setScore(experiment, currentScoreText, totalScoreText);

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

                    setScore(experiment, currentScoreText, totalScoreText);
                }
                if (isRoundEnded(carWay, currentNanoTime)) {
                    if (trafficLight.getImage() != TrafficLightState.RED.getImage()) {
                        trafficLight.setImage(TrafficLightState.RED.getImage());
                    }
                    carWay.stop();
                    round.setWin(carWay.isWin());
                    if (switchSceneDelay == 0) {
                        switchSceneDelay = System.nanoTime();
                    } else {
                        // 3 sec
                        if (System.nanoTime() - switchSceneDelay > EOG_DELAY * 1000000000l) {
                            this.stop();
                            game.nextScene();
                        }
                    }
                }
            }

        };
        at.start();

        carWay.setPadding(new Insets(CARWAY_POT_PADDING, 0, 0, 0));
        pane.getChildren().add(carWay);

        final Scene scene = new Scene(pane);
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
        return scene;
    }

    private void setScore(final Experiment experiment, final Text currentScoreText, final Text totalScoreText) {
        final String format = "%,d";

        currentScoreText.setText(String.format(format, round.getTotalScore()).replace(',', ' '));
        totalScoreText
        .setText(String.format(format, experiment.getTotalScore() + round.getTotalScore()).replace(',', ' '));
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

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void activated() {
        scene = createNewScene();
    }
}
