/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.animation.AnimControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Bawb
 */
public class SkeletonFinder {
    
  public AnimControl findAnimControl(final Spatial parent){
      AnimControl animControl = parent.getControl(AnimControl.class);
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
    
}
