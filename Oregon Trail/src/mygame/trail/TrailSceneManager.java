/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.trail;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Random;
import mygame.GameManager;
import mygame.hunt.AnimalManager;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.player.wagon.WagonGui;
import mygame.player.wagon.WagonModel;
import mygame.util.Interactable;
import mygame.util.SkeletonFinder;

/**
 *
 * @author Bawb
 */
public class TrailSceneManager {
    
    private SimpleApplication        app;
    private Node                     scene;
    private float                    sceneMult;
    private Player                   player;
    private WagonInteractionManager  wagIntMan;
    private Node                     interactableNode;
    private EnvironmentManager       envMan;
    private AnimalManager            anMan;
    private boolean                  wagonized;
    private Long                     updateWait;
    private boolean                  scaledGrass;
    
    public TrailSceneManager(SimpleApplication app) {
    
        this.app   = (SimpleApplication) app;
        sceneMult  = 1;
        updateWait = System.currentTimeMillis()/1000;
        createInteractionManager();
        createEnvironmentManager();
        createAnimalManager();
        setPlayer();
        
    }
    
    private void createInteractionManager() {
        wagIntMan  = new WagonInteractionManager(app.getStateManager());
    }
    
    private void createAnimalManager() {
        anMan = new AnimalManager(app);
    }
    
    private void createEnvironmentManager() {
        envMan = new EnvironmentManager(app.getStateManager());
    }
    
    private void setPlayer() {
        player = app.getStateManager().getState(PlayerManager.class).getPlayer();
    }    
    
    public void initScene() {
        
        scene            = (Node) app.getAssetManager().loadModel("Scenes/Terrain.j3o");
        interactableNode = (Node) scene.getChild("Interactable");
        interactableNode.attachChild(player.getWagon().getModel());
        interactableNode.getChild("Wagon").setLocalTranslation(-64,0,0);
        interactableNode.getChild("Wagon").rotate(0,89.5f,0);
        envMan.setInitialFill(true);
        app.getRootNode().attachChild(envMan.getEnvNode());
        app.getRootNode().attachChild(anMan.getAnimalNode());
        scene.scale(sceneMult);
        app.getRootNode().attachChild(scene);
        player.setInWagon(false);
        player.getHud().endHunt();
        player.getPhys().warp(new Vector3f(-60,0,0));
        wagIntMan.setScene(scene);
        animateWagon(app.getStateManager());
        initInteractableWagon();
        player.saveAll();
        player.getHud().getAimButton().show();
        ((WagonModel) interactableNode.getChild(0)).checkOxen();
        
        if(!scaledGrass)
            scaleGrassTexture();
    
    }
    
    private void scaleGrassTexture() {
        
        scaledGrass = true;
        Node sn     = ((Node) scene.getChild("Scene Node"));
        Node t1     = (Node)  sn.getChild("t1");
        Geometry g1 = (Geometry) ((Node)((Node)t1.getChild(0)).getChild(0)).getChild(0);
        Node t2     = (Node)  sn.getChild("t2");
        Geometry g2 = (Geometry) ((Node)((Node)t2.getChild(0)).getChild(0)).getChild(0);
        g1.getMesh().scaleTextureCoordinates(new Vector2f(10,10));
        g2.getMesh().scaleTextureCoordinates(new Vector2f(10,10));
        
    }
    
    public Node getInteractableNode() {
        return interactableNode;
    }
    
    private void initInteractableWagon() {
        Node a        = (Node) interactableNode.getChild(0);
        WagonModel wm = new WagonModel(app.getStateManager());
        wm.setName("Wagon");
        wm.setLocalTranslation(a.getWorldTranslation());
        wm.attachChild(a);
        interactableNode.attachChild(wm);
        a.setLocalTranslation(0,0,0);
    }
    
    private void animateWagon(AppStateManager stateManager) {
        
        SkeletonFinder sf = stateManager.getState(GameManager.class).getUtilityManager().getSkeletonFinder();
        
        Node wm      = (Node) player.getWagon().getModel();
        Node ox1     = (Node) wm.getChild("RightCow");
        Node ox2     = (Node) wm.getChild("LeftCow");
        
        sf.findAnimControl(ox1).clearChannels();
        sf.findAnimControl(ox2).clearChannels();
        
        if(wagonized) {
            sf.findAnimControl(ox1).createChannel().setAnim("Walk");
            sf.findAnimControl(ox2).createChannel().setAnim("Walk");
            sf.findAnimControl(ox1).getChannel(0).setSpeed(5);
            sf.findAnimControl(ox2).getChannel(0).setSpeed(5);
        }
        
        else {
            sf.findAnimControl(ox1).createChannel().setAnim("@Moo");
            sf.findAnimControl(ox2).createChannel().setAnim("LookL");        
            sf.findAnimControl(ox1).getChannel(0).setSpeed(2);
            sf.findAnimControl(ox2).getChannel(0).setSpeed(2);            
        }
        
    }
    
