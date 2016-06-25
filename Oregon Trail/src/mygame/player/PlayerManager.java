/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import mygame.GameManager;

/**
 *
 * @author Bawb
 */
public class PlayerManager extends AbstractAppState {
    
    private Player            player;
    private SimpleApplication app;
    private Long              deathTime;
    private boolean           cooling;
    
    public PlayerManager(Application app) {
        this.app = (SimpleApplication) app;
        createPlayer();
        player.createChaseControl();
    }
    
    private void createPlayer() {
        player = new Player(app.getStateManager());
    }
    
    public void initPersonPlayer(BulletAppState physics) {
        app.getRootNode().attachChild(player);
        physics.getPhysicsSpace().add(player.getPhys());
        player.getPhys().setGravity(new Vector3f(0,-50,0));
    }
    
    public void loadPlayerInfo() {
        player.load();
        player.initParty();
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public void endGame() {
        player.reset();
        player.getHud().getLeftStick().show();
        app.getStateManager().getState(GameManager.class).initTown();
    }
    
    @Override
    public void update(float tpf) {
        if (player.getWorldTranslation().y < -5)
            player.getPhys().warp(player.getWorldTranslation().add(0,10,0));
    }
    
}
