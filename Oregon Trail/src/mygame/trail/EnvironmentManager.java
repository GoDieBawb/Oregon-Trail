/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.trail;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import java.util.Random;
import mygame.GameManager;
import mygame.player.PlayerManager;

/**
 *
 * @author Bawb
 */
public class EnvironmentManager {
    
    private Node              envNode;
    private AppStateManager   stateManager;
    private SimpleApplication app;
    
    public EnvironmentManager(AppStateManager stateManager) {
        this.stateManager = stateManager;
        app               = (SimpleApplication) stateManager.getApplication();
        envNode           = new Node();
    }
    
    public Node getEnvNode() {
        return envNode;
    }
    
    private void createObject() {
        Node tree = (Node) app.getAssetManager().loadModel("Models/Plants/Bush/Bush.j3o");
        tree.scale(.5f);
        envNode.attachChild(tree);   
        placeObject(tree);
        stateManager.getState(GameManager.class).getUtilityManager().getMaterialManager().makeUnshaded(tree);
    }
    
    private void placeObject(Node object) {
        
        int xSpot = randInt(1,96);
        int zSpot = randInt(1,64);
        int negChance = randInt(1,2);
        int negChance1 = randInt(1,2);
        int negMult1 = 1;
        int negMult  = 1;
        
        if (negChance1 == 1)
        negMult1 = -1;
        
        if (negChance == 1)
        negMult = -1;
        
        object.setLocalTranslation(xSpot*negMult, 4f , zSpot*negMult1);
        object.rotate(0, 0, randInt(0,180));
        stateManager.getState(GameManager.class).getUtilityManager().getPhysicsManager().addToPhysics(object);
        
    }
    
    private int randInt(int min, int max) {
        Random rand   = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }       
    
    private void envMove(float tpf) {
        int  ms = stateManager.getState(PlayerManager.class).getPlayer().getWagonSpeed();
        WagonInteractionManager wagIntMan = stateManager.getState(TrailState.class).getTrailSceneManager().getWagonInteractionManager();
        for (int i = 0; i< envNode.getQuantity(); i++) {
            envNode.getChild(i).move(-ms*tpf,0,wagIntMan.getTurnValue()*tpf);
        }
        
    }
    
    public void update(float tpf) {
        
        if (stateManager.getState(PlayerManager.class).getPlayer().getInWagon())
        envMove(tpf);    
        
        if (envNode.getQuantity() < 40) {
            createObject();
        }
        
        for (int i = 0; i < envNode.getQuantity(); i++) {
            if (envNode.getChild(i).getLocalTranslation().x <= -128)
            envNode.getChild(i).removeFromParent();
        }
        
    }
    
}
