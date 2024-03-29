package tankx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author markfavis
 */
public class TankX extends JApplet implements Runnable {

    // necessity
    private Thread mainThread;
    private BufferedImage bimg, bimg2, miniMap;
    static Graphics2D window1Graphics, window2Graphics, miniMapGraphics;
    static final int mapWidth = 2048, mapHeight = 1152, screenWidth = 1024, screenHeight = 576;
    int xMoveP1, yMoveP1, xMoveP2, yMoveP2, player1x, player1y, player2x, player2y, p1UP = 0, p2UP = 0;
    static GameEvents GlobalGameEvents;
    boolean gameStart = false;
    AudioStream backgroundMusic;
    AudioStream bigExplosionAudio;

    // Object ArrayLists
    ArrayList<GameExplosion> explode = new ArrayList<>();

    // Sprite ArrayLists
    ArrayList<Image> explosion = new ArrayList<>();
    ArrayList<Image> playerOneSprite = new ArrayList<>();
    ArrayList<Image> playerTwoSprite = new ArrayList<>();
    ArrayList<Image> wallsSprite = new ArrayList<>();
    ArrayList<Image> bulletSprite = new ArrayList<>();
    ArrayList<Image> rocketSprite = new ArrayList<>();
    ArrayList<Image> powerupSprite = new ArrayList<>();

    // Wall Objects
    ArrayList<GameWalls> wallArray = new ArrayList<>();

    // bullet objects
    ArrayList<GameBullets> bulletArray = new ArrayList<>();

    // powerup objects
    ArrayList<GamePowerUp> powerUpArray = new ArrayList<>();

    // Images
    Image groundImg;
    Image offScreen;

    // player objects
    GamePlayer playerOne;
    GamePlayer playerTwo;

    // environment mover
    MovePlayer moveEnvironment;

