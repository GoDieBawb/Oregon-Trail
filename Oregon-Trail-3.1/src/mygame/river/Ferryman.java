/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.river;

import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import mygame.player.PlayerManager;
import mygame.river.gui.FerrymanGui;
import mygame.util.Interactable;

/**
 *
 * @author Bawb
 */
public class Ferryman extends Interactable {
    
    private FerrymanGui gui;
    
    public Ferryman(AppStateManager stateManager) {
        super(stateManager);
        gui = new FerrymanGui(getStateManager());
        setName("Ferryman");
    }
    
    @Override
    public void enterProximity() {
        super.enterProximity();
        gui.getInteractButton().show();
        getStateManager().getState(PlayerManager.class).getPlayer().getHud().showAlert("Ferryman", "Offers trips across the river, for a price.");
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
