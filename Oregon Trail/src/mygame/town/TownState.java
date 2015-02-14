/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author Bawb
 */
public class TownState extends AbstractAppState {
    
    private TownInteractionManager townInteractionManager;
    private TownSceneManager       townSceneManager;
    private SimpleApplication      app;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication) app;
        createTownInteractionManager();
        createTownSceneManager();
    }
    
    public void initTown() {
        townSceneManager.initScene(app);
    }
    
    private void createTownInteractionManager() {
        townInteractionManager = new TownInteractionManager(app.getStateManager());
    }
    
    private void createTownSceneManager() {
        townSceneManager = new TownSceneManager();
    }
    
    public TownSceneManager getTownSceneManager() {
        return townSceneManager;
    }
    
    @Override
    public void update(float tpf) {
        townInteractionManager.update(tpf);
        townSceneManager.update(tpf);
    }
    
}
