package ir.ac.kntu.logic;

import java.io.Serializable;

public interface Movable extends Serializable {
    void move(Direction direction);
}
