/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.river;


import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Bawb
 */
public class RiverBoatControl extends AbstractControl {

    private boolean isCrossing;
    private boolean goDown;
    
    public void cross() {
        isCrossing = true;
    }
    
    public boolean isCrossing() {
        return isCrossing();
    }
    
    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    @Override
    public void update(float tpf) {
        
        if (isCrossing) {
            getSpatial().move(0,0,-1f*tpf);
            
            if (getSpatial().getLocalTranslation().z < -11.5f) {
                isCrossing = false;
            }
            
        }
        
        if (goDown && getSpatial().getLocalTranslation().y < -1.1) {
            goDown = false;
        }
        
        else if (!goDown && getSpatial().getLocalTranslation().y > -0.95f) {
            goDown = true;
        }
        
        if (goDown) {
            getSpatial().move(0,-.1f*tpf,0);
        }
        
        else {
            getSpatial().move(0,.1f*tpf,0);
        }
        
    }
    
}
