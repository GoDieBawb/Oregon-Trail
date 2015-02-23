/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import java.util.HashMap;
import mygame.util.Gui;
import mygame.util.YamlLoader;
import tonegod.gui.controls.windows.AlertBox;

/**
 *
 * @author Bawb
 */
public class Hud extends Gui {

    private AlertBox infoText;
    private HashMap  scripts;
    
    public Hud(AppStateManager stateManager) {
        super(stateManager);
        setScripts();
    }
    
    @Override
    public void createElements() {
        createInfoText();
    }
    
    private void createInfoText() {
        
        infoText = new AlertBox(getScreen(), Vector2f.ZERO) {
            @Override
            public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {
                hideWithEffect();
            }
        };
        
        infoText.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        infoText.setFont("Interface/Fonts/UnrealTournament.fnt");
        infoText.setWindowTitle("Welcome");
        infoText.setMsg("Welcome to Townyville.");
        infoText.setButtonOkText("X");
        infoText.setLockToParentBounds(true);
        infoText.setClippingLayer(infoText);
        infoText.setMinDimensions(new Vector2f(150,180));
        infoText.setIsResizable(true);
        getScreen().addElement(infoText);
        infoText.hide();
        
    }
        
    public void showAlert(String title, String text){
        infoText.showWithEffect();
        infoText.setWindowTitle(title);
        infoText.setMsg(text);
    }
    
    private void setScripts() {
        getStateManager().getApplication().getAssetManager().registerLoader(YamlLoader.class, "yml");
        scripts = (HashMap) getStateManager().getApplication().getAssetManager().loadAsset("Yaml/Alerts.yml");
        System.out.println(scripts.get("Start"));
    }
    
    public HashMap getScripts() {
        return scripts;
    }
    
}
