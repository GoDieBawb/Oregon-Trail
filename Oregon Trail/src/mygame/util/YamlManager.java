/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Bob
 */
public class YamlManager {
    
    public HashMap loadYaml(String path) {
        
        System.out.println("Attempting to load Yaml");
        
        HashMap map;
        File    file;
        Yaml    yaml = new Yaml();
        
        try {
            file = new File(path);
            FileInputStream fi = new FileInputStream(file);
            Object obj = yaml.load(fi);
            map = (LinkedHashMap) obj;
        }
        
        catch(FileNotFoundException fnf) {
            return null;
        }
        
        return map;
    }
    
    public void saveYaml(String path, HashMap map) {
        
        System.out.println("Attemping to save to: " + path);
        
        DumperOptions options = new DumperOptions();
        File file             = new File(path);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setAllowUnicode(true);
        Yaml yaml = new Yaml(options);         
        
        try {
        
            FileWriter fw = new FileWriter(file);
            yaml.dump(map, fw);
            fw.close();
            
        }
        
        catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
}