    public Node getScene() {
        return scene;
    }
    
    public WagonInteractionManager getWagonInteractionManager() {
        return wagIntMan;
    }
    
    private void wagonMove(float tpf) {
    
        Node sn = ((Node) scene.getChild("Scene Node"));
        Node t1 = (Node)  sn.getChild("t1");
        Node t2 = (Node)  sn.getChild("t2");
        int  ms = player.getWagonSpeed();
        
        t1.move(-ms*tpf,0,wagIntMan.getTurnValue()*tpf);
        t2.move(-ms*tpf,0,wagIntMan.getTurnValue()*tpf);
        
        for(int i = 0; i < anMan.getAnimalNode().getQuantity(); i++) {
        
            anMan.getAnimalNode().getChild(i).move(-ms*tpf,0,wagIntMan.getTurnValue()*tpf);
            
        }
        
        if (t1.getLocalTranslation().x <= -128 ) {
            t1.setLocalTranslation(128,0,0);
        }
        
        if (t2.getLocalTranslation().x <= -128 ) {
            t2.setLocalTranslation(128,0,0);
        }
        
        float distance = t2.getLocalTranslation().x - t1.getLocalTranslation().x;
        
        if (distance < 0) {
            
            distance         = distance*-1;
            float correction = 128 - distance;
            t2.move(-correction,0,0);
        }
        
        else {
            
          float correction = 128 - distance;
          t1.move(-correction,0,0);
          
        }        
        
    }
    
    private void checkProximity(Interactable actor) {
        
        if (actor.getLocalTranslation().distance(player.getLocalTranslation()) < 2.5f) {
            
            actor.whileInProx();
            
            if(!actor.inProx()) 
            actor.enterProximity(); 
            
        }
        
        else {
        
            if(actor.inProx())
            actor.exitProximity();
            
        }
        
    }
    
    private void wagonizePlayer() {
        player.setInWagon(true);
        wagonized  = true;
        Spatial a  = ((Node) interactableNode.getChild("Wagon")).getChild("Seat");
        updateWait = System.currentTimeMillis()/1000;
        player.getChaseControl().getCameraManager().getChaseCam().setEnabled(false);
        app.getCamera().setLocation(a.getWorldTranslation());
        app.getCamera().lookAtDirection(new Vector3f(500,0,0), new Vector3f(0,1,0));
        animateWagon(app.getStateManager());
        player.getHud().getInfoText().getButtonOk().hide();
        player.setLocalScale(.1f);
    }
    
    private void dewagonizePlayer() {
        player.setInWagon(false);
        wagonized = false;
        player.getChaseControl().getCameraManager().getChaseCam().setEnabled(true);
        player.getChaseControl().getCameraManager().getChaseCam().setDragToRotate(false);
        animateWagon(app.getStateManager());
        player.getHud().getInfoText().getButtonOk().show();
        player.getHud().getInfoText().hide();
        player.setLocalScale(1f);
    }
    
    private void showSituation() {

        int milesTraveled = (Integer) player.getSituation().get("Total Distance");
        int goalCount     = (Integer) player.getSituation().get("Goals Reached");
                
        HashMap goals     = (HashMap) app.getAssetManager().loadAsset("Yaml/Goals.yml");
        HashMap goalMap   = (HashMap) goals.get(goalCount+1);
        int     hay       = (Integer) player.getInventory().get("Hay");
        int     food      = (Integer) player.getInventory().get("Food");    
       
        String goalName   = (String) goalMap.get("Name");
        int    goalDist   = ((Integer) goalMap.get("At")) - milesTraveled;
        String foodInfo   = "Current Food: " + food + " pounds" + System.getProperty("line.separator");
        String hayInfo    = "Current Hay: " + hay + " pounds" + System.getProperty("line.separator");
                
        String info       = "Goal: " + goalName + System.getProperty("line.separator")
                            + "Distance Remaining: " + goalDist + System.getProperty("line.separator")
                                + foodInfo
                                    + hayInfo;
                
        player.getHud().showAlert("Situation", info);
         
    }
    
