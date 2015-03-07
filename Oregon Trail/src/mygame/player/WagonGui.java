/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import java.util.HashMap;
import mygame.GameManager;
import mygame.util.Gui;
import tonegod.gui.controls.buttons.ButtonAdapter;

/**
 *
 * @author Bawb
 */
public class WagonGui extends Gui {

    private ButtonAdapter moveButton;
    private ButtonAdapter situationButton;
    private ButtonAdapter suppliesButton;
    private ButtonAdapter stopButton;
    
    public WagonGui(AppStateManager stateManager) {
        super(stateManager);
    }    
    
    @Override
    public void createElements() {
        createStopButton();
        createMoveButton();
        createSituationButton();
        createSuppliesButton();
    }
    
    private void createStopButton() {
    
        stopButton = new ButtonAdapter(getScreen(), "Stop Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player  = getStateManager().getState(PlayerManager.class).getPlayer();
                player.setInWagon(false);
                player.setLocalScale(1f);
                stopButton.hide();
                
            }
        
        };        
        
        getScreen().addElement(stopButton);        
        stopButton.setDimensions(getScreen().getWidth()/5, getScreen().getHeight()/10);
        stopButton.setPosition(getScreen().getWidth()/2 - stopButton.getWidth()/2, getScreen().getHeight()/10);
        stopButton.hide();
        stopButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        stopButton.setText("Stop");
        getElements().add(stopButton);
        stopButton.setFont("Interface/Fonts/UnrealTournament.fnt");         
        
    }
    
    private void createMoveButton() {
    
        moveButton = new ButtonAdapter(getScreen(), "Move Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player  = getStateManager().getState(PlayerManager.class).getPlayer();
                
                if(player.getSituation().get("Setting").equals("Town")) {
                    getStateManager().getState(GameManager.class).initTrail();
                    moveButton.hide();
                    suppliesButton.hide();
                    situationButton.hide();
                    player.getSituation().put("Setting", "Trail");
                }
                
                else if(player.getSituation().get("Setting").equals("Trail")) {
                
                    moveButton.hide();
                    suppliesButton.hide();
                    situationButton.hide();
                    player.setInWagon(true);
                    player.setLocalScale(.1f);
                    stopButton.show();
                    
                }
            
            }
        
        };
        
        getScreen().addElement(moveButton);        
        moveButton.setDimensions(getScreen().getWidth()/3, getScreen().getHeight()/10);
        moveButton.setPosition(getScreen().getWidth()/2 - moveButton.getWidth()/2, getScreen().getHeight()/2 + moveButton.getHeight()*2);
        moveButton.hide();
        moveButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        moveButton.setText("Hit the Trail");
        getElements().add(moveButton);
        moveButton.setFont("Interface/Fonts/UnrealTournament.fnt");          
        
    }
    
    private void createSituationButton() {
        
        situationButton = new ButtonAdapter(getScreen(), "Situaton Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player  = getStateManager().getState(PlayerManager.class).getPlayer();
                String setting = (String) player.getSituation().get("Setting");
                
                int day           = (Integer) player.getSituation().get("Day Number")-1;
                int milesTraveled = (Integer) player.getSituation().get("Total Distance");
                String setName    = (String)  player.getSituation().get("Setting Name");
                int    goalCount  = (Integer) player.getSituation().get("Goals Reached");
                
                HashMap goals     = (HashMap) getStateManager().getApplication().getAssetManager().loadAsset("Yaml/Goals.yml");
                HashMap goalMap   = (HashMap) goals.get(goalCount+1);
                
                String goalName   = (String) goalMap.get("Name");
                int goalDist      = ((Integer) goalMap.get("At")) - milesTraveled;
                
                String setInfo;
                
                if(setting.equals("Town"))
                setInfo = "in the " + setting + " of " + setName;
                else
                setInfo = "on the trail to "  +goalName;
                
                String info = "You are " + setInfo +  "." + System.getProperty("line.separator")
                              + "You have traveled a total of " + milesTraveled + " miles in " + day + " days." + System.getProperty("line.separator")
                              + "Your next destination is " + goalName + " which is " + goalDist + " miles away." + System.getProperty("line.separator");
                
                player.getHud().showAlert("Situation", info);
                
            }
            
        };
             
        getScreen().addElement(situationButton);        
        situationButton.setDimensions(getScreen().getWidth()/3, getScreen().getHeight()/10);
        situationButton.setPosition(getScreen().getWidth()/2 - situationButton.getWidth()/2, getScreen().getHeight()/2);
        situationButton.hide();
        situationButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        situationButton.setText("Check Situation");
        getElements().add(situationButton);
        situationButton.setFont("Interface/Fonts/UnrealTournament.fnt");        
        
    }
    
    private void createSuppliesButton() {
    
        suppliesButton = new ButtonAdapter(getScreen(), "Supplies Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player  = getStateManager().getState(PlayerManager.class).getPlayer();
                int    hay     = (Integer) player.getInventory().get("Hay");
                int    oxen    = (Integer) player.getInventory().get("Oxen");
                int    food    = (Integer) player.getInventory().get("Food");
                int    tools   = (Integer) player.getInventory().get("Tools");
                int    bullets = (Integer) player.getInventory().get("Bullets");
                
                String hayInfo    = "Current Hay: " + hay + " pounds" + System.getProperty("line.separator");
                String oxInfo     = "Oxen: " + oxen + System.getProperty("line.separator");
                String foodInfo   = "Current Food: " + food + " pounds" + System.getProperty("line.separator");
                String toolInfo   = "Current Tools: " + tools + System.getProperty("line.separator");
                String bulletInfo = "Current Bullets: " + bullets + " rounds";
                
                String info = hayInfo + oxInfo + foodInfo + toolInfo + bulletInfo;
                player.getHud().showAlert("Supplies", info);
        
            }
            
        };
        
        getScreen().addElement(suppliesButton);        
        suppliesButton.setDimensions(getScreen().getWidth()/3, getScreen().getHeight()/10);
        suppliesButton.setPosition(getScreen().getWidth()/2 - suppliesButton.getWidth()/2, getScreen().getHeight()/2 - suppliesButton.getHeight()*2);
        suppliesButton.hide();
        suppliesButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        suppliesButton.setText("Check Supplies");
        getElements().add(suppliesButton);
        suppliesButton.setFont("Interface/Fonts/UnrealTournament.fnt");            
        
    }
    
    public ButtonAdapter getMoveButton() {
        return moveButton;
    }
    
    public ButtonAdapter getSituationButton() {
        return situationButton;
    }    
    
    public ButtonAdapter getSuppliesButton() {
        return suppliesButton;
    }
    
    public ButtonAdapter getStopButton() {
        return stopButton;
    }
    
}
