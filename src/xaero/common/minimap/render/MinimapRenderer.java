package xaero.common.minimap.render;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_308;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_638;
import net.minecraft.class_757;
import net.minecraft.class_2338.class_2339;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_4597.class_4598;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.CustomVertexConsumers;
import xaero.common.graphics.GuiHelper;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.misc.OptimizedMath;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.compass.render.CompassRenderer;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.render.WaypointsGuiRenderer;
import xaero.hud.render.TextureLocations;

public abstract class MinimapRenderer {
   public static final int black = -16777216;
   public static final int slime = -2142047936;
   protected IXaeroMinimap modMain;
   protected class_310 mc;
   protected Minimap minimap;
   protected MinimapRendererHelper helper;
   protected WaypointsGuiRenderer waypointsGuiRenderer;
   private int lastMinimapSize;
   protected double zoom = 1.0;
   private class_2339 mutableBlockPos;
   protected final CompassRenderer compassRenderer;
   private double lastMapDimensionScale = 1.0;
   private double lastPlayerDimDiv = 1.0;

   public MinimapRenderer(IXaeroMinimap modMain, class_310 mc, WaypointsGuiRenderer waypointsGuiRenderer, Minimap minimap, CompassRenderer compassRenderer) {
      this.modMain = modMain;
      this.mc = mc;
      this.waypointsGuiRenderer = waypointsGuiRenderer;
      this.minimap = minimap;
      this.helper = new MinimapRendererHelper();
      this.mutableBlockPos = new class_2339();
      this.compassRenderer = compassRenderer;
   }

   public double getRenderAngle(boolean lockedNorth) {
      return lockedNorth ? 90.0 : this.getActualAngle();
   }

   private double getActualAngle() {
      double rotation = (double)this.mc.field_1773.method_19418().method_19330();
      return -90.0 - rotation;
   }

   protected abstract void renderChunks(
      MinimapSession var1,
      class_332 var2,
      MinimapProcessor var3,
      class_243 var4,
      double var5,
      double var7,
      int var9,
      int var10,
      float var11,
      float var12,
      int var13,
      boolean var14,
      boolean var15,
      int var16,
      double var17,
      double var19,
      boolean var21,
      boolean var22,
      ModSettings var23,
      CustomVertexConsumers var24
   );

