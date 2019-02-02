package uis.fancyttyui;

import com.googlecode.lanterna.TextColor;
import uis.Texel;

public interface ColorPallet {
    Texel getTexel (int color);
    Texel getBackgroundTexel();
    Texel getForegroundTexel();
    TextColor.RGB getCoordinateForegroundColor ();
    TextColor.RGB getCoordinateBackgroundColor ();

    boolean drawCoordinates ();
}
