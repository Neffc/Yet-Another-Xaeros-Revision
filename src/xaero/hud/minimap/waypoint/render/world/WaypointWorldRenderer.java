package xaero.hud.minimap.waypoint.render.world;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import xaero.common.HudMod;
import xaero.common.effect.Effects;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.gui.GuiMisc;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointUtil;
import xaero.common.minimap.waypoints.render.WaypointsIngameRenderer;
import xaero.common.misc.Misc;
import xaero.common.misc.OptimizedMath;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.element.render.MinimapElementReader;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.element.render.MinimapElementRenderer;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointPurpose;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;

public abstract class WaypointWorldRenderer extends MinimapElementRenderer<Waypoint, WaypointWorldRenderContext> {
   private Vector3f lookVector;
   private boolean temporaryWaypointsGlobal;
   private double waypointsDistance;
   private double waypointsDistanceMin;
   private int distanceSetting;
   private boolean displayShortDistances;
   private boolean dimensionScaleDistance;
   private double clampDepth;
   private int lookingAtAngle;
   private int lookingAtAngleVertical;
   private boolean keepWaypointNames;
   private int autoConvertWaypointDistanceToKmThreshold;
   private int waypointDistancePrecision;
   private float iconScale;
   private int distanceTextScale;
   private int nameScale;
   protected int opacity;
   private float cameraAngleYaw;
   private float cameraAnglePitch;
   private String subWorldName;
   private MinimapRendererHelper helper;
   private class_327 fontRenderer;
   private class_4598 minimapBufferSource;
   protected class_4588 texturedIconConsumer;
   protected class_4588 waypointBackgroundConsumer;

