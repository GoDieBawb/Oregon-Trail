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
import mygame.player.Player;
import mygame.player.PlayerManager;

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
        
        partyInfo = stateManager.getState(GameManager.class).getUtilityManager().getYamlManager().loadYaml(filePath + "PartySave.yml");
    
        if (partyInfo == null) {
           createParty();
        }
       
    }
    
    public void createParty() {
        
        Player  player = stateManager.getState(PlayerManager.class).getPlayer();
        partyInfo      = new HashMap();
        PartyNpc mom   = new PartyNpc();
        PartyNpc boy   = new PartyNpc();
        PartyNpc girl  = new PartyNpc();
        
        player.generate();
        mom.generate(randomFemaleName());
        boy.generate(randomMaleName());
        girl.generate(randomFemaleName());
        
        partyInfo.put("Player",   player.getCondition());
        partyInfo.put("Wife",     mom.getCondition());
        partyInfo.put("Son",      boy.getCondition());
        partyInfo.put("Daughter", girl.getCondition());
        partyInfo.put("LastName", randomLastName());
        
        saveParty();
        
    }
    
    public void updateCondition () {
        
        String info       = "";
        String playerInfo = calculateCondition("Player") + "\n";
        String wifeInfo   = calculateCondition("Wife") + "\n";
        String sonInfo    = calculateCondition("Son")  + "\n";
        String girlInfo   = calculateCondition("Daughter") + "\n";
        
        if (playerInfo.contains("now"))
            info += playerInfo;
        
        if (wifeInfo.contains("now"))
            info += wifeInfo;
        
        if (sonInfo.contains("now"))
            info += sonInfo;
        
        if (girlInfo.contains("now"))
            info += girlInfo;
        
        if (info.contains("now"))
            stateManager.getState(PlayerManager.class).getPlayer().getHud().showAlert("Party", info);
        
    }
    
    
    private String calculateCondition(String memberName) {
    
        Player  player    = stateManager.getState(PlayerManager.class).getPlayer();
        int     food      = (Integer) player.getInventory().get("Food");
        HashMap condition = (HashMap) partyInfo.get(memberName);
        boolean starve    = (Boolean) condition.get("Starving");
        boolean dysent    = (Boolean) condition.get("Dysentary");
        boolean measles   = (Boolean) condition.get("Measles");
        boolean dead      = (Boolean) condition.get("Dead");
        boolean tired     = (Boolean) condition.get("Tired");
        String  name      = (String) condition.get("Name");
        String  lname     = (String) partyInfo.get("LastName");
        String infoPrefix = "Your " + memberName + " " + name + " " + lname + " is now ";
        
        if (memberName.equals("Player"))
            infoPrefix = "You are now ";        
        
        if (dead)
            return "";
        
        if (measles || dysent) {
        
            int tiredDiv = 2;
            
            if (measles && dysent) {
                tiredDiv *= 2;
            }
            
            if (starve)
                tiredDiv += 2;
            
            int tiredChance = 24/tiredDiv;
            
            if (randInt(1,tiredChance) == tiredChance) {
                
                if (!tired) {
                    String info = infoPrefix + "exhausted.";
                    condition.put("Tired", true);
                    return info;
                }
                
            }
            
        }
        
        if (food <= 0) {
        
            int starveDiv = 2;
            
            if (measles && dysent) {
                starveDiv *= 2;
            }
            
            int starveChance = 16/starveDiv;
            
            if (randInt(1,starveChance) == starveChance) {
                
                if (!starve) {
                    String info = infoPrefix + "starving.";
                    condition.put("Starving", true);
                    return info;
                }
                
            }
            
        }
        
        else {
        
            if (starve) {
                String info = infoPrefix + "no longer starving.";
                condition.put("Starving", false);
                return info;
            }
            
            if (tired) {
                
                int renewChance = randInt(1,3);
                
                if (renewChance == 3) {
                    String info = infoPrefix + "no longer exhausted.";
                    condition.put("Tired", false);
                    return info;
                }
                
            }
            
        }
        
        return calculateHealth(memberName);
        
    }    
    
    private String calculateHealth(String memberName) {
    
        HashMap condition = (HashMap) partyInfo.get(memberName);        
        boolean starve    = (Boolean) condition.get("Starving");
        boolean tired     = (Boolean) condition.get("Tired");
        boolean dysent    = (Boolean) condition.get("Dysentary");
        boolean measles   = (Boolean) condition.get("Measles");
        Player  player    = stateManager.getState(PlayerManager.class).getPlayer();
        String  name      = (String) condition.get("Name");
        String  lname     = (String) partyInfo.get("LastName");
        
        int speedMod = 0;
        
        switch (player.getPace()) {

            case "Slow":
                speedMod = 1;
                break;

            case "Normal":
                speedMod = 2;
                break;

            case "Fast":
                speedMod = 3;

        }        
        
        String infoPrefix = "Your " + memberName + " " + name + " " + lname + " now has ";
        
        if (memberName.equals("Player"))
            infoPrefix = "You now have ";
        
        if (starve || tired) {
        

            int dysDiv     = speedMod;
            int measDiv    = speedMod;
            
            int dysChance  = randInt(1,4);
            int measChance = randInt(1,4);
            
            if (starve && tired) {
                measDiv *= 2;
                dysDiv  *= 2;
            }
            
            else if (starve || tired) {
                measDiv += 1;
                dysDiv  += 1;
            }
            
            int dysOdds    = 24/dysDiv;
            int measOdds   = 24/measDiv;            
            
            if (randInt(1,dysOdds) == dysOdds) {
                
                if (!dysent) {
                    String info = infoPrefix + "dysentary.";
                    condition.put("Dysentary", true);
                    return info;
                }
                
            }
            
            else if (randInt(1,measOdds) == measOdds) {
                
                if (!measles) {
                    String info = infoPrefix + "the measles.";
                    condition.put("Measles", true);
                    return info;
                }
                
            }
            
        }
        
        else {
        
            if (measles) {
                
                int healChance = randInt(1,4);
                
                if (healChance == 4) {
                    String info = infoPrefix + "been cured of the measles.";
                    condition.put("Measles", false);
                    return info;
                }
                
            }
            
            else if (dysent) {
                
                int healChance = randInt(1,4);
                
                if (healChance == 4) {
                    String info = infoPrefix + "been cured of dystentary.";
                    condition.put("Dysentary", false);
                    return info;
                }
                
            }
            
        }
        
        return calculateDeath(memberName);
        
    }
    
    private String calculateDeath(String memberName) {
    
        HashMap condition   = (HashMap) partyInfo.get(memberName);  
        boolean starve      = (Boolean) condition.get("Starving");
        boolean tired       = (Boolean) condition.get("Tired");
        boolean dysent      = (Boolean) condition.get("Dysentary");
        boolean measles     = (Boolean) condition.get("Measles");
        boolean hasDied     = false;
        String  cause       = "";
        Player  player      = stateManager.getState(PlayerManager.class).getPlayer();
        
        int speedMod = 0;
        
        switch (player.getPace()) {

            case "Slow":
                speedMod = 1;
                break;

            case "Normal":
                speedMod = 2;
                break;

            case "Fast":
                speedMod = 3;

        }          
        
        int deathChance = speedMod;
        
        if (dysent)
            deathChance += 1;
        
        if (measles)
            deathChance += 1;
        
        if (starve)
            deathChance += 1;
        
        if (tired)
            deathChance += 1;
        
        if (deathChance > speedMod) {
            
            if (randInt(0+deathChance, 8) == 8) {
                
                hasDied = true;
                
                if (dysent)
                    cause = "Dysentary";
                
                else if (starve)
                    cause = "Starvation";
                
                else if (measles)
                    cause = "Measles";
                
                else
                    cause = "Exhaustion";
                
            }
            
        }
        
        deathChance = randInt(1,500);
        
        if (deathChance == 500) {
            hasDied = true;
            cause   = randomDeath();
        }
        
        if (hasDied) {

            String name   = (String) condition.get("Name");
            String lname  = (String) partyInfo.get("LastName");
            
            if (memberName.equals("Player")) {
                player.setIsDead(true);
                player.setCondition(condition);
                condition.put("Cause", cause);
                return "";
            }
            
            else {
                String deathInfo;
                deathInfo = "Your " + memberName + " " + name + " " + lname + " has died of " + cause;
                condition.put("Dead", true);
                player.getHud().showAlert("Death", deathInfo);
                return deathInfo;
            }
            
        }
        
        else {
            return "";
        }
        
    }
    
    public String randomDeath() {
        String cause;
        int    rand = randInt(1,10);
        
        switch(rand) {
            case 1:
                cause = "RattleSnake";
                break;
            default:
                cause = "Broken Leg";
                break;
                
        }
        
        return cause;
        
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
