package ru.highcode.chicken;

import java.util.LinkedList;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public class ExperimentHistory {
    private LinkedList<ExperimentStepHistory> steps;
    private final LongProperty playerNumber = new SimpleLongProperty(0);

    public ExperimentHistory() {
    }

    public long getLastScore() {
        if (steps.isEmpty()) {
            return 0;
        }
        return steps.getLast().getScore();
    }

    public long getTotalScore() {
        if (steps.isEmpty()) {
            return 0;
        }
        return steps.stream().mapToLong(ExperimentStepHistory::getScore).sum();
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

    static class ExperimentStepHistory {
        private final long startTime;
        private final long stopTime;

        public ExperimentStepHistory(long startTime, long stopTime) {
            this.startTime = startTime;
            this.stopTime = stopTime;
        }

        public long getScore() {
            return startTime - stopTime;
        }
    }
}
