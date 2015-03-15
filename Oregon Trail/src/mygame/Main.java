package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setShowSettings(false);
        AppSettings newSetting = new AppSettings(true);
        newSetting.setFrameRate(30);
        setSettings(newSetting);
        setDisplayFps(false);
        setDisplayStatView(false);
        this.setPauseOnLostFocus(false);
        stateManager.attach(new GameManager());
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
