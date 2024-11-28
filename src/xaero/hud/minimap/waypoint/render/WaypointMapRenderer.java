package xaero.hud.minimap.waypoint.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1041;
import net.minecraft.class_1074;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.effect.Effects;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointUtil;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.minimap.waypoints.render.WaypointGuiRenderContext;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointPurpose;
import xaero.hud.minimap.waypoint.WaypointSession;
import xaero.hud.minimap.world.MinimapWorld;

public abstract class WaypointMapRenderer extends MinimapElementRenderer<Waypoint, WaypointMapRenderContext> {
   private MinimapRendererHelper helper;
   private int scale;
   private boolean temporaryWaypointsGlobal;
   private double waypointsDistance;
   private boolean dimensionScaleDistance;
   private int opacity;
   private class_4598 minimapBufferSource;
   private class_4588 texturedIconConsumer;
   private class_4588 waypointBackgroundConsumer;

   protected WaypointMapRenderer(WaypointReader elementReader, WaypointRenderProvider provider, WaypointMapRenderContext context) {
      super(elementReader, provider, context);
   }

   public boolean renderElement(
      Waypoint w,
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
      double waypointPosDivider = renderInfo.backgroundCoordinateScale / this.context.dimCoordinateScale;
      double wX = (double)w.getX(waypointPosDivider) + 0.5;
      double wZ = (double)w.getZ(waypointPosDivider) + 0.5;
      double offX = wX - renderInfo.renderPos.field_1352;
      double offZ = wZ - renderInfo.renderPos.field_1350;
      double distance2D = Math.sqrt(offX * offX + offZ * offZ);
      double distanceScale = this.dimensionScaleDistance ? renderInfo.backgroundCoordinateScale : 1.0;
      double scaledDistance2D = distance2D * distanceScale;
      if (!w.isDestination()
         && w.getPurpose() != WaypointPurpose.DEATH
         && !w.isGlobal()
         && (!w.isTemporary() || !this.temporaryWaypointsGlobal)
         && this.waypointsDistance != 0.0
         && scaledDistance2D > this.waypointsDistance) {
         return false;
      } else {
         class_4587 matrixStack = guiGraphics.method_51448();
         MinimapElementRenderLocation location = renderInfo.location;
         matrixStack.method_22904(-1.0, -1.0, optionalDepth);
         if (this.scale > 0 && location == MinimapElementRenderLocation.OVER_MINIMAP) {
            matrixStack.method_22905((float)this.scale, (float)this.scale, 1.0F);
         } else {
            matrixStack.method_22905(optionalScale, optionalScale, 1.0F);
         }

         this.drawIconOnGUI(guiGraphics, this.helper, w, 0, 0, this.minimapBufferSource, this.waypointBackgroundConsumer, this.texturedIconConsumer);
         return true;
      }
   }

