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
    private GuiManager         guiManager;
    private SkeletonFinder     skeletonFinder;
    private AudioManager       audioManager;

    public UtilityManager(Application app) {
        System.out.println("Creating Utility Manager");
        this.app = (SimpleApplication) app;
        createInteractionManager();
        createMaterialManager();
        createYamlManager();
        createCameraManager();
        createPhysicsManager();
        createGuiManager();
        createSkeletonFinder();
        createAudioManager();
    }
    
    private void createAudioManager() {
        audioManager = new AudioManager(app.getStateManager());
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }
    
    private void createSkeletonFinder() {
        skeletonFinder = new SkeletonFinder();
    }
    
    public SkeletonFinder getSkeletonFinder() {
        return skeletonFinder;
    }
    
    private void createGuiManager() {
        guiManager = new GuiManager(app);
    }
    
    public GuiManager getGuiManager() {
        return guiManager;
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
        interactionManager = new InteractionManager(app);
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
