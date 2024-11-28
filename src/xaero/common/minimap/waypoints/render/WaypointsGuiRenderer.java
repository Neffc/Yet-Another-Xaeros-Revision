package xaero.common.minimap.waypoints.render;

import net.minecraft.class_1041;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.render.WaypointDeleter;
import xaero.hud.minimap.world.MinimapWorldManager;

@Deprecated
public final class WaypointsGuiRenderer extends xaero.hud.minimap.waypoint.render.WaypointsGuiRenderer {
   private MinimapElementRenderInfo compatibleRenderInfo;
   private WaypointDeleter waypointReachDeleter;

   public WaypointsGuiRenderer(
      IXaeroMinimap modMain,
      WaypointReader elementReader,
      WaypointRenderProvider provider,
      WaypointGuiRenderContext context,
      WaypointDeleter waypointReachDeleter
   ) {
      super(modMain, elementReader, provider, context, waypointReachDeleter);
      this.waypointReachDeleter = waypointReachDeleter;
   }

   @Deprecated
   public void updateWaypointCollection(IXaeroMinimap modMain) {
      super.updateWaypointCollection();
      MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
      MinimapWorldManager manager = session.getWorldManager();
      this.waypointReachDeleter.begin();
      this.context.dimDiv = session.getDimensionHelper().getDimensionDivision(manager.getCurrentWorld());
      class_243 cameraPos = class_310.method_1551().field_1773.method_19418().method_19326();
      Waypoint.RENDER_SORTING_POS = new class_243(cameraPos.field_1352 * this.context.dimDiv, cameraPos.field_1351, cameraPos.field_1350 * this.context.dimDiv);
      ModSettings settings = modMain.getSettings();
      this.context.filterParams.cameraPos = cameraPos;
      WaypointFilterParams oldFilterParams = (WaypointFilterParams)this.context.filterParams;
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
   }

   @Deprecated
   @Override
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
      super.drawIconOnGUI(guiGraphics, rendererHelper, w, settings, drawX, drawY, renderTypeBuffer, waypointBackgroundConsumer);
   }

   @Deprecated
   @Override
   public void drawSetChange(WaypointsManager waypointsManager, class_332 guiGraphics, class_1041 res) {
      this.drawSetChange((MinimapSession)waypointsManager, guiGraphics, res);
   }

   @Deprecated
   @Override
   public void drawSetChange(MinimapSession session, class_332 guiGraphics, class_1041 res) {
      super.drawSetChange(session, guiGraphics, res);
   }

   @Deprecated
   @Override
   public boolean shouldRender(int location) {
      return super.shouldRender(MinimapElementRenderLocation.fromIndex(location));
   }

   @Deprecated
   @Override
   public int getOrder() {
      return super.getOrder();
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
      Waypoint element,
      double partialX,
      double partialY,
      boolean cave,
      float partialTicks
   ) {
      if (this.compatibleRenderInfo == null) {
         this.compatibleRenderInfo = new MinimapElementRenderInfo(
            MinimapElementRenderLocation.fromIndex(location), renderEntity, player, new class_243(renderX, renderY, renderZ), cave, partialTicks, framebuffer
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
            MinimapElementRenderLocation.fromIndex(location), renderEntity, player, new class_243(renderX, renderY, renderZ), false, 1.0F, null
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
}
