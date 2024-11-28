package xaero.common.minimap.render.radar;

import net.minecraft.class_630;

public class ModelPartRenderDetectionInfo {
   public class_630 modelPart;
   public float red;
   public float green;
   public float blue;
   public float alpha;

   public ModelPartRenderDetectionInfo(class_630 modelPart, float red, float green, float blue, float alpha) {
      this.modelPart = modelPart;
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.alpha = alpha;
   }
}
