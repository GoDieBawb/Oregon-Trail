/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import mygame.player.PlayerManager;
import mygame.town.TownState;
import mygame.util.UtilityManager;

/**
 *
 * @author Bawb
 */
public class GameManager extends AbstractAppState {
    
    private UtilityManager    utilityManager;
    private SimpleApplication app;
    private TownState         townState;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        createUtilityManager();
        this.app = (SimpleApplication) app;
        createTownState();
    }

    private void createTownState() {
        app.getStateManager().attach(new TownState());
    }
    
    public void initTown() {
        clearAll();
        townState.initTown();
        utilityManager.getMaterialManager().makeUnshaded(app.getRootNode());
        utilityManager.getPhysicsManager().addToPhysics(townState.getTownSceneManager().getScene());
        app.getStateManager().getState(PlayerManager.class).initTownPlayer(utilityManager.getPhysicsManager().getPhysics());
    }
    
    private void createUtilityManager() {
        utilityManager = new UtilityManager(app);
    }
    
    public UtilityManager getUtilityManager() {
        return utilityManager;
    }
    
    private void clearAll() {
        app.getRootNode().detachAllChildren();
        utilityManager.getPhysicsManager().clearPhysics(app.getStateManager());
    }
    
}
