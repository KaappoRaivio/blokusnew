package listener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import uis.fancyttyui.Sprite;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyListener implements Runnable {
    private static volatile Key key;
    private _KeyListener wrapper;

    public KeyListener() {
        wrapper = new _KeyListener(this);
    }

    public void run () {
        disableOut();

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
            throw new RuntimeException(e);
		}

		enableOut();


		GlobalScreen.addNativeKeyListener(wrapper);

    }

    public void addKeyEventListener (KeyEventListener keyEventListener) {
        System.out.println(wrapper);
        wrapper.addKeyEventListener(keyEventListener);
    }

    public void clearAllListeners () {
        wrapper.clearAllKeyListeners();
    }

    public Key getKey () {
        synchronized (KeyListener.class) {
            if (key == null) {
                return Key.NO_KEY;
            } else {
                int ord = key.ordinal();
                key = null;
                return Key.values()[ord];
            }
        }
    }

    void updateKey (Key key) {
        synchronized (KeyListener.class) {
            KeyListener.key = key;
        }
    }

    public void close () {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
    }



    private static void disableOut () {
	    System.setOut(new java.io.PrintStream(new java.io.OutputStream() {@Override public void write(int b) {}}) {
                @Override public void flush() {}
                @Override public void close() {}
                @Override public void write(int b) {}
                @Override public void write(byte[] b) {}
                @Override public void write(byte[] buf, int off, int len) {}
                @Override public void print(boolean b) {}
                @Override public void print(char c) {}
                @Override public void print(int i) {}
                @Override public void print(long l) {}
                @Override public void print(float f) {}
                @Override public void print(double d) {}
                @Override public void print(char[] s) {}
                @Override public void print(String s) {}
                @Override public void print(Object obj) {}
                @Override public void println() {}
                @Override public void println(boolean x) {}
                @Override public void println(char x) {}
                @Override public void println(int x) {}
                @Override public void println(long x) {}
                @Override public void println(float x) {}
                @Override public void println(double x) {}
                @Override public void println(char[] x) {}
                @Override public void println(String x) {}
                @Override public void println(Object x) {}
                @Override public java.io.PrintStream printf(String format, Object... args) { return this; }
                @Override public java.io.PrintStream printf(java.util.Locale l, String format, Object... args) { return this; }
                @Override public java.io.PrintStream format(String format, Object... args) { return this; }
                @Override public java.io.PrintStream format(java.util.Locale l, String format, Object... args) { return this; }
                @Override public java.io.PrintStream append(CharSequence csq) { return this; }
                @Override public java.io.PrintStream append(CharSequence csq, int start, int end) { return this; }
                @Override public java.io.PrintStream append(char c) { return this; }
        });
    }

    private static void enableOut () {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

}
