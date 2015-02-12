/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author Bawb
 */
public class UtilityManager extends AbstractAppState {
    
    private InteractionManager interactionManager;
    private SimpleApplication  app;
    private MaterialManager    matMan;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        createInteractionManager();
        createMaterialManager();
    }

    private void createMaterialManager() {
       matMan = new MaterialManager(app.getAssetManager());
    }
    
    public MaterialManager getMaterialManager() {
        return matMan;
    }
    
    private void createInteractionManager() {
        interactionManager = new InteractionManager(app.getInputManager());
    }
    
    public InteractionManager getInteractionManager() {
        return interactionManager;
    }
    
}
