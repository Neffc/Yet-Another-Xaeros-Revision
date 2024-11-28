package xaero.common.minimap.waypoints.render;

import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import xaero.common.HudMod;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.element.render.MinimapElementReader;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.render.world.WaypointWorldRenderContext;
import xaero.hud.minimap.waypoint.render.world.WaypointWorldRenderProvider;
import xaero.hud.minimap.waypoint.render.world.WaypointWorldRenderer;

@Deprecated
public class WaypointsIngameRenderer extends WaypointWorldRenderer {
   private class_332 guiGraphics;
   private Vector4f origin4f = new Vector4f(0.0F, 0.0F, 0.0F, 1.0F);

   public WaypointsIngameRenderer(
      MinimapElementReader<Waypoint, WaypointWorldRenderContext> elementReader,
      WaypointWorldRenderProvider provider,
      WaypointWorldRenderContext context,
      Vector4f origin4f
   ) {
      super(elementReader, provider, context);
   }

   @Deprecated
   public void render(MinimapSession session, float partial, MinimapProcessor minimap, Matrix4f waypointsProjection, Matrix4f worldModelView) {
      class_243 renderPos = class_310.method_1551().field_1773.method_19418().method_19326();
      if (this.guiGraphics == null) {
         this.guiGraphics = new class_332(class_310.method_1551(), class_310.method_1551().method_22940().method_23000());
      }

      HudMod.INSTANCE.getMinimap().getWorldRendererHandler().prepareRender(waypointsProjection, worldModelView);
      class_310 mc = class_310.method_1551();
      HudMod.INSTANCE
         .getMinimap()
         .getWorldRendererHandler()
         .render(this.guiGraphics, renderPos, partial, null, mc.field_1687.method_8597().comp_646(), mc.field_1687.method_27983());
   }

   @Deprecated
   public void drawAsOverlay(
      class_4587 matrixStack,
      class_4587 matrixStackOverlay,
      MinimapRendererHelper helper,
      Waypoint w,
      ModSettings settings,
      class_287 vertexBuffer,
      class_289 tessellator,
      class_327 fontrenderer,
      String name,
      String distance,
      float textSize,
      boolean showDistance,
      class_4598 renderTypeBuffer,
      class_4588 waypointBackgroundConsumer,
      Matrix4f waypointsProjection,
      int screenWidth,
      int screenHeight,
      double depthClamp,
      double depth,
      boolean isTheMain,
      String subworldName
   ) {
      this.origin4f.mul(matrixStack.method_23760().method_23761());
      this.origin4f.mul(waypointsProjection);
      int overlayPosX = (int)((1.0F + this.origin4f.x() / this.origin4f.w()) / 2.0F * (float)screenWidth);
      int overlayPosY = (int)((1.0F - this.origin4f.y() / this.origin4f.w()) / 2.0F * (float)screenHeight);
      this.origin4f.set(0.0F, 0.0F, 0.0F, 1.0F);
      matrixStackOverlay.method_46416((float)overlayPosX, (float)overlayPosY, 0.0F);
      if (depth < depthClamp) {
         float scale = (float)(depthClamp / depth);
         matrixStackOverlay.method_22905(scale, scale, scale);
      }

      this.drawIconInWorld(
         matrixStackOverlay,
         helper,
         w,
         settings,
         vertexBuffer,
         tessellator,
         fontrenderer,
         name,
         distance,
         textSize,
         showDistance,
         renderTypeBuffer,
         waypointBackgroundConsumer,
         isTheMain,
         subworldName
      );
   }

   @Deprecated
   public void drawIconInWorld(
      class_4587 matrixStack,
      MinimapRendererHelper helper,
      Waypoint w,
      ModSettings settings,
      class_287 vertexBuffer,
      class_289 tessellator,
      class_327 fontRenderer,
      String name,
      String distanceText,
      float textSize,
      boolean displayingDistance,
      class_4598 bufferSource,
      class_4588 waypointBackgroundConsumer,
      boolean isTheMain,
      String subWorldName
   ) {
      if (!displayingDistance) {
         distanceText = null;
      }

      this.texturedIconConsumer = bufferSource.getBuffer(CustomRenderTypes.GUI_NEAREST);
      this.waypointBackgroundConsumer = waypointBackgroundConsumer;
      this.opacity = settings.waypointOpacityIngame;
      float iconScale = settings.getWaypointsIngameIconScale();
      int distanceTextScale = (int)Math.ceil((double)settings.getWaypointsIngameDistanceScale());
      int nameScale = settings.getWaypointsIngameNameScale();
      if (class_310.method_1551().method_1573()) {
         iconScale = (float)(Math.ceil((double)(iconScale / 2.0F)) * 2.0);
         distanceTextScale = (distanceTextScale + 1) / 2 * 2;
         nameScale = (nameScale + 1) / 2 * 2;
      }

      int halfIconPixel = (int)iconScale / 2;
      matrixStack.method_46416((float)halfIconPixel, 0.0F, 0.0F);
      this.renderIconWithLabels(
         w, isTheMain, name, distanceText, subWorldName, iconScale, nameScale, distanceTextScale, fontRenderer, halfIconPixel, matrixStack, bufferSource
      );
   }
}
