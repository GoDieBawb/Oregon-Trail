/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.trail;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import mygame.player.Player;
import mygame.player.PlayerManager;

/**
 *
 * @author Bawb
 */
public class TrailSceneManager extends AbstractAppState {
    
    private SimpleApplication       app;
    private Node                    scene;
    private float                   sceneMult;
    private Player                  player;
    private TrailInteractionManager trailIntMan;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
    
        this.app          = (SimpleApplication) app;
        sceneMult         = 3;
        createInteractionManager();
        initScene();
        setPlayer();
        
    }
    
    private void createInteractionManager() {
        app.getStateManager().attach(new TrailInteractionManager());
    }
    
    private void setPlayer() {
        player = app.getStateManager().getState(PlayerManager.class).getPlayer();
    }    
    
    private void initScene() {
    
        scene               = (Node) app.getAssetManager().loadModel("Scenes/Terrain.j3o");
        scene.scale(sceneMult);
        //make unshadedhere
    
    }
    
    @Override
    public void update(float tpf) {
    
        Node sn = ((Node) scene.getChild("Scene Node"));
        Node t1 = (Node)  sn.getChild("t1");
        Node t2 = (Node)  sn.getChild("t2");
        int  ms = player.getWagon().getMoveSpeed();
        
        t1.move(-ms*tpf,0,trailIntMan.getTurnValue()*tpf);
        t2.move(-ms*tpf,0,trailIntMan.getTurnValue()*tpf);
        
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
    
}
