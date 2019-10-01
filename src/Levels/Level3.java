package Levels;

import Game.Crawler;
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

public class Level3 {

    static ImageView Background;
    static Obstacle Ceiling;
    static Obstacle Floor;
    static Obstacle RightWall;
    static Obstacle LeftWall;
    static Obstacle platform;
    static Obstacle platform2;
    static boolean seen = false;

    static List AllObjects = new ArrayList();
    static List<Obstacle> Obstacles = new ArrayList();
    static List<TrickObstacle> SpecialObstacles = new ArrayList();
    static List<Crawler> Crawlers = new ArrayList();

    static double gamewidth = Resolution.gamewidth;
    static double gameheight = Resolution.gameheight;

    public Level3() {

    }

    public static List load(Pane LevelGeometry) {
        if (Resolution.dead3 == true) {
            seen = false;
            Resolution.dead3 = false;
        }
        if (seen == false) {
            Background = new ImageView(new Image("Sprites/LevelGeometry/BG3.png", gamewidth, gameheight, false, false));
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

            ImageView rightwall = new ImageView(new Image("Sprites/LevelGeometry/wall.png", gamewidth / 16, gameheight / 2, false, false));
            rightwall.setFitWidth(gamewidth / 16);
            rightwall.setFitHeight(gameheight / 2);
            RightWall = new Obstacle(LevelGeometry, rightwall, 15 * gamewidth / 16, gameheight - gameheight / 12 - gameheight / 2, 0);

            ImageView leftwall = new ImageView(new Image("Sprites/LevelGeometry/wall.png", gamewidth / 16, 5 * gamewidth / 6, false, false));
            leftwall.setFitWidth(gamewidth / 16);
            leftwall.setFitHeight(gameheight / 2);
            LeftWall = new Obstacle(LevelGeometry, leftwall, 0, gameheight / 12, 0);

            ImageView midblock = new ImageView(new Image("Sprites/LevelGeometry/platform.png", gamewidth / 4, 2 * gameheight / 12, false, false));
            midblock.setFitWidth(gamewidth / 4);
            midblock.setFitHeight(2 * gameheight / 15);
            platform = new Obstacle(LevelGeometry, midblock, 1.3 * gamewidth / 4, 8.5 * gameheight / 12, 0);
            Obstacles.add(platform);

            ImageView plat = new ImageView(new Image("Sprites/LevelGeometry/platform.png", gamewidth / 4, 2 * gameheight / 12, false, false));
            plat.setFitWidth(gamewidth / 5);
            plat.setFitHeight(2 * gameheight / 15);
            platform2 = new Obstacle(LevelGeometry, plat, gamewidth * 2 / 3, 8.1 * gameheight / 16, 0);

            ImageView crawlingman = new ImageView(new Image("Sprites/Enemy/Spider/spider1.png", gamewidth / 13, gamewidth / 15, false, false));
            crawlingman.setFitWidth(gamewidth / 13);
            crawlingman.setFitHeight(gamewidth / 15);
            Crawler crawlboy1 = new Crawler(LevelGeometry, crawlingman, gamewidth * 2 / 3 + crawlingman.getFitWidth(), 4 * gamewidth / 16, 1);

            ImageView crawlingman2 = new ImageView(new Image("Sprites/Enemy/Spider/spider1.png", gamewidth / 13, gamewidth / 15, false, false));
            crawlingman2.setFitWidth(gamewidth / 13);
            crawlingman2.setFitHeight(gamewidth / 15);
            Crawler crawlboy2 = new Crawler(LevelGeometry, crawlingman2, 1.3 * gamewidth / 4 + crawlingman.getFitWidth(), 8.5 * gameheight / 12 - gamewidth / 15, 1);

            Obstacles.add(platform2);
            Obstacles.add(Ceiling);
            Obstacles.add(Floor);
            Obstacles.add(RightWall);
            Obstacles.add(LeftWall);
            Crawlers.add(crawlboy1);
            Crawlers.add(crawlboy2);

            seen = true;
        } else {
            LevelGeometry.getChildren().add(Background);
            Obstacles.add(Ceiling);
            Obstacles.add(Floor);
            Obstacles.add(LeftWall);
            Obstacles.add(RightWall);
            Obstacles.add(platform2);
            Obstacles.add(platform);
            //SpecialObstacles.add(StasisTube);
            for (int k = 0; k < Obstacles.size(); k++) {
                Obstacles.get(k).addToLayer();
            }
            for (int k = 0; k < SpecialObstacles.size(); k++) {
                SpecialObstacles.get(k).addToLayer();
            }
            ImageView crawlingman = new ImageView(new Image("Sprites/Enemy/Spider/spider1.png", gamewidth / 13, gamewidth / 15, false, false));
            crawlingman.setFitWidth(gamewidth / 13);
            crawlingman.setFitHeight(gamewidth / 15);
            Crawler crawlboy1 = new Crawler(LevelGeometry, crawlingman, gamewidth * 2 / 3 + crawlingman.getFitWidth(), 4 * gamewidth / 16, gamewidth / 1080);
            Crawlers.add(crawlboy1);

            ImageView crawlingman2 = new ImageView(new Image("Sprites/Enemy/Spider/spider1.png", gamewidth / 13, gamewidth / 15, false, false));
            crawlingman2.setFitWidth(gamewidth / 13);
            crawlingman2.setFitHeight(gamewidth / 15);
            Crawler crawlboy2 = new Crawler(LevelGeometry, crawlingman2, 1.3 * gamewidth / 4 + crawlingman.getFitWidth(), 8.5 * gameheight / 12 - gamewidth / 15, 1);
            Crawlers.add(crawlboy2);
        }

        AllObjects.add(Obstacles);
        AllObjects.add(SpecialObstacles);
        AllObjects.add(Crawlers);
        AllObjects.add(Background);
        return AllObjects;
    }
}
