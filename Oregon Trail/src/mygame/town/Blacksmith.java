/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import com.jme3.app.state.AppStateManager;
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
