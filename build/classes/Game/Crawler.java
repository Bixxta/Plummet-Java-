package Game;

import Settings.Resolution;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Crawler extends Sprites {

    boolean onground = false;
    int health;
    int animationtimer;
    int currentframe;
    ImageView[] CrawlerAnimation = new ImageView[8];
    double gamewidth = Resolution.gamewidth;
    double acceleration;
    boolean flipped = true;

    public Crawler(Pane PlayLayer, ImageView BugImage, double x, double y, double g) {
        super(PlayLayer, BugImage, x, y, 0, 0, g);
        this.health = 50;
        dx = gamewidth/1920;
        createCharacter();
    }

    public void createCharacter() {
        CrawlerAnimation[0] = new ImageView(new Image("Sprites/Enemy/Spider/spider1.png", gamewidth / 13, gamewidth / 15, false, false));
        CrawlerAnimation[1] = new ImageView(new Image("Sprites/Enemy/Spider/spider2.png", gamewidth / 13, gamewidth / 15, false, false));
        CrawlerAnimation[2] = new ImageView(new Image("Sprites/Enemy/Spider/spider3.png", gamewidth / 13, gamewidth / 15, false, false));
        CrawlerAnimation[3] = new ImageView(new Image("Sprites/Enemy/Spider/spider4.png", gamewidth / 13, gamewidth / 15, false, false));
        CrawlerAnimation[4] = new ImageView(new Image("Sprites/Enemy/Spider/spider5.png", gamewidth / 13, gamewidth / 15, false, false));
        CrawlerAnimation[5] = new ImageView(new Image("Sprites/Enemy/Spider/spider6.png", gamewidth / 13, gamewidth / 15, false, false));
        CrawlerAnimation[6] = new ImageView(new Image("Sprites/Enemy/Spider/spider7.png", gamewidth / 13, gamewidth / 15, false, false));
        CrawlerAnimation[7] = new ImageView(new Image("Sprites/Enemy/Spider/spider8.png", gamewidth / 13, gamewidth / 15, false, false));
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void moveCrawler(List<Obstacle> Obstacles) {
        for (int k = 0; k < Obstacles.size(); k++) {
            Obstacle tempgeo = Obstacles.get(k);
            if (collidesWith(Obstacles.get(k))) {
                if (getY() + getH() > tempgeo.getY() && getY() <= tempgeo.getY() + tempgeo.getH()) {
                    setX(getX() - getDx());
                    setDx(getDx() * -1);
                    flipped = !flipped;
                }
                if (getX() < tempgeo.getX() || getX() + getW() > tempgeo.getX() + tempgeo.getW()) {
                    setX(getX() - getDx());
                   setDx(getDx() * -1);
                    flipped = !flipped;
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

    public void updateAnimation() {
        animationtimer++;
        if (animationtimer == 7) {
            removeFromLayer();
            setView(CrawlerAnimation[currentframe]);
            if (flipped) {
                CrawlerAnimation[currentframe].setScaleX(-1);
            } else {
                CrawlerAnimation[currentframe].setScaleX(1);
            }
            currentframe++;
            if (currentframe == 8) {
                currentframe = 0;
            }
            animationtimer = 0;
            addToLayer();
        }
    }

    @Override
    public void checkRemovability() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
