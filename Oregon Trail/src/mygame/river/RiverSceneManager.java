/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.river;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Random;
import mygame.GameManager;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.player.wagon.WagonModel;
import mygame.util.Interactable;
import mygame.util.SkeletonFinder;

/**
 *
 * @author Bawb
 */
public class RiverSceneManager {
    
    private Node    scene;
    private Node    interactableNode;
    private Player  player;
    private boolean wagonized;
    private boolean isCrossing;
    private Long    lastCrash;
    private SimpleApplication   app;
    
    public void initScene(SimpleApplication app) {
        scene            = (Node) app.getAssetManager().loadModel("Scenes/River.j3o");
        interactableNode = (Node) scene.getChild("Interactable");
        Node wagonSpot   = (Node) interactableNode.getChild("Wagon");
        player           = app.getStateManager().getState(PlayerManager.class).getPlayer();
        this.app         = app;
        isCrossing       = false;
        app.getRootNode().attachChild(scene);
        wagonSpot.attachChild(player.getWagon().getModel());
        initInteractables(app);
        animateScene(app.getStateManager());
        player.getPhys().warp(scene.getChild("PlayerSpot").getLocalTranslation());
        informPlayer();
        player.getWagon().getModel().setLocalRotation(new Quaternion(0,0,0,1));
        initFordControl();
    }
    
    public Node getScene() {
        return scene;
    }
    
    public Node getInteractableNode() {
        return interactableNode;
    }
    
    private void initInteractables(SimpleApplication app) {
        
        SkeletonFinder sf = app.getStateManager().getState(GameManager.class).getUtilityManager().getSkeletonFinder();
        
        Node wagonNode       = (Node) interactableNode.getChild("Wagon");
        Node ferryManNode    = (Node) interactableNode.getChild("Ferryman");
        Node indianGuideNode = (Node) interactableNode.getChild("Indian Guide");
        Node partyNode       = (Node) scene.getChild("PartyNode");
        
        WagonModel  wm       = new WagonModel(app.getStateManager());
        Ferryman    fm       = new Ferryman(app.getStateManager());
        IndianGuide ig       = new IndianGuide(app.getStateManager());
        
        Node        ox1      = (Node) wagonNode.getChild("RightCow");
        Node        ox2      = (Node) wagonNode.getChild("LeftCow");
        
        Node mm  = (Node) partyNode.getChild("Mother");
        Node sn  = (Node) partyNode.getChild("Son");
        Node dt  = (Node) partyNode.getChild("Daughter");        
        
        wm.setLocalTranslation(wagonNode.getWorldTranslation());
        fm.setLocalTranslation(ferryManNode.getLocalTranslation());
        ig.setLocalTranslation(indianGuideNode.getLocalTranslation());
        
        wm.attachChild(wagonNode);
        fm.attachChild(ferryManNode);
        ig.attachChild(indianGuideNode);
        
        sf.findAnimControl(ig).createChannel().setAnim("ArmIdle");
        sf.findAnimControl(fm).createChannel().setAnim("ArmIdle");
        
        sf.findAnimControl(mm).createChannel().setAnim("ArmIdle");
        sf.findAnimControl(sn).createChannel().setAnim("ArmIdle");
        sf.findAnimControl(dt).createChannel().setAnim("ArmIdle");        
        
        boolean  mDead = (Boolean) ((HashMap)player.getParty().getInfo().get("Wife")).get("Dead");
        boolean  sDead = (Boolean) ((HashMap)player.getParty().getInfo().get("Son")).get("Dead");
        boolean  dDead = (Boolean) ((HashMap)player.getParty().getInfo().get("Daughter")).get("Dead");
        AppStateManager stateManager = app.getStateManager();
        
        if (!mDead)
            mm.setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/Female.j3m"));
        else
            mm.setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/Ghost.j3m"));
        
        if (!dDead)
            dt.setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/Female.j3m"));
        else
            dt.setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/Ghost.j3m"));
        
        if (!sDead)
            sn.setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/FerryMan.j3m"));
        else
            sn.setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/Ghost.j3m"));        
        
        ferryManNode.setMaterial(app.getAssetManager().loadMaterial("Materials/FerryMan.j3m"));
        ferryManNode.getChild("Hat").setMaterial(app.getAssetManager().loadMaterial("Materials/Hay.j3m"));
        
        indianGuideNode.setMaterial(app.getAssetManager().loadMaterial("Materials/Indian.j3m"));
        indianGuideNode.getChild("Feather").setMaterial(app.getAssetManager().loadMaterial("Materials/Feather.j3m"));
        
        sf.findAnimControl(ox1).clearChannels();
        sf.findAnimControl(ox2).clearChannels();
        sf.findAnimControl(ox1).createChannel().setAnim("@Moo");
        sf.findAnimControl(ox2).createChannel().setAnim("LookL");
        
        interactableNode.attachChild(wm);
        interactableNode.attachChild(fm);
        interactableNode.attachChild(ig);
        
        wagonNode.setLocalTranslation(0,0,0);
        ferryManNode.setLocalTranslation(0,0,0);
        indianGuideNode.setLocalTranslation(0,0,0);
        
        RiverBoatControl rbc = new RiverBoatControl();
        ((Node)scene.getChild("Static")).getChild("Ferry").addControl(rbc);
        
        wm.checkOxen();
        
    }
    
