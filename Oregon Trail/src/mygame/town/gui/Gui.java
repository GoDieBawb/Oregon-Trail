/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town.gui;

import com.jme3.app.state.AppStateManager;
import java.util.ArrayList;
import mygame.GameManager;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bawb
 */
public abstract class Gui {
    
    private ArrayList<Element> elements;
    private Screen             screen;
    private AppStateManager    stateManager;
    
    public Gui(AppStateManager stateManager) {
        screen            = stateManager.getState(GameManager.class).getUtilityManager().getGuiManager().getScreen();
        this.stateManager = stateManager;
        elements = new ArrayList<Element>();
        createElements();
    }
    
    public abstract void createElements();
    
    public AppStateManager getStateManager() {
        return stateManager;
    }
    
    public Screen getScreen() {
        return screen;
    }
    
    public ArrayList<Element> getElements() {
        return elements;
    }
    
    public void attach() {
        
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).showWithEffect();
        }
        
    }
    
    public void remove() {
        
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).hideWithEffect();
        }
    
    }
    
}
