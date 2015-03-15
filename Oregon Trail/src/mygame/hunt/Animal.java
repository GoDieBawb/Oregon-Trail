package mygame.hunt;

import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Random;
import mygame.GameManager;

/**
 *
 * @author Bawb
 */
public class Animal extends Node {
    
    private String   type;
    private boolean  isDead;
    private Node     model;
    private Vector3f moveDir;
    
    public Animal(AppStateManager stateManager) {
        
        int chance = randInt(1,5);
        
        if(chance == 1)
            makeBear(stateManager);
        
        else if (chance == 2)
            makeDeer(stateManager);
        
        else
            makeRabbit(stateManager);
        
        stateManager.getState(GameManager.class).getUtilityManager().getMaterialManager().makeUnshaded(model);
        attachChild(model);
        model.setLocalTranslation(0,0,0);
        
    }
    
    private void makeBear(AppStateManager stateManager) {
        type  = "Bear";
        model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models/Creatures/bear.j3o");
    }
    
    private void makeDeer(AppStateManager stateManager) {
        type  = "Deer";
        model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models/Creatures/deer.j3o");
    }
    
    private void makeRabbit(AppStateManager stateManager) {
        type  = "Rabbit";
        model = (Node) stateManager.getApplication().getAssetManager().loadModel("Models/Creatures/rabbit.j3o");
    }
    
    public void run() {
        
        model.getControl(AnimControl.class).clearChannels();
        
        if(type.equals("Bear"))
            model.getControl(AnimControl.class).createChannel().setAnim("run");
        else
            model.getControl(AnimControl.class).createChannel().setAnim("Run");
        
    }
    
    public void die() {
        
        isDead = true;
        model.getControl(AnimControl.class).clearChannels();
        
        if(type.equals("Bear"))
            model.getControl(AnimControl.class).createChannel().setAnim("die");
        else if (type.equals("Rabbit"))
            model.getControl(AnimControl.class).createChannel().setAnim("Death1");
        else
            model.getControl(AnimControl.class).createChannel().setAnim("Dead");
        
        model.getControl(AnimControl.class).getChannel(0).setLoopMode(LoopMode.DontLoop);
        
    }
    
    public String getType() {
        return type;
    }
    
    public void setMoveDir(int xMove, int zMove) {
        moveDir = new Vector3f(xMove, 0, zMove);
    }
    
    public void setMoveDir(Vector3f newDir) {
        moveDir = newDir;
    }
    
    public Vector3f getMoveDir() {
        return moveDir;
    }
    
    public boolean isDead() {
        return isDead;
    }
    
    private int randInt(int min, int max) {
        Random rand   = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }       
    
}
