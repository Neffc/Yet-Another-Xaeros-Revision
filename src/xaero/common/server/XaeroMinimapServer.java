package xaero.common.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xaero.common.AXaeroMinimap;
import xaero.common.server.events.ServerEvents;

public class XaeroMinimapServer {
   public static int SERVER_COMPATIBILITY = 1;
   private final Logger LOGGER = LogManager.getLogger();
   private AXaeroMinimap modMain;
   private ServerEvents serverEvents;

   public XaeroMinimapServer(AXaeroMinimap modMain) {
      this.modMain = modMain;
   }

   public void load() {
      this.serverEvents = new ServerEvents(this);
      this.serverEvents.register();
   }

   public void loadLater() {
   }

   public AXaeroMinimap getModMain() {
      return this.modMain;
   }

   public ServerEvents getServerEvents() {
      return this.serverEvents;
   }
}
