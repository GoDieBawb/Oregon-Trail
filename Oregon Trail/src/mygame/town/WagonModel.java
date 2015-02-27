/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import mygame.util.Interactable;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import mygame.player.PlayerManager;
import mygame.util.WagonGui;

/**
 *
 * @author Bawb
 */
public class WagonModel extends Interactable {
    
    public WagonModel(AppStateManager stateManager) {
        super(stateManager);
        setGui(new WagonGui(getStateManager()));
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
    }
    
    @Override 
    public void exitProximity() {
        super.exitProximity();
    }
    
}
