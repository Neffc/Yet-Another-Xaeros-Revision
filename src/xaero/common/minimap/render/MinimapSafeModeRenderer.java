package xaero.common.minimap.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import xaero.common.IXaeroMinimap;
import xaero.common.core.IXaeroMinimapMinecraftClient;
import xaero.common.effect.Effects;
import xaero.common.graphics.CustomVertexConsumers;
import xaero.common.graphics.MinimapTexture;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.radar.MinimapRadarList;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.minimap.region.MinimapChunk;
import xaero.common.minimap.region.MinimapTile;
import xaero.common.minimap.waypoints.render.CompassRenderer;
import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
import xaero.common.misc.Misc;
import xaero.common.misc.OptimizedMath;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.module.MinimapSession;

public class MinimapSafeModeRenderer extends MinimapRenderer {
   private static final class_2960 mapTextures = new class_2960("xaeromaptexture");
   private byte[] bytes;
   private byte drawYState;
   private final int[] tempColor = new int[3];
   private MinimapTexture mapTexture = new MinimapTexture(mapTextures);

   public MinimapSafeModeRenderer(
      IXaeroMinimap modMain, class_310 mc, WaypointsGuiRenderer waypointsGuiRenderer, Minimap minimap, CompassRenderer compassRenderer
   ) throws IOException {
      super(modMain, mc, waypointsGuiRenderer, minimap, compassRenderer);
   }

   public void updateMapFrameSafeMode(
      MinimapSession session,
      MinimapProcessor minimap,
      class_1657 player,
      class_1297 renderEntity,
      int bufferSize,
      int mapW,
      float partial,
      int level,
      boolean lockedNorth,
      int shape,
      double ps,
      double pc,
      boolean cave,
      ModSettings settings
   ) {
      if (level >= 0) {
         class_1657 p = player;
         long before = System.currentTimeMillis();
         if (minimap.isToResetImage()) {
            this.bytes = new byte[bufferSize * bufferSize * 3];
            minimap.setToResetImage(false);
         }

         int debugFPS = ((IXaeroMinimapMinecraftClient)this.mc).getXaeroMinimap_fps();
         boolean motionBlur = debugFPS >= 35;
         int increaseY = motionBlur ? 2 : 1;
         int mapH = mapW;
         int halfW = mapW / 2;
         int halfH = mapW / 2;
         double halfWZoomed = (double)halfW / this.zoom;
         double halfHZoomed = (double)halfH / this.zoom;
         byte currentState = this.drawYState;
         double playerX = minimap.getEntityRadar().getEntityX(renderEntity, partial);
         double playerZ = minimap.getEntityRadar().getEntityZ(renderEntity, partial);
         boolean terrainMapVisible = !cave
            || !Misc.hasEffect(this.mc.field_1724, Effects.NO_CAVE_MAPS) && !Misc.hasEffect(this.mc.field_1724, Effects.NO_CAVE_MAPS_HARMFUL);

         for (int currentX = 0; currentX < mapW; currentX++) {
            double currentXZoomed = ((double)currentX + 0.5) / this.zoom;
            double offx = currentXZoomed - halfWZoomed;
            double psx = ps * offx;
            double pcx = pc * offx;

            for (int currentY = motionBlur ? currentState : 0; currentY < mapH; currentY += increaseY) {
               double offy = ((double)currentY + 0.5) / this.zoom - halfHZoomed;
               if (terrainMapVisible) {
                  this.getLoadedBlockColor(
                     session,
                     minimap,
                     this.tempColor,
                     OptimizedMath.myFloor(playerX + psx + pc * offy),
                     OptimizedMath.myFloor(playerZ + ps * offy - pcx),
                     level
                  );
               } else {
                  this.tempColor[0] = this.tempColor[1] = this.tempColor[2] = 1;
               }

               this.getHelper().putColor(this.bytes, currentX, bufferSize - 1 - currentY, this.tempColor[0], this.tempColor[1], this.tempColor[2], bufferSize);
            }

            currentState = (byte)(currentState == 1 ? 0 : 1);
         }

         MinimapRadar minimapRadar = minimap.getEntityRadar();
         double maxDistance = minimapRadar.getMaxDistance(minimap, this.modMain.getSettings().minimapShape == 1);
         if (this.modMain.getSettings().getEntityRadar()) {
            Iterator<MinimapRadarList> entityLists = minimapRadar.getRadarListsIterator();

            while (entityLists.hasNext()) {
               MinimapRadarList entityList = entityLists.next();
               EntityRadarCategory entityCategory = entityList.getCategory();
               int heightLimit = entityCategory.getSettingValue(EntityRadarCategorySettings.HEIGHT_LIMIT).intValue();
               boolean heightBasedFade = entityCategory.getSettingValue(EntityRadarCategorySettings.HEIGHT_FADE);
               int colorIndex = entityCategory.getSettingValue(EntityRadarCategorySettings.COLOR).intValue();
               int startFadingAt = entityCategory.getSettingValue(EntityRadarCategorySettings.START_FADING_AT).intValue();
               this.renderEntityListSafeMode(
                  minimap,
                  p,
                  renderEntity,
                  entityList.getEntities().iterator(),
                  pc,
                  ps,
                  mapW,
                  bufferSize,
                  halfW,
                  halfH,
                  playerX,
                  playerZ,
                  partial,
                  cave,
                  entityCategory,
                  heightLimit,
                  heightBasedFade,
                  startFadingAt,
                  colorIndex,
                  maxDistance
               );
            }
         }

         if (this.modMain.getSettings().mainEntityAs == 1) {
            EntityRadarCategory mainEntityCategory = minimapRadar.getEntityCategoryManager()
               .getRuleResolver()
               .resolve(minimapRadar.getEntityCategoryManager().getRootCategory(), renderEntity, p);
            if (mainEntityCategory == null) {
               mainEntityCategory = minimapRadar.getEntityCategoryManager().getRootCategory();
            }

            this.renderEntityDotSafeMode(
               minimap,
               p,
               renderEntity,
               renderEntity,
               pc,
               ps,
               mapW,
               bufferSize,
               halfW,
               halfH,
               playerX,
               playerZ,
               partial,
               cave,
               mainEntityCategory,
               100,
               false,
               100,
               mainEntityCategory.getSettingValue(EntityRadarCategorySettings.COLOR).intValue(),
               maxDistance
            );
         }

         this.drawYState = (byte)(this.drawYState == 1 ? 0 : 1);
         ByteBuffer buffer = this.mapTexture.buffer;
         buffer.clear();
         buffer.put(this.bytes);
         buffer.flip();
      }
   }

