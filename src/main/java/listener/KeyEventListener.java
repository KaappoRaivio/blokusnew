package listener;

import org.jnativehook.keyboard.NativeKeyEvent;

public interface KeyEventListener {
    void reportKey(NativeKeyEvent event);
}
