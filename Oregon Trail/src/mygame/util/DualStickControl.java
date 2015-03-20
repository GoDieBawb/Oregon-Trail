/*
 * this is a comment
 * more and more
 * more and more
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
public class DualStickControl extends InteractionControl {
    
    private boolean            left, right, up, down,left1, right1, up1, down1;
    private AppStateManager    stateManager;
    private Player             player;
    private SimpleApplication  app;
    private Vector3f           camDir = new Vector3f(), camLeft = new Vector3f(), walkDirection = new Vector3f();
    private DualJoystickCameraManager cameraManager;
    
    public DualStickControl(AppStateManager stateManager) {
        this.stateManager = stateManager;
        player            = stateManager.getState(PlayerManager.class).getPlayer();
        app               = (SimpleApplication) stateManager.getApplication();
        createCameraManager();
    }
    
    private void createCameraManager() {
        cameraManager = new DualJoystickCameraManager(app);
    }
    
    private void updateKeys() {
        InteractionManager im = stateManager.getState(GameManager.class).getUtilityManager().getInteractionManager();
        up    = im.getIsPressed("Up");
        down  = im.getIsPressed("Down");
        left  = im.getIsPressed("Left");
        right = im.getIsPressed("Right");
        up1    = im.getIsPressed("Up1");
        down1  = im.getIsPressed("Down1");
        left1  = im.getIsPressed("Left1");
        right1 = im.getIsPressed("Right1");
    }
    
    private void chaseMove(float tpf){
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
        speedMult = player.getSpeedMult()/2f;
        
        player.getPhys().setWalkDirection(walkDirection.mult(speedMult));
        
        if(walkDirection.x < 0) {
            
            if(Math.abs(walkDirection.x) > 8)
                walkDirection.x = -8;
            
        }
        
        else {
            
            if(Math.abs(walkDirection.x) > 8)
                walkDirection.x = 8;
            
        }
        
        if(walkDirection.z < 0) {
            
            if(Math.abs(walkDirection.z) > 8)
                walkDirection.z = -8;
            
        }
        
        else {
            
            if(Math.abs(walkDirection.z) > 8)
                walkDirection.z = 8;
            
        }
        
        //System.out.println(walkDirection);
        
        if (!up && !down && !left && !right)
        player.getPhys().setViewDirection(camDir);
        else
        player.getPhys().setViewDirection(walkDirection);
        
    }
    
    private void updateLookHeight(float tpf) {
        
        float lookSpeed = 1.5f*tpf;
        
        if(up1) {
            if(player.getLookHeight() < 1f)
            player.setLookHeight(player.getLookHeight()+lookSpeed);
        }
        
        else if(down1) {
            
            if(player.getLookHeight() > -1f)
            player.setLookHeight(player.getLookHeight()-lookSpeed);
        }
        
        else {
        
            if (player.getLookHeight() < 0) {
                
                player.setLookHeight(player.getLookHeight()+lookSpeed);
                
            }
            
            else if (player.getLookHeight() > 0){
                
                player.setLookHeight(player.getLookHeight()-lookSpeed);
                
            }
            
        }
        
    }
    
    public DualJoystickCameraManager getCameraManager() {
        return cameraManager;
    }
    
    @Override
    public void update(float tpf) {
        chaseMove(tpf);
        updateKeys();
        updateLookHeight(tpf);
        cameraManager.update(tpf);
    }
    
}
