/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.player.party;

import com.jme3.app.state.AppStateManager;
import java.util.HashMap;
import java.util.Random;
import mygame.GameManager;

/**
 *
 * @author root
 */
public class Party {
    
    private final AppStateManager stateManager;
    private final String          filePath;
    private       HashMap         partyInfo;
    
    public Party(AppStateManager stateManager, String filePath) {
        this.stateManager = stateManager;
        this.filePath     = filePath;
    }
    
    public void loadParty() {
        
       partyInfo =  stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().loadYaml(filePath + "PartySave.yml");
    
       if (partyInfo == null) {
           createParty();
       }
       
    }
    
    public void createParty() {
        
        partyInfo     = new HashMap();
        PartyNpc mom  = new PartyNpc();
        PartyNpc boy  = new PartyNpc();
        PartyNpc girl = new PartyNpc();
        
        mom.generate(randomFemaleName());
        boy.generate(randomMaleName());
        girl.generate(randomFemaleName());
        
        partyInfo.put("Wife",  mom.getCondition());
        partyInfo.put("Son",  boy.getCondition());
        partyInfo.put("Daughter", girl.getCondition());
        partyInfo.put("LastName", randomLastName());
        
        saveParty();
        
    }
    
    public String randomMaleName() {
        
        String name = "";
        int    rand = randInt(1,10);
        
        switch(rand) {
        
            case 1:
                name = "James";
                break;
            case 2:
                name = "John";
                break;
            case 3:
                name = "Jacob";
                break;
            case 4:
                name = "David";
                break;
            case 5:
                name = "Charles";
                break;
            case 6:
                name = "William";
                break;
            case 7:
                name = "Joseph";
                break;
            case 8:
                name = "Henry";
                break;
            case 9:
                name = "Robert";
                break;
            case 10:
                name = "Thomas";
                break;
            
        }
        
        return name;
        
    }
    
    private String randomFemaleName() {
        
        String name = "";
        int    rand = randInt(1,10);
        
        switch(rand) {
        
            case 1:
                name = "Mary";
                break;
            case 2:
                name = "Anna";
                break;
            case 3:
                name = "Alice";
                break;
            case 4:
                name = "Grace";
                break;
            case 5:
                name = "Martha";
                break;
            case 6:
                name = "Elizabeth";
                break;
            case 7:
                name = "Helen";
                break;
            case 8:
                name = "Louise";
                break;
            case 9:
                name = "Lucy";
                break;
            case 10:
                name = "Rose";
                break;
            
        }
        
        return name;
    }    
    
    private String randomLastName() {
        
        String name = "";
        int    rand = randInt(1,10);
        
        switch(rand) {
        
            case 1:
                name = "Smith";
                break;
            case 2:
                name = "Jones";
                break;
            case 3:
                name = "Davis";
                break;
            case 4:
                name = "Miller";
                break;
            case 5:
                name = "Jackson";
                break;
            case 6:
                name = "Walker";
                break;
            case 7:
                name = "Brown";
                break;
            case 8:
                name = "White";
                break;
            case 9:
                name = "Harris";
                break;
            case 10:
                name = "Clark";
                break;
            
        }
        
        return name;
        
    }
    
    private int randInt(int min, int max) {
        Random rand   = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }         
    
    public HashMap getInfo() {
        return partyInfo;
    }
    
    public void saveParty() {
        stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().saveYaml(filePath + "PartySave.yml", partyInfo);
    }
    
}
