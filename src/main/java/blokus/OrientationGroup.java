package blokus;

import java.util.ArrayList;
import java.util.List;

public class OrientationGroup {
    private boolean upFalse;
    private boolean upTrue;
    private boolean leftFalse;
    private boolean leftTrue;
    private boolean downFalse;
    private boolean downTrue;
    private boolean rightFalse;
    private boolean rightTrue;

    public OrientationGroup(boolean upFalse, boolean upTrue, boolean leftFalse, boolean leftTrue, boolean downFalse, boolean downTrue, boolean rightFalse, boolean rightTrue) {
        this.upFalse = upFalse;
        this.upTrue = upTrue;
        this.leftFalse = leftFalse;
        this.leftTrue = leftTrue;
        this.downFalse = downFalse;
        this.downTrue = downTrue;
        this.rightFalse = rightFalse;
        this.rightTrue = rightTrue;
    }

    public boolean isRelevant (Orientation orientation, boolean flip) {
        if (flip) {
            switch (orientation) {
                case UP:
                    return upTrue;
                case DOWN:
                    return downTrue;
                case LEFT:
                    return leftTrue;
                case RIGHT:
                    return rightTrue;
                default:
                    throw new RuntimeException("Invalid stuff (shouldn't happen)");
            }
        } else {
            switch (orientation) {
                case UP:
                    return upFalse;
                case DOWN:
                    return downFalse;
                case LEFT:
                    return leftFalse;
                case RIGHT:
                    return rightFalse;
                default:
                    throw new RuntimeException("Invalid stuff (shouldn't happen)");
            }
        }
    }
}
