package Game;

import javafx.scene.image.*;
import javafx.scene.layout.Pane;

public abstract class Sprites {

    //These vars are set when the sprite is created, and updated as time goes on.
    ImageView spriteimage;
    Pane layer;
    double x;
    double y;
    double r;
    double w;
    double h;
    double g;
    double dx;
    double dy;
    double dr;
    double gy;

    public Sprites(Pane layer, ImageView image, double x, double y, double dx, double dy, double g) {
        this.spriteimage = image;
        this.layer = layer;
        this.x = x;
        this.y = y;
        this.w = spriteimage.getFitWidth();
        this.h = spriteimage.getFitHeight();
        this.g = g;
        this.spriteimage.relocate(x, y);
        this.spriteimage.setRotate(r);
        addToLayer();
    }

    public void addToLayer() {
        layer.getChildren().add(this.spriteimage);
    }

    public void removeFromLayer() {
        layer.getChildren().remove(this.spriteimage);
    }

    public Pane getLayer() {
        return layer;
    }

    public void setLayer(Pane layer) {
        this.layer = layer;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getGy() {
        return gy;
    }

    public void setGy(double gy) {
        this.gy = gy;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getG() {
        return g;
    }

    public double getW() {
        return w;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void move() {
        x += dx;
        dy += gy;
        y += dy;
    }

    public void setView(ImageView newimage) {
        this.spriteimage = newimage;
    }

    public ImageView getView() {
        return spriteimage;
    }

    public void updateUI() {
        spriteimage.setFitWidth(w);
        spriteimage.setFitHeight(h);
        spriteimage.relocate(x, y);
        spriteimage.setRotate(r);
    }

    //THIS CURRENTLY IS ONLY FOR SQUARE OBJECTS. IT MAY BE NECESSARY TO MAKE THIS MORE COMPLEX IN THE FUTURE.
    public boolean collidesWith(Sprites otherSprite) {
        return (otherSprite.x + otherSprite.w >= x && otherSprite.y + otherSprite.h >= y && otherSprite.x <= x + w && otherSprite.y <= y + h);
    }

    public abstract void checkRemovability();

}
