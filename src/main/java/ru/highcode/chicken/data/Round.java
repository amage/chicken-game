package ru.highcode.chicken.data;

import java.util.LinkedList;

import ru.highcode.chicken.Game;

public class Round {
    private final String name;
    private boolean practics = false;
    private final LinkedList<StepHistory> steps = new LinkedList<>();

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

    public long getTotalScore() {
        if (steps.isEmpty()) {
            return 0;
        }
        return steps.stream().mapToLong(StepHistory::getScore).sum();
    }

    public StepHistory getLastStep() {
        return steps.getLast();
    }
}
