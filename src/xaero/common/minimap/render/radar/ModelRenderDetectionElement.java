package xaero.common.minimap.render.radar;

import java.util.HashMap;
import net.minecraft.class_1058;
import net.minecraft.class_2960;
import net.minecraft.class_583;
import net.minecraft.class_630;
import xaero.common.graphics.CustomRenderTypes;

public class ModelRenderDetectionElement {
   public class_583<?> model;
   public class_2960 renderTexture;
   public class_1058 renderAtlasSprite;
   CustomRenderTypes.EntityIconLayerPhases layerPhases;
   public float red;
   public float green;
   public float blue;
   public float alpha;
   public boolean allVisible;
   private HashMap<class_630, ModelPartRenderDetectionInfo> visibleParts;

   public ModelRenderDetectionElement(
      class_583<?> model,
      class_2960 renderTexture,
      class_1058 renderAtlasSprite,
      CustomRenderTypes.EntityIconLayerPhases layerPhases,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      this.model = model;
      this.renderTexture = renderTexture;
      this.renderAtlasSprite = renderAtlasSprite;
      this.layerPhases = layerPhases;
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.alpha = alpha;
   }

   @Override
   public String toString() {
      return this.model + " " + this.layerPhases.texture;
   }

   public void addVisibleModelPart(class_630 part, float red, float green, float blue, float alpha) {
      if (this.visibleParts == null) {
         this.visibleParts = new HashMap<>();
      }

      this.visibleParts.put(part, new ModelPartRenderDetectionInfo(part, red, green, blue, alpha));
   }

   public ModelPartRenderDetectionInfo getModelPartRenderInfo(class_630 part) {
      ModelPartRenderDetectionInfo mprdi = this.visibleParts == null ? null : this.visibleParts.get(part);
      if (mprdi == null && this.allVisible) {
         mprdi = new ModelPartRenderDetectionInfo(part, this.red, this.green, this.blue, this.alpha);
      }

      return mprdi;
   }

   public boolean isEmpty() {
      return !this.allVisible && (this.visibleParts == null || this.visibleParts.isEmpty());
   }

   public boolean sameVisibility(ModelRenderDetectionElement other) {
      HashMap<class_630, ModelPartRenderDetectionInfo> otherVisibleParts = other.visibleParts;
      if (this.visibleParts == null != (otherVisibleParts == null)) {
         return false;
      } else if (this.visibleParts == null) {
         return true;
      } else if (this.visibleParts.size() != otherVisibleParts.size()) {
         return false;
      } else {
         for (class_630 key : this.visibleParts.keySet()) {
            if (!otherVisibleParts.containsKey(key)) {
               return false;
            }
         }

         return true;
      }
   }
}
