package Levels;

import Game.Experiment;
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
 * @author Scott
 */
public class Level4 {

    static ImageView Background;
    static Obstacle Ceiling;
    static Obstacle Floor;
    static Obstacle RightWall;
    static Obstacle LeftWall;
    static Obstacle DeckFloor;
    static Obstacle DeckWall;
    static TrickObstacle CrashTrigger;
    static TrickObstacle BossTrigger;

    static boolean seen = false;
    static boolean bosskilled = false;

    static List AllObjects = new ArrayList();
    static List<Obstacle> Obstacles = new ArrayList();
    static List<TrickObstacle> SpecialObstacles = new ArrayList();
    static List<Experiment> Boss = new ArrayList();

    static double gamewidth = Resolution.gamewidth * 3 / 2;
    static double gameheight = Resolution.gameheight * 3 / 2;

    public Level4() {

    }

    public static List load(Pane LevelGeometry) {
        if (Resolution.dead4 == true) {
            seen = false;
            Obstacles.clear();
            SpecialObstacles.clear();
            Boss.clear();
            AllObjects.clear();
            Resolution.dead4 = false;
        }
        Boss.clear();
        if (seen == false) {
            System.out.println("reloadd");
            //TEMPLATE FOR CREATING LEVEL GEOMETRY
            Background = new ImageView(new Image("Sprites/LevelGeometry/BG3.png", gamewidth, gameheight, false, false));
            Background.setFitWidth(gamewidth);
            Background.setFitHeight(gameheight);
            Background.relocate(0, 0);
            LevelGeometry.getChildren().add(Background);

            ImageView ceiling = new ImageView(new Image("Sprites/LevelGeometry/floor.png", gamewidth, gameheight / 24, false, false));
            ceiling.setFitWidth(gamewidth);
            ceiling.setFitHeight(gameheight / 24);
            Ceiling = new Obstacle(LevelGeometry, ceiling, 0, 0, 0);

            ImageView floor = new ImageView(new Image("Sprites/LevelGeometry/floor.png", gamewidth, gameheight / 24, false, false));
            floor.setFitWidth(gamewidth);
            floor.setFitHeight(gameheight / 24);
            Floor = new Obstacle(LevelGeometry, floor, 0, gameheight - gameheight / 24, 0);

            ImageView rightwall = new ImageView(new Image("Sprites/LevelGeometry/wall.png", gamewidth / 32, gameheight, false, false));
            rightwall.setFitWidth(gamewidth / 32);
            rightwall.setFitHeight(gameheight);
            RightWall = new Obstacle(LevelGeometry, rightwall, gamewidth - gamewidth / 32, gameheight / 24, 0);

            ImageView leftwall = new ImageView(new Image("Sprites/LevelGeometry/wall.png", gamewidth / 16, 9 * gameheight / 10, false, false));
            leftwall.setFitWidth(gamewidth / 16);
            leftwall.setFitHeight(9 * gameheight / 10);
            LeftWall = new Obstacle(LevelGeometry, leftwall, 0, gameheight / 3.7, 0);

            ImageView deckfloor = new ImageView(new Image("Sprites/LevelGeometry/glassfloor.png", gamewidth / 6, gameheight / 48, false, false));
            deckfloor.setFitWidth(gamewidth / 6);
            deckfloor.setFitHeight(gameheight / 48);
            DeckFloor = new Obstacle(LevelGeometry, deckfloor, gamewidth / 16, gameheight / 3.7 + gameheight / 48, 2);

            ImageView deckobservation = new ImageView(new Image("Sprites/LevelGeometry/platformglass.png", gamewidth / 70, gameheight / 3.7, false, false));
            deckobservation.setFitWidth(gamewidth / 70);
            deckobservation.setFitHeight(gameheight / 3.7);
            DeckWall = new Obstacle(LevelGeometry, deckobservation, gamewidth / 16 + gamewidth / 6, gameheight / 24, 0);

            ImageView crashtrigger = new ImageView(new Image("Sprites/LevelGeometry/platform.png", gamewidth / 10, gameheight / 24, false, false));
            crashtrigger.setFitWidth(gamewidth / 30);
            crashtrigger.setFitHeight(gameheight / 3.7);
            crashtrigger.setOpacity(0);
            CrashTrigger = new TrickObstacle(LevelGeometry, crashtrigger, gamewidth / 7, gameheight / 24, 50);
            CrashTrigger.setUsed(false);

            ImageView bosstrigger = new ImageView(new Image("Sprites/LevelGeometry/platform.png", 1, 1, false, false));
            bosstrigger.setFitWidth(gamewidth / 30);
            bosstrigger.setFitHeight(gameheight);
            bosstrigger.setOpacity(0);
            BossTrigger = new TrickObstacle(LevelGeometry, bosstrigger, 3 * gamewidth / 8, 0, 51);
            BossTrigger.setUsed(false);

            ImageView experiment = new ImageView(new Image("Sprites/Enemy/Boss1/bossleep.png", gamewidth / 3, gamewidth / 10, false, false));
            experiment.setFitWidth(gamewidth / 3);
            experiment.setFitHeight(gamewidth / 10);
            Experiment BossMonster = new Experiment(LevelGeometry, experiment, gamewidth - gamewidth / 3 - gamewidth / 32, gameheight - gameheight / 24 - gamewidth / 10, gamewidth / 200);

            Obstacles.add(Ceiling);
            Obstacles.add(Floor);
            Obstacles.add(RightWall);
            Obstacles.add(LeftWall);
            Obstacles.add(DeckFloor);
            Obstacles.add(DeckWall);
            SpecialObstacles.add(CrashTrigger);
            SpecialObstacles.add(BossTrigger);
            Boss.add(BossMonster);

            seen = true;
        } else {
            LevelGeometry.getChildren().add(Background);
            Obstacles.add(Ceiling);
            Obstacles.add(Floor);
            Obstacles.add(RightWall);
            Obstacles.add(LeftWall);
            Obstacles.add(DeckFloor);
            Obstacles.add(DeckWall);
            if (CrashTrigger.getUsed() == false) {
                SpecialObstacles.add(CrashTrigger);
            }
            if (BossTrigger.getUsed() == false) {
                SpecialObstacles.add(BossTrigger);
            }
            for (int k = 0; k < Obstacles.size(); k++) {
                Obstacles.get(k).addToLayer();
            }
            for (int k = 0; k < SpecialObstacles.size(); k++) {
                SpecialObstacles.get(k).addToLayer();
            }
            if (bosskilled == false) {
                ImageView experiment = new ImageView(new Image("Sprites/Enemy/Boss1/bossleep.png", gamewidth / 3, gamewidth / 10, false, false));
                experiment.setFitWidth(gamewidth / 3);
                experiment.setFitHeight(gamewidth / 10);
                Experiment BossMonster = new Experiment(LevelGeometry, experiment, gamewidth - gamewidth / 3 - gamewidth / 32, gameheight - gameheight / 24 - gamewidth / 10, gamewidth / 200);
                Boss.add(BossMonster);
            }
        }

        AllObjects.add(Obstacles);
        AllObjects.add(SpecialObstacles);
        AllObjects.add(Boss);
        return AllObjects;
    }
}
