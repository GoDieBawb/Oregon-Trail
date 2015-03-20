/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.util.DualStickControl;
import mygame.util.Interactable;

/**
 *
 * @author Bawb
 */
public class TownState extends AbstractAppState {
    
    private DualStickControl    townInteractionManager;
    private TownSceneManager    townSceneManager;
    private SimpleApplication   app;
    
    public TownState(SimpleApplication app) {
        this.app = app;
        createTownInteractionManager();
        createTownSceneManager();
    }
    
    public void initTown() {
        townSceneManager.initScene(app);
    }
    
    public void removeTown() {
        app.getRootNode().detachAllChildren();
        app.getStateManager().getState(GameManager.class).getUtilityManager().getPhysicsManager().clearPhysics(app.getStateManager(), null);
        Node intNode = townSceneManager.getInteractableNode() ;
        for (int i = 0; i < intNode.getQuantity(); i++) {
            Interactable a = (Interactable) intNode.getChild(i);
            a.getGui().remove();
        }
    
    }
    
    private void createTownInteractionManager() {
        townInteractionManager = new DualStickControl(app.getStateManager());
    }
    
    private void createTownSceneManager() {
        townSceneManager = new TownSceneManager();
    }
    
    public TownSceneManager getTownSceneManager() {
        return townSceneManager;
    }
    
    public DualStickControl getControl() {
        return townInteractionManager;
    }
    
    @Override
    public void update(float tpf) {
        townInteractionManager.update(tpf);
        townSceneManager.update(tpf);
    }
    
}