    private void updateSituation() {
    
        if (System.currentTimeMillis()/1000 - updateWait > 5) {
        
            int speedMod      = 1;
            
            if (player.getInventory().get("Oxen").equals(1))
                speedMod          = 2;
            
            updateWait        = System.currentTimeMillis()/1000;
            int     newMiles  = (Integer) player.getSituation().get("Total Distance")+randInt(4/speedMod,8/speedMod);
            int     newHay    = (Integer) player.getInventory().get("Hay")-randInt(1,3);
            int     newFood   = (Integer) player.getInventory().get("Food")-randInt(1,3); 
            HashMap goals     = (HashMap) app.getAssetManager().loadAsset("Yaml/Goals.yml");
            int     goalCount = (Integer) player.getSituation().get("Goals Reached");
            HashMap goalMap   = (HashMap) goals.get(goalCount+1);
            int     goalDist  = ((Integer) goalMap.get("At")) - newMiles;
            int     newDay    = (Integer) player.getSituation().get("Day Number")+randInt(0,1);
            
            player.getSituation().put("Total Distance", newMiles);
            player.getSituation().put("Day Number", newDay);
            
            player.getInventory().put("Hay", newHay);
            player.getInventory().put("Food", newFood);
            player.saveAll();
            
            if(newHay <= 0) {
                
                player.getInventory().put("Hay", 0);
                
                if(randInt(1,5) == 5) {
                    killOx();
                    return;
                }
                
            }
            
            if(newFood <= 0){
                
                player.getInventory().put("Food", 0);
                
                if(randInt(1,5) == 5){
                    killPlayer("Starvation");
                    return;
                }
                
            }    
            
            if (goalDist <= 0) {
                
                reachGoal();
                return;
                
            }
            
            showSituation(); 
            
        }
        
    }
    
    public void killPlayer(String reason) {
        dewagonizePlayer();
        WagonModel wm = (WagonModel) interactableNode.getChild("Wagon");
        player.getHud().endHunt();
        envMan.getEnvNode().detachAllChildren();
        anMan.getAnimalNode().detachAllChildren();
        ((WagonGui) wm.getGui()).getStopButton().hide();
        player.die(reason);
    }
    
    private void killOx() {
        
       int newOxCount = ((Integer)player.getInventory().get("Oxen")) - 1;
       int meatWeight = randInt(30,76);
       int newFood    = ((Integer)player.getInventory().get("Food")) + meatWeight;
       String oxWarn  = "";
       
       player.getInventory().put("Oxen", newOxCount);
       player.getInventory().put("Food", newFood);
       
       if(newOxCount == 1) {
           oxWarn = "You are down to one ox, your speed is cut in half.";
           ((WagonModel)interactableNode.getChild("Wagon")).checkOxen();
       }
       
       if(newOxCount == 0){
           killPlayer("Stranded");
           //((WagonModel)interactableNode.getChild("Wagon")).checkOxen();
           return;
       }
       
       player.getHud().showAlert("Death", "One of your Oxen has died. You find " + meatWeight + " pounds of usable meat. " + oxWarn);
       
    }
    
    private void reachGoal() {
        
        WagonModel currentActor = (WagonModel) interactableNode.getChild("Wagon");
        int goalCount           = (Integer) player.getSituation().get("Goals Reached")+1;
        HashMap goals           = (HashMap) app.getAssetManager().loadAsset("Yaml/Goals.yml");
        HashMap goalMap         = (HashMap) goals.get(goalCount);
        String  goalName        = (String)  goalMap.get("Name");
        String goalType         = (String)  goalMap.get("Type");
        

        
        if (goalName.equals("Oregon")) {
            player.finishGame();
            return;
        }

        System.out.println(currentActor);
        ((WagonGui) currentActor.getGui()).getStopButton().hide();
        player.getSituation().put("Setting Name", goalName);
        player.getSituation().put("Goals Reached", goalCount);
        player.getSituation().put("Setting", goalType);
        player.setLocalScale(1);
        player.saveInventory();
        player.saveSituation();
        app.getStateManager().getState(GameManager.class).loadSituation();
        player.getHud().getInfoText().getButtonOk().show();
        player.getHud().getLeftStick().show();
        player.getChaseControl().getCameraManager().getChaseCam().setEnabled(true);
        dewagonizePlayer();
        
    }
    
    private int randInt(int min, int max) {
        Random rand   = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }        
    
    public void clearTrail(AppStateManager stateManager) {
        stateManager.getState(GameManager.class).getUtilityManager().getPhysicsManager().clearPhysics(stateManager, scene);
        interactableNode = null;
        scene            = null;
        interactableNode = new Node();
        scene            = new Node();
        anMan.clear();
        envMan.clear();
    }
    
    public AnimalManager getAnimalManager() {
        return anMan;
    }
    
    public void update(float tpf) {
    
        if (player.getInWagon()) {
            
            if(!wagonized)
                wagonizePlayer();
            
            wagIntMan.update(tpf);
            wagonMove(tpf);
            updateSituation();
            
        }
        
        else {
            
            if(wagonized)
                dewagonizePlayer();
            
            player.getChaseControl().update(tpf);
            
        }
        
        for (int i = 0; i < interactableNode.getQuantity(); i++) {
            
            Interactable currentActor =  (Interactable) interactableNode.getChild(i);
            checkProximity(currentActor);
            
        }        
        
        envMan.update(tpf);
        anMan.update(tpf);
        
    }
    
}
