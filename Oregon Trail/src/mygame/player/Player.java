/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import mygame.player.wagon.Wagon;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.GameManager;
import mygame.player.party.Party;
import mygame.player.party.PartyMember;
import mygame.trail.TrailState;
import mygame.util.AndroidManager;
import mygame.util.Gui;
import mygame.util.control.ChaseControl;

/**
 *
 * @author Bawb
 */
public class Player extends Node implements PartyMember {
    
    private HashMap         inventory;
    private Wagon           wagon;
    private String          filePath;
    private Node            model;
    private AnimControl     animControl;
    private AnimChannel     armChannel, legChannel;
    private float           speedMult;
    private float           strafeMult;
    private float           lookHeight;
    private HashMap         situation;
    private boolean         noMove;
    private Gui             hud;
    private boolean         inWagon;
    private boolean         isDead;
    private boolean         isAiming;
    private boolean         hasWon;
    private ChaseControl    chaseControl;
    private HashMap         condition;
    private Party           party;
    private BetterCharacterControl  phys;
    private final AppStateManager   stateManager;
    
    public Player(AppStateManager stateManager) {
        this.stateManager = stateManager;
        setFilePath();
        setModel();
        createPhys();
        createAnimControl();
        speedMult  = 2f;
        strafeMult = .5f;
        name       = "Player";
    }
    
    public void initParty() {
        
        party = new Party(stateManager, filePath);
        party.loadParty();
        
    }
    
    public void initCondition() {
        
        condition = (HashMap) stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().loadYaml(filePath + "PartySave.yml").get("Player");
        
        if (condition == null) {
            generate();
        }
    
        party.getInfo().put("Player", condition);
        party.saveParty();
    
    }
    
    
    private void generate() {
        
        condition         = new HashMap();
        String  firstName = party.randomMaleName();
        boolean starving  = false;
        boolean dysentary = false;
        boolean measles   = false;
        boolean tired     = false;
        
        condition.put("Name",      firstName);
        condition.put("Starving",  starving);
        condition.put("Dysentary", dysentary);
        condition.put("Measles",   measles);
        condition.put("Tired",     tired);
        party.getInfo().put("Player", condition);
        
    }
    
    public int getWagonSpeed() {
        
        int wagonSpeed = wagon.getMoveSpeed()/2;
        int oxCount    = (Integer) inventory.get("Oxen");
        
        if(oxCount > 2)
            oxCount = 2;
        
        wagonSpeed = wagonSpeed*oxCount;
        return wagonSpeed;
        
    }
    
    public void reset() {
        isDead = false;
        createSituation();
        createInventory();
        generate();
        party.createParty();
        getWagon().makeNewWagon(stateManager, filePath);
        saveAll();
    }
    
    public void saveAll(){
        saveInventory();
        saveSituation();
        party.saveParty();
        wagon.save(stateManager, filePath);
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
        
        SkeletonControl skelControl = model.getChild("Person").getControl(SkeletonControl.class);
        skelControl.setHardwareSkinningPreferred(true);
        
        animControl = model.getChild("Person").getControl(AnimControl.class);
        armChannel  = animControl.createChannel();
        legChannel  = animControl.createChannel();
        armChannel.addFromRootBone("TopSpine");
        legChannel.addFromRootBone("BottomSpine");
        armChannel.setAnim("ArmIdle");
        legChannel.setAnim("LegsIdle");
        
    }
    
    public void run() {
  
        if (!armChannel.getAnimationName().equals("ArmRun")) {
            armChannel.setAnim("ArmRun");
        }
      
        if (!legChannel.getAnimationName().equals("LegRun")) {
            legChannel.setAnim("LegRun");
        }    
      
    }
  
    public void idle() {
  
        if (!armChannel.getAnimationName().equals("ArmIdle")) {
            armChannel.setAnim("ArmIdle");
        }
      
        if (!legChannel.getAnimationName().equals("LegsIdle")) {
            legChannel.setAnim("LegsIdle");
        }
        
    }
    
    @Override
    public void setModel(Node model) {
    
        this.model = model;
        
    }
      
    private void setModel() {
        
          if(situation != null) {
              if (situation.get("Setting").equals("Trail")) 
              model = wagon.getModel();
          }
        
          else {
              model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models/Person/Person.j3o");
              model.setLocalScale(.2f);
              model.setLocalTranslation(0,.4f,0);
          }
        
          attachChild(model);
        
    }
    
    private void setFilePath() {
        
        filePath = System.getProperty("user.home") + "/../home/bob/Documents/Games/Oregon Trail/";
        
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
    
    @Override
    public Node getModel() {
        return model;
    }
    
    private void createPhys() {
        
        phys = new BetterCharacterControl(.3f, 1.1f, 100);
        
        if ("Dalvik".equals(System.getProperty("java.vm.name"))) 
            phys = new BetterCharacterControl(.11f, .8f, 100);
        
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
        getHud().getAimButton().hide();
        setIsDead(true);
        
        String deathInfo = "You've died of Dysentery";
        
        switch (reason) {
            case "Starvation":
                deathInfo = "You have starved to death";
                break;
            case "Stranded":
                deathInfo = "With the death of your last ox you become stranded in the wilderness... You soon run out of supplies and die";
                break;
            case "Bear":
                deathInfo = "You've been mauled to death by a bear";
                break;
            case "Broken Wagon":
                deathInfo = "The damage to your wagon has become to great and you become stranded in the wilderness... You soon run out of supplies and die";
                break;
            default:
                break;
        }
        
        getHud().showAlert("Dead", deathInfo);
        getHud().getInfoText().getButtonOk().show();
        
    }    
    
    public void finishGame() {
        hasWon = true;
        stateManager.getState(GameManager.class).clearAll();
        getHud().showAlert("Congratulations", "You have successfully reached Oregon!");
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
    
    public float getLookHeight() {
        return lookHeight;
    }
    
    public void setLookHeight(float newVal) {
        lookHeight = newVal;
    }
    
    public void createChaseControl() {
        chaseControl = new ChaseControl(stateManager, this);
    }
    
    public ChaseControl getChaseControl() {
        return chaseControl;
    }

    @Override
    public HashMap getCondition() {
        return condition;
    }
    
}
