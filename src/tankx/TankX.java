package tankx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 *
 * @author markfavis
 */
public class TankX extends JApplet implements Runnable {

    // necessity
    private Thread mainThread;
    private BufferedImage bimg;
    static Graphics2D graphics;
    static final int w = 1024, h = 576;
    static GameEvents GlobalGameEvents;

    // Object ArrayLists
    ArrayList<GameExplosion> explode = new ArrayList<>();

    // Sprite ArrayLists
    ArrayList<Image> explosion = new ArrayList<>();
    ArrayList<Image> playerOneSprite = new ArrayList<>();
    ArrayList<Image> playerTwoSprite = new ArrayList<>();

    // Images
    Image groundImg;
    Image wallImg;
    Image wallDamagedImg;
    Image player1Img;
    Image player2Img;

    // player objects
    GamePlayer playerOne;
    GamePlayer playerTwo;

    public void init() {
        // set initial background color
        setBackground(Color.BLACK);

        // initialize images
        // initialize sprites
        try {
            groundImg = ImageIO.read(new File("ResourcesTank/Background.png"));
            wallImg = ImageIO.read(new File("ResourcesTank/Blue_wall1.png"));
            wallDamagedImg = ImageIO.read(new File("ResourcesTank/Blue_wall2.png"));
            player1Img = ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60.png"));
            player2Img = ImageIO.read(new File("ResourcesTank/Tank_red_basic_strip60.png"));

            // explosion sprite
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_1.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_2.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_3.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_4.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_5.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_6.png")));

            // player 1 sprite
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_01.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_02.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_03.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_04.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_05.png")));

            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_06.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_07.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_08.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_09.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_10.png")));

            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_11.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_12.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_13.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_14.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_15.png")));

            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_16.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_17.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_18.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_19.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_20.png")));

            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_21.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_22.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_23.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_24.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_25.png")));

            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_26.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_27.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_28.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_29.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_30.png")));

            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_31.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_32.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_33.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_34.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_35.png")));

            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_36.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_37.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_38.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_39.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_40.png")));

            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_41.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_42.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_43.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_44.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_45.png")));

            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_46.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_47.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_48.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_49.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_50.png")));

            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_51.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_52.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_53.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_54.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_55.png")));

            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_56.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_57.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_58.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_59.png")));
            playerOneSprite.add(ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_60.png")));

            // initlize players
            playerOne = new GamePlayer(playerOneSprite, 200, 200, 1, 1, w, h);
            //playerTwo = new GamePlayer(playerTwoSprite, 200, 300, 1, 2, w, h);

            // game events
            GlobalGameEvents = new GameEvents();
            GlobalGameEvents.addObserver(playerOne);
            //GlobalGameEvents.addObserver(playerTwo);

            // key controls
            KeyControl key = new KeyControl();
            addKeyListener(key);

        } catch (Exception ex) {
            System.out.println("Error: public void init() in TankX class");
        }

        // test; delete later
    }

    public class KeyControl extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            GlobalGameEvents.setValue(e);
        }
    }

    public void drawBackGroundWithTileImage() {
        int TileWidth = groundImg.getWidth(this);
        int TileHeight = groundImg.getHeight(this);

        int NumberX = (int) (w / TileWidth);
        int NumberY = (int) (h / TileHeight);

        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                graphics.drawImage(groundImg, j * TileWidth,
                        i * TileHeight + 0, TileWidth,
                        TileHeight, this);
            }
        }
    }

    public void drawExternalWalls() {
        int TileWidth = wallImg.getWidth(this);
        int TileHeight = wallImg.getHeight(this);

        int NumberX = (int) (w / TileWidth);
        int NumberY = (int) (h / TileHeight);

        for (int i = -1; i <= NumberY; i++) {
            if (i == 0 || i == NumberY - 1) {
                for (int j = 0; j <= NumberX; j++) {
                    graphics.drawImage(wallImg, j * TileWidth,
                            i * TileHeight, TileWidth,
                            TileHeight, this);
                }
            } else {
                graphics.drawImage(wallImg, 0,
                        i * TileHeight, TileWidth,
                        TileHeight, this);
                graphics.drawImage(wallImg, (NumberX - 1) * TileWidth,
                        i * TileHeight, TileWidth,
                        TileHeight, this);
            }
        }
    }

    public void drawGame() {
        drawBackGroundWithTileImage();
        drawExternalWalls();

        playerOne.draw(graphics, this);
    }

    public void drawGameWindow2() {
        drawBackGroundWithTileImage();
        drawExternalWalls();
    }

    @Override
    public void paint(Graphics g) {
        if (bimg == null) {
            Dimension windowSize = getSize();
            bimg = (BufferedImage) createImage(windowSize.width,
                    windowSize.height);
            graphics = bimg.createGraphics();
        }
        drawGame();
        g.drawImage(bimg, 0, 0, this);
    }

    @Override
    public void start() {
        mainThread = new Thread(this);
        mainThread.setPriority(Thread.NORM_PRIORITY);
        setFocusable(true);
        mainThread.start();
    }

    @Override
    public void run() {
        Thread me = Thread.currentThread();
        while (mainThread == me) {
            repaint();
            try {
                mainThread.sleep(25);
            } catch (InterruptedException e) {
                break;
            }

        }
    }

    public static void main(String argv[]) {
        final TankX game = new TankX();
        game.init();
        JFrame f = new JFrame("TankX");
        f.addWindowListener(new WindowAdapter() {
        });
        f.getContentPane().add("Center", game);
        f.pack();
        f.setSize(new Dimension(w, h));
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.start();
    }
}
