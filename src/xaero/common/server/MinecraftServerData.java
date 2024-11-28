package xaero.common.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import xaero.common.IXaeroMinimap;
import xaero.common.server.level.LevelMapProperties;
import xaero.common.server.level.LevelMapPropertiesIO;
import xaero.common.server.radar.tracker.SyncedPlayerTracker;
import xaero.common.server.radar.tracker.SyncedPlayerTrackerSystemManager;

public class MinecraftServerData {
   private final SyncedPlayerTrackerSystemManager syncedPlayerTrackerSystemManager;
   private final SyncedPlayerTracker syncedPlayerTracker;
   private final Map<Path, LevelMapProperties> levelProperties;
   private final LevelMapPropertiesIO propertiesIO;
   private final IXaeroMinimap modMain;

   public MinecraftServerData(SyncedPlayerTrackerSystemManager syncedPlayerTrackerSystemManager, SyncedPlayerTracker syncedPlayerTracker, IXaeroMinimap modMain) {
      this.syncedPlayerTrackerSystemManager = syncedPlayerTrackerSystemManager;
      this.syncedPlayerTracker = syncedPlayerTracker;
      this.levelProperties = new HashMap<>();
      this.propertiesIO = new LevelMapPropertiesIO();
      this.modMain = modMain;
   }

   public LevelMapProperties getLevelProperties(Path path) {
      LevelMapProperties properties = this.levelProperties.get(path);
      if (properties == null) {
         properties = new LevelMapProperties();

         try {
            this.propertiesIO.load(path, properties);
         } catch (FileNotFoundException var4) {
            this.propertiesIO.save(path, properties);
         } catch (IOException var5) {
            throw new RuntimeException(var5);
         }

         this.levelProperties.put(path, properties);
      }

      return properties;
   }

   public SyncedPlayerTrackerSystemManager getSyncedPlayerTrackerSystemManager() {
      return this.syncedPlayerTrackerSystemManager;
   }

   public SyncedPlayerTracker getSyncedPlayerTracker() {
      return this.syncedPlayerTracker;
   }

   public static MinecraftServerData get(MinecraftServer server) {
      return ((IMinecraftServer)server).getXaeroMinimapServerData();
   }

   public IXaeroMinimap getModMain() {
      return this.modMain;
   }
}
