package minesweeper.consoleui;

import minesweeper.core.Field;

public interface UserInterface {
    void Play(Field field);

    void update();
}
