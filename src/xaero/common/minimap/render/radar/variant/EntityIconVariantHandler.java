package xaero.common.minimap.render.radar.variant;

import java.lang.reflect.Method;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_2960;
import net.minecraft.class_897;
import xaero.common.minimap.render.radar.EntityIconDefinitions;
import xaero.common.minimap.render.radar.resource.EntityIconDefinition;
import xaero.hud.minimap.MinimapLogs;

public class EntityIconVariantHandler {
   private final StringBuilder legacyEntityStringBuilder = new StringBuilder();

   public <T extends class_1297> Object getEntityVariant(EntityIconDefinition iconDefinition, T entity, class_897<? super T> entityRenderer) {
      Object variant = null;
      class_2960 entityTexture = null;

      try {
         class_2960 entityTextureUnchecked = entityRenderer.method_3931(entity);
         entityTexture = entityTextureUnchecked;
      } catch (Throwable var10) {
         MinimapLogs.LOGGER.error("Exception while fetching entity texture to build its variant ID for " + class_1299.method_5890(entity.method_5864()));
         MinimapLogs.LOGGER
            .error(
               "The exception is most likely on another mod's end and suppressing it here could lead to more issues. Please report to appropriate mod devs.",
               var10
            );
      }

      if (iconDefinition != null) {
         Method variantMethod = iconDefinition.getVariantMethod();
         if (variantMethod != null) {
            try {
               variantMethod.invoke(null, entityTexture, entityRenderer, entity);
            } catch (Throwable var9) {
               class_2960 entityId = class_1299.method_5890(entity.method_5864());
               MinimapLogs.LOGGER.error("Exception while using the variant ID method " + iconDefinition.getVariantMethodString() + " defined for " + entityId);
               MinimapLogs.LOGGER
                  .error(
                     "If the exception is on another mod's end, suppressing it here could lead to more issues. Please report to appropriate mod devs.", var9
                  );
               iconDefinition.setVariantMethod(null);
            }
         } else {
            variant = this.getLegacyVariantId(iconDefinition, entity, entityRenderer);
         }
      }

      if (variant == null) {
         variant = EntityIconDefinitions.getVariant(entityTexture, entityRenderer, entity);
      }

      return variant;
   }

   private <T extends class_1297> String getLegacyVariantId(EntityIconDefinition iconDefinition, T entity, class_897<? super T> entityRenderer) {
      boolean variantIdAppended = false;
      Method variantIdBuilderMethod = iconDefinition.getVariantIdBuilderMethod();
      if (variantIdBuilderMethod != null && !variantIdBuilderMethod.equals(EntityIconDefinitions.BUILD_VARIANT_ID_STRING_METHOD)) {
         this.legacyEntityStringBuilder.setLength(0);

         try {
            variantIdBuilderMethod.invoke(null, this.legacyEntityStringBuilder, entityRenderer, entity);
            variantIdAppended = true;
         } catch (Throwable var10) {
            class_2960 entityId = class_1299.method_5890(entity.method_5864());
            MinimapLogs.LOGGER
               .error("Exception while using the variant builder ID method " + iconDefinition.getVariantIdBuilderMethodString() + " defined for " + entityId);
            MinimapLogs.LOGGER
               .error("If the exception is on another mod's end, suppressing it here could lead to more issues. Please report to appropriate mod devs.", var10);
            iconDefinition.setVariantIdBuilderMethod(null);
         }
      } else {
         Method variantOldIdMethod = iconDefinition.getOldVariantIdMethod();
         if (variantOldIdMethod != null && !variantOldIdMethod.equals(EntityIconDefinitions.GET_VARIANT_ID_STRING_METHOD)) {
            this.legacyEntityStringBuilder.setLength(0);

            try {
               String entityVariantString = (String)variantOldIdMethod.invoke(null, entityRenderer, entity);
               this.legacyEntityStringBuilder.append(entityVariantString);
               variantIdAppended = true;
            } catch (Throwable var9) {
               class_2960 entityId = class_1299.method_5890(entity.method_5864());
               MinimapLogs.LOGGER
                  .error("Exception while using the variant ID method " + iconDefinition.getOldVariantIdMethodString() + " defined for " + entityId);
               MinimapLogs.LOGGER
                  .error(
                     "If the exception is on another mod's end, suppressing it here could lead to more issues. Please report to appropriate mod devs.", var9
                  );
               iconDefinition.setOldVariantIdMethod(null);
            }
         }
      }

      return !variantIdAppended ? null : this.legacyEntityStringBuilder.toString();
   }
}
