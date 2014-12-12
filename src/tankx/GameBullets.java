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
import java.util.Random;

/**
 *
 * @author markfavis
 */
public class GameBullets {

    Image img;
    int x, y, speed, width, height;
    boolean show;
    Rectangle bbox;

    public GameBullets(Image img, int speed) {
        this.img = img;
        this.x = 0;
        this.y = 0;
        this.speed = speed;
        this.show = false;
        width = img.getWidth(null);
        height = img.getHeight(null);
    }

    public void update() {
        if (show == true) {
            y += speed;
        }
    }

    public void reset() {
        show = false;
        this.x = 0;
        this.y = 0;
    }

    public void draw(Graphics2D graphics, ImageObserver obs) {
        if (show) {
            graphics.drawImage(img, x, y, obs);
        }
    }

    /**
     * 
     * @param x is the current x location 
     * @param y is the current y location
     * @param w is the width of the object
     * @param h is the height of the object
     * @return 
     */
    public boolean collision(int x, int y, int w, int h) {
        bbox = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle otherBBox = new Rectangle(x, y, w, h);
        if (this.bbox.intersects(otherBBox)) {
            return true;
        }
        return false;
    }
}
