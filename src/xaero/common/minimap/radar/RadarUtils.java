package xaero.common.minimap.radar;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.class_1267;
import net.minecraft.class_1297;
import net.minecraft.class_1321;
import net.minecraft.class_1496;
import net.minecraft.class_1569;
import net.minecraft.class_1588;
import net.minecraft.class_1657;
import net.minecraft.class_2940;
import net.minecraft.class_310;
import net.minecraft.class_3419;
import net.minecraft.class_4019;
import net.minecraft.class_4836;
import xaero.common.misc.Misc;
import xaero.hud.minimap.MinimapLogs;

public class RadarUtils {
   private static class_2940<Optional<UUID>> FOX_TRUSTED_UUID_SECONDARY;
   private static class_2940<Optional<UUID>> FOX_TRUSTED_UUID_MAIN;

   public static boolean hostileException(class_1297 e) {
      return e instanceof class_4836 ? ((class_4836)e).method_6109() : false;
   }

   public static boolean isTamed(class_1297 e, class_1657 p) {
      if (e instanceof class_1321 tameable) {
         if (tameable.method_6181() && p.method_5667().equals(tameable.method_6139())) {
            return true;
         }
      } else if (e instanceof class_1496 horse) {
         if (horse.method_6727() && (horse.method_6139() == null || p.method_5667().equals(horse.method_6139()))) {
            return true;
         }
      } else if (e instanceof class_4019 fox) {
         if (FOX_TRUSTED_UUID_SECONDARY != null && p.method_5667().equals(((Optional)fox.method_5841().method_12789(FOX_TRUSTED_UUID_SECONDARY)).orElse(null))) {
            return true;
         }

         if (FOX_TRUSTED_UUID_MAIN != null && p.method_5667().equals(((Optional)fox.method_5841().method_12789(FOX_TRUSTED_UUID_MAIN)).orElse(null))) {
            return true;
         }
      }

      return false;
   }

   public static boolean isHostile(class_1297 e) {
      return class_310.method_1551().field_1687.method_8407() != class_1267.field_5801
         && !hostileException(e)
         && (e instanceof class_1588 || e instanceof class_1569 || e.method_5634() == class_3419.field_15251);
   }

   static {
      Field foxTrustSecondaryField = null;
      Field foxTrustMainField = null;

      try {
         foxTrustSecondaryField = Misc.getFieldReflection(class_4019.class, "DATA_TRUSTED_ID_0", "field_17951", "Lnet/minecraft/class_2940;", "f_28439_");
      } catch (Exception var6) {
         MinimapLogs.LOGGER.error("suppressed exception", var6);
      }

      try {
         foxTrustMainField = Misc.getFieldReflection(class_4019.class, "DATA_TRUSTED_ID_1", "field_17952", "Lnet/minecraft/class_2940;", "f_28440_");
      } catch (Exception var5) {
         MinimapLogs.LOGGER.error("suppressed exception", var5);
      }

      if (foxTrustSecondaryField != null) {
         try {
            boolean accessibleBU = foxTrustSecondaryField.isAccessible();
            foxTrustSecondaryField.setAccessible(true);
            FOX_TRUSTED_UUID_SECONDARY = (class_2940<Optional<UUID>>)foxTrustSecondaryField.get(null);
            foxTrustSecondaryField.setAccessible(accessibleBU);
         } catch (Exception var4) {
            MinimapLogs.LOGGER.error("suppressed exception", var4);
         }
      }

      if (foxTrustMainField != null) {
         try {
            boolean accessibleBU = foxTrustMainField.isAccessible();
            foxTrustMainField.setAccessible(true);
            FOX_TRUSTED_UUID_MAIN = (class_2940<Optional<UUID>>)foxTrustMainField.get(null);
            foxTrustMainField.setAccessible(accessibleBU);
         } catch (Exception var3) {
            MinimapLogs.LOGGER.error("suppressed exception", var3);
         }
      }
   }
}
