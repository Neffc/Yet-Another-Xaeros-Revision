package xaero.common.minimap.render.radar.element;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1921;
import net.minecraft.class_2561;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import org.lwjgl.opengl.GL11;
import xaero.common.AXaeroMinimap;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.icon.XaeroIcon;
import xaero.common.interfaces.render.InterfaceRenderer;
import xaero.common.minimap.MinimapInterface;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.render.radar.EntityIconManager;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;

public final class RadarRenderer extends MinimapElementRenderer<class_1297, RadarRenderContext> {
   private final AXaeroMinimap modMain;
   private final EntityIconManager entityIconManager;
   private final MinimapInterface minimapInterface;

   private RadarRenderer(
      AXaeroMinimap modMain,
      EntityIconManager entityIconManager,
      MinimapInterface minimapInterface,
      RadarElementReader elementReader,
      RadarRenderProvider provider,
      RadarRenderContext context
   ) {
      super(elementReader, provider, context);
      this.modMain = modMain;
      this.entityIconManager = entityIconManager;
      this.minimapInterface = minimapInterface;
   }

   @Override
   public void preRender(
      int location,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      AXaeroMinimap modMain,
      class_4598 renderTypeBuffers,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      ModSettings settings = modMain.getSettings();
      this.entityIconManager.allowPrerender();
      RenderSystem.setShaderTexture(0, InterfaceRenderer.guiTextures);
      class_310.method_1551().method_1531().method_22813(InterfaceRenderer.guiTextures);
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
      this.context
         .setupGlobalContext(
            settings.getDotNameScale(),
            settings.getSmoothDots(),
            settings.debugEntityIcons,
            settings.debugEntityVariantIds,
            settings.getDotsStyle(),
            dotsRenderType,
            renderTypeBuffers.getBuffer(dotsRenderType),
            renderTypeBuffers.getBuffer(CustomRenderTypes.RADAR_NAME_BGS),
            multiTextureRenderTypeRenderers.getRenderer(
               t -> RenderSystem.setShaderTexture(0, t), MultiTextureRenderTypeRendererProvider::defaultTextureBind, CustomRenderTypes.GUI_BILINEAR
            ),
            renderEntity
         );
   }

