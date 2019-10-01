package Game;

import Settings.*;
import Levels.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Game {

    //Scenes (main menu, opening, settings screen, pause menu)
    TitleScreen titlescreen;
    Scene GameScreen;
    Input pausecheck;
    //Groups
    Group LayerGroup;
    //Panes that will appear
    Pane LevelGeometry, BulletLayer, GunLayer, PlayerLayer;
    AnchorPane UILayer;
    AnimationTimer MoveDoor;
    //Objects that are only created once
    Player player;
    Shooter shooter;
    Stage mainGame;
    List AllObstacles = new ArrayList();
    List<Obstacle> Obstacles = new ArrayList();
    List<TrickObstacle> SpecialObstacles = new ArrayList();
    List<Crawler> Crawlers = new ArrayList();
    List<Projectile> projectiles = new ArrayList();
    List<Projectile> enemyprojectiles = new ArrayList();
    int mapx;
    int mapy;
    Rectangle endback;
    ImageView credits = new ImageView();
    Button coolbutton;
    double gamewidth = Resolution.gamewidth;
    double gameheight = Resolution.gameheight;
    boolean bossactive = false;
    boolean shootagain = false;
    boolean doormoving = false;
    boolean hasgun = false;
    boolean cutscenelogic = false; //For use with any cutscene
    boolean nohitcontrol = false;
    boolean boss1killed = false; //Checks if the first boss is dead
    boolean ishit = false;
    MediaPlayer song = new MediaPlayer(new Media(new File("src/Sounds/Premonition.mp3").toURI().toString()));
    Experiment experiment;

    public Game() {

    }

    public void Game(Stage mainGame) {
        titlescreen = new TitleScreen();
        this.mainGame = mainGame;
        //create layers and scene
        UILayer = new AnchorPane();
        PlayerLayer = new Pane();
        LevelGeometry = new Pane();
        BulletLayer = new Pane();
        GunLayer = new Pane();
        LayerGroup = new Group();
        //LAYERS SHOULD BE ADDED TO THE GROUP IN ORDER OF HOW THEY'RE LAYED, BACK TO FRONT
        LayerGroup.getChildren().addAll(LevelGeometry, BulletLayer, GunLayer, PlayerLayer, UILayer);

        GameScreen = new Scene(LayerGroup, gamewidth, gameheight);
        mainGame.setFullScreen(Resolution.isFullcreen);
        //creates player and ui; if we do a starting cutscene, these can be activated later.
        pausecheck = new Input(GameScreen);
        pausecheck.addListeners();
        openingCutscene.start();
        //sets starting level
        mapx = 1;
        mapy = 10;
        //Load the first level seperate from the call
        AllObstacles = Level1.load(LevelGeometry);
        Obstacles = (List<Obstacle>) AllObstacles.get(0);
        SpecialObstacles = (List<TrickObstacle>) AllObstacles.get(1);
        //show the game
        mainGame.setScene(GameScreen);
        mainGame.show();
        playMusic("src/Sounds/Premonition.mp3");
        //Sets music
    }

    private void createPlayer() {
        // register input listeners
        Input input = new Input(GameScreen);
        input.addListeners();
        ImageView imagefinal = new ImageView(new Image("Sprites/Player/plumeteernogun.png", gamewidth / 16, gameheight / 4, false, false));
        //scaled according to settings
        double playerwidth = gamewidth / 16;
        double playerheight = gameheight / 4;
        imagefinal.setFitWidth(playerwidth);
        imagefinal.setFitHeight(playerheight);
        player = new Player(PlayerLayer, imagefinal, gamewidth / 6, gameheight - gameheight / 3, playerwidth / 200, playerwidth / 12, input);
        playerLoop.start();
        cameraLoop.start();
        deathCheck.start();
        CheckLevel.start();
        createUILayer();
        checkPause.start();
    }

    private void createGun(Player player) {
        ImageView shootergun = new ImageView(new Image("Sprites/Player/shooter.png", gamewidth / 24, gameheight / 22, false, false));
        shootergun.setFitWidth(gamewidth / 24);
        shootergun.setFitHeight(gameheight / 22);
        shooter = new Shooter(GunLayer, BulletLayer, player, shootergun, 0, 0);
        addAmmo();
        hasgun = true;
        UpdateUI.start();
        gunmovement.start();
        projectilegoing.start();
    }

    private void createUILayer() {
        //create bar
        Text HUDHEALTH = new Text();
        HUDHEALTH.setFont(Font.font("Candara", FontWeight.BOLD, gamewidth / 20));
        HUDHEALTH.setFill(Color.WHITE);
        HUDHEALTH.setStroke(Color.BLACK);
        HUDHEALTH.relocate(gamewidth / 45, gameheight / 160);
        HUDHEALTH.setText("+");
        HUDHEALTH.setBoundsType(TextBoundsType.VISUAL);

        Text HealthCounter = new Text();
        HealthCounter.setFont(Font.font("Candara", FontWeight.BOLD, gamewidth / 20));
        HealthCounter.setFill(Color.WHITE);
        HealthCounter.setStroke(Color.BLACK);
        HealthCounter.setText("100");
        HealthCounter.setBoundsType(TextBoundsType.VISUAL);
        HealthCounter.relocate(gamewidth / 20, gameheight / 50);
        UILayer.getChildren().addAll(HUDHEALTH, HealthCounter);
    }

    private void addAmmo() {
        //create ammo
        ImageView GunExample = new ImageView(new Image("Sprites/Player/shooter.png", gamewidth / 18.29, gameheight / 15.43, false, false));
        GunExample.setFitWidth(gamewidth / 18.3);
        GunExample.setFitHeight(gameheight / 15.4);
        GunExample.relocate(gamewidth / 192, gameheight / 1.1);

        ImageView AmmoOutline = new ImageView("Sprites/Player/battery.png");
        AmmoOutline.setFitWidth(gamewidth / 10.66);
        AmmoOutline.setFitHeight(gameheight / 14);
        AmmoOutline.relocate(gamewidth / 192 + gamewidth / 18, gameheight / 1.105);

        Rectangle AmmoBar1 = new Rectangle();
        AmmoBar1.setFill(Color.GREENYELLOW);
        AmmoBar1.setWidth(gamewidth / 48);
        AmmoBar1.setHeight(gameheight / 15.45);
        AmmoBar1.relocate(gamewidth / 192 + gamewidth / 17.2, gameheight / 1.1);

        Rectangle AmmoBar2 = new Rectangle();
        AmmoBar2.setFill(Color.YELLOW);
        AmmoBar2.setWidth(gamewidth / 48);
        AmmoBar2.setHeight(gameheight / 15.45);
        AmmoBar2.relocate(gamewidth / 192 + gamewidth / 17.2 + gamewidth / 46, gameheight / 1.1);

        Rectangle AmmoBar3 = new Rectangle();
        AmmoBar3.setFill(Color.ORANGE);
        AmmoBar3.setWidth(gamewidth / 48);
        AmmoBar3.setHeight(gameheight / 15.45);
        AmmoBar3.relocate(gamewidth / 192 + gamewidth / 17.2 + gamewidth / 23, gameheight / 1.1);

        Rectangle AmmoBar4 = new Rectangle();
        AmmoBar4.setFill(Color.RED);
        AmmoBar4.setWidth(gamewidth / 48);
        AmmoBar4.setHeight(gameheight / 15.45);
        AmmoBar4.relocate(gamewidth / 192 + gamewidth / 17.2 + gamewidth / 23 + gamewidth / 46, gameheight / 1.1);

        UILayer.getChildren().addAll(AmmoOutline, AmmoBar1, AmmoBar2, AmmoBar3, AmmoBar4, GunExample);
        UpdateUI.start();
    }

    private void changeLevel() {
        Obstacles.clear();
        SpecialObstacles.clear();
        LevelGeometry.getChildren().clear();
        Crawlers.clear();
        for (int k = 0; k < projectiles.size(); k++) {
            projectiles.get(k).removeFromLayer();
            projectiles.get(k).updateUI();
        }
        projectiles.clear();
        if (mapx == 1 && mapy == 10) {
            AllObstacles = Level1.load(LevelGeometry);
            Obstacles = (List<Obstacle>) AllObstacles.get(0);
            SpecialObstacles = (List<TrickObstacle>) AllObstacles.get(1);
            alarmpulse.start();
        } else if (mapx == 2 && mapy == 10) {
            alarmpulse.stop();
            AllObstacles = Level2.load(LevelGeometry);
            Obstacles = (List<Obstacle>) AllObstacles.get(0);
            SpecialObstacles = (List<TrickObstacle>) AllObstacles.get(1);
            crawlerLogic.stop();
        } else if (mapx == 3 && mapy == 10) {
            AllObstacles = Level3.load(LevelGeometry);
            Obstacles = (List<Obstacle>) AllObstacles.get(0);
            SpecialObstacles = (List<TrickObstacle>) AllObstacles.get(1);
            Crawlers = (List<Crawler>) AllObstacles.get(2);
            crawlerLogic.start();
            gamewidth = Resolution.gamewidth;
            gameheight = Resolution.gameheight;
        } else if (mapx == 4 && mapy == 10) {
            AllObstacles = Level4.load(LevelGeometry);
            gamewidth = 3 * Resolution.gamewidth / 2;
            gameheight = 3 * Resolution.gameheight / 2;
            Obstacles = (List<Obstacle>) AllObstacles.get(0);
            SpecialObstacles = (List<TrickObstacle>) AllObstacles.get(1);
            crawlerLogic.stop();
            if (boss1killed == false) {
                List<Experiment> templist = (List<Experiment>) AllObstacles.get(2);
                experiment = (Experiment) templist.get(0);
            }
        } else if (mapx == 4 && mapy == 11) {
            AllObstacles = Level5.load(LevelGeometry);
            gamewidth = Resolution.gamewidth;
            gameheight = 5 * Resolution.gameheight;
            Obstacles = (List<Obstacle>) AllObstacles.get(0);
        } else if (mapx == 4 && mapy == 12) {
            AllObstacles = Level6.load(LevelGeometry);
            Obstacles = (List<Obstacle>) AllObstacles.get(0);
            gamewidth = Resolution.gamewidth;
            gameheight = Resolution.gameheight;
        }
    }

    public void moveDoor(Obstacle tempobstacle) {
        tempobstacle.setDy(-gameheight / 200);
        double tempy = tempobstacle.getY();
        DoorOpenPlayer.play();
        MoveDoor = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (tempobstacle.getY() > tempy - tempobstacle.getH() + gameheight / 100) {
                    tempobstacle.move();
                    tempobstacle.updateUI();
                    doormoving = true;
                } else {
                    doormoving = false;
                    this.stop();
                }
            }
        };
        MoveDoor.start();
    }

    public void bossCutscene(double currentlocation) {
        //Stops everything to show the boss
        nohitcontrol = true;
        player.setDx(0);
        AnimationTimer BossIntro = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (cutscenelogic == false) {
                    if (LayerGroup.getTranslateX() >= currentlocation - gamewidth / 4 + gamewidth / 8) {
                        LayerGroup.setTranslateX(LayerGroup.getTranslateX() - gamewidth / 960);
                        UILayer.setVisible(false);
                    } else {
                        song.stop();
                        ScreamPlayer.play();
                        Scream.start();
                    }
                } else {
                    if (LayerGroup.getTranslateX() < -player.getX() + gamewidth / 4) {
                        LayerGroup.setTranslateX(LayerGroup.getTranslateX() + gamewidth / 64);
                    } else {
                        cutscenelogic = false;
                        checkPause.start();
                        cameraLoop.start();
                        projectilegoing.start();
                        gunmovement.start();
                        playerLoop.start();
                        UILayer.setVisible(true);
                        this.stop();
                    }
                }
            }
        };
        BossIntro.start();
    }

    public void playMusic(String musicFile) {
        song.stop();
        Media SongChoice = new Media(new File(musicFile).toURI().toString());
        song = new MediaPlayer(SongChoice);
        song.setCycleCount(MediaPlayer.INDEFINITE);
        song.play();
    }
    ////////////////
    //TIMERS BEGIN//
    ////////////////

    ////////////////////////////////
    ///////////ITEM CODES///////////
    ////////////////////////////////
    //WALL 0 - DOOR 1             //
    //DOOR SWITCH 20              //
    //CRAWLER 30 - BOSS 1 35      //
    //FIRST GUN 40                //
    //FIRST CRASH T 50 - BOSS T 51//
    ////////////////////////////////
    AnimationTimer cameraLoop = new AnimationTimer() {
        @Override
        public void handle(long now) {
            double camX = player.getX();
            double camY = player.getY();
            double screenX = LayerGroup.getScaleX();
            double screenY = LayerGroup.getScaleY();
            if (0 <= screenX && screenX < gamewidth - Resolution.gamewidth) {
                if (camX > Resolution.gamewidth / 4 && camX < gamewidth - 6 * Resolution.gamewidth / 8) {
                    LayerGroup.setTranslateX(-camX + Resolution.gamewidth / 4);
                    for (int k = 0; k < UILayer.getChildren().size(); k++) {
                        UILayer.getChildren().get(k).setTranslateX(camX - Resolution.gamewidth / 4);
                    }
                }
            } else {
                LayerGroup.setTranslateX(0);
            }
            if (0 <= screenY && screenY < gameheight - Resolution.gameheight) {
                if (camY > Resolution.gameheight / 4 && camY < gameheight - 6 * Resolution.gameheight / 8) {
                    LayerGroup.setTranslateY(-camY + Resolution.gameheight / 4);
                    for (int k = 0; k < UILayer.getChildren().size(); k++) {
                        UILayer.getChildren().get(k).setTranslateY(camY - Resolution.gameheight / 4);
                    }
                }

            } else {
                LayerGroup.setTranslateY(0);
            }
        }
    };

    AnimationTimer playerLoop = new AnimationTimer() {
        @Override
        public void handle(long now) {
            //Moving the player
            if (nohitcontrol == false) {
                player.processInput(); //check inputs
            }
            player.checkMotions();//this class will check what states the player is in and adjust sprites accordingly, and then move the player
            player.checkCollisions(Obstacles);//checks collisions with obstacles, and stops player from phasing into them
            int specialcollisionsindex = player.checkSpecialCollisions(SpecialObstacles);
            switch (specialcollisionsindex) {
                case 40:
                    createGun(player);
                    break;
                case 50:
                    Obstacles.get(4).removeFromLayer();
                    Obstacles.get(4).updateUI();
                    GlassShatterPlayer.play();
                    Obstacles.remove(4);
                    break;
                case 51:
                    checkPause.stop();
                    projectilegoing.stop();
                    gunmovement.stop();
                    bossCutscene(LayerGroup.getTranslateX());
                    playerLoop.stop();
                    cameraLoop.stop();
                default:
                    break;
            }
            if (ishit == false) {
                if (player.checkCrawlerCollisions(Crawlers)) {
                    player.setDy(Resolution.gamewidth / -48);
                    player.setGy(0);
                    if (player.movingleft) {
                        player.setDx(Resolution.gamewidth / 192);
                    } else {
                        player.setDx(Resolution.gamewidth / -192);
                    }
                    nohitcontrol = true;
                    ishit = true;
                    player.onground = false;
                    player.setG(Resolution.gamewidth / 9600);
                    playerHit.start();
                }
                if (mapx == 4 && mapy == 10) {
                    if (boss1killed == false) {
                        if (player.checkExperimentCollisions(experiment)) {
                            player.setDy(Resolution.gamewidth / -48);
                            player.setGy(0);
                            player.setG(Resolution.gamewidth / 9600);
                            player.setDx(Resolution.gamewidth / -192);
                            nohitcontrol = true;
                            ishit = true;
                            player.onground = false;
                            playerHit.start();
                        }
                    }
                }
                if (mapx == 4 && mapy == 12 && player.onground) {
                    cameraLoop.stop();
                    playerLoop.stop();
                    playerHit.stop();
                    deathCheck.stop();
                    projectilegoing.stop();
                    gunmovement.stop();
                    checkPause.stop();
                    CheckLevel.stop();
                    UpdateUI.stop();
                    UILayer.getChildren().clear();
                    UILayer.setOpacity(1);
                    UILayer.setVisible(true);
                    endback = new Rectangle();
                    endback.setHeight(gameheight);
                    endback.setWidth(gamewidth);
                    endback.setFill(Color.BLACK);
                    credits = new ImageView(new Image("Sprites/Menu/credits.png", Resolution.gamewidth, Resolution.gameheight, false, false));
                    credits.setFitHeight(gameheight / 1.5);
                    credits.setFitWidth(gamewidth / 1.5);
                    credits.relocate(gamewidth / 2 - gamewidth / 3, gamewidth / 24);
                    coolbutton = new Button();
                    Resolution.dead = true;
                    Resolution.dead2 = true;
                    Resolution.dead3 = true;
                    Resolution.dead4 = true;
                    Resolution.dead5 = true;
                    Resolution.dead6 = true;
                    coolbutton.resize(Resolution.gamewidth / 4, Resolution.gamewidth / 20);
                    coolbutton.setPrefSize(Resolution.gamewidth / 4, Resolution.gamewidth / 20);
                    coolbutton.relocate(gamewidth / 2 - gamewidth / 8, gameheight - gamewidth / 10);
                    coolbutton.setText("Return to main menu");
                    coolbutton.addEventHandler(ActionEvent.ACTION, (e) -> {
                        nohitcontrol = true;
                        LayerGroup.getChildren().clear();
                        UILayer.getChildren().clear();
                        cameraLoop.stop();
                        playerLoop.stop();
                        playerHit.stop();
                        deathCheck.stop();
                        projectilegoing.stop();
                        checkPause.stop();
                        BossLogic.stop();
                        crawlerLogic.stop();
                        PlayerShaking.stop();
                        CheckLevel.stop();
                        UpdateUI.stop();
                        Scream.stop();
                        song.stop();
                        this.stop();

                        titlescreen.start(mainGame);
                    });
                    UILayer.getChildren().addAll(endback, credits, coolbutton);
                    this.stop();
                }
            }
            player.updateUI(); //update screen
        }
    };

    AnimationTimer playerHit = new AnimationTimer() {
        int invisframes = 0;
        int iframes = 60;

        @Override
        public void handle(long now) {
            iframes--;
            if (iframes == 0) {
                iframes = 60;
                player.setDy(0);
                player.getView().setVisible(true);
                player.setG(Resolution.gamewidth / 3200);
                ishit = false;
                playerHit.stop();
            }
            invisframes++;
            if (invisframes == 5) {
                player.getView().setVisible(false);
            } else if (invisframes == 8) {
                player.getView().setVisible(true);
                invisframes = 0;
            }
            if (player.onground) {
                nohitcontrol = false;
                player.setDy(0);
                player.setG(Resolution.gamewidth / 3200);
            } else {
                if (nohitcontrol) {
                    player.setDy(0.8 * player.getDy());
                }
            }
        }
    };

    AnimationTimer playerDeath = new AnimationTimer() {
        double deathframes = 300;
        Rectangle whiteframe = new Rectangle();
        boolean fadeout = false;
        double temp;

        @Override
        public void handle(long now) {
            if (fadeout == false) {
                deathframes--;
                if (deathframes == 0 && player.onground) {
                    whiteframe.setHeight(gameheight);
                    whiteframe.setWidth(gamewidth);
                    whiteframe.setFill(Color.WHITE);
                    whiteframe.setOpacity(0);
                    UILayer.getChildren().add(whiteframe);
                    fadeout = true;

                }
                if (player.getR() <= -90) {
                } else {
                    player.setR(player.getR() - 5);
                }
                if (player.onground) {

                    player.setDy(0);
                    player.setDx(0);
                    player.setG(Resolution.gamewidth / 3200);

                } else {

                }
            } else {
                if (deathframes >= 200) {

                    LayerGroup.getChildren().clear();
                    UILayer.getChildren().clear();
                    cameraLoop.stop();
                    playerLoop.stop();
                    playerHit.stop();
                    deathCheck.stop();
                    projectilegoing.stop();
                    checkPause.stop();
                    BossLogic.stop();
                    crawlerLogic.stop();
                    PlayerShaking.stop();
                    CheckLevel.stop();
                    UpdateUI.stop();
                    Scream.stop();
                    song.stop();
                    this.stop();

                    titlescreen.start(mainGame);
                } else {
                    deathframes++;
                    whiteframe.setOpacity(deathframes / 100);
                }
            }
        }
    };

    //MOVING PROJECTILES (CALLED FROM GUN)
    AnimationTimer projectilegoing = new AnimationTimer() {
        @Override
        public void handle(long now) {
            for (int k = 0; k < projectiles.size(); k++) {
                if (projectiles.get(k).checkCollisions(Obstacles)) {
                    projectiles.remove(k);
                } else {
                    if (projectiles.get(k).checkSpecialCollisions(SpecialObstacles) == 20) {
                        for (int c = 0; c < Obstacles.size(); c++) {
                            if (Obstacles.get(c).obstacleid == 1) {  //INTERACTION CODE: 1 IS OPEN DOOR (add here)
                                moveDoor(Obstacles.get(c));
                            }
                        }
                        projectiles.remove(k);
                    } else {
                        if (projectiles.get(k).checkCrawlerCollisions(Crawlers)) {
                            projectiles.remove(k);
                        } else {
                            if (boss1killed == false && mapx == 4 && mapy == 10) {
                                if (projectiles.get(k).checkExperimentCollisions(experiment)) {
                                    projectiles.remove(k);
                                } else {
                                    projectiles.get(k).updateUI();
                                    projectiles.get(k).move();
                                }
                            } else {
                                projectiles.get(k).updateUI();
                                projectiles.get(k).move();
                            }
                        }
                    }
                }
            }
            for (int k = 0; k < enemyprojectiles.size(); k++) {
                enemyprojectiles.get(k).updateUI();
                enemyprojectiles.get(k).move();
                if (enemyprojectiles.get(k).checkCollisions(Obstacles)) {
                    enemyprojectiles.remove(k);
                } else {
                    if (ishit == false) {
                        if (enemyprojectiles.get(k).checkPlayerCollisions(player)) {
                            player.dy = Resolution.gamewidth / -50;
                            player.setGy(0);
                            player.g = Resolution.gamewidth / 9600;
                            if (player.movingleft) {
                                player.setDx(Resolution.gamewidth / 192);
                            } else {
                                player.setDx(Resolution.gamewidth / -192);
                            }
                            nohitcontrol = true;
                            ishit = true;
                            player.onground = false;
                            playerHit.start();
                            enemyprojectiles.remove(k);
                        }
                    }
                }
            }
        }
    };
