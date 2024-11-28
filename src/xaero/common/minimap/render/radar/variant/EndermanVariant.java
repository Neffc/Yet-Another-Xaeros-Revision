package xaero.common.minimap.render.radar.variant;

import java.util.Objects;
import net.minecraft.class_2960;

public class EndermanVariant {
   private final class_2960 texture;
   private final boolean angry;

   public EndermanVariant(class_2960 texture, boolean angry) {
      this.texture = texture;
      this.angry = angry;
   }

   @Override
   public String toString() {
      return this.texture + "%" + this.angry;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         EndermanVariant that = (EndermanVariant)o;
         return this.angry == that.angry && Objects.equals(this.texture, that.texture);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.texture, this.angry);
   }
}
