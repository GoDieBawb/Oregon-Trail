/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.GameManager;

/**
 *
 * @author Bawb
 */
public class Wagon extends Node {
 
    private Node   model;
    private int    moveSpeed;
    private int    turnSpeed;
    private int    currentHealth;
    private int    maxHealth;
    private String upgrades;
    
    public void load(AppStateManager stateManager, String filePath) {
        
        HashMap wagonInfo = stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().loadYaml(filePath + "WagonSave.yml");
        
        if (wagonInfo == null) {
           wagonInfo = makeNewWagon();
           save(stateManager, filePath, wagonInfo);
        }
        
        String type   = (String)  wagonInfo.get("Model"); 
        turnSpeed     = (Integer) wagonInfo.get("TurnSpeed");
        moveSpeed     = (Integer) wagonInfo.get("MoveSpeed");
        maxHealth     = (Integer) wagonInfo.get("MaxHealth");
        currentHealth = (Integer) wagonInfo.get("CurrentHealth");
        upgrades      = (String)  wagonInfo.get("Upgrades");
        
        if (type.equals("Basic")) 
        model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models");
        
        else
        model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models");
        
    }
    
    public void save(AppStateManager stateManager, String filePath, HashMap wagonInfo) {
        stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().saveYaml(filePath, wagonInfo);
    }
    
    private HashMap makeNewWagon() {
        
        HashMap wagonInfo = new HashMap();
        wagonInfo.put("Model", "Basic");
        wagonInfo.put("TurnSpeed", 1);
        wagonInfo.put("MoveSpeed", 1);
        wagonInfo.put("MaxHealth", 20);
        wagonInfo.put("CurrentHealth", 20);
        wagonInfo.put("Upgrades", "None");
        return wagonInfo;
        
    }
    
    public Node getModel() {
        return model;
    }
    
    public int getMoveSpeed() {
        return moveSpeed;
    }
    
    public int getTurnSpeed() {
        return turnSpeed;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    public String getUpgrades() {
        return upgrades;
    }
    
}
