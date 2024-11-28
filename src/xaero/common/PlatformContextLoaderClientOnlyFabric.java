package xaero.common;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import xaero.common.message.MinimapMessage;
import xaero.common.message.MinimapMessageHandlerFabric;
import xaero.common.message.client.ClientMinimapPacketConsumer;

public class PlatformContextLoaderClientOnlyFabric extends PlatformContextLoaderClientOnly {
   @Override
   public void preInit(String modId, IXaeroMinimap modMain) {
      ClientPlayNetworking.registerGlobalReceiver(
         MinimapMessage.MAIN_CHANNEL, new ClientMinimapPacketConsumer((MinimapMessageHandlerFabric)modMain.getMessageHandler())
      );
      modMain.ensureControlsRegister();
      modMain.getControlsRegister().registerKeybindings(KeyBindingHelper::registerKeyBinding);
   }
}
