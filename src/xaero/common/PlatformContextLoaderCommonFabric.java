package xaero.common;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_2378;
import net.minecraft.class_7923;
import xaero.common.effect.EffectsRegister;
import xaero.common.events.CommonEventsFabric;
import xaero.common.message.MinimapMessage;
import xaero.common.message.MinimapMessageHandlerFabric;
import xaero.common.message.server.ServerMinimapPacketConsumer;

public class PlatformContextLoaderCommonFabric extends PlatformContextLoaderCommon {
   @Override
   public void setup(IXaeroMinimap modMain) {
      ServerPlayNetworking.registerGlobalReceiver(
         MinimapMessage.MAIN_CHANNEL, new ServerMinimapPacketConsumer((MinimapMessageHandlerFabric)modMain.getMessageHandler())
      );
      ((CommonEventsFabric)modMain.getCommonEvents()).register();
      if (modMain.getCommonConfig().registerStatusEffects) {
         new EffectsRegister().registerEffects(effect -> class_2378.method_10230(class_7923.field_41174, effect.getRegistryName(), effect));
      }
   }
}
