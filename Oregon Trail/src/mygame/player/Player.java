/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.GameManager;
import mygame.util.AndroidManager;

/**
 *
 * @author Bawb
 */
public class Player extends Node {
    
    private HashMap         inventory;
    private Wagon           wagon;
    private AppStateManager stateManager;
    private String          filePath;
    private Node            model;
    private AnimControl     animControl;
    private AnimChannel     armChannel, legChannel;
    private BetterCharacterControl phys;
    private float           speedMult;
    private float           strafeMult;
    private HashMap         situation;
    
    public Player(AppStateManager stateManager) {
        setFilePath();
        loadInventory();
        loadWagon();
        createPhys();
        createAnimControl();
        setSpeedMult(.8f);
        setStrafeMult(.5f);
    }
    
    public void setSpeedMult(float mult) {
        speedMult = mult;
    }
    
    public void setStrafeMult(float mult) {
        strafeMult = mult;
    }
    
    public float getSpeedMult() {
        return speedMult;
    }
    
    public float getStrafeMult() {
        return strafeMult;
    }
    
    private void createAnimControl() {
        animControl = model.getControl(AnimControl.class);
        armChannel  = animControl.createChannel();
        legChannel  = animControl.createChannel();
        armChannel.addFromRootBone("TopSpine");
        legChannel.addFromRootBone("BottomSpine");
    }
    
    public void run() {
    }
    
    public void idle() {
    }
    
    public void setModel(AppStateManager stateManager, boolean isWagon) {
        
        if (isWagon) {
            model = wagon.getModel();
        }
        
        else {
            model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models/Truman/Truman.j3o");
            model.setLocalScale(.125f);
        }
        
    }
    
    private void setFilePath() {
        
        filePath = System.getProperty("user.home") + "/OregonTrail/";
        
        if ("Dalvik".equals(System.getProperty("java.vm.name"))) {
            filePath = stateManager.getState(AndroidManager.class).filePath;
        }
        
    }
    
    private void loadWagon() {
        wagon = new Wagon();
        wagon.load(stateManager, filePath);
    }
    
    private void loadInventory() {
        
       inventory =  stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().loadYaml(filePath + "InventorySave.yml");
    
       if (inventory == null) {
           createInventory();
       }
       
    }
    
    public void saveInventory() {
        stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().saveYaml(filePath + "InventorySave.yml", inventory);
    }
    
    private void createInventory() {
        inventory = new HashMap();
        inventory.put("Money", 500);
        inventory.put("Oxen", 0);
        inventory.put("Wheels", 0);
        inventory.put("Axels", 0);
        inventory.put("Food", 0);
        inventory.put("Bullets", 0);
        saveInventory();
    }
    
    public HashMap getInventory(){
        return inventory;
    }
    
    public Wagon getWagon() {
        return wagon;
    }
    
    public Node getModel() {
        return model;
    }
    
    private void createPhys() {
        phys = new BetterCharacterControl();
        model.addControl(phys);
    }
    
    public BetterCharacterControl getPhys() {
        return phys;
    }
    
    
    
}
