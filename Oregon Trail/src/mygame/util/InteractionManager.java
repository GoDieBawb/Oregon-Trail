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

/**
 *
 * @author Bawb
 */
public class InteractionManager implements ActionListener {
    
    private InputManager      inputManager;
    private SimpleApplication app;
    private boolean      up = false, down = false, left = false, right = false, click = false;
    
    public InteractionManager(SimpleApplication app) {
        this.app     = app;
        inputManager = app.getInputManager();
        setUpKeys();
    }
    
    private void setUpKeys() {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Click");
    }
    
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
        
        else if (binding.equals("Click")) {
            
            click = isPressed;
            
        }
        
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
        else
        return false;
        
    }
    
}
