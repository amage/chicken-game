package ru.highcode.chicken;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
        pane.add(new Label("ФИО"), 0, 1);

        final TextField playerNumber = new TextField();
        pane.add(playerNumber, 1, 1);
        final HBox bbox = new HBox();
        final Button button = new Button("Старт");
        button.disableProperty().bind(Bindings.isEmpty(playerNumber.textProperty()));
        button.setOnAction(e -> {
            game.getExperiment().setPlayerName(playerNumber.getText());
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
        final String RISK_VALUATION_TEXT = "Оценте, насколько рискованным было Ваше поведение в прошлом раунде от 1 до 10"
                + "\n" + "(где 1 — совсем не рискованное, 10 — очень рискованное).";
        final Label label = new Label(RISK_VALUATION_TEXT);
        label.setFont(Game.BIG_FONT);
        pane.add(label, 0, 0, 2, 1);

        final HBox ratePane = new HBox();
        ratePane.setAlignment(Pos.CENTER);

        final Slider slider = new Slider(1, 10, 5);
        HBox.setHgrow(slider, Priority.ALWAYS);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
        slider.setBlockIncrement(1);
        slider.setFocusTraversable(true);

        ratePane.getChildren().add(slider);
        ratePane.setPadding(new Insets(20, 300, 0, 300));
        pane.add(ratePane, 0, 1, 2, 1);

        final HBox bbox = new HBox();
        final Button button = new Button("Оценить");
        button.setOnAction(e -> {
            game.getExperiment().getRound(gameName).setRisk(slider.valueProperty().getValue().intValue());
            game.nextScene();
        });
        bbox.getChildren().add(button);
        bbox.setAlignment(Pos.BOTTOM_RIGHT);
        pane.add(bbox, 0, 2, 2, 1);
        return new IChickenScene() {
            private final Scene scene = new Scene(pane);

            @Override
            public Scene getScene() {
                scene.getStylesheets().add("file:style.css");
                return scene;
            }

            @Override
            public void activated() {
                slider.setValue(5);
            }
        };
    }

    public static IChickenScene totalScoreScene(IGame game) {
        final VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(20);
        final Text scoreLabel = new Text("Игра закончена!\n\nВы заработали: ");
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
                scoreText.setText(String.format("%d очков", game.getExperiment().getTotalScoreView()));
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
                final StringBuilder sb = new StringBuilder();
                if (round.isWin()) {
                    sb.append(String.format("Ура! Вы не попали на красный свет!\n\nОчки за раунд: %d",
                            round.getTotalScoreView()));
                    if (!round.isPractics()) {
                        sb.append("\n");
                        sb.append(String.format("Общие очки за игру: %d", game.getExperiment().getTotalScoreView()));
                    }

                } else {
                    sb.append("Вы попали в аварию и разбились.\n");
                    if (!round.isPractics()) {
                        sb.append("\nВы теряете очки за этот раунд.\n");
                        sb.append(String.format("Общие очки за игру: %d", game.getExperiment().getTotalScoreView()));
                    }
                }
                message.setText(sb.toString());
            }
        };
    }

    public static IChickenScene gameScene(String gameName, IGame switcher) {
        return new GameRoundScene(gameName, switcher);
    }
}
