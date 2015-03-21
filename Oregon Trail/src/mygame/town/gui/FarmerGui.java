/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.town.TownState;
import mygame.player.wagon.WagonModel;
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
    private Player        player;
    
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
                selectedItem.addControl(player.getChaseControl().getCameraManager().getChaseCam());
                
            }
            
        };
        
        getScreen().addElement(nextButton);
        nextButton.setDimensions(getScreen().getWidth()/8, getScreen().getHeight()/10);
        nextButton.setPosition(getScreen().getWidth()/2 - nextButton.getWidth()/2  + nextButton.getWidth() * 2, getScreen().getHeight()/10);
        nextButton.hide();
        nextButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        nextButton.setFont("Interface/Fonts/UnrealTournament.fnt");
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
                player.getHud().getLeftStick().hide();
                endInteractButton.show();
                nextButton.show();
                interactButton.hide();
                buyButton.show();
                
                player.getModel().removeControl(player.getChaseControl().getCameraManager().getChaseCam());
                selectedItem.addControl(player.getChaseControl().getCameraManager().getChaseCam());
                showItemInfo();
                
            }
            
        };
        
        getScreen().addElement(interactButton);
        interactButton.setDimensions(getScreen().getWidth()/8, getScreen().getHeight()/10);
        interactButton.setPosition(getScreen().getWidth()/2 - interactButton.getWidth()/2, getScreen().getHeight()/10);
        interactButton.hide();
        getElements().add(interactButton);
        interactButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        interactButton.setText("Trade");
        interactButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        
    }
    
    private void showItemInfo() {
    
        if (itemName.equals("Hay")) {
            
            int currentHay = (Integer) player.getInventory().get("Hay");
            int money      = (Integer) player.getInventory().get("Money");
            int price      = getPrice();
            String info    = "Current Money: " + money + System.getProperty("line.separator") 
                    + " Current Hay: " + currentHay + " pounds"  + System.getProperty("line.separator") + "Current Price: " + price;
            player.getHud().showAlert("5 Pounds of Hay", info);
            
        }
                
        else if (itemName.equals("Ox")) {
                
            int currentOxen = (Integer) player.getInventory().get("Oxen");
            int money       = (Integer) player.getInventory().get("Money");
            int price       = getPrice();
            String info     = "Current Money: " + money +  System.getProperty("line.separator") + "Current Oxen: " + currentOxen
                    +  System.getProperty("line.separator") + "Current Price: " + price;
            player.getHud().showAlert("Oxen", info);
                
        }        
        
    }
    
    private int getPrice() {
    
        int price = 999;
        int miles = (Integer) player.getSituation().get("Total Distance");
        
        if (itemName.equals("Hay")) { 
            
            String biome = (String)  player.getSituation().get("Biome");
            
            price = 15;
            
            if (biome.equals("Desert")) {
                price = price + 5;
            }
  
        }
        
        else if (itemName.equals("Ox")) {
        
            String biome = (String)  player.getSituation().get("Biome");
            
            price = 100;
            
            if (biome.equals("Desert")) {
                price = price + 50;
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
                player.setNoMove(false);
                player.getHud().getLeftStick().show();
                player.getModel().addControl(player.getChaseControl().getCameraManager().getChaseCam());
                endInteractButton.hide();
                nextButton.hide();
                buyButton.hide();
                player.getHud().getInfoText().hide();
                
            }
            
        };
        
        getScreen().addElement(endInteractButton);        
        endInteractButton.setDimensions(getScreen().getWidth()/8, getScreen().getHeight()/10);
        endInteractButton.setPosition(getScreen().getWidth()/2 - endInteractButton.getWidth()/2, getScreen().getHeight()/10);
        endInteractButton.hide();
        getElements().add(endInteractButton);
        endInteractButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        endInteractButton.setText("Finish");
        endInteractButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        
    }
    
    private void createBuyButton () {
    
        buyButton = new ButtonAdapter(getScreen(), "Farmer Buy Button", new Vector2f(12,12)) {
        
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
        buyButton.setPosition(getScreen().getWidth()/2  - buyButton.getWidth() / 2 - buyButton.getWidth() * 2, getScreen().getHeight()/10f);
        buyButton.hide();
        getElements().add(buyButton);
        buyButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        buyButton.setText("Buy");
        buyButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        
    }
    
    private void buyItem() {
    
        if (itemName.equals("Hay")) {
        
            int newHay   = ((Integer) player.getInventory().get("Hay")) + 5;
            int newMoney = ((Integer) player.getInventory().get("Money")) - getPrice();
            player.getInventory().put("Hay", newHay);
            player.getInventory().put("Money", newMoney);
            
        }
        
        else {
        
            int newOxen  = ((Integer) player.getInventory().get("Oxen")) + 1;
            int newMoney = ((Integer) player.getInventory().get("Money")) - getPrice();
            player.getInventory().put("Oxen", newOxen);
            player.getInventory().put("Money", newMoney);
            Node intNode  = getStateManager().getState(TownState.class).getTownSceneManager().getInteractableNode();
            WagonModel wm = (WagonModel) intNode.getChild("Wagon");
            wm.checkOxen();
            
        }
        
        showItemInfo();
        player.saveInventory();
        
    }
    
}
