package Settings;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/*
Bixxta
 */
public class Resolution {

    static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public static double gamewidth = gd.getDisplayMode().getWidth();
    public static double gameheight = 5 * gamewidth / 9;
    public static boolean dead = false;
    public static boolean dead2 = false;
    public static boolean dead3 = false;
    public static boolean dead4 = false;
    public static boolean dead5 = false;
    public static boolean dead6 = false;
    public static boolean isFullcreen = false;
    public Resolution() {

    }
}
