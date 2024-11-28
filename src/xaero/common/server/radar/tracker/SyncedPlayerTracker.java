package xaero.common.server.radar.tracker;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.class_3222;
import net.minecraft.server.MinecraftServer;
import xaero.common.AXaeroMinimap;
import xaero.common.message.tracker.ClientboundTrackedPlayerPacket;
import xaero.common.server.MinecraftServerData;
import xaero.common.server.player.ServerPlayerData;

public class SyncedPlayerTracker {
   private final AXaeroMinimap modMain;

   public SyncedPlayerTracker(AXaeroMinimap modMain) {
      this.modMain = modMain;
   }

   public void onTick(MinecraftServer server, class_3222 player, MinecraftServerData serverData, ServerPlayerData playerData) {
      long currentTime = System.currentTimeMillis();
      if (currentTime - playerData.getLastTrackedPlayerSync() >= 250L) {
         playerData.setLastTrackedPlayerSync(currentTime);
         boolean playerHasMod = playerData.hasMod();
         boolean shouldSyncToPlayer = playerHasMod;
         Iterable<ISyncedPlayerTrackerSystem> playerTrackerSystems = serverData.getSyncedPlayerTrackerSystemManager().getSystems();
         Set<UUID> syncedPlayers = playerData.ensureCurrentlySyncedPlayers();
         Set<UUID> leftoverPlayers = new HashSet<>(syncedPlayers);
         SyncedTrackedPlayer toSync = playerData.getLastSyncedData();
         boolean shouldSyncToOthers = toSync == null || !toSync.matchesEnough(player, 0.0);
         if (shouldSyncToOthers) {
            toSync = playerData.ensureLastSyncedData();
            toSync.update(player);
         }

         for (class_3222 otherPlayer : server.method_3760().method_14571()) {
            if (otherPlayer != player) {
               leftoverPlayers.remove(otherPlayer.method_5667());
               ServerPlayerData otherPlayerData = ServerPlayerData.get(otherPlayer);
               if (shouldSyncToOthers) {
                  Set<UUID> otherPlayerSyncedPlayers = otherPlayerData.getCurrentlySyncedPlayers();
                  if (otherPlayerSyncedPlayers != null && otherPlayerSyncedPlayers.contains(player.method_5667())) {
                     this.sendTrackedPlayerPacket(otherPlayer, toSync);
                  }
               }

               if (shouldSyncToPlayer) {
                  boolean tracked = false;
                  boolean opacConfigsAllowPartySync = !this.modMain.getSupportServerMods().hasOpac()
                     || this.modMain.getSupportServerMods().getOpac().isPositionSyncAllowed(2, player, otherPlayer);
                  boolean opacConfigsAllowAllySync = !this.modMain.getSupportServerMods().hasOpac()
                     || this.modMain.getSupportServerMods().getOpac().isPositionSyncAllowed(1, player, otherPlayer);

                  for (ISyncedPlayerTrackerSystem system : playerTrackerSystems) {
                     int trackingLevel = system.getTrackingLevel(player, otherPlayer);
                     if (trackingLevel > 0
                        && (!system.isPartySystem() || trackingLevel == 1 && opacConfigsAllowAllySync || trackingLevel > 1 && opacConfigsAllowPartySync)) {
                        tracked = true;
                        break;
                     }
                  }

                  boolean alreadySynced = syncedPlayers.contains(otherPlayer.method_5667());
                  if (!tracked) {
                     if (alreadySynced) {
                        syncedPlayers.remove(otherPlayer.method_5667());
                        this.sendRemovePacket(player, otherPlayer.method_5667());
                     }
                  } else if (!alreadySynced && otherPlayerData.getLastSyncedData() != null) {
                     syncedPlayers.add(otherPlayer.method_5667());
                     this.sendTrackedPlayerPacket(player, otherPlayerData.getLastSyncedData());
                  }
               }
            }
         }

         for (UUID offlineId : leftoverPlayers) {
            syncedPlayers.remove(offlineId);
            this.sendRemovePacket(player, offlineId);
         }
      }
   }

   private void sendRemovePacket(class_3222 player, UUID toRemove) {
      this.modMain.getMessageHandler().sendToPlayer(player, new ClientboundTrackedPlayerPacket(true, toRemove, 0.0, 0.0, 0.0, null));
   }

   private void sendTrackedPlayerPacket(class_3222 player, SyncedTrackedPlayer tracked) {
      this.modMain
         .getMessageHandler()
         .sendToPlayer(
            player,
            new ClientboundTrackedPlayerPacket(false, tracked.getId(), tracked.getX(), tracked.getY(), tracked.getZ(), tracked.getDimensionKey().method_29177())
         );
   }
}
