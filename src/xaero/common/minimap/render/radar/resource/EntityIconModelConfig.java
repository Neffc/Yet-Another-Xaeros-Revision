package xaero.common.minimap.render.radar.resource;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;

public class EntityIconModelConfig {
   @Expose
   public float baseScale = 1.0F;
   @Expose
   public float rotationY;
   @Expose
   public float rotationX;
   @Expose
   public float rotationZ;
   @Expose
   public float offsetX;
   @Expose
   public float offsetY;
   @Expose
   public boolean modelPartsRotationReset = true;
   @Expose
   public Boolean renderingFullModel;
   @Expose
   public ArrayList<String> modelMainPartFieldAliases;
   @Expose
   public ArrayList<String> modelPartsFields;
   @Expose
   public ArrayList<ArrayList<String>> modelRootPath;
   @Expose
   public boolean layersAllowed = true;
}
