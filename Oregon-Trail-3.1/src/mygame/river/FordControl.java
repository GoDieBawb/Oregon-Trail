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
public class FordControl extends AbstractControl {

    private boolean isCrossing;
    private boolean goDown;
    
    public void cross() {
        isCrossing = true;
    }
    
    public boolean isCrossing() {
        return isCrossing;
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
            
            getSpatial().move(0,0,1f*tpf);
            
            if (getSpatial().getLocalTranslation().z > 38f) {
                isCrossing = false;
            }
            
        }
        
        if (getSpatial().getLocalTranslation().z > 6 && getSpatial().getLocalTranslation().z < 28) {
        
            if (goDown && getSpatial().getLocalTranslation().y < -1.65) {
                goDown = false;
            }
        
            else if (!goDown && getSpatial().getLocalTranslation().y > -1.5f) {
                goDown = true;
            }
            
        }
        
        else {
            
            if (getSpatial().getLocalTranslation().z > 4 && getSpatial().getLocalTranslation().z < 6f)
                getSpatial().move(0,-1f*tpf,0);
            
            if (getSpatial().getLocalTranslation().z > 30) {
                
                goDown = false;
                
                if (getSpatial().getLocalTranslation().y < 0)
                    getSpatial().move(0,1f*tpf,0);
                
            }
            
            else if (getSpatial().getLocalTranslation().y < 0)
                getSpatial().move(0,.25f*tpf,0);
            
        }
        
        if (goDown) {
            getSpatial().move(0,-.1f*tpf,0);
        }
        
        else {
            
            if (getSpatial().getLocalTranslation().y < 0)
                getSpatial().move(0,.1f*tpf,0);
            
        }
        
    }
    
}
