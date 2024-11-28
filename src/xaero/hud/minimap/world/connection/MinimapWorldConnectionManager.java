package xaero.hud.minimap.world.connection;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import xaero.common.minimap.waypoints.WaypointWorldConnectionManager;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.path.XaeroPath;

public abstract class MinimapWorldConnectionManager {
   private Map<XaeroPath, Set<XaeroPath>> allConnections = new HashMap<>();
   private final boolean multiplayer;

   protected MinimapWorldConnectionManager(boolean multiplayer) {
      this.multiplayer = multiplayer;
   }

   public void addConnection(MinimapWorld world1, MinimapWorld world2) {
      this.addConnection(world1.getLocalWorldKey(), world2.getLocalWorldKey());
   }

   public void addConnection(XaeroPath worldKey1, XaeroPath worldKey2) {
      this.addOneWayConnection(worldKey1, worldKey2);
      this.addOneWayConnection(worldKey2, worldKey1);
   }

   private void addOneWayConnection(XaeroPath worldKey1, XaeroPath worldKey2) {
      Set<XaeroPath> connections = this.allConnections.get(worldKey1);
      if (connections == null) {
         this.allConnections.put(worldKey1, connections = new HashSet<>());
      }

      connections.add(worldKey2);
   }

   public void removeConnection(MinimapWorld world1, MinimapWorld world2) {
      this.removeConnection(world1.getLocalWorldKey(), world2.getLocalWorldKey());
   }

   protected void removeConnection(XaeroPath worldKey1, XaeroPath worldKey2) {
      this.removeOneWayConnection(worldKey1, worldKey2);
      this.removeOneWayConnection(worldKey2, worldKey1);
   }

   private void removeOneWayConnection(XaeroPath worldKey1, XaeroPath worldKey2) {
      Set<XaeroPath> connections = this.allConnections.get(worldKey1);
      if (connections != null) {
         connections.remove(worldKey2);
      }
   }

   public boolean isConnected(MinimapWorld world1, MinimapWorld world2) {
      if (!this.multiplayer) {
         return true;
      } else if (world1 == world2) {
         return true;
      } else if (world1 != null && world2 != null) {
         Set<XaeroPath> connections = this.allConnections.get(world1.getLocalWorldKey());
         return connections == null ? false : connections.contains(world2.getLocalWorldKey());
      } else {
         return false;
      }
   }

   public boolean isEmpty() {
      return this.allConnections.isEmpty();
   }

   public void save(PrintWriter writer) {
      if (!this.allConnections.isEmpty()) {
         Set<String> redundantConnections = new HashSet<>();

         for (Entry<XaeroPath, Set<XaeroPath>> entry : this.allConnections.entrySet()) {
            XaeroPath worldKey = entry.getKey();

            for (XaeroPath c : entry.getValue()) {
               String fullConnection = worldKey + ":" + c;
               if (!redundantConnections.contains(fullConnection)) {
                  writer.println("connection:" + fullConnection);
                  redundantConnections.add(c + ":" + worldKey);
               }
            }
         }
      }
   }

   public void swapConnections(MinimapWorld world1, MinimapWorld world2) {
      this.swapConnections(world1.getLocalWorldKey(), world2.getLocalWorldKey());
   }

   private void swapConnections(XaeroPath worldKey1, XaeroPath worldKey2) {
      Set<XaeroPath> connections1 = new HashSet<>(this.allConnections.getOrDefault(worldKey1, new HashSet<>()));
      Set<XaeroPath> connections2 = new HashSet<>(this.allConnections.getOrDefault(worldKey2, new HashSet<>()));

      for (XaeroPath c : connections1) {
         if (!c.equals(worldKey2)) {
            this.removeConnection(worldKey1, c);
         }
      }

      for (XaeroPath cx : connections2) {
         if (!cx.equals(worldKey1)) {
            this.addConnection(worldKey1, cx);
         }
      }

      for (XaeroPath cxx : connections2) {
         if (!cxx.equals(worldKey1)) {
            this.removeConnection(worldKey2, cxx);
         }
      }

      for (XaeroPath cxxx : connections1) {
         if (!cxxx.equals(worldKey2)) {
            this.addConnection(worldKey2, cxxx);
         }
      }
   }

   public void renameDimension(String oldName, String newName) {
      for (XaeroPath worldKey : new HashSet<>(this.allConnections.keySet())) {
         if (worldKey.getNodeCount() > 1 && worldKey.getRoot().getLastNode().equals(oldName)) {
            XaeroPath nonDimPart = worldKey.getSubPath(1);
            this.swapConnections(worldKey, XaeroPath.root(newName).resolve(nonDimPart));
         }
      }
   }

   public static final class Builder {
      private boolean multiplayer;

      private Builder() {
      }

      public MinimapWorldConnectionManager.Builder setDefault() {
         this.setMultiplayer(true);
         return this;
      }

      public MinimapWorldConnectionManager.Builder setMultiplayer(boolean multiplayer) {
         this.multiplayer = multiplayer;
         return this;
      }

      public MinimapWorldConnectionManager build() {
         return new WaypointWorldConnectionManager(this.multiplayer);
      }

      public static MinimapWorldConnectionManager.Builder begin() {
         return new MinimapWorldConnectionManager.Builder().setDefault();
      }
   }
}
