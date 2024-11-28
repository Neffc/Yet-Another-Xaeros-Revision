package xaero.common.minimap.render.radar.armor;

import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1498;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2487;
import net.minecraft.class_3489;

public class EntityIconArmorHandler {
   public EntityIconArmor getArmor(class_1309 livingEntity) {
      EntityIconArmor armor = null;
      class_1304 relevantArmourSlot = livingEntity instanceof class_1498 ? class_1304.field_6174 : class_1304.field_6169;
      class_1799 armorItemStack = livingEntity.method_6118(relevantArmourSlot);
      if (armorItemStack != null && armorItemStack != class_1799.field_8037) {
         class_1792 armorItem = armorItemStack.method_7909();
         String trimMaterial = null;
         String trimPattern = null;
         if (armorItemStack.method_31573(class_3489.field_41890)
            && armorItemStack.method_7969() != null
            && armorItemStack.method_7969().method_10573("Trim", 10)) {
            class_2487 trimTag = armorItemStack.method_7969().method_10562("Trim");
            if (trimTag.method_10573("material", 8) && trimTag.method_10573("pattern", 8)) {
               trimMaterial = trimTag.method_10558("material");
               trimPattern = trimTag.method_10558("pattern");
            } else {
               trimMaterial = "inline_material";
               trimPattern = "inline_pattern";
            }
         }

         armor = new EntityIconArmor(armorItem, trimMaterial, trimPattern);
      }

      return armor;
   }
}
