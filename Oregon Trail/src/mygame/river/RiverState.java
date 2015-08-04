/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.river;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;

/**
 *
 * @author Bawb
 */
public class RiverState extends AbstractAppState {
    
    private RiverSceneManager riverSceneManager;
    private SimpleApplication app;
    
    public RiverState(SimpleApplication app) {
        this.app = app;
        createRiverSceneManager();
    }
 
    public void initRiver() {
        riverSceneManager.initScene(app);
    }
    
    private void createRiverSceneManager() {
        riverSceneManager = new RiverSceneManager();
    }
    
    public RiverSceneManager getRiverSceneManager() {
        return riverSceneManager;
    }
    
    @Override
    public void update(float tpf) {
        riverSceneManager.update(tpf);
    }
    
}
