package xaero.common.graphics.shader;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.class_284;
import net.minecraft.class_290;
import net.minecraft.class_5912;
import net.minecraft.class_5944;

public class PositionTexAlphaTestShader extends class_5944 {
   @Nullable
   private class_284 discardAlpha = this.method_34582("DiscardAlpha");

   public PositionTexAlphaTestShader(class_5912 factory, String path) throws IOException {
      super(factory, path, class_290.field_1585);
   }

   public void setDiscardAlpha(float alpha) {
      if (this.discardAlpha.method_35664().get(0) != alpha) {
         this.discardAlpha.method_1251(alpha);
      }
   }
}
