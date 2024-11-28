package xaero.common.mixin;

import java.util.function.BooleanSupplier;
import net.minecraft.class_3176;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.AXaeroMinimap;
import xaero.common.server.IMinecraftServer;
import xaero.common.server.MinecraftServerData;

@Mixin({MinecraftServer.class})
public class MixinMinecraftServer implements IMinecraftServer {
   private MinecraftServerData xaeroMinimapServerData;

   @Inject(
      at = {@At("HEAD")},
      method = {"tickServer"}
   )
   public void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
      if (this instanceof class_3176) {
         AXaeroMinimap.INSTANCE.tryLoadLaterServer();
      }
   }

   @Override
   public MinecraftServerData getXaeroMinimapServerData() {
      return this.xaeroMinimapServerData;
   }

   @Override
   public void setXaeroMinimapServerData(MinecraftServerData data) {
      this.xaeroMinimapServerData = data;
   }
}
