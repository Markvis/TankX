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

    int x, y, width, height, imageIndex, firedBy, damage;
    double xSpeed, ySpeed;
    boolean show;
    Rectangle bbox;
    ArrayList<Image> imageArray;

    /**
     * initialize the bullet with the following:
     * 
     * @param img array list of images
     * @param xStart original x coordinate of the bullet
     * @param yStart original y coordinate of the bullet
     * @param xMoveSpeed x speed of the bullet
     * @param yMoveSpeed y speed of the bullet
     * @param passedImageIndex direction of the bullet
     * @param visibility true to draw object
     * @param fire player who wired
     * @param damage damage of the bullet
     */
    public GameBullets(ArrayList<Image> img, int xStart, int yStart, double xMoveSpeed, 
            double yMoveSpeed, int passedImageIndex, boolean visibility, int fire, int damage) {
        this.x = xStart;
        this.y = yStart;
        this.xSpeed = xMoveSpeed;
        this.ySpeed = yMoveSpeed;
        this.show = visibility;
        this.imageArray = img;
        this.imageIndex = passedImageIndex;
        this.firedBy = fire;
        this.damage = damage;
        width = imageArray.get(0).getWidth(null);
        height = imageArray.get(0).getHeight(null);
    }

    /**
     * move the bullet from point a to b based on the x and y speed
     */
    public void update() {
        if (show == true) {
            x += xSpeed;
            y -= ySpeed;
        }
    }

    /**
     * reset the bullet object to x = 0 y = 0 and turn visibility off
     */
    public void reset() {
        show = false;
        this.x = 0;
        this.y = 0;
    }

    /**
     * this will draw the bullet on the passed graphics2d
     * 
     * @param graphics which graphics2d to draw the bullet
     * @param xOffset x offset based relative to the player on screen
     * @param yOffset y offset based relative to the player on screen
     * @param obs the image observer
     */
    public void draw(Graphics2D graphics, int xOffset, int yOffset, ImageObserver obs) {
        if (show) {
            graphics.drawImage(imageArray.get(imageIndex), x - (this.width/2) - xOffset, y - (this.height/2) - yOffset, obs);
        }
    }

    /**
     * this will check the object for collision
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
        return this.bbox.intersects(otherBBox);
    }
}
