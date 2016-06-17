/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.river;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.util.Interactable;

/**
 *
 * @author Bawb
 */
public class RiverState extends AbstractAppState {
    
    private RiverSceneManager riverSceneManager;
    private SimpleApplication app;
    
    
    public RiverState(SimpleApplication app) {
        this.app = app;
        createRiverSceneManager();
    }
 
    public void initRiver() {
        riverSceneManager.initScene(app);
    }
    
    private void createRiverSceneManager() {
        riverSceneManager = new RiverSceneManager();
    }
    
    public RiverSceneManager getRiverSceneManager() {
        return riverSceneManager;
    }
    
    public void removeRiver() {
        
        app.getRootNode().detachAllChildren();
        app.getStateManager().getState(GameManager.class).getUtilityManager().getPhysicsManager().clearPhysics(app.getStateManager(), null);
        Node intNode = riverSceneManager.getInteractableNode();
        
        for (int i = 0; i < intNode.getQuantity(); i++) {
            Interactable a = (Interactable) intNode.getChild(i);
            a.getGui().remove();
        }
    
    }
    
    @Override
    public void update(float tpf) {
        riverSceneManager.update(tpf);
    }
    
}
