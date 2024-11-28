package xaero.common.minimap.render.radar;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1299;
import xaero.common.MinimapLogs;
import xaero.common.icon.XaeroIcon;

public class EntityIconCache {
   private final class_1299<?> entityType;
   private final Map<EntityIconKey, XaeroIcon> storage;
   private final Map<Object, String> variantStringCache;
   private boolean classValidityChecked;
   private boolean invalidVariantClass;
   private Class<?> variantClass;

   public EntityIconCache(class_1299<?> entityType) {
      this.entityType = entityType;
      this.storage = new HashMap<>();
      this.variantStringCache = new HashMap<>();
   }

   public XaeroIcon get(EntityIconKey key) {
      if (this.invalidVariantClass) {
         return null;
      } else if (key.getVariant() == null) {
         MinimapLogs.LOGGER.error("One of the variant IDs for entity {} is null!", class_1299.method_5890(this.entityType));
         MinimapLogs.LOGGER.error("This is most likely caused by a resource pack or mod that adds entity icons to Xaero's Minimap.");
         this.invalidVariantClass = true;
         return null;
      } else {
         return this.storage.get(key);
      }
   }

   public XaeroIcon add(EntityIconKey key, XaeroIcon icon) {
      if (this.invalidVariantClass) {
         return null;
      } else {
         Class<?> c = key.getVariant().getClass();
         if (this.variantClass == null) {
            this.variantClass = c;
         } else if (c != this.variantClass) {
            MinimapLogs.LOGGER
               .error("The variant IDs of entity {} don't use the same class! {} is not {}", class_1299.method_5890(this.entityType), c, this.variantClass);
            MinimapLogs.LOGGER.error("This is most likely caused by a resource pack or mod that adds entity icons to Xaero's Minimap.");
            this.invalidVariantClass = true;
            return null;
         }

         if (!this.classValidityChecked) {
            this.classValidityChecked = true;
            if (c == Object.class) {
               MinimapLogs.LOGGER.error("The class used for variant IDs of entity {} can't be Object!", class_1299.method_5890(this.entityType));
               MinimapLogs.LOGGER.error("This is most likely caused by a resource pack or mod that adds entity icons to Xaero's Minimap.");
               this.invalidVariantClass = true;
               return null;
            }

            try {
               c.getDeclaredMethod("toString");
               c.getDeclaredMethod("hashCode");
               c.getDeclaredMethod("equals", Object.class);
            } catch (NoSuchMethodException var5) {
               MinimapLogs.LOGGER
                  .error(
                     "The {} used for variant IDs of entity {} doesn't declare toString, hashCode or equals methods!",
                     c,
                     class_1299.method_5890(this.entityType)
                  );
               MinimapLogs.LOGGER
                  .error("If you're a regular player, this is most likely caused by a resource pack or mod that adds entity icons to Xaero's Minimap.");
               MinimapLogs.LOGGER
                  .error(
                     "If you are the icon resource pack or mod author, please use Java records for variant IDs, if possible. You can also let your IDE generate all 3 methods for you."
                  );
               MinimapLogs.LOGGER.error("Declaring the hashCode or equals methods incorrectly might destroy the game's performance and then crash it.");
               MinimapLogs.LOGGER
                  .error(
                     "The simplest way to get this to work is to just use String variant IDs, but it won't perform as well as properly using the new system."
                  );
               this.invalidVariantClass = true;
               return null;
            }
         }

         this.variantStringCache.remove(key.getVariant());
         return this.storage.put(key, icon);
      }
   }

   public String getVariantString(EntityIconKey key) {
      Object variant = key.getVariant();
      String result = this.variantStringCache.get(variant);
      if (result == null) {
         this.variantStringCache.put(variant, result = variant.toString());
      }

      return result;
   }

   public boolean isInvalidVariantClass() {
      return this.invalidVariantClass;
   }
}
