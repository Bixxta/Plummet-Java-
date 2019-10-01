package Game;

import java.util.BitSet;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

//This is where we check inputs
public class Input {
    public BitSet keyboardBitSet = new BitSet();

    private final KeyCode upKey = KeyCode.W;
    private final KeyCode downKey = KeyCode.S;
    private final KeyCode leftKey = KeyCode.A;
    private final KeyCode rightKey = KeyCode.D;
    private final KeyCode jump = KeyCode.SPACE;
    private final KeyCode flip = KeyCode.F;
    private final KeyCode pause = KeyCode.ESCAPE;
    
    Scene scene;

    public Input(Scene scene) {
        this.scene = scene;
    }

    public void addListeners() {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
    }

    private final EventHandler<KeyEvent> keyPressedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            keyboardBitSet.set(event.getCode().ordinal(), true);

        }
    };
    private final EventHandler<KeyEvent> keyReleasedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            keyboardBitSet.set(event.getCode().ordinal(), false);
        }
    };
    
    public boolean isMoveUp() {
        return keyboardBitSet.get(upKey.ordinal()) && !keyboardBitSet.get(downKey.ordinal());
    }

    public boolean isMoveDown() {
        return keyboardBitSet.get(downKey.ordinal()) && !keyboardBitSet.get(upKey.ordinal());
    }

    public boolean isMoveLeft() {
        
        return keyboardBitSet.get(leftKey.ordinal()) && !keyboardBitSet.get(rightKey.ordinal());
    }

    public boolean isMoveRight() {
        return keyboardBitSet.get(rightKey.ordinal()) && !keyboardBitSet.get(leftKey.ordinal());
    }

    public boolean isJumping() {
        return keyboardBitSet.get(jump.ordinal());
    }
    
    public boolean isFlipping() {
        return keyboardBitSet.get(flip.ordinal());
    }
    public boolean isPausing() {
        return keyboardBitSet.get(pause.ordinal());
    }
}
