package xaero.common;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import xaero.common.message.MinimapMessage;
import xaero.common.message.client.ClientMinimapPacketConsumer;

public class XaeroMinimapClientBase {
   public void preInit(String modId, AXaeroMinimap modMain) {
      ClientPlayNetworking.registerGlobalReceiver(MinimapMessage.MAIN_CHANNEL, new ClientMinimapPacketConsumer(modMain.getMessageHandler()));
   }
}
