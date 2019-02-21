package uis;

import uis.fancyttyui.ColorPallet;

public interface Texelizeable {
    Texel[][] texelize (ColorPallet colorPallet, int scaleX, int scaleY);
}