    private void animateScene(AppStateManager stateManager) {
    }
    
    public void clearRiver(AppStateManager stateManager) {
        stateManager.getState(GameManager.class).getUtilityManager().getPhysicsManager().clearPhysics(stateManager, scene);
        scene            = null;
        interactableNode = null;
        scene            = new Node();
        interactableNode = new Node();
    }
    
    private void informPlayer() {
        
        int day           = (Integer) player.getSituation().get("Day Number");
        int milesTraveled = (Integer) player.getSituation().get("Total Distance");
        String name       = (String)  player.getSituation().get("Setting Name");
        
        String info = "Welcome to " + name + ". You have traveled a total of " + milesTraveled + " miles in " + day + " days.";
        
        if (day == 1)
            player.getHud().showAlert("Welcome", player.getHud().getScripts().get("Start").toString());
        
        else
            player.getHud().showAlert("Welcome", info);   
        
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
        player.getChaseControl().getCameraManager().getChaseCam().setDragToRotate(false);
        player.getChaseControl().getCameraManager().getChaseCam().setEnabled(false);
        app.getCamera().setLocation(a.getWorldTranslation());
        app.getCamera().lookAtDirection(new Vector3f(0,0,-500), new Vector3f(0,1,0));
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
    
    private void deferryPlayer() {
        player.getChaseControl().getCameraManager().getChaseCam().setEnabled(true);
        player.getModel().addControl(player.getChaseControl().getCameraManager().getChaseCam());
        player.getChaseControl().getCameraManager().getChaseCam().setLookAtOffset(new Vector3f(0,0,0).add(0, .5f, 0));
        player.getChaseControl().getCameraManager().getChaseCam().setDefaultDistance(3);
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
    
    private void initFordControl() {
        FordControl fordControl = new FordControl();
        player.getWagon().getModel().addControl(fordControl);
    }
    
    public void update(float tpf) {
    
        player.getChaseControl().update(tpf);
        FordControl      fc  =  player.getWagon().getModel().getControl(FordControl.class);
        RiverBoatControl rbc =  ((Node)scene.getChild("Static")).getChild("Ferry").getControl
                                    (RiverBoatControl.class);
        
        if (!isCrossing) {
            
            if (fc.isCrossing() || rbc.isCrossing()) {
                isCrossing = true;
                lastCrash = System.currentTimeMillis();
            }
            
        }
        
        else {
        
            if (fc.isCrossing() && fc.canCrash()) {
            
                int crashCooldown                = 3;
                if (fc.isIndian()) crashCooldown = 5;
                
                if (System.currentTimeMillis() / 1000 - lastCrash / 1000 > crashCooldown-1) {
                    
                    lastCrash       = System.currentTimeMillis();
                    Random rand     = new Random();
                    int crashChance = rand.nextInt((6 - 1) + 1) + 1;
                    
                    if (crashChance == 6) {
                        fc.getSpatial().setLocalTranslation(0,0,0);
                        fc.getSpatial().removeControl(fc);
                        fc.setEnabled(false);
                        player.die("Drown");
                        player.setNoMove(false);
                        dewagonizePlayer();
                        deferryPlayer();
                        return;
                    }
                    
                }
                
            }
            
            if (!fc.isCrossing() && !rbc.isCrossing()) {
                
                isCrossing = false;
                
                if (wagonized) {
                    dewagonizePlayer();
                }
                
                deferryPlayer();
                player.getSituation().put("Setting", "Trail");
                app.getStateManager().getState(GameManager.class).initTrail();
                return;
                
            }
            
        }
        
        if (player.getInWagon()) {
            
            Spatial a  = ((Node) interactableNode.getChild("Wagon")).getChild("Seat");
            app.getCamera().setLocation(a.getWorldTranslation());
            
            if(!wagonized)
                wagonizePlayer();
            
        }
        
        else {
            
            if(wagonized)
                dewagonizePlayer();
            
            player.getChaseControl().update(tpf);
            
        }
        
        for (int i = 0; i < interactableNode.getQuantity(); i++) {
            
            try {
                Interactable currentActor =  (Interactable) interactableNode.getChild(i);
                checkProximity(currentActor);
            }
            
            catch(ClassCastException e) {
            }
            
            
        }
        
    }   
    
}
