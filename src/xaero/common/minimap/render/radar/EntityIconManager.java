package xaero.common.minimap.render.radar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1498;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_2487;
import net.minecraft.class_2561;
import net.minecraft.class_276;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3298;
import net.minecraft.class_332;
import net.minecraft.class_3489;
import net.minecraft.class_4588;
import net.minecraft.class_583;
import net.minecraft.class_630;
import net.minecraft.class_7923;
import net.minecraft.class_897;
import net.minecraft.class_898;
import org.lwjgl.opengl.GL11;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.icon.XaeroIcon;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.render.radar.resource.EntityIconDefinition;
import xaero.common.minimap.render.radar.resource.EntityIconModelConfig;

public class EntityIconManager {
   public static final XaeroIcon FAILED = new XaeroIcon(null, 0, 0);
   public static final XaeroIcon DOT = new XaeroIcon(null, 0, 0);
   private EntityIconPrerenderer prerenderer;
   private final AXaeroMinimap modMain;
   private Map<class_2960, EntityIconDefinition> iconDefinitions;
   private Map<String, XaeroIcon> cachedTextures;
   private boolean canPrerender;
   private Gson gson;
   private StringBuilder entityStringBuilder;
   private EntityIconModelConfig defaultModelConfig;

   public EntityIconManager(AXaeroMinimap modMain, EntityIconPrerenderer prerenderer) {
      this.modMain = modMain;
      this.prerenderer = prerenderer;
      this.iconDefinitions = new HashMap<>();
      this.cachedTextures = new HashMap<>();
      this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
      this.resetResources();
      this.entityStringBuilder = new StringBuilder();
      this.defaultModelConfig = new EntityIconModelConfig();
   }

   protected String getSavedEntityId(class_1297 e) {
      class_1299<?> entityType = e.method_5864();
      class_2960 identifier = class_1299.method_5890(entityType);
      return entityType.method_5893() && identifier != null ? identifier.toString() : null;
   }

   public <T extends class_1297> XaeroIcon getEntityHeadTexture(
      class_332 guiGraphics, T entity, class_276 defaultFramebuffer, MinimapRendererHelper helper, float scale, boolean debug, boolean debugEntityVariantIds
   ) {
      class_2960 entityId = class_1299.method_5890(entity.method_5864());
      EntityIconDefinition iconDefinition = this.iconDefinitions.get(entityId);
      class_898 renderManager = class_310.method_1551().method_1561();
      class_897<? super T> entityRenderer = renderManager.method_3953(entity);
      StringBuilder entityStringBuilder = this.entityStringBuilder;
      entityStringBuilder.setLength(0);
      boolean variantIdAppended = false;
      if (iconDefinition != null) {
         Method variantIdBuilderMethod = iconDefinition.getVariantIdBuilderMethod();
         if (variantIdBuilderMethod != null) {
            try {
               variantIdBuilderMethod.invoke(null, entityStringBuilder, entityRenderer, entity);
               variantIdAppended = true;
            } catch (Throwable var23) {
               MinimapLogs.LOGGER
                  .error("Exception while using the variant builder ID method " + iconDefinition.getVariantIdBuilderMethodString() + " defined for " + entityId);
               MinimapLogs.LOGGER
                  .error(
                     "If the exception is on another mod's end, suppressing it here could lead to more issues. Please report to appropriate mod devs.", var23
                  );
               iconDefinition.setVariantIdBuilderMethod(null);
            }
         } else {
            Method variantOldIdMethod = iconDefinition.getOldVariantIdMethod();
            if (variantOldIdMethod != null) {
               try {
                  String entityVariantString = (String)variantOldIdMethod.invoke(null, entityRenderer, entity);
                  entityStringBuilder.append(entityVariantString);
                  variantIdAppended = true;
               } catch (Throwable var22) {
                  MinimapLogs.LOGGER
                     .error("Exception while using the variant ID method " + iconDefinition.getOldVariantIdMethodString() + " defined for " + entityId);
                  MinimapLogs.LOGGER
                     .error(
                        "If the exception is on another mod's end, suppressing it here could lead to more issues. Please report to appropriate mod devs.",
                        var22
                     );
                  iconDefinition.setOldVariantIdMethod(null);
               }
            }
         }
      }

      if (!variantIdAppended) {
         EntityIconDefinitions.buildVariantIdString(entityStringBuilder, entityRenderer, entity);
      }

      while (GL11.glGetError() != 0) {
      }

      String entityVariantString = entityStringBuilder.toString();
      entityStringBuilder.append("%").append(this.getSavedEntityId(entity));
      if (entity instanceof class_1309 && !(entity instanceof class_1657)) {
         class_1309 livingEntity = (class_1309)entity;
         class_1304 relevantArmourSlot = livingEntity instanceof class_1498 ? class_1304.field_6174 : class_1304.field_6169;
         class_1799 armorItemStack = livingEntity.method_6118(relevantArmourSlot);
         if (armorItemStack != null && armorItemStack != class_1799.field_8037) {
            entityStringBuilder.append("%").append(class_7923.field_41178.method_10221(armorItemStack.method_7909()));
         }

         if (armorItemStack.method_31573(class_3489.field_41890)
            && armorItemStack.method_7969() != null
            && armorItemStack.method_7969().method_10573("Trim", 10)) {
            class_2487 trimTag = armorItemStack.method_7969().method_10562("Trim");
            if (trimTag.method_10573("material", 8) && trimTag.method_10573("pattern", 8)) {
               entityStringBuilder.append("%").append(trimTag.method_10558("material")).append("%").append(trimTag.method_10558("pattern"));
            } else {
               entityStringBuilder.append("%").append("inline_material").append("%").append("inline_pattern");
            }
         }
      }

      String cacheKey = entityStringBuilder.toString();
      XaeroIcon cachedValue = this.cachedTextures.get(cacheKey);
      if (cachedValue == null) {
         String variantMapKey = entityVariantString;
         class_2960 iconType;
         if (iconDefinition != null) {
            iconType = iconDefinition.getVariantType(entityVariantString);
            if (iconType == null) {
               variantMapKey = "default";
               iconType = iconDefinition.getVariantType(variantMapKey);
            }
         } else {
            iconType = entity instanceof class_1309 ? EntityIconDefinition.MODEL_TYPE : EntityIconDefinition.DOT_TYPE;
         }

         if (debugEntityVariantIds && (this.canPrerender || iconType == EntityIconDefinition.DOT_TYPE)) {
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
                     cacheKey,
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
               this.cachedTextures.put(cacheKey, cachedValue);
               this.canPrerender = false;
            } else {
               cachedValue = null;
            }
         } else if (iconType == EntityIconDefinition.DOT_TYPE) {
            cachedValue = DOT;
            this.cachedTextures.put(cacheKey, cachedValue);
         } else if (this.canPrerender) {
            class_2960 sprite = iconDefinition.getSprite(variantMapKey);
            boolean isOutlined = iconType == EntityIconDefinition.OUTLINED_SPRITE;
            boolean isFlipped = !isOutlined && iconType != EntityIconDefinition.NORMAL_SPRITE;
            cachedValue = this.prerenderer
               .prerender(
                  guiGraphics,
                  cacheKey,
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
            this.cachedTextures.put(cacheKey, cachedValue);
            this.canPrerender = false;
         } else {
            cachedValue = null;
         }
      }

      return cachedValue;
   }

