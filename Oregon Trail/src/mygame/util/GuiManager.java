/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bawb
 */
public class GuiManager {
    
    private Screen screen;
    
    public GuiManager(SimpleApplication app) {
        createScreen(app);
    }
    
    private void createScreen(SimpleApplication app) {
        screen = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
        screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
        screen.setUseMultiTouch(true);
        app.getGuiNode().addControl(screen);
    }
    
    public Screen getScreen() {
        return screen;
    }
    
}
