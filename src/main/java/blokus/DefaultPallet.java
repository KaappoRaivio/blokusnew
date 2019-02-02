package blokus;

import com.googlecode.lanterna.TextColor;
import uis.Texel;
import uis.fancyttyui.ColorPallet;

public class DefaultPallet implements ColorPallet {
    @Override
    public Texel getTexel(int color) {
        switch (color) {
            case 0:
                return new Texel(new TextColor.RGB(0, 0, 255), '█');
            case 1:
                return new Texel(new TextColor.RGB(255, 127, 0), '█');
            default:
                return getBackgroundTexel();
        }
    }

    @Override
    public Texel getBackgroundTexel() {
        return new Texel(new TextColor.RGB(84, 84, 84), '█'); //░
    }

    @Override
    public Texel getForegroundTexel() {
        return new Texel(new TextColor.RGB(32, 32, 32), '▒');
    }

    @Override
    public TextColor.RGB getCoordinateForegroundColor() {
        return new TextColor.RGB(255, 255, 255);
    }

    @Override
    public TextColor.RGB getCoordinateBackgroundColor() {
        return new TextColor.RGB(0, 0, 0);
    }

    @Override
    public boolean drawCoordinates() {
        return true;
    }
}

