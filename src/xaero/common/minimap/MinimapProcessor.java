package xaero.common.minimap;

import java.io.IOException;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_638;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.anim.OldAnimation;
import xaero.common.graphics.CustomVertexConsumers;
import xaero.common.minimap.mcworld.MinimapClientWorldData;
import xaero.common.minimap.mcworld.MinimapClientWorldDataHelper;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.radar.category.EntityRadarCategoryManager;
import xaero.common.minimap.radar.tracker.synced.ClientSyncedTrackedPlayerManager;
import xaero.common.minimap.write.MinimapWriter;
import xaero.common.misc.Misc;
import xaero.common.settings.ModOptions;
import xaero.common.settings.ModSettings;

public class MinimapProcessor {
   public static final boolean DEBUG = false;
   public static final int FRAME = 9;
   private IXaeroMinimap modMain;
   private XaeroMinimapSession minimapSession;
   private MinimapWriter minimapWriter;
   private MinimapRadar entityRadar;
   private MinimapInterface minimapInterface;
   private EntityRadarCategoryManager entityCategoryManager;
   private ClientSyncedTrackedPlayerManager clientSyncedTrackedPlayerManager;
   private double minimapZoom;
   private boolean toResetImage;
   private boolean enlargedMap;
   private boolean manualCaveMode;
   private boolean noMinimapMessageReceived;
   private boolean fairPlayOnlyMessageReceived;

   public MinimapProcessor(
      IXaeroMinimap modMain,
      XaeroMinimapSession minimapSession,
      MinimapWriter minimapWriter,
      MinimapRadar entityRadar,
      ClientSyncedTrackedPlayerManager clientSyncedTrackedPlayerManager
   ) throws IOException {
      this.modMain = modMain;
      this.minimapSession = minimapSession;
      this.minimapWriter = minimapWriter;
      this.entityRadar = entityRadar;
      this.minimapZoom = 1.0;
      this.toResetImage = true;
      this.minimapInterface = modMain.getInterfaces().getMinimapInterface();
      this.clientSyncedTrackedPlayerManager = clientSyncedTrackedPlayerManager;
   }

   public int getMinimapSize() {
      return this.enlargedMap ? 500 : this.modMain.getSettings().getMinimapSize() * 2;
   }

   public int getMinimapBufferSize(int minimapSize) {
      int bufferSize = 128 * (int)Math.pow(2.0, Math.ceil(Math.log((double)minimapSize / 128.0) / Math.log(2.0)));
      if (bufferSize < 128) {
         return 128;
      } else {
         return bufferSize > 512 ? 512 : bufferSize;
      }
   }

   public boolean isEnlargedMap() {
      return this.enlargedMap;
   }

   public void setEnlargedMap(boolean enlargedMap) {
      this.enlargedMap = enlargedMap;
   }

   public double getMinimapZoom() {
      return this.minimapZoom;
   }

   public boolean isCaveModeDisplayed() {
      return this.minimapWriter.getLoadedCaving() != Integer.MAX_VALUE;
   }

   public double getTargetZoom() {
      this.modMain.getSettings();
      float settingsZoom = ModSettings.zooms[this.modMain.getSettings().zoom];
      if (this.enlargedMap && this.modMain.getSettings().zoomOnEnlarged > 0) {
         settingsZoom = (float)this.modMain.getSettings().zoomOnEnlarged;
      }

      float target = settingsZoom
         * (this.modMain.getSettings().caveZoom > 0 && this.isCaveModeDisplayed() ? (float)(1 + this.modMain.getSettings().caveZoom) : 1.0F);
      this.modMain.getSettings();
      float[] var10001 = ModSettings.zooms;
      this.modMain.getSettings();
      if (target > var10001[ModSettings.zooms.length - 1]) {
         this.modMain.getSettings();
         float[] var10000 = ModSettings.zooms;
         this.modMain.getSettings();
         target = var10000[ModSettings.zooms.length - 1];
      }

      return (double)target;
   }

   public void instantZoom() {
      this.minimapZoom = this.getTargetZoom();
   }

   public void updateZoom() {
      double target = this.getTargetZoom();
      double off = target - this.minimapZoom;
      if (!(off > 0.01) && !(off < -0.01)) {
         off = 0.0;
      } else {
         off = (double)((float)OldAnimation.animate(off, 0.8));
      }

      this.minimapZoom = target - off;
   }

