package xaero.common.gui.widget;

import net.minecraft.class_437;

public class ImageWidget extends ScalableWidget {
   private String imageId;
   private int imageW;
   private int imageH;
   private int glTexture;

   public ImageWidget(
      Class<? extends class_437> location,
      float horizontalAnchor,
      float verticalAnchor,
      ClickAction onClick,
      HoverAction onHover,
      int x,
      int y,
      int scaledOffsetX,
      int scaledOffsetY,
      String url,
      String tooltip,
      double scale,
      String imageId,
      int imageW,
      int imageH,
      int glTexture,
      boolean noGuiScale
   ) {
      super(WidgetType.IMAGE, location, horizontalAnchor, verticalAnchor, onClick, onHover, x, y, scaledOffsetX, scaledOffsetY, url, tooltip, noGuiScale, scale);
      this.imageId = imageId;
      this.imageW = imageW;
      this.imageH = imageH;
      this.glTexture = glTexture;
   }

   public String getImageId() {
      return this.imageId;
   }

   public int getImageW() {
      return this.imageW;
   }

   public int getImageH() {
      return this.imageH;
   }

   public int getGlTexture() {
      return this.glTexture;
   }

   @Override
   public int getW() {
      return this.imageW;
   }

   @Override
   public int getH() {
      return this.imageH;
   }
}
