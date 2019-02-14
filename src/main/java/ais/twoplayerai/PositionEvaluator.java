package ais.twoplayerai;

import blokus.Board;

public interface PositionEvaluator {
    double evaluatePosition (Board position, int color);
    double evaluatePosition (Board position, int color, boolean verbose);
}
