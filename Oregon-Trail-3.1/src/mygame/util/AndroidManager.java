package mygame.util;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
/**
*
* @author Bob
*/
public class AndroidManager extends AbstractAppState {
        
    public String filePath;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
}