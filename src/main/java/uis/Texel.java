package uis;

import com.googlecode.lanterna.TextColor;

public class Texel {
    public Texel(char value) {
        this(new TextColor.RGB(255 ,255, 255), value);
    }

    public Texel (TextColor.RGB foregroundColor, char value) {
        this(foregroundColor, new TextColor.RGB(0, 0, 0), value);
    }

    public void setForegroundColor(TextColor.RGB foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public void setBackgroundColor(TextColor.RGB backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public TextColor.RGB getForegroundColor() {
        return foregroundColor;
    }

    public TextColor.RGB getBackgroundColor() {
        return backgroundColor;
    }

    public char getValue() {
        return value;
    }

    public Texel (TextColor.RGB foregroundColor, TextColor.RGB backgroundColor, char value) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.value = value;
    }

    private TextColor.RGB foregroundColor;
    private TextColor.RGB backgroundColor;
    private char value;
}
