package xaero.common.minimap.waypoints.render;

import net.minecraft.class_1041;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4184;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorldManager;

@Deprecated
public final class WaypointsGuiRenderer extends xaero.hud.minimap.waypoint.render.WaypointsGuiRenderer {
   private MinimapElementRenderInfo compatibleRenderInfo;
   private boolean temporaryWaypointsGlobal;

   public WaypointsGuiRenderer(WaypointReader elementReader, WaypointRenderProvider provider, WaypointGuiRenderContext context) {
      super(elementReader, provider, context);
   }

   @Deprecated
   public void updateWaypointCollection(IXaeroMinimap modMain) {
      super.updateWaypointCollection();
      MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
      MinimapWorldManager manager = session.getWorldManager();
      class_4184 activeRender = class_310.method_1551().field_1773.method_19418();
      class_243 cameraPos = activeRender.method_19326();
      this.context.dimCoordinateScale = session.getDimensionHelper().getDimCoordinateScale(manager.getCurrentWorld());
      double cameraPosMultiplier = class_310.method_1551().field_1687.method_8597().comp_646() / this.context.dimCoordinateScale;
      Waypoint.RENDER_SORTING_POS = new class_243(cameraPos.field_1352 * cameraPosMultiplier, cameraPos.field_1351, cameraPos.field_1350 * cameraPosMultiplier);
   }

   @Deprecated
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
      super.drawIconOnGUI(
         guiGraphics, rendererHelper, w, drawX, drawY, renderTypeBuffer, waypointBackgroundConsumer, renderTypeBuffer.getBuffer(CustomRenderTypes.GUI_NEAREST)
      );
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
}
