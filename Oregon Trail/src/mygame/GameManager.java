/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
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
    private Long              debugCool;
    private boolean           isDebug = false;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication) app;
        createPlayerManager();
        createUtilityManager();
        createTownState();
        createTrailState();
        playerManager.loadPlayerInfo();
        playerManager.getPlayer().createHud();
        townState.setEnabled(false);
        trailState.setEnabled(false);
        loadSituation();
        debugCool = System.currentTimeMillis();
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
        utilityManager.getMaterialManager().makeUnshaded(app.getRootNode());
        townState.setEnabled(true);
        app.getStateManager().getState(PlayerManager.class).initPersonPlayer(utilityManager.getPhysicsManager().getPhysics());
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
    
    public void clearAll() {
        trailState.setEnabled(false);
        townState.setEnabled(false);
        clearTerrainLod();
        utilityManager.getGuiManager().clearScreen(app);
        app.getRootNode().detachAllChildren();
        utilityManager.getPhysicsManager().clearPhysics(app.getStateManager(), null);
        trailState.getTrailSceneManager().clearTrail(app.getStateManager());
        townState.getTownSceneManager().clearTown(app.getStateManager());
        System.gc();
    }
    
    private void clearTerrainLod() {
    
        SceneGraphVisitor sgv = new SceneGraphVisitor() {
            
            public void visit(Spatial spatial) {
            
                if (spatial instanceof Terrain) {
                    
                    TerrainLodControl tlc = spatial.getControl(TerrainLodControl.class);
                    Field f;
                    
                    try {
                        f = tlc.getClass().getDeclaredField("executor");
                        f.setAccessible(true);
                        ExecutorService ex = (ExecutorService) f.get(tlc);
                        ex.shutdown();
                    }
                    
                    catch (NoSuchFieldException nsf) {
                    }
                    
                    catch (IllegalAccessException e) {
                    }
                    
                    catch (Exception a) {
                    
                    }
            
                }
            }
        
        };
        
        app.getRootNode().depthFirstTraversal(sgv);
        
    }
    
    @Override
    public void update (float tpf) {
    
        if(isDebug)
        if (System.currentTimeMillis()/1000 - debugCool/1000 > 1) {
        
            if (townState.isEnabled()) {
                townState.setEnabled(false);
                initTrail();
                System.out.println("Trail Making");
            }
            
            else {
                trailState.setEnabled(false);
                initTown();
                System.out.println("Town Making");
            }
            
            debugCool = System.currentTimeMillis();
            
        }
       
    }
    
}
