/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import java.util.ArrayList;

/**
 *
 * @author Bawb
 */
public class Player {
    
    private ArrayList<String> inventory;
    private Wagon             wagon;
    private int               money;
    
    public ArrayList<String> getInventory(){
        return inventory;
    }
    
    public Wagon getWagon() {
        return wagon;
    }
    
}
