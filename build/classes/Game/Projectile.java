package Game;

import Settings.Resolution;
import java.io.File;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Projectile extends Sprites {

    double gamewidth = Resolution.gamewidth;
    double dz;

    public Projectile(Pane bulletpane, ImageView projectile, double x, double y, double gunangle, double dzo, boolean isflipped) {
        super(bulletpane, projectile, x, y, 3, 0, 0);
        if (isflipped) {
            dz = -dzo;
        } else {
            dz = dzo;
        }
        gunangle = gunangle * Math.PI / 180;
        dx = dz * Math.cos(gunangle);
        dy = dz * Math.sin(gunangle);
    }

    public boolean checkCollisions(List<Obstacle> Obstacles) {
        boolean collided = false;
        for (int k = 0; k < Obstacles.size(); k++) {
            if (collidesWith(Obstacles.get(k))) {
                removeFromLayer();
                updateUI();
                collided = true;
                return collided;
            }
        }
        return collided;
    }

    public int checkSpecialCollisions(List<TrickObstacle> SpecialObstacles) {
        int interaction = 0;
        for (int k = 0; k < SpecialObstacles.size(); k++) {
            if (collidesWith(SpecialObstacles.get(k))) {
                if (SpecialObstacles.get(k).GetId() < 50) {
                    removeFromLayer();
                    updateUI();
                    if (SpecialObstacles.get(k).getUsed() == true) {
                        return 0;
                    } else {
                        interaction = SpecialObstacles.get(k).GetId();
                    }
                    if (interaction == 20) {
                        if (SpecialObstacles.get(k).getUsed() == false) {
                            SpecialObstacles.get(k).setUsed(true);
                            SpecialObstacles.get(k).removeFromLayer();
                            SpecialObstacles.get(k).setView(new ImageView(new Image("Sprites/LevelGeometry/switchused.png", SpecialObstacles.get(k).getW(), SpecialObstacles.get(k).getH(), false, false)));
                            SpecialObstacles.get(k).updateUI();
                            SpecialObstacles.get(k).addToLayer();
                            return interaction;
                        }
                    }
                }
            }
        }
        return interaction;
    }

    public boolean checkCrawlerCollisions(List<Crawler> Crawlers) {
        boolean iscollided = false;
        for (int k = 0; k < Crawlers.size(); k++) {
            if (collidesWith(Crawlers.get(k))) {
                removeFromLayer();
                updateUI();
                Crawlers.get(k).setHealth(Crawlers.get(k).getHealth() - 10);
                MediaPlayer HitPlayer = new MediaPlayer(hitsound);
                HitPlayer.play();
                iscollided = true;
                if (Crawlers.get(k).getHealth() <= 0) {
                    Crawlers.get(k).removeFromLayer();
                    Crawlers.get(k).updateUI();
                    Crawlers.remove(k);
                }
            }
        }
        return iscollided;
    }

    public boolean checkExperimentCollisions(Experiment experiment) {
        boolean iscollided = false;
        if (collidesWith(experiment)) {
            removeFromLayer();
            updateUI();
            iscollided = true;
            if (experiment.getVuln()) {
                experiment.setHealth(experiment.getHealth() - 10);
                MediaPlayer HitPlayer = new MediaPlayer(hitsound);
                HitPlayer.play();
            } else {
                MediaPlayer TinkPlayer = new MediaPlayer(tinksound);
                TinkPlayer.play();
            }
        }
        return iscollided;
    }

    public boolean checkPlayerCollisions(Player player) {
        boolean iscollided = false;
        if (collidesWith(player)) {
            player.setHealth(player.getHealth() - 10);
            removeFromLayer();
            updateUI();
            iscollided = true;
        }
        return iscollided;
    }

    @Override
    public void checkRemovability() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    Media tinksound = new Media(new File("src/Sounds/Tink.mp3").toURI().toString());
    Media hitsound = new Media(new File("src/Sounds/Hit.mp3").toURI().toString());
}
