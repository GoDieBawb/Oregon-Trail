/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import mygame.player.wagon.WagonModel;
import mygame.util.Interactable;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.util.SkeletonFinder;

/**
 *
 * @author Bawb
 */
public class TownSceneManager {
    
    private Node   scene;
    private Node   interactableNode;
    private Player player;
    
    public void initScene(SimpleApplication app) {
        scene            = (Node) app.getAssetManager().loadModel("Scenes/Town.j3o");
        interactableNode = (Node) scene.getChild("Interactable");
        Node wagonSpot   = (Node) interactableNode.getChild("Wagon");
        player           = app.getStateManager().getState(PlayerManager.class).getPlayer();
        app.getRootNode().attachChild(scene);
        wagonSpot.attachChild(player.getWagon().getModel());
        initInteractables(app);
        animateTown(app.getStateManager());
        player.getPhys().warp(scene.getChild("PlayerSpot").getLocalTranslation());
        informPlayer();
        player.getWagon().getModel().setLocalRotation(new Quaternion(0,0,0,1));
    }
    
    public void clearTown(AppStateManager stateManager) {
        stateManager.getState(GameManager.class).getUtilityManager().getPhysicsManager().clearPhysics(stateManager, scene);
        scene            = null;
        interactableNode = null;
        scene            = new Node();
        interactableNode = new Node();
    }
    
    public Node getInteractableNode() {
        return interactableNode;
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
    
    private void animateTown(AppStateManager stateManager) {
        
        SkeletonFinder sf = stateManager.getState(GameManager.class).getUtilityManager().getSkeletonFinder();
        
        Node intNode   = (Node) scene.getChild("Interactable");
        Node oxNode    = (Node) scene.getChild("Oxen");
        Node partyNode = (Node) scene.getChild("PartyNode");
        
        Node sk  = (Node) intNode.getChild("ShopKeeper");
        Node bs  = (Node) intNode.getChild("BlackSmith");
        Node wm  = (Node) ((Node) intNode.getChild("Wagon")).getChild(0);
        Node fm  = (Node) intNode.getChild("Farmer");
        
        Node mm  = (Node) partyNode.getChild("Mother");
        Node sn  = (Node) partyNode.getChild("Son");
        Node dt  = (Node) partyNode.getChild("Daughter");
        
        Node ox1 = (Node) wm.getChild("RightCow");
        Node ox2 = (Node) wm.getChild("LeftCow");
        
        Node ox3 = (Node) oxNode.getChild(0);
        Node ox4 = (Node) oxNode.getChild(1);

        sf.findAnimControl(sk).createChannel().setAnim("ArmIdle");
        sf.findAnimControl(bs).createChannel().setAnim("ArmIdle");
        sf.findAnimControl(fm).createChannel().setAnim("ArmIdle");
        
        sf.findAnimControl(mm).createChannel().setAnim("ArmIdle");
        sf.findAnimControl(sn).createChannel().setAnim("ArmIdle");
        sf.findAnimControl(dt).createChannel().setAnim("ArmIdle");
        
        sf.findAnimControl(ox1).clearChannels();
        sf.findAnimControl(ox2).clearChannels();
        
        sf.findAnimControl(ox1).createChannel().setAnim("@Moo");
        sf.findAnimControl(ox2).createChannel().setAnim("LookL");
        
        sf.findAnimControl(ox3).createChannel().setAnim("Sit");
        sf.findAnimControl(ox4).createChannel().setAnim("EatLow");
        
    }
    
    private void initInteractables(SimpleApplication app) {
        
        Node a = (Node) interactableNode.getChild(0);
        Node b = (Node) interactableNode.getChild(1);
        Node c = (Node) interactableNode.getChild(2);
        Node d = (Node) interactableNode.getChild(3);
        
        ShopKeeper sk = new ShopKeeper(app.getStateManager());
        Blacksmith bs = new Blacksmith(app.getStateManager());
        WagonModel wm = new WagonModel(app.getStateManager());
        Farmer     fm = new Farmer(app.getStateManager());
        
        a.setMaterial(app.getAssetManager().loadMaterial("Materials/Shopkeeper.j3m"));
        b.setMaterial(app.getAssetManager().loadMaterial("Materials/Blacksmith.j3m"));
        d.setMaterial(app.getAssetManager().loadMaterial("Materials/Farmer.j3m"));
        
        sk.setLocalTranslation(a.getWorldTranslation());
        bs.setLocalTranslation(b.getWorldTranslation());
        wm.setLocalTranslation(c.getWorldTranslation());
        fm.setLocalTranslation(d.getWorldTranslation());
        
        sk.attachChild(a);
        bs.attachChild(b);
        wm.attachChild(c);
        fm.attachChild(d);
        
        interactableNode.attachChild(sk);
        interactableNode.attachChild(bs);
        interactableNode.attachChild(wm);
        interactableNode.attachChild(fm);
        
        a.setLocalTranslation(0,0,0);
        b.setLocalTranslation(0,0,0);
        c.setLocalTranslation(0,0,0);
        d.setLocalTranslation(0,0,0);
        
        wm.checkOxen();
        
    }
    
    public Node getScene() {
        return scene;
    }
    
    private void checkProximity(Interactable actor) {

        if (actor.getLocalTranslation().distance(player.getLocalTranslation()) < actor.getProxDistance()) {
            
            actor.whileInProx();
            
            if(!actor.inProx()) 
            actor.enterProximity(); 
            
        }
        
        else {
        
            if(actor.inProx())
            actor.exitProximity();
            
        }
        
    }
    
    public void update(float tpf) {
    
        player.getChaseControl().update(tpf);
        
        for (int i = 0; i < interactableNode.getQuantity(); i++) {
            
            Interactable currentActor =  (Interactable) interactableNode.getChild(i);
            checkProximity(currentActor);
            
        }
        
    }
    
}
