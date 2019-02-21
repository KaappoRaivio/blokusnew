package blokus;

import com.googlecode.lanterna.TextColor;
import uis.Texel;
import uis.fancyttyui.DefaultPallet;

public class PiecePallet extends DefaultPallet {
    @Override
    public Texel getTexel (int color) {
//        switch (color) {
//            case 0:
//                return new Texel(new TextColor.RGB(0, 0, 180), '▓');
//            case 1:
//                return new Texel(new TextColor.RGB(180, 110, 0), '▓');
//            default:
//                return getBackgroundTexel();
//        }
        Texel texel = super.getTexel(color);
        var foregroundColor = texel.getForegroundColor();
        var backgroundColor = texel.getBackgroundColor();
        return new Texel(dim(foregroundColor, 50), dim(backgroundColor, 10), '▓');

    }

    private static TextColor.RGB dim (TextColor.RGB original, int amount) {
        int r = Math.max(original.getRed() - amount, 0);
        int g = Math.max(original.getGreen() - amount, 0);
        int b = Math.max(original.getBlue() - amount, 0);

        return new TextColor.RGB(r, g, b);
    }
}
