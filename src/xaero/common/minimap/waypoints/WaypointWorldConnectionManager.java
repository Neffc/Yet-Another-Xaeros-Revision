package xaero.common.minimap.waypoints;

import java.io.PrintWriter;
import xaero.hud.minimap.world.connection.MinimapWorldConnectionManager;
import xaero.hud.path.XaeroPathReader;

@Deprecated
public class WaypointWorldConnectionManager extends MinimapWorldConnectionManager {
   private final XaeroPathReader pathReader = new XaeroPathReader();

   @Deprecated
   public WaypointWorldConnectionManager() {
      this(true);
   }

   @Deprecated
   public WaypointWorldConnectionManager(boolean multiplayer) {
      super(multiplayer);
   }

   @Deprecated
   public void addConnection(WaypointWorld world1, WaypointWorld world2) {
      super.addConnection(world1, world2);
   }

   @Deprecated
   void addConnection(String worldKey1, String worldKey2) {
      super.addConnection(this.pathReader.read(worldKey1), this.pathReader.read(worldKey2));
   }

   @Deprecated
   public void removeConnection(WaypointWorld world1, WaypointWorld world2) {
      super.removeConnection(world1, world2);
   }

   @Deprecated
   private void removeConnection(String worldKey1, String worldKey2) {
      super.removeConnection(this.pathReader.read(worldKey1), this.pathReader.read(worldKey2));
   }

   @Deprecated
   public boolean isConnected(WaypointWorld world1, WaypointWorld world2) {
      return super.isConnected(world1, world2);
   }

   @Deprecated
   @Override
   public boolean isEmpty() {
      return super.isEmpty();
   }

   @Deprecated
   @Override
   public void save(PrintWriter writer) {
      super.save(writer);
   }

   @Deprecated
   public void swapConnections(WaypointWorld world1, WaypointWorld world2) {
      super.swapConnections(world1, world2);
   }
}
