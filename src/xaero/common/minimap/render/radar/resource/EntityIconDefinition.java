package xaero.common.minimap.render.radar.resource;

import com.google.gson.annotations.Expose;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import net.minecraft.class_1297;
import net.minecraft.class_2960;
import net.minecraft.class_897;
import xaero.common.MinimapLogs;

public class EntityIconDefinition {
   public static final class_2960 MODEL_TYPE = new class_2960("xaerominimap", "entity/icon/model");
   public static final class_2960 DOT_TYPE = new class_2960("xaerominimap", "entity/icon/dot");
   public static final class_2960 OUTLINED_SPRITE = new class_2960("xaerominimap", "entity/icon/outlined_sprite");
   public static final class_2960 NORMAL_SPRITE = new class_2960("xaerominimap", "entity/icon/normal_sprite");
   public static final class_2960 OLD_SPRITE = new class_2960("xaerominimap", "entity/icon/old_sprite");
   @Expose
   private HashMap<String, String> variants;
   @Expose
   private ArrayList<EntityIconModelConfig> modelConfigs;
   private HashMap<String, class_2960> variantTypes;
   private HashMap<String, EntityIconModelConfig> variantModelConfigs;
   private HashMap<String, class_2960> variantSprites;
   @Expose
   private String variantIdBuilderMethod;
   private Method variantIdBuilderMethodReflect;
   @Expose
   private String variantIdMethod;
   private Method variantIdMethodReflect;

   public class_2960 getVariantType(String variantId) {
      return this.variantTypes == null ? MODEL_TYPE : this.variantTypes.get(variantId);
   }

   public EntityIconModelConfig getModelConfig(String variantId) {
      return this.variantModelConfigs == null ? null : this.variantModelConfigs.get(variantId);
   }

   public class_2960 getSprite(String variantId) {
      return this.variantSprites == null ? null : this.variantSprites.get(variantId);
   }

   public void onConstruct(class_2960 entityId) {
      if (this.variantIdBuilderMethod != null) {
         this.variantIdBuilderMethodReflect = this.convertStringToMethod(
            this.variantIdBuilderMethod, entityId.toString(), "variant ID builder", void.class, StringBuilder.class, class_897.class, class_1297.class
         );
      }

      if (this.variantIdMethod != null) {
         this.variantIdMethodReflect = this.convertStringToMethod(
            this.variantIdMethod, entityId.toString(), "variant ID", String.class, class_897.class, class_1297.class
         );
      }

      if (this.variants == null) {
         this.variants = new HashMap<>();
      }

      if (this.variants.get("default") == null) {
         this.variants.put("default", "model");
      }

      for (Entry<String, String> entry : this.variants.entrySet()) {
         String value = entry.getValue();
         class_2960 type;
         if (!value.equals("model")) {
            String[] valueSplit = value.split(":");
            String iconType = valueSplit[0];
            if (iconType.equals("outlined_sprite")) {
               if (valueSplit.length != 2) {
                  MinimapLogs.LOGGER.info("Skipping invalid icon type: " + value + " for " + entityId);
                  continue;
               }

               type = OUTLINED_SPRITE;
               class_2960 sprite = new class_2960("xaerominimap", "entity/icon/sprite/" + valueSplit[1]);
               if (this.variantSprites == null) {
                  this.variantSprites = new HashMap<>();
               }

               this.variantSprites.put(entry.getKey(), sprite);
            } else if (iconType.equals("sprite") || iconType.equals("normal_sprite")) {
               if (valueSplit.length != 2) {
                  MinimapLogs.LOGGER.info("Skipping invalid icon type: " + value + " for " + entityId);
                  continue;
               }

               type = iconType.equals("sprite") ? OLD_SPRITE : NORMAL_SPRITE;
               class_2960 sprite = new class_2960("xaerominimap", "entity/icon/sprite/" + valueSplit[1]);
               if (this.variantSprites == null) {
                  this.variantSprites = new HashMap<>();
               }

               this.variantSprites.put(entry.getKey(), sprite);
            } else if (iconType.equals("dot")) {
               if (valueSplit.length != 1) {
                  MinimapLogs.LOGGER.info("Skipping invalid icon type: " + value + " for " + entityId);
                  continue;
               }

               type = DOT_TYPE;
            } else {
               if (!iconType.equals("model")) {
                  MinimapLogs.LOGGER.info("Skipping invalid icon type: " + value + " for " + entityId);
                  continue;
               }

               if (valueSplit.length != 2) {
                  MinimapLogs.LOGGER.info("Skipping invalid icon type: " + value + " for " + entityId);
                  continue;
               }

               type = MODEL_TYPE;
               int configIndex = 0;

               try {
                  configIndex = Integer.parseInt(valueSplit[1]);
               } catch (NumberFormatException var10) {
                  MinimapLogs.LOGGER.error("suppressed exception", var10);
               }

               EntityIconModelConfig referencedModelConfig = this.modelConfigs != null && configIndex >= 0 && configIndex < this.modelConfigs.size()
                  ? this.modelConfigs.get(configIndex)
                  : null;
               if (referencedModelConfig == null) {
                  MinimapLogs.LOGGER.info("Specified model config is not defined: " + value + " for " + entityId);
               } else {
                  if (this.variantModelConfigs == null) {
                     this.variantModelConfigs = new HashMap<>();
                  }

                  this.variantModelConfigs.put(entry.getKey(), referencedModelConfig);
               }
            }
         } else {
            type = MODEL_TYPE;
         }

         if (this.variantTypes == null) {
            this.variantTypes = new HashMap<>();
         }

         this.variantTypes.put(entry.getKey(), type);
      }
   }

   public String getVariantIdBuilderMethodString() {
      return this.variantIdBuilderMethod;
   }

   public Method getVariantIdBuilderMethod() {
      return this.variantIdBuilderMethodReflect;
   }

   public void setVariantIdBuilderMethod(Method variantIdBuilderMethodReflect) {
      this.variantIdBuilderMethodReflect = variantIdBuilderMethodReflect;
   }

   private Method convertStringToMethod(String methodPath, String entityId, String methodDisplayName, Class<?> returnType, Class<?>... parameterTypes) {
      Method result = null;
      if (methodPath != null) {
         int lastDot = methodPath.lastIndexOf(46);
         String classPath = methodPath.substring(0, lastDot);
         String methodName = methodPath.substring(lastDot + 1);

         try {
            Class<?> c = Class.forName(classPath);
            result = c.getDeclaredMethod(methodName, parameterTypes);
            if (result.getReturnType() != returnType) {
               MinimapLogs.LOGGER
                  .info(String.format("The return type of the %s method for %s is not %s. Can't use it.", methodDisplayName, entityId, returnType));
               result = null;
            }
         } catch (Exception var11) {
            MinimapLogs.LOGGER
               .error(String.format("Could not find %s method %s defined for %s", methodDisplayName, this.variantIdBuilderMethod, entityId), var11);
         }
      }

      return result;
   }

   public String getOldVariantIdMethodString() {
      return this.variantIdMethod;
   }

   public Method getOldVariantIdMethod() {
      return this.variantIdMethodReflect;
   }

   public void setOldVariantIdMethod(Method variantIdMethodReflect) {
      this.variantIdMethodReflect = variantIdMethodReflect;
   }
}