   public void renderMinimap(
      MinimapSession minimapSession,
      class_332 guiGraphics,
      MinimapProcessor minimap,
      int x,
      int y,
      int width,
      int height,
      double scale,
      int size,
      float partial,
      CustomVertexConsumers cvc
   ) {
      class_4587 matrixStack = guiGraphics.method_51448();
      ModSettings settings = this.modMain.getSettings();
      if (settings.getMinimapSize() != this.lastMinimapSize) {
         this.lastMinimapSize = settings.getMinimapSize();
         minimap.setToResetImage(true);
      }

      minimap.getEntityRadar().setLastRenderViewEntity(this.mc.method_1560());
      int mapSize = minimapSession.getProcessor().getMinimapSize();
      int bufferSize = minimapSession.getProcessor().getMinimapBufferSize(mapSize);
      if (this.minimap.usingFBO()) {
         bufferSize = minimap.getFBOBufferSize();
      }

      float minimapScale = settings.getMinimapScale();
      float mapScale = (float)(scale / (double)minimapScale);
      minimap.updateZoom();
      this.zoom = minimap.getMinimapZoom();
      class_308.method_24210();
      RenderSystem.disableDepthTest();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.pixelStore(3317, 4);
      RenderSystem.pixelStore(3316, 0);
      RenderSystem.pixelStore(3315, 0);
      RenderSystem.pixelStore(3314, 0);
      float sizeFix = (float)bufferSize / 512.0F;
      int shape = settings.minimapShape;
      boolean lockedNorth = settings.getLockNorth(mapSize / 2, shape);
      double angle = Math.toRadians(this.getRenderAngle(lockedNorth));
      double ps = Math.sin(Math.PI - angle);
      double pc = Math.cos(Math.PI - angle);
      boolean useWorldMap = this.modMain.getSupportMods().shouldUseWorldMapChunks() && !minimap.getMinimapWriter().isLoadedNonWorldMap();
      int lightLevel = (int)(
         (1.0F - Math.min(1.0F, this.getSunBrightness(minimap, settings.getLighting()))) * (float)(minimap.getMinimapWriter().getLoadedLevels() - 1)
      );
      boolean cave = minimap.isCaveModeDisplayed();
      boolean circleShape = shape == 1;
      double playerX = minimap.getEntityRadar().getEntityX(this.mc.method_1560(), partial);
      double playerY = minimap.getEntityRadar().getEntityY(this.mc.method_1560(), partial);
      double playerZ = minimap.getEntityRadar().getEntityZ(this.mc.method_1560(), partial);
      double renderX = playerX;
      double renderZ = playerZ;
      double mapDimensionScale = this.mc.field_1687.method_8597().comp_646();
      double playerDimDiv = 1.0;
      if (useWorldMap) {
         double playerCoordinateScale = mapDimensionScale;
         mapDimensionScale = this.modMain.getSupportMods().worldmapSupport.getMapDimensionScale();
         if (mapDimensionScale == 0.0) {
            mapDimensionScale = this.lastMapDimensionScale;
         }

         playerDimDiv = mapDimensionScale / playerCoordinateScale;
         renderX = playerX / playerDimDiv;
         renderZ = playerZ / playerDimDiv;
         this.lastMapDimensionScale = mapDimensionScale;
      }

      this.lastPlayerDimDiv = playerDimDiv;
      class_243 renderPos = new class_243(renderX, playerY, renderZ);
      matrixStack.method_22903();
      this.renderChunks(
         minimapSession,
         guiGraphics,
         minimap,
         renderPos,
         playerDimDiv,
         mapDimensionScale,
         mapSize,
         bufferSize,
         sizeFix,
         partial,
         lightLevel,
         useWorldMap,
         lockedNorth,
         shape,
         ps,
         pc,
         cave,
         circleShape,
         settings,
         cvc
      );
      if (this.minimap.usingFBO()) {
         sizeFix = 1.0F;
      }

      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(class_4535.ONE, class_4534.ONE_MINUS_SRC_ALPHA, class_4535.ZERO, class_4534.ONE);
      matrixStack.method_22905(1.0F / mapScale, 1.0F / mapScale, 1.0F);
      int scaledX = (int)((float)x * mapScale);
      int scaledY = (int)((float)y * mapScale);
      int minimapFrameSize = (int)((float)(mapSize / 2) / sizeFix);
      int circleSides = Math.max(32, (int)Math.ceil(Math.PI * (double)(minimapFrameSize + 8) / 8.0 / 4.0) * 4);
      double circleSeamAngle = -Math.PI / 4;
      int circleSeamWidth = 32;
      int circleFrameThickness = 4;
      double circleStartAngle = 0.0;
      if (!circleShape) {
         this.getHelper()
            .drawMyTexturedModalRect(
               matrixStack,
               (float)((int)((float)(scaledX + 9) / sizeFix)),
               (float)((int)((float)(scaledY + 9) / sizeFix)),
               0,
               256 - minimapFrameSize,
               (float)minimapFrameSize,
               (float)minimapFrameSize,
               (float)minimapFrameSize,
               256.0F
            );
      } else {
         float outerRadius = (float)(mapSize / 4 + circleFrameThickness);
         circleStartAngle = circleSeamAngle - (double)((float)(circleSeamWidth / 2) / outerRadius);
         this.getHelper()
            .drawTexturedElipseInsideRectangle(
               matrixStack,
               circleStartAngle,
               circleSides,
               (float)((int)((float)(scaledX + 9) / sizeFix)),
               (float)((int)((float)(scaledY + 9) / sizeFix)),
               0,
               256 - minimapFrameSize,
               (float)minimapFrameSize,
               256.0F
            );
      }

      RenderSystem.defaultBlendFunc();
      if (!this.minimap.usingFBO()) {
         matrixStack.method_22905(1.0F / sizeFix, 1.0F / sizeFix, 1.0F);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }

      int frameType = settings.minimapFrame;
      boolean renderFrame = frameType < ModSettings.FRAME_OPTIONS.length - 1;
      if (frameType > 0) {
         int frameColor = ModSettings.COLORS[settings.minimapFrameColor];
         RenderSystem.setShaderColor(
            (float)(frameColor >> 16 & 0xFF) / 255.0F, (float)(frameColor >> 8 & 0xFF) / 255.0F, (float)(frameColor & 0xFF) / 255.0F, 1.0F
         );
      }

      MinimapRendererHelper helper = this.getHelper();
      if (renderFrame) {
         RenderSystem.setShaderTexture(0, TextureLocations.MINIMAP_FRAME_TEXTURES);
      }

      if (renderFrame && !circleShape) {
         int rightCornerStartX = scaledX + 9 + mapSize / 2 + 4 - 16;
         int bottomCornerStartY = scaledY + 9 + mapSize / 2 + 4 - 16;
         RenderSystem.setShader(class_757::method_34542);
         class_289 tessellator = class_289.method_1348();
         class_287 vertexBuffer = tessellator.method_1349();
         vertexBuffer.method_1328(class_5596.field_27382, class_290.field_1585);
         Matrix4f matrix = matrixStack.method_23760().method_23761();
         int cornerTextureX = frameType == 0 ? 192 : (frameType == 1 ? 208 : 224);
         helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, (float)(scaledX + 9 - 4), (float)(scaledY + 9 - 4), cornerTextureX, 97, 16, 16);
         helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, (float)rightCornerStartX, (float)(scaledY + 9 - 4), cornerTextureX, 113, 16, 16);
         helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, (float)(scaledX + 9 - 4), (float)bottomCornerStartY, cornerTextureX, 129, 16, 16);
         helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, (float)rightCornerStartX, (float)bottomCornerStartY, cornerTextureX, 145, 16, 16);
         int horLineStartX = scaledX + 9 - 4 + 16;
         int horLineWidth = rightCornerStartX - horLineStartX;
         int horPieceTextureY = frameType == 0 ? 0 : (frameType == 1 ? 32 : 64);
         int horPieceWidth = 226;
         int horLineLength = (int)Math.ceil((double)horLineWidth / (double)horPieceWidth);

         for (int i = 0; i < horLineLength; i++) {
            int pieceX = scaledX + 9 - 4 + 16 + i * horPieceWidth;
            int pieceW = horPieceWidth;
            if (i == horLineLength - 1 && pieceX + horPieceWidth > rightCornerStartX) {
               pieceW = rightCornerStartX - pieceX;
            }

            helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, (float)pieceX, (float)(scaledY + 9 - 4), 0, horPieceTextureY, pieceW, 16);
            helper.addTexturedRectToExistingBuffer(
               matrix, vertexBuffer, (float)pieceX, (float)(scaledY + 9 + mapSize / 2 - 12), 0, horPieceTextureY + 16, pieceW, 16
            );
         }

         int verLineStartY = scaledY + 9 - 4 + 16;
         int verLineHeight = bottomCornerStartY - verLineStartY;
         int verPieceTextureX = frameType == 0 ? 0 : (frameType == 1 ? 64 : 128);
         int verPieceHeight = 113;
         int vertLineLength = (int)Math.ceil((double)verLineHeight / (double)verPieceHeight);

         for (int i = 0; i < vertLineLength; i++) {
            int pieceY = scaledY + 9 - 4 + 16 + i * verPieceHeight;
            int pieceU = verPieceTextureX + 32 * (i & 1);
            int pieceH = verPieceHeight;
            if (i == vertLineLength - 1 && pieceY + verPieceHeight > bottomCornerStartY) {
               pieceH = bottomCornerStartY - pieceY;
            }

            helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, (float)(scaledX + 9 - 4), (float)pieceY, pieceU, 97, 16, pieceH);
            helper.addTexturedRectToExistingBuffer(matrix, vertexBuffer, (float)(scaledX + 9 + mapSize / 2 - 12), (float)pieceY, pieceU + 16, 97, 16, pieceH);
         }

         tessellator.method_1350();
      } else if (renderFrame) {
         int frameTextureY = frameType == 0 ? 210 : (frameType == 1 ? 214 : 218);
         double shadeStartAngle = (Math.PI / 4) - circleStartAngle;
         int shadeStartIndex = (int)(shadeStartAngle / 2.0 / Math.PI * (double)circleSides);
         int circleLeftX = scaledX + 9;
         int circleTopY = scaledY + 9;
         int innerCircleDiameter = mapSize / 2;
         helper.drawTexturedElipseInsideRectangleFrame(
            matrixStack,
            false,
            false,
            circleStartAngle,
            0,
            shadeStartIndex,
            circleSides,
            (float)circleFrameThickness,
            (float)circleLeftX,
            (float)circleTopY,
            0,
            frameTextureY,
            (float)innerCircleDiameter,
            73.0F,
            (float)circleFrameThickness,
            circleSeamWidth,
            256.0F
         );
         helper.drawTexturedElipseInsideRectangleFrame(
            matrixStack,
            true,
            false,
            circleStartAngle,
            shadeStartIndex,
            shadeStartIndex + circleSides / 4,
            circleSides,
            (float)circleFrameThickness,
            (float)circleLeftX,
            (float)circleTopY,
            138,
            frameTextureY,
            (float)innerCircleDiameter,
            68.0F,
            (float)circleFrameThickness,
            20,
            256.0F
         );
         helper.drawTexturedElipseInsideRectangleFrame(
            matrixStack,
            true,
            true,
            circleStartAngle,
            shadeStartIndex + circleSides / 4,
            shadeStartIndex + circleSides / 2,
            circleSides,
            (float)circleFrameThickness,
            (float)circleLeftX,
            (float)circleTopY,
            138,
            frameTextureY,
            (float)innerCircleDiameter,
            68.0F,
            (float)circleFrameThickness,
            20,
            256.0F
         );
         helper.drawTexturedElipseInsideRectangleFrame(
            matrixStack,
            false,
            false,
            circleStartAngle,
            shadeStartIndex + circleSides / 2,
            circleSides,
            circleSides,
            (float)circleFrameThickness,
            (float)circleLeftX,
            (float)circleTopY,
            0,
            frameTextureY,
            (float)innerCircleDiameter,
            73.0F,
            (float)circleFrameThickness,
            circleSeamWidth,
            256.0F
         );
      }

      RenderSystem.setShaderTexture(0, TextureLocations.GUI_TEXTURES);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      matrixStack.method_22903();
      matrixStack.method_46416((float)(scaledX + 9), (float)(scaledY + 9), 0.0F);
      matrixStack.method_22905(1.0F / minimapScale, 1.0F / minimapScale, 1.0F);
      int halfFrame = (int)((float)mapSize * minimapScale / 2.0F / 2.0F);
      matrixStack.method_46416((float)halfFrame, (float)halfFrame, 0.0F);
      int specW = halfFrame + (int)(3.0F * minimapScale);
      boolean safeMode = this instanceof MinimapSafeModeRenderer;
      class_4598 renderTypeBuffers = this.modMain.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers = minimapSession.getMultiTextureRenderTypeRenderers();
      double scaledZoom = this.zoom * (double)minimapScale / 2.0;
      if (!this.modMain.getSettings().compassOverEverything) {
         this.renderCompass(matrixStack, settings, renderTypeBuffers, specW, specW, halfFrame, ps, pc, circleShape, minimapScale);
      }

      this.minimap.getOverMapRendererHandler().prepareRender(specW, specW, halfFrame, halfFrame, circleShape, minimapScale);
      this.minimap
         .getOverMapRendererHandler()
         .render(
            guiGraphics,
            this.mc.method_1560(),
            this.mc.field_1724,
            renderPos,
            playerDimDiv,
            ps,
            pc,
            scaledZoom,
            cave,
            partial,
            null,
            multiTextureRenderTypeRenderers
         );
      if (this.modMain.getSettings().compassOverEverything) {
         this.renderCompass(matrixStack, settings, renderTypeBuffers, specW, specW, halfFrame, ps, pc, circleShape, minimapScale);
      }

      renderTypeBuffers.method_22993();
      matrixStack.method_22909();
      int depthClearerX = scaledX - 25;
      int depthClearerY = scaledY - 25;
      int depthClearerW = 18 + mapSize / 2 + 50;
      guiGraphics.method_51739(
         CustomRenderTypes.DEPTH_CLEAR, depthClearerX, depthClearerY, depthClearerX + depthClearerW, depthClearerY + depthClearerW, -16777216
      );
      RenderSystem.enableBlend();
      boolean crosshairDisplayed = settings.mainEntityAs == 0 && !lockedNorth;
      if (crosshairDisplayed) {
         matrixStack.method_22903();
         matrixStack.method_46416((float)(scaledX + 9), (float)(scaledY + 9), 0.0F);
         matrixStack.method_22905(0.5F, 0.5F, 1.0F);
         matrixStack.method_46416((float)(mapSize / 2), (float)(mapSize / 2), 0.0F);
         RenderSystem.blendFuncSeparate(775, 0, 1, 0);
         this.getHelper().drawMyColoredRect(matrixStack, -5.0F, -1.0F, 5.0F, 1.0F);
         this.getHelper().drawMyColoredRect(matrixStack, -1.0F, 3.0F, 1.0F, 5.0F);
         this.getHelper().drawMyColoredRect(matrixStack, -1.0F, -5.0F, 1.0F, -3.0F);
         RenderSystem.blendFunc(770, 771);
         MinimapRadar minimapRadar = minimap.getEntityRadar();
         EntityRadarCategory mainEntityCategory = minimapRadar.getEntityCategoryManager()
            .getRuleResolver()
            .resolve(minimapRadar.getEntityCategoryManager().getRootCategory(), this.mc.method_1560(), this.mc.field_1724);
         if (mainEntityCategory == null) {
            mainEntityCategory = minimapRadar.getEntityCategoryManager().getRootCategory();
         }

         int crosshairColor = minimapRadar.getEntityColour(
            this.mc.field_1724,
            this.mc.method_1560(),
            0.0F,
            false,
            mainEntityCategory,
            100,
            100,
            false,
            mainEntityCategory.getSettingValue(EntityRadarCategorySettings.COLOR).intValue()
         );
         RenderSystem.setShaderColor(
            (float)(crosshairColor >> 16 & 0xFF) / 255.0F, (float)(crosshairColor >> 8 & 0xFF) / 255.0F, (float)(crosshairColor & 0xFF) / 255.0F, 1.0F
         );
         this.getHelper().drawMyColoredRect(matrixStack, 1.0F, -1.0F, 3.0F, 1.0F);
         this.getHelper().drawMyColoredRect(matrixStack, -3.0F, -1.0F, -1.0F, 1.0F);
         this.getHelper().drawMyColoredRect(matrixStack, -1.0F, 1.0F, 1.0F, 3.0F);
         this.getHelper().drawMyColoredRect(matrixStack, -1.0F, -3.0F, 1.0F, -1.0F);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.enableBlend();
         matrixStack.method_22909();
      }

      double centerX = (double)(2 * scaledX + 18 + mapSize / 2);
      double centerY = (double)(2 * scaledY + 18 + mapSize / 2);
      matrixStack.method_22903();
      matrixStack.method_22905(0.5F, 0.5F, 1.0F);
      matrixStack.method_22904(centerX, centerY, 0.0);
      this.mc.method_1531().method_22813(TextureLocations.GUI_TEXTURES);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10241, 9729);
      class_1297 mainEntity = this.mc.method_1560();
      if (!safeMode && !crosshairDisplayed) {
         MinimapRadar radar = minimap.getEntityRadar();
         this.minimap
            .getMinimapFBORenderer()
            .renderMainEntityDot(
               guiGraphics,
               minimap,
               this.mc.field_1724,
               mainEntity,
               ps,
               pc,
               radar.getEntityX(mainEntity, partial),
               radar.getEntityZ(mainEntity, partial),
               partial,
               radar,
               lockedNorth,
               settings.getDotsStyle(),
               true,
               settings.debugEntityIcons,
               cave,
               settings.getDotNameScale(),
               settings,
               cvc.getBetterPVPRenderTypeBuffers(),
               2.0F
            );
      }

      RenderSystem.setShaderTexture(0, TextureLocations.GUI_TEXTURES);
      RenderSystem.enableBlend();
      if (lockedNorth || settings.mainEntityAs == 2) {
         float arrowAngle = lockedNorth ? mainEntity.method_5705(partial) : 180.0F;
         float arrowOpacity = (float)settings.playerArrowOpacity / 100.0F;
         if (arrowOpacity == 1.0F) {
            this.drawArrow(matrixStack, arrowAngle, 0.0, 1.0, 0.0F, 0.0F, 0.0F, 0.5F, settings);
         }

         float g;
         float b;
         float a;
         float r;
         if (settings.arrowColour != -1) {
            float[] c = ModSettings.arrowColours[settings.arrowColour];
            r = c[0];
            g = c[1];
            b = c[2];
            a = c[3];
         } else {
            int rgb = minimap.getEntityRadar().getTeamColour((class_1297)(this.mc.field_1724 == null ? mainEntity : this.mc.field_1724));
            if (rgb != -1) {
               r = (float)(rgb >> 16 & 0xFF) / 255.0F;
               g = (float)(rgb >> 8 & 0xFF) / 255.0F;
               b = (float)(rgb & 0xFF) / 255.0F;
               a = 1.0F;
            } else {
               float[] c = ModSettings.arrowColours[0];
               r = c[0];
               g = c[1];
               b = c[2];
               a = c[3];
            }
         }

         a *= arrowOpacity;
         this.drawArrow(matrixStack, arrowAngle, 0.0, 0.0, r, g, b, a, settings);
      }

      matrixStack.method_22909();
      this.mc.method_1531().method_22813(TextureLocations.GUI_TEXTURES);
      GL11.glTexParameteri(3553, 10240, 9728);
      GL11.glTexParameteri(3553, 10241, 9728);
      int playerBlockX = OptimizedMath.myFloor(mainEntity.method_23317());
      int playerBlockY = OptimizedMath.myFloor(mainEntity.method_23318());
      int playerBlockZ = OptimizedMath.myFloor(mainEntity.method_23321());
      class_2338 pos = this.mutableBlockPos.method_10103(playerBlockX, playerBlockY, playerBlockZ);
      this.minimap
         .getInfoDisplays()
         .getRenderer()
         .render(guiGraphics, minimapSession, this.minimap, height, size, pos, scaledX, scaledY, mapScale, renderTypeBuffers);
      matrixStack.method_22909();
      class_308.method_24211();
   }

   private void renderCompass(
      class_4587 matrixStack,
      ModSettings settings,
      class_4598 renderTypeBuffers,
      int specW,
      int specH,
      int halfFrame,
      double ps,
      double pc,
      boolean circleShape,
      float minimapScale
   ) {
      class_4588 nameBgBuilder = renderTypeBuffers.getBuffer(CustomRenderTypes.RADAR_NAME_BGS);
      int compassScale = settings.getCompassScale();
      if (compassScale <= 0) {
         compassScale = settings.compassLocation == 1 ? (int)Math.ceil((double)(minimapScale / 2.0F)) : (int)minimapScale;
      }

      if (settings.compassLocation == 1) {
         if (class_310.method_1551().method_1573()) {
            compassScale *= 2;
         }

         halfFrame = (int)((float)halfFrame - 7.0F * minimapScale / 2.0F);
         this.compassRenderer
            .drawCompass(
               matrixStack,
               halfFrame - 3 * compassScale,
               halfFrame - 3 * compassScale,
               ps,
               pc,
               1.0,
               circleShape,
               (float)compassScale,
               true,
               renderTypeBuffers,
               nameBgBuilder
            );
      } else if (settings.compassLocation == 2) {
         this.compassRenderer.drawCompass(matrixStack, specW, specH, ps, pc, this.zoom, circleShape, (float)compassScale, false, renderTypeBuffers, null);
      }
   }

   private void drawArrow(class_4587 matrixStack, float angle, double arrowX, double arrowY, float r, float g, float b, float a, ModSettings settings) {
      matrixStack.method_22903();
      matrixStack.method_22904(arrowX, arrowY, 0.0);
      OptimizedMath.rotatePose(matrixStack, angle, OptimizedMath.ZP);
      matrixStack.method_22905((float)(0.5 * settings.arrowScale), (float)(0.5 * settings.arrowScale), 1.0F);
      int offsetY = -6;
      int h = 28;
      int ty = 0;
      matrixStack.method_46416(-13.0F, (float)offsetY, 0.0F);
      RenderSystem.setShaderColor(r, g, b, a);
      RenderSystem.blendFuncSeparate(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA, class_4535.ONE, class_4534.ONE_MINUS_SRC_ALPHA);
      GuiHelper.blit(matrixStack, 0, 0, 49.0F, (float)ty, 26, h);
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      matrixStack.method_22909();
   }

   public double getZoom() {
      return this.zoom;
   }

   public void setZoom(double zoom) {
      this.zoom = zoom;
   }

   public float getSunBrightness(MinimapProcessor minimap, boolean lighting) {
      class_638 world = this.mc.field_1687;
      float sunBrightness = (world.method_23783(1.0F) - 0.2F) / 0.8F;
      float ambient = world.method_8597().comp_656() * 24.0F / 15.0F;
      if (ambient > 1.0F) {
         ambient = 1.0F;
      }

      return ambient + (1.0F - ambient) * class_3532.method_15363(sunBrightness, 0.0F, 1.0F);
   }

   public MinimapRendererHelper getHelper() {
      return this.helper;
   }

   public double getLastPlayerDimDiv() {
      return this.lastPlayerDimDiv;
   }
}
