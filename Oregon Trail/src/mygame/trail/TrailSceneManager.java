/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.trail;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.GameManager;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.town.WagonModel;
import mygame.util.Interactable;
import mygame.util.PersonInteractionManager;
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
    private PersonInteractionManager persIntMan;
    private Node                     interactableNode;
    private EnvironmentManager       envMan;
    private boolean                  wagonized;
    
    public TrailSceneManager(SimpleApplication app) {
    
        this.app  = (SimpleApplication) app;
        sceneMult = 1;
        createInteractionManager();
        createEnvironmentManager();
        setPlayer();
        
    }
    
    private void createInteractionManager() {
        wagIntMan  = new WagonInteractionManager(app.getStateManager());
        persIntMan = new  PersonInteractionManager(app.getStateManager());
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
        app.getRootNode().attachChild(envMan.getEnvNode());
        scene.scale(sceneMult);
        app.getRootNode().attachChild(scene);
        player.setInWagon(false);
        player.getPhys().warp(new Vector3f(-60,0,0));
        wagIntMan.setScene(scene);
        animateWagon(app.getStateManager());
        initInteractableWagon();
    
    }
    
    private void initInteractableWagon() {
        Node a        = (Node) interactableNode.getChild(0);
        WagonModel wm = new WagonModel(app.getStateManager());
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
        int  ms = player.getWagon().getMoveSpeed();
        
        t1.move(-ms*tpf,0,wagIntMan.getTurnValue()*tpf);
        t2.move(-ms*tpf,0,wagIntMan.getTurnValue()*tpf);
        
        if (t1.getLocalTranslation().x <= -128 ) {
            t1.setLocalTranslation(128,0,0);
        }
        
        if (t2.getLocalTranslation().x <= -128 ) {
            t2.setLocalTranslation(128,0,0);
        }
        
        float distance = t2.getLocalTranslation().x - t1.getLocalTranslation().x;
        
        if (distance < 0) {
            
            distance = distance*-1;
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
        wagonized = true;
        Spatial a = ((Node) interactableNode.getChild("Wagon")).getChild("Seat");
        app.getCamera().setLocation(a.getWorldTranslation());
        app.getCamera().lookAtDirection(new Vector3f(500,0,0), new Vector3f(0,1,0));
        animateWagon(app.getStateManager());
    }
    
    private void dewagonizePlayer() {
        wagonized = false;
        animateWagon(app.getStateManager());
    }
    
    public void update(float tpf) {
    
        if (player.getInWagon()) {
            wagIntMan.update(tpf);
            wagonMove(tpf);
            if(!wagonized)
            wagonizePlayer();
        }
        
        else {
            persIntMan.update(tpf);
            app.getStateManager().getState(GameManager.class).getUtilityManager().getCameraManager().update(tpf);
            if(wagonized)
            dewagonizePlayer();
        }
        
        for (int i = 0; i < interactableNode.getQuantity(); i++) {
            
            Interactable currentActor =  (Interactable) interactableNode.getChild(i);
            checkProximity(currentActor);
            
        }        
        
        envMan.update(tpf);
        
    }
    
}
