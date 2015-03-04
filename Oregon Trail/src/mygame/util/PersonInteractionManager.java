/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import mygame.GameManager;
import mygame.player.Player;
import mygame.player.PlayerManager;

/**
 *
 * @author Bawb
 */
public class PersonInteractionManager {
    
    private boolean           left, right, up, down;
    private AppStateManager   stateManager;
    private Player            player;
    private SimpleApplication app;
    private Vector3f          camDir = new Vector3f(), camLeft = new Vector3f(), walkDirection = new Vector3f();
    
    public PersonInteractionManager(AppStateManager stateManager) {
        this.stateManager = stateManager;
        player            = stateManager.getState(PlayerManager.class).getPlayer();
        app               = (SimpleApplication) stateManager.getApplication();
    } 
    
    private void updateKeys() {
        InteractionManager im = stateManager.getState(GameManager.class).getUtilityManager().getInteractionManager();
        up    = im.getIsPressed("Up");
        down  = im.getIsPressed("Down");
        left  = im.getIsPressed("Left");
        right = im.getIsPressed("Right");
    }
    
    private void chaseMove(){
        
        if(player.getNoMove())
        return;
        
        camDir.set(app.getCamera().getDirection()).multLocal(10.0f, 0.0f, 10.0f);
        camLeft.set(app.getCamera().getLeft()).multLocal(10.0f);
        walkDirection.set(0, 0, 0);
        
        if (up) {
            walkDirection.addLocal(camDir);
            player.run();
        }
        else if (down) {
            walkDirection.addLocal(camDir.negate());
            player.run();
        }
        if (left) {
            walkDirection.addLocal(camLeft);
            player.run();
        }
        else if (right) {
            walkDirection.addLocal(camLeft.negate());
            player.run();
        }
        else if (!up && !down) {
            player.idle();
        }
        
        float speedMult;
        speedMult = player.getSpeedMult()*.8f + player.getStrafeMult()*.2f;
        player.getPhys().setWalkDirection(walkDirection.mult(.5f));
        if (!up && !down && !left && !right)
        player.getPhys().setViewDirection(camDir);
        else
        player.getPhys().setViewDirection(walkDirection);
        
    }
    
    public void update(float tpf) {
        chaseMove();
        updateKeys();
    }
    
}
