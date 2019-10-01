package Levels;

import Settings.*;
import Game.Obstacle;
import Game.TrickObstacle;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Level6 {

    static ImageView Background;
    static Obstacle Floor;
    static Obstacle RightWall;
    static Obstacle LeftWall;
    static boolean seen = false;

    static List AllObjects = new ArrayList();
    static List<Obstacle> Obstacles = new ArrayList();
    static List<TrickObstacle> SpecialObstacles = new ArrayList();

    static double gamewidth = Resolution.gamewidth;
    static double gameheight = Resolution.gameheight;

    public Level6() {

    }

    public static List load(Pane LevelGeometry) {
        if (Resolution.dead6 == true) {
            seen = false;
            Resolution.dead6 = false;
        }
        if (seen == false) {
            Background = new ImageView(new Image("Sprites/LevelGeometry/BG4.png", gamewidth, gameheight, false, false));
            Background.setFitWidth(gamewidth);
            Background.setFitHeight(gameheight);
            Background.relocate(0, 0);
            LevelGeometry.getChildren().add(Background);
            //TEMPLATE FOR CREATING LEVEL GEOMETRY

            ImageView floor = new ImageView(new Image("Sprites/LevelGeometry/dirtside.png", gamewidth, gameheight / 12, false, false));
            floor.setFitWidth(gamewidth);
            floor.setFitHeight(gameheight / 12);
            Floor = new Obstacle(LevelGeometry, floor, 0, gameheight - gameheight / 12, 0);

            ImageView rightwall = new ImageView(new Image("Sprites/LevelGeometry/dirtside.png", gamewidth / 16, gameheight / 2, false, false));
            rightwall.setFitWidth(gamewidth / 16);
            rightwall.setFitHeight(gameheight / 2);
            RightWall = new Obstacle(LevelGeometry, rightwall, 15 * gamewidth / 16, 0, 0);

            ImageView leftwall = new ImageView(new Image("Sprites/LevelGeometry/dirtside.png", gamewidth / 16, 5 * gamewidth / 6, false, false));
            leftwall.setFitWidth(gamewidth / 16);
            leftwall.setFitHeight(gameheight / 2);
            LeftWall = new Obstacle(LevelGeometry, leftwall, 0, 0, 0);

            Obstacles.add(Floor);
            Obstacles.add(RightWall);
            Obstacles.add(LeftWall);

            seen = true;
        } else {
            LevelGeometry.getChildren().add(Background);
            Obstacles.add(Floor);
            Obstacles.add(LeftWall);
            Obstacles.add(RightWall);

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
        return AllObjects;
    }
}
