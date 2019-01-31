package uis.fancyttyui;

public class Sprite {

    private static int spriteID = 0;

    private char[][] mesh;
    private char[][] underlying;

    private int posX = -1;
    private int posY = -1;
    private boolean drawn;

    private int dimX;
    private int dimY;
    private Screen screen;


    private Sprite (char[][] mesh, int dimX, int dimY, Screen screen) {
        if (!(mesh.length == dimY && mesh[0].length == dimX)) {
            throw new RuntimeException("Invalid dimensions!");
        }

        this.mesh = mesh;
        this.underlying = new char[dimY][dimX];

        this.dimX = dimX;
        this.dimY = dimY;
        this.screen = screen;
    }

    public static Sprite fromString (String string, Screen screen) {
        int dimY = string.split("\n").length;
        int dimX = string.split("\n")[0].length();

        return new Sprite(misc.ConvertToList.convertToList(string, "\n", "", '$'), dimX, dimY, screen);
    }

    public void draw(int posX, int posY) {
        draw(posX, posY, true);
    }

    public void draw (int posX, int posY, boolean commit) {
        if (drawn) {
            throw new RuntimeException("Cannot draw twice!");
        }

        for (int y = posY; y < posY + dimY; y++) {
            for (int x = posX; x < posX + dimX; x++) {
                try {
                    underlying[y - posY][x - posX] = screen.getPixel(x, y);
                    screen.setPixel(x, y, mesh[y - posY][x - posX]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Trying to draw ouside the screen at " + x + ", " + y + "!");
                }
            }
        }

        this.posX = posX;
        this.posY = posY;
        drawn = true;

        if (commit) {
            screen.commit();
        }
    }

    public void unDraw() {
        unDraw(true);
    }

    public void unDraw (boolean commit) {
        if (!drawn || posX == -1 || posY == -1) {
            throw new RuntimeException("Cannot undraw twice!");
        }

        for (int y = posY; y < posY + dimY; y++) {
            for (int x = posX; x < posX + dimX; x++) {
                try {
                    screen.setPixel(x, y, underlying[y - posY][x - posX]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Trying to undraw ouside the screen at " + x + ", " + y + "!");
                }
            }
        }

        drawn = false;
        posX = -1;
        posY = -1;
        
        if (commit) {
            screen.commit();
        }
    }
    
    

    public void jump (int deltaX, int deltaY) {
        int oldX = posX;
        int oldY = posY;

        unDraw(false);
        draw(oldX + deltaX, oldY + deltaY);
        screen.commit();
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public static void main (String[] args) {
        Screen screen = new Terminal();
        Sprite sprite = Sprite.fromString(".....\n..a..\n..b..\n.....", screen);
        sprite.draw(0, 0);


        for (int i = 0; i < 22; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            sprite.jump(2, 1);
        }

//        screen.close();
    }
}