//MOVING THE GUN
    AnimationTimer gunmovement = new AnimationTimer() {
        @Override
        public void handle(long now) {
            shooter.checkMotions();
            if (nohitcontrol == false) {
                GameScreen.setOnMouseMoved((MouseEvent event) -> {
                    if (nohitcontrol == false) {
                        shooter.setrotation(event.getX(), event.getY(), gamewidth / Resolution.gamewidth);
                    }
                });
                GameScreen.setOnMouseClicked((MouseEvent event) -> {
                    if (nohitcontrol == false) {
                        if (shooter.checkCoolDownRate() < 200 && shootagain) {
                            projectiles.add(shooter.newprojectile(BulletLayer));
                            MediaPlayer templaser = new MediaPlayer(laser);
                            templaser.play();
                        } else {
                            shootagain = false;
                        }
                    }
                });
            }
            shooter.updateUI();
            if (shooter.checkCoolDownRate() > 0) {
                shooter.firecooldown--;
            } else if (shooter.checkCoolDownRate() == 0 && shootagain == false) {
                shootagain = true;
            }
        }
    };
    AnimationTimer openingCutscene = new AnimationTimer() {
        double alarmtimer = 0;
        double addopacity = 0.1;
        double waitforstart = 0;
        boolean alarmstarted = false;
        double fadein = 100;

        @Override
        public void handle(long now) {
            if (alarmstarted == false) {
                alarmpulse.stop();
                waitforstart++;
                if (waitforstart == 150) {
                    alarmstarted = true;
                    waitforstart = 0;
                    alarmpulse.start();
                }
            } else {
                waitforstart++;
                if (waitforstart >= 300) {
                    fadein--;
                    if (fadein == 0) {
                        createPlayer(); //Creates player and gun that attaches to it
                        checkPause.start();
                        this.stop();
                    }
                    if (mapx == 1) {
                        Rectangle mask = (Rectangle) AllObstacles.get(3);
                        mask.setOpacity(fadein / 100);
                    } else if (mapx == 2) {
                        Rectangle mask = (Rectangle) AllObstacles.get(3);
                        mask.setOpacity(1);
                    }

                }
            }
        }
    };

    AnimationTimer alarmpulse = new AnimationTimer() {
        double alarmtimer = 0;
        double addopacity = 0.1;

        @Override
        public void handle(long now) {
            if (alarmtimer < 1 && alarmtimer > 0) {
                if (mapx == 1) {
                    Obstacles.get(5).getView().setOpacity(alarmtimer);
                }
            } else if (alarmtimer <= 0) {
                addopacity = 0.1;
                AlarmPlayer.stop();
                AlarmPlayer.play();
            } else if (alarmtimer >= 1) {
                addopacity = -0.006;
            }
            alarmtimer += addopacity;
            if (mapx == 2) {
                Obstacles.get(5).getView().setOpacity(1);
            }
        }
    };

    private TranslateTransition FloatingItem(TrickObstacle a) {
        Node b = a.getView();
        TranslateTransition animation = new TranslateTransition(Duration.seconds(1), b);
        animation.setByY(gamewidth / 132);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setAutoReverse(true);
        animation.play();
        return animation;
    }
    AnimationTimer checkPause = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (pausecheck.isPausing()) {
                pausecheck.keyboardBitSet.clear();
                Pause();
                checkPause.stop();
            }
        }
    };

    AnimationTimer deathCheck = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (player.getHealth() <= 0) {
                player.setG(Resolution.gamewidth / 15000);
                player.setHealth(0);
                checkPause.stop();
                gunmovement.stop();
                shooter.removeFromLayer();
                shooter.updateUI();
                playerHit.stop();
                nohitcontrol = true;
                Resolution.dead = true;
                Resolution.dead2 = true;
                Resolution.dead3 = true;
                Resolution.dead4 = true;
                Resolution.dead5 = true;
                Resolution.dead6 = true;
                playerDeath.start();
                this.stop();
            }
        }
    };

    public void Pause() {
        //PAUSE ALL TIMERS. PROBABLY A BETTER WAY TO DO THIS BUT HERE IT IS
        cameraLoop.stop();
        playerLoop.stop();
        checkPause.stop();
        playerHit.stop();
        deathCheck.stop();
        projectilegoing.stop();
        BossLogic.stop();
        crawlerLogic.stop();
        PlayerShaking.stop();
        CheckLevel.stop();
        UpdateUI.stop();
        gunmovement.stop();
        if (doormoving) {
            MoveDoor.stop();
        }
        LayerGroup.setEffect(new GaussianBlur());
        VBox pause = new VBox(20);
        pause.getChildren().add(new Label("Paused"));
        pause.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        pause.setAlignment(Pos.CENTER);
        pause.setPadding(new Insets(20));
        Button resume = new Button("Resume");
        Button quit = new Button("Quit");
        pause.getChildren().addAll(resume, quit);
        Stage popupStage = new Stage(StageStyle.TRANSPARENT);
        popupStage.initOwner(mainGame);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(pause, Color.TRANSPARENT));
        popupStage.show();
        resume.setOnAction(event -> {
            LayerGroup.setEffect(null);
            popupStage.hide();
            cameraLoop.start();
            playerLoop.start();
            deathCheck.start();
            projectilegoing.start();
            checkPause.start();
            CheckLevel.start();
            if (Crawlers.size() > 0) {
                crawlerLogic.start();
            }
            if (bossactive) {
                BossLogic.start();
            }
            if (hasgun) {
                projectilegoing.start();
                gunmovement.start();
                UpdateUI.start();
            }
            if (doormoving) {
                MoveDoor.start();
            }
        });
        quit.setOnAction(event -> {
            System.exit(0);
        });
    }

    AnimationTimer crawlerLogic = new AnimationTimer() {
        @Override
        public void handle(long now) {
            for (int k = 0; k < Crawlers.size(); k++) {
                Crawlers.get(k).moveCrawler(Obstacles);
                Crawlers.get(k).updateAnimation();
                Crawlers.get(k).updateUI();
            }
        }
    };
    AnimationTimer Scream = new AnimationTimer() {
        int ScreamFrames = 0;
        boolean atEndOfMedia = false;

        @Override
        public void handle(long now) {
            ScreamPlayer.setOnEndOfMedia(() -> {
                atEndOfMedia = true;
            });
            if (!atEndOfMedia) {
                ScreamFrames++;
                switch (ScreamFrames) {
                    case 5:
                        experiment.removeFromLayer();
                        experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossroar1.png", gamewidth / 3, gamewidth / 10, false, false)));
                        experiment.addToLayer();
                        break;
                    case 10:
                        experiment.removeFromLayer();
                        experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossroar2.png", gamewidth / 3, gamewidth / 10, false, false)));
                        experiment.addToLayer();
                        break;
                    case 15:
                        experiment.removeFromLayer();
                        experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossroar3.png", gamewidth / 3, gamewidth / 10, false, false)));
                        experiment.addToLayer();
                        break;
                    case 20:
                        experiment.removeFromLayer();
                        experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossroar2.png", gamewidth / 3, gamewidth / 10, false, false)));
                        experiment.addToLayer();
                        ScreamFrames = 0;
                    default:
                        break;
                }
                experiment.updateUI();
            } else {
                experiment.removeFromLayer();
                playMusic("src/Sounds/Attack!.mp3");
                experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossleep.png", gamewidth / 3, gamewidth / 10, false, false)));
                experiment.addToLayer();
                cutscenelogic = true;
                BossLogic.start();
                nohitcontrol = false;
                this.stop();
            }
        }
    };
    AnimationTimer PlayerShaking = new AnimationTimer() {
        int playershake = 0;
        int shaketime = 0;

        @Override
        public void handle(long now) {
            cameraLoop.stop();
            playerLoop.stop();
            gunmovement.stop();
            playershake++;
            shaketime++;
            if (playershake == 4) {
                player.setX(player.getX() - gamewidth / 50);
            } else if (playershake == 8) {
                player.setX(player.getX() + gamewidth / 50);
                playershake = 0;
            }
            if (shaketime == 300) {
                cameraLoop.start();
                playerLoop.start();
                gunmovement.start();
                shaketime = 0;
                playershake = 0;
                this.stop();
            }
            player.updateUI();
            shooter.checkMotions();
            shooter.updateUI();
        }
    };
    AnimationTimer BossLogic = new AnimationTimer() {
        boolean isattacking = false;
        int attacktype;
        int attacktimer = 200;
        int attackdelay = 0;
        int bosscharge = 200;
        int bossmoveup = 0;
        boolean abletoshoot = false;
        int stompwait = 150;
        int stomptime = 0;
        int attackset = 0;

        @Override
        public void handle(long now) {
            bossactive = true;
            if (experiment.getHealth() <= 0) {
                BossLogic.stop();
                bossactive = false;
                song.stop();
                cameraLoop.stop();
                player.setDx(0);
                nohitcontrol = true;
                UILayer.setVisible(false);
                ScreamPlayer.stop();
                gunmovement.stop();
                BossDeathCutscene.start();
                this.stop();
            }
            if (!isattacking) {
                isattacking = new Random().nextInt(attacktimer) == 0;
                attacktimer--;
                experiment.moveMonster(Obstacles);
                if (attacktimer == 1 || isattacking == true) {
                    experiment.removeFromLayer();
                    experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bosscharge.png", gamewidth / 3, gamewidth / 10, false, false)));
                    experiment.addToLayer();
                    isattacking = true;
                    attacktype = new Random().nextInt(3);
                    if (attacktype != 2) {
                        attackset++;
                        if (attackset == 3) {
                            attacktype = 2;
                            attackset = 0;
                        }
                    }

                    attacktimer = 200;
                }
                //Attack, then set isattacking to true
            } else {
                switch (attacktype) {
                    case 0:
                        attackdelay++;
                        if (attackdelay == 60 || attackdelay == 120 || attackdelay == 180) {
                            enemyprojectiles.add(experiment.smallprojectile(BulletLayer));
                        } else if (attackdelay == 220) {
                            experiment.removeFromLayer();
                            experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossleep.png", gamewidth / 3, gamewidth / 10, false, false)));
                            experiment.addToLayer();
                            attackdelay = 0;
                            isattacking = false;
                        }
                        break;
                    case 1:
                        bosscharge--;
                        if (Math.floorMod(bosscharge, 4) == 0) {
                            experiment.removeFromLayer();
                            experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossoverload.png", gamewidth / 3, gamewidth / 10, false, false)));
                            experiment.addToLayer();
                        } else {
                            experiment.removeFromLayer();
                            experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossoverload2.png", gamewidth / 3, gamewidth / 10, false, false)));
                            experiment.addToLayer();
                        }
                        if (bosscharge == 50) {
                            enemyprojectiles.add(experiment.bigprojectile(BulletLayer));
                        } else if (bosscharge <= 0) {
                            experiment.removeFromLayer();
                            experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossleep.png", gamewidth / 3, gamewidth / 10, false, false)));
                            experiment.addToLayer();
                            bosscharge = 200;
                            isattacking = false;
                        }
                        break;
                    case 2:
                        if (abletoshoot == false) {
                            bossmoveup++;
                            experiment.removeFromLayer();
                            switch (bossmoveup) {
                                case 30:
                                    experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossvulnerable1.png", gamewidth / 3 / 1.15, gamewidth / 10 * 1.42, false, false)));
                                    experiment.setX(experiment.getX() + (experiment.getW() - experiment.getW() * 0.87));
                                    experiment.setY(experiment.getY() + (experiment.getH() - experiment.getH() * 1.42));
                                    experiment.setW(experiment.getW() * 0.87);
                                    experiment.setH(experiment.getH() * 1.42);
                                    break;
                                case 60:
                                    experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossvulnerable2.png", gamewidth / 3, gamewidth / 10, false, false)));
                                    experiment.setX(experiment.getX() + (experiment.getW() - experiment.getW() * 0.93));
                                    experiment.setY(experiment.getY() + (experiment.getH() - experiment.getH() * 1.25));
                                    experiment.setW(experiment.getW() * 0.93);
                                    experiment.setH(experiment.getH() * 1.25);
                                    break;
                                case 90:
                                    experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossvulnerable3.png", gamewidth / 3, gamewidth / 10, false, false)));
                                    experiment.setX(experiment.getX() + (experiment.getW() - experiment.getW() * 0.81));
                                    experiment.setY(experiment.getY() + (experiment.getH() - experiment.getH() * 1.27));
                                    experiment.setW(experiment.getW() * 0.81);
                                    experiment.setH(experiment.getH() * 1.27);
                                    break;
                                case 140:
                                    experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossvulerablefinal.png", gamewidth / 3, gamewidth / 10, false, false)));
                                    experiment.setX(experiment.getX() + (experiment.getW() - experiment.getW() * 0.71));
                                    experiment.setY(experiment.getY() + (experiment.getH() - experiment.getH() * 1.06));
                                    experiment.setW(experiment.getW() * 0.71);
                                    experiment.setH(experiment.getH() * 1.06);
                                    abletoshoot = true;
                                    ScreamPlayer.stop();
                                    ScreamPlayer.play();
                                    experiment.setVuln(true);
                                    bossmoveup = 40;
                                    break;
                                default:
                                    break;
                            }
                            experiment.addToLayer();
                            break;
                        } else {
                            if (stompwait > 0) {
                                stompwait--;
                            }
                            if (experiment.getVuln() == false || stompwait == 0) {
                                experiment.setVuln(false);
                                bossmoveup--;
                                experiment.removeFromLayer();
                                switch (bossmoveup) {

                                    case 0:
                                        experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossleep.png", gamewidth / 3, gamewidth / 10, false, false)));
                                        if (stomptime == 0) {
                                            if (player.onground) {
                                                player.setHealth(player.getHealth() - 5);
                                                PlayerShaking.start();
                                            }
                                            experiment.setX(experiment.getX() + (experiment.getW() - gamewidth / 3));
                                            experiment.setY(gameheight - gameheight / 24 - gamewidth / 10);
                                            experiment.setW(gamewidth / 3);
                                            experiment.setH(gamewidth / 10);
                                        }
                                        stomptime++;
                                        cameraLoop.stop();
                                        bossmoveup = 1;
                                        if (Math.floorMod(stomptime, 4) == 0 || Math.floorMod(stomptime, 3) == 0 || Math.floorMod(stomptime, 5) == 0) {
                                            LayerGroup.setTranslateX(LayerGroup.getTranslateX() - gameheight / 100);
                                        }
                                        if (Math.floorMod(stomptime, 2) == 0) {
                                            LayerGroup.setTranslateX(LayerGroup.getTranslateX() + gameheight / 100);
                                        }
                                        if (stomptime == 100) {
                                            cameraLoop.start();
                                            isattacking = false;
                                            stompwait = 150;
                                            abletoshoot = false;
                                            stomptime = 0;
                                        }
                                        break;
                                    case 10:
                                        experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossvulnerable1.png", experiment.getW() * 1.07, experiment.getH() * 0.75, false, false)));
                                        experiment.setX(experiment.getX() + (experiment.getW() - experiment.getW() * 1.07));
                                        experiment.setY(experiment.getY() + (experiment.getH() - experiment.getH() * 0.75));
                                        experiment.setW(experiment.getW() * 1.07);
                                        experiment.setH(experiment.getH() * 0.75);
                                        break;
                                    case 20:
                                        experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossvulnerable2.png", experiment.getW() * 1.19, experiment.getH() * 0.73, false, false)));
                                        experiment.setX(experiment.getX() + (experiment.getW() - experiment.getW() * 1.19));
                                        experiment.setY(experiment.getY() + (experiment.getH() - experiment.getH() * 0.73));
                                        experiment.setW(experiment.getW() * 1.19);
                                        experiment.setH(experiment.getH() * 0.73);
                                        break;
                                    case 30:
                                        experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossvulnerable3.png", experiment.getW() * 1.29, experiment.getH() * 0.94, false, false)));
                                        experiment.setX(experiment.getX() + (experiment.getW() - experiment.getW() * 1.29));
                                        experiment.setY(experiment.getY() + (experiment.getH() - experiment.getH() * 0.94));
                                        experiment.setW(experiment.getW() * 1.29);
                                        experiment.setH(experiment.getH() * 0.94);
                                        break;
                                    default:
                                        break;
                                }
                                experiment.addToLayer();
                            }
                        }
                    default:
                        break;
                }
            }
            experiment.updateUI();
        }
    };

    AnimationTimer BossDeathCutscene = new AnimationTimer() {
        boolean initialtransition = true;
        boolean atEndOfMedia = false;
        boolean flipped = false;

        @Override
        public void handle(long now) {
            ScreamPlayer.setOnEndOfMedia(() -> {
                atEndOfMedia = true;
                experiment.removeFromLayer();
                if (flipped == false) {
                    experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossvulerableflipped.png", gamewidth / 3, gamewidth / 10, false, false)));
                } else {
                    experiment.setView(new ImageView(new Image("Sprites/Enemy/Boss1/bossvulerablefinal.png", gamewidth / 3, gamewidth / 10, false, false)));
                }
                flipped = !flipped;
                experiment.addToLayer();
                experiment.flip();
            });
            if (initialtransition) {
                if (LayerGroup.getTranslateX() < experiment.getX()) {
                    initialtransition = false;
                    ScreamPlayer.play();
                } else {
                    LayerGroup.setTranslateX(LayerGroup.getTranslateX() - gamewidth / 64);
                }

            } else {
                double camY = experiment.getY();
                if (camY > Resolution.gameheight / 4 && camY < gameheight - 6 * Resolution.gameheight / 8) {
                    LayerGroup.setTranslateY(-camY + Resolution.gameheight / 4);
                }
                if (atEndOfMedia) {
                    experiment.moveall();
                    if (experiment.getY() <= gameheight / 24) {
                        experiment.setY(gameheight / 24);
                        atEndOfMedia = false;
                        ScreamPlayer.stop();
                        ScreamPlayer.play();
                    } else if (experiment.getY() + experiment.getH() > 23 * gameheight / 24) {
                        Obstacles.get(1).removeFromLayer();
                        Obstacles.remove(1);
                        nohitcontrol = false;
                        gunmovement.start();
                        this.stop();
                        cameraLoop.start();
                    }
                }
            }
        }
    };
    //Making the UI accurate to current stats
    AnimationTimer UpdateUI = new AnimationTimer() {
        @Override
        public void handle(long now) {
            double amountfilled = shooter.firecooldown / 200;
            if (amountfilled < 0.25) {
                UILayer.getChildren().get(3).setVisible(false);
                UILayer.getChildren().get(4).setVisible(false);
                UILayer.getChildren().get(5).setVisible(false);
                UILayer.getChildren().get(6).setVisible(false);
            } else if (amountfilled >= 0.25 && amountfilled < 0.5) {
                UILayer.getChildren().get(3).setVisible(true);
                UILayer.getChildren().get(4).setVisible(false);
                UILayer.getChildren().get(5).setVisible(false);
                UILayer.getChildren().get(6).setVisible(false);
            } else if (amountfilled >= 0.5 && amountfilled < 0.75) {
                UILayer.getChildren().get(4).setVisible(true);
                UILayer.getChildren().get(5).setVisible(false);
                UILayer.getChildren().get(6).setVisible(false);
            } else if (amountfilled >= 0.75 && amountfilled < 0.98) {
                UILayer.getChildren().get(5).setVisible(true);
                UILayer.getChildren().get(6).setVisible(false);
            } else if (amountfilled >= 1) {
                UILayer.getChildren().get(6).setVisible(true);
            }
            Text healthcounter = (Text) UILayer.getChildren().get(1);
            healthcounter.setText(Integer.toString(player.health));
        }
    };
    //SWAPPING SCREENS
    AnimationTimer CheckLevel = new AnimationTimer() {
        @Override
        public void handle(long now) {
            //FIRST, CHECK COORDS. MAP IS GRID EXTENDED FROM 1,1 TOP LEFT CORNER, AS YOU HEAD LEFT AND DOWN X AND Y INCREASE RESPECTIVELY
            if (player.getX() > gamewidth) {
                mapx++;
                changeLevel();
                player.setX(1 - player.getW());
            } else if (player.getX() < 0 - player.getW()) {
                mapx--;
                changeLevel();
                player.setX(gamewidth - 1);
            }
            if (player.getY() > gameheight) {
                mapy++;
                changeLevel();
                player.setY(1 - player.getH());
            } else if (player.getY() < 0 - player.getH()) {
                mapy--;
                changeLevel();
                player.setY(gameheight - 1);
                if (gamewidth > Resolution.gamewidth) {
                    player.setX(player.getX() / gamewidth / Resolution.gamewidth - gamewidth / 24);
                }
            }
        }
    };
    //////////////
    //TIMERS END//
    //////////////
    ////////////////
    //SOUNDS BEGIN//
    ////////////////
    Media dooropen = new Media(new File("src/Sounds/DoorOpening.mp3").toURI().toString());
    MediaPlayer DoorOpenPlayer = new MediaPlayer(dooropen);
    Media Alarm = new Media(new File("src/Sounds/alarm.mp3").toURI().toString());
    MediaPlayer AlarmPlayer = new MediaPlayer(Alarm);
    Media glass = new Media(new File("src/Sounds/glass.wav").toURI().toString());
    MediaPlayer GlassShatterPlayer = new MediaPlayer(glass);
    Media laser = new Media(new File("src/Sounds/laser.mp3").toURI().toString());
    MediaPlayer LaserPlayer = new MediaPlayer(laser);
    Media enemyscream = new Media(new File("src/Sounds/screech.mp3").toURI().toString());
    MediaPlayer ScreamPlayer = new MediaPlayer(enemyscream);
}
