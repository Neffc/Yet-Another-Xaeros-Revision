package xaero.common.minimap.waypoints.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.class_1041;
import net.minecraft.class_1074;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import xaero.common.AXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.effect.Effects;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.GuiHelper;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.interfaces.render.InterfaceRenderer;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointSet;
import xaero.common.minimap.waypoints.WaypointUtil;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;

public final class WaypointsGuiRenderer extends MinimapElementRenderer<Waypoint, WaypointGuiRenderContext> {
   private final AXaeroMinimap modMain;
   private final WaypointDeleter waypointReachDeleter;

   private WaypointsGuiRenderer(
      AXaeroMinimap modMain,
      WaypointReader elementReader,
      WaypointRenderProvider provider,
      WaypointGuiRenderContext context,
      WaypointDeleter waypointReachDeleter
   ) {
      super(elementReader, provider, context);
      this.modMain = modMain;
      this.waypointReachDeleter = waypointReachDeleter;
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
      Waypoint w,
      double partialX,
      double partialY,
      boolean cave,
      float partialTicks
   ) {
      class_4587 matrixStack = guiGraphics.method_51448();
      if ((this.context.deleteReachedDeathpoints || w.getWaypointType() != 1 && w.getWaypointType() != 2)
         && w.isOneoffDestination()
         && System.currentTimeMillis() - w.getCreatedAt() > 5000L) {
         double correctOffX = renderEntity.method_23317() - (double)w.getX(this.context.dimDiv);
         double correctOffY = renderEntity.method_23318() - (double)w.getY();
         if (!w.isYIncluded()) {
            correctOffY = 0.0;
         }

         double correctOffZ = renderEntity.method_23321() - (double)w.getZ(this.context.dimDiv);
         double correctDistance = Math.sqrt(correctOffX * correctOffX + correctOffY * correctOffY + correctOffZ * correctOffZ);
         if (correctDistance < 4.0) {
            this.waypointReachDeleter.add(w);
         }
      }

      matrixStack.method_22904(-1.0, -1.0, optionalDepth);
      if (this.context.scale > 0 && location == 1) {
         matrixStack.method_22905((float)this.context.scale, (float)this.context.scale, 1.0F);
      } else {
         matrixStack.method_22905(optionalScale, optionalScale, 1.0F);
      }

      this.drawIconOnGUI(guiGraphics, helper, w, this.context.settings, 0, 0, renderTypeBuffers, this.context.waypointBackgroundConsumer);
      return true;
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
      this.context.settings = modMain.getSettings();
      this.context.waypointBackgroundConsumer = renderTypeBuffers.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);
      this.context.deleteReachedDeathpoints = modMain.getSettings().deleteReachedDeathpoints;
      this.context.scale = modMain.getSettings().waypointOnMapScale;
      this.updateWaypointCollection(renderX, renderY, renderZ, modMain);
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
      renderTypeBuffers.method_22993();
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      WaypointsManager waypointsManager = minimapSession.getWaypointsManager();
      this.waypointReachDeleter.deleteCollected(waypointsManager.getCurrentWorld(), modMain.getSettings().renderAllSets);
   }

   public void updateWaypointCollection(double renderX, double renderY, double renderZ, AXaeroMinimap modMain) {
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      WaypointsManager waypointsManager = minimapSession.getWaypointsManager();
      List<Waypoint> sortingList = this.context.sortingList;
      sortingList.clear();
      if (waypointsManager.getWaypoints() != null) {
         if (modMain.getSettings().renderAllSets) {
            HashMap<String, WaypointSet> sets = waypointsManager.getCurrentWorld().getSets();

            for (Entry<String, WaypointSet> setEntry : sets.entrySet()) {
               sortingList.addAll(setEntry.getValue().getList());
            }
         } else {
            sortingList.addAll(waypointsManager.getWaypoints().getList());
         }
      }

      Hashtable<String, Hashtable<Integer, Waypoint>> customWaypoints = WaypointsManager.customWaypoints;
      if (!customWaypoints.isEmpty()) {
         for (Hashtable<Integer, Waypoint> modCustomWaypoints : customWaypoints.values()) {
            sortingList.addAll(modCustomWaypoints.values());
         }
      }

      this.waypointReachDeleter.begin();
      this.context.dimDiv = waypointsManager.getDimensionDivision(waypointsManager.getCurrentWorld());
      class_243 cameraPos = class_310.method_1551().field_1773.method_19418().method_19326();
      Waypoint.RENDER_SORTING_POS = new class_243(cameraPos.field_1352 * this.context.dimDiv, cameraPos.field_1351, cameraPos.field_1350 * this.context.dimDiv);
      ModSettings settings = modMain.getSettings();
      this.context
         .filterParams
         .setParams(
            cameraPos.field_1352,
            cameraPos.field_1351,
            cameraPos.field_1350,
            null,
            this.context.dimDiv,
            settings.getDeathpoints(),
            settings.temporaryWaypointsGlobal,
            settings.getMaxWaypointsDistance(),
            settings.waypointsDistanceMin,
            0.0,
            settings.dimensionScaledMaxWaypointDistance
         );
   }

   public void drawIconOnGUI(
      class_332 guiGraphics,
      MinimapRendererHelper rendererHelper,
      Waypoint w,
      ModSettings settings,
      int drawX,
      int drawY,
      class_4598 renderTypeBuffer,
      class_4588 waypointBackgroundConsumer
   ) {
      class_4587 matrixStack = guiGraphics.method_51448();
      int c = ModSettings.COLORS[w.getColor()];
      int r = c >> 16 & 0xFF;
      int g = c >> 8 & 0xFF;
      int b = c & 0xFF;
      int a = (int)(255.0F * ((float)settings.waypointOpacityMap / 100.0F));
      int initialsWidth = w.getWaypointType() == 1 ? 7 : class_310.method_1551().field_1772.method_1727(w.getSymbol());
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
         (float)a / 255.0F
      );
      if (w.getWaypointType() != 1) {
         Misc.drawNormalText(matrixStack, w.getSymbol(), (float)(drawX + 1 - initialsWidth / 2), (float)(drawY - 3), -1, true, renderTypeBuffer);
      } else {
         RenderSystem.enableDepthTest();
         RenderSystem.setShaderTexture(0, InterfaceRenderer.guiTextures);
         RenderSystem.setShaderColor(0.2431F, 0.2431F, 0.2431F, 1.0F);
         GuiHelper.blit(matrixStack, rectX1 + 1, rectY1 + 1, 0.0F, 78.0F, 9, 9);
         RenderSystem.setShaderColor(0.9882F, 0.9882F, 0.9882F, 1.0F);
         GuiHelper.blit(matrixStack, rectX1, rectY1, 0.0F, 78.0F, 9, 9);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   public void drawSetChange(WaypointsManager waypointsManager, class_332 guiGraphics, class_1041 res) {
      if (waypointsManager.getWaypoints() != null && waypointsManager.setChanged != 0L) {
         int passed = (int)(System.currentTimeMillis() - waypointsManager.setChanged);
         if (passed < 1500) {
            int fadeTime = 300;
            boolean fading = passed > 1500 - fadeTime;
            int alpha = 3 + (int)(252.0F * (fading ? (float)(1500 - passed) / (float)fadeTime : 1.0F));
            int c = 16777215 | alpha << 24;
            class_4598 textRenderTypeBuffers = this.modMain.getInterfaceRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
            Misc.drawCenteredPiercingText(
               guiGraphics.method_51448(),
               class_1074.method_4662(waypointsManager.getWaypoints().getName(), new Object[0]),
               (float)(res.method_4486() / 2),
               (float)(res.method_4502() / 2 + 50),
               c,
               true,
               textRenderTypeBuffers
            );
            textRenderTypeBuffers.method_22993();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(770, 771);
         } else {
            waypointsManager.setChanged = 0L;
         }
      }
   }

   @Override
   public boolean shouldRender(int location) {
      return (location != 1 && location != 0 || this.modMain.getSettings().getShowWaypoints())
         && !Misc.hasEffect(Effects.NO_WAYPOINTS)
         && !Misc.hasEffect(Effects.NO_WAYPOINTS_HARMFUL);
   }

   @Override
   public int getOrder() {
      return 1;
   }

   public static final class Builder {
      private WaypointDeleter waypointDeleter;
      private final AXaeroMinimap modMain;

      private Builder(AXaeroMinimap modMain) {
         this.modMain = modMain;
      }

      private WaypointsGuiRenderer.Builder setDefault() {
         this.setWaypointDeleter(null);
         return this;
      }

      public WaypointsGuiRenderer.Builder setWaypointDeleter(WaypointDeleter waypointDeleter) {
         this.waypointDeleter = waypointDeleter;
         return this;
      }

      public WaypointsGuiRenderer build() {
         if (this.waypointDeleter == null) {
            throw new IllegalStateException();
         } else {
            WaypointGuiRenderContext context = new WaypointGuiRenderContext();
            context.filter = w -> {
               WaypointFilterParams filterParams = context.filterParams;
               boolean deathpoints = filterParams.deathpoints;
               if (!w.isDisabled()
                  && w.getVisibilityType() != 2
                  && w.getVisibilityType() != 3
                  && (w.getWaypointType() != 1 && w.getWaypointType() != 2 || deathpoints)) {
                  double offx = (double)w.getX(filterParams.dimDiv) + 0.5 - filterParams.cameraX;
                  double offz = (double)w.getZ(filterParams.dimDiv) + 0.5 - filterParams.cameraZ;
                  double distanceScale = context.filterParams.dimensionScaleDistance ? class_310.method_1551().field_1687.method_8597().comp_646() : 1.0;
                  double distance = Math.sqrt(offx * offx + offz * offz) * distanceScale;
                  double waypointsDistance = filterParams.waypointsDistance;
                  return w.isOneoffDestination()
                     || w.getWaypointType() == 1
                     || w.isGlobal()
                     || w.isTemporary() && filterParams.temporaryWaypointsGlobal
                     || waypointsDistance == 0.0
                     || !(distance > waypointsDistance);
               } else {
                  return false;
               }
            };
            return new WaypointsGuiRenderer(this.modMain, new WaypointReader(), new WaypointRenderProvider(), context, this.waypointDeleter);
         }
      }

      public static WaypointsGuiRenderer.Builder begin(AXaeroMinimap modMain) {
         return new WaypointsGuiRenderer.Builder(modMain).setDefault();
      }
   }
}
