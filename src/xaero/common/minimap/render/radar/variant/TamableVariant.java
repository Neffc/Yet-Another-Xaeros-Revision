package xaero.common.minimap.render.radar.variant;

import java.util.Objects;
import net.minecraft.class_2960;

public class TamableVariant {
   private final class_2960 texture;
   private final boolean tame;

   public TamableVariant(class_2960 texture, boolean tame) {
      this.texture = texture;
      this.tame = tame;
   }

   @Override
   public String toString() {
      return this.texture + "%" + this.tame;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TamableVariant that = (TamableVariant)o;
         return this.tame == that.tame && this.texture.equals(that.texture);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.texture, this.tame);
   }
}
