package xaero.common.minimap.render.radar;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_276;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4588;
import net.minecraft.class_583;
import net.minecraft.class_630;
import net.minecraft.class_897;
import net.minecraft.class_898;
import org.lwjgl.opengl.GL11;
import xaero.common.IXaeroMinimap;
import xaero.common.icon.XaeroIcon;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.render.radar.armor.EntityIconArmor;
import xaero.common.minimap.render.radar.armor.EntityIconArmorHandler;
import xaero.common.minimap.render.radar.resource.EntityIconDefinition;
import xaero.common.minimap.render.radar.resource.EntityIconDefinitionManager;
import xaero.common.minimap.render.radar.resource.EntityIconModelConfig;
import xaero.common.minimap.render.radar.variant.EntityIconVariantHandler;
import xaero.hud.minimap.MinimapLogs;

public class EntityIconManager {
   public static final XaeroIcon FAILED = new XaeroIcon(null, 0, 0);
   public static final XaeroIcon DOT = new XaeroIcon(null, 0, 0);
   private EntityIconPrerenderer prerenderer;
   private final IXaeroMinimap modMain;
   private Map<class_1299<?>, EntityIconCache> cachedIcons;
   private boolean canPrerender;
   private EntityIconModelConfig defaultModelConfig;
   private final EntityIconDefinitionManager definitionManager;
   private final EntityIconVariantHandler variantHandler;
   private final EntityIconArmorHandler armorHandler;

   public EntityIconManager(IXaeroMinimap modMain, EntityIconPrerenderer prerenderer) {
      this.modMain = modMain;
      this.prerenderer = prerenderer;
      this.definitionManager = new EntityIconDefinitionManager();
      this.variantHandler = new EntityIconVariantHandler();
      this.cachedIcons = new HashMap<>();
      this.definitionManager.reloadResources();
      this.defaultModelConfig = new EntityIconModelConfig();
      this.armorHandler = new EntityIconArmorHandler();
   }

   protected EntityIconCache getVariantIconCache(class_1299<?> entityType) {
      EntityIconCache result = this.cachedIcons.get(entityType);
      if (result == null) {
         this.cachedIcons.put(entityType, result = new EntityIconCache(entityType));
      }

      return result;
   }

   public <T extends class_1297> XaeroIcon getEntityIcon(
      class_332 guiGraphics, T entity, class_276 defaultFramebuffer, MinimapRendererHelper helper, float scale, boolean debug, boolean debugEntityVariantIds
   ) {
      class_1299<?> entityType = entity.method_5864();
      EntityIconDefinition iconDefinition = this.definitionManager.get(class_1299.method_5890(entityType));
      class_898 renderManager = class_310.method_1551().method_1561();
      class_897<? super T> entityRenderer = renderManager.method_3953(entity);
      Object variant = this.variantHandler.getEntityVariant(iconDefinition, entity, entityRenderer);

      while (GL11.glGetError() != 0) {
      }

      if (variant == null) {
         return null;
      } else {
         EntityIconArmor armor = null;
         if (entity instanceof class_1309 && !(entity instanceof class_1657)) {
            armor = this.armorHandler.getArmor((class_1309)entity);
         }

         EntityIconCache entityIconCache = this.getVariantIconCache(entityType);
         EntityIconKey iconKey = new EntityIconKey(variant, armor);
         XaeroIcon cachedValue = entityIconCache.get(iconKey);
         if (entityIconCache.isInvalidVariantClass()) {
            return FAILED;
         } else {
            if (cachedValue == null) {
               String entityVariantString = entityIconCache.getVariantString(iconKey);
               String variantMapKey = entityVariantString;
               class_2960 iconType;
               if (iconDefinition != null) {
                  iconType = entityVariantString == null ? null : iconDefinition.getVariantType(entityVariantString);
                  if (iconType == null) {
                     variantMapKey = "default";
                     iconType = iconDefinition.getVariantType(variantMapKey);
                  }
               } else {
                  iconType = entity instanceof class_1309 ? EntityIconDefinition.MODEL_TYPE : EntityIconDefinition.DOT_TYPE;
               }

               if (debugEntityVariantIds && entityVariantString != null && (this.canPrerender || iconType == EntityIconDefinition.DOT_TYPE)) {
                  class_310.method_1551().field_1705.method_1743().method_1812(class_2561.method_43470(entityVariantString));
               }

               if (iconType == EntityIconDefinition.MODEL_TYPE) {
                  if (this.canPrerender) {
                     EntityIconModelConfig modelConfig = this.defaultModelConfig;
                     EntityIconModelConfig variantModelConfig = iconDefinition == null ? null : iconDefinition.getModelConfig(variantMapKey);
                     if (variantModelConfig != null) {
                        modelConfig = variantModelConfig;
                     }

                     cachedValue = this.prerenderer
                        .prerender(
                           guiGraphics,
                           variant,
                           entityRenderer,
                           entity,
                           defaultFramebuffer,
                           helper,
                           scale,
                           modelConfig,
                           this.defaultModelConfig,
                           null,
                           true,
                           false,
                           debug
                        );
                     entityIconCache.add(iconKey, cachedValue);
                     this.canPrerender = false;
                  } else {
                     cachedValue = null;
                  }
               } else if (iconType == EntityIconDefinition.DOT_TYPE) {
                  cachedValue = DOT;
                  entityIconCache.add(iconKey, cachedValue);
               } else if (this.canPrerender) {
                  class_2960 sprite = iconDefinition.getSprite(variantMapKey);
                  boolean isOutlined = iconType == EntityIconDefinition.OUTLINED_SPRITE;
                  boolean isFlipped = !isOutlined && iconType != EntityIconDefinition.NORMAL_SPRITE;
                  cachedValue = this.prerenderer
                     .prerender(
                        guiGraphics,
                        variant,
                        entityRenderer,
                        entity,
                        defaultFramebuffer,
                        helper,
                        scale,
                        null,
                        this.defaultModelConfig,
                        sprite,
                        isOutlined,
                        isFlipped,
                        debug
                     );
                  entityIconCache.add(iconKey, cachedValue);
                  this.canPrerender = false;
               } else {
                  cachedValue = null;
               }
            }

            return cachedValue;
         }
      }
   }

   public void reset() {
      this.prerenderer.clearAtlases();
      this.cachedIcons.clear();
      MinimapLogs.LOGGER.info("Entity icon manager reset!");
   }

   public void resetResources() {
      this.definitionManager.reloadResources();
   }

   public void allowPrerender() {
      this.canPrerender = true;
   }

   public void onModelRenderDetection(class_583<?> model, class_4588 vertexConsumer, float red, float green, float blue, float alpha) {
      this.prerenderer.onModelRenderDetection(model, vertexConsumer, red, green, blue, alpha);
   }

   public void onModelPartRenderDetection(class_630 modelRenderer, float red, float green, float blue, float alpha) {
      this.prerenderer.onModelPartRenderDetection(modelRenderer, red, green, blue, alpha);
   }
}
