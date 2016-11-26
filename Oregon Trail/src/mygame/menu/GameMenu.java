/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.menu;

import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import mygame.player.Hud;
import mygame.player.PlayerManager;
import mygame.util.Gui;
import tonegod.gui.controls.buttons.ButtonAdapter;

/**
 *
 * @author Bawb
 */
public class GameMenu extends Gui {
    
    private ButtonAdapter restartButton;
    private ButtonAdapter yesButton;
    private ButtonAdapter noButton;
    
    public GameMenu(AppStateManager stateManager) {
        super(stateManager);
    }
    
    @Override
    public void createElements() {
        createRestartButton();
        createYesButton();
        createNoButton();
    }
    
    private void createRestartButton() {
        
        restartButton = new ButtonAdapter(getScreen(), "Restart Button", new Vector2f(12,12)) {
            
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                yesButton.show();
                noButton.show();
                restartButton.hide();
                Hud hud = getStateManager().getState(PlayerManager.class).getPlayer().getHud();
                hud.showAlert("Restart Game", "Are you sure you want to restart?");
                hud.getInfoText().getButtonOk().hide();
                
            }
        };
              
        restartButton.setDimensions(getScreen().getWidth()/5, getScreen().getHeight()/10);
        restartButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        getElements().add(restartButton);
        restartButton.setFont("Interface/Fonts/UnrealTournament.fnt");     
        restartButton.setPosition(0, getScreen().getHeight()-restartButton.getHeight());
        restartButton.setText("Restart");        
        
    }
    
    private void createYesButton() {
        
        yesButton = new ButtonAdapter(getScreen(), "Yes Button", new Vector2f(12,12)) {
            
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Hud hud = getStateManager().getState(PlayerManager.class).getPlayer().getHud();
                hud.getMenuButton().setText("Menu");
                getStateManager().getState(PlayerManager.class).endGame();
                removeElements();
                
            }
        };
              
        yesButton.setDimensions(getScreen().getWidth()/5, getScreen().getHeight()/10);
        yesButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        yesButton.setFont("Interface/Fonts/UnrealTournament.fnt");     
        yesButton.setText("Yes");        
        
    }

    private void createNoButton() {
    
        noButton = new ButtonAdapter(getScreen(), "No Button", new Vector2f(12,12)) {
            
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                yesButton.hide();
                noButton.hide();
                restartButton.show();
                Hud hud = getStateManager().getState(PlayerManager.class).getPlayer().getHud();
                hud.getInfoText().getButtonOk().show();
                hud.getInfoText().hide();
                
            }
        }; 
           
        noButton.setDimensions(getScreen().getWidth()/5, getScreen().getHeight()/10);
        noButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        getElements().add(noButton);
        noButton.setFont("Interface/Fonts/UnrealTournament.fnt");     
        noButton.setText("No");        
        
    }
    
    public void addElements() {
        getScreen().addElement(restartButton);
        getScreen().addElement(yesButton);
        getScreen().addElement(noButton);
        restartButton.setPosition(getScreen().getWidth()/2 - restartButton.getWidth()/2, getScreen().getHeight()/2 - restartButton.getHeight()/2 + restartButton.getHeight()*2);
        yesButton.setPosition(getScreen().getWidth()/2 - yesButton.getWidth()/2, getScreen().getHeight()/2 - yesButton.getHeight()/2 - yesButton.getHeight());
        noButton.setPosition(getScreen().getWidth()/2 - noButton.getWidth()/2, getScreen().getHeight()/2 - noButton.getHeight()/2 - noButton.getHeight()*3);
        yesButton.hide();
        noButton.hide();
    }
    
    public void removeElements() {
        getScreen().removeElement(restartButton);
        getScreen().removeElement(yesButton);
        getScreen().removeElement(noButton);
    }
    
}
