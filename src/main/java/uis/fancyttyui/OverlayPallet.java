package uis.fancyttyui;

import com.googlecode.lanterna.TextColor;
import uis.Texel;

public class OverlayPallet extends DefaultPallet {
    @Override
    public Texel getTexel(int color) {
            switch (color) {
            case 0:
                return new Texel(new TextColor.RGB(0, 200, 255), '▓');
            case 1:
                return new Texel(new TextColor.RGB(255, 200, 0), '▓');
            case -1:
                return new Texel(new TextColor.RGB(200, 200, 200), '▓');
            default:
                return getBackgroundTexel();
        }
    }

}
