package ru.highcode.chicken;

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
import ru.highcode.chicken.data.Round;

public class SceneFactory {

    public static IChickenScene textScene(String text, IGame switcher) {
        return textScene(text, "Продолжить", switcher);
    }

    public static IChickenScene textScene(String text, String btnText, IGame switcher) {
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
        return IChickenScene.wrap(new Scene(pane));
    }

    public static IChickenScene loginScene(IGame game) {
        final GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(20);
        pane.setVgap(20);
        pane.add(new Label("Данные об испытуемом"), 0, 0, 2, 1);
        pane.add(new Label("Номер"), 0, 1);

        final TextField playerNumber = new TextField();
        playerNumber.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }
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
        button.disableProperty().bind(Bindings.isEmpty(playerNumber.textProperty()));
        button.setOnAction(e -> {
            game.getExperiment().setPlayerNumber(Long.parseLong(playerNumber.getText()));
            game.nextScene();
        });
        bbox.getChildren().add(button);
        bbox.setAlignment(Pos.BOTTOM_RIGHT);
        pane.add(bbox, 0, 2, 2, 1);
        return new IChickenScene() {
            final Scene scene = new Scene(pane);

            @Override
            public Scene getScene() {
                return scene;
            }

            @Override
            public void activated() {
                playerNumber.textProperty().set("");
            }
        };
    }

    public static IChickenScene rateGameScene(String gameName, IGame game) {
        final GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(20);
        pane.setVgap(20);
        final String RISK_VALUATION_TEXT = "Оцени, насколько рискованным было твое поведение в прошлом раунде от 0 до 10"
                + "\n" + "(где 0 — совсем не рискованное, 10 — очень рискованное).";
        pane.add(new Label(RISK_VALUATION_TEXT), 0, 0, 2, 1);
        pane.add(new Label("Оценка"), 0, 1);
        final TextField playerGameRate = new TextField("");
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
            game.getExperiment().getRound(gameName).setRisk(Integer.parseInt(playerGameRate.textProperty().get()));
            game.nextScene();
        });
        bbox.getChildren().add(button);
        bbox.setAlignment(Pos.BOTTOM_RIGHT);
        pane.add(bbox, 0, 2, 2, 1);
        return new IChickenScene() {
            private final Scene scene = new Scene(pane);

            @Override
            public Scene getScene() {
                return scene;
            }

            @Override
            public void activated() {
                playerGameRate.setText("");
            }
        };
    }

    public static IChickenScene totalScoreScene(IGame game) {
        final VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(20);
        final Text scoreLabel = new Text("Вы заработали: ");
        scoreLabel.setFont(new Font(21));
        pane.getChildren().add(scoreLabel);
        final Text scoreText = new Text();
        scoreText.setFont(new Font(42));
        pane.getChildren().add(scoreText);
        final Button nextBtn = new Button("Продолжить");
        nextBtn.setOnAction(e -> {
            game.nextScene();
        });
        pane.getChildren().add(nextBtn);
        return new IChickenScene() {
            private final Scene scene = new Scene(pane);

            @Override
            public Scene getScene() {
                return scene;
            }

            @Override
            public void activated() {
                scoreText.setText(String.format("%d очков", game.getExperiment().getTotalScore()));
            }
        };
    }

    public static IChickenScene gameRoundResult(String gameName, IGame game) {
        final VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(20);
        final Text message = new Text();

        message.setFont(new Font(42));
        pane.getChildren().add(message);
        final Button nextBtn = new Button("Продолжить");
        nextBtn.setFocusTraversable(false);
        nextBtn.setOnAction(e -> {
            game.nextScene();
        });
        pane.getChildren().add(nextBtn);
        return new IChickenScene() {
            private final Scene scene = new Scene(pane);

            @Override
            public Scene getScene() {
                return scene;
            }

            @Override
            public void activated() {
                final Round round = game.getExperiment().getRound(gameName);
                if (round.isWin()) {
                    message.setText(String.format("Ваш счет за раунд: %d",
                            round.getTotalScore()));
                } else {
                    message.setText(String.format(
                            "Вы попали в аварию и разбились.\n" + "Вы теряете очки за этот раунд.\n" + "У вас %d очков",
                            game.getExperiment().getTotalScore()));
                }
            }
        };
    }

    public static IChickenScene gameScene(String gameName, IGame switcher) {
        return new GameRoundScene(gameName, switcher);
    }
}
