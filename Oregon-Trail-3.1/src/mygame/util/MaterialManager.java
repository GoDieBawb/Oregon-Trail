/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author Bawb
 */
public class MaterialManager {
  
    private AssetManager assetManager;
    
    public MaterialManager(AssetManager am) {
    
        assetManager = am;
        
    }
    
    public void makeUnshaded(Node node) {
      
        SceneGraphVisitor sgv = new SceneGraphVisitor() {
 
            public void visit(Spatial spatial) {
        
                    if (spatial instanceof Geometry) {
          
                    Geometry geom = (Geometry) spatial;
                    Material mat  = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    Material tat  = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");

                    if (geom.getName().equals("Invisible"));
        
                    else if (geom.getMaterial().getTextureParam("DiffuseMap_1") != null) {
            
                        tat.setTexture("Alpha", geom.getMaterial().getTextureParam("AlphaMap").getTextureValue());
          
                        if (geom.getMaterial().getTextureParam("DiffuseMap") != null) {
           
                            tat.setTexture("Tex1", geom.getMaterial().getTextureParam("DiffuseMap").getTextureValue());
                            tat.getTextureParam("Tex1").getTextureValue().setWrap(Texture.WrapMode.Repeat);
                            tat.setFloat("Tex1Scale", Float.valueOf(geom.getMaterial().getParam("DiffuseMap_0_scale").getValueAsString()));
          
                        }
        
                        if (geom.getMaterial().getTextureParam("DiffuseMap_1") != null) {
              
                            tat.setTexture("Tex2", geom.getMaterial().getTextureParam("DiffuseMap_1").getTextureValue());
                            tat.getTextureParam("Tex2").getTextureValue().setWrap(Texture.WrapMode.Repeat);
                            tat.setFloat("Tex2Scale", Float.valueOf(geom.getMaterial().getParam("DiffuseMap_1_scale").getValueAsString()));
          
                        }
        
                        if (geom.getMaterial().getTextureParam("DiffuseMap_2") != null) {
              
                            tat.setTexture("Tex3", geom.getMaterial().getTextureParam("DiffuseMap_2").getTextureValue());
                            tat.getTextureParam("Tex3").getTextureValue().setWrap(Texture.WrapMode.Repeat);
                            tat.setFloat("Tex3Scale", Float.valueOf(geom.getMaterial().getParam("DiffuseMap_2_scale").getValueAsString()));
          
                        }

                        tat.setBoolean("useTriPlanarMapping", true);
                        geom.setMaterial(tat);
          
                    }
        
                    else if (geom.getMaterial().getTextureParam("DiffuseMap") != null) {
              
                        mat.setTexture("ColorMap", geom.getMaterial().getTextureParam("DiffuseMap").getTextureValue());
                        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                        mat.setFloat("AlphaDiscardThreshold", .5f);
                        mat.setFloat("ShadowIntensity", 5);
                        mat.setVector3("LightPos", new Vector3f(5,20,5));
                        geom.setMaterial(mat);
              
                    }
       
                }
      
            }
    
        };
    
    node.depthFirstTraversal(sgv);
    
    }       
    
}
