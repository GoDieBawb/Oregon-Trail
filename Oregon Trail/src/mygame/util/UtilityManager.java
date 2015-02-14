/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;

/**
 *
 * @author Bawb
 */
public class UtilityManager {
    
    private InteractionManager interactionManager;
    private SimpleApplication  app;
    private MaterialManager    materialManager;
    private YamlManager        yamlManager;
    private CameraManager      cameraManager;
    private PhysicsManager     physicsManager;

    public UtilityManager(Application app) {
        this.app = (SimpleApplication) app;
        createInteractionManager();
        createMaterialManager();
        createYamlManager();
        createCameraManager();
    }

    private void createCameraManager() {
        cameraManager = new CameraManager(app);
    }
    
    public CameraManager getCameraManager() {
        return cameraManager;
    }
    
    private void createYamlManager() {
        yamlManager = new YamlManager();
    }
    
    public YamlManager getYamlManager() {
        return yamlManager;
    }

    private void createMaterialManager() {
       materialManager = new MaterialManager(app.getAssetManager());
    }
    
    public MaterialManager getMaterialManager() {
        return materialManager;
    }
    
    private void createInteractionManager() {
        interactionManager = new InteractionManager(app.getInputManager());
    }
    
    public InteractionManager getInteractionManager() {
        return interactionManager;
    }
    
    private void createPhysicsManager() {
        physicsManager = new PhysicsManager(app.getStateManager());
    }
    
    public PhysicsManager getPhysicsManager() {
        return physicsManager;
    }
    
}
