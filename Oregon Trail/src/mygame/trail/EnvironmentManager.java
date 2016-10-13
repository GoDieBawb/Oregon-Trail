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
import mygame.player.wagon.WagonModel;

/**
 *
 * @author Bawb
 */
public class EnvironmentManager {
    
    private Node              envNode;
    private AppStateManager   stateManager;
    private SimpleApplication app;
    private boolean           initialFill;
    private int               minX;
    
    public EnvironmentManager(AppStateManager stateManager) {
        this.stateManager = stateManager;
        app               = (SimpleApplication) stateManager.getApplication();
        envNode           = new Node();
    }
    
    public Node getEnvNode() {
        return envNode;
    }
    
    private void createObject() {
        
        //Special Minus makes sure no special objects are generated on initial fill
        int specMinus = 0;
        
        if (initialFill)
            specMinus = 1;
        
        switch (randInt(1,25-specMinus)) {
            
            case 25:
                createSpecialObject();
                break;
                
            default:
                createEnvironmentalObject();
                break;
                
        }
        
    }
    
    private void createSpecialObject() {
    
        Node object;
        
        switch (randInt(1, 10)) {
            
            case 10:
                object = (Node) app.getAssetManager().loadModel("Models/TeePee/TeePee Group.j3o");
                object.scale(.75f);
                object.setName("TeePee");
                break;            
            
            case 5:
                object = (Node) app.getAssetManager().loadModel("Models/CoveredWagon/BrokenWagon.j3o");
                object.scale(1.5f);
                object.setName("Broken Wagon");
                break;                     
                
            default:
                object = (Node) app.getAssetManager().loadModel("Models/Plants/GrassPatch.j3o");
                object.scale(.5f);
                object.setName("Grass");
                break;
                
        }        
        
        envNode.attachChild(object);   
        placeObject(object);
        stateManager.getState(GameManager.class).getUtilityManager().getMaterialManager().makeUnshaded(object);        
        
    }
    
    private void createEnvironmentalObject() {
        
        Node object;
        
        switch (randInt(1,5)) {
            
            case 5:
                object = (Node) app.getAssetManager().loadModel("Models/Misc/moraf_boulders_clusters.j3o");
                object.scale(.5f);
                object.setName("Rock");
                break;              
            
            default:
                object = (Node) app.getAssetManager().loadModel("Models/Plants/Maple.j3o");
                object.scale(.25f);
                object.setName("Tree");
                break;
                
        }    
        
        envNode.attachChild(object);   
        placeObject(object);
        stateManager.getState(GameManager.class).getUtilityManager().getMaterialManager().makeUnshaded(object);        
        
    }
    
    private void placeObject(Node object) {
        
        int xSpot      = randInt(minX,96);
        int ySpot      = 0;
        int zSpot      = randInt(0,64);
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
        
        Player player  = stateManager.getState(PlayerManager.class).getPlayer();
        int wagonSpeed = player.getWagon().getMoveSpeed();

        WagonInteractionManager wagIntMan = stateManager.getState(TrailState.class).getTrailSceneManager().getWagonInteractionManager();
        
        for (int i = 0; i< envNode.getQuantity(); i++) {
            envNode.getChild(i).move(-wagonSpeed*tpf,0,wagIntMan.getTurnValue()*tpf);
        }
        
    }
    
    public void setInitialFill(boolean newVal) {
        
        initialFill = newVal;
        
        if(initialFill)
            minX = 0;
        
    }
    
