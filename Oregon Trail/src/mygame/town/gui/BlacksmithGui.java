/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town.gui;

import mygame.util.Gui;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.font.LineWrapMode;
import com.jme3.input.ChaseCamera;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.player.wagon.Wagon;
import mygame.town.TownState;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.core.Element;

/**
 *
 * @author Bawb
 */
public class BlacksmithGui extends Gui {
    
    private ButtonAdapter interactButton;
    private ButtonAdapter endInteractButton;
    private Indicator     wagonHealth;
    private ButtonAdapter nextButton;
    private String        itemName;
    private ButtonAdapter buyButton;
    private Player        player;
    
    public BlacksmithGui(AppStateManager stateManager) {
        super(stateManager);
        setSelectedItem("Fix");
        player = getStateManager().getState(PlayerManager.class).getPlayer();
    }
    
    private void setSelectedItem(String newItem) {
        
        itemName = newItem; 
        if (newItem.equals("Fix")){}
        else{}
        
    }        
    
    @Override
    public void createElements() {
        createInteractButton();
        createEndInteractButton();
        createWagonHealth();
        createNextButton();
        createBuyButton();
    }
    
    private void createNextButton() {
        
        itemName = "Fix";
        nextButton = new ButtonAdapter(getScreen(), "Blacksmith Next Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                if (itemName.equals("Fix")) {
                    setSelectedItem("Upgrade");
                    buyButton.setText("Boost");
                    showItemInfo();
                }
                
                else if (itemName.equals("Upgrade")) {
                    setSelectedItem("Fix");
                    buyButton.setText("Fix");
                    showItemInfo();
                }
                
            }
            
        };
        
        getScreen().addElement(nextButton);
        nextButton.setDimensions(getScreen().getWidth()/8, getScreen().getHeight()/10);
        nextButton.setPosition(getScreen().getWidth()/2 - nextButton.getWidth()/2  + nextButton.getWidth() * 2, getScreen().getHeight()/10);
        nextButton.hide();
        nextButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        nextButton.setText("Next");
        getElements().add(nextButton);
        nextButton.setFont("Interface/Fonts/UnrealTournament.fnt");
    
    }        
    
    public Element getInteractButton() {
        return interactButton;
    }
    
    private void createInteractButton() {
    
        interactButton = new ButtonAdapter(getScreen(), "Blacksmith Interact Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                Node   inter  = (Node) ((SimpleApplication) getStateManager().getApplication()).getRootNode().getChild("Interactable");
                Node   wagon  = (Node) inter.getChild("Wagon");
                player.getHud().getLeftStick().hide();
                player.setNoMove(true);
                player.getModel().removeControl(player.getChaseControl().getCameraManager().getChaseCam());
                wagon.addControl(player.getChaseControl().getCameraManager().getChaseCam());
                endInteractButton.show();
                wagonHealth.show();
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
    
        endInteractButton = new ButtonAdapter(getScreen(), "Blacksmith End Interact Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                player.setNoMove(false);
                player.getModel().addControl(player.getChaseControl().getCameraManager().getChaseCam());
                player.getHud().getLeftStick().show();
                endInteractButton.hide();
                wagonHealth.hide();
                nextButton.hide();
                buyButton.hide();
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
    
    private void createWagonHealth() {
    
        wagonHealth = new Indicator(getScreen(), "Wagon Health", new Vector2f(12,12), Indicator.Orientation.HORIZONTAL) {
        
            @Override
            public void onChange(float currentValue, float currentPercentage) {
            }
        
        };
        
        getScreen().addElement(wagonHealth);
        wagonHealth.setIndicatorColor(ColorRGBA.Red);
        wagonHealth.setTextWrap(LineWrapMode.NoWrap);
        wagonHealth.setTextVAlign(VAlign.Center);
        wagonHealth.setTextAlign(Align.Center);
        wagonHealth.setBaseImage(getScreen().getStyle("Window").getString("defaultImg"));
        wagonHealth.setIndicatorPadding(new Vector4f(7,7,7,7));
        wagonHealth.setX(getScreen().getWidth()/2 - wagonHealth.getWidth()/2);
        wagonHealth.setY(getScreen().getHeight()/10 + wagonHealth.getHeight()*3);
        wagonHealth.hide();
        setWagonHealth();
        
    }
    
    private void setWagonHealth() {
        Wagon wagon = getStateManager().getState(PlayerManager.class).getPlayer().getWagon();
        wagonHealth.setMaxValue(wagon.getMaxHealth());
        wagonHealth.setCurrentValue(wagon.getCurrentHealth());
        wagonHealth.getTextDisplayElement().setText(wagon.getCurrentHealth() + " / " + wagon.getMaxHealth());
    }
    
    private void createBuyButton () {
    
        buyButton = new ButtonAdapter(getScreen(), "Blacksmith Buy Button", new Vector2f(12,12)) {
        
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
        buyButton.setText(itemName);
        getElements().add(buyButton);
        buyButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        
    }
    
    private void showItemInfo() {
    
        if (itemName.equals("Upgrade")) {
            
            int currentMaxHealth = (Integer) player.getWagon().getMaxHealth();
            int money            = (Integer) player.getInventory().get("Money");
            int price            = getPrice();
            String info          = "Current Money: " + money + System.getProperty("line.separator") 
                    + " Current Max Health: " + currentMaxHealth + System.getProperty("line.separator") + "Current Cost to Upgrade: " + price;
            player.getHud().showAlert("Upgrade", info);
            
        }
                
        else if (itemName.equals("Fix")) {
                
            int currentHealth = (Integer) player.getWagon().getCurrentHealth();
            int money         = (Integer) player.getInventory().get("Money");
            int price         = getPrice();
            
            String info;
            
            if (player.getWagon().getMaxHealth() - player.getWagon().getCurrentHealth() == 0) {
            
                info = "Your wagon is currently at full health;";
                
            }
            
            else {
                
                info = "Current Money: " + money +  System.getProperty("line.separator") + "Current Health: " + currentHealth
                    +  System.getProperty("line.separator") + "Current Price: " + price;
            
            }
            
            player.getHud().showAlert("Fix", info);
                
        }        
        
    }
    
    private int getPrice() {
    
        int price = 999;
        int miles = (Integer) player.getSituation().get("Total Distance");
        
        if (itemName.equals("Fix")) { 
            
            String biome = (String)  player.getSituation().get("Biome");
            
            price = 10;
            int repairNeed = player.getWagon().getMaxHealth() - player.getWagon().getCurrentHealth();
            
            if (repairNeed < 10) {
                price = repairNeed;
            }
            
            if (biome.equals("Desert")) {
                price = price +10;
            }
  
        }
        
        else if (itemName.equals("Upgrade")) {
        
            String biome = (String)  player.getSituation().get("Biome");
            
            price = 50;
            
            float upCount = (player.getWagon().getMaxHealth() - 20)/10;
            
             if (upCount == 1) {
                 price = price + 25;
             }
            
            else if (upCount == 2) {
                price = price + 50;
            }
            
            else if (upCount == 3) {
                price = price + 75;
            }            
            
            else if (upCount == 4) {
                price = price + 100;
            }
             
            else if (upCount == 5) {
                price = price + 125;
            }                 
             
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
    
    private void buyItem() {
    
        if (itemName.equals("Upgrade")) {
        
            int newUpgrade   = player.getWagon().getMaxHealth() + 10;
            int newMoney = ((Integer) player.getInventory().get("Money")) - getPrice();
            player.getWagon().setMaxHealth(newUpgrade);
            player.getWagon().setCurrentHealth(newUpgrade);
            player.getInventory().put("Money", newMoney);
            
        }
        
        else {
            
            int repairNeed = player.getWagon().getMaxHealth() - player.getWagon().getCurrentHealth();
            int newFix;
            
            if (repairNeed < 10)
            newFix       = (Integer) player.getWagon().getCurrentHealth() + repairNeed;
            else
            newFix       = (Integer) player.getWagon().getCurrentHealth() + 10;
            
            int newMoney     = ((Integer) player.getInventory().get("Money")) - getPrice();
            player.getWagon().setCurrentHealth(newFix);
            player.getInventory().put("Money", newMoney);
            
        }
        
        showItemInfo();
        player.getWagon().save(getStateManager(), player.getFilePath());
        setWagonHealth();
        
    }    
    
}
