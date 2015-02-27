/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import mygame.player.PlayerManager;

/**
 *
 * @author Bawb
 */
public abstract class Interactable extends Node {
    
    private boolean         inProx;
    private AppStateManager stateManager;
    private Gui             gui;
    
    public Interactable(AppStateManager stateManager) {
        this.stateManager = stateManager;
    }
    
    public AppStateManager getStateManager() {
        return stateManager;
    }
    
    public void enterProximity() {
        inProx = true;
    };
    
    public void exitProximity() {
        inProx = false;
        stateManager.getState(PlayerManager.class).getPlayer().getHud().getInfoText().hide();
    };
    
    public void whileInProx() {
    }
    
    public void setInProx(boolean inProx) {
        this.inProx = inProx;
    }
    
    public boolean inProx() {
        return inProx;
    }
    
    public void setGui(Gui gui) {
        this.gui = gui;
    }
    
    public Gui getGui() {
        return gui;
    }
    
}
