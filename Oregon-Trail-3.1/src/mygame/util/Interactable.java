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
    private float           proxDistance;
    
    public Interactable(AppStateManager stateManager) {
        this.stateManager = stateManager;
        proxDistance      = 2.5f;
    }
    
    public AppStateManager getStateManager() {
        return stateManager;
    }
    
    public void enterProximity() {
        inProx = true;
        stateManager.getState(PlayerManager.class).getPlayer().getHud().getInfoText().getButtonOk().hide();
    }
    
    public void exitProximity() {
        inProx = false;
        stateManager.getState(PlayerManager.class).getPlayer().getHud().getInfoText().hide();
        stateManager.getState(PlayerManager.class).getPlayer().getHud().getInfoText().getButtonOk().show();
    }
    
    public void setProxDistance(float newProx) {
        proxDistance = newProx;
    }
    
    public float getProxDistance() {
        return proxDistance;
    }
    
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
