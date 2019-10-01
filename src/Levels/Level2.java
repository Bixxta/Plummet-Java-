package Levels;

import Settings.*;
import Game.Obstacle;
import Game.TrickObstacle;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author bixxt
 */
public class Level2 {

    static ImageView Background;
    static Obstacle CeilingBar;
    static Obstacle FloorBar;
    static Obstacle MidBar;
    static Obstacle FloatBar;
    static TrickObstacle DoorSwitch;
    static TrickObstacle FirstGun;
    static Obstacle Door;
    static Obstacle BlockBar;
    static Obstacle LeftWall;

    static boolean seen = false;

    static List AllObjects = new ArrayList();
    static List<Obstacle> Obstacles = new ArrayList();
    static List<TrickObstacle> SpecialObstacles = new ArrayList();

    static double gamewidth = Resolution.gamewidth;
    static double gameheight = Resolution.gameheight;

    public Level2() {

    }

    public static List load(Pane LevelGeometry) {
        if (Resolution.dead2 == true) {
            seen = false;
            Resolution.dead2 = false;
        }
        if (seen == false) {
            Background = new ImageView(new Image("Sprites/LevelGeometry/BG2.png", gamewidth, gameheight, false, false));
            Background.setFitWidth(gamewidth);
            Background.setFitHeight(gameheight);
            Background.relocate(0, 0);
            LevelGeometry.getChildren().add(Background);

            ImageView ceilingtexture = new ImageView(new Image("Sprites/LevelGeometry/floor.png", gamewidth, gameheight / 12, false, false));
            ceilingtexture.setFitWidth(gamewidth);
            ceilingtexture.setFitHeight(gameheight / 12);
            CeilingBar = new Obstacle(LevelGeometry, ceilingtexture, 0, 0, 0);
            Obstacles.add(CeilingBar);

            ImageView door = new ImageView(new Image("Sprites/LevelGeometry/door.png", 100, 200, false, false));
            door.setFitWidth(gamewidth / 16);
            door.setFitHeight(gameheight / 3);
            Door = new Obstacle(LevelGeometry, door, 15 * gamewidth / 16, 11 * gameheight / 12 - gameheight / 3, 1);
            Obstacles.add(Door);

            ImageView floortexture = new ImageView(new Image("Sprites/LevelGeometry/floor.png", gamewidth, gameheight / 12, false, false));
            floortexture.setFitWidth(gamewidth);
            floortexture.setFitHeight(gameheight / 12);
            FloorBar = new Obstacle(LevelGeometry, floortexture, 0, 11 * gameheight / 12, 0);
            Obstacles.add(FloorBar);

            ImageView leftwall = new ImageView(new Image("Sprites/LevelGeometry/wall.png", gamewidth / 16, 5 * gamewidth / 6, false, false));
            leftwall.setFitWidth(gamewidth / 16);
            leftwall.setFitHeight(gameheight / 2);
            LeftWall = new Obstacle(LevelGeometry, leftwall, 0, gameheight / 12, 0);
            Obstacles.add(LeftWall);

            ImageView midblock = new ImageView(new Image("Sprites/LevelGeometry/platform.png", gamewidth / 4, 2 * gameheight / 12, false, false));
            midblock.setFitWidth(gamewidth / 4);
            midblock.setFitHeight(2 * gameheight / 12);
            MidBar = new Obstacle(LevelGeometry, midblock, gamewidth / 2, 9 * gameheight / 12, 0);
            Obstacles.add(MidBar);

            ImageView blocker = new ImageView(new Image("Sprites/LevelGeometry/wall.png", gamewidth / 16, gamewidth / 2, false, false));
            blocker.setRotate(90);
            blocker.setFitWidth(gamewidth / 16);
            blocker.setFitHeight(gameheight / 2);
            BlockBar = new Obstacle(LevelGeometry, blocker, 15 * gamewidth / 16, gameheight / 12, 0);
            Obstacles.add(BlockBar);

            ImageView floatblock = new ImageView(new Image("Sprites/LevelGeometry/platform.png", gamewidth / 4, gameheight / 8, false, false));
            floatblock.setFitWidth(gamewidth / 6);
            floatblock.setFitHeight(gameheight / 10);
            FloatBar = new Obstacle(LevelGeometry, floatblock, 2 * gamewidth / 8, 5.5 * gameheight / 10, 0);
            Obstacles.add(FloatBar);

            ImageView doorswitch = new ImageView(new Image("Sprites/LevelGeometry/switch.png", gamewidth / 32, gamewidth / 16, false, false));
            doorswitch.setFitWidth(gamewidth / 32);
            doorswitch.setFitHeight(gamewidth / 16);
            DoorSwitch = new TrickObstacle(LevelGeometry, doorswitch, 29 * gamewidth / 32, 3 * gameheight / 8, 20);
            SpecialObstacles.add(DoorSwitch);

            ImageView firstgun = new ImageView(new Image("Sprites/Player/shooter.png", gamewidth / 30 / .7, gamewidth / 30, false, false));
            firstgun.setFitHeight(gamewidth / 30);
            firstgun.setFitWidth(gamewidth / 30 / .7);
            FirstGun = new TrickObstacle(LevelGeometry, firstgun, 15 * gamewidth / 48, 4.5 * gameheight / 10, 40);
            SpecialObstacles.add(FirstGun);
            seen = true;
        } else {

            LevelGeometry.getChildren().add(Background);
            Obstacles.add(CeilingBar);
            Obstacles.add(Door);
            Obstacles.add(FloorBar);
            Obstacles.add(LeftWall);
            Obstacles.add(MidBar);
            BlockBar.getView().setOpacity(1);
            Obstacles.add(BlockBar);
            Obstacles.add(FloatBar);
            
            for (int k = 0; k < Obstacles.size(); k++) {
                Obstacles.get(k).addToLayer();
            }
            SpecialObstacles.add(DoorSwitch);
            if (FirstGun.getUsed() == false) {
                SpecialObstacles.add(FirstGun);
            }
            for (int k = 0; k < SpecialObstacles.size(); k++) {
                SpecialObstacles.get(k).addToLayer();
            }
        }
        AllObjects.add(Obstacles);
        AllObjects.add(SpecialObstacles);
        AllObjects.add(Background);
        return AllObjects;
    }
}
