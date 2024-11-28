package xaero.common.minimap.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_308;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_583;
import net.minecraft.class_630;
import net.minecraft.class_4587.class_4665;
import net.minecraft.class_4597.class_4598;
import org.lwjgl.opengl.GL11;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.effect.Effects;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.CustomVertexConsumers;
import xaero.common.graphics.ImprovedFramebuffer;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.graphics.shader.MinimapShaders;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.element.render.map.MinimapElementMapRendererHandler;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.minimap.region.MinimapChunk;
import xaero.common.minimap.render.radar.EntityIconManager;
import xaero.common.minimap.render.radar.EntityIconPrerenderer;
import xaero.common.minimap.render.radar.element.RadarRenderer;
import xaero.common.minimap.waypoints.render.CompassRenderer;
import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
import xaero.common.misc.Misc;
import xaero.common.misc.OptimizedMath;
import xaero.common.settings.ModSettings;
import xaero.hud.compat.mods.ImmediatelyFastHelper;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.MinimapLogs;

public class MinimapFBORenderer extends MinimapRenderer {
   private ImprovedFramebuffer scalingFramebuffer;
   private ImprovedFramebuffer rotationFramebuffer;
   private MinimapElementMapRendererHandler minimapElementMapRendererHandler;
   private RadarRenderer radarRenderer;
   private EntityIconManager entityIconManager;
   private boolean triedFBO;
   private boolean loadedFBO;

   public MinimapFBORenderer(IXaeroMinimap modMain, class_310 mc, WaypointsGuiRenderer waypointsGuiRenderer, Minimap minimap, CompassRenderer compassRenderer) {
      super(modMain, mc, waypointsGuiRenderer, minimap, compassRenderer);
   }

   public void loadFrameBuffer(MinimapProcessor minimapProcessor) {
      if (!minimapProcessor.canUseFrameBuffer()) {
         MinimapLogs.LOGGER.info("FBO mode not supported! Using minimap safe mode.");
      } else {
         this.scalingFramebuffer = new ImprovedFramebuffer(512, 512, false);
         this.rotationFramebuffer = new ImprovedFramebuffer(512, 512, true);
         this.rotationFramebuffer.method_1232(9729);
         this.entityIconManager = new EntityIconManager(this.modMain, new EntityIconPrerenderer(this.modMain));
         this.loadedFBO = this.scalingFramebuffer.field_1476 != -1 && this.rotationFramebuffer.field_1476 != -1;
         this.minimapElementMapRendererHandler = MinimapElementMapRendererHandler.Builder.begin().build();
         this.radarRenderer = RadarRenderer.Builder.begin()
            .setModMain(this.modMain)
            .setEntityIconManager(this.entityIconManager)
            .setMinimap(this.minimap)
            .build();
         this.minimapElementMapRendererHandler.add(this.radarRenderer);
         this.minimap.getOverMapRendererHandler().add(this.radarRenderer);
         if (this.modMain.getSupportMods().worldmap()) {
            this.modMain.getSupportMods().worldmapSupport.createRadarRenderWrapper(this.radarRenderer);
         }
      }

      this.triedFBO = true;
   }

   @Override
   protected void renderChunks(
      XaeroMinimapSession minimapSession,
      class_332 guiGraphics,
      MinimapProcessor minimap,
      double playerX,
      double playerZ,
      double playerDimDiv,
      double mapDimensionScale,
      int mapSize,
      int bufferSize,
      float sizeFix,
      float partial,
      int lightLevel,
      boolean useWorldMap,
      boolean lockedNorth,
      int shape,
      double ps,
      double pc,
      boolean cave,
      boolean circle,
      ModSettings settings,
      CustomVertexConsumers cvc
   ) {
      synchronized (minimap.getMinimapWriter()) {
         this.renderChunksToFBO(
            minimapSession,
            guiGraphics,
            minimap,
            this.mc.field_1724,
            this.mc.method_1560(),
            playerX,
            playerZ,
            playerDimDiv,
            mapDimensionScale,
            bufferSize,
            mapSize,
            sizeFix,
            partial,
            lightLevel,
            true,
            useWorldMap,
            lockedNorth,
            shape,
            ps,
            pc,
            cave,
            circle,
            cvc
         );
      }

      this.scalingFramebuffer.bindDefaultFramebuffer(class_310.method_1551());
      GlStateManager._viewport(0, 0, class_310.method_1551().method_22683().method_4489(), class_310.method_1551().method_22683().method_4506());
      this.rotationFramebuffer.method_35610();
   }

