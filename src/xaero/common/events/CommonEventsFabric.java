package xaero.common.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import xaero.common.IXaeroMinimap;

public class CommonEventsFabric extends CommonEvents {
   public CommonEventsFabric(IXaeroMinimap modMain) {
      super(modMain);
   }

   public void register() {
      ServerPlayerEvents.COPY_FROM.register(this::onPlayerClone);
      ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarting);
      ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);
   }
}