   public MinimapWriter getMinimapWriter() {
      return this.minimapWriter;
   }

   public boolean canUseFrameBuffer() {
      return true;
   }

   public int getFBOBufferSize() {
      return 512;
   }

   public void onClientTick() {
      class_1937 world = null;
      class_1657 player = class_310.method_1551().field_1724;
      if (player != null && player.method_37908() instanceof class_638) {
         world = player.method_37908();
      }

      this.entityRadar.updateRadar((class_638)world, player, class_310.method_1551().method_1560(), this);
   }

   public void onPlayerTick() {
   }

   public void checkFBO() {
      if (this.minimapInterface.getMinimapFBORenderer().isLoadedFBO() && !this.canUseFrameBuffer()) {
         this.minimapInterface.getMinimapFBORenderer().setLoadedFBO(false);
         this.minimapInterface.getMinimapFBORenderer().deleteFramebuffers();
         this.toResetImage = true;
      }

      if (!this.minimapInterface.getMinimapFBORenderer().isLoadedFBO()
         && !this.modMain.getSettings().mapSafeMode
         && !this.minimapInterface.getMinimapFBORenderer().isTriedFBO()) {
         if (class_310.method_1551().method_18506() != null) {
            return;
         }

         this.minimapInterface.getMinimapFBORenderer().loadFrameBuffer(this);
      }
   }

   public void onRender(
      class_332 guiGraphics, int x, int y, int width, int height, double scale, int size, int boxSize, float partial, CustomVertexConsumers cvc
   ) {
      try {
         if (this.enlargedMap && this.modMain.getSettings().centeredEnlarged) {
            x = (width - boxSize) / 2;
            y = (height - boxSize) / 2;
         }

         if (this.minimapInterface.usingFBO()) {
            this.minimapInterface.getMinimapFBORenderer().renderMinimap(this.minimapSession, guiGraphics, this, x, y, width, height, scale, size, partial, cvc);
         } else {
            this.minimapInterface
               .getMinimapSafeModeRenderer()
               .renderMinimap(this.minimapSession, guiGraphics, this, x, y, width, height, scale, size, partial, cvc);
         }
      } catch (Throwable var13) {
         this.minimapInterface.setCrashedWith(var13);
      }
   }

   public static boolean hasMinimapItem(class_1657 player) {
      return Misc.hasItem(player, ModSettings.minimapItem);
   }

   public boolean isToResetImage() {
      return this.toResetImage;
   }

   public void setToResetImage(boolean toResetImage) {
      this.toResetImage = toResetImage;
   }

   public MinimapRadar getEntityRadar() {
      return this.entityRadar;
   }

   public void cleanup() {
      this.minimapWriter.cleanup();
   }

   public boolean isManualCaveMode() {
      return this.manualCaveMode
         || this.modMain.getSettings().usesWorldMapScreenValue(ModOptions.MANUAL_CAVE_MODE_START)
            && this.modMain.getSupportMods().worldmapSupport.getManualCaveStart() != Integer.MAX_VALUE;
   }

   public void toggleManualCaveMode() {
      this.manualCaveMode = !this.isManualCaveMode();
   }

   public MinimapInterface getMinimapInterface() {
      return this.minimapInterface;
   }

   public boolean getNoMinimapMessageReceived() {
      return this.noMinimapMessageReceived;
   }

   public void setNoMinimapMessageReceived(boolean noMinimapMessageReceived) {
      this.noMinimapMessageReceived = noMinimapMessageReceived;
   }

   public boolean getForcedFairPlay() {
      return this.fairPlayOnlyMessageReceived;
   }

   public void setFairPlayOnlyMessageReceived(boolean fairPlayOnlyMessageReceived) {
      this.fairPlayOnlyMessageReceived = fairPlayOnlyMessageReceived;
   }

   public ClientSyncedTrackedPlayerManager getClientSyncedTrackedPlayerManager() {
      return this.clientSyncedTrackedPlayerManager;
   }

   public boolean serverHasMod() {
      MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
      return worldData != null && worldData.serverLevelId != null;
   }

   public void setServerModNetworkVersion(int networkVersion) {
      MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
      if (worldData != null) {
         worldData.setServerModNetworkVersion(networkVersion);
      }
   }

   public int getServerModNetworkVersion() {
      MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
      return worldData == null ? 0 : worldData.getServerModNetworkVersion();
   }
}
