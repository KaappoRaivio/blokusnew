package listener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

class _KeyListener implements NativeKeyListener {
    private KeyListener wrapper;
    private boolean verbose;

    public _KeyListener(KeyListener wrapper) {
        this(wrapper, false);
    }

    _KeyListener (KeyListener wrapper, boolean verbose) {
        this.wrapper = wrapper;
        this.verbose = verbose;
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("asdasdasd" + NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase());
        Key key;
        switch (NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase()) {
            case "up":
                key = Key.UP;
                break;
            case "down":
                key = Key.DOWN;
                break;
            case "left":
                key = Key.LEFT;
                break;
            case "right":
                key = Key.RIGHT;
                break;
            case "enter":
                key = Key.ENTER;
                break;
            case "left control":
                key = Key.CTRL;
                break;
            case "right control":
                key = Key.CTRL;
                break;
            case "left shift":
                key = Key.SHIFT;
                break;
            case "right shift":
                key = Key.SHIFT;
                break;
            default:
                key = Key.NO_KEY;
        }

        wrapper.updateKey(key);

        if (verbose) {
		    System.out.println("Key pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + " " +  e.getKeyCode());
        }
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
        if (verbose) {
		    System.out.println("Key released: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + " " +  e.getKeyCode());
        }
    }

	public void nativeKeyTyped(NativeKeyEvent e) {
        if (verbose) {
		    System.out.println("Key typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + " " +  e.getKeyCode());
        }
    }
}
