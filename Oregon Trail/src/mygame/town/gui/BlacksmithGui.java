/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.town.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont.Align;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.player.Wagon;
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
    
    public BlacksmithGui(AppStateManager stateManager) {
        super(stateManager);
    }
    
    @Override
    public void createElements(){
        createInteractButton();
        createEndInteractButton();
        createWagonHealth();
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
                player.setNoMove(true);
                player.setModel(wagon);
                endInteractButton.show();
                wagonHealth.show();
                interactButton.hide();
                
            }
            
        };
        
        getScreen().addElement(interactButton);
        interactButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        interactButton.setPosition(getScreen().getWidth()/2 - interactButton.getWidth()/2, getScreen().getHeight()/5);
        interactButton.hide();
        getElements().add(interactButton);
        
    }
    
    private void createEndInteractButton() {
    
        endInteractButton = new ButtonAdapter(getScreen(), "Blacksmith End Interact Butotn", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                Node   model  = (Node) ((SimpleApplication) getStateManager().getApplication()).getRootNode().getChild("Player");
                player.setNoMove(false);
                player.setModel((Node)model.getChild(0));
                endInteractButton.hide();
                wagonHealth.hide();
                
            }
            
        };
        
        getScreen().addElement(endInteractButton);        
        endInteractButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        endInteractButton.setPosition(getScreen().getWidth()/2 - endInteractButton.getWidth()/2, getScreen().getHeight()/5);
        endInteractButton.hide();
        getElements().add(endInteractButton);
        
    }
    
    private void createWagonHealth() {
    
        wagonHealth = new Indicator(getScreen(), "Wagon Health", new Vector2f(12,12), Indicator.Orientation.HORIZONTAL) {
        
            @Override
            public void onChange(float currentValue, float currentPercentage) {
            }
        
        };
        
        Wagon wagon = getStateManager().getState(PlayerManager.class).getPlayer().getWagon();
        
        getScreen().addElement(wagonHealth);
        wagonHealth.setIndicatorColor(ColorRGBA.Red);
        //wagonHealth.setText("Wagon Health");
        wagonHealth.setTextWrap(LineWrapMode.NoWrap);
        wagonHealth.setTextVAlign(VAlign.Center);
        wagonHealth.setTextAlign(Align.Center);
        wagonHealth.setBaseImage(getScreen().getStyle("Window").getString("defaultImg"));
        wagonHealth.setIndicatorPadding(new Vector4f(7,7,7,7));
        //wagonHealth.setWidth(getScreen().getWidth() * .75f);
        //wagonHealth.setHeight(getScreen().getHeight()/10);
        wagonHealth.setX(getScreen().getWidth()/2 - wagonHealth.getWidth()/2);
        wagonHealth.setY(getScreen().getHeight() * .9f);
        wagonHealth.hide();
        wagonHealth.setMaxValue(wagon.getMaxHealth());
        wagonHealth.setCurrentValue(wagon.getCurrentHealth());
        
    }
    
}
