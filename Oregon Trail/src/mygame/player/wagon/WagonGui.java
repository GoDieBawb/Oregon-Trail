/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player.wagon;

import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.GameManager;
import mygame.player.Player;
import mygame.player.PlayerManager;
import mygame.river.FordControl;
import mygame.util.Gui;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.extras.Indicator;

/**
 *
 * @author Bawb
 */
public class WagonGui extends Gui {

    private ButtonAdapter moveButton;
    private ButtonAdapter situationButton;
    private ButtonAdapter suppliesButton;
    private ButtonAdapter partyButton;
    private ButtonAdapter stopButton;
    private ButtonAdapter fastButton;
    private ButtonAdapter slowButton;
    private Indicator     wagonHealth;
    private int           partySelect;
    
    public WagonGui(AppStateManager stateManager) {
        super(stateManager);
    }    
    
    @Override
    public void createElements() {
        createStopButton();
        createMoveButton();
        createSituationButton();
        createSuppliesButton();
        createPartyButton();
        createWagonHealth();
        createIncreaseSpeedButton();
        createDecreaseSpeedButton();
    }
    
    private void createStopButton() {
    
        stopButton = new ButtonAdapter(getScreen(), "Stop Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                player.setInWagon(false);
                stopButton.hide();
                wagonHealth.hide();
                fastButton.hide();
                slowButton.hide();
                player.getHud().getLeftStick().show();
                
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
    
    private void createIncreaseSpeedButton() {
    
        fastButton = new ButtonAdapter(getScreen(), "Increase Speed Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                    Player player  = getStateManager().getState(PlayerManager.class).getPlayer();
                   
                    switch (player.getPace()) {
                            
                        case "Slow":
                            player.getWagon().setMoveSpeed(6);
                            break;
                        
                        case "Normal":
                            player.getWagon().setMoveSpeed(9);
                            break;
                        
                    }                    
                    
                    switch (player.getPace()) {
                            
                        case "Normal":
                            fastButton.show();
                            slowButton.show();
                            break;
                            
                        case "Fast":
                            slowButton.show();
                            fastButton.hide();
                            break;
                        
                    }
                
            }
        
        };        
        
        getScreen().addElement(fastButton);        
        fastButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        fastButton.setPosition(getScreen().getWidth() - fastButton.getWidth()*1.5f, fastButton.getHeight()*2.5f);
        fastButton.hide();
        fastButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        getElements().add(fastButton);
        fastButton.setTextAlign(BitmapFont.Align.Center);
        fastButton.setFont("Interface/Fonts/UnrealTournament.fnt");   
        fastButton.setFontSize(50);
        fastButton.setText("+");
        
    }    
    
    private void createDecreaseSpeedButton() {
    
        slowButton = new ButtonAdapter(getScreen(), "Decrase Speed Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                    Player player  = getStateManager().getState(PlayerManager.class).getPlayer();
                   
                    switch (player.getPace()) {
                            
                        case "Normal":
                            player.getWagon().setMoveSpeed(3);
                            break;
                            
                        case "Fast":
                            player.getWagon().setMoveSpeed(6);
                            break;
                        
                    }                    
                    
                    switch (player.getPace()) {
                    
                        case "Slow":
                            fastButton.show();
                            slowButton.hide();
                            break;
                            
                        case "Normal":
                            fastButton.show();
                            slowButton.show();
                            break;
                        
                    }
                
            }
        
        };        
        
        getScreen().addElement(slowButton);        
        slowButton.setDimensions(getScreen().getWidth()/10, getScreen().getHeight()/10);
        slowButton.setPosition(getScreen().getWidth() - slowButton.getWidth()*1.5f, 0 + slowButton.getHeight()*.5f);
        slowButton.hide();
        slowButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        getElements().add(slowButton);
        slowButton.setTextAlign(BitmapFont.Align.Center);
        slowButton.setFont("Interface/Fonts/UnrealTournament.fnt");   
        slowButton.setFontSize(50);
        slowButton.setText("-");
        
    }      
    
    private void createWagonHealth() {
    
        wagonHealth = new Indicator(getScreen(), "Wagon Trail Health", new Vector2f(12,12), Indicator.Orientation.HORIZONTAL) {
        
            @Override
            public void onChange(float currentValue, float currentPercentage) {
            }
        
        };
        
        getScreen().addElement(wagonHealth);
        wagonHealth.setIndicatorColor(ColorRGBA.Red);
        wagonHealth.setTextWrap(LineWrapMode.NoWrap);
        wagonHealth.setTextVAlign(BitmapFont.VAlign.Center);
        wagonHealth.setTextAlign(BitmapFont.Align.Center);
        wagonHealth.setBaseImage(getScreen().getStyle("Window").getString("defaultImg"));
        wagonHealth.setIndicatorPadding(new Vector4f(7,7,7,7));
        wagonHealth.setX(0 + wagonHealth.getWidth()/2);
        wagonHealth.setY(0 + wagonHealth.getHeight()*4);
        wagonHealth.hide();
        setWagonHealth();
        
    }
    
