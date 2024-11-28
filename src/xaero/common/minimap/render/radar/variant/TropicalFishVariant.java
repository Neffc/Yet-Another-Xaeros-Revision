package xaero.common.minimap.render.radar.variant;

import java.util.Objects;
import net.minecraft.class_1767;
import net.minecraft.class_2960;
import net.minecraft.class_1474.class_1475;

public class TropicalFishVariant {
   private final class_2960 texture;
   private final class_1475 pattern;
   private final class_1767 baseColor;
   private final class_1767 patternColor;

   public TropicalFishVariant(class_2960 texture, class_1475 pattern, class_1767 baseColor, class_1767 patternColor) {
      this.texture = texture;
      this.pattern = pattern;
      this.baseColor = baseColor;
      this.patternColor = patternColor;
   }

   @Override
   public String toString() {
      return this.texture + "%" + this.pattern + "%" + this.baseColor + "%" + this.patternColor;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TropicalFishVariant that = (TropicalFishVariant)o;
         return this.texture.equals(that.texture) && this.pattern == that.pattern && this.baseColor == that.baseColor && this.patternColor == that.patternColor;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.texture, this.pattern, this.baseColor, this.patternColor);
   }
}
