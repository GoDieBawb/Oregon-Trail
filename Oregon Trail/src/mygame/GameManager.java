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
import mygame.trail.TrailState;
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
    private TrailState        trailState;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication) app;
        createPlayerManager();
        createUtilityManager();
        createTownState();
        createTrailState();
        playerManager.loadPlayerInfo();
        playerManager.getPlayer().createHud();
        loadSituation();
    }

    private void loadSituation() {
        String setting = (String) playerManager.getPlayer().getSituation().get("Setting");
        
        if (setting.equals("Town")) {
            initTown();
            playerManager.initPersonPlayer(utilityManager.getPhysicsManager().getPhysics());
            townState.setEnabled(true);
        }
        
        else if (setting.equals("Trail")) {
            initTrail();
            trailState.setEnabled(true);
        }
        
    }
    
    private void createPlayerManager() {
        app.getStateManager().attach(new PlayerManager(app));
        playerManager = app.getStateManager().getState(PlayerManager.class);
    }
    
    private void createTownState() {
        app.getStateManager().attach(new TownState(app));
        townState = app.getStateManager().getState(TownState.class);
        townState.setEnabled(false);
    }
    
    public void initTown() {
        clearAll();
        townState.initTown();
        utilityManager.getPhysicsManager().addToPhysics(townState.getTownSceneManager().getScene());
        app.getStateManager().getState(PlayerManager.class).initPersonPlayer(utilityManager.getPhysicsManager().getPhysics());
        utilityManager.getMaterialManager().makeUnshaded(app.getRootNode());
        townState.setEnabled(true);
    }
    
    private void createTrailState() {
        app.getStateManager().attach(new TrailState(app));
        trailState = app.getStateManager().getState(TrailState.class);
        trailState.setEnabled(false);
    }
    
    public void initTrail() {
        clearAll();
        trailState.initTrail();
        utilityManager.getPhysicsManager().addToPhysics(trailState.getTrailSceneManager().getScene());
        utilityManager.getMaterialManager().makeUnshaded(app.getRootNode());
        trailState.setEnabled(true);
        app.getStateManager().getState(PlayerManager.class).initPersonPlayer(utilityManager.getPhysicsManager().getPhysics());
    }
    
    private void createUtilityManager() {
        utilityManager = new UtilityManager(app);
    }
    
    public UtilityManager getUtilityManager() {
        return utilityManager;
    }
    
    private void clearAll() {
        utilityManager.getGuiManager().clearScreen(app);
        app.getRootNode().detachAllChildren();
        utilityManager.getPhysicsManager().clearPhysics(app.getStateManager());
        trailState.setEnabled(false);
        townState.setEnabled(false);
    }
    
}
