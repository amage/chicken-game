package ru.highcode.chicken.data;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import ru.highcode.chicken.Game;

public class Experiment {
    public final static long SCORE_DEVIDER = 100000;
    private String playerName;

    private final Map<String, Round> rounds = new HashMap<>();

    public Experiment() {
    }

    public Round getRound(String name) {
        rounds.putIfAbsent(name, new Round(name));
        return rounds.get(name);
    }

    public long getTotalScoreView() {
        return (long) (getTotalScore() * Game.SCORE_RATE);
    }

    public long getTotalScore() {
        long score = 0;
        for (final Round round : rounds.values()) {
            if (round.isPractics() || !round.isWin()) {
                continue;
            }
            score += round.getTotalScore();
        }
        return score;
    }

    public void save(String fileName) {
        final CSVResultLine line = prepareCSV();

        try (final FileWriter fileWriter = new FileWriter(fileName, true)) {
            @SuppressWarnings("unchecked")
            final StatefulBeanToCsv<CSVResultLine> csvBind = new StatefulBeanToCsvBuilder<CSVResultLine>(fileWriter)
            .build();
            csvBind.write(line);
        } catch (final IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }
    }

    private CSVResultLine prepareCSV() {
        final CSVResultLine line = new CSVResultLine();
        line.setPid(String.valueOf(getPlayerName()));
        for (int i = 1; i <= 15; i++) {
            final Round round = getRound(String.valueOf(i));
            try {
                final Method mScore = CSVResultLine.class.getMethod("setScore" + i, String.class);
                final Method mStarts = CSVResultLine.class.getMethod("setStarts" + i, String.class);
                final Method mRed = CSVResultLine.class.getMethod("setRed" + i, String.class);
                final Method mRisk = CSVResultLine.class.getMethod("setRisk" + i, String.class);

                mScore.invoke(line, String.valueOf(round.getTotalScore()));
                mStarts.invoke(line, String.valueOf(round.getStepsCount()));
                mRed.invoke(line, round.isWin() ? "0" : "1");
                mRisk.invoke(line, String.valueOf(round.getRisk()));
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
