/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town.gui;

import mygame.util.Gui;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import mygame.player.Player;
import mygame.player.PlayerManager;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.TextElement;
import tonegod.gui.core.Element;

/**
 *
 * @author Bawb
 */
public class StoreGui extends Gui {
    
    private ButtonAdapter interactButton;
    private ButtonAdapter endInteractButton;
    private Node          selectedItem;
    private ButtonAdapter nextButton;
    private String        itemName;
    private ButtonAdapter buyButton;
    private TextElement   infoText;
    
    public StoreGui(AppStateManager stateManager) {
        super(stateManager);
    } 
    
    @Override
    public void createElements(){
        createInteractButton();
        createEndInteractButton();
        setSelectedItem("Food");
        createNextButton();
    }
    
    private void setSelectedItem(String newItem) {
        
         Node   scene  = (Node) ((SimpleApplication) getStateManager().getApplication()).getRootNode().getChild("Scene");
         Node   items  = (Node) scene.getChild("Items");
         itemName = newItem;
         
        if (newItem.equals("Food"))
        selectedItem = (Node) items.getChild(0);
        else if (newItem.equals("Bullets"))
        selectedItem = (Node) items.getChild(1);
        else
        selectedItem = (Node) items.getChild(2);
        
    }    
    
    private void createNextButton() {
        
        nextButton = new ButtonAdapter(getScreen(), "Store Next Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                if (itemName.equals("Food")) {
                    setSelectedItem("Tools");
                }
                
                else if (itemName.equals("Tools")) {
                    setSelectedItem("Bullets");
                }
                
                else if (itemName.equals("Bullets")) {
                    setSelectedItem("Food");
                }
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                player.setModel((Node)selectedItem.getChild(0));
                
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
    
        interactButton = new ButtonAdapter(getScreen(), "Store Interact Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                player.setNoMove(true);
                player.getModel().scale(.1f);
                player.setModel((Node)selectedItem.getChild(0));
                endInteractButton.show();
                interactButton.hide();
                nextButton.show();
                
            }
            
        };
        
        getScreen().addElement(interactButton);
        interactButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        interactButton.setPosition(getScreen().getWidth()/2 - interactButton.getWidth()/2, getScreen().getHeight()/10);
        interactButton.setText("Trade");
        interactButton.hide();
        getElements().add(interactButton);
        
    }
    
    private void createEndInteractButton() {
    
        endInteractButton = new ButtonAdapter(getScreen(), "Store End Interact Butotn", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                Node   model  = (Node) ((SimpleApplication) getStateManager().getApplication()).getRootNode().getChild("Player");
                player.setNoMove(false);
                player.setModel((Node)model.getChild(0));
                endInteractButton.hide();
                nextButton.hide();
                player.getModel().scale(10f);
                
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
