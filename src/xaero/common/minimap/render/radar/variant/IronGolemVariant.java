package xaero.common.minimap.render.radar.variant;

import java.util.Objects;
import net.minecraft.class_2960;
import net.minecraft.class_1439.class_4621;

public class IronGolemVariant {
   private final class_2960 texture;
   private final class_4621 cracks;

   public IronGolemVariant(class_2960 texture, class_4621 cracks) {
      this.texture = texture;
      this.cracks = cracks;
   }

   @Override
   public String toString() {
      return this.texture + "%" + this.cracks;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         IronGolemVariant that = (IronGolemVariant)o;
         return this.texture.equals(that.texture) && this.cracks == that.cracks;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.texture, this.cracks);
   }
}
