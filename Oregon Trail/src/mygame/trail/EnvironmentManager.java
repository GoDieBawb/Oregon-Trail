/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.trail;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.scene.Node;
import java.util.Random;
import mygame.GameManager;
import mygame.player.Player;
import mygame.player.PlayerManager;

/**
 *
 * @author Bawb
 */
public class EnvironmentManager {
    
    private Node              envNode;
    private AppStateManager   stateManager;
    private SimpleApplication app;
    
    public EnvironmentManager(AppStateManager stateManager) {
        this.stateManager = stateManager;
        app               = (SimpleApplication) stateManager.getApplication();
        envNode           = new Node();
    }
    
    public Node getEnvNode() {
        return envNode;
    }
    
    private void createObject() {
        Node object;
        
        if(randInt(1,25) == 25) {
            object = (Node) app.getAssetManager().loadModel("Models/Plants/GrassPatch.j3o");
            object.scale(.5f);  
            object.setName("Grass");
        }
        
        else {
            object = (Node) app.getAssetManager().loadModel("Models/Plants/Maple.j3o");
            //object.rotate(0, 0, randInt(0,180));
            object.scale(.25f);
            object.setName("Tree");
        }
        
        envNode.attachChild(object);   
        placeObject(object);
        stateManager.getState(GameManager.class).getUtilityManager().getMaterialManager().makeUnshaded(object);
    }
    
    private void placeObject(Node object) {
        
        int xSpot      = randInt(1,96);
        int ySpot      = 0;
        int zSpot      = randInt(1,64);
        int negChance  = randInt(1,2);
        int negChance1 = randInt(1,2);
        int negMult1   = 1;
        int negMult    = 1;
        
        if (negChance1 == 1)
        negMult1 = -1;
        
        if (negChance == 1)
        negMult = -1;
        
        if(object.getName().equals("Tree"))
            ySpot = -2;
        
        object.setLocalTranslation(xSpot*negMult, ySpot , zSpot*negMult1);
        //stateManager.getState(GameManager.class).getUtilityManager().getPhysicsManager().addToPhysics(object);
        
    }
    
    private int randInt(int min, int max) {
        Random rand   = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }       
    
    private void envMove(float tpf) {
        int  ms = stateManager.getState(PlayerManager.class).getPlayer().getWagonSpeed();
        WagonInteractionManager wagIntMan = stateManager.getState(TrailState.class).getTrailSceneManager().getWagonInteractionManager();
        for (int i = 0; i< envNode.getQuantity(); i++) {
            envNode.getChild(i).move(-ms*tpf,0,wagIntMan.getTurnValue()*tpf);
        }
        
    }
    
    private void envActionCheck() {
        
        Player player = stateManager.getState(PlayerManager.class).getPlayer();
        
        for (int i = 0; i < envNode.getQuantity(); i++) {
        
            Node plant = (Node) envNode.getChild(i);
            
            if(plant.getName().equals("Grass")) {
                
                float distance = player.getWorldTranslation().distance(plant.getWorldTranslation());
                        
                if ( distance < 5) {
                    
                    plant.removeFromParent();
                    int feedWeight = randInt(3,17);
                    int newFeed    = ((Integer) player.getInventory().get("Hay")) + feedWeight;
                    player.getInventory().put("Hay", newFeed);
                    player.getHud().showAlert("Grass", "You manage to collect " + feedWeight + " pounds of hay from the tall prarie grass.");
                    
                }
                
            }
            
            else if (player.getInWagon()) {
                
                CollisionResults results = new CollisionResults();
                Node wm = (Node) stateManager.getState(TrailState.class).getTrailSceneManager().getInteractableNode().getChild("Wagon");
                
                try {
                    plant.collideWith(wm.getWorldBound(), results);
                }
                catch (UnsupportedCollisionException e) {
                    return;
                }
                
                if (results.size() > 0) {
                    
                    //stateManager.getState(GameManager.class).getUtilityManager().getPhysicsManager().getPhysics().getPhysicsSpace().remove(plant);
                    plant.removeFromParent();
                    player.getWagon().setCurrentHealth(player.getWagon().getCurrentHealth()-5);
                    
                    if(player.getWagon().getCurrentHealth() <= 0) {
                        stateManager.getState(TrailState.class).getTrailSceneManager().killPlayer("Broken Wagon");
                    }
                    
                }
                
            }
            
        }
    
    }
    
    public void clear() {
        envNode.detachAllChildren();
        envNode = null;
        envNode = new Node();
    }
    
    public void update(float tpf) {
        
        envActionCheck();
        
        if (stateManager.getState(PlayerManager.class).getPlayer().getInWagon())
            envMove(tpf);    
        
        if (envNode.getQuantity() < 40) {
            createObject();
        }
        
        for (int i = 0; i < envNode.getQuantity(); i++) {
            
            if (envNode.getChild(i).getLocalTranslation().x <= -128) {
                //stateManager.getState(GameManager.class).getUtilityManager().getPhysicsManager().getPhysics().getPhysicsSpace().remove(envNode.getChild(i));
                envNode.getChild(i).removeFromParent();
            }
            
        }
        
    }
    
}
