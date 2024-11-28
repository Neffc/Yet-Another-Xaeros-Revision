package xaero.common.minimap.render.radar.element;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1921;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import org.lwjgl.opengl.GL11;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.icon.XaeroIcon;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.EntityRadarCategoryManager;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.render.radar.EntityIconManager;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.render.TextureLocations;

public final class RadarRenderer extends MinimapElementRenderer<class_1297, RadarRenderContext> {
   private final IXaeroMinimap modMain;
   private final EntityIconManager entityIconManager;
   private final Minimap minimap;
   private MinimapElementRenderInfo compatibleRenderInfo;
   private final RadarRenderProvider radarRenderProvider;
   private boolean ignoreProviderCheck;
   private class_4598 minimapBufferSource;
   private double maxDistanceSquared;

   private RadarRenderer(
      IXaeroMinimap modMain,
      EntityIconManager entityIconManager,
      Minimap minimap,
      RadarElementReader elementReader,
      RadarRenderProvider provider,
      RadarRenderContext context
   ) {
      super(elementReader, provider, context);
      this.modMain = modMain;
      this.entityIconManager = entityIconManager;
      this.minimap = minimap;
      this.radarRenderProvider = provider;
   }

   @Override
   public void preRender(
      MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      ModSettings settings = this.modMain.getSettings();
      this.entityIconManager.allowPrerender();
      RenderSystem.setShaderTexture(0, TextureLocations.GUI_TEXTURES);
      class_310.method_1551().method_1531().method_22813(TextureLocations.GUI_TEXTURES);
      if (settings.getSmoothDots()) {
         GL11.glTexParameteri(3553, 10240, 9729);
         GL11.glTexParameteri(3553, 10241, 9729);
      } else {
         GL11.glTexParameteri(3553, 10240, 9728);
         GL11.glTexParameteri(3553, 10241, 9728);
      }

      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(770, 771, 1, 771);
      class_1921 dotsRenderType = settings.getSmoothDots() ? CustomRenderTypes.GUI_BILINEAR : CustomRenderTypes.GUI_NEAREST;
      MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
      this.context.minimapRadar = session.getProcessor().getEntityRadar();
      this.context.reversedOrder = ModSettings.keyReverseEntityRadar.method_1434();
      this.context.nameScale = settings.getDotNameScale();
      this.context.smoothDots = settings.getSmoothDots();
      this.context.debugEntityIcons = settings.debugEntityIcons;
      this.context.debugEntityVariantIds = settings.debugEntityVariantIds;
      this.context.dotsStyle = settings.getDotsStyle();
      this.context.dotsRenderType = dotsRenderType;
      RenderSystem.disableDepthTest();
      vanillaBufferSource.method_22993();
      this.minimapBufferSource = this.modMain.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
      this.context.dotsBufferBuilder = this.minimapBufferSource.getBuffer(dotsRenderType);
      this.context.nameBgBuilder = this.minimapBufferSource.getBuffer(CustomRenderTypes.RADAR_NAME_BGS);
      this.context.iconsRenderer = multiTextureRenderTypeRenderers.getRenderer(
         t -> RenderSystem.setShaderTexture(0, t), MultiTextureRenderTypeRendererProvider::defaultTextureBind, CustomRenderTypes.GUI_BILINEAR
      );
      this.context.font = class_310.method_1551().field_1772;
      this.context.helper = this.modMain.getMinimap().getMinimapFBORenderer().getHelper();
      double playerDimDiv = renderInfo.backgroundCoordinateScale / renderInfo.renderEntityDimensionScale;
      this.maxDistanceSquared = this.context.minimapRadar.getMaxDistance(session.getProcessor(), settings.minimapShape == 1) * playerDimDiv * playerDimDiv;
   }

