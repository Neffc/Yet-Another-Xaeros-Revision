package xaero.common.events;

import java.nio.file.Path;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.class_2561;
import net.minecraft.class_3222;
import net.minecraft.class_5218;
import net.minecraft.server.MinecraftServer;
import xaero.common.AXaeroMinimap;
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

public class ForgeCommonEventHandler {
   private final AXaeroMinimap modMain;

   public ForgeCommonEventHandler(AXaeroMinimap modMain) {
      this.modMain = modMain;
   }

   public void register() {
      ServerPlayerEvents.COPY_FROM.register(this::onPlayerClone);
      ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarting);
      ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);
   }

   private void onPlayerClone(class_3222 oldPlayer, class_3222 newPlayer, boolean alive) {
      ((IServerPlayer)newPlayer).setXaeroMinimapPlayerData(ServerPlayerData.get(oldPlayer));
   }

   public void onServerStarting(MinecraftServer server) {
      new MineraftServerDataInitializer().init(server, this.modMain);
   }

   public void onServerStopped(MinecraftServer server) {
   }

   public void onPlayerLogIn(class_3222 serverPlayer) {
      this.modMain.getMessageHandler().sendToPlayer(serverPlayer, new ClientboundPlayerTrackerResetPacket());
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
}
