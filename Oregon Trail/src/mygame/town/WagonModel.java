/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import mygame.util.Interactable;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import mygame.player.PlayerManager;
import mygame.player.WagonGui;

/**
 *
 * @author Bawb
 */
public class WagonModel extends Interactable {
    
    private WagonGui gui;
    
    public WagonModel(AppStateManager stateManager) {
        super(stateManager);
        gui = new WagonGui(getStateManager());
        setName("Wagon");
    }    
    
    public void checkOxen() {
        
        int oxCount = (Integer) getStateManager().getState(PlayerManager.class).getPlayer().getInventory().get("Oxen");
        Node leftOx  = ((Node) ((Node) getChild("Wagon")).getChild("LeftCow"));
        Node rightOx = ((Node) ((Node) getChild("Wagon")).getChild("RightCow"));
        
        if (oxCount == 0) {
            leftOx.getChild(0).setLocalTranslation(0,-5,0);
            rightOx.getChild(0).setLocalTranslation(0,-5,0);        
        }
        
        else if (oxCount == 1) {
            leftOx.getChild(0).setLocalTranslation(0,-5,0);
            rightOx.getChild(0).setLocalTranslation(0,0,0);
        }
        
        else {
            leftOx.getChild(0).setLocalTranslation(0,0,0);
            rightOx.getChild(0).setLocalTranslation(0,0,0);
        } 
        
    }
    
    @Override
    public void enterProximity() {
        super.enterProximity();
        getStateManager().getState(PlayerManager.class).getPlayer().getHud().getInfoText().getButtonOk().show();
        gui.getMoveButton().show();
        gui.getSituationButton().show();
        gui.getSuppliesButton().show();
    }
    
    @Override 
    public void exitProximity() {
        super.exitProximity();
        gui.getMoveButton().hide();
        gui.getSituationButton().hide();
        gui.getSuppliesButton().hide();
    }
    
}
