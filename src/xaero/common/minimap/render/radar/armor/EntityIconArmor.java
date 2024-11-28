package xaero.common.minimap.render.radar.armor;

import java.util.Objects;
import net.minecraft.class_1792;

public class EntityIconArmor {
   private final class_1792 armor;
   private final String trimMaterial;
   private final String trimPattern;

   public EntityIconArmor(class_1792 armor, String trimMaterial, String trimPattern) {
      this.armor = armor;
      this.trimMaterial = trimMaterial;
      this.trimPattern = trimPattern;
   }

   @Override
   public String toString() {
      return "EntityIconArmor{" + this.armor + ", " + this.trimMaterial + ", " + this.trimPattern + "}";
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         EntityIconArmor that = (EntityIconArmor)o;
         return this.armor.equals(that.armor) && Objects.equals(this.trimMaterial, that.trimMaterial) && Objects.equals(this.trimPattern, that.trimPattern);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.armor, this.trimMaterial, this.trimPattern);
   }
}
