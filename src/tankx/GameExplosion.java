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
public class GameExplosion {
    
    
    int x, y, imageWidth, imageHeight, imageIndex;
    boolean visible;
    ArrayList <Image> imageArray;

    /**
     * 
     * @param sprite pass a arraylist of images also known as sprites
     */
    GameExplosion(ArrayList <Image> sprite) {
        this.x = 0;
        this.y = 0;
        this.visible = false;
        this.imageArray = sprite;
        this.imageIndex = 0;
        this.imageWidth = this.imageArray.get(0).getWidth(null);
        this.imageHeight = this.imageArray.get(0).getHeight(null);
    }
    
    /**
     * will update the explosion animation
     */
    public void update(){
        if(visible){
            this.imageIndex++;
            if(this.imageIndex >= imageArray.size()){
                visible = false;
                imageIndex = 0;
            }
        }
    }
    
    /**
     * will draw the animation of the explosion
     * @param graphics the graphics2d where the animation will be drawn
     * @param obs the imageobserver
     */
    public void draw(Graphics2D graphics, ImageObserver obs){
        if(visible){
            graphics.drawImage(imageArray.get(imageIndex), 
                    x-this.imageWidth/2, y-this.imageHeight/2, obs);
        }
    }
}
