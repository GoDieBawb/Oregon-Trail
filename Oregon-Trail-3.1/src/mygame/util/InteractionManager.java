/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import mygame.player.PlayerManager;

/**
 *
 * @author Bawb
 */
public class InteractionManager implements ActionListener {
    
    private InputManager      inputManager;
    private SimpleApplication app;
    private boolean           up = false, down = false, left = false, right = false, click = false;
    private boolean           up1 = false, down1 = false, left1 = false, right1 = false;
    private Vector2f          touchSpot;
    
    public InteractionManager(SimpleApplication app) {
        this.app     = app;
        inputManager = app.getInputManager();
        inputManager.setCursorVisible(false);
        setUpKeys();
    }
    
    private void setUpKeys() {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Cursor", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Cursor");
        inputManager.addListener(this, "Click");
        inputManager.addMapping("Up1", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down1", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Left1", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right1", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(this, "Up1");
        inputManager.addListener(this, "Down1");
        inputManager.addListener(this, "Left1");
        inputManager.addListener(this, "Right1");
    }
    
    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {

        if (binding.equals("Up")) {
            up = isPressed;
        }
        
        else if (binding.equals("Down")) {
            down = isPressed;
        }
        
        else if (binding.equals("Left")) {
            left = isPressed;
        }
        
        else if (binding .equals("Right")) {
            right = isPressed;
        }
        
        else if (binding .equals("Cursor")) {
            
            if(isPressed) {
                inputManager.setCursorVisible(true);
                app.getStateManager().getState(PlayerManager.class).getPlayer().getChaseControl().getCameraManager().getChaseCam().setDragToRotate(true);
            }
            
            else {
                inputManager.setCursorVisible(false);
                app.getStateManager().getState(PlayerManager.class).getPlayer().getChaseControl().getCameraManager().getChaseCam().setDragToRotate(false);
            }
                
        }        
        
        else if (binding.equals("Click")) {
            
            click = isPressed;
            
        }
        
        if (binding.equals("Up1")) {
            up1 = isPressed;
        }
        
        else if (binding.equals("Down1")) {
            down1 = isPressed;
        }
        
        else if (binding.equals("Left1")) {
            left1 = isPressed;
        }
        
        else if (binding .equals("Right1")) {
            right1 = isPressed;
        }        
        
    }
    
    public void setTouchSpot(Vector2f newVal) {
        touchSpot = newVal;
    }
    
    public Vector2f getTouchSpot() {
        return touchSpot;
    }
    
    public void setClick(boolean newVal) {
        click = newVal;
    }
    
    public void setUp(boolean newVal) {
        up = newVal;
    }
    
    public void setDown(boolean newVal) {
        down = newVal;
    }
    
    public void setLeft(boolean newVal) {
        left = newVal;
    }
    
    public void setRight(boolean newVal) {
        right = newVal;
    }
    
    public void setUp1(boolean newVal) {
        up1 = newVal;
    }
    
    public void setDown1(boolean newVal) {
        down1 = newVal;
    }
    
    public void setLeft1(boolean newVal) {
        left1 = newVal;
    }
    
    public void setRight1(boolean newVal) {
        right1 = newVal;
    }    
    
    public boolean getIsPressed(String triggerName) {
        
        if (triggerName.equals("Left"))
            return left;
        
        else if (triggerName.equals("Right"))
            return right;
        
        else if (triggerName.equals("Up"))
            return up;
        
        else if (triggerName.equals("Down"))
            return down;
        
        else if (triggerName.equals("Click"))
            return click;
        
        if (triggerName.equals("Left1"))
            return left1;
        
        else if (triggerName.equals("Right1"))
            return right1;
        
        else if (triggerName.equals("Up1"))
            return up1;
        
        else if (triggerName.equals("Down1"))
            return down1;   
        
        else
            return false;
        
    }
    
}
