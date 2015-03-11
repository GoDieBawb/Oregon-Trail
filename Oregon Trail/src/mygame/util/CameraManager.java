/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.util.TempVars;
import mygame.player.Player;
import mygame.player.PlayerManager;

/**
 *
 * @author Bawb
 */
public class CameraManager {
  
  private SimpleApplication app;
  private Player            player;
  public  Camera            cam;
  private Vector3f          panDir;
  private Vector3f          cameraSpot;
  private Vector3f          cameraLook;
  public  boolean           isPan;
  private boolean           isHunt;
  
  public CameraManager(SimpleApplication app) {
     this.app   = app;
     player     = app.getStateManager().getState(PlayerManager.class).getPlayer();
     isHunt     = false;
     initCamera();
  }
  
  //Creates camera
  private void initCamera() {

    //Creates a new chase cam and attached it to the player.getModel() for the game
    app.getFlyByCamera().setEnabled(false);
    app.getFlyByCamera().setDragToRotate(true);
    cam = this.app.getCamera();
    cameraLook = player.getModel().getWorldTranslation().add(0,1f,0);
    
  }
  
  public void chaseCamMove(float tpf) {
      
      float minDistance = 2f;
      
      cameraLook = cameraLook.mult(.7f).add
                    (player.getModel().getWorldTranslation().add(0,1f,0).mult(.3f));
      
      cameraSpot = player.getModel().getWorldTranslation()
                      .add(player.getPhys().getViewDirection()
                          .normalize()
                            .negate().mult(minDistance)).add(0,1f,0);
      
      slerpLookAt(cameraLook, tpf);
 
      if (cam.getLocation().distance(player.getModel().getWorldTranslation()) > minDistance && !isPan) {
          
          panDir = cameraSpot.subtract(cam.getLocation()).mult(2);
          cam.setLocation(cam.getLocation().addLocal(panDir.mult(tpf)));
          
      }
      
      else if (cam.getLocation().distance(player.getModel().getWorldTranslation()) < minDistance && !isPan) {
          
          panDir = cameraSpot.subtract(cam.getLocation());
          cam.setLocation(cam.getLocation().addLocal(panDir.mult(tpf)));
          isPan = true;
          
      }
      
      else if (isPan) {
      
          cam.setLocation(cam.getLocation().addLocal(panDir.mult(tpf)));
          
          if (cam.getLocation().distance(cameraSpot) < .05f)
          isPan = false; 
          else if (cam.getLocation().distance(cameraSpot) > 2f)
          isPan = false;    
      
      }
    
  }
 
  private void slerpLookAt(Vector3f pos, float amount) {
      
    TempVars vars = TempVars.get();
    Vector3f newDirection = vars.vect1;
    Vector3f newUp = vars.vect2;
    Vector3f newLeft = vars.vect3;
    Quaternion airRotation = vars.quat1;

    newDirection.set(pos).subtractLocal(cam.getLocation()).normalizeLocal();

    newLeft.set(Vector3f.UNIT_Y).crossLocal(newDirection).normalizeLocal();
    
    if (newLeft.equals(Vector3f.ZERO)) {
        
        if (newDirection.x != 0) {
            newLeft.set(newDirection.y, -newDirection.x, 0f);
        } 
        
        else {
            newLeft.set(0f, newDirection.z, -newDirection.y);
        }
        
    }

     newUp.set(newDirection).crossLocal(newLeft).normalizeLocal();

     airRotation.fromAxes(newLeft, newUp, newDirection);
     airRotation.normalizeLocal();

     slerpTo(airRotation, amount*5);

     vars.release();

    }
  
    private void slerpTo(Quaternion quaternion, float amount) {
        Quaternion rotation = cam.getRotation();
        rotation.slerp(quaternion, amount);
        cam.setRotation(rotation);
    }  
    
    public void setHuntCam(boolean hunt) {
        
        isHunt = hunt;
        
        if(isHunt) {
            app.getFlyByCamera().setEnabled(true);
            app.getFlyByCamera().setMoveSpeed(0);
        }
        
        else {
            app.getFlyByCamera().setEnabled(false);
        }
        
    }
    
    public void update(float tpf) {
        if(!isHunt) 
        chaseCamMove(tpf);
    }
    
}
