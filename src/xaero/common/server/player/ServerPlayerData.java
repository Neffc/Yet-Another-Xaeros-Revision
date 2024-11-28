package xaero.common.server.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_3222;
import xaero.common.server.radar.tracker.SyncedTrackedPlayer;

public class ServerPlayerData {
   private final UUID playerId;
   private SyncedTrackedPlayer lastSyncedData;
   private Set<UUID> currentlySyncedPlayers;
   private long lastTrackedPlayerSync;
   private int clientModNetworkVersion;

   public ServerPlayerData(UUID playerId) {
      this.playerId = playerId;
   }

   public SyncedTrackedPlayer getLastSyncedData() {
      return this.lastSyncedData;
   }

   public SyncedTrackedPlayer ensureLastSyncedData() {
      if (this.lastSyncedData == null) {
         this.lastSyncedData = new SyncedTrackedPlayer(this.playerId, 0.0, 0.0, 0.0, null);
      }

      return this.lastSyncedData;
   }

   public Set<UUID> getCurrentlySyncedPlayers() {
      return this.currentlySyncedPlayers;
   }

   public Set<UUID> ensureCurrentlySyncedPlayers() {
      if (this.currentlySyncedPlayers == null) {
         this.currentlySyncedPlayers = new HashSet<>();
      }

      return this.currentlySyncedPlayers;
   }

   public long getLastTrackedPlayerSync() {
      return this.lastTrackedPlayerSync;
   }

   public void setLastTrackedPlayerSync(long lastTrackedPlayerSync) {
      this.lastTrackedPlayerSync = lastTrackedPlayerSync;
   }

   public static ServerPlayerData get(class_3222 player) {
      ServerPlayerData result = ((IServerPlayer)player).getXaeroMinimapPlayerData();
      if (result == null) {
         ((IServerPlayer)player).setXaeroMinimapPlayerData(result = new ServerPlayerData(player.method_5667()));
      }

      return result;
   }

   public boolean hasMod() {
      return this.clientModNetworkVersion != 0;
   }

   public void setClientModNetworkVersion(int clientModNetworkVersion) {
      this.clientModNetworkVersion = clientModNetworkVersion;
   }

   public int getClientModNetworkVersion() {
      return this.clientModNetworkVersion;
   }
}