    public void setWagonHealth() {
        Wagon wagon = getStateManager().getState(PlayerManager.class).getPlayer().getWagon();
        wagonHealth.setMaxValue(wagon.getMaxHealth());
        wagonHealth.setCurrentValue(wagon.getCurrentHealth());
        wagonHealth.getTextDisplayElement().setText(wagon.getCurrentHealth() + " / " + wagon.getMaxHealth());
    }
    
    private void createMoveButton() {
    
        moveButton = new ButtonAdapter(getScreen(), "Move Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player  = getStateManager().getState(PlayerManager.class).getPlayer();
                hideButtons();
                
                if (player.getSituation().get("Setting").equals("Town")) {
                    
                    if ((Integer)player.getInventory().get("Oxen") < 1){
                        player.getHud().showAlert("Oxen", "You'll need at least one ox to pull your wagon!");
                        return;
                    }
                    
                    player.getSituation().put("Setting", "Trail");
                    getStateManager().getState(GameManager.class).initTrail();
                    
                    
                }
                
                else if(player.getSituation().get("Setting").equals("Trail")) {
                
                    moveButton.hide();
                    suppliesButton.hide();
                    situationButton.hide();
                    player.setInWagon(true);
                    stopButton.show();
                    wagonHealth.show();
                    
                    player.getHud().getLeftStick().hide();

                    switch (player.getPace()) {
                    
                        case "Slow":
                            fastButton.show();
                            break;
                            
                        case "Normal":
                            fastButton.show();
                            slowButton.show();
                            break;
                            
                        case "Fast":
                            slowButton.show();
                            break;
                        
                    }
                    
                    int oxCount = (Integer) player.getInventory().get("Oxen");
                    
                    if (oxCount == 1) {
                        fastButton.hide();
                        slowButton.hide();
                        player.getWagon().setMoveSpeed(3);
                    }
                    
                }
                
                else if (player.getSituation().get("Setting").equals("River")) {
                    
                    player.setInWagon(true);
                    player.setNoMove(true);                    
                    FordControl fordControl = player.getWagon().getModel().getControl(FordControl.class); 
                    fordControl.setEnabled(true);
                    fordControl.cross(false);
                    
                }
            
            }
        
        };
        
        getScreen().addElement(moveButton);        
        moveButton.setDimensions(getScreen().getWidth()/3, getScreen().getHeight()/10);
        moveButton.setPosition(getScreen().getWidth()/2 - moveButton.getWidth()/2, getScreen().getHeight()/2 + moveButton.getHeight()*2);
        moveButton.hide();
        moveButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        getElements().add(moveButton);
        moveButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        moveButton.setZOrder(-1);
        
        Player player  = getStateManager().getState(PlayerManager.class).getPlayer();
        
        if (player.getSituation().get("Setting").equals("River")) {
            moveButton.setText("Ford the River");
        }
        
