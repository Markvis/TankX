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

    GamePowerUp(ArrayList<Image> sprite, int xPassed, int yPassed) {

        this.visible = true;
        this.imageArray = sprite;
        this.imageIndex = 0;
        this.imageWidth = this.imageArray.get(0).getWidth(null);
        this.imageHeight = this.imageArray.get(0).getHeight(null);
        this.x = xPassed;
        this.y = yPassed;
    }

    public boolean collision(int x, int y, int w, int h) {
        bbox = new Rectangle(this.x, this.y, this.imageWidth, this.imageHeight);
        Rectangle otherBBox = new Rectangle(x, y, w, h);
        return this.bbox.intersects(otherBBox);
    }

    public void reset() {
        this.visible = false;
        this.x = 0;
        this.y = 0;
    }

    public void draw(Graphics2D graphics,int xOffset, int yOffset, ImageObserver obs) {
        if (visible) {
            graphics.drawImage(imageArray.get(imageIndex), x-xOffset, y-yOffset, obs);
        }
    }
}
