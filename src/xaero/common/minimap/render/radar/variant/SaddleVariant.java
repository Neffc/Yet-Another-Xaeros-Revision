package xaero.common.minimap.render.radar.variant;

import java.util.Objects;
import net.minecraft.class_2960;

public class SaddleVariant {
   private final class_2960 texture;
   private final boolean saddled;

   public SaddleVariant(class_2960 texture, boolean saddled) {
      this.texture = texture;
      this.saddled = saddled;
   }

   @Override
   public String toString() {
      return this.texture + "%" + this.saddled;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         SaddleVariant that = (SaddleVariant)o;
         return this.saddled == that.saddled && this.texture.equals(that.texture);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.texture, this.saddled);
   }
}
