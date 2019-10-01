package Game;

import Settings.Resolution;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Shooter extends Sprites {

    Pane Gunlayer;
    Player player;
    double gamewidth = Resolution.gamewidth;
    double firecooldown = 0;

    public Shooter(Pane Gunlayer, Pane BulletLayer, Player player, ImageView bimage, double x, double y) {
        super(Gunlayer, bimage, x, y, 0, 0, 0);
        this.Gunlayer = Gunlayer;
        this.player = player;
    }

    public Projectile newprojectile(Pane BulletLayer) {
        firecooldown += 30;
        ImageView tempproj = new ImageView(new Image("Sprites/Player/projectile.png", gamewidth / 55, gamewidth / 55, false, false));
        tempproj.setFitHeight(gamewidth / 55);
        tempproj.setFitWidth(gamewidth / 55);
        return new Projectile(BulletLayer, tempproj, getX(), getY(), getR(), gamewidth / 76.8, player.movingleft);
    }

    public void setrotation(double mousex, double mousey, double scale) {
        double distancefromgunx = (scale*mousex - getX());
        double distancefromguny = -(scale*mousey - getY());
        double anglebeta = Math.abs(Math.atan(distancefromguny / distancefromgunx));
        double angletheta = 0;
        if (distancefromguny > 0 && distancefromgunx > 0) { //top right corner
            if (player.movingleft == false) {
                angletheta = anglebeta;
                if (angletheta > (3 * (Math.PI) / 16)) {
                    angletheta = (3 * Math.PI / 16);
                }
            } else {
                angletheta = -anglebeta;
                if (angletheta < -(3 * (Math.PI) / 16)) {
                    angletheta = -(3 * Math.PI / 16);
                }
            }
        } else if (distancefromguny < 0 && distancefromgunx > 0) { //bottom right coner
            if (player.movingleft == false) {
                angletheta = (2 * Math.PI - anglebeta);
                if (angletheta < (29 * (Math.PI) / 16)) {
                    angletheta = (29 * (Math.PI) / 16);
                }
            } else {
                angletheta = -(2 * Math.PI - anglebeta);
                if (angletheta > -(29 * (Math.PI) / 16)) {
                    angletheta = (3 * (Math.PI) / 16);
                }
            }
        } else if (distancefromguny > 0 && distancefromgunx < 0) { //top left corner
            if (player.movingleft) {
                angletheta = -anglebeta;
                if (angletheta < -(3 * (Math.PI) / 16)) {
                    angletheta = -(3 * Math.PI / 16);
                }
            } else {
                angletheta = anglebeta;
                if (angletheta > (3 * (Math.PI) / 16)) {
                    angletheta = (3 * Math.PI / 16);
                }
            }
        } else if (distancefromguny < 0 && distancefromgunx < 0) {//bottom left corner 
            if (player.movingleft) {
                angletheta = -(2 * Math.PI - anglebeta);
                if (angletheta > -(29 * (Math.PI) / 16)) {
                    angletheta = (3 * (Math.PI) / 16);
                }
            } else {
                angletheta = (29 * (Math.PI) / 16);
            }
        }
        setR(-(angletheta * 180 / Math.PI));
    }

    public void checkMotions() {
        if (player.movingleft) {
            setX(player.getX() - player.getW() / 6);
        } else if (player.movingleft == false) {
            setX(player.getX() + player.getW() / 1.8);
        }
        if (player.flipped) {
            setY(player.getY() + player.getH() / 3);
        } else if (player.flipped == false) {
            setY(player.getY() + player.getH() / 3); //X AND Y CO ORDINATES OF SHOOTER ARE DIRECTLY PROPORTIONAL TO PLAYER MODEL
        }

        ImageView flippedsprite = getView();
        if (player.flipped) {
            flippedsprite.setScaleY(-1);
        } else if (player.flipped == false) {
            flippedsprite.setScaleY(1);
        }
        if (player.movingleft) {
            flippedsprite.setScaleX(-1);
        } else if (player.movingleft == false) {
            flippedsprite.setScaleX(1);
        }
        setView(flippedsprite);
    }

    public double checkCoolDownRate() {
        return (firecooldown);
    }

    @Override
    public void checkRemovability() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
