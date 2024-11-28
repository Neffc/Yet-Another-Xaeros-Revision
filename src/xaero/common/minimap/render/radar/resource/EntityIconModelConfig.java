package xaero.common.minimap.render.radar.resource;

import java.util.ArrayList;

public class EntityIconModelConfig {
   public float baseScale = 1.0F;
   public float rotationY;
   public float rotationX;
   public float rotationZ;
   public float offsetX;
   public float offsetY;
   public boolean modelPartsRotationReset = true;
   public Boolean renderingFullModel;
   public ArrayList<String> modelMainPartFieldAliases;
   public ArrayList<String> modelPartsFields;
   public ArrayList<ArrayList<String>> modelRootPath;
   public boolean layersAllowed = true;
}
