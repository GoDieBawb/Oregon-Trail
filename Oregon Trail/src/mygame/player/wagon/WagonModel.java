/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player.wagon;

import mygame.util.Interactable;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.util.Gui;

/**
 *
 * @author Bawb
 */
public class WagonModel extends Interactable {
    
    private final WagonGui gui;
    
    public WagonModel(AppStateManager stateManager) {
        super(stateManager);
        gui  = new WagonGui(stateManager);
        name = "Wagon";
    }    
    
    public void checkOxen() {
        
        int oxCount  = (Integer) getStateManager().getState(PlayerManager.class).getPlayer().getInventory().get("Oxen");
        Node leftOx  = ((Node) ((Node) getChild("Wagon")).getChild("LeftCow"));
        Node rightOx = ((Node) ((Node) getChild("Wagon")).getChild("RightCow"));
        
        switch (oxCount) {
            case 0:
                leftOx.getChild(0).setLocalTranslation(0,-5,0);        
                rightOx.getChild(0).setLocalTranslation(0,-5,0);
                break;
            case 1:
                leftOx.getChild(0).setLocalTranslation(0,-5,0);
                rightOx.getChild(0).setLocalTranslation(0,0,0);
                break;
            default:
                leftOx.getChild(0).setLocalTranslation(0,0,0);
                rightOx.getChild(0).setLocalTranslation(0,0,0);
                break;
        }
        
    }
    
    @Override
    public Gui getGui() {
        return gui;
    }
    
    @Override
    public void enterProximity() {
        super.enterProximity();
        Player player = getStateManager().getState(PlayerManager.class).getPlayer();
        player.getHud().getInfoText().getButtonOk().show();
        player.getHud().getAimButton().hide();
        gui.getMoveButton().show();
        gui.getSituationButton().show();
        gui.getSuppliesButton().show();
        gui.getPartyButton().show();
    }
    
    @Override 
    public void exitProximity() {
        super.exitProximity();
        Player player = getStateManager().getState(PlayerManager.class).getPlayer();
        gui.getMoveButton().hide();
        gui.getSituationButton().hide();
        gui.getSuppliesButton().hide();
        gui.getPartyButton().hide();
        if(!player.getInWagon()  && player.getSituation().get("Setting").equals("Trail"))
            player.getHud().getAimButton().show();
        
    }
    
}