   public void renderChunksToFBO(
      XaeroMinimapSession minimapSession,
      class_332 guiGraphics,
      MinimapProcessor minimap,
      class_1657 player,
      class_1297 renderEntity,
      double playerX,
      double playerZ,
      double playerDimDiv,
      double mapDimensionScale,
      int bufferSize,
      int viewW,
      float sizeFix,
      float partial,
      int level,
      boolean retryIfError,
      boolean useWorldMap,
      boolean lockedNorth,
      int shape,
      double ps,
      double pc,
      boolean cave,
      boolean circle,
      CustomVertexConsumers cvc
   ) {
      class_4587 matrixStack = guiGraphics.method_51448();
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers = minimapSession.getMultiTextureRenderTypeRenderers();
      double maxVisibleLength = !lockedNorth && shape != 1 ? (double)viewW * Math.sqrt(2.0) : (double)viewW;
      double halfMaxVisibleLength = maxVisibleLength / 2.0;
      double radiusBlocks = maxVisibleLength / 2.0 / this.zoom;
      int xFloored = OptimizedMath.myFloor(playerX);
      int zFloored = OptimizedMath.myFloor(playerZ);
      int playerChunkX = xFloored >> 6;
      int playerChunkZ = zFloored >> 6;
      int offsetX = xFloored & 63;
      int offsetZ = zFloored & 63;
      boolean zooming = (double)((int)this.zoom) != this.zoom;
      ImmediatelyFastHelper.triggerBatchingBuffersFlush(guiGraphics);
      this.scalingFramebuffer.method_1235(true);
      GL11.glClear(16640);
      class_308.method_24210();
      long before = System.currentTimeMillis();
      GlStateManager._clear(256, class_310.field_1703);
      this.helper.defaultOrtho(this.scalingFramebuffer, false);
      class_4587 shaderMatrixStack = RenderSystem.getModelViewStack();
      shaderMatrixStack.method_22903();
      shaderMatrixStack.method_34426();
      before = System.currentTimeMillis();
      double xInsidePixel = playerX - (double)xFloored;
      if (xInsidePixel < 0.0) {
         xInsidePixel++;
      }

      double zInsidePixel = playerZ - (double)zFloored;
      if (zInsidePixel < 0.0) {
         zInsidePixel++;
      }

      float halfWView = (float)viewW / 2.0F;
      float angle = (float)(90.0 - this.getRenderAngle(lockedNorth));
      RenderSystem.enableBlend();
      shaderMatrixStack.method_46416(256.0F, 256.0F, -2000.0F);
      shaderMatrixStack.method_22905((float)this.zoom, (float)this.zoom, 1.0F);
      RenderSystem.applyModelViewMatrix();
      guiGraphics.method_25294(-256, -256, 256, 256, -16777216);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      class_4598 renderTypeBuffers = cvc.getBetterPVPRenderTypeBuffers();
      class_4588 overlayBufferBuilder = renderTypeBuffers.getBuffer(CustomRenderTypes.MAP_CHUNK_OVERLAY);
      float chunkGridAlphaMultiplier = 1.0F;
      int minX = playerChunkX + (int)Math.floor(((double)offsetX - radiusBlocks) / 64.0);
      int minZ = playerChunkZ + (int)Math.floor(((double)offsetZ - radiusBlocks) / 64.0);
      int maxX = playerChunkX + (int)Math.floor(((double)(offsetX + 1) + radiusBlocks) / 64.0);
      int maxZ = playerChunkZ + (int)Math.floor(((double)(offsetZ + 1) + radiusBlocks) / 64.0);
      if (!cave || !Misc.hasEffect(this.mc.field_1724, Effects.NO_CAVE_MAPS) && !Misc.hasEffect(this.mc.field_1724, Effects.NO_CAVE_MAPS_HARMFUL)) {
         if (useWorldMap) {
            chunkGridAlphaMultiplier = this.modMain.getSupportMods().worldmapSupport.getMinimapBrightness();
            this.modMain
               .getSupportMods()
               .worldmapSupport
               .drawMinimap(
                  minimapSession,
                  matrixStack,
                  this.getHelper(),
                  xFloored,
                  zFloored,
                  minX,
                  minZ,
                  maxX,
                  maxZ,
                  zooming,
                  this.zoom,
                  mapDimensionScale,
                  overlayBufferBuilder,
                  multiTextureRenderTypeRenderers
               );
         } else if (minimap.getMinimapWriter().getLoadedBlocks() != null && level >= 0) {
            int loadedLevels = minimap.getMinimapWriter().getLoadedLevels();
            chunkGridAlphaMultiplier = loadedLevels <= 1 ? 1.0F : 0.375F + 0.625F * (1.0F - (float)level / (float)(loadedLevels - 1));
            int loadedMapChunkX = minimap.getMinimapWriter().getLoadedMapChunkX();
            int loadedMapChunkZ = minimap.getMinimapWriter().getLoadedMapChunkZ();
            int loadedWidth = minimap.getMinimapWriter().getLoadedBlocks().length;
            boolean slimeChunks = this.modMain.getSettings().getSlimeChunks(minimapSession.getWaypointsManager());
            minX = Math.max(minX, loadedMapChunkX);
            minZ = Math.max(minZ, loadedMapChunkZ);
            maxX = Math.min(maxX, loadedMapChunkX + loadedWidth - 1);
            maxZ = Math.min(maxZ, loadedMapChunkZ + loadedWidth - 1);
            MultiTextureRenderTypeRenderer multiTextureRenderTypeRenderer = multiTextureRenderTypeRenderers.getRenderer(
               t -> RenderSystem.setShaderTexture(0, t), MultiTextureRenderTypeRendererProvider::defaultTextureBind, CustomRenderTypes.GUI_BILINEAR
            );
            MinimapRendererHelper helper = this.getHelper();

            for (int X = minX; X <= maxX; X++) {
               int canvasX = X - minimap.getMinimapWriter().getLoadedMapChunkX();

               for (int Z = minZ; Z <= maxZ; Z++) {
                  int canvasZ = Z - minimap.getMinimapWriter().getLoadedMapChunkZ();
                  MinimapChunk mchunk = minimap.getMinimapWriter().getLoadedBlocks()[canvasX][canvasZ];
                  if (mchunk != null) {
                     int texture = mchunk.bindTexture(level);
                     if (mchunk.isHasSomething() && level < mchunk.getLevelsBuffered() && texture != 0) {
                        if (!zooming) {
                           GL11.glTexParameteri(3553, 10240, 9728);
                        } else {
                           GL11.glTexParameteri(3553, 10240, 9729);
                        }

                        int drawX = (X - playerChunkX) * 64 - offsetX;
                        int drawZ = (Z - playerChunkZ) * 64 - offsetZ;
                        helper.prepareMyTexturedColoredModalRect(
                           matrixStack.method_23760().method_23761(),
                           (float)drawX,
                           (float)drawZ,
                           0,
                           64,
                           64.0F,
                           64.0F,
                           -64.0F,
                           64.0F,
                           texture,
                           1.0F,
                           1.0F,
                           1.0F,
                           1.0F,
                           multiTextureRenderTypeRenderer
                        );
                        if (slimeChunks) {
                           for (int t = 0; t < 16; t++) {
                              if (mchunk.getTile(t % 4, t / 4) != null && mchunk.getTile(t % 4, t / 4).isSlimeChunk()) {
                                 int slimeDrawX = drawX + 16 * (t % 4);
                                 int slimeDrawZ = drawZ + 16 * (t / 4);
                                 helper.addColoredRectToExistingBuffer(
                                    matrixStack.method_23760().method_23761(), overlayBufferBuilder, (float)slimeDrawX, (float)slimeDrawZ, 16, 16, -2142047936
                                 );
                              }
                           }
                        }
                     }
                  }
               }
            }

            multiTextureRenderTypeRenderers.draw(multiTextureRenderTypeRenderer);
         }
      }

      if (this.modMain.getSettings().chunkGrid > -1) {
         class_4588 lineBufferBuilder = renderTypeBuffers.getBuffer(CustomRenderTypes.MAP_LINES);
         int grid = ModSettings.COLORS[this.modMain.getSettings().chunkGrid];
         int r = grid >> 16 & 0xFF;
         int g = grid >> 8 & 0xFF;
         int b = grid & 0xFF;
         MinimapShaders.FRAMEBUFFER_LINES.setFrameSize((float)this.scalingFramebuffer.field_1480, (float)this.scalingFramebuffer.field_1477);
         float red = (float)r / 255.0F;
         float green = (float)g / 255.0F;
         float blue = (float)b / 255.0F;
         float alpha = 0.8F;
         red *= chunkGridAlphaMultiplier;
         green *= chunkGridAlphaMultiplier;
         blue *= chunkGridAlphaMultiplier;
         RenderSystem.lineWidth((float)this.modMain.getSettings().chunkGridLineWidth);
         int bias = 1;
         class_4665 matrices = matrixStack.method_23760();

         for (int X = minX; X <= maxX; X++) {
            int drawX = (X - playerChunkX + 1) * 64 - offsetX;

            for (int i = 0; i < 4; i++) {
               float lineX = (float)drawX + (float)(-16 * i);
               this.helper
                  .addColoredLineToExistingBuffer(
                     matrices,
                     lineBufferBuilder,
                     lineX,
                     -((float)halfMaxVisibleLength),
                     lineX,
                     (float)halfMaxVisibleLength + (float)bias,
                     red,
                     green,
                     blue,
                     alpha
                  );
            }
         }

         for (int Zx = minZ; Zx <= maxZ; Zx++) {
            int drawZ = (Zx - playerChunkZ + 1) * 64 - offsetZ;

            for (int i = 0; i < 4; i++) {
               float lineZ = (float)drawZ + (float)((double)(-16 * i) - 1.0 / this.zoom);
               this.helper
                  .addColoredLineToExistingBuffer(
                     matrices,
                     lineBufferBuilder,
                     -((float)halfMaxVisibleLength),
                     lineZ,
                     (float)halfMaxVisibleLength + (float)bias,
                     lineZ,
                     red,
                     green,
                     blue,
                     alpha
                  );
            }
         }
      }

      renderTypeBuffers.method_22993();
      this.scalingFramebuffer.method_1240();
      this.rotationFramebuffer.method_1235(false);
      GL11.glClear(16640);
      this.scalingFramebuffer.method_35610();
      shaderMatrixStack.method_34426();
      if (this.modMain.getSettings().getAntiAliasing()) {
         GL11.glTexParameteri(3553, 10240, 9729);
         GL11.glTexParameteri(3553, 10241, 9729);
      } else {
         GL11.glTexParameteri(3553, 10240, 9728);
         GL11.glTexParameteri(3553, 10241, 9728);
      }

      shaderMatrixStack.method_46416(halfWView, halfWView, -2980.0F);
      shaderMatrixStack.method_22903();
      if (!lockedNorth) {
         OptimizedMath.rotatePose(shaderMatrixStack, -angle, OptimizedMath.ZP);
      }

      shaderMatrixStack.method_22904(-xInsidePixel * this.zoom, -zInsidePixel * this.zoom, 0.0);
      RenderSystem.applyModelViewMatrix();
      RenderSystem.disableBlend();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float)(this.modMain.getSettings().minimapOpacity / 100.0));
      this.helper.drawMyTexturedModalRect(matrixStack, -256.0F, -256.0F, 0, 0, 512.0F, 512.0F, 512.0F, 512.0F);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      shaderMatrixStack.method_22909();
      RenderSystem.applyModelViewMatrix();
      before = System.currentTimeMillis();
      RenderSystem.disableBlend();
      RenderSystem.blendFuncSeparate(770, 771, 1, 1);
      GlStateManager._depthFunc(519);
      GlStateManager._depthFunc(515);
      GlStateManager._depthMask(false);
      GlStateManager._depthMask(true);
      GL11.glBindTexture(3553, 0);
      GlStateManager._bindTexture(0);
      GlStateManager._enableBlend();
      GlStateManager._blendFuncSeparate(770, 771, 1, 771);
      matrixStack.method_22903();
      this.minimapElementMapRendererHandler
         .render(
            guiGraphics,
            renderEntity,
            player,
            playerX,
            renderEntity.method_23318(),
            playerZ,
            playerDimDiv,
            ps,
            pc,
            this.zoom,
            cave,
            partial,
            this.rotationFramebuffer,
            this.modMain,
            this.helper,
            renderTypeBuffers,
            this.mc.field_1772,
            multiTextureRenderTypeRenderers,
            halfWView
         );
      matrixStack.method_22909();
      renderTypeBuffers.method_22993();
      ImmediatelyFastHelper.triggerBatchingBuffersFlush(guiGraphics);
      this.rotationFramebuffer.method_1240();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
      Misc.minecraftOrtho(this.mc, false);
      shaderMatrixStack.method_22909();
      RenderSystem.applyModelViewMatrix();
   }

   public void deleteFramebuffers() {
      this.scalingFramebuffer.method_1238();
      this.rotationFramebuffer.method_1238();
      if (this.entityIconManager != null) {
         this.entityIconManager.reset();
      }
   }

   public boolean isLoadedFBO() {
      return this.loadedFBO;
   }

   public void setLoadedFBO(boolean loadedFBO) {
      this.loadedFBO = loadedFBO;
   }

   public boolean isTriedFBO() {
      return this.triedFBO;
   }

   public void resetEntityIcons() {
      if (this.entityIconManager != null) {
         this.entityIconManager.reset();
      }
   }

   public void resetEntityIconsResources() {
      if (this.entityIconManager != null) {
         this.entityIconManager.resetResources();
      }
   }

   public void onEntityIconsModelRenderDetection(class_583<?> model, class_4588 vertexConsumer, float red, float green, float blue, float alpha) {
      this.entityIconManager.onModelRenderDetection(model, vertexConsumer, red, green, blue, alpha);
   }

   public void onEntityIconsModelPartRenderDetection(class_630 modelRenderer, float red, float green, float blue, float alpha) {
      this.entityIconManager.onModelPartRenderDetection(modelRenderer, red, green, blue, alpha);
   }

   public void renderMainEntityDot(
      class_332 guiGraphics,
      MinimapProcessor minimap,
      class_1657 p,
      class_1297 renderEntity,
      double ps,
      double pc,
      double playerX,
      double playerZ,
      float partial,
      MinimapRadar minimapRadar,
      boolean lockedNorth,
      int style,
      boolean smooth,
      boolean debug,
      boolean cave,
      double dotNameScale,
      ModSettings settings,
      class_4598 renderTypeBuffers,
      float minimapScale
   ) {
      EntityRadarCategory mainEntityCategory = minimapRadar.getEntityCategoryManager()
         .getRuleResolver()
         .resolve(minimapRadar.getEntityCategoryManager().getRootCategory(), renderEntity, p);
      if (mainEntityCategory == null) {
         mainEntityCategory = minimapRadar.getEntityCategoryManager().getRootCategory();
      }

      int dotSize = settings.mainDotSize;
      this.radarRenderer
         .renderEntityDotToFBO(
            1,
            false,
            guiGraphics,
            minimap,
            p,
            renderEntity,
            renderEntity,
            partial,
            false,
            false,
            minimapRadar,
            style,
            smooth,
            debug,
            false,
            cave,
            dotNameScale,
            renderTypeBuffers,
            CustomRenderTypes.GUI_BILINEAR,
            renderTypeBuffers.getBuffer(CustomRenderTypes.GUI_BILINEAR),
            null,
            null,
            0,
            false,
            100,
            false,
            100,
            1.0,
            dotSize,
            mainEntityCategory.getSettingValue(EntityRadarCategorySettings.COLOR).intValue(),
            0,
            mainEntityCategory,
            this.getHelper(),
            this.mc.field_1772,
            this.rotationFramebuffer,
            minimapScale
         );
      renderTypeBuffers.method_22993();
   }

   public RadarRenderer getRadarRenderer() {
      return this.radarRenderer;
   }
}
