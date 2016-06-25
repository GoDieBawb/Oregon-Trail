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
public class PartyNpc implements PartyMember {

    private Node    model;
    private HashMap condition;
    
    public PartyNpc() {
        condition = new HashMap();
    }
    
    public void generate(String name) {
        
        String  firstName = name;
        boolean starving  = false;
        boolean dysentary = false;
        boolean measles   = false;
        boolean tired     = false;
        boolean dead      = false;
        
        condition.put("Name",      firstName);
        condition.put("Starving",  starving);
        condition.put("Dysentary", dysentary);
        condition.put("Measles",   measles);
        condition.put("Tired",     tired);
        condition.put("Dead",      dead);
        
    }
    
    @Override
    public HashMap getCondition() {
        return condition;
    }

    @Override
    public void setModel(Node model) {
        
    }

    @Override
    public Node getModel() {
        return model;
    }
    
}
