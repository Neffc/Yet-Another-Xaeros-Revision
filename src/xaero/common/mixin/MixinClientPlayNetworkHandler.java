package xaero.common.mixin;

import net.minecraft.class_2626;
import net.minecraft.class_2637;
import net.minecraft.class_2666;
import net.minecraft.class_2672;
import net.minecraft.class_2676;
import net.minecraft.class_2678;
import net.minecraft.class_2759;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_6603;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.common.AXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.IXaeroMinimapClientPlayNetHandler;
import xaero.common.core.XaeroMinimapCore;

@Mixin({class_634.class})
public class MixinClientPlayNetworkHandler implements IXaeroMinimapClientPlayNetHandler {
   XaeroMinimapSession xaero_minimapSession;

   @Inject(
      at = {@At(
         value = "INVOKE",
         shift = Shift.AFTER,
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V"
      )},
      method = {"handleChunkBlocksUpdate"}
   )
   public void onOnChunkDeltaUpdate(class_2637 packet, CallbackInfo info) {
      XaeroMinimapCore.onMultiBlockChange(packet);
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"updateLevelChunk"}
   )
   public void onOnChunkData(int x, int z, class_6603 packet, CallbackInfo info) {
      XaeroMinimapCore.onChunkData(x, z, packet);
   }

   @Inject(
      at = {@At(
         value = "INVOKE",
         shift = Shift.AFTER,
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V"
      )},
      method = {"handleLevelChunkWithLight"}
   )
   public void onHandleLevelChunkWithLight(class_2672 packet, CallbackInfo info) {
      XaeroMinimapCore.onChunkLightData(packet.method_11523(), packet.method_11524());
   }

   @Inject(
      at = {@At(
         value = "INVOKE",
         shift = Shift.AFTER,
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V"
      )},
      method = {"handleLightUpdatePacket"}
   )
   public void onHandleLightUpdatePacket(class_2676 packet, CallbackInfo info) {
      XaeroMinimapCore.onChunkLightData(packet.method_11558(), packet.method_11554());
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"queueLightRemoval"}
   )
   public void onQueueLightRemoval(class_2666 packet, CallbackInfo info) {
      XaeroMinimapCore.onChunkLightData(packet.method_11487(), packet.method_11485());
   }

   @Inject(
      at = {@At(
         value = "INVOKE",
         shift = Shift.AFTER,
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V"
      )},
      method = {"handleBlockUpdate"}
   )
   public void onOnBlockUpdate(class_2626 packet, CallbackInfo info) {
      XaeroMinimapCore.onBlockChange(packet);
   }

   @Inject(
      at = {@At(
         value = "INVOKE",
         shift = Shift.AFTER,
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V"
      )},
      method = {"handleSetSpawn"}
   )
   public void onOnPlayerSpawnPosition(class_2759 packet, CallbackInfo info) {
      AXaeroMinimap.INSTANCE.getEvents().handlePlayerSetSpawnEvent(packet.method_11870(), class_310.method_1551().field_1687);
   }

   @Override
   public XaeroMinimapSession getXaero_minimapSession() {
      return this.xaero_minimapSession;
   }

   @Override
   public void setXaero_minimapSession(XaeroMinimapSession session) {
      this.xaero_minimapSession = session;
   }

   @Inject(
      at = {@At(
         value = "INVOKE",
         shift = Shift.AFTER,
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V"
      )},
      method = {"handleLogin"}
   )
   public void onOnGameJoin(class_2678 packet, CallbackInfo info) {
      XaeroMinimapCore.onPlayNetHandler((class_634)this, packet);
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"close"}
   )
   public void onClose(CallbackInfo info) {
      XaeroMinimapCore.onPlayNetHandlerCleanup((class_634)this);
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"sendChat(Ljava/lang/String;)V"},
      cancellable = true
   )
   public void onSendChat(String string_1, CallbackInfo info) {
      String result = AXaeroMinimap.INSTANCE.getEvents().handleClientSendChatEvent(string_1);
      if (result == null || result.isEmpty()) {
         info.cancel();
      }
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"sendCommand(Ljava/lang/String;)V"},
      cancellable = true
   )
   public void onSendCommand(String string_1, CallbackInfo info) {
      String result = AXaeroMinimap.INSTANCE.getEvents().handleClientSendChatEvent(string_1);
      if (result == null || result.isEmpty()) {
         info.cancel();
      }
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"sendUnsignedCommand(Ljava/lang/String;)Z"},
      cancellable = true
   )
   public void onSendUnsignedCommand(String string_1, CallbackInfoReturnable<Boolean> info) {
      String result = AXaeroMinimap.INSTANCE.getEvents().handleClientSendChatEvent(string_1);
      if (result == null || result.isEmpty()) {
         info.setReturnValue(true);
      }
   }
}
