package uis;

import uis.fancyttyui.ColorPallet;

public interface Texelizeable {
    Texel[][] texelize (ColorPallet colorPallet, int scaleX, int scaleY);
//    int getScaleX ();
//    int getScaleY ();
//
//    void setScaleX (int newScaleX);
//    void setScaleY (int newScaleY);
}
