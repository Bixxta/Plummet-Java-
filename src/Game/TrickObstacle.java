package Game;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class TrickObstacle extends Sprites {

    boolean used = false;
    int obstacleid;
    
    public TrickObstacle(Pane layer, ImageView image, double x, double y, int tricktype) {
        super(layer, image, x, y, 0, 0, 0);
        obstacleid = tricktype;
    }

    public void setUsed(boolean isused) {
        used = isused;
    }

    public boolean getUsed() {
        return used;
    }
    
    public int GetId() {
        return obstacleid;
    }

    @Override
    public void checkRemovability() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
