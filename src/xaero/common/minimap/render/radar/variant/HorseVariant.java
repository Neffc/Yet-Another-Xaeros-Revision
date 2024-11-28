package xaero.common.minimap.render.radar.variant;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import net.minecraft.class_156;
import net.minecraft.class_2960;
import net.minecraft.class_5148;

public class HorseVariant {
   public static final Map<class_5148, class_2960> HORSE_MARKINGS = (Map<class_5148, class_2960>)class_156.method_654(
      Maps.newEnumMap(class_5148.class), map -> {
         map.put(class_5148.field_23808, (class_2960)null);
         map.put(class_5148.field_23809, new class_2960("textures/entity/horse/horse_markings_white.png"));
         map.put(class_5148.field_23810, new class_2960("textures/entity/horse/horse_markings_whitefield.png"));
         map.put(class_5148.field_23811, new class_2960("textures/entity/horse/horse_markings_whitedots.png"));
         map.put(class_5148.field_23812, new class_2960("textures/entity/horse/horse_markings_blackdots.png"));
      }
   );
   private final class_2960 texture;
   private final class_5148 markings;

   public HorseVariant(class_2960 texture, class_5148 markings) {
      this.texture = texture;
      this.markings = markings;
   }

   @Override
   public String toString() {
      return this.texture + "%" + HORSE_MARKINGS.get(this.markings);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         HorseVariant that = (HorseVariant)o;
         return Objects.equals(this.texture, that.texture) && this.markings == that.markings;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.texture, this.markings);
   }
}
