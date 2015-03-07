/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.hunt;

import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.scene.Node;
import java.util.Random;

/**
 *
 * @author Bawb
 */
public class Animal extends Node {
    
    private String  type;
    private boolean isDead;
    private Node    model;
    
    public Animal(AppStateManager stateManager) {
        
        int chance = randInt(0,1);
        
        if(chance == 1)
            makeBear(stateManager);
        else
            makeDeer(stateManager);
        
    }
    
    private void makeBear(AppStateManager stateManager) {
        type  = "Bear";
        model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models/Creatures/bear.j3o");
    }
    
    private void makeDeer(AppStateManager stateManager) {
        type  = "Deer";
        model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models/Creatures/deer.j3o");
    }
    
    private int randInt(int min, int max) {
        Random rand   = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }       
    
}
