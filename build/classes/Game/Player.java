package Game;

import java.io.File;
import java.util.List;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class Player extends Sprites {

    Input input;
    double speed;
    //JUMP/MOVEMENT PARAMETERS
    boolean onground;
    boolean jumpinprogress;
    double airvelocity;
    double acceleration;
    boolean flipped = false;
    boolean movingleft = false;
    boolean hasjumped = false;
    boolean flippedpressed = false;
    boolean CrystalObtained = false;
    int health;
    //Media footstep = new Media(new File("src/Sounds/footsteps.mp3").toURI().toString());
    //MediaPlayer footloop = new MediaPlayer(footstep);

    public Player(Pane Playlayer, ImageView bimage, double x, double y, double g, double speed, Input input) {
        super(Playlayer, bimage, x, y, 0, 0, g);
        this.speed = speed;
        this.input = input;
        acceleration = g * 1.2;
        health = 100;
    }

    public void processInput() {
        // horizontal
        if (input.isMoveLeft()) {
            dx = -speed;
            movingleft = true;
        } else if (input.isMoveRight()) {
            dx = speed;
            movingleft = false;
            if (onground) {
                //if (footloop.getStatus().equals(Status.PLAYING) == false) {
                // footloop.play();
                //}
            }
        } else {
            dx = 0;
        }
        //upon first true, start a one second timer in which the button can continually be held; on end, stop input until ground is hit again
        if (input.isJumping()) {
            if (onground) {
                if (hasjumped == false) {
                    jumpinprogress = true;
                    hasjumped = true;
                    airvelocity = -getG() * 40;
                } else {
                    airvelocity = 0;
                }
            } else if (onground == false) {
                if (jumpinprogress == true) {
                    airvelocity += acceleration;
                    if (airvelocity == 0) {
                        jumpinprogress = false;
                    }
                }
            }
        } else if (input.isJumping() == false) {
            if (onground == false) {
                airvelocity = 0;
            } else {
                airvelocity = 0;
                if (hasjumped = true) {
                    hasjumped = false;
                }
            }
        }
        dy = airvelocity;
        if (CrystalObtained) {
            if (input.isFlipping()) {
                if (flippedpressed == false) {
                    if (onground) {
                        setG(-getG());
                        setY(getY() + getG() * 1.2); //move a pixel away so not onground
                        acceleration = getG() * 1.2;
                        flippedpressed = true;
                        flipped = !flipped;
                    }
                } else {
                    if (onground == false) {
                        flippedpressed = false;
                    }
                }
            }
        }
        //footloop.setOnEndOfMedia(() -> {
        //footloop.stop();
        //});
    }

    public void checkMotions() {
        ImageView flippedsprite = (getView());
        if (flipped) {
            flippedsprite.setScaleY(-1);
        } else if (flipped == false) {
            flippedsprite.setScaleY(1);
        }
        if (movingleft) {
            flippedsprite.setScaleX(-1);
        } else if (movingleft == false) {
            flippedsprite.setScaleX(1);
        }
        setView(flippedsprite);
        move();
    }

    public void checkCollisions(List<Obstacle> Obstacles) {
        boolean ongroundthisframe = false;
        for (int k = 0; k < Obstacles.size(); k++) {
            Obstacle tempgeo = Obstacles.get(k);
            if (collidesWith(Obstacles.get(k))) {
                if (flipped == false && getY() + getH() > tempgeo.getY() && getY() <= tempgeo.getY() + tempgeo.getH()) {
                    setX(getX() - getDx());
                } else if (flipped == true && getY() + getH() > tempgeo.getY() && getY() < tempgeo.getY() + tempgeo.getH()) {
                    setX(getX() - getDx());
                }
                if (getX() + getW() > tempgeo.getX() && getX() <= tempgeo.getX() + tempgeo.getW()) {
                    if (flipped == false) {
                        if (getY() > tempgeo.getY()) {
                            setY(getY() - getDy());
                            airvelocity = 0;
                        } else if (getY() <= tempgeo.getY()) {
                            onground = true;
                            ongroundthisframe = true;
                            setGy(0);
                            setY(tempgeo.getY() - getH());
                        }
                    } else {
                        if (getY() < tempgeo.getY()) {
                            setY(getY() - getDy());
                        } else if (getY() >= tempgeo.getY()) {
                            onground = true;
                            ongroundthisframe = true;
                            setGy(0);
                            setY(tempgeo.getY() + tempgeo.getH());
                        }
                    }
                }
            } else {
                if (ongroundthisframe == false) {
                    onground = false;
                }
            }
        }
        if (onground == false) {
            setGy(getGy() + g);
            if (getGy() > getH() / 10) {
                setGy(getH() / 10);
            }
        }
    }

    public int checkSpecialCollisions(List<TrickObstacle> SpecialObstacles) {
        int specialcollision = 0;
        for (int k = 0; k < SpecialObstacles.size(); k++) {
            if (collidesWith(SpecialObstacles.get(k))) {
                specialcollision = SpecialObstacles.get(k).GetId();
                switch (specialcollision) {
                    case 40:
                        SpecialObstacles.get(k).removeFromLayer();
                        SpecialObstacles.get(k).setUsed(true);
                        SpecialObstacles.remove(k);
                        return specialcollision;
                    case 50:
                        SpecialObstacles.get(k).removeFromLayer();
                        SpecialObstacles.get(k).setUsed(true);
                        SpecialObstacles.remove(k);
                        return specialcollision;
                    case 51:
                        SpecialObstacles.get(k).removeFromLayer();
                        SpecialObstacles.get(k).setUsed(true);
                        SpecialObstacles.remove(k);
                        return specialcollision;
                    default:
                        break;
                }
            }
        }
        return specialcollision;
    }

    public boolean checkCrawlerCollisions(List<Crawler> Crawlers) {
        boolean gothit = false;
        for (int k = 0; k < Crawlers.size(); k++) {
            if (collidesWith(Crawlers.get(k))) {
                gothit = true;
                health -= 20;
                return gothit;
            }
        }
        return gothit;
    }

    public boolean checkExperimentCollisions(Experiment experiment) {
        boolean gothit = false;
        if (collidesWith(experiment)) {
            gothit = true;
            health -= 30;
            return gothit;
        }
        return gothit;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public void checkRemovability() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
