package blokus;

import com.googlecode.lanterna.TextColor;
import uis.Texel;
import uis.fancyttyui.ColorPallet;

public class PiecePallet extends DefaultPallet {
    @Override
    public Texel getTexel (int color) {
        switch (color) {
            case 0:
                return new Texel(new TextColor.RGB(0, 0, 180), '▓');
            case 1:
                return new Texel(new TextColor.RGB(180, 110, 0), '▓');
            default:
                return getBackgroundTexel();
        }
    }
}
