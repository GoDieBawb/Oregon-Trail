/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;

/**
 *
 * @author Bawb
 */
public class PhysicsManager {
    
    private BulletAppState physics;
    
    public PhysicsManager(AppStateManager stateManager) {
        physics = new BulletAppState();
        stateManager.attach(physics);
    }
    
    public void clearPhysics(AppStateManager stateManager) {
        physics.getPhysicsSpace().removeAll(((SimpleApplication)stateManager.getApplication()).getRootNode());
    }
    
    public void addToPhysics(Node node) {
        physics.getPhysicsSpace().add(node);
    }
    
    public BulletAppState getPhysics() {
        return physics;
    }
    
}
