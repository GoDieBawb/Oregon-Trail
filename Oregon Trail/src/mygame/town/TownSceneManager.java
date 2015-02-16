/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import mygame.player.Player;
import mygame.player.PlayerManager;

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
    }
    
    private void initInteractables(SimpleApplication app) {
        
        Node a = (Node) interactableNode.getChild(0);
        Node b = (Node) interactableNode.getChild(1);
        Node c = (Node) interactableNode.getChild(2);
        
        ShopKeeper sk = new ShopKeeper(app.getStateManager());
        Blacksmith bs = new Blacksmith(app.getStateManager());
        WagonModel wm = new WagonModel(app.getStateManager());
        
        sk.setLocalTranslation(a.getWorldTranslation());
        bs.setLocalTranslation(b.getWorldTranslation());
        wm.setLocalTranslation(c.getWorldTranslation());
        
        sk.attachChild(a);
        bs.attachChild(b);
        wm.attachChild(c);
        
        interactableNode.attachChild(sk);
        interactableNode.attachChild(bs);
        interactableNode.attachChild(wm);
        
        a.setLocalTranslation(0,0,0);
        b.setLocalTranslation(0,0,0);
        c.setLocalTranslation(0,0,0);
        
        
    }
    
    public Node getScene() {
        return scene;
    }
    
    private void checkProximity(Interactable actor) {

        if (actor.getLocalTranslation().distance(player.getLocalTranslation()) < 2.5f) {
            
            if(!actor.inProx()) 
            actor.enterProximity();
            
        }
        
        else {
        
            if(actor.inProx())
            actor.exitProximity();
            
        }
        
    }
    
    public void update(float tpf) {
    
        for (int i = 0; i < interactableNode.getQuantity(); i++) {
            
            Interactable currentActor =  (Interactable) interactableNode.getChild(i);
            checkProximity(currentActor);
            
        }
        
    }
    
}
