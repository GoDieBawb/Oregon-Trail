/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import com.jme3.app.state.AppStateManager;
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
    }
    
    @Override
    public void enterProximity() {
        super.enterProximity();
        gui.getInteractButton().show();
    }
    
    @Override 
    public void exitProximity() {
        super.exitProximity();
        gui.getInteractButton().hide();
    }    
    
}
