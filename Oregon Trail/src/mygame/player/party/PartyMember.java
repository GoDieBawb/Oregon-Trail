/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player.party;

import com.jme3.scene.Node;
import java.util.HashMap;

/**
 *
 * @author root
 */
public interface PartyMember {
    
    public HashMap getCondition();
    public void    setModel(Node model);
    public Node    getModel();
    
}
