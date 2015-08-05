/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.river.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.util.Gui;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Element;

/**
 *
 * @author Bawb
 */
public class IndianGuideGui extends Gui {

    private ButtonAdapter interactButton;
    private ButtonAdapter yesButton;
    private ButtonAdapter noButton;
    private Node          selectedItem;
    
    public IndianGuideGui(AppStateManager stateManager) {
        super(stateManager);
    }
    
    @Override
    public void createElements() {
        createInteractButton();
        createNoButton();
        createYesButton();
        setSelectedItem();
    }
    
    private void setSelectedItem() {
        
         Node   scene  = (Node) ((SimpleApplication) getStateManager().getApplication()).getRootNode().getChild("River");
         selectedItem  = (Node) scene.getChild("Indian Spot");
         
    }
    
    private void showInfo() {
        Player player = getStateManager().getState(PlayerManager.class).getPlayer();
        player.getHud().showAlert("Guide", "The Indian Guide claims he can ford the river for 10$" + System.getProperty("line.separator")+ "Current Money: $" + player.getInventory().get("Money"));
    }
    
    private void createYesButton() {
        
        yesButton = new ButtonAdapter(getScreen(), "Indian Guide Yes Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                selectedItem.addControl(player.getChaseControl().getCameraManager().getChaseCam());
                
            }
            
        };
        
        getScreen().addElement(yesButton);
        yesButton.setDimensions(getScreen().getWidth()/8, getScreen().getHeight()/10);
        yesButton.setPosition(getScreen().getWidth()/2 - yesButton.getWidth()/2  + yesButton.getWidth() * 2, getScreen().getHeight()/10);
        yesButton.hide();
        yesButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        yesButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        yesButton.setText("Yes");
        getElements().add(yesButton); 
    
    }    
    
    public ButtonAdapter getYesButton() {
        return yesButton;
    }
    
    private void createNoButton() {
    
        noButton = new ButtonAdapter(getScreen(), "Indian Guide No Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                player.setNoMove(false);
                player.getHud().getLeftStick().show();
                player.getModel().addControl(player.getChaseControl().getCameraManager().getChaseCam());
                noButton.hide();
                yesButton.hide();
                player.getHud().getInfoText().hide();
                
            }
            
        };
        
        getScreen().addElement(noButton);        
        noButton.setDimensions(getScreen().getWidth()/8, getScreen().getHeight()/10);
        noButton.setPosition(getScreen().getWidth()/2 - noButton.getWidth()/2 - noButton.getWidth() * 2, getScreen().getHeight()/10);
        noButton.hide();
        getElements().add(noButton);
        noButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        noButton.setText("No");
        noButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        
    }
    
    public ButtonAdapter getNoButton() {
        return noButton;
    }
    
    private void createInteractButton() {
    
        interactButton = new ButtonAdapter(getScreen(), "Indian Guide Interact Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                player.setNoMove(true);
                player.getHud().getLeftStick().hide();
                yesButton.show();
                noButton.show();
                interactButton.hide();
                
                player.getModel().removeControl(player.getChaseControl().getCameraManager().getChaseCam());
                selectedItem.addControl(player.getChaseControl().getCameraManager().getChaseCam());
                showInfo();
                
            }
            
        };
        
        getScreen().addElement(interactButton);
        interactButton.setDimensions(getScreen().getWidth()/8, getScreen().getHeight()/10);
        interactButton.setPosition(getScreen().getWidth()/2 - interactButton.getWidth()/2, getScreen().getHeight()/10);
        interactButton.hide();
        getElements().add(interactButton);
        interactButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        interactButton.setText("Talk");
        interactButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        
    }
    
    public Element getInteractButton() {
        return interactButton;
    }
    
}
