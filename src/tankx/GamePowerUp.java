/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankx;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

/**
 *
 * @author markfavis
 */
public class GamePowerUp {

    int x, y, imageWidth, imageHeight, imageIndex;
    boolean visible;
    Rectangle bbox;
    ArrayList<Image> imageArray;

    /**
     * gamepowerup constructor
     * @param sprite this is the arraylist of the sprite
     * @param xPassed this is the x coordinate of the powerup
     * @param yPassed this is the y coordinate of the powerup
     */
    GamePowerUp(ArrayList<Image> sprite, int xPassed, int yPassed) {

        this.visible = true;
        this.imageArray = sprite;
        this.imageIndex = 0;
        this.imageWidth = this.imageArray.get(0).getWidth(null);
        this.imageHeight = this.imageArray.get(0).getHeight(null);
        this.x = xPassed;
        this.y = yPassed;
    }

    /**
     * this will check for collision withe power up with any other object
     * @param x this is the x coordinate of the other object
     * @param y this is the y coordinate of the other object
     * @param w this is the width of the other object
     * @param h this is the height of the other object
     * @return true if theres a collision
     */
    public boolean collision(int x, int y, int w, int h) {
        bbox = new Rectangle(this.x, this.y, this.imageWidth, this.imageHeight);
        Rectangle otherBBox = new Rectangle(x, y, w, h);
        return this.bbox.intersects(otherBBox);
    }

    /**
     * this will reset the power up with x=0 y=0 visible = false
     */
    public void reset() {
        this.visible = false;
        this.x = 0;
        this.y = 0;
    }

    /**
     * this will draw the gamepowerup object on the screen
     * @param graphics this is the graphics2d object from the caller
     * @param xOffset this is the x offset relative to the screen
     * @param yOffset this is the y offset relative to the screen
     * @param obs this is the observer from the caller
     */
    public void draw(Graphics2D graphics,int xOffset, int yOffset, ImageObserver obs) {
        if (visible) {
            graphics.drawImage(imageArray.get(imageIndex), x-xOffset, y-yOffset, obs);
        }
    }
}