    /**
     * this will initialize the main game object
     */
    @Override
    public void init() {
        // set initial background color
        setBackground(Color.BLACK);

        // initialize resources
        try {
            // images
            groundImg = ImageIO.read(new File("ResourcesTank/Background.png"));

            // explosion sprite
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_1.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_2.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_3.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_4.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_5.png")));
            explosion.add(ImageIO.read(new File("ResourcesTank/explosion1_6.png")));

            // game powerup sprite
            powerupSprite.add(ImageIO.read(new File("ResourcesTank/Pickup_1.png")));

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
            // load rocket sprite
            for (int i = 0; i < 60; i++) {
                String FileName;
                if (i < 9) {
                    FileName = "ResourcesTank/Rocket_strip60/Rocket_0" + (i + 1) + ".png";
                } else {
                    FileName = "ResourcesTank/Rocket_strip60/Rocket_" + (i + 1) + ".png";
                }
                rocketSprite.add(ImageIO.read(new File(FileName)));
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
                } else if ("5".equals(temps.get(i))) {
                    powerUpArray.add(new GamePowerUp(powerupSprite, x, y));
                }
                x += 32;
            }

            // music
            InputStream in = new FileInputStream(new File("ResourcesTank/Music.mid"));
            backgroundMusic = new AudioStream(in);
            in = new FileInputStream(new File("ResourcesTank/Explosion_large.wav"));
            bigExplosionAudio = new AudioStream(in);

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

    /**
     * this will manage the player key presses
     */
    public class KeyControl extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            GlobalGameEvents.setValue(e);
        }
    }

    /**
     * this will observe what keys are pressed and respond accordingly
     */
    public class MovePlayer implements Observer {

        /**
         * this will update the game player
         *
         * @param o the observable from the caller
         * @param arg the key press from the caller
         */
        @Override
        public void update(Observable o, Object arg) {
            KeyEvent e = (KeyEvent) GlobalGameEvents.event;

            if (gameStart == false && e.getKeyCode() == KeyEvent.VK_ENTER) {
                gameStart = true;
                AudioPlayer.player.start(backgroundMusic);
            }

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
                if (p1UP == 0) {
                    bulletArray.add(new GameBullets(bulletSprite, playerOne.xOnMap + playerOne.width / 2, playerOne.yOnMap + playerOne.height / 2,
                            5.0 * Math.cos(angle), 5.0 * Math.sin(angle), playerOne.imageIndex, true, 1, 1));
                } else if (p1UP == 1) {
                    bulletArray.add(new GameBullets(rocketSprite, playerOne.xOnMap + playerOne.width / 2, playerOne.yOnMap + playerOne.height / 2,
                            10.0 * Math.cos(angle), 15.0 * Math.sin(angle), playerOne.imageIndex, true, 1, 3));
                }
            }
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
                if (p2UP == 0) {
                    bulletArray.add(new GameBullets(bulletSprite, playerTwo.xOnMap + playerTwo.width / 2, playerTwo.yOnMap + playerTwo.height / 2,
                            5.0 * Math.cos(angle), 5.0 * Math.sin(angle), playerTwo.imageIndex, true, 2, 1));
                } else if (p2UP == 1) {
                    bulletArray.add(new GameBullets(rocketSprite, playerTwo.xOnMap + playerTwo.width / 2, playerTwo.yOnMap + playerTwo.height / 2,
                            10.0 * Math.cos(angle), 15.0 * Math.sin(angle), playerTwo.imageIndex, true, 2, 3));
                }
            }
        }
    }

    /**
     * this will get the angle of the players direction
     *
     * @param index is the direction of the player
     * @return returns the angle in radians
     */
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
     * this will draw the background of the game on the passed screen by the
     * caller
     *
     * @param passedGraphics the graphics source for window
     * @param playerNum the player number
     */
    public void drawBackGroundWithTileImage(Graphics2D passedGraphics, int playerNum) {
        int xMove = 0;
        int yMove = 0;

        if (playerNum == 1) {
            xMove = xMoveP1;
            yMove = yMoveP1;
        } else if (playerNum == 2) {
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

    /**
     * this will check collision between the players and the walls
     *
     * @param arg is the GamePlayer
     * @return true if theres a collision false otherwise
     */
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

    /**
     * this will check bullet collision with soft and hard walls
     */
    public void bulletToWallCollision() {
        for (int i = 0; i < wallArray.size(); i++) {
            for (int j = 0; j < bulletArray.size(); j++) {
                if (bulletArray.get(j).show
                        && wallArray.get(i).destructable
                        && wallArray.get(i).visible
                        && wallArray.get(i).collision(bulletArray.get(j).x, bulletArray.get(j).y,
                                bulletArray.get(j).width, bulletArray.get(j).height)) {
                    wallArray.get(i).health -= bulletArray.get(j).damage;
                    wallArray.get(i).imageIndex++;
                    bulletArray.get(j).show = false;
                    bulletArray.get(j).reset();
                    explosionSound1();
                } else if (bulletArray.get(j).show
                        && !wallArray.get(i).destructable
                        && wallArray.get(i).visible
                        && wallArray.get(i).collision(bulletArray.get(j).x, bulletArray.get(j).y,
                                bulletArray.get(j).width, bulletArray.get(j).height)) {
                    bulletArray.get(j).show = false;
                    bulletArray.get(j).reset();
                    explosionSound1();
                }
            }
        }
    }

    /**
     * this will check bullet to player collision subtract health from player
     * based on the bullet damage
     */
    public void bulletToPlayerCollision() {
        for (int i = 0; i < bulletArray.size(); i++) {
            if (bulletArray.get(i).show) {
                if (bulletArray.get(i).firedBy == 2
                        && bulletArray.get(i).collision(playerOne.xOnMap, playerOne.yOnMap, playerOne.width, playerOne.height)) {
                    bulletArray.get(i).reset();
                    playerOne.health -= bulletArray.get(i).damage;
                    explosionSound1();
                } else if (bulletArray.get(i).firedBy == 1
                        && bulletArray.get(i).collision(playerTwo.xOnMap, playerTwo.yOnMap, playerTwo.width, playerTwo.height)) {
                    bulletArray.get(i).reset();
                    playerTwo.health -= bulletArray.get(i).damage;
                    explosionSound1();
                }
            }
        }
    }

    /**
     * this will check powerup to player collision
     *
     */
    public void powerUpToPlayerCollision() {
        for (int i = 0; i < powerUpArray.size(); i++) {
            if (powerUpArray.get(i).visible) {
                if (powerUpArray.get(i).collision(playerOne.xOnMap, playerOne.yOnMap, playerOne.width, playerOne.height)) {
                    powerUpArray.get(i).reset();
                    p1UP = 1;
                } else if (powerUpArray.get(i).collision(playerTwo.xOnMap, playerTwo.yOnMap, playerTwo.width, playerTwo.height)) {
                    powerUpArray.get(i).reset();
                    p2UP = 1;
                }
            }
        }
    }

    /**
     * this will draw the minimap on the screen
     */
    public void drawMiniMap() {
        if (miniMap == null) {
            Dimension windowSize = new Dimension(mapWidth, mapHeight);
            miniMap = (BufferedImage) createImage(windowSize.width,
                    windowSize.height);
            miniMapGraphics = miniMap.createGraphics();
        }

        int TileWidth = groundImg.getWidth(this);
        int TileHeight = groundImg.getHeight(this);

        int NumberX = (int) (mapWidth / TileWidth);
        int NumberY = (int) (mapHeight / TileHeight);

        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                miniMapGraphics.drawImage(groundImg, j * TileWidth,
                        i * TileHeight + 0, TileWidth,
                        TileHeight, this);
            }
        }

        // draw players
        playerOne.draw2(miniMapGraphics, 0, 0, this);
        playerTwo.draw2(miniMapGraphics, 0, 0, this);

        // draw powerups
        for (int i = 0; i < powerUpArray.size(); i++) {
            powerUpArray.get(i).draw(miniMapGraphics, 0, 0, this);
        }

        // draw bullets
        for (int i = 0; i < bulletArray.size(); i++) {
            bulletArray.get(i).draw(miniMapGraphics, 0, 0, this);
        }

        // draw walls window 1
        for (int i = 0; i < wallArray.size(); i++) {
            wallArray.get(i).draw(miniMapGraphics, 0, 0, this);
        }
    }

    /**
     * this will draw the whole game and check for all the collisions
     */
    public void drawGame() {
        drawBackGroundWithTileImage(window1Graphics, 1);
        drawBackGroundWithTileImage(window2Graphics, 2);

        // check collisions
        bulletToWallCollision();
        bulletToPlayerCollision();
        powerUpToPlayerCollision();

        // update bullets
        for (int i = 0; i < bulletArray.size(); i++) {
            bulletArray.get(i).update();
        }

        // update walls
        for (int i = 0; i < wallArray.size(); i++) {
            wallArray.get(i).update();
        }

        // draw powerups window 1
        for (int i = 0; i < powerUpArray.size(); i++) {
            powerUpArray.get(i).draw(window1Graphics, playerOne.xOnMap - playerOne.staticX, playerOne.yOnMap - playerOne.staticY, this);
        }

        // draw powerups window 2
        for (int i = 0; i < powerUpArray.size(); i++) {
            powerUpArray.get(i).draw(window2Graphics, playerTwo.xOnMap - playerTwo.staticX, playerTwo.yOnMap - playerTwo.staticY, this);
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

        drawMiniMap();
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

        if (gameStart == false) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, screenWidth, screenHeight);
            Image startScreen = null;
            try {
                startScreen = ImageIO.read(new File("ResourcesTank/Title.png"));
            } catch (Exception e) {
                System.out.println("start screen error");
            }

            g.drawImage(startScreen, (screenWidth / 2) - (startScreen.getWidth(null) / 2),
                    screenHeight / 2 - (startScreen.getHeight(null) / 2), this);

            g.setFont(new Font("A", Font.PLAIN, 30));
            g.setColor(Color.red);
            g.drawString("Please Press ENTER to Start", screenWidth / 3, screenHeight / 6);
            g.drawString("Player 1 Contols", 25, screenHeight / 3);
            g.drawString("Arrow Left", 25, screenHeight / 3 + 25);
            g.drawString("Arrow Right", 25, screenHeight / 3 + 50);
            g.drawString("Arrow Up", 25, screenHeight / 3 + 75);
            g.drawString("Arrow Down", 25, screenHeight / 3 + 100);
            g.drawString("/ to fire", 25, screenHeight / 3 + 125);

            g.drawString("Player 2 Contols", screenWidth * 3 / 4 + 25, screenHeight / 3);
            g.drawString("W", screenWidth * 3 / 4 + 25, screenHeight / 3 + 25);
            g.drawString("A", screenWidth * 3 / 4 + 25, screenHeight / 3 + 50);
            g.drawString("S", screenWidth * 3 / 4 + 25, screenHeight / 3 + 75);
            g.drawString("D", screenWidth * 3 / 4 + 25, screenHeight / 3 + 100);
            g.drawString("Space to fire", screenWidth * 3 / 4 + 25, screenHeight / 3 + 125);

            g.drawString("Powerup will destroy objets with one hit", screenWidth / 4, screenHeight * 5 / 6);
        }

        if (gameStart && playerOne.health > 0 && playerTwo.health > 0) {
            drawGame();

            // draw the two screens
            g.drawImage(bimg2, screenWidth / 2, 0, this);
            g.drawImage(bimg, 0, 0, this);
            // draw division line between the two screens
            g.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight);

            // draw minimap
            g.clearRect(screenWidth / 2 - mapWidth / 4, screenHeight - mapHeight / 8,
                    mapWidth / 8, mapHeight / 8);
            g.drawImage(miniMap, screenWidth / 2 - (miniMap.getWidth() / 8) / 2, screenHeight - miniMap.getHeight() / 8,
                 miniMap.getWidth() / 8, miniMap.getHeight() / 8, this);
        }

        if (playerOne.health <= 0) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, screenWidth, screenHeight);
            AudioPlayer.player.start(bigExplosionAudio);
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g.setColor(Color.RED);
            g.drawString("Player two Wins!!!", screenWidth / 3, screenHeight / 2);
        } else if (playerTwo.health <= 0) {
            AudioPlayer.player.start(bigExplosionAudio);
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g.setColor(Color.red);
            g.drawString("Player one Wins!!!", screenWidth / 3, screenHeight / 2);
        }
        g.dispose();
    }

    /**
     * this will make an explosion sound go off
     */
    public static void explosionSound1() {
        try {
            InputStream backgroundMusicPath = new FileInputStream(new File("ResourcesTank/Explosion_small.wav"));
            AudioStream explosionSound = new AudioStream(backgroundMusicPath);
            AudioPlayer.player.start(explosionSound);
        } catch (Exception e) {
            System.out.println("Error accessing explosionSound1() file");
        }
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
        f.setSize(new Dimension(1024, 576));
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.start();
    }
}
