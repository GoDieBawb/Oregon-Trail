/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.hunt;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Random;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.trail.TrailState;

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
        
        int animalChance = randInt(1,3);
        
        if (System.currentTimeMillis()/1000 - cooldown > 3 && animalNode.getQuantity() < 3) {
                
            if (animalChance == 3) {
        
                createAnimal();
            
            }
                
            cooldown = System.currentTimeMillis()/1000;
                
        }
        
    }
    
    public void checkForHits(CollisionResults results) {
        
        for(int i = 0; i < results.size(); i++) {
        
            Node hitNode = results.getCollision(i).getGeometry().getParent();
            checkForAnimal(hitNode);
            
        }
        
    }
    
    private void checkForAnimal(Node hitNode) {
        
        if(hitNode.getParent() == app.getRootNode());
        
        else if (hitNode instanceof Animal) {
            Animal hitAnimal = (Animal) hitNode;
            hitAnimal.die();
        }
        
        else {
            checkForAnimal(hitNode.getParent());
        }
        
    }
    
    private void createAnimal() {
        Animal animal = new Animal(app.getStateManager());
        placeAnimal(animal);
        animal.run();
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
                zMove = randInt(-1,1);
                
            }
            
            else {
            
                zSpot = 190;
                zMove = -1;
                if(isNeg) {    
                    zSpot = -190;
                    zMove = 1;
                }
                
                xSpot = randInt(-75, 75);                
                xMove = randInt(-1, 1);
                
            }
            
            animal.setLocalTranslation(xSpot, 0, zSpot);
            animal.setMoveDir(xMove, zMove);
            animalNode.attachChild(animal);
    }
    
    private void moveAnimals(float tpf) {
    
        for(int i = 0; i < animalNode.getQuantity(); i++) {
            
            Animal animal = (Animal) animalNode.getChild(i);
            
            if(!animal.isDead()) {
                animal.move(animal.getMoveDir().mult(5).mult(tpf));
                animal.lookAt(animal.getMoveDir().mult(500), new Vector3f(0,1,0));
            }
            
            if (Math.abs(animal.getLocalTranslation().x) > 200) {
                animal.removeFromParent();
            }
            
            if (Math.abs(animal.getLocalTranslation().z) > 200) {
                animal.removeFromParent();
            }
            
        }
        
    }
    
    private void checkProximity() {
    
        for(int i = 0; i < animalNode.getQuantity(); i++) {
            
            Animal animal   = (Animal) animalNode.getChild(i);
            float  distance = animal.getWorldTranslation().distance(player.getWorldTranslation());
            
            if (distance < 20) {
            
                if (animal.getType().equals("Bear") && !player.getInWagon()) {
                    animal.setMoveDir(animal.getWorldTranslation().subtract(player.getWorldTranslation()).normalize().negate());
                }
                
                else {
                    animal.setMoveDir(animal.getWorldTranslation().subtract(player.getWorldTranslation()).normalize().multLocal(1,0,1));
                }
                
            }
            
            if (distance < 3) {
            
                if(animal.isDead()) {
                    
                    animal.removeFromParent();
                    int meatWeight = 0;
                    
                    if (animal.getType().equals("Bear")) {
                        meatWeight = randInt(35, 100);
                        player.getHud().showAlert("Hunt", "You collect " + meatWeight + " pounds of meat from the bear.");
                    }
                    
                    else if (animal.getType().equals("Deer")){
                        meatWeight =  randInt(35, 100);
                        player.getHud().showAlert("Hunt", "You collect " + meatWeight + " pounds of meat from the deer.");
                    }
                    
                    else if (animal.getType().equals("Rabbit")) {
                        meatWeight =  randInt(3, 9);
                        player.getHud().showAlert("Hunt", "You collect " + meatWeight + " pounds of meat from the rabbit.");
                    }
                    
                    int newMeat = ((Integer) player.getInventory().get("Food")) + meatWeight;
                    player.getInventory().put("Food", newMeat);
                    
                }
                
                else if (animal.getType().equals("Bear")) {
                    app.getStateManager().getState(TrailState.class).getTrailSceneManager().killPlayer("Bear");
                }
                
            }
            
        }
    
    }
    
    private int randInt(int min, int max) {
        Random rand   = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    
    public void clear() {
        animalNode.detachAllChildren();
        animalNode = null;
        animalNode = new Node();
    }
    
    public void update(float tpf) {
        checkPlayer();
        moveAnimals(tpf);
        checkProximity();
    }
    
}
