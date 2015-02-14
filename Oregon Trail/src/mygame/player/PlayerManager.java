/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.bullet.BulletAppState;

/**
 *
 * @author Bawb
 */
public class PlayerManager extends AbstractAppState {
    
    private Player            player;
    private SimpleApplication app;
    
    public PlayerManager(Application app) {
        this.app = (SimpleApplication) app;
        createPlayer();
    }
    
    private void createPlayer() {
        player = new Player(app.getStateManager());
    }
    
    public void initTownPlayer(BulletAppState physics) {
        app.getRootNode().attachChild(player);
        physics.getPhysicsSpace().add(player.getPhys());
    }
    
    public void loadPlayerInfo() {
        player.load();
    }
    
    public Player getPlayer() {
        return player;
    }
    
}
