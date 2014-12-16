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
public class GameWalls {

    int x, y, w, h, health, imageIndex;
    Rectangle bbox;
    boolean destructable, visible;
    ArrayList<Image> sprite;

    /**
     * this is the constructor for the GameWalls
     * @param xPassed the  x coordinate of the wall
     * @param yPassed the  y coordinate of the wall
     * @param visibility this is the visibility of the wall false to not draw
     * @param passedDestructable this indicates the destructability of the wall
     * @param imageArray this is the sprite of the wall
     */
    public GameWalls(int xPassed, int yPassed, boolean visibility, boolean passedDestructable, ArrayList<Image> imageArray) {
        this.x = xPassed;
        this.y = yPassed;
        this.sprite = imageArray;
        this.w = sprite.get(0).getWidth(null);
        this.h = sprite.get(0).getHeight(null);
        this.imageIndex = 0;
        this.destructable = passedDestructable;
        this.visible = visibility;
        if (destructable) {
            health = 2;
        } else {
            health = 10;
        }
    }

    /**
     * this will update the wall based on its health
     */
    public void update() {
        if (health < 1) {
            visible = false;
        }
    }

    /**
     * this will reset the wall based on the visibility
     */
    public void reset() {
        if (destructable) {
            health = 2;
        }
        imageIndex = 0;
    }
    
    /**
     * this will check collision with the wall against any other object on the screen
     * @param x coordinate of the other object
     * @param y coordinate of the other object
     * @param w width of the other object
     * @param h height of the other object
     * @return true if theres a collision
     */
    public boolean collision(int x, int y, int w, int h) {
        bbox = new Rectangle(this.x, this.y, this.w, this.h);
        Rectangle otherBBox = new Rectangle(x, y, w, h);
        return this.bbox.intersects(otherBBox);
    }

    public void draw(Graphics2D graphics,int xOffset, int yOffset, ImageObserver obs) {
        if (visible) {
            graphics.drawImage(sprite.get(imageIndex), x-xOffset, y-yOffset, obs);
        }
    }
}
