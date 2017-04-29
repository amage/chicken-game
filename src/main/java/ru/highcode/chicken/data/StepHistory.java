package ru.highcode.chicken.data;

public class StepHistory {
    private final long startTime;
    private long stopTime = 0;

    public StepHistory(long startTime) {
        this.startTime = startTime;
    }

    public void setStopTime(long stopTime) {
        if (this.stopTime != 0) {
            throw new IllegalStateException();
        }
        this.stopTime = stopTime;
    }
    public long getScore() {
        if (stopTime == 0) {
            return (System.nanoTime() - startTime) / Experiment.SCORE_DEVIDER;
        }
        return (stopTime - startTime) / Experiment.SCORE_DEVIDER;
    }
}