    private void envActionCheck() {
        
        Player player = stateManager.getState(PlayerManager.class).getPlayer();
        
        for (int i = 0; i < envNode.getQuantity(); i++) {
        
            Node currentObject = (Node) envNode.getChild(i);
            
            if(currentObject.getName().equals("Grass")) {
                
                float distance = player.getWorldTranslation().distance(currentObject.getWorldTranslation());
                        
                if (distance < 5) {
                    
                    currentObject.removeFromParent();
                    int feedWeight = randInt(3,17);
                    int newFeed    = ((Integer) player.getInventory().get("Hay")) + feedWeight;
                    player.getInventory().put("Hay", newFeed);
                    player.getHud().showAlert("Grass", "You manage to collect " + feedWeight + " pounds of hay from the tall prarie grass.");
                    
                }
                
            }
            
            else if(currentObject.getName().equals("TeePee")) {
                
                float distance = player.getWorldTranslation().distance(currentObject.getWorldTranslation());
                        
                if (distance < 5) {
                    
                    String rewardType;
                    String info = "This friendly tribe provides you with ";
                    int    rewardAmount;
                    int    rewardChance = randInt(1,10);
                    
                    switch (rewardChance) {
                    
                        case 10:
                            rewardType   = "Oxen";
                            rewardAmount = randInt(1,2);
                            info += rewardAmount + " oxen.";
                            break;
                            
                        default:
                            rewardType   = "Food";
                            rewardAmount = randInt(20,50);
                            info += rewardAmount + " pounds of food.";
                            break;
                    
                    }
                    
                    currentObject.removeFromParent();
                    int newAmount    = ((Integer) player.getInventory().get(rewardType)) + rewardAmount;
                    player.getInventory().put(rewardType, newAmount);
                    player.getHud().showAlert("Tribe", info);
                    
                }
                
            }            
            
            else if(currentObject.getName().equals("Broken Wagon")) {
                
                float distance = player.getWorldTranslation().distance(currentObject.getWorldTranslation());
                        
                if (distance < 5) {
                    
                    String rewardType;
                    String info = "Searching the broken wagon yields ";
                    int    rewardAmount;
                    int    rewardChance = randInt(1,10);
                    
                    switch (rewardChance) {
                    
                        case 10:
                            rewardType   = "Money";
                            rewardAmount = randInt(20,200);
                            info += rewardAmount + "$ in cash.";
                            break;
                            
                        case 9:
                            rewardType   = "Tools";
                            rewardAmount = 1;
                            info += " a set of tools.";
                            break;                            
                            
                        default:
                            rewardType   = "Bullets";
                            rewardAmount = randInt(3,11);
                            info += rewardAmount + " bullets.";
                            break;
                    
                    }
                    
                    currentObject.removeFromParent();
                    int newAmount    = ((Integer) player.getInventory().get(rewardType)) + rewardAmount;
                    player.getInventory().put(rewardType, newAmount);
                    player.getHud().showAlert("Tribe", info);
                    
                }
                
            }            
            
            else if (player.getInWagon()) {
                
                CollisionResults results = new CollisionResults();
                WagonModel wm = (WagonModel) stateManager.getState(TrailState.class).getTrailSceneManager().getInteractableNode().getChild("Wagon");
                
                try {
                    currentObject.collideWith(wm.getWorldBound(), results);
                }
                catch (UnsupportedCollisionException e) {
                    return;
                }
                
                if (results.size() > 0) {
                    
                    //stateManager.getState(GameManager.class).getUtilityManager().getPhysicsManager().getPhysics().getPhysicsSpace().remove(plant);
                    currentObject.removeFromParent();
                    player.getWagon().setCurrentHealth(player.getWagon().getCurrentHealth()-5);
                    ((WagonModel) wm).getGui().setWagonHealth();
                    
                    if(player.getWagon().getCurrentHealth() <= 0) {
                        
                        int toolCount = (Integer) player.getInventory().get("Tools");
                        
                        if (toolCount > 0) {
                            int newTools = (Integer) player.getInventory().get("Tools") - 1;
                            player.getInventory().put("Tools", newTools);
                            player.getWagon().setCurrentHealth(player.getWagon().getMaxHealth());
                            player.getHud().showAlert("Repair", "You use your set of tools to repair your wagon.");
                            wm.getGui().setWagonHealth();
                            player.saveInventory();
                        }
                        
                        else {
                            stateManager.getState(TrailState.class).getTrailSceneManager().killPlayer("Broken Wagon");
                        }
                        
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
        
        else if (initialFill) {
            initialFill = false;
            minX        = 50;
        }
        
        for (int i = 0; i < envNode.getQuantity(); i++) {
            
            if (envNode.getChild(i).getLocalTranslation().x <= -128) {
                //stateManager.getState(GameManager.class).getUtilityManager().getPhysicsManager().getPhysics().getPhysicsSpace().remove(envNode.getChild(i));
                envNode.getChild(i).removeFromParent();
            }
            
        }
        
    }
    
}