   @Override
   public void preRender(
      MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      RenderSystem.disableDepthTest();
      vanillaBufferSource.method_22993();
      this.minimapBufferSource = HudMod.INSTANCE.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
      this.waypointBackgroundConsumer = this.minimapBufferSource.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);
      this.texturedIconConsumer = this.minimapBufferSource.getBuffer(CustomRenderTypes.GUI_NEAREST);
      this.helper = HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().getHelper();
      MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
      MinimapWorld currentWorld = session.getWorldManager().getCurrentWorld();
      this.context.dimCoordinateScale = session.getDimensionHelper().getDimCoordinateScale(currentWorld);
      ModSettings settings = HudMod.INSTANCE.getSettings();
      this.scale = settings.waypointOnMapScale;
      this.temporaryWaypointsGlobal = settings.temporaryWaypointsGlobal;
      this.waypointsDistance = settings.getMaxWaypointsDistance();
      this.dimensionScaleDistance = settings.dimensionScaledMaxWaypointDistance;
      this.opacity = settings.waypointOpacityMap;
   }

   @Override
   public void postRender(
      MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      this.minimapBufferSource.method_22993();
      RenderSystem.enableDepthTest();
      RenderSystem.depthFunc(515);
      this.waypointBackgroundConsumer = null;
   }

   @Deprecated
   public void updateWaypointCollection() {
   }

   public void drawIconOnGUI(
      class_332 guiGraphics,
      MinimapRendererHelper rendererHelper,
      Waypoint w,
      int drawX,
      int drawY,
      class_4598 renderTypeBuffer,
      class_4588 waypointBackgroundConsumer,
      class_4588 texturedIconConsumer
   ) {
      class_4587 matrixStack = guiGraphics.method_51448();
      int color = w.getWaypointColor().getHex();
      int r = color >> 16 & 0xFF;
      int g = color >> 8 & 0xFF;
      int b = color & 0xFF;
      float a = (float)this.opacity / 100.0F;
      int initialsWidth = w.getPurpose() == WaypointPurpose.DEATH ? 7 : class_310.method_1551().field_1772.method_1727(w.getInitials());
      int addedFrame = WaypointUtil.getAddedMinimapIconFrame(initialsWidth);
      int rectX1 = drawX - 4 - addedFrame;
      int rectY1 = drawY - 4;
      int rectX2 = drawX + 5 + addedFrame;
      int rectY2 = drawY + 5;
      rendererHelper.addColoredRectToExistingBuffer(
         matrixStack.method_23760().method_23761(),
         waypointBackgroundConsumer,
         (float)rectX1,
         (float)rectY1,
         rectX2 - rectX1,
         rectY2 - rectY1,
         (float)r / 255.0F,
         (float)g / 255.0F,
         (float)b / 255.0F,
         a
      );
      if (w.getPurpose() == WaypointPurpose.DEATH) {
         rendererHelper.addTexturedColoredRectToExistingBuffer(
            matrixStack.method_23760().method_23761(),
            texturedIconConsumer,
            (float)(rectX1 + 1),
            (float)(rectY1 + 1),
            0,
            87,
            9,
            9,
            9,
            -9,
            0.2431F,
            0.2431F,
            0.2431F,
            1.0F,
            256.0F
         );
         rendererHelper.addTexturedColoredRectToExistingBuffer(
            matrixStack.method_23760().method_23761(),
            texturedIconConsumer,
            (float)rectX1,
            (float)rectY1,
            0,
            87,
            9,
            9,
            9,
            -9,
            0.9882F,
            0.9882F,
            0.9882F,
            1.0F,
            256.0F
         );
      } else {
         Misc.drawNormalText(matrixStack, w.getInitials(), (float)(drawX + 1 - initialsWidth / 2), (float)(drawY - 3), -1, true, renderTypeBuffer);
      }
   }

   @Deprecated
   public void drawSetChange(WaypointsManager waypointsManager, class_332 guiGraphics, class_1041 res) {
      this.drawSetChange((MinimapSession)waypointsManager, guiGraphics, res);
   }

   public void drawSetChange(MinimapSession session, class_332 guiGraphics, class_1041 res) {
      MinimapWorld minimapWorld = session.getWorldManager().getCurrentWorld();
      if (minimapWorld != null) {
         WaypointSession waypointSession = session.getWaypointSession();
         if (waypointSession.getSetChangedTime() != 0L) {
            int passed = (int)(System.currentTimeMillis() - waypointSession.getSetChangedTime());
            if (passed >= 1500) {
               waypointSession.setSetChangedTime(0L);
            } else {
               int fadeTime = 300;
               boolean fading = passed > 1500 - fadeTime;
               float fadeFactor = fading ? (float)(1500 - passed) / (float)fadeTime : 1.0F;
               int alpha = 3 + (int)(252.0F * fadeFactor);
               int c = 16777215 | alpha << 24;
               class_4598 renderBuffers = HudMod.INSTANCE.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
               Misc.drawCenteredPiercingText(
                  guiGraphics.method_51448(),
                  class_1074.method_4662(minimapWorld.getCurrentWaypointSet().getName(), new Object[0]),
                  (float)(res.method_4486() / 2),
                  (float)(res.method_4502() / 2 + 50),
                  c,
                  true,
                  renderBuffers
               );
               renderBuffers.method_22993();
               RenderSystem.enableBlend();
               RenderSystem.blendFunc(770, 771);
            }
         }
      }
   }

   @Override
   public boolean shouldRender(MinimapElementRenderLocation location) {
      return (location == MinimapElementRenderLocation.OVER_MINIMAP || location == MinimapElementRenderLocation.IN_MINIMAP)
            && !HudMod.INSTANCE.getSettings().getShowWaypoints()
         ? false
         : !Misc.hasEffect(Effects.NO_WAYPOINTS) && !Misc.hasEffect(Effects.NO_WAYPOINTS_HARMFUL);
   }

   @Override
   public int getOrder() {
      return 100;
   }

   public static final class Builder {
      private WaypointDeleter waypointDeleter;
      private final IXaeroMinimap modMain;

      private Builder(IXaeroMinimap modMain) {
         this.modMain = modMain;
      }

      private WaypointMapRenderer.Builder setDefault() {
         this.setWaypointDeleter(null);
         return this;
      }

      public WaypointMapRenderer.Builder setWaypointDeleter(WaypointDeleter waypointDeleter) {
         this.waypointDeleter = waypointDeleter;
         return this;
      }

      public WaypointMapRenderer build() {
         if (this.waypointDeleter == null) {
            throw new IllegalStateException();
         } else {
            WaypointGuiRenderContext context = new WaypointGuiRenderContext();
            return new xaero.common.minimap.waypoints.render.WaypointsGuiRenderer(
               new xaero.common.minimap.waypoints.render.WaypointReader(), new xaero.common.minimap.waypoints.render.WaypointRenderProvider(), context
            );
         }
      }

      public static WaypointMapRenderer.Builder begin(IXaeroMinimap modMain) {
         return new WaypointMapRenderer.Builder(modMain).setDefault();
      }
   }
}
