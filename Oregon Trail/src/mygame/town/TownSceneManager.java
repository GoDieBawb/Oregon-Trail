/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;

/**
 *
 * @author Bawb
 */
public class TownSceneManager {
    
    private Node scene;
    
    public void initScene(SimpleApplication app) {
        scene = (Node) app.getAssetManager().loadModel("Scenes/Town.j3o");
        app.getRootNode().attachChild(scene);
    }
    
    public Node getScene() {
        return scene;
    }
    
    public void update(float tpf) {
    
    }
    
}
