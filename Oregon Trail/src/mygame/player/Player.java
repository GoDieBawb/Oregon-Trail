/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import mygame.player.wagon.Wagon;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.GameManager;
import mygame.player.wagon.WagonGui;
import mygame.player.wagon.WagonModel;
import mygame.trail.TrailState;
import mygame.util.AndroidManager;
import mygame.util.Gui;

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
    private float           speedMult;
    private float           strafeMult;
    private HashMap         situation;
    private boolean         noMove;
    private Gui             hud;
    private boolean         inWagon;
    private boolean         isDead;
    private boolean         isAiming;
    private BetterCharacterControl phys;
    
    public Player(AppStateManager stateManager) {
        this.stateManager = stateManager;
        setFilePath();
        setModel();
        createPhys();
        createAnimControl();
        setSpeedMult(3f);
        setStrafeMult(1f);
        setName("Player");
    }
    
    public int getWagonSpeed() {
        
        int wagonSpeed = wagon.getMoveSpeed()/2;
        int oxCount    = (Integer) inventory.get("Oxen");
        if(oxCount > 2)
            oxCount = 2;
        wagonSpeed  = wagonSpeed*oxCount;
        return wagonSpeed;
        
    }
    
    public void reset() {
        isDead = false;
        createSituation();
        createInventory();
        getWagon().makeNewWagon(stateManager, filePath);
        saveAll();
    }
    
    public void saveAll(){
        saveInventory();
        saveSituation();
    }
    
    public boolean getInWagon() {
        return inWagon;
    }
    
    public void setInWagon(boolean inWagon) {
        this.inWagon = inWagon;
    }
    
    public void createHud() {
        hud = new Hud(stateManager);
    }
    
    public Hud getHud() {
        return (Hud) hud;
    }
    
    public void load() {
        loadInventory();
        loadWagon();
        loadSituation();
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
        animControl = model.getChild("Body").getControl(AnimControl.class);
        armChannel  = animControl.createChannel();
        legChannel  = animControl.createChannel();
        armChannel.addFromRootBone("TopSPine");
        legChannel.addFromRootBone("BottomSpine");
        armChannel.setAnim("StillArms");
        legChannel.setAnim("StillLegs");
    }
    
    public void run() {
  
        if (!armChannel.getAnimationName().equals("UnarmedRun")) {
            armChannel.setAnim("UnarmedRun");
        }
      
        if (!legChannel.getAnimationName().equals("RunAction")) {
            legChannel.setAnim("RunAction");
        }    
      
    }
  
    public void idle() {
  
        if (!armChannel.getAnimationName().equals("StillArms")) {
            armChannel.setAnim("StillArms");
        }
      
        if (!legChannel.getAnimationName().equals("StillLegs")) {
            legChannel.setAnim("StillLegs");
        }
        
    }
    
    public void setModel(Node model) {
    
        this.model = model;
        
    }
      
    public void setModel() {
        
          if(situation != null) {
              if (situation.get("Setting").equals("Trail")) 
              model = wagon.getModel();
          }
        
          else {
              model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models/Truman/Truman.j3o");
              model.setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/Citizen.j3m"));
              model.setLocalScale(.125f);
          }
        
          attachChild(model);
        
      }
    
    private void setFilePath() {
        
        filePath = System.getProperty("user.home") + "/Oregon Trail/";
        
        if ("Dalvik".equals(System.getProperty("java.vm.name"))) {
            filePath = stateManager.getState(AndroidManager.class).filePath;
        }
        
    }
    
    public String getFilePath() {
        return filePath;
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
        inventory.put("Food", 0);
        inventory.put("Tools", 0);
        inventory.put("Bullets", 0);
        inventory.put("Hay", 0);
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
        phys = new BetterCharacterControl(.3f, 1.1f, 100);
        addControl(phys);
    }
    
    public BetterCharacterControl getPhys() {
        return phys;
    }
    
    private void loadSituation() {
        
        situation = stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().loadYaml(filePath + "SituationSave.yml");
        
        if (situation == null) {
            createSituation();
        }
        
    }
    
    public void saveSituation() {
        stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().saveYaml(filePath + "SituationSave.yml", situation);
    }
    
    private void createSituation() {
        situation = new HashMap();
        situation.put("Setting", "Town");
        situation.put("Setting Name", "Independence");
        situation.put("Total Distance", 0);
        situation.put("Distance Remaining", 0);
        situation.put("Day Number", 1);
        situation.put("Goals Reached", 0);
        situation.put("Weather", "Hot");
        situation.put("Biome", "Plains");
        saveSituation();
    }
    
    public void shoot() {
        
        Ray ray                  = new Ray(stateManager.getApplication().getCamera().getLocation(), stateManager.getApplication().getCamera().getDirection());
        CollisionResults results = new CollisionResults();
        ((SimpleApplication)stateManager.getApplication()).getRootNode().collideWith(ray, results);
        stateManager.getState(TrailState.class).getTrailSceneManager().getAnimalManager().checkForHits(results);
        
    }
    
    public void die(String reason) {
        stateManager.getState(GameManager.class).clearAll();
        setIsDead(true);
        
        String deathInfo = "You've died of Dysentery";
        
        if(reason.equals("Starvation")) {
            deathInfo = "You have starved to death";
        }
        
        else if (reason.equals("Stranded")) {
            deathInfo = "With the death of your last ox you become stranded in the wilderness... You soon run out of supplies and die";
        }
        
        else if (reason.equals("Bear")) {
            deathInfo = "You've been mauled to death by a bear";
        }        
        
        getHud().showAlert("Dead", deathInfo);
        getHud().getInfoText().getButtonOk().show();
    }    
    
    public HashMap getSituation() {
        return situation;
    }
    
    public void setNoMove(boolean noMove) {
        this.noMove = noMove;
    }
    
    public boolean getNoMove() {
        return noMove;
    }
    
    public boolean getIsDead() {
        return isDead;
    }
    
    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }
    
    public void setIsAiming(boolean aim) {
        isAiming = aim;
    }
    
    public boolean isAiming() {
        return isAiming;
    }
    
}
