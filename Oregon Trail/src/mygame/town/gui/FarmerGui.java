/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.util.Gui;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.TextElement;
import tonegod.gui.core.Element;

/**
 *
 * @author Bawb
 */
public class FarmerGui extends Gui {
    
    private ButtonAdapter interactButton;
    private ButtonAdapter endInteractButton;
    private ButtonAdapter nextButton;
    private ButtonAdapter buyButton;
    private TextElement   infoText;
    private Node          selectedItem;
    private String        itemName;
    
    public FarmerGui(AppStateManager stateManager) {
        super(stateManager);
    }
    
    @Override
    public void createElements() {
        createInteractButton();
        createEndInteractButton();
        createNextButton();
        setSelectedItem("Ox");
    }    
    
    private void setSelectedItem(String newItem) {
        
         Node   scene  = (Node) ((SimpleApplication) getStateManager().getApplication()).getRootNode().getChild("Scene");
         Node   ox     = (Node) scene.getChild("Oxen");
         Node   hay    = (Node) scene.getChild("Hay");
         itemName      = newItem;
         
        if (newItem.equals("Ox"))
        selectedItem = ox;
        else if (newItem.equals("Hay"))
        selectedItem = (Node) hay;
        
    }        
    
    private void createNextButton() {
        
        nextButton = new ButtonAdapter(getScreen(), "Farmer Next Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                if (itemName.equals("Ox")) {
                    setSelectedItem("Hay");
                }
                
                else if (itemName.equals("Hay")) {
                    setSelectedItem("Ox");
                }
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                player.setModel(selectedItem);
                
            }
            
        };
        
        getScreen().addElement(nextButton);
        nextButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        nextButton.setPosition(getScreen().getWidth() * .75f, getScreen().getHeight()/2 - nextButton.getHeight()/2);
        nextButton.hide();
        nextButton.setText("Next");
        getElements().add(nextButton);        
    
    }
    
    public Element getInteractButton() {
        return interactButton;
    }
    
    private void createInteractButton() {
    
        interactButton = new ButtonAdapter(getScreen(), "Farmer Interact Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                player.setNoMove(true);
                player.setModel(selectedItem);
                endInteractButton.show();
                nextButton.show();
                interactButton.hide();
                
            }
            
        };
        
        getScreen().addElement(interactButton);
        interactButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        interactButton.setPosition(getScreen().getWidth()/2 - interactButton.getWidth()/2, getScreen().getHeight()/10);
        interactButton.hide();
        interactButton.setText("Trade");
        getElements().add(interactButton);
        
    }
    
    private void createEndInteractButton() {
    
        endInteractButton = new ButtonAdapter(getScreen(), "Farmer End Interact Butotn", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                Node   model  = (Node) ((SimpleApplication) getStateManager().getApplication()).getRootNode().getChild("Player");
                player.setNoMove(false);
                player.setModel((Node)model.getChild(0));
                endInteractButton.hide();
                nextButton.hide();
                
            }
            
        };
        
        getScreen().addElement(endInteractButton);        
        endInteractButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        endInteractButton.setPosition(getScreen().getWidth()/2 - endInteractButton.getWidth()/2, getScreen().getHeight()/10);
        endInteractButton.hide();
        endInteractButton.setText("Finish");
        getElements().add(endInteractButton);
        
    }
    
}
