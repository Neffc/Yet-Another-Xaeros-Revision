package xaero.common.events;

import java.nio.file.Path;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_3222;
import net.minecraft.class_5218;
import net.minecraft.server.MinecraftServer;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.config.CommonConfig;
import xaero.common.message.basic.ClientboundRulesPacket;
import xaero.common.message.basic.HandshakePacket;
import xaero.common.message.tracker.ClientboundPlayerTrackerResetPacket;
import xaero.common.server.MinecraftServerData;
import xaero.common.server.MineraftServerDataInitializer;
import xaero.common.server.level.LevelMapProperties;
import xaero.common.server.player.IServerPlayer;
import xaero.common.server.player.ServerPlayerData;

public abstract class CommonEvents {
   private final IXaeroMinimap modMain;

   public CommonEvents(IXaeroMinimap modMain) {
      this.modMain = modMain;
   }

   void onPlayerClone(class_1657 oldPlayer, class_1657 newPlayer, boolean alive) {
      if (oldPlayer instanceof class_3222) {
         ((IServerPlayer)newPlayer).setXaeroMinimapPlayerData(ServerPlayerData.get((class_3222)oldPlayer));
      }
   }

   public void onServerStarting(MinecraftServer server) {
      new MineraftServerDataInitializer().init(server, this.modMain);
   }

   public void onServerStopped(MinecraftServer server) {
   }

   public void onPlayerLogIn(class_1657 player) {
      if (player instanceof class_3222 serverPlayer) {
         this.modMain.getMessageHandler().sendToPlayer(serverPlayer, new ClientboundPlayerTrackerResetPacket());
      }
   }

   public void onPlayerWorldJoin(class_3222 player) {
      this.modMain.getMessageHandler().sendToPlayer(player, new HandshakePacket());
      CommonConfig config = this.modMain.getCommonConfig();
      this.modMain
         .getMessageHandler()
         .sendToPlayer(player, new ClientboundRulesPacket(config.allowCaveModeOnServer, config.allowNetherCaveModeOnServer, config.allowRadarOnServer));
      Path propertiesPath = player.method_37908().method_8503().method_27050(class_5218.field_24184).getParent().resolve("xaeromap.txt");

      try {
         MinecraftServerData serverData = MinecraftServerData.get(player.method_5682());
         LevelMapProperties properties = serverData.getLevelProperties(propertiesPath);
         this.modMain.getMessageHandler().sendToPlayer(player, properties);
      } catch (Throwable var6) {
         MinimapLogs.LOGGER.error("suppressed exception", var6);
         player.field_13987.method_14367(class_2561.method_43471("gui.xaero_error_loading_properties"));
      }
   }

   public void handlePlayerTickStart(class_1657 player) {
      if (player instanceof class_3222) {
         this.modMain.getServerPlayerTickHandler().tick((class_3222)player);
      } else {
         if (HudMod.INSTANCE.getEvents() != null) {
            HudMod.INSTANCE.getEvents().handlePlayerTickStart(player);
         }
      }
   }
}