   @Override
   public void postRender(
      int location,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      AXaeroMinimap modMain,
      class_4598 renderTypeBuffers,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      if (this.context.reversedOrder) {
         renderTypeBuffers.method_22994(this.context.dotsRenderType);
      }

      multiTextureRenderTypeRenderers.draw(this.context.iconsRenderer);
      if (!this.context.reversedOrder) {
         renderTypeBuffers.method_22994(this.context.dotsRenderType);
      }

      renderTypeBuffers.method_22993();
      this.context.renderEntity = null;
      this.context.iconsRenderer = null;
      RenderSystem.setShaderTexture(0, InterfaceRenderer.guiTextures);
      class_310.method_1551().method_1531().method_22813(InterfaceRenderer.guiTextures);
      GL11.glTexParameteri(3553, 10240, 9728);
      GL11.glTexParameteri(3553, 10241, 9728);
   }

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
      class_1297 e,
      double partialX,
      double partialY,
      boolean cave,
      float partialTicks
   ) {
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

      float optionScaleAdjust = this.elementReader.getBoxScale(location, e, this.context);
      optionalScale *= optionScaleAdjust;
      RadarRenderContext context = this.context;
      int nameOffsetX = 0;
      int nameOffsetY = 0;
      matrixStack.method_22903();
      boolean icon = context.icon;
      boolean name = context.name;
      XaeroIcon entityHeadTexture = icon
         ? this.entityIconManager
            .getEntityHeadTexture(guiGraphics, e, framebuffer, helper, (float)context.iconScale, context.debugEntityIcons, context.debugEntityVariantIds)
         : null;
      if (entityHeadTexture == EntityIconManager.DOT) {
         entityHeadTexture = null;
         icon = false;
      }

      if (entityHeadTexture == EntityIconManager.FAILED) {
         entityHeadTexture = null;
      }

      float offh = (float)(renderEntity.method_23318() - e.method_23318());
      matrixStack.method_22904(partialX, partialY, 0.0);
      double dotsScale;
      if (entityHeadTexture != null) {
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
         int dotSize = context.dotSize;
         if (icon && context.displayNameWhenIconFails) {
            name = true;
         }

         dotsScale = 1.0 + 0.5 * (double)(dotSize - 1);
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
         short var76;
         byte var77;
         byte var78;
         if (context.dotsStyle == 1) {
            if (smooth) {
               dotTextureX = 1;
               var76 = 88;
            } else {
               dotsScale = (double)((int)dotsScale);
               dotTextureX = 9;
               var76 = 77;
            }

            dotOffset = -3.5F;
            var78 = 8;
            var77 = 8;
            dotActualScale *= dotsScale;
            matrixStack.method_22905((float)dotsScale, (float)dotsScale, 1.0F);
         } else {
            switch (dotSize) {
               case 1:
                  dotOffset = -4.5F;
                  var76 = 108;
                  var78 = 9;
                  var77 = 9;
                  break;
               case 2:
               default:
                  dotOffset = -5.5F;
                  var76 = 117;
                  var78 = 11;
                  var77 = 11;
                  break;
               case 3:
                  dotOffset = -7.5F;
                  var76 = 128;
                  var78 = 15;
                  var77 = 15;
                  break;
               case 4:
                  dotOffset = -10.5F;
                  var76 = 160;
                  var78 = 21;
                  var77 = 21;
            }
         }

         if (!smooth) {
            double dotRadius = (double)(-dotOffset) * dotActualScale;
            double dotRadiusPartial = dotRadius - (double)((int)dotRadius);
            nameOffsetX = partialX - dotRadiusPartial <= -0.5 ? -1 : 0;
            nameOffsetY = partialY - dotRadiusPartial < -0.5 ? -1 : 0;
         }

         helper.addTexturedColoredRectToExistingBuffer(
            matrixStack.method_23760().method_23761(), context.dotsBufferBuilder, dotOffset, dotOffset, dotTextureX, var76, var77, var78, r, g, b, a, 256.0F
         );
      }

      matrixStack.method_22909();
      int displayY = context.displayY;
      if (name || displayY > 0) {
         matrixStack.method_22903();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         matrixStack.method_22904((double)nameOffsetX, (double)nameOffsetY, optionalDepth);
         int offsetY = entityHeadTexture != null ? 11 : 5;
         matrixStack.method_46416(0.0F, (float)Math.round((double)offsetY * dotsScale * (double)optionalScale), 0.0F);
         optionalScale = (float)Math.ceil((double)optionalScale);
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

         if (name) {
            class_2561 component = Misc.getFixedDisplayName(e);
            if (component != null) {
               String entityName = component.getString();
               if (displayY > 0) {
                  entityName = entityName + "(" + yValueString + ")";
               }

               int nameW = font.method_1727(entityName);
               helper.addColoredRectToExistingBuffer(
                  matrixStack.method_23760().method_23761(), context.nameBgBuilder, (float)(-nameW / 2 - 2), -1.0F, nameW + 3, 10, 0.0F, 0.0F, 0.0F, 0.3529412F
               );
               Misc.drawNormalText(matrixStack, entityName, (float)(-nameW / 2), 0.0F, -1, false, renderTypeBuffers);
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
            Misc.drawNormalText(matrixStack, yValueString, (float)(-yStringW / 2), 0.0F, -1, false, renderTypeBuffers);
         }

         RenderSystem.enableBlend();
         RenderSystem.blendFuncSeparate(770, 771, 1, 771);
         matrixStack.method_22909();
      }

      return true;
   }

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
      this.context
         .setupGlobalContext(
            dotNameScale, smooth, debug, debugEntityVariantIds, style, dotsRenderType, dotsBufferBuilder, nameBgBuilder, iconsRenderer, renderEntity
         );
      this.context.minimapRadar = minimapRadar;
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
      this.renderElement(
         location,
         highlit,
         false,
         guiGraphics,
         textRenderTypeBuffer,
         font,
         framebuffer,
         helper,
         renderEntity,
         p,
         0.0,
         0.0,
         0.0,
         0,
         0.0,
         minimapScale,
         e,
         0.0,
         0.0,
         cave,
         1.0F
      );
      this.context.renderEntity = null;
      this.context.minimapRadar = null;
      this.context.iconsRenderer = null;
   }

   @Override
   public boolean shouldRender(int location) {
      return this.minimapInterface.usingFBO() && (location == 3 || location == 4 || this.modMain.getSettings().getEntityRadar());
   }

   public static final class Builder {
      private AXaeroMinimap modMain;
      private EntityIconManager entityIconManager;
      private MinimapInterface minimapInterface;

      private Builder() {
      }

      private RadarRenderer.Builder setDefault() {
         this.setEntityIconManager(null);
         return this;
      }

      public RadarRenderer.Builder setModMain(AXaeroMinimap modMain) {
         this.modMain = modMain;
         return this;
      }

      public RadarRenderer.Builder setEntityIconManager(EntityIconManager entityIconManager) {
         this.entityIconManager = entityIconManager;
         return this;
      }

      public RadarRenderer.Builder setMinimapInterface(MinimapInterface minimapInterface) {
         this.minimapInterface = minimapInterface;
         return this;
      }

      public RadarRenderer build() {
         if (this.modMain != null && this.entityIconManager != null && this.minimapInterface != null) {
            RadarElementReader elementReader = new RadarElementReader();
            RadarRenderProvider provider = new RadarRenderProvider();
            RadarRenderContext context = new RadarRenderContext();
            return new RadarRenderer(this.modMain, this.entityIconManager, this.minimapInterface, elementReader, provider, context);
         } else {
            throw new IllegalStateException();
         }
      }

      public static RadarRenderer.Builder begin() {
         return new RadarRenderer.Builder().setDefault();
      }
   }
}