   @Override
   public void postRender(
      MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      if (this.context.reversedOrder) {
         this.minimapBufferSource.method_22994(this.context.dotsRenderType);
      }

      multiTextureRenderTypeRenderers.draw(this.context.iconsRenderer);
      if (!this.context.reversedOrder) {
         this.minimapBufferSource.method_22994(this.context.dotsRenderType);
      }

      this.minimapBufferSource.method_22993();
      RenderSystem.enableDepthTest();
      RenderSystem.depthFunc(515);
      this.context.iconsRenderer = null;
      RenderSystem.setShaderTexture(0, TextureLocations.GUI_TEXTURES);
      class_310.method_1551().method_1531().method_22813(TextureLocations.GUI_TEXTURES);
      GL11.glTexParameteri(3553, 10240, 9728);
      GL11.glTexParameteri(3553, 10241, 9728);
   }

   public boolean renderElement(
      class_1297 e,
      boolean highlighted,
      boolean outOfBounds,
      double optionalDepth,
      float optionalScale,
      double partialX,
      double partialY,
      MinimapElementRenderInfo renderInfo,
      class_332 guiGraphics,
      class_4598 vanillaBufferSource
   ) {
      if (!highlighted && !this.ignoreProviderCheck && !this.radarRenderProvider.isUsed()) {
         throw new IllegalStateException("this renderer can only be used with its element provider");
      } else {
         if (renderInfo.location == MinimapElementRenderLocation.IN_MINIMAP) {
            double offx = e.method_23317() - this.context.renderEntity.method_23317();
            double offx2 = offx * offx;
            if (offx2 > this.maxDistanceSquared) {
               return false;
            }

            double offy = e.method_23321() - this.context.renderEntity.method_23321();
            double offy2 = offy * offy;
            if (offy2 > this.maxDistanceSquared) {
               return false;
            }
         }

         int nameSetting = this.context.nameSettingForList;
         if (highlighted && !this.radarRenderProvider.isUsed()) {
            EntityRadarCategoryManager entityCategoryManager = this.context.minimapRadar.getEntityCategoryManager();
            EntityRadarCategory rootCategory = entityCategoryManager.getRootCategory();
            EntityRadarCategory category = entityCategoryManager.getRuleResolver().resolve(rootCategory, e, renderInfo.player);
            this.radarRenderProvider.setupContextForCategory(category, this.context);
            this.radarRenderProvider.setupContextForEntity(e, this.context);
            nameSetting = category.getSettingValue(EntityRadarCategorySettings.NAMES).intValue();
         }

         RadarRenderContext context = this.context;
         MinimapRendererHelper helper = context.helper;
         class_4587 matrixStack = guiGraphics.method_51448();
         if (e instanceof class_1657) {
            if (this.modMain.getTrackedPlayerRenderer().getCollector().playerExists(e.method_5667())) {
               this.modMain.getTrackedPlayerRenderer().getCollector().confirmPlayerRadarRender((class_1657)e);
            }

            if (this.modMain.getSupportMods().worldmap()
               && (
                  this.modMain.getSupportMods().worldmapSupport.hasTrackedPlayerSystemSupport()
                     || this.modMain.getSupportMods().pac() && this.modMain.getSupportMods().worldmapSupport.supportsPacPlayerRadarFilter()
               )) {
               this.modMain.getSupportMods().worldmapSupport.confirmPlayerRadarRender((class_1657)e);
            }
         }

         MinimapElementRenderLocation location = renderInfo.location;
         class_276 framebuffer = renderInfo.framebuffer;
         class_1297 renderEntity = renderInfo.renderEntity;
         boolean cave = renderInfo.cave;
         class_1657 player = renderInfo.player;
         float optionScaleAdjust = location == MinimapElementRenderLocation.OVER_MINIMAP ? 0.5F : 1.0F;
         optionalScale *= optionScaleAdjust;
         int nameOffsetX = 0;
         int nameOffsetY = 0;
         matrixStack.method_22903();
         boolean icon = context.icon;
         boolean name = context.name;
         if (highlighted && nameSetting > 0) {
            name = true;
         }

         XaeroIcon entityHeadTexture = icon
            ? this.entityIconManager
               .getEntityIcon(guiGraphics, e, framebuffer, helper, (float)context.iconScale, context.debugEntityIcons, context.debugEntityVariantIds)
            : null;
         if (entityHeadTexture == EntityIconManager.DOT) {
            entityHeadTexture = null;
            icon = false;
         }

         float offh = (float)(renderEntity.method_23318() - e.method_23318());
         matrixStack.method_22904(partialX, partialY, 0.0);
         boolean usableIcon = entityHeadTexture != null && entityHeadTexture != EntityIconManager.FAILED;
         double dotsScale;
         if (usableIcon) {
            dotsScale = context.iconScale;
            double clampedScale = Math.max(1.0, dotsScale * (double)optionalScale);
            matrixStack.method_22905((float)clampedScale, (float)clampedScale, 1.0F);
            float brightness = context.minimapRadar.getEntityBrightness(offh, context.heightLimit, context.startFadingAt, context.heightBasedFade);
            float r;
            float g;
            float b;
            float a;
            if (!cave) {
               b = brightness;
               g = brightness;
               r = brightness;
               a = 1.0F;
            } else {
               b = 1.0F;
               g = 1.0F;
               r = 1.0F;
               a = brightness;
            }

            helper.prepareMyTexturedColoredModalRect(
               matrixStack.method_23760().method_23761(),
               -31.0F,
               -31.0F,
               entityHeadTexture.getOffsetX() + 1,
               entityHeadTexture.getOffsetY() + 1,
               62.0F,
               62.0F,
               62.0F,
               (float)entityHeadTexture.getTextureAtlas().getWidth(),
               entityHeadTexture.getTextureAtlas().getTextureId(),
               r,
               g,
               b,
               a,
               context.iconsRenderer
            );
         } else {
            boolean smooth = context.smoothDots;
            if (!smooth) {
               optionalScale = (float)Math.ceil((double)optionalScale);
            }

            matrixStack.method_22905(optionalScale, optionalScale, 1.0F);
            if (icon && context.displayNameWhenIconFails && entityHeadTexture == EntityIconManager.FAILED) {
               name = true;
            }

            dotsScale = context.dotScale;
            int color = context.minimapRadar
               .getEntityColour(
                  player, e, offh, cave, context.entityCategory, context.heightLimit, context.startFadingAt, context.heightBasedFade, context.colorIndex
               );
            float r = (float)(color >> 16 & 0xFF) / 255.0F;
            float g = (float)(color >> 8 & 0xFF) / 255.0F;
            float b = (float)(color & 0xFF) / 255.0F;
            float a = (float)(color >> 24 & 0xFF) / 255.0F;
            int dotTextureX = 0;
            int dotTextureY = 0;
            int dotTextureW = 0;
            int dotTextureH = 0;
            float dotOffset = 0.0F;
            double dotActualScale = (double)optionalScale;
            short var73;
            byte var76;
            byte var78;
            if (context.dotsStyle == 1) {
               if (smooth) {
                  dotTextureX = 1;
                  var73 = 88;
               } else {
                  dotsScale = (double)((int)dotsScale);
                  dotTextureX = 9;
                  var73 = 77;
               }

               dotOffset = -3.5F;
               var78 = 8;
               var76 = 8;
               dotActualScale *= dotsScale;
               matrixStack.method_22905((float)dotsScale, (float)dotsScale, 1.0F);
            } else {
               switch (context.dotSize) {
                  case 1:
                     dotOffset = -4.5F;
                     var73 = 108;
                     var78 = 9;
                     var76 = 9;
                     break;
                  case 2:
                  default:
                     dotOffset = -5.5F;
                     var73 = 117;
                     var78 = 11;
                     var76 = 11;
                     break;
                  case 3:
                     dotOffset = -7.5F;
                     var73 = 128;
                     var78 = 15;
                     var76 = 15;
                     break;
                  case 4:
                     dotOffset = -10.5F;
                     var73 = 160;
                     var78 = 21;
                     var76 = 21;
               }
            }

            if (!smooth) {
               double dotRadius = (double)(-dotOffset) * dotActualScale;
               double dotRadiusPartial = dotRadius - (double)((int)dotRadius);
               nameOffsetX = partialX - dotRadiusPartial <= -0.5 ? -1 : 0;
               nameOffsetY = partialY - dotRadiusPartial < -0.5 ? -1 : 0;
            }

            helper.addTexturedColoredRectToExistingBuffer(
               matrixStack.method_23760().method_23761(), context.dotsBufferBuilder, dotOffset, dotOffset, dotTextureX, var73, var76, var78, r, g, b, a, 256.0F
            );
         }

         matrixStack.method_22909();
         int displayY = context.displayY;
         if (name || displayY > 0) {
            matrixStack.method_22903();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.method_22904((double)nameOffsetX, (double)nameOffsetY, optionalDepth + 0.1F);
            int offsetY = usableIcon ? 11 : 5;
            matrixStack.method_46416(0.0F, (float)Math.round((double)offsetY * dotsScale * (double)optionalScale), 0.0F);
            if (optionalScale < 1.0F) {
               optionalScale = 1.0F;
            }

            double dotNameScale = context.nameScale * (double)optionalScale;
            matrixStack.method_22905((float)dotNameScale, (float)dotNameScale, 1.0F);
            String yValueString = null;
            if (displayY > 0) {
               int yInt = (int)Math.floor(e.method_23318());
               int pYInt = (int)Math.floor(renderEntity.method_23318());
               if (displayY == 1) {
                  yValueString = yInt + "";
               } else if (displayY == 2) {
                  yValueString = yInt - pYInt + "";
               } else {
                  yValueString = "";
               }

               yValueString = yValueString + (yInt > pYInt ? "↑" : (yInt != pYInt ? "↓" : ""));
               if (yValueString.length() == 0) {
                  yValueString = "-";
               }
            }

            class_327 font = context.font;
            if (name) {
               class_2561 component = Misc.getFixedDisplayName(e);
               if (component != null) {
                  String entityName = component.getString();
                  if (displayY > 0) {
                     entityName = entityName + "(" + yValueString + ")";
                  }

                  int nameW = font.method_1727(entityName);
                  helper.addColoredRectToExistingBuffer(
                     matrixStack.method_23760().method_23761(),
                     context.nameBgBuilder,
                     (float)(-nameW / 2 - 2),
                     -1.0F,
                     nameW + 3,
                     10,
                     0.0F,
                     0.0F,
                     0.0F,
                     0.3529412F
                  );
                  Misc.drawNormalText(matrixStack, entityName, (float)(-nameW / 2), 0.0F, -1, false, this.minimapBufferSource);
               }
            } else if (displayY > 0) {
               int yStringW = font.method_1727(yValueString);
               helper.addColoredRectToExistingBuffer(
                  matrixStack.method_23760().method_23761(),
                  context.nameBgBuilder,
                  (float)(-yStringW / 2 - 2),
                  -1.0F,
                  yStringW + 3,
                  10,
                  0.0F,
                  0.0F,
                  0.0F,
                  0.3529412F
               );
               Misc.drawNormalText(matrixStack, yValueString, (float)(-yStringW / 2), 0.0F, -1, false, this.minimapBufferSource);
            }

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(770, 771, 1, 771);
            matrixStack.method_22909();
         }

         return true;
      }
   }

