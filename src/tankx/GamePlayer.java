/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankx;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author markfavis
 */
public class GamePlayer implements Observer {

    int staticX, staticY, speed, width, height, power, xOnMap, yOnMap, health;
    Rectangle bbox;
    static int sensitivity = 15;
    int playerNumber;
    int imageIndex;
    ArrayList<Image> imageArray;

    /**
     * gameplayer constructor
     * @param arrayOfImages this is the sprite of the player
     * @param x this is the x draw location of the player on the screen
     * @param y this is the y draw location of the player on the screen
     * @param speed this is the speed of the player
     * @param pNum this is the player number
     * @param xRelativeToMap this is the x location of the player relative to that map
     * @param yRelativeToMap this is the y location of the player relative to that map
     */
    GamePlayer(ArrayList<Image> arrayOfImages, int x, int y, int speed, int pNum, int xRelativeToMap, int yRelativeToMap) {
        this.staticX = x;
        this.staticY = y;
        this.xOnMap = xRelativeToMap;
        this.yOnMap = yRelativeToMap;
        this.speed = speed;
        this.imageArray = arrayOfImages;
        this.imageIndex = 0;
        this.power = 0;
        this.health = 3;
        width = imageArray.get(0).getWidth(null);
        height = imageArray.get(0).getHeight(null);
        this.playerNumber = pNum;
    }

    /**
     * draw the gameplayer on the screen
     * @param graphics this is the graphics2d
     * @param obs image observer
     */
    public void draw(Graphics2D graphics, ImageObserver obs) {
        graphics.drawImage(imageArray.get(imageIndex), staticX, staticY, obs);
    }
    
    /**
     * this will draw the gameplayer relative to the map
     * @param graphics this is the graphics2d location
     * @param xOffset this is the x offset of the gameplayer
     * @param yOffset this is the y offset of the gameplayer
     * @param obs this is the image observer
     */
    public void draw2(Graphics2D graphics, int xOffset, int yOffset, ImageObserver obs) {
        graphics.drawImage(imageArray.get(imageIndex), this.xOnMap - xOffset, this.yOnMap - yOffset, obs);
    }

    /**
     * this will check for collision of the gameplayer
     * @param x the x coordinate of the other object
     * @param y the y coordinate of the other object
     * @param w the width of the other object
     * @param h the height of the other object
     * @return true if theres a collision
     */
    public boolean collision(int x, int y, int w, int h) {
        bbox = new Rectangle(this.xOnMap, this.yOnMap, this.width, this.height);
        Rectangle otherBBox = new Rectangle(x, y, w, h);
        return this.bbox.intersects(otherBBox);
    }

    /**
     * this will update the gameplayer's sprite based on the key press
     * @param obj observable from caller
     * @param arg keyevent from caller
     */
    @Override
    public void update(Observable obj, Object arg) {
        GameEvents ge = (GameEvents) arg;

        if (playerNumber == 1 && ge.type == 1) {
            KeyEvent e = (KeyEvent) ge.event;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (imageIndex < imageArray.size() - 1) {
                        imageIndex++;
                    } else {
                        imageIndex = 0;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (imageIndex > 0) {
                        imageIndex--;
                    } else {
                        imageIndex = 59;
                    }
                    break;
                default:
//                    if (e.getKeyChar() == '/') {
//                        fire();
//                        System.out.println("player 1 Fire");
//                    }
            }
        } else if (playerNumber == 2 && ge.type == 1) {
            KeyEvent e = (KeyEvent) ge.event;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    if (imageIndex < imageArray.size() - 1) {
                        imageIndex++;
                    } else {
                        imageIndex = 0;
                    }
                    break;
                case KeyEvent.VK_D:
                    if (staticX < xOnMap - width) {
                        if (imageIndex > 0) {
                            imageIndex--;
                        } else {
                            imageIndex = 59;
                        }
                    }
                    break;
                default:
//                    if (e.getKeyChar() == ' ') {
//                        fire();
//                        System.out.println("player 2 Fire");
//                    }
            }
        } else if (ge.type == 2) {
            String msg = (String) ge.event;
            switch (msg) {
                case "Explosion player 1":
                    System.out.println("Player 1 hit!");
                    break;
                case "Explosion player 2":
                    System.out.println("Player 2 hit!");
                    break;
            }
        }
    }

    /**
     * this method will return the central x y coordinates of the player
     *
     * @return the center coordinates of the player
     */
    public ArrayList<Integer> fire() {
        ArrayList<Integer> coordinates = new ArrayList<>();

        coordinates.add(this.xOnMap + (imageArray.get(0).getWidth(null) / 2));
        coordinates.add(this.yOnMap + (imageArray.get(0).getHeight(null) / 2));

        return coordinates;
    }
}
