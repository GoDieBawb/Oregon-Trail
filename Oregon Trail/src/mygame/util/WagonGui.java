/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.state.AppStateManager;
import tonegod.gui.controls.buttons.ButtonAdapter;

/**
 *
 * @author Bawb
 */
public class WagonGui extends Gui {

    private ButtonAdapter moveButton;
    private ButtonAdapter situationButton;
    private ButtonAdapter suppliesButton;
    
    public WagonGui(AppStateManager stateManager) {
        super(stateManager);
    }    
    
    @Override
    public void createElements() {
        createMoveButton();
        createSituationButton();
        createSuppliesButton();
    }
    
    private void createMoveButton() {
    
    }
    
    private void createSituationButton() {
    
    }
    
    private void createSuppliesButton() {
    
    }
    
}
