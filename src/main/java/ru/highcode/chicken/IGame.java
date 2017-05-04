package ru.highcode.chicken;

import ru.highcode.chicken.data.Experiment;

public interface IGame {
    Experiment getExperiment();
    void nextScene();
}