   @Deprecated
   public void renderEntityDotToFBO(
      int location,
      boolean highlit,
      class_332 guiGraphics,
      MinimapProcessor minimap,
      class_1657 p,
      class_1297 renderEntity,
      class_1297 e,
      float partial,
      boolean name,
      boolean icon,
      MinimapRadar minimapRadar,
      int style,
      boolean smooth,
      boolean debug,
      boolean debugEntityVariantIds,
      boolean cave,
      double dotNameScale,
      class_4598 textRenderTypeBuffer,
      class_1921 dotsRenderType,
      class_4588 dotsBufferBuilder,
      MultiTextureRenderTypeRenderer iconsRenderer,
      class_4588 nameBgBuilder,
      int dotIndex,
      boolean displayNameWhenIconFails,
      int heightLimit,
      boolean heightBasedFade,
      int startFadingAt,
      double iconScale,
      int dotSize,
      int colorIndex,
      int displayY,
      EntityRadarCategory category,
      MinimapRendererHelper helper,
      class_327 font,
      class_276 framebuffer,
      float minimapScale
   ) {
      this.renderEntityDotToFBO(
         MinimapElementRenderLocation.fromIndex(location),
         highlit,
         guiGraphics,
         minimap,
         p,
         renderEntity,
         e,
         partial,
         name,
         icon,
         minimapRadar,
         style,
         smooth,
         debug,
         debugEntityVariantIds,
         cave,
         dotNameScale,
         textRenderTypeBuffer,
         dotsRenderType,
         dotsBufferBuilder,
         iconsRenderer,
         nameBgBuilder,
         dotIndex,
         displayNameWhenIconFails,
         heightLimit,
         heightBasedFade,
         startFadingAt,
         iconScale,
         dotSize,
         colorIndex,
         displayY,
         category,
         helper,
         font,
         framebuffer,
         minimapScale
      );
   }

