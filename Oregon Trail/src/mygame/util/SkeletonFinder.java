/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Bawb
 */
public class SkeletonFinder {
    
  public AnimControl findAnimControl(final Spatial parent){
      
      
      SkeletonControl skelControl = getSkeletonControl(parent);
      skelControl.setHardwareSkinningPreferred(true);
      
      AnimControl animControl     = parent.getControl(AnimControl.class);
      
      if (animControl != null) {
        return animControl;
      }
      
      if (parent instanceof Node) {
          
          for (final Spatial s : ((Node) parent).getChildren()) {
              
              final AnimControl animControl2 = findAnimControl(s);
              
              if (animControl2 != null) {
                  return animControl2;
              }
          }
          
      }
      
      return null;
      
  }
  
  private SkeletonControl getSkeletonControl(final Spatial parent){
      SkeletonControl skelControl = parent.getControl(SkeletonControl.class);
      if (skelControl != null) {
        return skelControl;
      }
      if (parent instanceof Node) {
          for (final Spatial s : ((Node) parent).getChildren()) {
              final SkeletonControl skelControl2 = getSkeletonControl(s);
              if (skelControl2 != null) {
                  return skelControl2;
              }
          }
      }
      return null;
  }
  
}
