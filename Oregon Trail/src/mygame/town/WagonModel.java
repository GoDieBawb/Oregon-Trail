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
public class WagonModel extends Interactable {
    
    public WagonModel(AppStateManager stateManager) {
        super(stateManager);
        //gui = new BlacksmithGui(getStateManager());
    }    
    
    @Override
    public void enterProximity() {
        super.enterProximity();
    }
    
    @Override 
    public void exitProximity() {
        super.exitProximity();
    }
    
}