   public void renderEntityListSafeMode(
      MinimapProcessor minimap,
      class_1657 p,
      class_1297 renderEntity,
      Iterator<class_1297> iter,
      double pc,
      double ps,
      int mapW,
      int bufferSize,
      int halfW,
      int halfH,
      double playerX,
      double playerZ,
      float partial,
      boolean cave,
      EntityRadarCategory category,
      int heightLimit,
      boolean heightBasedFade,
      int startFadingAt,
      int colorIndex,
      double maxDistance
   ) {
      while (iter.hasNext()) {
         class_1297 e = iter.next();
         if (renderEntity != e
            && !this.renderEntityDotSafeMode(
               minimap,
               p,
               renderEntity,
               e,
               pc,
               ps,
               mapW,
               bufferSize,
               halfW,
               halfH,
               playerX,
               playerZ,
               partial,
               cave,
               category,
               heightLimit,
               heightBasedFade,
               startFadingAt,
               colorIndex,
               maxDistance
            )) {
         }
      }
   }

   public boolean renderEntityDotSafeMode(
      MinimapProcessor minimap,
      class_1657 p,
      class_1297 renderEntity,
      class_1297 e,
      double pc,
      double ps,
      int mapW,
      int bufferSize,
      int halfW,
      int halfH,
      double playerX,
      double playerZ,
      float partial,
      boolean cave,
      EntityRadarCategory category,
      int heightLimit,
      boolean heightBasedFade,
      int startFadingAt,
      int colorIndex,
      double maxDistance
   ) {
      double offx = minimap.getEntityRadar().getEntityX(e, partial) - playerX;
      double offx2 = offx * offx;
      if (offx2 > maxDistance) {
         return false;
      } else {
         double offz = minimap.getEntityRadar().getEntityZ(e, partial) - playerZ;
         double offz2 = offz * offz;
         if (offz2 > maxDistance) {
            return false;
         } else {
            if (e instanceof class_1657 && this.modMain.getTrackedPlayerRenderer().getCollector().playerExists(e.method_5667())) {
               this.modMain.getTrackedPlayerRenderer().getCollector().confirmPlayerRadarRender((class_1657)e);
            }

            float offh = (float)(renderEntity.method_23318() - e.method_23318());
            double Z = pc * offx + ps * offz;
            double X = ps * offx - pc * offz;
            double drawXDouble = (double)halfW + X * this.zoom;
            double drawYDouble = (double)halfH + Z * this.zoom;
            float drawLeft = (float)drawXDouble - 2.5F;
            float drawTop = (float)drawYDouble - 2.5F;
            int drawX = mapW - Math.round((float)mapW - drawLeft) + 2;
            int drawY = Math.round(drawTop) + 2;
            int color = minimap.getEntityRadar().getEntityColour(p, e, offh, false, category, heightLimit, startFadingAt, heightBasedFade, colorIndex);

            for (int a = drawX - 2; a < drawX + 4; a++) {
               if (a >= 0 && a < mapW) {
                  for (int b = drawY - 2; b < drawY + 4; b++) {
                     if (b >= 0
                        && b < mapW
                        && (a != drawX - 2 && a != drawX + 3 || b != drawY - 2 && b != drawY + 3)
                        && (a != drawX + 2 || b != drawY - 2)
                        && (a != drawX + 3 || b != drawY - 1)
                        && (a != drawX - 2 || b != drawY + 2)
                        && (a != drawX - 1 || b != drawY + 3)) {
                        if (a != drawX + 3 && b != drawY + 3 && (a != drawX + 2 || b != drawY + 2)) {
                           this.getHelper().putColor(this.bytes, a, bufferSize - 1 - b, color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, bufferSize);
                        } else {
                           this.getHelper().putColor(this.bytes, a, bufferSize - 1 - b, 0, 0, 0, bufferSize);
                        }
                     }
                  }
               }
            }

            return true;
         }
      }
   }

