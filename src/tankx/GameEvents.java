/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankx;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 *
 * @author markfavis
 */
public class GameEvents extends Observable {

    int type;
    Object event;

    public void setValue(KeyEvent e) {
        type = 1; // let's assume this means key input. 
        //Should use CONSTANT value for this when you program
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setValue(String msg) {
        type = 2;
        event = msg;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }
    
    /**
     * 
     * @param status if status is 1, its gameover, if status is 2 then its a win
     */

    public void gameOver(int status) {
        if (status == 0) {
            type = 3;
            event = "You Lose";
            setChanged();
            notifyObservers(this);
        } else if (status == 1) {
            type = 4;
            event = "You Win";
            setChanged();
            notifyObservers(this);
        } else {
            System.out.println("invalid gameover Status");
        }
    }
}
