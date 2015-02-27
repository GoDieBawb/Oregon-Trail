/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import mygame.util.Interactable;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import mygame.player.PlayerManager;
import mygame.town.gui.BlacksmithGui;


/**
 *
 * @author Bawb
 */
public class Blacksmith extends Interactable {
    
    private BlacksmithGui gui;
    
    public Blacksmith(AppStateManager stateManager) {
        super(stateManager);
        gui = new BlacksmithGui(getStateManager());
        setName("Blacksmith");
    }
    
    @Override
    public void enterProximity() {
        super.enterProximity();
        gui.getInteractButton().show();
        getStateManager().getState(PlayerManager.class).getPlayer().getHud().showAlert("Blacksmith", "Repair and Upgrade your Wagon at the Blacksmith.");
    }
    
    @Override 
    public void exitProximity() {
        super.exitProximity();
        gui.getInteractButton().hide();
    }
    
    @Override
    public void whileInProx() {
        lookAt(getStateManager().getState(PlayerManager.class).getPlayer().getModel().getWorldTranslation(), new Vector3f(0,1,0));
    }    
    
}
