/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package mygame.util.control;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.scene.Node;
import mygame.player.Player;
import mygame.player.PlayerManager;

/**
*
* @author Bob
*/
public class ChaseCameraManager {

    private SimpleApplication app;
    private AppStateManager   stateManager;
    private Player            player;
    private ChaseCamera       cam;
    private boolean           enabled;
    private boolean           isHunt;
  
    public ChaseCameraManager(Application app, Player player){
        this.app           = (SimpleApplication) app;
        this.stateManager  = this.app.getStateManager();
        this.player        = player;
        initCamera();
    }
  
    //Creates camera
     private void initCamera(){
        //Creates a new chase cam and attached it to the player.model for the game
        cam = new ChaseCamera(this.app.getCamera(), player.getModel(), this.app.getInputManager());
        cam.setMinDistance(0.5f);
        cam.setMaxDistance(5);
        cam.setDefaultDistance(3);
        cam.setDragToRotate(true);
        cam.setRotationSpeed(5f);
        cam.setLookAtOffset(player.getLocalTranslation().add(0, .75f, 0));
        cam.setDefaultVerticalRotation(.145f);
        cam.setMaxVerticalRotation(.145f);
        cam.setMinVerticalRotation(.145f);
    }

    private void chaseCamMove() {
        
        if (cam.getDistanceToTarget() < 1){
            cam.setMinVerticalRotation(0f);
            cam.setMaxVerticalRotation(5f);
        }
        
        else {
            cam.setMaxVerticalRotation(.145f);
            cam.setMinVerticalRotation(.145f); 
        }
        
    }
    
    public ChaseCamera getChaseCam() {
        return cam;
    }
    
    public void setEnabled(boolean newVal) {
        enabled = newVal;
        cam.setEnabled(newVal);
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setHuntCam(boolean hunt) {
        
        isHunt = hunt;
        
        if(isHunt) {
            
            app.getFlyByCamera().setEnabled(true);
            
            if ("Dalvik".equals(System.getProperty("java.vm.name"))) 
                app.getFlyByCamera().setDragToRotate(true);
            
            app.getFlyByCamera().setMoveSpeed(0);
            app.getFlyByCamera().setRotationSpeed(2f);
            
        }
        
        else {
            
            app.getFlyByCamera().setEnabled(false);
            
        }
        
    }    
    
    public void update(float tpf) {
    
        if(!isHunt)
        if (enabled)
            chaseCamMove();
    
    }
  
  }