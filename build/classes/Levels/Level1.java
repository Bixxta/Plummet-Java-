package Levels;

import Settings.*;
import Game.Obstacle;
import Game.TrickObstacle;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author bixxt
 */
public class Level1 {

    static ImageView Background;
    static Obstacle Ceiling;
    static Obstacle Floor;
    static Obstacle RightWall;
    static Obstacle LeftWall;
    static TrickObstacle StasisTube;
    static Obstacle Alarm;
    static Obstacle AlarmOn;
    static Rectangle Mask = new Rectangle();
    static boolean seen = false;

    static List AllObjects = new ArrayList();
    static List<Obstacle> Obstacles = new ArrayList();
    static List<TrickObstacle> SpecialObstacles = new ArrayList();

    static double gamewidth = Resolution.gamewidth;
    static double gameheight = Resolution.gameheight;

    public Level1() {

    }

    public static List load(Pane LevelGeometry) {
        if (Resolution.dead == true) {
            seen = false;
            Resolution.dead = false;
        }
        if (seen == false) {
            Background = new ImageView(new Image("Sprites/LevelGeometry/BG1.png", gamewidth, gameheight, false, false));
            Background.setFitWidth(gamewidth);
            Background.setFitHeight(gameheight);
            Background.relocate(0, 0);
            LevelGeometry.getChildren().add(Background);

            //TEMPLATE FOR CREATING LEVEL GEOMETRY
            ImageView ceiling = new ImageView(new Image("Sprites/LevelGeometry/floor.png", gamewidth, gameheight / 12, false, false));
            ceiling.setFitWidth(gamewidth);
            ceiling.setFitHeight(gameheight / 12);
            Ceiling = new Obstacle(LevelGeometry, ceiling, 0, 0, 0);

            ImageView floor = new ImageView(new Image("Sprites/LevelGeometry/floor.png", gamewidth, gameheight / 12, false, false));
            floor.setFitWidth(gamewidth);
            floor.setFitHeight(gameheight / 12);
            Floor = new Obstacle(LevelGeometry, floor, 0, gameheight - gameheight / 12, 0);

            ImageView rightwall = new ImageView(new Image("Sprites/LevelGeometry/wall.png", gamewidth / 16, gamewidth / 2, false, false));
            rightwall.setFitWidth(gamewidth / 16);
            rightwall.setFitHeight(gameheight / 2);
            rightwall.setRotate(90);
            RightWall = new Obstacle(LevelGeometry, rightwall, 15 * gamewidth / 16, gameheight / 12, 0);

            ImageView leftwall = new ImageView(new Image("Sprites/LevelGeometry/wall.png", gamewidth / 16, 5 * gamewidth / 6, false, false));
            leftwall.setFitWidth(gamewidth / 16);
            leftwall.setFitHeight(5 * gameheight / 6);
            LeftWall = new Obstacle(LevelGeometry, leftwall, 0, gameheight / 12, 0);

            ImageView stasisthing = new ImageView(new Image("Sprites/LevelGeometry/stasistubedark.png", gamewidth / 5, gameheight / 7.5, false, false));
            stasisthing.setFitWidth(gamewidth / 5);
            stasisthing.setFitHeight(gameheight / 7.5);
            StasisTube = new TrickObstacle(LevelGeometry, stasisthing, gamewidth / 16, gameheight - gameheight / 12 - gameheight / 7.5, 3);

            ImageView alarm = new ImageView(new Image("Sprites/LevelGeometry/alarmoff.png", gamewidth / 15, gamewidth / 25, false, false));
            alarm.setFitWidth(gamewidth / 15);
            alarm.setFitHeight(gamewidth / 25);
            Alarm = new Obstacle(LevelGeometry, alarm, gamewidth / 2, gameheight / 12, 0);

            Mask.setFill(Color.BLACK);
            Mask.setWidth(gamewidth);
            Mask.setHeight(gameheight);
            Mask.setOpacity(1);
            LevelGeometry.getChildren().add(Mask);

            ImageView alarmon = new ImageView(new Image("Sprites/LevelGeometry/alarmon.png", gamewidth / 15, gamewidth / 25, false, false));
            alarmon.setFitWidth(gamewidth / 15);
            alarmon.setFitHeight(gamewidth / 25);
            alarmon.setOpacity(0);
            AlarmOn = new Obstacle(LevelGeometry, alarmon, gamewidth / 2, gameheight / 12, 0);
            //Layer order (important for effects)

            Obstacles.add(Ceiling);
            Obstacles.add(Floor);
            Obstacles.add(RightWall);
            Obstacles.add(LeftWall);
            Obstacles.add(Alarm);
            Obstacles.add(AlarmOn);
            SpecialObstacles.add(StasisTube);
            seen = true;
        } else {
            LevelGeometry.getChildren().add(Background);
            Obstacles.add(Ceiling);
            Obstacles.add(Floor);
            Obstacles.add(LeftWall);
            Obstacles.add(RightWall);
            Obstacles.add(Alarm);
            Obstacles.add(AlarmOn);
            SpecialObstacles.add(StasisTube);

            for (int k = 0; k < Obstacles.size(); k++) {
                Obstacles.get(k).addToLayer();
            }
            for (int k = 0; k < SpecialObstacles.size(); k++) {
                SpecialObstacles.get(k).addToLayer();
            }
        }

        AllObjects.add(Obstacles);
        AllObjects.add(SpecialObstacles);
        AllObjects.add(Background);
        AllObjects.add(Mask);
        return AllObjects;
    }
}
