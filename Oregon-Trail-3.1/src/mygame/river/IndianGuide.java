/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.river;

import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import mygame.player.PlayerManager;
import mygame.river.gui.IndianGuideGui;
import mygame.util.Interactable;

/**
 *
 * @author Bawb
 */
public class IndianGuide extends Interactable {
    
    private IndianGuideGui gui;
    
    public IndianGuide(AppStateManager stateManager) {
        super(stateManager);
        gui = new IndianGuideGui(getStateManager());
        setName("Indian Guide");
    }
    
    @Override
    public void enterProximity() {
        super.enterProximity();
        gui.getInteractButton().show();
        getStateManager().getState(PlayerManager.class).getPlayer().getHud().showAlert("Indian", "An Indian Guide.");
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
