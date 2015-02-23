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
import mygame.player.Hud;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.util.Gui;
import tonegod.gui.controls.buttons.ButtonAdapter;
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
    private Node          selectedItem;
    private String        itemName;
    private Player           player;
    
    public FarmerGui(AppStateManager stateManager) {
        super(stateManager);
        player = getStateManager().getState(PlayerManager.class).getPlayer();
    }
    
    @Override
    public void createElements() {
        createInteractButton();
        createEndInteractButton();
        createNextButton();
        setSelectedItem("Ox");
        createBuyButton();
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
                
                showItemInfo();
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                player.setModel(selectedItem);
                
            }
            
        };
        
        getScreen().addElement(nextButton);
        nextButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        nextButton.setPosition(getScreen().getWidth()/2 - nextButton.getWidth()/2  + nextButton.getWidth() * 2, getScreen().getHeight()/10);
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
                endInteractButton.show();
                nextButton.show();
                interactButton.hide();
                buyButton.show();
                
                player.setModel(selectedItem);
                showItemInfo();
                
            }
            
        };
        
        getScreen().addElement(interactButton);
        interactButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        interactButton.setPosition(getScreen().getWidth()/2 - interactButton.getWidth()/2, getScreen().getHeight()/10);
        interactButton.hide();
        interactButton.setText("Trade");
        getElements().add(interactButton);
        
    }
    
    private void showItemInfo() {
    
        if (itemName.equals("Hay")) {
            
            int currentHay = (Integer) player.getInventory().get("Hay");
            int money      = (Integer) player.getInventory().get("Money");
            int price      = priceCalculator();
            String info    = "Current Money: " + money + System.getProperty("line.separator") 
                    + " Current Hay: " + currentHay + " pounds"  + System.getProperty("line.separator") + "Current Price: " + price;
            player.getHud().showAlert("Hay", info);
            
            }
                
            else if (itemName.equals("Ox")) {
                
                int currentOxen = (Integer) player.getInventory().get("Oxen");
                int money       = (Integer) player.getInventory().get("Money");
                int price       = priceCalculator();
                String info     = "Current Money: " + money +  System.getProperty("line.separator") + "Current Oxen: " + currentOxen
                        +  System.getProperty("line.separator") + "Current Price: " + price;
                player.getHud().showAlert("Oxen", info);
                
            }        
        
    }
    
    private int priceCalculator() {
    
        int price = 999;
        int miles = (Integer) player.getSituation().get("Total Distance");
        
        if (itemName.equals("Hay")) { 
            
            String biome = (String)  player.getSituation().get("Biome");
            
            price = 10;
            
            if (biome.equals("Desert")) {
                price = price + 50;
            }
  
        }
        
        else if (itemName.equals("Ox")) {
        
            String biome = (String)  player.getSituation().get("Biome");
            
            price = 100;
            
            if (biome.equals("Desert")) {
                price = price + 100;
            }
            
        }
        
        if (miles > 100) {
            price = Math.round(price + price * .1f);
        }
        
        else if (miles > 500) {
            price = Math.round(price + price * .2f);
        }
        
        else if (miles > 1000) {
            price = Math.round(price + price * .3f);
        }
            
        else if (miles > 1500) {
            price = Math.round(price + price * .4f);
        }    
        
        else if (miles > 2000) {
            price = Math.round(price + price * .5f);
        }
        
        return price;
        
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
                buyButton.hide();
                
            }
            
        };
        
        getScreen().addElement(endInteractButton);        
        endInteractButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        endInteractButton.setPosition(getScreen().getWidth()/2 - endInteractButton.getWidth()/2, getScreen().getHeight()/10);
        endInteractButton.hide();
        endInteractButton.setText("Finish");
        getElements().add(endInteractButton);
        
    }
    
    private void createBuyButton () {
    
        buyButton = new ButtonAdapter(getScreen(), "Farmer Buy Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                
                
            }
            
        };
        
        getScreen().addElement(buyButton);        
        buyButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        buyButton.setPosition(getScreen().getWidth()/2  - buyButton.getWidth() / 2 - buyButton.getWidth() * 2, getScreen().getHeight()/10f);
        buyButton.hide();
        buyButton.setText("Buy");
        getElements().add(buyButton);
        
    }
    
}
