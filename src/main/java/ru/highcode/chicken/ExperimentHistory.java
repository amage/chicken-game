package ru.highcode.chicken;

import java.util.LinkedList;

public class ExperimentHistory {
    private LinkedList<ExperimentStepHistory> steps;

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