   protected WaypointWorldRenderer(
      MinimapElementReader<Waypoint, WaypointWorldRenderContext> elementReader, WaypointWorldRenderProvider provider, WaypointWorldRenderContext context
   ) {
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
      double offY = (double)w.getY() + 1.0 - renderInfo.renderPos.field_1351;
      if (!w.isYIncluded()) {
         offY = renderInfo.renderEntityPos.field_1351 + 1.0 - renderInfo.renderPos.field_1351;
      }

      double offZ = wZ - renderInfo.renderPos.field_1350;
      double distance2D = Math.sqrt(offX * offX + offZ * offZ);
      if (this.waypointsDistanceMin != 0.0 && distance2D < this.waypointsDistanceMin) {
         return false;
      } else {
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
            Vector3f lookVector = this.lookVector;
            double depth = offX * (double)lookVector.x() + offY * (double)lookVector.y() + offZ * (double)lookVector.z();
            double xFromEntity = wX - renderInfo.renderEntityPos.field_1352;
            double yFromEntity = (double)w.getY() - renderInfo.renderEntityPos.field_1351;
            if (!w.isYIncluded()) {
               yFromEntity = 0.0;
            }

            double zFromEntity = wZ - renderInfo.renderEntityPos.field_1350;
            double distanceFromEntity = Math.sqrt(xFromEntity * xFromEntity + yFromEntity * yFromEntity + zFromEntity * zFromEntity);
            boolean usingNearbyDisplay = distanceFromEntity <= 20.0 && !this.displayShortDistances;
            boolean displayingDistance = !usingNearbyDisplay && highlighted;
            String distanceText = displayingDistance ? this.getDistanceText(distanceFromEntity) : null;
            String name = null;
            if (usingNearbyDisplay || displayingDistance && this.keepWaypointNames || !displayingDistance && w.getPurpose() == WaypointPurpose.DEATH) {
               name = w.getLocalizedName();
            }

            class_327 fontRenderer = this.fontRenderer;
            class_4598 bufferSource = this.minimapBufferSource;
            float iconScale = this.iconScale;
            int nameScale = this.nameScale;
            int halfIconPixel = (int)iconScale / 2;
            class_4587 matrixStack = guiGraphics.method_51448();
            if (renderInfo.location == MinimapElementRenderLocation.IN_WORLD && depth < this.clampDepth) {
               float scale = (float)(this.clampDepth / depth);
               matrixStack.method_22905(scale, scale, 1.0F);
            }

            matrixStack.method_22904((double)halfIconPixel, 0.0, optionalDepth);
            this.renderIconWithLabels(
               w,
               highlighted,
               name,
               distanceText,
               this.subWorldName,
               iconScale,
               nameScale,
               this.distanceTextScale,
               fontRenderer,
               halfIconPixel,
               matrixStack,
               bufferSource
            );
            return true;
         }
      }
   }

   @Override
   public void preRender(
      MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      class_310 mc = class_310.method_1551();
      class_4184 activeRender = mc.field_1773.method_19418();
      MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
      MinimapWorldManager manager = session.getWorldManager();
      MinimapWorld currentWorld = manager.getCurrentWorld();
      ModSettings settings = HudMod.INSTANCE.getSettings();
      this.lookVector = activeRender.method_19335().get(new Vector3f());
      this.cameraAngleYaw = activeRender.method_19330();
      this.cameraAnglePitch = activeRender.method_19329();
      double fov = ((Integer)mc.field_1690.method_41808().method_41753()).doubleValue();
      int screenWidth = mc.method_22683().method_4489();
      int screenHeight = mc.method_22683().method_4506();
      this.subWorldName = null;
      if (currentWorld != null && manager.getAutoWorld() != currentWorld) {
         this.subWorldName = "(" + currentWorld.getContainer().getSubName() + ")";
      }

      this.context.dimCoordinateScale = session.getDimensionHelper().getDimCoordinateScale(manager.getCurrentWorld());
      this.context.renderEntityPos = renderInfo.renderEntityPos;
      int displayMultipleWaypointInfo = settings.displayMultipleWaypointInfo;
      this.context.onlyMainInfo = displayMultipleWaypointInfo == 0 || displayMultipleWaypointInfo == 1 && !renderInfo.renderEntity.method_5715();
      this.temporaryWaypointsGlobal = settings.temporaryWaypointsGlobal;
      this.waypointsDistance = settings.getMaxWaypointsDistance();
      this.waypointsDistanceMin = settings.waypointsDistanceMin;
      this.distanceSetting = settings.distance;
      this.displayShortDistances = settings.alwaysShowDistance;
      this.dimensionScaleDistance = settings.dimensionScaledMaxWaypointDistance;
      this.clampDepth = settings.getWaypointsClampDepth(fov, screenHeight);
      this.lookingAtAngle = class_3532.method_15340(settings.lookingAtAngle, 0, 180);
      this.lookingAtAngleVertical = class_3532.method_15340(settings.lookingAtAngleVertical, 0, 180);
      this.keepWaypointNames = settings.keepWaypointNames;
      this.autoConvertWaypointDistanceToKmThreshold = settings.autoConvertWaypointDistanceToKmThreshold;
      this.waypointDistancePrecision = settings.waypointDistancePrecision;
      this.iconScale = settings.getWaypointsIngameIconScale();
      this.distanceTextScale = (int)Math.ceil((double)settings.getWaypointsIngameDistanceScale());
      this.nameScale = settings.getWaypointsIngameNameScale();
      this.opacity = settings.waypointOpacityIngame;
      this.context.interactionBoxTop = this.distanceSetting == 0 || this.lookingAtAngleVertical == 0
         ? 0
         : (
            this.distanceSetting != 2 && this.lookingAtAngleVertical < 90
               ? -OptimizedMath.myFloor(
                  (double)(screenHeight / 2) * Math.tan(Math.toRadians((double)this.lookingAtAngleVertical)) / Math.tan(Math.toRadians(fov / 2.0))
               )
               : -screenHeight
         );
      double horizontalTan = Math.tan(Math.toRadians(fov / 2.0)) * (double)screenWidth / (double)screenHeight;
      this.context.interactionBoxLeft = this.distanceSetting == 0 || this.lookingAtAngle == 0
         ? 0
         : (
            this.distanceSetting != 2 && this.lookingAtAngle < 90
               ? -OptimizedMath.myFloor((double)(screenWidth / 2) * Math.tan(Math.toRadians((double)this.lookingAtAngle)) / horizontalTan)
               : -screenWidth
         );
      if (class_310.method_1551().method_1573()) {
         this.iconScale = (float)(Math.ceil((double)(this.iconScale / 2.0F)) * 2.0);
         this.distanceTextScale = (this.distanceTextScale + 1) / 2 * 2;
         this.nameScale = (this.nameScale + 1) / 2 * 2;
      }

      this.helper = HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().getHelper();
      this.fontRenderer = mc.field_1772;
      RenderSystem.disableDepthTest();
      vanillaBufferSource.method_22993();
      this.minimapBufferSource = HudMod.INSTANCE.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
      this.waypointBackgroundConsumer = this.minimapBufferSource.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);
      this.texturedIconConsumer = this.minimapBufferSource.getBuffer(CustomRenderTypes.GUI_NEAREST);
   }

   @Override
   public void postRender(
      MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      this.minimapBufferSource.method_22993();
      RenderSystem.enableDepthTest();
      RenderSystem.depthFunc(515);
      this.context.onlyMainInfo = false;
      this.context.renderEntityPos = null;
      this.fontRenderer = null;
      this.minimapBufferSource = null;
      this.waypointBackgroundConsumer = null;
      this.texturedIconConsumer = null;
   }

   protected void renderIconWithLabels(
      Waypoint w,
      boolean highlit,
      String name,
      String distanceText,
      String subWorldName,
      float iconScale,
      int nameScale,
      int distanceTextScale,
      class_327 fontRenderer,
      int halfIconPixel,
      class_4587 matrixStack,
      class_4598 bufferSource
   ) {
      matrixStack.method_22905(iconScale, iconScale, 1.0F);
      this.renderIcon(w, highlit, matrixStack, fontRenderer, bufferSource);
      matrixStack.method_22905(1.0F / iconScale, 1.0F / iconScale, 1.0F);
      matrixStack.method_46416((float)(-halfIconPixel), 0.0F, 0.0F);
      matrixStack.method_46416(0.0F, 2.0F, 0.0F);
      float labelAlpha = 0.3529412F;
      if ((distanceText != null || name != null) && subWorldName != null) {
         this.renderWaypointLabel(subWorldName, matrixStack, this.helper, fontRenderer, nameScale, labelAlpha);
         matrixStack.method_46416(0.0F, 2.0F, 0.0F);
      }

      if (name != null) {
         this.renderWaypointLabel(name, matrixStack, this.helper, fontRenderer, nameScale, labelAlpha);
      }

      matrixStack.method_46416(0.0F, 2.0F, 0.0F);
      if (distanceText != null) {
         this.renderWaypointLabel(distanceText, matrixStack, this.helper, fontRenderer, distanceTextScale, labelAlpha);
      }
   }

   protected void renderIcon(Waypoint w, boolean highlit, class_4587 matrixStack, class_327 fontRenderer, class_4598 bufferSource) {
      int color = w.getWaypointColor().getHex();
      float red = (float)(color >> 16 & 0xFF) / 255.0F;
      float green = (float)(color >> 8 & 0xFF) / 255.0F;
      float blue = (float)(color & 0xFF) / 255.0F;
      float alpha = 0.52274513F * (float)this.opacity / 100.0F;
      if (highlit && this.context.onlyMainInfo) {
         alpha = Math.min(1.0F, alpha * 1.5F);
      }

      int initialsWidth = w.getPurpose() == WaypointPurpose.DEATH ? 7 : fontRenderer.method_1727(w.getInitials());
      int addedFrame = WaypointUtil.getAddedMinimapIconFrame(initialsWidth);
      this.renderColorBackground(matrixStack, addedFrame, red, green, blue, alpha, this.waypointBackgroundConsumer);
      if (w.getPurpose() == WaypointPurpose.DEATH) {
         this.renderTexturedIcon(matrixStack, addedFrame, 0, 78, 0.9882F, 0.9882F, 0.9882F, 1.0F, this.texturedIconConsumer);
      } else {
         Misc.drawNormalText(matrixStack, w.getInitials(), (float)(-initialsWidth / 2), -8.0F, -1, false, bufferSource);
      }
   }

   private void renderColorBackground(class_4587 matrixStack, int addedFrame, float r, float g, float b, float a, class_4588 waypointBackgroundConsumer) {
      Matrix4f matrix = matrixStack.method_23760().method_23761();
      waypointBackgroundConsumer.method_22918(matrix, (float)(-5 - addedFrame), -9.0F, 0.0F).method_22915(r, g, b, a).method_1344();
      waypointBackgroundConsumer.method_22918(matrix, (float)(-5 - addedFrame), 0.0F, 0.0F).method_22915(r, g, b, a).method_1344();
      waypointBackgroundConsumer.method_22918(matrix, (float)(4 + addedFrame), 0.0F, 0.0F).method_22915(r, g, b, a).method_1344();
      waypointBackgroundConsumer.method_22918(matrix, (float)(4 + addedFrame), -9.0F, 0.0F).method_22915(r, g, b, a).method_1344();
   }

   private void renderTexturedIcon(
      class_4587 matrixStack, int addedFrame, int textureX, int textureY, float r, float g, float b, float a, class_4588 vertexBuffer
   ) {
      float f = 0.00390625F;
      float f1 = 0.00390625F;
      Matrix4f matrix = matrixStack.method_23760().method_23761();
      vertexBuffer.method_22918(matrix, (float)(-5 - addedFrame), (float)(-9 - addedFrame), 0.0F)
         .method_22915(r, g, b, a)
         .method_22913((float)textureX * f, (float)textureY * f1)
         .method_1344();
      vertexBuffer.method_22918(matrix, (float)(-5 - addedFrame), (float)addedFrame, 0.0F)
         .method_22915(r, g, b, a)
         .method_22913((float)textureX * f, (float)(textureY + 9 + addedFrame * 2) * f1)
         .method_1344();
      vertexBuffer.method_22918(matrix, (float)(4 + addedFrame), (float)addedFrame, 0.0F)
         .method_22915(r, g, b, a)
         .method_22913((float)(textureX + 9 + addedFrame * 2) * f, (float)(textureY + 9 + addedFrame * 2) * f1)
         .method_1344();
      vertexBuffer.method_22918(matrix, (float)(4 + addedFrame), (float)(-9 - addedFrame), 0.0F)
         .method_22915(r, g, b, a)
         .method_22913((float)(textureX + 9 + addedFrame * 2) * f, (float)textureY * f1)
         .method_1344();
   }

   protected void renderWaypointLabel(String label, class_4587 matrixStack, MinimapRendererHelper helper, class_327 fontRenderer, int labelScale, float bgAlpha) {
      int nameWidth = fontRenderer.method_1727(label);
      int backgroundWidth = nameWidth + 3;
      int halfBackgroundWidth = backgroundWidth / 2;
      int halfPixel = 0;
      if ((backgroundWidth & 1) != 0) {
         halfPixel = labelScale - labelScale / 2;
         matrixStack.method_46416((float)(-halfPixel), 0.0F, 0.0F);
      }

      matrixStack.method_22905((float)labelScale, (float)labelScale, 1.0F);
      helper.addColoredRectToExistingBuffer(
         matrixStack.method_23760().method_23761(),
         this.waypointBackgroundConsumer,
         (float)(-halfBackgroundWidth),
         0.0F,
         backgroundWidth,
         9,
         0.0F,
         0.0F,
         0.0F,
         bgAlpha
      );
      Misc.drawNormalText(matrixStack, label, (float)(-halfBackgroundWidth + 2), 1.0F, -1, false, this.minimapBufferSource);
      matrixStack.method_46416(0.0F, 9.0F, 0.0F);
      matrixStack.method_22905(1.0F / (float)labelScale, 1.0F / (float)labelScale, 1.0F);
      if ((backgroundWidth & 1) != 0) {
         matrixStack.method_46416((float)halfPixel, 0.0F, 0.0F);
      }
   }

   private String getDistanceText(double distanceFromEntity) {
      return this.autoConvertWaypointDistanceToKmThreshold != -1 && distanceFromEntity >= (double)this.autoConvertWaypointDistanceToKmThreshold
         ? GuiMisc.getFormat(this.waypointDistancePrecision).format(distanceFromEntity / 1000.0) + "km"
         : GuiMisc.getFormat(this.waypointDistancePrecision).format(distanceFromEntity) + "m";
   }

   @Override
   public boolean shouldRender(MinimapElementRenderLocation location) {
      if (!HudMod.INSTANCE.getSettings().getShowIngameWaypoints()) {
         return false;
      } else {
         class_310 mc = class_310.method_1551();
         return mc.field_1724 != null && !Misc.hasEffect(mc.field_1724, Effects.NO_WAYPOINTS) && !Misc.hasEffect(mc.field_1724, Effects.NO_WAYPOINTS_HARMFUL);
      }
   }

   @Override
   public int getOrder() {
      return 100;
   }

   public static final class Builder {
      private Builder() {
      }

      private WaypointWorldRenderer.Builder setDefault() {
         return this;
      }

      public WaypointWorldRenderer build() {
         WaypointWorldRenderContext context = new WaypointWorldRenderContext();
         return new WaypointsIngameRenderer(
            new WaypointWorldRenderReader(context), new WaypointWorldRenderProvider(), context, new Vector4f(0.0F, 0.0F, 0.0F, 1.0F)
         );
      }

      public static WaypointWorldRenderer.Builder begin() {
         return new WaypointWorldRenderer.Builder().setDefault();
      }
   }
}
