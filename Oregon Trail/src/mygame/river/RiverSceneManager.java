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
    
    public void update(float tpf) {
        player.getChaseControl().update(tpf);
    }    
    
}
