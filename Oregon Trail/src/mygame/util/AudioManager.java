/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import java.util.HashMap;

/**
 *
 * @author Bawb
 */
public class AudioManager {
    
    private HashMap         sounds;
    private AppStateManager stateManager; 
    
    public AudioManager(AppStateManager stateManager) {
        this.stateManager = stateManager;
        sounds = new HashMap();
    }
    
    public void loadSound(String soundName, String path, boolean loop) {
        AudioNode an = new AudioNode(stateManager.getApplication().getAssetManager(), path, false);
        an.setPositional(false);
        an.setLooping(loop);
        an.setVolume(.5f);
        sounds.put(soundName, an);
    }
    
    public void playSound(String soundName) {
        ((AudioNode)sounds.get(soundName)).stop();
        ((AudioNode)sounds.get(soundName)).play();
    }
    
}
