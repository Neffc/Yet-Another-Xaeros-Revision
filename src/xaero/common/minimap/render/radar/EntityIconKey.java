package xaero.common.minimap.render.radar;

import java.util.Objects;
import xaero.common.minimap.render.radar.armor.EntityIconArmor;

public class EntityIconKey {
   private final Object variant;
   private final EntityIconArmor armor;

   public EntityIconKey(Object variant, EntityIconArmor armor) {
      this.variant = variant;
      this.armor = armor;
   }

   public Object getVariant() {
      return this.variant;
   }

   @Override
   public String toString() {
      return "EntityIconKey{" + this.variant + ", " + this.armor + "}";
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         EntityIconKey that = (EntityIconKey)o;
         return this.variant.equals(that.variant) && Objects.equals(this.armor, that.armor);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.variant, this.armor);
   }
}
