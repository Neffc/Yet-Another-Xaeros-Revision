package xaero.hud.minimap.waypoint.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Hashtable;
import java.util.List;
import net.minecraft.class_1041;
import net.minecraft.class_1074;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import xaero.common.IXaeroMinimap;
import xaero.common.effect.Effects;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.GuiHelper;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointUtil;
import xaero.common.minimap.waypoints.WaypointVisibilityType;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointPurpose;
import xaero.hud.minimap.waypoint.WaypointSession;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;

public abstract class WaypointsGuiRenderer extends MinimapElementRenderer<Waypoint, WaypointGuiRenderContext> {
   private final IXaeroMinimap modMain;
   private final WaypointDeleter waypointReachDeleter;

   protected WaypointsGuiRenderer(
      IXaeroMinimap modMain,
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
      Waypoint w,
      boolean highlit,
      boolean outOfBounds,
      double optionalDepth,
      float optionalScale,
      double partialX,
      double partialY,
      MinimapElementRenderInfo renderInfo,
      class_332 guiGraphics,
      class_4598 renderTypeBuffers
   ) {
      class_4587 matrixStack = guiGraphics.method_51448();
      MinimapElementRenderLocation location = renderInfo.location;
      this.handleDestinationWaypoints(w, renderInfo);
      matrixStack.method_22904(-1.0, -1.0, optionalDepth);
      if (this.context.scale > 0 && location == MinimapElementRenderLocation.OVER_MINIMAP) {
         matrixStack.method_22905((float)this.context.scale, (float)this.context.scale, 1.0F);
      } else {
         matrixStack.method_22905(optionalScale, optionalScale, 1.0F);
      }

      this.drawIconOnGUI(guiGraphics, this.context.helper, w, this.context.settings, 0, 0, renderTypeBuffers, this.context.waypointBackgroundConsumer);
      return true;
   }

