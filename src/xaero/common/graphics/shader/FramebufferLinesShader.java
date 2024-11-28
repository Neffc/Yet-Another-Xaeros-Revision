package xaero.common.graphics.shader;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.class_284;
import net.minecraft.class_290;
import net.minecraft.class_5912;
import net.minecraft.class_5944;

public class FramebufferLinesShader extends class_5944 {
   @Nullable
   private class_284 frameSize = this.method_34582("FrameSize");

   public FramebufferLinesShader(class_5912 factory) throws IOException {
      super(factory, "xaerominimap/framebuffer_lines", class_290.field_29337);
   }

   public void setFrameSize(float width, float height) {
      if (this.frameSize.method_35664().get(0) != width || this.frameSize.method_35664().get(1) != height) {
         this.frameSize.method_1255(width, height);
      }
   }
}
