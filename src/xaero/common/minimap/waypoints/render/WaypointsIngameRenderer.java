package xaero.common.minimap.waypoints.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_308;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import xaero.common.AXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.effect.Effects;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.gui.GuiMisc;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointSet;
import xaero.common.minimap.waypoints.WaypointUtil;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;

public class WaypointsIngameRenderer {
   private AXaeroMinimap modMain;
   private class_4587 identityMatrixStack;
   private class_4587 identityMatrixStackOverlay;
   private Vector4f origin4f;
   private List<Waypoint> sortingList;
   private WaypointFilterParams filterParams;
   private Predicate<Waypoint> filter;
   private Waypoint previousClosest;
   private Waypoint workingClosest;
   private double workingClosestCos;
   private final WaypointDeleter waypointReachDeleter;

   public WaypointsIngameRenderer(AXaeroMinimap modMain, WaypointDeleter waypointReachDeleter, class_310 mc) {
      this.modMain = modMain;
      this.identityMatrixStack = new class_4587();
      this.identityMatrixStackOverlay = new class_4587();
      this.origin4f = new Vector4f(0.0F, 0.0F, 0.0F, 1.0F);
      this.sortingList = new ArrayList<>();
      this.filterParams = new WaypointFilterParams();
      this.filter = w -> {
         WaypointFilterParams filterParams = this.filterParams;
         boolean deathpoints = filterParams.deathpoints;
         if (!w.isDisabled()
            && w.getVisibilityType() != 2
            && w.getVisibilityType() != 3
            && (w.getWaypointType() != 1 && w.getWaypointType() != 2 || deathpoints)) {
            double offX = (double)w.getX(filterParams.dimDiv) - filterParams.cameraX + 0.5;
            double offY = (double)w.getY() - filterParams.cameraY + 1.0;
            if (!w.isYIncluded()) {
               offY = filterParams.playerY - filterParams.cameraY + 1.0;
            }

            double offZ = (double)w.getZ(filterParams.dimDiv) - filterParams.cameraZ + 0.5;
            double depth = offX * (double)filterParams.lookVector.x() + offY * (double)filterParams.lookVector.y() + offZ * (double)filterParams.lookVector.z();
            if (depth <= 0.1) {
               return false;
            } else {
               double distanceScale = filterParams.dimensionScaleDistance ? class_310.method_1551().field_1687.method_8597().comp_646() : 1.0;
               double unscaledDistance2D = Math.sqrt(offX * offX + offZ * offZ);
               double distance2D = unscaledDistance2D * distanceScale;
               double waypointsDistance = filterParams.waypointsDistance;
               double waypointsDistanceMin = filterParams.waypointsDistanceMin;
               return w.isOneoffDestination()
                  || (
                        w.getWaypointType() == 1
                           || w.isGlobal()
                           || w.isTemporary() && filterParams.temporaryWaypointsGlobal
                           || waypointsDistance == 0.0
                           || !(distance2D > waypointsDistance)
                     )
                     && (waypointsDistanceMin == 0.0 || !(unscaledDistance2D < waypointsDistanceMin));
            }
         } else {
            return false;
         }
      };
      this.waypointReachDeleter = waypointReachDeleter;
   }