   public void reset() {
      this.prerenderer.clearAtlases();
      this.cachedTextures.clear();
      MinimapLogs.LOGGER.info("Entity icon manager reset!");
   }

   public void resetResources() {
      MinimapLogs.LOGGER.info("Reloading entity icon resources...");
      Set<class_2960> entityIds = class_7923.field_41177.method_10235();
      Gson gson = this.gson;
      int attempts = 5;

      for (int i = 0; i < attempts; i++) {
         try {
            this.resetResourcesAttempt(gson, entityIds);
            break;
         } catch (IOException var6) {
            if (i == attempts - 1) {
               throw new RuntimeException(var6);
            }
         }
      }

      MinimapLogs.LOGGER.info("Done!");
   }

   private void resetResourcesAttempt(Gson gson, Set<class_2960> entityIds) throws IOException {
      this.iconDefinitions.clear();

      for (class_2960 id : entityIds) {
         InputStream resourceInput = null;
         BufferedReader reader = null;
         String entityDefinitionJson = null;

         try {
            Optional<class_3298> oResource = class_310.method_1551()
               .method_1478()
               .method_14486(new class_2960("xaerominimap", "entity/icon/definition/" + id.method_12836() + "/" + id.method_12832() + ".json"));
            if (!oResource.isPresent()) {
               continue;
            }

            class_3298 resource = oResource.get();
            if (resource == null) {
               continue;
            }

            resourceInput = resource.method_14482();
            reader = new BufferedReader(new InputStreamReader(resourceInput));
            StringBuilder stringBuilder = new StringBuilder();
            reader.lines().forEach(line -> {
               stringBuilder.append(line);
               stringBuilder.append('\n');
            });
            entityDefinitionJson = stringBuilder.toString();
         } finally {
            if (reader != null) {
               reader.close();
            }

            if (resourceInput != null) {
               resourceInput.close();
            }
         }

         try {
            EntityIconDefinition entityIconDefinition = (EntityIconDefinition)gson.fromJson(entityDefinitionJson, EntityIconDefinition.class);
            entityIconDefinition.onConstruct(id);
            this.iconDefinitions.put(id, entityIconDefinition);
         } catch (JsonSyntaxException var14) {
            MinimapLogs.LOGGER.error("Json syntax exception when loading the entity icon definition for " + id + ".", var14);
         }
      }
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
