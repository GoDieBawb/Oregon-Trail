/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.river;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.player.wagon.WagonModel;
import mygame.util.Interactable;

/**
 *
 * @author Bawb
 */
public class RiverSceneManager {
    
    private Node scene;
    private Node interactableNode;
    private Player player;
    
    public void initScene(SimpleApplication app) {
        scene            = (Node) app.getAssetManager().loadModel("Scenes/River.j3o");
        interactableNode = (Node) scene.getChild("Interactable");
        Node wagonSpot   = (Node) interactableNode.getChild("Wagon");
        player           = app.getStateManager().getState(PlayerManager.class).getPlayer();
        app.getRootNode().attachChild(scene);
        wagonSpot.attachChild(player.getWagon().getModel());
        initInteractables(app);
        animateScene(app.getStateManager());
        player.getPhys().warp(scene.getChild("PlayerSpot").getLocalTranslation());
        informPlayer();
        player.getWagon().getModel().setLocalRotation(new Quaternion(0,0,0,1));
    }
    
    public Node getScene() {
        return scene;
    }
    
    private void initInteractables(SimpleApplication app) {
        
        Node wagonNode       = (Node) interactableNode.getChild("Wagon");
        Node ferryManNode    = (Node) interactableNode.getChild("Ferryman");
        Node indianGuideNode = (Node) interactableNode.getChild("Indian Guide");
        
        WagonModel  wm       = new WagonModel(app.getStateManager());
        Ferryman    fm       = new Ferryman(app.getStateManager());
        IndianGuide ig       = new IndianGuide(app.getStateManager());

        wm.setLocalTranslation(wagonNode.getWorldTranslation());
        fm.setLocalTranslation(ferryManNode.getLocalTranslation());
        ig.setLocalTranslation(indianGuideNode.getLocalTranslation());
        
        wm.attachChild(wagonNode);
        fm.attachChild(ferryManNode);
        ig.attachChild(indianGuideNode);
        
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
    
    public void update(float tpf) {
    
        player.getChaseControl().update(tpf);
        
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
