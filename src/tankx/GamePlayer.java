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

    int staticX, staticY, speed, width, height, power, xOnMap, yOnMap;
    Rectangle bbox;
    static int sensitivity = 15;
    int playerNumber;
    int imageIndex;
    ArrayList<Image> imageArray;

    GamePlayer(ArrayList<Image> arrayOfImages, int x, int y, int speed, int pNum, int xRelativeToMap, int yRelativeToMap) {
        this.staticX = x;
        this.staticY = y;
        this.xOnMap = xRelativeToMap;
        this.yOnMap = yRelativeToMap;
        this.speed = speed;
        this.imageArray = arrayOfImages;
        this.imageIndex = 0;
        this.power = 0;
        width = imageArray.get(0).getWidth(null);
        height = imageArray.get(0).getHeight(null);
        this.playerNumber = pNum;
    }

    public void draw(Graphics2D graphics, ImageObserver obs) {
        graphics.drawImage(imageArray.get(imageIndex), staticX, staticY, obs);
    }

    public boolean collision(int x, int y, int w, int h) {
        bbox = new Rectangle(this.staticX, this.staticY, this.width, this.height);
        Rectangle otherBBox = new Rectangle(x, y, w, h);
        if (this.bbox.intersects(otherBBox)) {
            return true;
        }
        return false;
    }

    public void update(Observable obj, Object arg) {
        GameEvents ge = (GameEvents) arg;

        if (playerNumber == 1 && ge.type == 1) {
            KeyEvent e = (KeyEvent) ge.event;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (staticX > 0) {
                        if (imageIndex < imageArray.size() - 1) {
                            imageIndex++;
                        } else {
                            imageIndex = 0;
                        }
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (staticX < xOnMap - width) {
                        if (imageIndex > 0) {
                            imageIndex--;
                        } else {
                            imageIndex = 59;
                        }
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (staticY > 0) {
                        //y -= speed ;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (staticY < yOnMap - height - 20) {
                        //y += speed ;
                    }
                    break;
                default:
                    if (e.getKeyChar() == '/') {
                        fire();
                        System.out.println("player 1 Fire");
                    }
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
                case KeyEvent.VK_W:
                    if (staticY > 0) {
                        //y -= speed + sensitivity;
                    }
                    break;
                case KeyEvent.VK_S:
                    if (staticY < yOnMap - height - 20) {
                        //y += speed + sensitivity;
                    }
                    break;
                default:
                    if (e.getKeyChar() == ' ') {
                        fire();
                        System.out.println("player 2 Fire");
                    }
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

        coordinates.add(this.staticX + (imageArray.get(0).getWidth(null) / 2));
        coordinates.add(this.staticY + (imageArray.get(0).getHeight(null) / 2));

        return coordinates;
    }

    public int getXcenter() {
        return staticX + xOnMap / 2;
    }

    public int getYcenter() {
        return staticY + yOnMap / 2;
    }
}
