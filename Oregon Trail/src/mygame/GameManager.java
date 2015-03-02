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
    private PlayerManager     playerManager;
    private TownState         townState;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication) app;
        createPlayerManager();
        createUtilityManager();
        createTownState();
        playerManager.loadPlayerInfo();
        playerManager.getPlayer().createHud();
        loadSituation();
    }

    private void loadSituation() {
        String setting = (String) playerManager.getPlayer().getSituation().get("Setting");
        
        if (setting.equals("Town")) {
            initTown();
            playerManager.initPersonPlayer(utilityManager.getPhysicsManager().getPhysics());
        }
        
    }
    
    private void createPlayerManager() {
        app.getStateManager().attach(new PlayerManager(app));
        playerManager = app.getStateManager().getState(PlayerManager.class);
    }
    
    private void createTownState() {
        app.getStateManager().attach(new TownState(app));
        townState = app.getStateManager().getState(TownState.class);
    }
    
    public void initTown() {
        clearAll();
        townState.initTown();
        utilityManager.getPhysicsManager().addToPhysics(townState.getTownSceneManager().getScene());
        app.getStateManager().getState(PlayerManager.class).initPersonPlayer(utilityManager.getPhysicsManager().getPhysics());
        utilityManager.getMaterialManager().makeUnshaded(app.getRootNode());
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
