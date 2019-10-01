package Game;

import static Settings.Resolution.gamewidth;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Experiment extends Sprites {

    int health;
    boolean onground;
    boolean isvulnerable;
    int hitcounter = 5;
    double acceleration;

    public Experiment(Pane PlayLayer, ImageView LizImage, double x, double y, double g) {
        super(PlayLayer, LizImage, x, y, 0, 0, g);
        this.health = 200;
        dx = gamewidth / 600;
        isvulnerable = false;
        acceleration = g * 1.2;
        createCharacter();
    }

    public void createCharacter() {

    }

    public Projectile smallprojectile(Pane BulletLayer) {
        ImageView tempproj = new ImageView(new Image("Sprites/Enemy/Boss1/enemyprojectile.png", gamewidth / 25, gamewidth / 25, false, false));
        tempproj.setFitHeight(gamewidth / 25);
        tempproj.setFitWidth(gamewidth / 25);
        return new Projectile(BulletLayer, tempproj, getX() + getW() / 8, getY() + getH() / 3, getR(), gamewidth / 200, true);
    }

    public Projectile bigprojectile(Pane BulletLayer) {
        ImageView tempproj = new ImageView(new Image("Sprites/Enemy/Boss1/enemyprojectile.png", gamewidth / 15, gamewidth / 15, false, false));
        tempproj.setFitHeight(gamewidth / 15);
        tempproj.setFitWidth(gamewidth / 15);
        return new Projectile(BulletLayer, tempproj, getX(), getY() + getH() / 3, getR(), gamewidth / 100, true);
    }

    public void setVuln(boolean vulnerablility) {
        isvulnerable = vulnerablility;
        if (isvulnerable) {
            hitcounter = 5;
        }
    }

    public boolean getVuln() {
        return isvulnerable;
    }

    public void setHealth(int health) {
        this.health = health;
        hitcounter--;
        if (hitcounter == 0) {
            isvulnerable = false;
        }
    }

    public int getHealth() {
        return health;
    }

    public void moveall() {
        setDy(acceleration);
        move();
        updateUI();
    }

    public void flip() {
        setG(-getG());
        acceleration = getG() * 1.2;
        ImageView tempimage = getView();
        tempimage.setScaleY(-tempimage.getScaleY());
        setView(tempimage);
        setDx(0);
    }

    public void moveMonster(List<Obstacle> Obstacles) {
        for (int k = 0; k < Obstacles.size(); k++) {
            Obstacle tempgeo = Obstacles.get(k);
            if (collidesWith(Obstacles.get(k))) {
                if (getY() + getH() > tempgeo.getY() && getY() <= tempgeo.getY() + tempgeo.getH()) {
                    setX(getX() - getDx());
                    setDx(getDx() * -1);
                }
                if (getX() < 2 * gamewidth / 3) {
                    setX(getX() - getDx());
                    setDx(getDx() * -1);
                }
                if (getX() + getW() > tempgeo.getX() && getX() <= tempgeo.getX() + tempgeo.getW()) {
                    onground = true;
                    setGy(0);
                    setDy(0);
                    setY(tempgeo.getY() - getH());
                }
            }
        }
        if (onground == false) {
            setGy(getGy() + g);
        }
        move();
    }

    @Override
    public void checkRemovability() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