   @Override
   protected void renderChunks(
      MinimapSession session,
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
      class_4587 matrixStack = guiGraphics.method_51448();
      this.updateMapFrameSafeMode(
         session, minimap, this.mc.field_1724, this.mc.method_1560(), bufferSize, mapSize, partial, lightLevel, lockedNorth, shape, ps, pc, cave, settings
      );
      matrixStack.method_22905(sizeFix, sizeFix, 1.0F);

      try {
         this.mapTexture.loadIfNeeded();
         this.getHelper().bindTextureBuffer(this.mapTexture.buffer, bufferSize, bufferSize, this.mapTexture.method_4624());
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float)(this.modMain.getSettings().minimapOpacity / 100.0));
      } catch (Throwable var30) {
         this.minimap.setCrashedWith(var30);
      }
   }

   private void getLoadedBlockColor(MinimapSession session, MinimapProcessor minimap, int[] result, int par1, int par2, int level) {
      int tileX = par1 >> 4;
      int tileZ = par2 >> 4;
      int chunkX = (tileX >> 2) - minimap.getMinimapWriter().getLoadedMapChunkX();
      int chunkZ = (tileZ >> 2) - minimap.getMinimapWriter().getLoadedMapChunkZ();
      if (minimap.getMinimapWriter().getLoadedBlocks() != null
         && chunkX >= 0
         && chunkX < minimap.getMinimapWriter().getLoadedBlocks().length
         && chunkZ >= 0
         && chunkZ < minimap.getMinimapWriter().getLoadedBlocks().length) {
         try {
            MinimapChunk current = minimap.getMinimapWriter().getLoadedBlocks()[chunkX][chunkZ];
            if (current != null) {
               MinimapTile tile = current.getTile(tileX & 3, tileZ & 3);
               if (tile != null) {
                  int insideX = par1 & 15;
                  int insideZ = par2 & 15;
                  this.chunkOverlay(
                     session, result, tile.getRed(level, insideX, insideZ), tile.getGreen(level, insideX, insideZ), tile.getBlue(level, insideX, insideZ), tile
                  );
                  return;
               }
            }
         } catch (ArrayIndexOutOfBoundsException var15) {
         }

         result[0] = result[1] = result[2] = 1;
      } else {
         result[0] = result[1] = result[2] = 1;
      }
   }

   private void chunkOverlay(MinimapSession session, int[] result, int red, int green, int blue, MinimapTile c) {
      if (this.modMain.getSettings().getSlimeChunks(session) && c.isSlimeChunk()) {
         this.getHelper().slimeOverlay(result, red, green, blue);
      } else if (this.modMain.getSettings().chunkGrid > -1 && c.isChunkGrid()) {
         this.getHelper().gridOverlay(result, ModSettings.COLORS[this.modMain.getSettings().chunkGrid], red, green, blue);
      } else {
         result[0] = red;
         result[1] = green;
         result[2] = blue;
      }
   }
}
