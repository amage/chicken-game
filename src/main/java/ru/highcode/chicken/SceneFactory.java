package ru.highcode.chicken;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.NumberStringConverter;
import ru.highcode.chicken.data.Experiment;
import ru.highcode.chicken.data.Round;

public class SceneFactory {

    public static Scene textScene(String text, IGame switcher) {
        return textScene(text, "Продолжить", switcher);
    }

    public static Scene textScene(String text, String btnText, IGame switcher) {
        final VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        final Text t = new Text(text);
        t.setFont(new Font(24));
        t.setWrappingWidth(800);
        t.setTextAlignment(TextAlignment.JUSTIFY);
        pane.setSpacing(50);
        pane.getChildren().add(t);
        final Button nextButton = new Button(btnText);
        pane.getChildren().add(nextButton);
        nextButton.setOnAction(e -> {
            switcher.nextScene();
        });
        return new Scene(pane);
    }

    public static Scene loginScene(IGame game) {
        final GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(20);
        pane.setVgap(20);
        pane.add(new Label("Данные об испытуемом"), 0, 0, 2, 1);
        pane.add(new Label("Номер"), 0, 1);

        final TextField playerNumber = new TextField();
        playerNumber.textProperty().bindBidirectional(game.getExperiment().playerNumberProperty(),
                new NumberStringConverter());
        playerNumber.setTextFormatter(new TextFormatter<>(c -> {
            try {
                Long.parseLong(c.getControlNewText());
            } catch (final Exception e) {
                return null;
            }
            return c;
        }));
        pane.add(playerNumber, 1, 1);
        final HBox bbox = new HBox();
        final Button button = new Button("Старт");
        button.setOnAction(e -> {
            game.nextScene();
        });
        bbox.getChildren().add(button);
        bbox.setAlignment(Pos.BOTTOM_RIGHT);
        pane.add(bbox, 0, 2, 2, 1);
        final Scene result = new Scene(pane);
        return result;
    }

    public static Scene rateGameScene(String gameName, Experiment experiment, IGame switcher) {
        final GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(20);
        pane.setVgap(20);
        final String RISK_VALUATION_TEXT = "Оцени, насколько рискованным было твое поведение в прошлом раунде от 0 до 10"
                + "\n" + "(где 0 — совсем не рискованное, 10 — очень рискованное).";
        pane.add(new Label(RISK_VALUATION_TEXT), 0, 0, 2, 1);
        pane.add(new Label("Оценка"), 0, 1);
        final TextField playerGameRate = new TextField("");
        // playerNumber.textProperty().bindBidirectional(experiment.playerNumberProperty(),
        // new NumberStringConverter());
        playerGameRate.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }
            try {
                final int rate = Integer.parseInt(c.getControlNewText());
                if (rate >= 0 && rate <= 10) {
                    return c;
                }
                return null;
            } catch (final Exception e) {
                return null;
            }
        }));
        pane.add(playerGameRate, 1, 1);
        final HBox bbox = new HBox();
        final Button button = new Button("Оценить");
        button.disableProperty().bind(Bindings.isEmpty(playerGameRate.textProperty()));
        button.setOnAction(e -> {
            switcher.nextScene();
        });
        bbox.getChildren().add(button);
        bbox.setAlignment(Pos.BOTTOM_RIGHT);
        pane.add(bbox, 0, 2, 2, 1);
        return new Scene(pane);
    }

    public static Scene totalScoreScene(Experiment experiment, IGame switcher) {
        final VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(20);
        final Text scoreLabel = new Text("Вы заработали: ");
        scoreLabel.setFont(new Font(21));
        pane.getChildren().add(scoreLabel);
        final Text scoreText = new Text(String.format("%d очков", experiment.getTotalScore()));
        scoreText.setFont(new Font(42));
        pane.getChildren().add(scoreText);
        final Button nextBtn = new Button("Продолжить");
        nextBtn.setOnAction(e -> {
            switcher.nextScene();
        });
        pane.getChildren().add(nextBtn);
        return new Scene(pane);
    }

    public static Scene gameRoundResult(String gameName, Experiment experiment, IGame switcher) {
        final VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(20);
        final Round round = experiment.getRound(gameName);
        final Text message = new Text();
        if (round.isWin()) {
            message.setText(String.format("Ваш счет за раунд: %d", round.getTotalScore()));
        } else {
            message.setText(String.format(
                    "Вы попали в аварию и разбились.\n" + "Вы теряете очки за этот раунд.\n" + "У вас %d очков",
                    experiment.getTotalScore()));
        }
        message.setFont(new Font(42));
        pane.getChildren().add(message);
        final Button nextBtn = new Button("Продолжить");
        nextBtn.setOnAction(e -> {
            switcher.nextScene();
        });
        pane.getChildren().add(nextBtn);
        return new Scene(pane);
    }

    public static Scene gameScene(String gameName, Experiment experiment, IGame switcher) throws IOException {
        final GameRoundScene gscene = new GameRoundScene(gameName, experiment, switcher);
        return gscene.getScene();
    }
}