   @Override
   public void preRender(
      MinimapElementRenderInfo renderInfo, class_4598 renderTypeBuffers, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      this.context.settings = this.modMain.getSettings();
      this.context.waypointBackgroundConsumer = renderTypeBuffers.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);
      this.context.deleteReachedDeathpoints = this.modMain.getSettings().deleteReachedDeathpoints;
      this.context.scale = this.modMain.getSettings().waypointOnMapScale;
      this.context.helper = this.modMain.getMinimap().getMinimapFBORenderer().getHelper();
      this.updateWaypointCollection();
      MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
      this.context.dimDiv = session.getDimensionHelper().getDimensionDivision(session.getWorldManager().getCurrentWorld());
      class_243 cameraPos = class_310.method_1551().field_1773.method_19418().method_19326();
      Waypoint.RENDER_SORTING_POS = new class_243(cameraPos.field_1352 * this.context.dimDiv, cameraPos.field_1351, cameraPos.field_1350 * this.context.dimDiv);
      ModSettings settings = this.modMain.getSettings();
      this.context.filterParams.cameraPos = cameraPos;
      xaero.common.minimap.waypoints.render.WaypointFilterParams oldFilterParams = (xaero.common.minimap.waypoints.render.WaypointFilterParams)this.context.filterParams;
      oldFilterParams.cameraX = cameraPos.field_1352;
      oldFilterParams.cameraY = cameraPos.field_1351;
      oldFilterParams.cameraZ = cameraPos.field_1350;
      this.context.filterParams.lookVector = null;
      this.context.filterParams.dimDiv = this.context.dimDiv;
      this.context.filterParams.deathpoints = settings.getDeathpoints();
      this.context.filterParams.temporaryWaypointsGlobal = settings.temporaryWaypointsGlobal;
      this.context.filterParams.waypointsDistance = settings.getMaxWaypointsDistance();
      this.context.filterParams.waypointsDistanceMin = settings.waypointsDistanceMin;
      this.context.filterParams.playerY = 0.0;
      this.context.filterParams.dimensionScaleDistance = settings.dimensionScaledMaxWaypointDistance;
      this.waypointReachDeleter.begin();
   }

   @Override
   public void postRender(
      MinimapElementRenderInfo renderInfo, class_4598 renderTypeBuffers, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      renderTypeBuffers.method_22993();
      MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
      MinimapWorldManager manager = session.getWorldManager();
      this.waypointReachDeleter.deleteCollected(session, manager.getCurrentWorld(), this.modMain.getSettings().renderAllSets);
   }

   private void handleDestinationWaypoints(Waypoint renderedWaypoint, MinimapElementRenderInfo renderInfo) {
      if (renderedWaypoint.isDestination()) {
         if (this.context.deleteReachedDeathpoints || !renderedWaypoint.getPurpose().isDeath()) {
            if (System.currentTimeMillis() - renderedWaypoint.getCreatedAt() > 5000L) {
               class_1297 renderEntity = renderInfo.renderEntity;
               double correctOffX = renderEntity.method_23317() - (double)renderedWaypoint.getX(this.context.dimDiv);
               double correctOffY = renderEntity.method_23318() - (double)renderedWaypoint.getY();
               if (!renderedWaypoint.isYIncluded()) {
                  correctOffY = 0.0;
               }

               double correctOffZ = renderEntity.method_23321() - (double)renderedWaypoint.getZ(this.context.dimDiv);
               double correctDistance = Math.sqrt(correctOffX * correctOffX + correctOffY * correctOffY + correctOffZ * correctOffZ);
               if (correctDistance < 4.0) {
                  this.waypointReachDeleter.add(renderedWaypoint);
               }
            }
         }
      }
   }

   public void updateWaypointCollection() {
      MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
      MinimapWorldManager manager = session.getWorldManager();
      List<Waypoint> sortingList = this.context.sortingList;
      sortingList.clear();
      if (manager.getCurrentWorld() != null) {
         if (this.modMain.getSettings().renderAllSets) {
            for (WaypointSet set : manager.getCurrentWorld().getIterableWaypointSets()) {
               set.addTo(sortingList);
            }
         } else {
            manager.getCurrentWorld().getCurrentWaypointSet().addTo(sortingList);
         }
      }

      Hashtable<String, Hashtable<Integer, Waypoint>> customWaypoints = WaypointsManager.customWaypoints;
      if (!customWaypoints.isEmpty()) {
         for (Hashtable<Integer, Waypoint> modCustomWaypoints : customWaypoints.values()) {
            sortingList.addAll(modCustomWaypoints.values());
         }
      }

      if (manager.hasCustomWaypoints()) {
         for (Waypoint waypoint : manager.getCustomWaypoints()) {
            sortingList.add(waypoint);
         }
      }
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
      int color = w.getWaypointColor().getHex();
      int r = color >> 16 & 0xFF;
      int g = color >> 8 & 0xFF;
      int b = color & 0xFF;
      float a = (float)settings.waypointOpacityMap / 100.0F;
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
         CustomRenderTypes.GUI_NEAREST.method_23516();
         RenderSystem.setShaderColor(0.2431F, 0.2431F, 0.2431F, 1.0F);
         GuiHelper.blit(matrixStack, rectX1 + 1, rectY1 + 1, 0.0F, 78.0F, 9, 9);
         RenderSystem.setShaderColor(0.9882F, 0.9882F, 0.9882F, 1.0F);
         GuiHelper.blit(matrixStack, rectX1, rectY1, 0.0F, 78.0F, 9, 9);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         CustomRenderTypes.GUI_NEAREST.method_23518();
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
               class_4598 renderBuffers = this.modMain.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
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
            && !this.modMain.getSettings().getShowWaypoints()
         ? false
         : !Misc.hasEffect(Effects.NO_WAYPOINTS) && !Misc.hasEffect(Effects.NO_WAYPOINTS_HARMFUL);
   }

   @Override
   public int getOrder() {
      return 1;
   }

   public static final class Builder {
      private WaypointDeleter waypointDeleter;
      private final IXaeroMinimap modMain;

      private Builder(IXaeroMinimap modMain) {
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
            xaero.common.minimap.waypoints.render.WaypointGuiRenderContext context = new xaero.common.minimap.waypoints.render.WaypointGuiRenderContext();
            context.filter = w -> {
               WaypointFilterParams filterParams = context.filterParams;
               if (w.isDisabled()) {
                  return false;
               } else if (w.getVisibility() == WaypointVisibilityType.WORLD_MAP_LOCAL) {
                  return false;
               } else if (w.getVisibility() == WaypointVisibilityType.WORLD_MAP_GLOBAL) {
                  return false;
               } else if (!filterParams.deathpoints && w.getPurpose().isDeath()) {
                  return false;
               } else if (w.isDestination()) {
                  return true;
               } else if (w.getPurpose() == WaypointPurpose.DEATH) {
                  return true;
               } else if (!w.isGlobal() && (!w.isTemporary() || !filterParams.temporaryWaypointsGlobal)) {
                  double offx = (double)w.getX(filterParams.dimDiv) + 0.5 - filterParams.cameraPos.field_1352;
                  double offz = (double)w.getZ(filterParams.dimDiv) + 0.5 - filterParams.cameraPos.field_1350;
                  double distanceScale = context.filterParams.dimensionScaleDistance ? class_310.method_1551().field_1687.method_8597().comp_646() : 1.0;
                  double distance = Math.sqrt(offx * offx + offz * offz) * distanceScale;
                  return filterParams.waypointsDistance == 0.0 || distance <= filterParams.waypointsDistance;
               } else {
                  return true;
               }
            };
            return new xaero.common.minimap.waypoints.render.WaypointsGuiRenderer(
               this.modMain,
               new xaero.common.minimap.waypoints.render.WaypointReader(),
               new xaero.common.minimap.waypoints.render.WaypointRenderProvider(),
               context,
               this.waypointDeleter
            );
         }
      }

      public static WaypointsGuiRenderer.Builder begin(IXaeroMinimap modMain) {
         return new WaypointsGuiRenderer.Builder(modMain).setDefault();
      }
   }
}
