package listener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

class _KeyListener implements NativeKeyListener {
    private KeyListener wrapper;
    private boolean verbose;
    private List<KeyEventListener> keyEventListeners = new Vector<>();

    public _KeyListener(KeyListener wrapper) {
        this(wrapper, false);
    }

    public void addKeyEventListener (KeyEventListener keyEventListener) {
        keyEventListeners.add(keyEventListener);
    }

    public void clearAllKeyListeners () {
        keyEventListeners = new Vector<>();
    }

    private _KeyListener (KeyListener wrapper, boolean verbose) {
        this.wrapper = wrapper;
        this.verbose = verbose;
    }

    private synchronized void broadcastKeyEvent (NativeKeyEvent e) {
        for (KeyEventListener keyEventListener : keyEventListeners) {
            keyEventListener.reportKey(e);
        }
    }


    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        broadcastKeyEvent(e);


        if (verbose) {
		    System.out.println("Key pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + " " +  e.getKeyCode());
        }
	}

    @Override
	public void nativeKeyReleased(NativeKeyEvent e) {
        if (verbose) {
		    System.out.println("Key released: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + " " +  e.getKeyCode());
        }
    }


    @Override
	public void nativeKeyTyped(NativeKeyEvent e) {
        if (verbose) {
		    System.out.println("Key typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + " " +  e.getKeyCode());
        }
    }
}
