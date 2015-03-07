/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.town.WagonModel;
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
                
                PlayerManager  pm = app.getStateManager().getState(PlayerManager.class);
                
                if(pm.getPlayer().getIsDead())
                pm.endGame();
                
                try {
                    
                  Node a        = (Node) ((SimpleApplication) app).getRootNode().getChild(0);
                  Node intNode  = (Node)       a.getChild("Interactable");
                  WagonModel wm = (WagonModel) intNode.getChild("Wagon");
                  
                  if(wm.inProx()) {
                      ((WagonGui)wm.getGui()).getMoveButton().show();
                      ((WagonGui)wm.getGui()).getSituationButton().show();
                      ((WagonGui)wm.getGui()).getSuppliesButton().show();
                  }
                  
                }
                
                catch (Exception e){
                }
                
            }
            
        };
        
        infoText.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        infoText.getButtonOk().setFont("Interface/Impact.fnt");
        infoText.setWindowTitle("Welcome");
        infoText.setMsg("Welcome to Townyville.");
        infoText.setButtonOkText("Ok");
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
    }
    
    public HashMap getScripts() {
        return scripts;
    }
    
    public AlertBox getInfoText(){
        return infoText;
    }
    
}
