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
    int xMoveP1, yMoveP1, xMoveP2, yMoveP2, player1x, player1y, player2x, player2y;
    static GameEvents GlobalGameEvents;

    // Object ArrayLists
    ArrayList<GameExplosion> explode = new ArrayList<>();

    // Sprite ArrayLists
    ArrayList<Image> explosion = new ArrayList<>();
    ArrayList<Image> playerOneSprite = new ArrayList<>();
    ArrayList<Image> playerTwoSprite = new ArrayList<>();
    ArrayList<Image> wallsSprite = new ArrayList<>();
    ArrayList<Image> bulletSprite = new ArrayList<>();

    // Wall Objects
    ArrayList<GameWalls> wallArray = new ArrayList<>();

    // bullet objects
    ArrayList<GameBullets> bulletArray = new ArrayList<>();

    // Images
    Image groundImg;
//    Image player1Img;
//    Image player2Img;

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
//            player1Img = ImageIO.read(new File("ResourcesTank/Tank_blue_basic_strip60.png"));
//            player2Img = ImageIO.read(new File("ResourcesTank/Tank_red_basic_strip60.png"));

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

            // load bullet sprite
            for (int i = 0; i < 60; i++) {
                String FileName;
                if (i < 9) {
                    FileName = "ResourcesTank/Shell_basic_strip60/Shell_basic_0" + (i + 1) + ".png";
                } else {
                    FileName = "ResourcesTank/Shell_basic_strip60/Shell_basic_" + (i + 1) + ".png";
                }
                bulletSprite.add(ImageIO.read(new File(FileName)));
            }

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
            Scanner inFile = new Scanner(new File("ResourcesTank/pregeneratedTankXMap.txt")).useDelimiter("\t\\s*|\n|\r");
            ArrayList<String> temps = new ArrayList<>();

            while (inFile.hasNext()) {
                token = inFile.next();
                temps.add(token);
            }
            inFile.close();

            // 1 hard wall
            // 2 soft wall 
            // 3 player 1 location
            // 4 player 2 location
            // 0 no wall
            for (int i = 0, x = 0, y = 0; i < temps.size(); i++) {
                if (x > 2016) {
                    x = 0;
                    y += 32;
                }
                if ("2".equals(temps.get(i))) {
                    wallArray.add(new GameWalls(x, y, true, true, wallsSprite));
                } else if ("1".equals(temps.get(i))) {
                    wallArray.add(new GameWalls(x, y, true, false, wallsSprite));
                } else if ("3".equals(temps.get(i))) {
                    player1x = x;
                    player1y = y;
                } else if ("4".equals(temps.get(i))) {
                    player2x = x;
                    player2y = y;
                }
                x += 32;
            }

            // initlize players
            playerOne = new GamePlayer(playerOneSprite, screenWidth / 4, screenHeight / 2, 1, 1, player1x, player1y);
            playerTwo = new GamePlayer(playerTwoSprite, 200, 200, 1, 2, player2x, player2y);
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
    }

    public class KeyControl extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            GlobalGameEvents.setValue(e);
        }
    }

    // moves the player/ environment observer
    public class MovePlayer implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            KeyEvent e = (KeyEvent) GlobalGameEvents.event;

            if (e.getKeyCode() == KeyEvent.VK_UP && !playerToWallCollision(playerOne)) {
                double angle = getMultiplier(playerOne.imageIndex);
                yMoveP1 -= 15.0 * Math.sin(angle);
                xMoveP1 -= 15.0 * Math.cos(angle);
                playerOne.yOnMap -= 15.0 * Math.sin(angle);
                playerOne.xOnMap += 15.0 * Math.cos(angle);
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                double angle = getMultiplier(playerOne.imageIndex);
                yMoveP1 += 15.0 * Math.sin(angle);
                xMoveP1 += 15.0 * Math.cos(angle);
                playerOne.yOnMap += 15.0 * Math.sin(angle);
                playerOne.xOnMap -= 15.0 * Math.cos(angle);
            } else if (e.getKeyCode() == KeyEvent.VK_SLASH) {
                double angle = getMultiplier(playerOne.imageIndex);
                bulletArray.add(new GameBullets(bulletSprite, playerOne.xOnMap + playerOne.width / 2, playerOne.yOnMap + playerOne.height / 2,
                        15.0 * Math.cos(angle), 15.0 * Math.sin(angle), playerOne.imageIndex, true));
            }
