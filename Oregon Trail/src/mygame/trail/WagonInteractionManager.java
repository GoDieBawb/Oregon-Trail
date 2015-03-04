/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.trail;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.util.InteractionManager;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bawb
 */
public class WagonInteractionManager {
    
    private int               turnValue;
    private boolean           left, right, up, down, click;
    private AppStateManager   stateManager;
    private Player            player;
    private SimpleApplication app;
    private Node              scene;

    public WagonInteractionManager(AppStateManager stateManager) {
        this.stateManager = stateManager;
        player            = stateManager.getState(PlayerManager.class).getPlayer();
        app               = (SimpleApplication) stateManager.getApplication();
    }    
    
    public void setScene(Node scene) {
        this.scene = scene;
    }
    
    public int getTurnValue() {
        return turnValue;
    }
    
    private void updateKeys() {
        InteractionManager im = stateManager.getState(GameManager.class).getUtilityManager().getInteractionManager();
        up    = im.getIsPressed("Up");
        down  = im.getIsPressed("Down");
        left  = im.getIsPressed("Left");
        right = im.getIsPressed("Right");
        click = im.getIsPressed("Click");
    }
    
    private void setTurnValue() {
        
            Vector2f clickSpot = app.getInputManager().getCursorPosition();
            float xSpot        = clickSpot.getX();
            Screen screen      = app.getStateManager().getState(GameManager.class).getUtilityManager().getGuiManager().getScreen();
            
            if(click) {
                
                if (xSpot < screen.getWidth()/2) left  = true;
                else                             right = true;
                
            }
            
            if (left)  turnValue =  player.getWagon().getTurnSpeed();
            
            else if (right) turnValue = -player.getWagon().getTurnSpeed();
            
            else turnValue = 0;
            
    }
    
    public void update(float tpf) {
        updateKeys();
        setTurnValue();
    }
    
}
