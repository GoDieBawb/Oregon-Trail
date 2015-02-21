/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import mygame.util.Interactable;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import mygame.player.PlayerManager;
import mygame.town.gui.StoreGui;

/**
 *
 * @author Bawb
 */
public class ShopKeeper extends Interactable {
    
    public ShopKeeper(AppStateManager stateManager) {
        super(stateManager);
        setGui(new StoreGui(getStateManager()));
    }    
    
    @Override
    public void enterProximity() {
        super.enterProximity();
        ((StoreGui)getGui()).getInteractButton().show();
    }
    
    @Override 
    public void exitProximity() {
        super.exitProximity();
        ((StoreGui)getGui()).getInteractButton().hide();
    }
    
    @Override
    public void whileInProx() {
        lookAt(getStateManager().getState(PlayerManager.class).getPlayer().getModel().getWorldTranslation(), new Vector3f(0,1,0));
    }
    
}
