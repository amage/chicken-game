package ru.highcode.chicken.data;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public class Experiment {
    public final static long SCORE_DEVIDER = 100000;
    private final LongProperty playerNumber = new SimpleLongProperty(0);

    private final Map<String, Round> rounds = new HashMap<>();

    public Experiment() {
    }

    public Round getRound(String name) {
        rounds.putIfAbsent(name, new Round(name));
        return rounds.get(name);
    }

    public final long getPlayerNumber() {
        return playerNumber.get();
    }

    public final void setPlayerNumber(long value) {
        playerNumber.set(value);
    }

    public LongProperty playerNumberProperty() {
        return playerNumber;
    }

    public long getTotalScore() {
        long score = 0;
        for (final Round round : rounds.values()) {
            if (round.isPractics()) {
                continue;
            }
            score += round.getTotalScore();
        }
        return score;
    }
}