   public void renderEntityDotToFBO(
      MinimapElementRenderLocation location,
      boolean highlit,
      class_332 guiGraphics,
      MinimapProcessor minimap,
      class_1657 p,
      class_1297 renderEntity,
      class_1297 e,
      float partial,
      boolean name,
      boolean icon,
      MinimapRadar minimapRadar,
      int style,
      boolean smooth,
      boolean debug,
      boolean debugEntityVariantIds,
      boolean cave,
      double dotNameScale,
      class_4598 textRenderTypeBuffer,
      class_1921 dotsRenderType,
      class_4588 dotsBufferBuilder,
      MultiTextureRenderTypeRenderer iconsRenderer,
      class_4588 nameBgBuilder,
      int dotIndex,
      boolean displayNameWhenIconFails,
      int heightLimit,
      boolean heightBasedFade,
      int startFadingAt,
      double iconScale,
      int dotSize,
      int colorIndex,
      int displayY,
      EntityRadarCategory category,
      MinimapRendererHelper helper,
      class_327 font,
      class_276 framebuffer,
      float minimapScale
   ) {
      this.context.nameScale = dotNameScale;
      this.context.smoothDots = smooth;
      this.context.debugEntityIcons = debug;
      this.context.debugEntityVariantIds = debugEntityVariantIds;
      this.context.dotsStyle = style;
      this.context.dotsRenderType = dotsRenderType;
      this.context.dotsBufferBuilder = dotsBufferBuilder;
      this.context.nameBgBuilder = nameBgBuilder;
      this.context.iconsRenderer = iconsRenderer;
      this.context.font = font;
      this.context.helper = helper;
      this.context.name = name;
      this.context.icon = icon;
      this.context.displayNameWhenIconFails = displayNameWhenIconFails;
      this.context.heightLimit = heightLimit;
      this.context.heightBasedFade = heightBasedFade;
      this.context.startFadingAt = startFadingAt;
      this.context.iconScale = iconScale;
      this.context.dotSize = dotSize;
      this.context.colorIndex = colorIndex;
      this.context.displayY = displayY;
      this.context.entityCategory = category;
      this.context.minimapRadar = minimapRadar;
      MinimapElementRenderInfo renderInfo = new MinimapElementRenderInfo(
         location, renderEntity, p, new class_243(0.0, 0.0, 0.0), cave, 1.0F, framebuffer, 1.0, null
      );
      this.ignoreProviderCheck = true;
      this.renderElement(e, highlit, false, 0.0, minimapScale, 0.0, 0.0, renderInfo, guiGraphics, textRenderTypeBuffer);
      this.ignoreProviderCheck = false;
      this.context.iconsRenderer = null;
   }

