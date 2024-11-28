package xaero.common.minimap.render.radar.variant;

import java.util.Objects;
import net.minecraft.class_1767;
import net.minecraft.class_2960;

public class LlamaVariant {
   private final class_2960 texture;
   private final boolean trader;
   private final class_1767 swag;

   public LlamaVariant(class_2960 texture, boolean trader, class_1767 swag) {
      this.texture = texture;
      this.trader = trader;
      this.swag = swag;
   }

   @Override
   public String toString() {
      return this.texture + "%" + this.trader + "%" + this.swag;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         LlamaVariant that = (LlamaVariant)o;
         return this.trader == that.trader && Objects.equals(this.texture, that.texture) && this.swag == that.swag;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.texture, this.trader, this.swag);
   }
}
