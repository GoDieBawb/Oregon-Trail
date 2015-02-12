/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import com.jme3.scene.Node;

/**
 *
 * @author Bawb
 */
public class Wagon extends Node {
 
    private Node model;
    private int  moveSpeed;
    private int  turnSpeed;
    
    public int getMoveSpeed() {
        return moveSpeed;
    }
    
    public int getTurnSpeed() {
        return turnSpeed;
    }
    
}