   public void render(XaeroMinimapSession minimapSession, float partial, MinimapProcessor minimap, Matrix4f waypointsProjection, Matrix4f worldModelView) {
      if (this.modMain.getSettings().getShowIngameWaypoints()) {
         class_310 mc = class_310.method_1551();
         if (mc.field_1724 == null || mc.field_1724.method_6059(Effects.NO_WAYPOINTS) || mc.field_1724.method_6059(Effects.NO_WAYPOINTS_HARMFUL)) {
            return;
         }

         if (this.modMain.getSupportMods().vivecraft) {
            return;
         }

         class_327 fontrenderer = mc.field_1772;
         if (fontrenderer == null) {
            return;
         }

         class_4587 matrixStack = this.identityMatrixStack;
         class_4587 matrixStackOverlay = this.identityMatrixStackOverlay;
         MinimapRendererHelper helper = this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().getHelper();
         WaypointsManager waypointsManager = minimapSession.getWaypointsManager();
         String subworldName = null;
         if (waypointsManager.getCurrentWorld() != null && waypointsManager.getAutoWorld() != waypointsManager.getCurrentWorld()) {
            subworldName = "(" + waypointsManager.getCurrentWorld().getContainer().getSubName() + ")";
         }

         class_1297 entity = mc.method_1560();
         class_4184 activeRender = mc.field_1773.method_19418();
         double actualEntityX = entity.method_23317();
         double actualEntityY = entity.method_23318();
         double actualEntityZ = entity.method_23321();
         double smoothEntityY = minimap.getEntityRadar().getEntityY(entity, partial);
         class_243 cameraPos = activeRender.method_19326();
         double dimDiv = waypointsManager.getDimensionDivision(waypointsManager.getCurrentWorld());
         Waypoint.RENDER_SORTING_POS = new class_243(cameraPos.field_1352 * dimDiv, cameraPos.field_1351, cameraPos.field_1350 * dimDiv);
         double d3 = cameraPos.method_10216();
         double d4 = cameraPos.method_10214();
         double d5 = cameraPos.method_10215();
         class_289 tessellator = class_289.method_1348();
         class_287 bufferbuilder = tessellator.method_1349();
         RenderSystem.disableCull();
         matrixStack.method_22903();
         matrixStack.method_23760().method_23761().mul(worldModelView);
         class_308.method_24210();
         double fov = ((Integer)mc.field_1690.method_41808().method_41753()).doubleValue();
         int screenWidth = class_310.method_1551().method_22683().method_4489();
         int screenHeight = class_310.method_1551().method_22683().method_4506();
         float cameraAngleYaw = activeRender.method_19330();
         float cameraAnglePitch = activeRender.method_19329();
         Vector3f lookVector = activeRender.method_19335().get(new Vector3f());
         double clampDepth = this.modMain.getSettings().getWaypointsClampDepth(fov, screenHeight);
         List<Waypoint> sortingList = this.sortingList;
         sortingList.clear();
         if (waypointsManager.getWaypoints() != null) {
            if (this.modMain.getSettings().renderAllSets) {
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

         matrixStackOverlay.method_22903();
         matrixStackOverlay.method_46416(0.0F, 0.0F, -2980.0F);
         if (!sortingList.isEmpty()) {
            this.waypointReachDeleter.begin();
            ModSettings settings = this.modMain.getSettings();
            this.filterParams
               .setParams(
                  d3,
                  d4,
                  d5,
                  lookVector,
                  dimDiv,
                  settings.getDeathpoints(),
                  settings.temporaryWaypointsGlobal,
                  settings.getMaxWaypointsDistance(),
                  settings.waypointsDistanceMin,
                  smoothEntityY,
                  settings.dimensionScaledMaxWaypointDistance
               );
            Stream<Waypoint> waypointStream = sortingList.stream().filter(this.filter).sorted();
            class_4598 cvcRenderTypeBuffer = this.modMain.getInterfaceRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
            class_4588 waypointBackgroundConsumer = cvcRenderTypeBuffer.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);
            double minDistance = settings.waypointsDistanceMin;
            this.renderWaypointsIterator(
               matrixStack,
               matrixStackOverlay,
               helper,
               waypointStream.iterator(),
               d3,
               d4,
               d5,
               entity,
               bufferbuilder,
               tessellator,
               dimDiv,
               actualEntityX,
               actualEntityY,
               actualEntityZ,
               smoothEntityY,
               fov,
               screenHeight,
               cameraAngleYaw,
               cameraAnglePitch,
               lookVector,
               clampDepth,
               cvcRenderTypeBuffer,
               waypointBackgroundConsumer,
               fontrenderer,
               waypointsProjection,
               screenWidth,
               false,
               minDistance,
               subworldName
            );
            this.waypointReachDeleter.deleteCollected(waypointsManager.getCurrentWorld(), settings.renderAllSets);
         }

         matrixStackOverlay.method_22909();
         RenderSystem.enableDepthTest();
         RenderSystem.enableCull();
         RenderSystem.depthMask(true);
         class_308.method_24211();
         matrixStack.method_22909();
      }
   }

   private void renderWaypointsIterator(
      class_4587 matrixStack,
      class_4587 matrixStackOverlay,
      MinimapRendererHelper helper,
      Iterator<Waypoint> iter,
      double d3,
      double d4,
      double d5,
      class_1297 entity,
      class_287 bufferbuilder,
      class_289 tessellator,
      double dimDiv,
      double actualEntityX,
      double actualEntityY,
      double actualEntityZ,
      double smoothEntityY,
      double fov,
      int screenHeight,
      float cameraAngleYaw,
      float cameraAnglePitch,
      Vector3f lookVector,
      double clampDepth,
      class_4598 renderTypeBuffer,
      class_4588 waypointBackgroundConsumer,
      class_327 fontrenderer,
      Matrix4f waypointsProjection,
      int screenWidth,
      boolean detailedDisplayAllowed,
      double minDistance,
      String subworldName
   ) {
      int count = 0;
      this.workingClosest = null;
      int displayMultipleWaypointInfo = this.modMain.getSettings().displayMultipleWaypointInfo;
      boolean deleteReachedDeathpoints = this.modMain.getSettings().deleteReachedDeathpoints;
      boolean onlyMainInfo = displayMultipleWaypointInfo == 0 || displayMultipleWaypointInfo == 1 && !entity.method_5715();

      while (iter.hasNext()) {
         Waypoint w = iter.next();
         this.renderWaypointIngame(
            matrixStack,
            matrixStackOverlay,
            helper,
            cameraAngleYaw,
            cameraAnglePitch,
            lookVector,
            w,
            this.modMain,
            clampDepth,
            d3,
            d4,
            d5,
            entity,
            bufferbuilder,
            tessellator,
            dimDiv,
            actualEntityX,
            actualEntityY,
            actualEntityZ,
            smoothEntityY,
            renderTypeBuffer,
            waypointBackgroundConsumer,
            fontrenderer,
            waypointsProjection,
            screenWidth,
            screenHeight,
            false,
            onlyMainInfo,
            minDistance,
            deleteReachedDeathpoints,
            subworldName
         );
         if (++count < 19500) {
            matrixStackOverlay.method_46416(0.0F, 0.0F, 0.1F);
         }
      }

      if (onlyMainInfo && this.previousClosest != null) {
         this.renderWaypointIngame(
            matrixStack,
            matrixStackOverlay,
            helper,
            cameraAngleYaw,
            cameraAnglePitch,
            lookVector,
            this.previousClosest,
            this.modMain,
            clampDepth,
            d3,
            d4,
            d5,
            entity,
            bufferbuilder,
            tessellator,
            dimDiv,
            actualEntityX,
            actualEntityY,
            actualEntityZ,
            smoothEntityY,
            renderTypeBuffer,
            waypointBackgroundConsumer,
            fontrenderer,
            waypointsProjection,
            screenWidth,
            screenHeight,
            true,
            onlyMainInfo,
            minDistance,
            false,
            subworldName
         );
      }

      this.previousClosest = this.workingClosest;
      renderTypeBuffer.method_22993();
      RenderSystem.clear(256, class_310.field_1703);
   }

   private void renderWaypointIngame(
      class_4587 matrixStack,
      class_4587 matrixStackOverlay,
      MinimapRendererHelper helper,
      float cameraAngleYaw,
      float cameraAnglePitch,
      Vector3f lookVector,
      Waypoint w,
      AXaeroMinimap modMain,
      double depthClamp,
      double d3,
      double d4,
      double d5,
      class_1297 entity,
      class_287 bufferBuilder,
      class_289 tessellator,
      double dimDiv,
      double actualEntityX,
      double actualEntityY,
      double actualEntityZ,
      double smoothEntityY,
      class_4598 textRenderTypeBuffer,
      class_4588 waypointBackgroundConsumer,
      class_327 fontrenderer,
      Matrix4f waypointsProjection,
      int screenWidth,
      int screenHeight,
      boolean isTheMain,
      boolean onlyMainInfo,
      double minDistance,
      boolean deleteReachedDeathpoints,
      String subworldName
   ) {
      int wX = w.getX(dimDiv);
      int wZ = w.getZ(dimDiv);
      double offX = (double)wX - d3 + 0.5;
      double offY = (double)w.getY() - d4 + 1.0;
      if (!w.isYIncluded()) {
         offY = smoothEntityY - d4 + 1.0;
      }

      double offZ = (double)wZ - d5 + 0.5;
      double depth = offX * (double)lookVector.x() + offY * (double)lookVector.y() + offZ * (double)lookVector.z();
      double correctOffX = actualEntityX - (double)wX - 0.5;
      double correctOffY = actualEntityY - (double)w.getY();
      if (!w.isYIncluded()) {
         correctOffY = 0.0;
      }

      double correctOffZ = actualEntityZ - (double)wZ - 0.5;
      double correctDistance = Math.sqrt(correctOffX * correctOffX + correctOffY * correctOffY + correctOffZ * correctOffZ);
      double distance2D = Math.sqrt(offX * offX + offZ * offZ);
      double distance = Math.sqrt(offX * offX + offY * offY + offZ * offZ);
      if ((deleteReachedDeathpoints || w.getWaypointType() != 1 && w.getWaypointType() != 2)
         && w.isOneoffDestination()
         && System.currentTimeMillis() - w.getCreatedAt() > 5000L
         && correctDistance < 4.0) {
         this.waypointReachDeleter.add(w);
      }

      if (minDistance == 0.0 || !(distance2D < minDistance)) {
         float textSize = 1.0F;
         String name = w.getLocalizedName();
         String distanceText = "";
         boolean couldShowDistance = false;
         boolean showDistance = false;
         if (correctDistance > 20.0 || modMain.getSettings().alwaysShowDistance) {
            if (modMain.getSettings().distance == 1) {
               float Z = (float)(offZ == 0.0 ? 0.001 : offZ);
               float angle = (float)Math.toDegrees(Math.atan(-offX / (double)Z));
               if (offZ < 0.0) {
                  if (offX < 0.0) {
                     angle += 180.0F;
                  } else {
                     angle -= 180.0F;
                  }
               }

               float offset = class_3532.method_15393(angle - cameraAngleYaw);
               couldShowDistance = Math.abs(offset) < (float)modMain.getSettings().lookingAtAngle;
               if (modMain.getSettings().lookingAtAngleVertical != 180) {
                  float verticalAngle = (float)Math.toDegrees(Math.atan(-offY / (distance2D == 0.0 ? 1.0E-5 : distance2D)));
                  couldShowDistance = couldShowDistance && Math.abs(verticalAngle - cameraAnglePitch) < (float)modMain.getSettings().lookingAtAngleVertical;
               }
            } else if (modMain.getSettings().distance == 2) {
               couldShowDistance = true;
            }

            if (couldShowDistance) {
               if (isTheMain) {
                  showDistance = true;
               } else {
                  double cos = depth / distance;
                  if (this.workingClosest == null || cos > this.workingClosestCos) {
                     this.workingClosest = w;
                     this.workingClosestCos = cos;
                  }

                  if (!onlyMainInfo) {
                     showDistance = true;
                  }
               }
            }

            if (showDistance) {
               int autoConvertWaypointDistanceToKmThreshold = modMain.getSettings().autoConvertWaypointDistanceToKmThreshold;
               if (autoConvertWaypointDistanceToKmThreshold != -1 && correctDistance >= (double)autoConvertWaypointDistanceToKmThreshold) {
                  distanceText = GuiMisc.getFormat(modMain.getSettings().waypointDistancePrecision).format(correctDistance / 1000.0) + "km";
               } else {
                  distanceText = GuiMisc.getFormat(modMain.getSettings().waypointDistancePrecision).format(correctDistance) + "m";
               }

               if (!modMain.getSettings().keepWaypointNames) {
                  name = "";
               }
            } else {
               name = "";
            }
         }

         if (!onlyMainInfo || this.previousClosest != w || isTheMain) {
            matrixStack.method_22903();
            matrixStackOverlay.method_22903();
            if (distance > 250000.0) {
               double offScaler = 250000.0 / distance;
               offX *= offScaler;
               offY *= offScaler;
               offZ *= offScaler;
            }

            matrixStack.method_22904(offX, offY, offZ);
            this.drawAsOverlay(
               matrixStack,
               matrixStackOverlay,
               helper,
               w,
               modMain.getSettings(),
               bufferBuilder,
               tessellator,
               fontrenderer,
               name,
               distanceText,
               textSize,
               showDistance,
               textRenderTypeBuffer,
               waypointBackgroundConsumer,
               waypointsProjection,
               screenWidth,
               screenHeight,
               depthClamp,
               depth,
               isTheMain,
               subworldName
            );
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.method_22909();
            matrixStackOverlay.method_22909();
         }
      }
   }

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

   public void drawIconInWorld(
      class_4587 matrixStack,
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
      boolean isTheMain,
      String subworldName
   ) {
      double iconScale = (double)settings.getWaypointsIngameIconScale();
      double distanceScale = (double)settings.getWaypointsIngameDistanceScale();
      double nameScale = (double)settings.getWaypointsIngameNameScale();
      int ingameOpacity = settings.waypointOpacityIngame;
      int addedFrame = 0;
      if (class_310.method_1551().method_1573()) {
         iconScale = (double)((int)((iconScale + 1.0) / 2.0) * 2);
         distanceScale = (double)((int)((distanceScale + 1.0) / 2.0) * 2);
         nameScale = (double)((int)((nameScale + 1.0) / 2.0) * 2);
      }

      int c = ModSettings.COLORS[w.getColor()];
      float red = (float)(c >> 16 & 0xFF) / 255.0F;
      float green = (float)(c >> 8 & 0xFF) / 255.0F;
      float blue = (float)(c & 0xFF) / 255.0F;
      float alpha = 133.3F * ((float)ingameOpacity / 100.0F) / 255.0F;
      if (isTheMain) {
         alpha = Math.min(1.0F, alpha * 1.5F);
      }

      int halfIconPixel = (int)iconScale / 2;
      matrixStack.method_46416((float)halfIconPixel, 0.0F, 0.0F);
      matrixStack.method_22905((float)iconScale, (float)iconScale, 1.0F);
      if (w.getWaypointType() != 1) {
         int initialsWidth = fontrenderer.method_1727(w.getSymbol());
         addedFrame = WaypointUtil.getAddedMinimapIconFrame(addedFrame, initialsWidth);
         this.renderColorBackground(matrixStack, addedFrame, red, green, blue, alpha, waypointBackgroundConsumer);
         Misc.drawNormalText(matrixStack, w.getSymbol(), (float)(-initialsWidth / 2), -8.0F, -1, false, renderTypeBuffer);
      } else {
         addedFrame = WaypointUtil.getAddedMinimapIconFrame(addedFrame, 7);
         this.renderColorBackground(matrixStack, addedFrame, red, green, blue, alpha, waypointBackgroundConsumer);
         class_4588 guiNearestConsumer = renderTypeBuffer.getBuffer(CustomRenderTypes.GUI_NEAREST);
         this.renderTexturedIcon(matrixStack, addedFrame, 0, 78, 0.9882F, 0.9882F, 0.9882F, 1.0F, guiNearestConsumer);
         if (!showDistance) {
            name = w.getLocalizedName();
         }
      }

      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(770, 771, 1, 0);
      boolean showingName = name.length() > 0;
      matrixStack.method_22905((float)(1.0 / iconScale), (float)(1.0 / iconScale), 1.0F);
      matrixStack.method_46416((float)(-halfIconPixel), 0.0F, 0.0F);
      matrixStack.method_46416(0.0F, 2.0F, 0.0F);
      float labelAlpha = 0.3529412F;
      if ((showDistance || showingName) && subworldName != null) {
         this.renderWaypointLabel(matrixStack, helper, fontrenderer, subworldName, nameScale, labelAlpha, renderTypeBuffer, waypointBackgroundConsumer);
         matrixStack.method_46416(0.0F, 2.0F, 0.0F);
      }

      if (showingName) {
         this.renderWaypointLabel(matrixStack, helper, fontrenderer, name, nameScale, labelAlpha, renderTypeBuffer, waypointBackgroundConsumer);
      }

      matrixStack.method_46416(0.0F, 2.0F, 0.0F);
      if (distance.length() > 0) {
         this.renderWaypointLabel(matrixStack, helper, fontrenderer, distance, distanceScale, labelAlpha, renderTypeBuffer, waypointBackgroundConsumer);
      }
   }

   private void renderWaypointLabel(
      class_4587 matrixStack,
      MinimapRendererHelper helper,
      class_327 fontrenderer,
      String label,
      double labelScale,
      float bgAlpha,
      class_4598 renderTypeBuffer,
      class_4588 waypointBackgroundConsumer
   ) {
      int nameW = fontrenderer.method_1727(label);
      int bgW = nameW + 3;
      int halfBgW = bgW / 2;
      int halfNamePixel = 0;
      if ((bgW & 1) != 0) {
         halfNamePixel = (int)labelScale - (int)labelScale / 2;
         matrixStack.method_46416((float)(-halfNamePixel), 0.0F, 0.0F);
      }

      matrixStack.method_22905((float)labelScale, (float)labelScale, 1.0F);
      helper.addColoredRectToExistingBuffer(
         matrixStack.method_23760().method_23761(), waypointBackgroundConsumer, (float)(-halfBgW), 0.0F, bgW, 9, 0.0F, 0.0F, 0.0F, bgAlpha
      );
      Misc.drawNormalText(matrixStack, label, (float)(-halfBgW + 2), 1.0F, -1, false, renderTypeBuffer);
      matrixStack.method_46416(0.0F, 9.0F, 0.0F);
      matrixStack.method_22905((float)(1.0 / labelScale), (float)(1.0 / labelScale), 1.0F);
      if ((bgW & 1) != 0) {
         matrixStack.method_46416((float)halfNamePixel, 0.0F, 0.0F);
      }

      RenderSystem.enableBlend();
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
}
