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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
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
    private BufferedImage bimg, bimg2;
    static Graphics2D window1Graphics, window2Graphics;
    static final int mapWidth = 2048, mapHeight = 1152, screenWidth = 1024, screenHeight = 576;
    int xMoveP1, yMoveP1, xMoveP2, yMoveP2;
    static GameEvents GlobalGameEvents;

    // Object ArrayLists
    ArrayList<GameExplosion> explode = new ArrayList<>();

    // Sprite ArrayLists
    ArrayList<Image> explosion = new ArrayList<>();
    ArrayList<Image> playerOneSprite = new ArrayList<>();
    ArrayList<Image> playerTwoSprite = new ArrayList<>();
    ArrayList<Image> wallsSprite = new ArrayList<>();

    // Wall Objects
    ArrayList<GameWalls> wallArray = new ArrayList<>();

    // Images
    Image groundImg;
    Image player1Img;
    Image player2Img;

    // player objects
    GamePlayer playerOne;
    GamePlayer playerTwo;

    // environment mover
    MovePlayer moveEnvironment;

    public void init() {
        // set initial background color
        setBackground(Color.BLACK);

        // initialize resources
        try {
            // images
            groundImg = ImageIO.read(new File("ResourcesTank/Background.png"));
            player1Img = ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60.png"));
            player2Img = ImageIO.read(new File("ResourcesTank/Tank_red_basic_strip60.png"));

            // explosion sprite
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_1.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_2.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_3.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_4.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_5.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_6.png")));

            // wall sprite
            wallsSprite.add(ImageIO.read(new File("ResourcesTank/Blue_wall1.png")));
            wallsSprite.add(ImageIO.read(new File("ResourcesTank/Blue_wall2.png")));

            // initialize player 1 sprite
            for (int i = 0; i < 60; i++) {
                String FileName;
                if (i < 9) {
                    FileName = "ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_0" + (i + 1) + ".png";
                } else {
                    FileName = "ResourcesTank/Tank_blue_basic_strip60/Tank_blue_basic_" + (i + 1) + ".png";
                }
                playerOneSprite.add(ImageIO.read(new File(FileName)));
            }

            // initialize player 2 sprite
            for (int i = 0; i < 60; i++) {
                String FileName;
                if (i < 9) {
                    FileName = "ResourcesTank/Tank_red_basic_strip60/Tank_red_basic_0" + (i + 1) + ".png";
                } else {
                    FileName = "ResourcesTank/Tank_red_basic_strip60/Tank_red_basic_" + (i + 1) + ".png";
                }
                playerTwoSprite.add(ImageIO.read(new File(FileName)));
            }

            // map input
            String token = "";
            Scanner inFile = new Scanner(new File("ResourcesTank/pregeneratedTankXMap.txt")).useDelimiter("\t\\s*|\n");
            ArrayList<String> temps = new ArrayList<>();

            while (inFile.hasNext()) {
                token = inFile.next();
                temps.add(token);
            }
            inFile.close();

            // initlize players
            playerOne = new GamePlayer(playerOneSprite, screenWidth / 4, screenHeight / 2, 1, 1, mapWidth / 4, mapHeight / 2);
            playerTwo = new GamePlayer(playerTwoSprite, 200, 200, 1, 2, mapWidth, mapHeight);
            playerTwo.imageIndex = 30;

            moveEnvironment = new MovePlayer();

            // game events
            GlobalGameEvents = new GameEvents();
            GlobalGameEvents.addObserver(playerOne);
            GlobalGameEvents.addObserver(playerTwo);
            GlobalGameEvents.addObserver(moveEnvironment);

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

    public class MovePlayer implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            KeyEvent e = (KeyEvent) GlobalGameEvents.event;
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                yMoveP1 -= 25;
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                xMoveP1 -= 25;
            }
        }

    }

    public void drawBackGroundWithTileImage(Graphics2D passedGraphics) {
        int TileWidth = groundImg.getWidth(this);
        int TileHeight = groundImg.getHeight(this);

        int NumberX = (int) (mapWidth / TileWidth);
        int NumberY = (int) (mapHeight / TileHeight);

        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                passedGraphics.drawImage(groundImg, j * TileWidth + (xMoveP1 % TileWidth),
                        i * TileHeight + (yMoveP1 % TileHeight), TileWidth,
                        TileHeight, this);
            }
        }
    }
    
    public void drawWall(Graphics2D passedGraphics){
        
    }

    public void drawGame() {
        drawBackGroundWithTileImage(window1Graphics);
        //drawBackGroundWithTileImage(window2Graphics);

        playerOne.draw(window1Graphics, this);
        //playerTwo.draw(window2Graphics, this);
    }

    @Override
    public void paint(Graphics g) {
        if (bimg == null) {
            Dimension windowSize = getSize();
            bimg = (BufferedImage) createImage(windowSize.width / 2,
                    windowSize.height);
            window1Graphics = bimg.createGraphics();
        }
        if (bimg2 == null) {
            Dimension windowSize = getSize();
            bimg2 = (BufferedImage) createImage(windowSize.width / 2,
                    windowSize.height);
            window2Graphics = bimg2.createGraphics();
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, mapWidth, mapHeight);

        drawGame();
        // draw division line between the two screens
        g.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight);
        g.drawImage(bimg, 0, 0, this);
        g.drawImage(bimg2, mapWidth / 2, 0, this);
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
//        f.setLocationRelativeTo(null);
        f.addWindowListener(new WindowAdapter() {
        });
        f.getContentPane().add("Center", game);
        f.pack();
        f.setSize(new Dimension(1024, 576));
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.start();
    }
}
