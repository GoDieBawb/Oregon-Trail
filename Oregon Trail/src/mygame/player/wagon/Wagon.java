/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player.wagon;

import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.GameManager;

/**
 *
 * @author Bawb
 */
public class Wagon extends Node {
 
    private Node    model;
    private int     moveSpeed;
    private int     turnSpeed;
    private int     currentHealth;
    private int     maxHealth;;
    private HashMap wagonInfo;
    
    public void load(AppStateManager stateManager, String filePath) {
        
        wagonInfo = stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().loadYaml(filePath);
        
        if (wagonInfo == null) {
           makeNewWagon(stateManager, filePath);
        }
        
        String type   = (String)  wagonInfo.get("Model"); 
        turnSpeed     = (Integer) wagonInfo.get("TurnSpeed");
        moveSpeed     = (Integer) wagonInfo.get("MoveSpeed");
        maxHealth     = (Integer) wagonInfo.get("MaxHealth");
        currentHealth = (Integer) wagonInfo.get("CurrentHealth");
        
        if (type.equals("Basic")) 
            model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models/CoveredWagon/CoveredWagon.j3o");
        
        else
            model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models/CoveredWagon/CoveredWagon.j3o");
        
    }
    
    public void save(AppStateManager stateManager, String filePath) {
        wagonInfo.put("TurnSpeed", turnSpeed);
        wagonInfo.put("MoveSpeed", moveSpeed);
        wagonInfo.put("MaxHealth", maxHealth);
        wagonInfo.put("CurrentHealth", currentHealth);
        wagonInfo.put("Upgrades", "None");
        stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().saveYaml(filePath + "WagonSave.yml", wagonInfo);
    }
    
    public void makeNewWagon(AppStateManager stateManager, String filePath) {
        
        wagonInfo = new HashMap();
        wagonInfo.put("Model", "Basic");
        wagonInfo.put("TurnSpeed", 7);
        wagonInfo.put("MoveSpeed", 6);
        wagonInfo.put("MaxHealth", 20);
        wagonInfo.put("CurrentHealth", 20);
        wagonInfo.put("Upgrades", "None");
        turnSpeed     = (Integer) wagonInfo.get("TurnSpeed");
        moveSpeed     = (Integer) wagonInfo.get("MoveSpeed");
        maxHealth     = (Integer) wagonInfo.get("MaxHealth");
        currentHealth = (Integer) wagonInfo.get("CurrentHealth");
        save(stateManager, filePath);
        
    }
    
    public Node getModel() {
        return model;
    }
    
    public int getMoveSpeed() {
        return moveSpeed;
    }
    
    public void setMoveSpeed(int newSpeed) {
        wagonInfo.put("MoveSpeed", newSpeed);
        moveSpeed = (Integer) wagonInfo.get("MoveSpeed");
    }
    
    public int getTurnSpeed() {
        return turnSpeed;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public void setMaxHealth(int newHealth) {
        maxHealth = newHealth;
    }
    
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    public void setCurrentHealth(int newHealth) {
        currentHealth = newHealth;
    }
    
}
