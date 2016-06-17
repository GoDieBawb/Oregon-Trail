/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.Vector2f;
import mygame.GameManager;
import mygame.player.PlayerManager;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bawb
 */
public class GuiManager {
    
    private Screen            screen;
    private SimpleApplication app;
    private AppStateManager   stateManager;
    
    public GuiManager(SimpleApplication app) {
        this.app     = app;
        stateManager = app.getStateManager();
        createScreen();
    }
    
    private void createScreen() {
        
        screen = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
        screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
        
        if ("Dalvik".equals(System.getProperty("java.vm.name"))) {
            screen.setUseMultiTouch(true);
            app.getInputManager().setSimulateMouse(true);
        }
        
        app.getGuiNode().addControl(screen);
        
    }
    
    public void clearScreen(SimpleApplication app) {
        //app.getGuiNode().removeControl(screen);
        //screen = null;
        //screen = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
        //screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
        //screen.setUseMultiTouch(true);
        //app.getInputManager().setSimulateMouse(true);
        //app.getGuiNode().addControl(screen);
        //screen.addElement(app.getStateManager().getState(PlayerManager.class).getPlayer().getHud().getInfoText());
        screen.getElements().removeAll(screen.getElements());
        app.getStateManager().getState(PlayerManager.class).getPlayer().getHud().getInfoText().hide();
        app.getStateManager().getState(PlayerManager.class).getPlayer().getHud().getInfoText().getButtonOk().show();
    }
    
    public Screen getScreen() {
        return screen;
    }
    
}