   @Override
   public boolean shouldRender(MinimapElementRenderLocation location) {
      return this.minimap.usingFBO()
         && (
            location == MinimapElementRenderLocation.WORLD_MAP
               || location == MinimapElementRenderLocation.WORLD_MAP_MENU
               || this.modMain.getSettings().getEntityRadar()
         );
   }

   @Deprecated
   public boolean renderElement(
      int location,
      boolean highlit,
      boolean outOfBounds,
      class_332 guiGraphics,
      class_4598 renderTypeBuffers,
      class_327 font,
      class_276 framebuffer,
      MinimapRendererHelper helper,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      int elementIndex,
      double optionalDepth,
      float optionalScale,
      class_1297 element,
      double partialX,
      double partialY,
      boolean cave,
      float partialTicks
   ) {
      if (this.compatibleRenderInfo == null) {
         MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
         this.compatibleRenderInfo = new MinimapElementRenderInfo(
            MinimapElementRenderLocation.fromIndex(location),
            renderEntity,
            player,
            new class_243(renderX, renderY, renderZ),
            cave,
            partialTicks,
            framebuffer,
            session.getProcessor().getLastMapDimensionScale(),
            session.getProcessor().getLastMapDimension()
         );
      }

      return this.renderElement(
         element, highlit, outOfBounds, optionalDepth, optionalScale, partialX, partialY, this.compatibleRenderInfo, guiGraphics, renderTypeBuffers
      );
   }

