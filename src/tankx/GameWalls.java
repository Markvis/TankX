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

    public void update() {
        if (health < 1) {
            visible = false;
        }
    }

    public void reset() {
        if (destructable) {
            health = 2;
        }
        imageIndex = 0;
    }
    
    public boolean collision(int x, int y, int w, int h) {
        bbox = new Rectangle(this.x, this.y, this.w, this.h);
        Rectangle otherBBox = new Rectangle(x, y, w, h);
        if (this.bbox.intersects(otherBBox)) {
            return true;
        }
        return false;
    }

    public void draw(Graphics2D graphics,int xOffset, int yOffset, ImageObserver obs) {
        if (visible) {
            graphics.drawImage(sprite.get(imageIndex), x-xOffset, y-yOffset, obs);
        }
    }
}