        else {
            moveButton.setText("Hit the Trail");
        }
        
    }
    
    private void createSituationButton() {
        
        situationButton = new ButtonAdapter(getScreen(), "Situation Button", new Vector2f(12,12)) {
        
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
                
                if(setting.equals("Town") || setting.equals("River"))
                    setInfo = "in the " + setting + " of " + setName;
                
                else
                    setInfo = "on the trail to "  + goalName;
                
                String info = "You are " + setInfo +  "." + System.getProperty("line.separator")
                              + "You have traveled a total of " + milesTraveled + " miles in " + day + " days." + System.getProperty("line.separator")
                              + "Your next destination is " + goalName + " which is " + goalDist + " miles away." + System.getProperty("line.separator");
                
                player.getHud().showAlert("Situation", info);
                hideButtons();
                
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
        situationButton.setZOrder(-1);
        
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
                hideButtons();
        
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
        suppliesButton.setZOrder(-1);
        
    }
    
    private void createPartyButton() {
        
        partyButton = new ButtonAdapter(getScreen(), "Party Button", new Vector2f(12,12)) {
        
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player     = getStateManager().getState(PlayerManager.class).getPlayer();
                boolean isTrail   = "Trail".equals((String) player.getSituation().get("Setting"));
                Node    partyNode = new Node();
                
                if (!isTrail) {
                    Node   sceneParty = (Node) ((Node) player.getParent().getChild("Scene")).getChild("PartyNode");
                    partyNode  = (Node) ((Node) sceneParty.getChild(0)).getChild(0);
                }
                
                player.getHud().getInfoText().getButtonOk().hide();
                
                switch (partySelect) {
                    
                    case 0:
                        player.setNoMove(true);
                        hideButtons();
                        this.setText("Next");
                        show();
                        printPartyMemberInfo("Player");
                        partySelect = 1;
                        break;
                    
                    case 1:
                        show();                        if (!isTrail)
                            partyNode.getChild(0).addControl(player.getChaseControl().getCameraManager().getChaseCam());
                        printPartyMemberInfo("Wife");
                        partySelect = 2;
                        break;
                        
                    case 2:
                        if (!isTrail)
                            partyNode.getChild(1).addControl(player.getChaseControl().getCameraManager().getChaseCam());
                        printPartyMemberInfo("Son");
                        partySelect = 3;
                        break;
                        
                    case 3:
                        this.setText("Finish");
                        if (!isTrail)
                            partyNode.getChild(2).addControl(player.getChaseControl().getCameraManager().getChaseCam());
                        printPartyMemberInfo("Daughter");
                        partySelect = 4;
                        break;
                        
                    default:
                        player.getModel().addControl(player.getChaseControl().getCameraManager().getChaseCam());
                        player.setNoMove(false);
                        player.getHud().getInfoText().getButtonOk().show();
                        player.getHud().getInfoText().hide();
                        this.setText("Check Party");
                        hideButtons();
                        partySelect = 0;
                        break;
                        
                }
        
            }
            
        };
        
        getScreen().addElement(partyButton);        
        partyButton.setDimensions(getScreen().getWidth()/3, getScreen().getHeight()/10);
        partyButton.setPosition(getScreen().getWidth()/2 - partyButton.getWidth()/2, getScreen().getHeight()/2 - partyButton.getHeight()*4);
        partyButton.hide();
        partyButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        partyButton.setText("Check Party");
        getElements().add(partyButton);
        partyButton.setFont("Interface/Fonts/UnrealTournament.fnt");
        partyButton.setZOrder(-1);
        
    }
    
    private void printPartyMemberInfo(String memberName) {
        
        String  info;
        Player  player   = getStateManager().getState(PlayerManager.class).getPlayer();
        HashMap cond     = (HashMap) player.getParty().getInfo().get(memberName);
        String  name     = (String)  cond.get("Name");
        String  lastName = (String)  player.getParty().getInfo().get("LastName");
        boolean starve   = (Boolean) cond.get("Starving");
        boolean dysent   = (Boolean) cond.get("Dysentary");
        boolean measle   = (Boolean) cond.get("Measles");
        boolean tired    = (Boolean) cond.get("Tired");
        boolean isDead   = (Boolean) cond.get("Dead");
        
        String nameInfo  = "Your " + memberName + " " + name + " " + lastName;
        String illness;
        String condition;
        
        if (dysent || measle) {
            
            if (dysent && measle)
                illness = name + " has dysentary and the measles.";
            
            else if (dysent)
                illness = name + " has dysentary.";
            
            else
                illness = name + " has the measles.";
        }
        
        else {
            illness = name + " is in good health.";
        }
        
        if (starve || tired) {
        
            if (starve && tired) {
                condition = name + " is tired and starving."; 
            }
            
            else if (starve) {
                condition = name + " is starving.";
            }
            
            else {
                condition = name + " is exhausted."; 
            }
            
        }
        
        else {
            condition = name + " is in good condition."; 
        }
        
        if (isDead) {
            info = nameInfo + " has died.";
        }
        
        else {
            info = nameInfo + "\n" + illness + "\n" + condition;
        }
        
        player.getHud().showAlert("Party", info);
        
    }
    
    public void hideButtons(){
        moveButton.hide();
        suppliesButton.hide();
        situationButton.hide();
        partyButton.hide();
        wagonHealth.hide();
        slowButton.hide();
        fastButton.hide();
        stopButton.hide();
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
    
    public ButtonAdapter getPartyButton() {
        return partyButton;
    }
    
    public ButtonAdapter getStopButton() {
        return stopButton;
    }
    
    public ButtonAdapter getFastButton() {
        return fastButton;
    }    
    
    public ButtonAdapter getSlowButton() {
        return slowButton;
    }    
    
}
