/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.trail;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;

/**
 *
 * @author Bawb
 */
public class TrailState extends AbstractAppState {
    
    private SimpleApplication app;
    private TrailSceneManager trailSceneManager;
    
    public TrailState(SimpleApplication app) {
        this.app = app;
        trailSceneManager = new TrailSceneManager();
    }
    
    @Override
    public void update(float tpf){
        trailSceneManager.update(tpf);
    }
    
}