   @Deprecated
   @Override
   public void preRender(
      int location,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      IXaeroMinimap modMain,
      class_4598 renderTypeBuffers,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      this.preRender(
         new MinimapElementRenderInfo(
            MinimapElementRenderLocation.fromIndex(location),
            renderEntity,
            player,
            new class_243(renderX, renderY, renderZ),
            false,
            1.0F,
            null,
            class_310.method_1551().field_1687.method_8597().comp_646(),
            class_310.method_1551().field_1687.method_27983()
         ),
         renderTypeBuffers,
         multiTextureRenderTypeRenderers
      );
   }

   @Deprecated
   @Override
   public void postRender(
      int location,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      IXaeroMinimap modMain,
      class_4598 renderTypeBuffers,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      this.postRender(this.compatibleRenderInfo, renderTypeBuffers, multiTextureRenderTypeRenderers);
      this.compatibleRenderInfo = null;
   }

   @Deprecated
   @Override
   public boolean shouldRender(int location) {
      return this.shouldRender(MinimapElementRenderLocation.fromIndex(location));
   }

   public static final class Builder {
      private IXaeroMinimap modMain;
      private EntityIconManager entityIconManager;
      private Minimap minimap;

      private Builder() {
      }

      private RadarRenderer.Builder setDefault() {
         this.setEntityIconManager(null);
         return this;
      }

      public RadarRenderer.Builder setModMain(IXaeroMinimap modMain) {
         this.modMain = modMain;
         return this;
      }

      public RadarRenderer.Builder setEntityIconManager(EntityIconManager entityIconManager) {
         this.entityIconManager = entityIconManager;
         return this;
      }

      public RadarRenderer.Builder setMinimap(Minimap minimap) {
         this.minimap = minimap;
         return this;
      }

      public RadarRenderer build() {
         if (this.modMain != null && this.entityIconManager != null && this.minimap != null) {
            RadarElementReader elementReader = new RadarElementReader();
            RadarRenderProvider provider = new RadarRenderProvider();
            RadarRenderContext context = new RadarRenderContext();
            return new RadarRenderer(this.modMain, this.entityIconManager, this.minimap, elementReader, provider, context);
         } else {
            throw new IllegalStateException();
         }
      }

      public static RadarRenderer.Builder begin() {
         return new RadarRenderer.Builder().setDefault();
      }
   }
}
