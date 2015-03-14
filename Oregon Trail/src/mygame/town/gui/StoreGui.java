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
    private Player        player;
    
    public StoreGui(AppStateManager stateManager) {
        super(stateManager);
        player = getStateManager().getState(PlayerManager.class).getPlayer();
    } 
    
    @Override
    public void createElements(){
        createInteractButton();
        createEndInteractButton();
        setSelectedItem("Food");
        createNextButton();
        createBuyButton();
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
                
                showItemInfo();
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                player.setModel((Node)selectedItem.getChild(0));
                
            }
            
        };
        
        getScreen().addElement(nextButton);
        nextButton.setDimensions(getScreen().getWidth()/8, getScreen().getHeight()/10);
        nextButton.setPosition(getScreen().getWidth()/2 - nextButton.getWidth()/2 + nextButton.getWidth() * 2, getScreen().getHeight()/10);
        nextButton.hide();
        getElements().add(nextButton);
        nextButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        nextButton.setText("Next");
        nextButton.setFont("Interface/Fonts/UnrealTournament.fnt");
    
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
                player.getHud().getJoystick().hide();
                endInteractButton.show();
                interactButton.hide();
                nextButton.show();
                buyButton.show();
                showItemInfo();
                
            }
            
        };
        
        getScreen().addElement(interactButton);
        interactButton.setDimensions(getScreen().getWidth()/8, getScreen().getHeight()/10);
        interactButton.setPosition(getScreen().getWidth()/2 - interactButton.getWidth()/2, getScreen().getHeight()/10);
        interactButton.hide();
        interactButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        interactButton.setText("Trade");
        getElements().add(interactButton);
        interactButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        
    }
    
    private void createEndInteractButton() {
    
        endInteractButton = new ButtonAdapter(getScreen(), "Store End Interact Butotn", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                Node   model  = (Node) ((SimpleApplication) getStateManager().getApplication()).getRootNode().getChild("Player");
                player.setNoMove(false);
                player.setModel((Node)model.getChild(0));
                player.getHud().getJoystick().show();
                endInteractButton.hide();
                nextButton.hide();
                buyButton.hide();
                player.getModel().scale(10f);
                player.getHud().getInfoText().hide();
                
            }
            
        };
        
        getScreen().addElement(endInteractButton);        
        endInteractButton.setDimensions(getScreen().getWidth()/8, getScreen().getHeight()/10);
        endInteractButton.setPosition(getScreen().getWidth()/2 - endInteractButton.getWidth()/2, getScreen().getHeight()/10);
        endInteractButton.hide();
        endInteractButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        endInteractButton.setText("Finish");
        getElements().add(endInteractButton);
        endInteractButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        
    }
    
    private void createBuyButton () {
    
        buyButton = new ButtonAdapter(getScreen(), "Store Buy Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                int money = (Integer) player.getInventory().get("Money");
                if (money >= getPrice())
                buyItem();
                else
                player.getHud().showAlert("Money", "You don't have enough money!");
                
            }
            
        };
        
        getScreen().addElement(buyButton);        
        buyButton.setDimensions(getScreen().getWidth()/8, getScreen().getHeight()/10);
        buyButton.setPosition(getScreen().getWidth()/2 - buyButton.getWidth()/2 - buyButton.getWidth() * 2, getScreen().getHeight()/10);
        buyButton.hide();
        buyButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        buyButton.setText("Buy");
        getElements().add(buyButton);
        buyButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        
    }
    
    private void showItemInfo() {
    
        if (itemName.equals("Tools")) {
            
            int currentTools = (Integer) player.getInventory().get("Tools");
            int money      = (Integer) player.getInventory().get("Money");
            int price      = getPrice();
            String info    = "Current Money: " + money + System.getProperty("line.separator") 
                    + " Current Tools: " + currentTools + System.getProperty("line.separator") + "Current Price: " + price;
            player.getHud().showAlert("Tools", info);
            
        }
                
        else if (itemName.equals("Food")) {
                
            int currentFood = (Integer) player.getInventory().get("Food");
            int money       = (Integer) player.getInventory().get("Money");
            int price       = getPrice();
            String info     = "Current Money: " + money +  System.getProperty("line.separator") + "Current Food: " + currentFood
                  + " pounds" +  System.getProperty("line.separator") + "Current Price: " + price;
            player.getHud().showAlert("5 Pounds of Food", info);
                
        }
        
        else if (itemName.equals("Bullets")) {
                
            int currentBullets = (Integer) player.getInventory().get("Bullets");
            int money       = (Integer) player.getInventory().get("Money");
            int price       = getPrice();
            String info     = "Current Money: " + money +  System.getProperty("line.separator") + "Current Bullets: " + currentBullets
                    +  System.getProperty("line.separator") + "Current Price: " + price;
            player.getHud().showAlert("5 Bullets", info);
                
        }               
        
    }
    
    private int getPrice() {
    
        int price = 999;
        int miles = (Integer) player.getSituation().get("Total Distance");
        
        if (itemName.equals("Tools")) { 
            
            String biome = (String)  player.getSituation().get("Biome");
            
            price = 100;
            
            if (biome.equals("Desert")) {
                price = price + 50;
            }
  
        }
        
        else if (itemName.equals("Food")) {
        
            String biome = (String)  player.getSituation().get("Biome");
            
            price = 10;
            
            if (biome.equals("Desert")) {
                price = price + 3;
            }
            
        }
        
        else if (itemName.equals("Bullets")) {
        
            String biome = (String)  player.getSituation().get("Biome");
            
            price = 20;
            
            if (biome.equals("Desert")) {
                price = price + 5;
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
    
    private void buyItem() {
    
        if (itemName.equals("Food")) {
        
            int newFood  = ((Integer) player.getInventory().get("Food")) + 5;
            int newMoney = ((Integer) player.getInventory().get("Money")) - getPrice();
            player.getInventory().put("Food", newFood);
            player.getInventory().put("Money", newMoney);
            
        }
        
        else if (itemName.equals("Bullets")) {
        
            int newBullets   = ((Integer) player.getInventory().get("Bullets")) + 5;
            int newMoney     = ((Integer) player.getInventory().get("Money")) - getPrice();
            player.getInventory().put("Bullets", newBullets);
            player.getInventory().put("Money", newMoney);
            
        }        
        
        else {
        
            int newTools = ((Integer) player.getInventory().get("Tools")) + 1;
            int newMoney = ((Integer) player.getInventory().get("Money")) - getPrice();
            player.getInventory().put("Tools", newTools);
            player.getInventory().put("Money", newMoney);
            
        }
        
        showItemInfo();
        player.saveInventory();
        
    }    
    
}
