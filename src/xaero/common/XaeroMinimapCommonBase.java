package xaero.common;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import xaero.common.effect.EffectsRegister;
import xaero.common.events.ModCommonEvents;
import xaero.common.message.MinimapMessage;
import xaero.common.message.MinimapMessageRegister;
import xaero.common.message.server.ServerMinimapPacketConsumer;
import xaero.common.server.player.ServerPlayerTickHandler;

public class XaeroMinimapCommonBase {
   public void setup(AXaeroMinimap modMain) {
      ServerPlayNetworking.registerGlobalReceiver(MinimapMessage.MAIN_CHANNEL, new ServerMinimapPacketConsumer(modMain.getMessageHandler()));
      new MinimapMessageRegister().register(modMain.getMessageHandler());
      modMain.getCommonEvents().register();
      modMain.setServerPlayerTickHandler(new ServerPlayerTickHandler());
      new ModCommonEvents();
      if (modMain.getCommonConfig().registerStatusEffects) {
         new EffectsRegister().registerEffects();
      }

      modMain.getSupportServerMods().check(modMain);
   }
}
