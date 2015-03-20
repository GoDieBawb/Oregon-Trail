/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player;

import mygame.player.wagon.WagonModel;
import mygame.player.wagon.WagonGui;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.TextureKey;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import java.util.HashMap;
import mygame.GameManager;
import mygame.trail.TrailState;
import mygame.util.DualStickControl;
import mygame.util.Gui;
import mygame.util.InteractionManager;
import mygame.util.UtilityManager;
import mygame.util.YamlLoader;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.AlertBox;
import tonegod.gui.effects.Effect;

/**
 *
 * @author Bawb
 */
public class Hud extends Gui {

    private AlertBox       infoText;
    private ButtonAdapter  aimButton;
    private HashMap        scripts;
    private BitmapText     crossHair;
    private BitmapText     bulletDisplay;
    private ButtonAdapter  shootButton;
    private MyJoystick     leftStick;
    private MyJoystick     rightStick;
    private UtilityManager um;

    
    public Hud(AppStateManager stateManager) {
        super(stateManager);
        setScripts();
    }
    
    @Override
    public void createElements() {
        createInfoText();
        createAimButton();
        createShootButton();
        createCrossHair();
        createBulletDisplay();
    }
    
    //For Elements that need the UtilityManager
    public void initUtilHudElements(UtilityManager um) {
        this.um = um;
        createLeftStick();
        createRightStick();
    }
    
    private void createAimButton() {
    
        aimButton = new ButtonAdapter(getScreen(), "Aim Button", new Vector2f(12,12)) {
            
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player    = getStateManager().getState(PlayerManager.class).getPlayer();
                
                if (!player.isAiming()) {
                    startHunt();
                }
                
                else {
                    endHunt();
                }
                
            }
        
        };
        
