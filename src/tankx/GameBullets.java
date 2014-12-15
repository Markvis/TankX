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
import java.util.Random;

/**
 *
 * @author markfavis
 */
public class GameBullets {

    int x, y, width, height;
    double xSpeed, ySpeed;
    boolean show;
    Rectangle bbox;
    int imageIndex;
    ArrayList<Image> imageArray;

    public GameBullets(ArrayList<Image> img, int xStart, int yStart, double xMoveSpeed, double yMoveSpeed, int passedImageIndex, boolean visibility) {
        this.x = xStart;
        this.y = yStart;
        this.xSpeed = xMoveSpeed;
        this.ySpeed = yMoveSpeed;
        this.show = visibility;
        this.imageArray = img;
        this.imageIndex = passedImageIndex;
        width = imageArray.get(0).getWidth(null);
        height = imageArray.get(0).getHeight(null);
    }

    public void update() {
        if (show == true) {
            x += xSpeed;
            y -= ySpeed;
        }
    }

    public void reset() {
        show = false;
        this.x = 0;
        this.y = 0;
    }

    public void draw(Graphics2D graphics, int xOffset, int yOffset, ImageObserver obs) {
        if (show) {
            graphics.drawImage(imageArray.get(imageIndex), x - xOffset, y - yOffset, obs);
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
