package Game;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Obstacle extends Sprites {

    boolean used = false;
    int obstacleid;

    public Obstacle(Pane LevelLayer, ImageView ObstacleSprite, double x, double y, int obstacleid) {
        super(LevelLayer, ObstacleSprite, x, y, 0, 0, 0);
        this.obstacleid = obstacleid;
    }

    public int GetId() {
        return obstacleid;
    }

    public void setUsed(boolean isused) {
        used = isused;
    }

    public boolean getUsed() {
        return used;
    }

    @Override
    public void checkRemovability() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
