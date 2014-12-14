/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankx;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

/**
 *
 * @author markfavis
 */
public class GameWalls {

    int x, y, w, h, health, imageIndex;
    boolean destructable, visible;
    ArrayList<Image> sprite;

    public GameWalls(int xPassed, int yPassed, boolean passedDestructable, ArrayList<Image> imageArray) {
        this.x = xPassed;
        this.y = yPassed;
        this.sprite = imageArray;
        this.w = sprite.get(0).getWidth(null);
        this.h = sprite.get(0).getHeight(null);
        this.imageIndex = 0;
        this.destructable = passedDestructable;
        this.visible = false;
        if (destructable) {
            health = 2;
        } else {
            health = 10;
        }
    }

    public void update() {
        if (health == 0) {
            visible = false;
        }
    }

    public void reset() {
        if (destructable) {
            health = 2;
        }
    }

    public void draw(Graphics2D graphics, ImageObserver obs) {
        if (visible) {
            graphics.drawImage(sprite.get(imageIndex), x, y, obs);
        }
    }
}
