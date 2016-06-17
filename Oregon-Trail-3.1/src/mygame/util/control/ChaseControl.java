/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.control;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import mygame.GameManager;
import mygame.player.Player;
import mygame.util.InteractionManager;

/**
 *
 * @author Bawb
 */
public class ChaseControl extends InteractionControl {
    
    private boolean            left, right, up, down;
    private AppStateManager    stateManager;
    private Player             player;
    private SimpleApplication  app;
    private Vector3f           camDir = new Vector3f(), camLeft = new Vector3f(), walkDirection = new Vector3f();
    private ChaseCameraManager cameraManager;
    private boolean            enabled;
    
    public ChaseControl(AppStateManager stateManager, Player player) {
        this.stateManager = stateManager;
        this.player       = player;
        app               = (SimpleApplication) stateManager.getApplication();
        createCameraManager();
    } 
    
    private void createCameraManager() {
        cameraManager = new ChaseCameraManager(app, player);
    }
    
    private void updateKeys() {
        InteractionManager im = stateManager.getState(GameManager.class).getUtilityManager().getInteractionManager();
        up    = im.getIsPressed("Up");
        down  = im.getIsPressed("Down");
        left  = im.getIsPressed("Left");
        right = im.getIsPressed("Right");
    }
    
    private void chaseMove(float tpf){
        camDir.set(app.getCamera().getDirection()).multLocal(10.0f, 0.0f, 10.0f);
        camLeft.set(app.getCamera().getLeft()).multLocal(10.0f);
        walkDirection.set(0, 0, 0);
        
        if(player.getNoMove()) {
            return;
        }
        
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
        speedMult = player.getSpeedMult()*.3f;
        
        player.getPhys().setWalkDirection(walkDirection.mult(speedMult));
        
        if (!up && !down && !left && !right)
            player.getPhys().setViewDirection(camDir);
        
        else
            player.getPhys().setViewDirection(walkDirection);
        
    }
    
    public ChaseCameraManager getCameraManager() {
        return cameraManager;
    }
    
    @Override
    public void setEnabled(boolean newVal) {
        super.setEnabled(newVal);
        cameraManager.setEnabled(newVal);
    }
    
    @Override
    public void update(float tpf) {
        chaseMove(tpf);
        updateKeys();
        cameraManager.update(tpf);
    }
    
}
