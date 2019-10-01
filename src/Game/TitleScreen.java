package Game;

import Settings.Resolution;
import java.io.File;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TitleScreen extends Application {

    Scene mainmenu;
    Pane MenuPane, BGPane, LogoPane, FlashPane;
    ImageView[] MenuAnimation = new ImageView[4];
    int animationtimer = 0;
    int currentframe = 0;
    Group MenuGroup;
    ImageView PlummetLogo;
    Game game = new Game();
    Stage Menu;
    MediaPlayer titlesong;
    Rectangle whiteflash;
    int screentype = -1;
    boolean stageset = false;
    int gamewidth = (int) Resolution.gamewidth;
    int gameheight = (int) Resolution.gameheight;

    @Override
    public void start(Stage menu) {
        this.Menu = menu;
        menu.setTitle("Project Plummet");
        menu.setResizable(false);
        BGPane = new Pane();
        MenuPane = new Pane();
        LogoPane = new Pane();
        FlashPane = new Pane();
        MenuGroup = new Group();
        MenuGroup.getChildren().addAll(BGPane, MenuPane, LogoPane, FlashPane);
        whiteflash = new Rectangle();
        whiteflash.setWidth(Resolution.gamewidth);
        whiteflash.setHeight(Resolution.gameheight);
        whiteflash.setFill(Color.WHITE);
        whiteflash.setOpacity(0);
        FlashPane.getChildren().add(whiteflash);
        mainmenu = new Scene(MenuGroup, Resolution.gamewidth, Resolution.gameheight);
        mainmenu.getStylesheets().add("Game/ButtonStyle.css");
        Media song = new Media(new File("src/Sounds/Title.mp3").toURI().toString());
        titlesong = new MediaPlayer(song);
        titlesong.play();

        animateintro.start();

        MenuAnimation[0] = new ImageView(new Image("Sprites/Menu/title2.png", Resolution.gamewidth, Resolution.gameheight, false, false));
        MenuAnimation[1] = new ImageView(new Image("Sprites/Menu/title3.png", Resolution.gamewidth, Resolution.gameheight, false, false));
        MenuAnimation[2] = new ImageView(new Image("Sprites/Menu/title4.png", Resolution.gamewidth, Resolution.gameheight, false, false));
        BGPane.getChildren().add(MenuAnimation[0]);

        PlummetLogo = new ImageView(new Image("Sprites/Menu/plummetlogo.png", Resolution.gamewidth / 5, Resolution.gameheight / 10, false, false));
        PlummetLogo.setX(Resolution.gamewidth / 2 - Resolution.gamewidth / 64);
        PlummetLogo.setY(Resolution.gameheight / 6);
        PlummetLogo.setFitHeight(Resolution.gamewidth / 160);
        PlummetLogo.setFitWidth(Resolution.gamewidth / 32);

        menu.setScene(mainmenu);
        menu.show();
        startgame.start();
    }

    AnimationTimer animateintro = new AnimationTimer() {
        @Override
        public void handle(long now) {
            updateAnimation();
        }
    };

    public void updateAnimation() {
        animationtimer++;
        if (animationtimer == 200) {
            BGPane.getChildren().clear();
            BGPane.getChildren().add(MenuAnimation[currentframe]);
            MenuAnimation[currentframe].relocate(0, 0);
            currentframe++;
            if (currentframe == 2) {
                currentframe = 0;
            }
            animationtimer = 0;
        }
    }

    public void buttonCreator(Button button, String ButtonText, int x, int y) {
        button.setText(ButtonText);
        button.setPrefSize(Resolution.gamewidth / 2, Resolution.gameheight / 11);
        button.relocate(x, y);
        MenuPane.getChildren().add(button);
    }

    public void createButtons() {
        //MAIN MENU CREATION BEGINS
        Button startButton = new Button();
        buttonCreator(startButton, "New Game", gamewidth / 2 - gamewidth / 4, 2 * gameheight / 5);
        startButton.addEventHandler(ActionEvent.ACTION, (e) -> {
            game.Game(Menu);
            titlesong.stop();
        });

        Button settings = new Button();
        buttonCreator(settings, "Change settings", gamewidth / 2 - gamewidth / 4, 2 * gameheight / 5 + gameheight / 11 + gameheight / 22);
        settings.addEventHandler(ActionEvent.ACTION, (e) -> {
            MenuPane.getChildren().clear();
            Button fullscreenbutton = new Button();
            buttonCreator(fullscreenbutton, "Fullscreen", gamewidth / 2 - gamewidth / 4, 2 * gameheight / 5 + gameheight / 11 + gameheight / 22);
            fullscreenbutton.addEventHandler(ActionEvent.ACTION, (f) -> {
                screentype++;
                switch (screentype) {
                    case 0:
                        Menu.setFullScreen(true);
                        Resolution.isFullcreen = true;
                        fullscreenbutton.setText("Windowed");
                        break;
                    case 1:
                        Menu.setFullScreen(false);
                        Resolution.isFullcreen = false;
                        fullscreenbutton.setText("Fullscreen");
                        screentype = -1;
                        break;
                    default:
                        break;
                }
            });

            Button returnbutton = new Button();
            buttonCreator(returnbutton, "Return", gamewidth / 2 - gamewidth / 4, 2 * gameheight / 5 + 3 * gameheight / 11);
            returnbutton.addEventHandler(ActionEvent.ACTION, (f) -> {
                MenuPane.getChildren().clear();
                createButtons();
            });
        });

        Button quitgame = new Button();
        buttonCreator(quitgame, "Exit", gamewidth / 2 - gamewidth / 4, 2 * gameheight / 5 + 3 * gameheight / 11);
        quitgame.setOnAction((ActionEvent event) -> {
            System.exit(0);
        }
        );
    }

    AnimationTimer startgame = new AnimationTimer() {
        double logoadd = 1;
        double logoframe = 0;
        double timer = 0;
        boolean startappearing = false;
        boolean startflash = false;

        @Override
        public void handle(long now) {
            if (startappearing == false) {
                timer++;
                if (timer == 130) {
                    startappearing = true;
                    LogoPane.getChildren().add(PlummetLogo);
                }
            } else {
                if (startflash == false) {
                    logoframe++;
                    if (logoframe == 3) {
                        PlummetLogo.setFitWidth(PlummetLogo.getFitWidth() + Resolution.gameheight / 120);
                        PlummetLogo.setFitHeight(PlummetLogo.getFitHeight() + Resolution.gameheight / 600);
                        PlummetLogo.setX(PlummetLogo.getX() - Resolution.gameheight / 240);
                        PlummetLogo.setY(PlummetLogo.getY() - Resolution.gameheight / 1200);
                        logoframe = 0;
                        if (PlummetLogo.getFitHeight() > Resolution.gameheight / 10) {
                            startflash = true;
                            logoframe = 0;
                        }
                    }
                } else {
                    logoframe += logoadd;
                    whiteflash.setOpacity(logoframe / 100);
                    if (logoframe >= 100) {
                        logoadd = -0.5;
                    }
                    if (logoadd == -0.5 && logoframe <= 0) {
                        FlashPane.getChildren().remove(whiteflash);
                        createButtons();
                        this.stop();
                    }
                }
            }
        }
    };

    public static void main(String[] args) {
        launch(args);
    }

}
