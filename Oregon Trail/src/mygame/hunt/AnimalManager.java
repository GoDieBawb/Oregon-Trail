/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.hunt;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import java.util.Random;
import mygame.player.Player;
import mygame.player.PlayerManager;

/**
 *
 * @author Bawb
 */
public class AnimalManager {
    
    private SimpleApplication app;
    private Node              animalNode;
    private Player            player;
    private Long              cooldown;
    
    public AnimalManager(SimpleApplication app) {
        this.app   = app;
        animalNode = new Node();
        player     = app.getStateManager().getState(PlayerManager.class).getPlayer();
        cooldown   = System.currentTimeMillis()/1000;
    }
    
    public Node getAnimalNode() {
        return animalNode;
    }
    
    private void checkPlayer() {
        
        if (!player.getInWagon()) {
            if(System.currentTimeMillis()/1000 - cooldown > 3) {
                if (randInt(1,10) == 10) {
        
                    createAnimal();
            
                }
                
            }
            
            cooldown = System.currentTimeMillis();
            
        }
        
    }
    
    private void createAnimal() {
        System.out.println("Animal Created");
        Animal animal = new Animal(app.getStateManager());
        placeAnimal(animal);
    }
    
    private void placeAnimal(Animal animal) {
        
            boolean bigX  = false;
            boolean isNeg = false;
            int xSpot;
            int zSpot;
            int xMove;
            int zMove;
            
            if(randInt(1,2) == 1)
                bigX = true;
            
            if(randInt(1,2) == 1)
                isNeg = true;
            
            if(bigX) {
            
                xSpot = 190;
                xMove = -1;
                if(isNeg) {
                    xSpot = -190;
                    xMove = 1;
                }
                
                zSpot = randInt(-75, 75);
                zMove = randInt(-75,75);
                
            }
            
            else {
            
                zSpot = 190;
                zMove = 1;
                if(isNeg) {    
                    zSpot = -190;
                    zMove = -1;
                }
                
                xSpot = randInt(-75, 75);                
                xMove = randInt(-75, 75);
                
            }
            
            animal.setLocalTranslation(xSpot, 0, zSpot);
            animal.setMoveDir(xMove, zMove);
            animalNode.attachChild(animal);
    }
    
    private void moveAnimals(float tpf) {
    
        for(int i = 0; i < animalNode.getQuantity(); i++) {
            
            Animal animal = (Animal) animalNode.getChild(i);
            
            if(animal.isDead())
            animal.move(animal.getMoveDir().mult(tpf));
            
            if (Math.abs(animal.getLocalTranslation().x) > 200)
                animal.removeFromParent();
            
            if (Math.abs(animal.getLocalTranslation().z) > 200)
                animal.removeFromParent();
            
        }
        
    }
    
    private int randInt(int min, int max) {
        Random rand   = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }   
    
    public void update(float tpf) {
        checkPlayer();
        moveAnimals(tpf);
    }
    
}
