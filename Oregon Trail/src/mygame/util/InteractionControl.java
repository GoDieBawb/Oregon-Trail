/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

/**
 *
 * @author Bawb
 */
 public abstract class InteractionControl {
    
    private boolean enabled;
     
    public abstract void update(float tpf);
    
    public void setEnabled(boolean newVal) {
        enabled = newVal;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
}