        getScreen().addElement(aimButton);        
        aimButton.setDimensions(getScreen().getWidth()/5, getScreen().getHeight()/10);
        aimButton.setPosition(getScreen().getWidth()/2 - aimButton.getWidth()/2, getScreen().getHeight()/10);
        aimButton.hide();
        aimButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        aimButton.setText("Aim");
        getElements().add(aimButton);
        aimButton.setFont("Interface/Fonts/UnrealTournament.fnt");     
    
    }
    
    private void startHunt() {
        Player player         = getStateManager().getState(PlayerManager.class).getPlayer();
        SimpleApplication app = (SimpleApplication) getStateManager().getApplication();
        DualStickControl con  = getStateManager().getState(TrailState.class).getTrailSceneManager().getControl();
        player.setIsAiming(true);
        player.setNoMove(true);
        con.getCameraManager().setHuntCam(true);
        app.getGuiNode().attachChild(crossHair);
        shootButton.show();
        app.getGuiNode().attachChild(bulletDisplay);
        app.getCamera().setLocation(player.getModel().getChild("Face").getWorldTranslation());
        player.setLocalScale(.1f);
        aimButton.setText("Done");
        updateBulletDisplay();
        leftStick.hide();
        rightStick.hide();
    }
    
    public void endHunt() {
        Player player         = getStateManager().getState(PlayerManager.class).getPlayer();
        DualStickControl con  = getStateManager().getState(TrailState.class).getTrailSceneManager().getControl();
        SimpleApplication app = (SimpleApplication) getStateManager().getApplication();
        player.setIsAiming(false);
        player.setNoMove(false);
        con.getCameraManager().setHuntCam(false);
        app.getGuiNode().detachChild(crossHair);
        shootButton.hide();
        app.getGuiNode().detachChild(bulletDisplay);
        player.setLocalScale(1f);
        aimButton.setText("Aim");
        leftStick.show();
        rightStick.show();
    }
    
    private void createShootButton() {
    
        shootButton = new ButtonAdapter(getScreen(), "Shoot Button", new Vector2f(12,12)) {
            
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isPressed) {
                
                Player player = getStateManager().getState(PlayerManager.class).getPlayer();
                int ammo      = (Integer)player.getInventory().get("Bullets");
                
                if (ammo > 0) {
                    player.getInventory().put("Bullets", ammo-1);
                    player.shoot();
                    updateBulletDisplay();
                    app.getCamera().lookAtDirection(app.getCamera().getDirection().add(0,.25f,0), new Vector3f(0,1,0));
                    getStateManager().getState(GameManager.class).getUtilityManager().getAudioManager().playSound("Gunshot");
                }
                
                else {
                    getStateManager().getState(GameManager.class).getUtilityManager().getAudioManager().playSound("Empty");
                }
                
            }
        
        };
        
        getScreen().addElement(shootButton);        
        shootButton.setDimensions(getScreen().getWidth()/5, getScreen().getHeight()/5);
        shootButton.setPosition(getScreen().getWidth()/2 - shootButton.getWidth()/2 + shootButton.getWidth()*1.5f, aimButton.getHeight()/2);
        shootButton.hide();
        shootButton.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        shootButton.setText("Shoot");
        getElements().add(shootButton);
        shootButton.setFont("Interface/Fonts/UnrealTournament.fnt");     
        
    }

    private void createLeftStick(){
        
        leftStick = new MyJoystick(getScreen(), Vector2f.ZERO, (int)(getScreen().getWidth()/6)) {
            
            InteractionManager im = um.getInteractionManager();
            
            @Override
            public void show() {
                
                boolean isAndroid = "Dalvik".equals(System.getProperty("java.vm.name"));
                
                //if (!isAndroid)
                    //return;
                
                super.show();
                
            }
            
            @Override
            public void onUpdate(float tpf, float deltaX, float deltaY) {
            
                
                float dzVal = .3f; // Dead zone threshold
            
                if (deltaX < -dzVal) {
                    im.setLeft(true);
                    im.setRight(false);
                }
            
                else if (deltaX > dzVal) {
                    im.setRight(true);
                    im.setLeft(false);
                }
            
                else {
                    im.setRight(false);
                    im.setLeft(false);
                }
            
                if (deltaY < -dzVal) {
                    im.setDown(true);
                    im.setUp(false);
                }
            
                else if (deltaY > dzVal) {
                    im.setDown(false);
                    im.setUp(true);
                }
            
                else {
                    im.setUp(false);
                    im.setDown(false);
                }
            
                app.getStateManager().getState(PlayerManager.class).getPlayer().setSpeedMult((FastMath.abs(deltaY) + FastMath.abs(deltaX)));
            
            }
        
        };
        
        
        TextureKey key = new TextureKey("Textures/barrel.png", false);
        Texture tex = ((SimpleApplication)getStateManager().getApplication()).getAssetManager().loadTexture(key);
        leftStick.setTextureAtlasImage(tex, "x=20|y=20|w=120|h=35");
        leftStick.getThumb().setTextureAtlasImage(tex, "x=20|y=20|w=120|h=35");
        getScreen().addElement(leftStick, true);
        leftStick.setPosition(getScreen().getWidth()/10 - leftStick.getWidth()/2, getScreen().getHeight() / 10f - leftStick.getHeight()/5);
        // Add some fancy effects
        Effect fxIn = new Effect(Effect.EffectType.FadeIn, Effect.EffectEvent.Show,.5f);
        leftStick.addEffect(fxIn);
        Effect fxOut = new Effect(Effect.EffectType.FadeOut, Effect.EffectEvent.Hide,.5f);
        leftStick.addEffect(fxOut);
        leftStick.show();
            
    }
    
    private void createRightStick(){
        
        rightStick = new MyJoystick(getScreen(), Vector2f.ZERO, (int)(getScreen().getWidth()/6)) {
            InteractionManager im = um.getInteractionManager();
            
            @Override
            public void show() {
                
                boolean isAndroid = "Dalvik".equals(System.getProperty("java.vm.name"));
                
                //if (!isAndroid)
                    //return;
                
                super.show();
                
            }
            
            @Override
            public void onUpdate(float tpf, float deltaX, float deltaY) {
            
                
                float dzVal = .3f; // Dead zone threshold
            
                if (deltaX < -dzVal) {
                    im.setLeft1(true);
                    im.setRight1(false);
                }
            
                else if (deltaX > dzVal) {
                    im.setRight1(true);
                    im.setLeft1(false);
                }
            
                else {
                    im.setRight1(false);
                    im.setLeft1(false);
                }
            
                if (deltaY < -dzVal) {
                    im.setDown1(true);
                    im.setUp1(false);
                }
            
                else if (deltaY > dzVal) {
                    im.setDown1(false);
                    im.setUp1(true);
                }
            
                else {
                    im.setUp1(false);
                    im.setDown1(false);
                }
            
            }
        
        };
        
        TextureKey key = new TextureKey("Textures/barrel.png", false);
        Texture tex = ((SimpleApplication)getStateManager().getApplication()).getAssetManager().loadTexture(key);
        rightStick.setTextureAtlasImage(tex, "x=20|y=20|w=120|h=35");
        rightStick.getThumb().setTextureAtlasImage(tex, "x=20|y=20|w=120|h=35");
        getScreen().addElement(rightStick, true);
        rightStick.setPosition(getScreen().getWidth()*.9f - rightStick.getWidth()/2, getScreen().getHeight() / 10f - rightStick.getHeight()/5);
        // Add some fancy effects
        Effect fxIn = new Effect(Effect.EffectType.FadeIn, Effect.EffectEvent.Show,.5f);
        rightStick.addEffect(fxIn);
        Effect fxOut = new Effect(Effect.EffectType.FadeOut, Effect.EffectEvent.Hide,.5f);
        rightStick.addEffect(fxOut);
        rightStick.show();
            
    }
    
    
    private void createCrossHair() {
        BitmapFont font = getStateManager().getApplication().getAssetManager().loadFont("Interface/Fonts/UnrealTournament.fnt");
        crossHair       = new BitmapText(font);
        crossHair.setText("+");
        crossHair.setLocalTranslation(getScreen().getWidth()/2 - crossHair.getLineWidth()/2, getScreen().getHeight()/2 + crossHair.getLineHeight()/2, 0);
        
    }
    
    private void createBulletDisplay() {
        BitmapFont font     = getStateManager().getApplication().getAssetManager().loadFont("Interface/Fonts/UnrealTournament.fnt");
        bulletDisplay       = new BitmapText(font);
        bulletDisplay.setSize(bulletDisplay.getSize()/2);
        bulletDisplay.setLocalTranslation(getScreen().getWidth()/10 - bulletDisplay.getLineWidth()/2, getScreen().getHeight() - getScreen().getHeight()/10 - bulletDisplay.getLineHeight()/2, 0);
        bulletDisplay.setText("Ammo: ");
        updateBulletDisplay();
        
    }
    
    private void updateBulletDisplay() {
        Player player    = getStateManager().getState(PlayerManager.class).getPlayer();
        bulletDisplay.setText("Ammo: " + (Integer)player.getInventory().get("Bullets"));
    }
    
    private void createInfoText() {
        
        infoText = new AlertBox(getScreen(), Vector2f.ZERO) {
            
            @Override
            public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {
                
                hideWithEffect();
                
                PlayerManager  pm = app.getStateManager().getState(PlayerManager.class);
                
                if(pm.getPlayer().getIsDead()) {
                    pm.endGame();
                    return;
                }
                
                try {
                    
                    Node a        = (Node) ((SimpleApplication) app).getRootNode().getChild(0);
                    Node intNode  = (Node)       a.getChild("Interactable");
                    WagonModel wm = (WagonModel) intNode.getChild("Wagon");
                  
                    if(wm.inProx()) {
                        ((WagonGui)wm.getGui()).getMoveButton().show();
                        ((WagonGui)wm.getGui()).getSituationButton().show();
                        ((WagonGui)wm.getGui()).getSuppliesButton().show();
                    }
                  
                }
                
                catch (Exception e) {
                    
                }
                
            }
            
        };
        
        infoText.setMaterial(getStateManager().getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        infoText.getButtonOk().setFont("Interface/Impact.fnt");
        infoText.setWindowTitle("Welcome");
        infoText.setMsg("Welcome to Townyville.");
        infoText.setButtonOkText("Ok");
        infoText.setLockToParentBounds(true);
        infoText.setClippingLayer(infoText);
        infoText.setMinDimensions(new Vector2f(150,180));
        infoText.setIsResizable(true);
        getScreen().addElement(infoText);
        infoText.hide();
        
    }
        
    public void showAlert(String title, String text){
        infoText.showWithEffect();
        infoText.setWindowTitle(title);
        infoText.setMsg(text);
    }
    
    private void setScripts() {
        getStateManager().getApplication().getAssetManager().registerLoader(YamlLoader.class, "yml");
        scripts = (HashMap) getStateManager().getApplication().getAssetManager().loadAsset("Yaml/Alerts.yml");
    }
    
    public HashMap getScripts() {
        return scripts;
    }
    
    public AlertBox getInfoText(){
        return infoText;
    }
    
    public ButtonAdapter getAimButton() {
        return aimButton;
    }
    
    public MyJoystick getLeftStick() {
        return leftStick;
    }
    
    public MyJoystick getRightStick() {
        return rightStick;
    }
    
}
