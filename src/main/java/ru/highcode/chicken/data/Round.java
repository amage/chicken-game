package ru.highcode.chicken.data;

import java.util.LinkedList;

import ru.highcode.chicken.Game;

public class Round {
    private final String name;
    private boolean practics = false;
    private final LinkedList<StepHistory> steps = new LinkedList<>();
    private Boolean win = null;
    private int risk;

    public Round(String name) {
        this.name = name;
        if (Game.PRACTICS_NAME_1.equals(name)) {
            practics = true;
        }
        if (Game.PRACTICS_NAME_2.equals(name)) {
            practics = true;
        }

    }

    public String getName() {
        return name;
    }

    public boolean isPractics() {
        return practics;
    }

    public void setPractics(boolean practics) {
        this.practics = practics;
    }

    public void addStep(StepHistory step) {
        steps.addLast(step);
    }

    public long getTotalScoreView() {
        return (long) (getTotalScore() * Game.SCORE_RATE);
    }

    public long getTotalScore() {
        if (steps.isEmpty()) {
            return 0;
        }
        return steps.stream().mapToLong(StepHistory::getScore).sum();
    }

    public StepHistory getLastStep() {
        return steps.getLast();
    }

    public int getStepsCount() {
        return steps.size();
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public boolean isWin() {
        return win == null? false : win;
    }

    public int getRisk() {
        return risk;
    }

    public void setRisk(int risk) {
        this.risk = risk;
    }
}
