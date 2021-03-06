/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import mygame.player.PlayerManager;
import mygame.town.gui.FarmerGui;
import mygame.util.Interactable;

/**
 *
 * @author Bawb
 */
public class Farmer extends Interactable {
    
    private FarmerGui gui;
    
    public Farmer(AppStateManager stateManager) {
        super(stateManager);
        gui = new FarmerGui(getStateManager());
        setName("Farmer");
    }
    
    @Override
    public void enterProximity() {
        super.enterProximity();
        gui.getInteractButton().show();
        getStateManager().getState(PlayerManager.class).getPlayer().getHud().showAlert("Farmer", "Buy Oxen to pull your wagon and Hay to feed them at the farmer.");
    }
    
    @Override 
    public void exitProximity() {
        super.exitProximity();
        gui.getInteractButton().hide();
    } 
    
    @Override
    public void whileInProx() {
        lookAt(getStateManager().getState(PlayerManager.class).getPlayer().getModel().getWorldTranslation().multLocal(1,0,1), new Vector3f(0,1,0));
    }    
    
}