//            if (e.getKeyCode() == KeyEvent.VK_W) {
//                yMoveP2 -= 25;
//            } else if (e.getKeyCode() == KeyEvent.VK_S) {
//                xMoveP2 -= 25;
//            }
            if (e.getKeyCode() == KeyEvent.VK_W && !playerToWallCollision(playerTwo)) {
                double angle = getMultiplier(playerTwo.imageIndex);
                yMoveP2 -= 15.0 * Math.sin(angle);
                xMoveP2 -= 15.0 * Math.cos(angle);
                playerTwo.yOnMap -= 15.0 * Math.sin(angle);
                playerTwo.xOnMap += 15.0 * Math.cos(angle);
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                double angle = getMultiplier(playerTwo.imageIndex);
                yMoveP2 += 15.0 * Math.sin(angle);
                xMoveP2 += 15.0 * Math.cos(angle);
                playerTwo.yOnMap += 15.0 * Math.sin(angle);
                playerTwo.xOnMap -= 15.0 * Math.cos(angle);
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                double angle = getMultiplier(playerTwo.imageIndex);
                bulletArray.add(new GameBullets(bulletSprite, playerTwo.xOnMap + playerTwo.width / 2, playerTwo.yOnMap + playerTwo.height / 2,
                        15.0 * Math.cos(angle), 15.0 * Math.sin(angle), playerTwo.imageIndex, true));
            }
        }
    }

    // will get the angle in degrees
    // then return it in radiands
    public double getMultiplier(double index) {
        if (index <= 15) {
            return Math.toRadians(index / 15 * 90);
        } else if (index <= 30 && index > 15) {
            return Math.toRadians(index / 30 * 180);
        } else if (index <= 45 && index > 30) {
            return Math.toRadians(index / 45 * 270);
        } else if (index <= 60 && index > 45) {
            return Math.toRadians(index / 60 * 360);
        }

        return 0.0;
    }

    /**
     * 
     * @param passedGraphics the graphics source for window
     * @param playerNum the player number
     */
    public void drawBackGroundWithTileImage(Graphics2D passedGraphics, int playerNum) {
        int xMove = 0;
        int yMove = 0;
        
         if(playerNum == 1){
             xMove = xMoveP1;
             yMove = yMoveP1;
         }else if (playerNum == 2) {
             xMove = xMoveP2;
             yMove = yMoveP2;
         }
        
        int TileWidth = groundImg.getWidth(this);
        int TileHeight = groundImg.getHeight(this);

        int NumberX = (int) (mapWidth / TileWidth);
        int NumberY = (int) (mapHeight / TileHeight);

        for (int i = -1; i <= NumberY; i++) {
            for (int j = -1; j <= NumberX; j++) {
                passedGraphics.drawImage(groundImg, j * TileWidth + (xMove % TileWidth),
                        i * TileHeight - (yMove % TileHeight), TileWidth,
                        TileHeight, this);
            }
        }
    }

    public boolean playerToWallCollision(Object arg) {

        GamePlayer temp = (GamePlayer) arg;

        int playerX = temp.xOnMap;
        int playerY = temp.yOnMap;
        int playerW = temp.width;
        int playerH = temp.height;

        for (int i = 0; i < wallArray.size(); i++) {
            if (wallArray.get(i).visible 
                    && wallArray.get(i).collision(playerX, playerY, playerW, playerH)) {
                return true;
            }
        }
        return false;
    }

    public void bulletToWallCollision() {
        for (int i = 0; i < wallArray.size(); i++) {
            for (int j = 0; j < bulletArray.size(); j++) {
                if (bulletArray.get(j).show 
                        && wallArray.get(i).destructable
                        && wallArray.get(i).visible
                        && wallArray.get(i).collision(bulletArray.get(j).x, bulletArray.get(j).y, 
                        bulletArray.get(j).width, bulletArray.get(j).height)) {
                    wallArray.get(i).health--;
                    wallArray.get(i).imageIndex++;
                    bulletArray.get(j).show = false;
                    bulletArray.get(j).reset();
                }
                else if(bulletArray.get(j).show 
                        && !wallArray.get(i).destructable
                        && wallArray.get(i).visible
                        && wallArray.get(i).collision(bulletArray.get(j).x, bulletArray.get(j).y, 
                        bulletArray.get(j).width, bulletArray.get(j).height)){
                    bulletArray.get(j).show = false;
                    bulletArray.get(j).reset();
                }
            }
        }
    }
    
    public void bulletToPlayerCollision(){
        
    }

    public void drawGame() {
        drawBackGroundWithTileImage(window1Graphics,1);
        drawBackGroundWithTileImage(window2Graphics,2);
        
        // check bullet and wall collision
        bulletToWallCollision();

        // update bullets
        for (int i = 0; i < bulletArray.size(); i++) {
            bulletArray.get(i).update();
        }

        // update walls
        for (int i = 0; i < wallArray.size(); i++) {
            wallArray.get(i).update();
        }

        // draw bullets window 1
        for (int i = 0; i < bulletArray.size(); i++) {
            bulletArray.get(i).draw(window1Graphics, playerOne.xOnMap - playerOne.staticX, playerOne.yOnMap - playerOne.staticY, this);
        }
        
        // draw bullets window 2
        for (int i = 0; i < bulletArray.size(); i++) {
            bulletArray.get(i).draw(window2Graphics, playerTwo.xOnMap - playerTwo.staticX, playerTwo.yOnMap - playerTwo.staticY, this);
        }

        // draw walls window 1
        for (int i = 0; i < wallArray.size(); i++) {
            wallArray.get(i).draw(window1Graphics, playerOne.xOnMap - playerOne.staticX, playerOne.yOnMap - playerOne.staticY, this);
        }
        
        // draw walls window 2
        for (int i = 0; i < wallArray.size(); i++) {
            wallArray.get(i).draw(window2Graphics, playerTwo.xOnMap - playerTwo.staticX, playerTwo.yOnMap - playerTwo.staticY, this);
        }

        playerOne.draw(window1Graphics, this);
        playerOne.draw2(window2Graphics, playerTwo.xOnMap - playerTwo.staticX, playerTwo.yOnMap - playerTwo.staticY, this);
        playerTwo.draw(window2Graphics, this);
        playerTwo.draw2(window1Graphics, playerOne.xOnMap - playerOne.staticX, playerOne.yOnMap - playerOne.staticY, this);
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
        
        // draw the two screens
        g.drawImage(bimg, 0, 0, this);
        g.drawImage(bimg2, screenWidth / 2, 0, this);
        
        // draw division line between the two screens
        g.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight);
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
