/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Levels;

import Game.Obstacle;
import Game.TrickObstacle;
import Settings.Resolution;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author bixxt
 */
public class Level5 {

    static ImageView Background;
    static Obstacle Ceiling;
    static Obstacle Floor;
    static Obstacle RightWall;
    static Obstacle LeftWall;
    static TrickObstacle StasisTube;
    static Obstacle Alarm;
    static Obstacle AlarmOn;
    static boolean seen = false;

    static List AllObjects = new ArrayList();
    static List<Obstacle> Obstacles = new ArrayList();

    static double gamewidth = Resolution.gamewidth;
    static double gameheight = Resolution.gameheight*5;

    public Level5() {

    }

    public static List load(Pane LevelGeometry) {
        if (Resolution.dead5 == true) {
            seen = false;
            Resolution.dead5 = false;
        }
        if (seen == false) {

            Background = new ImageView(new Image("Sprites/LevelGeometry/BG4.png", gamewidth, gameheight, false, false));
            Background.setFitWidth(gamewidth);
            Background.setFitHeight(gameheight);
            Background.relocate(0, 0);
            LevelGeometry.getChildren().add(Background);

            //TEMPLATE FOR CREATING LEVEL GEOMETRY
            ImageView rightwall = new ImageView(new Image("Sprites/LevelGeometry/dirtside.png", gamewidth / 16, gamewidth / 2, false, false));
            rightwall.setFitWidth(gamewidth / 16);
            rightwall.setFitHeight(gameheight);
            rightwall.setRotate(90);
            RightWall = new Obstacle(LevelGeometry, rightwall, 15 * gamewidth / 16, 0, 0);

            ImageView leftwall = new ImageView(new Image("Sprites/LevelGeometry/dirtside.png", gamewidth / 16, 5 * gamewidth / 6, false, false));
            leftwall.setFitWidth(gamewidth / 16);
            leftwall.setFitHeight(gameheight);
            LeftWall = new Obstacle(LevelGeometry, leftwall, 0, 0, 0);

            //Layer order (important for effects)
            Obstacles.add(RightWall);
            Obstacles.add(LeftWall);

            seen = true;
        } else {
            LevelGeometry.getChildren().add(Background);
            Obstacles.add(LeftWall);
            Obstacles.add(RightWall);

            for (int k = 0; k < Obstacles.size(); k++) {
                Obstacles.get(k).addToLayer();
            }
        }

        AllObjects.add(Obstacles);
        AllObjects.add(Background);
        return AllObjects;
    }
}
