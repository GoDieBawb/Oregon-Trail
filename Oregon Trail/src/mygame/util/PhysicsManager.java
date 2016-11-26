/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import mygame.player.PlayerManager;

/**
 *
 * @author Bawb
 */
public class PhysicsManager {
    
    private BulletAppState physics;
    
    public PhysicsManager(AppStateManager stateManager) {
        physics = new BulletAppState();
        //physics.setDebugEnabled(true);
        stateManager.attach(physics);
    }
    
    public void rePhys(AppStateManager stateManager, Node node) {
        physics.getPhysicsSpace().removeAll(node);
        physics.getPhysicsSpace().add(node);
    } 
    
    public void clearPhysics(AppStateManager stateManager, Node node) {
        
        if (node == null)
            node = ((SimpleApplication)stateManager.getApplication()).getRootNode();
        
        physics.getPhysicsSpace().removeAll(node);
        //physics = null;
        //physics = new BulletAppState();
        //stateManager.attach(physics);
    }
    
    public void addToPhysics(Node node) {
        RigidBodyControl rbc = new RigidBodyControl(0f);
        node.addControl(rbc);
        physics.getPhysicsSpace().add(node);
    }
    
    public BulletAppState getPhysics() {
        return physics;
    }
    
}
