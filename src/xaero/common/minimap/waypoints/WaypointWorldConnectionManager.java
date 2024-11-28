package xaero.common.minimap.waypoints;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class WaypointWorldConnectionManager {
   private Map<String, Set<String>> allConnections = new HashMap<>();

   public void addConnection(WaypointWorld world1, WaypointWorld world2) {
      this.addConnection(world1.getInternalWorldKey(), world2.getInternalWorldKey());
   }

   void addConnection(String worldKey1, String worldKey2) {
      this.addOneWayConnection(worldKey1, worldKey2);
      this.addOneWayConnection(worldKey2, worldKey1);
   }

   private void addOneWayConnection(String worldKey1, String worldKey2) {
      Set<String> connections = this.allConnections.get(worldKey1);
      if (connections == null) {
         this.allConnections.put(worldKey1, connections = new HashSet<>());
      }

      connections.add(worldKey2);
   }

   public void removeConnection(WaypointWorld world1, WaypointWorld world2) {
      this.removeConnection(world1.getInternalWorldKey(), world2.getInternalWorldKey());
   }

   private void removeConnection(String worldKey1, String worldKey2) {
      this.removeOneWayConnection(worldKey1, worldKey2);
      this.removeOneWayConnection(worldKey2, worldKey1);
   }

   private void removeOneWayConnection(String worldKey1, String worldKey2) {
      Set<String> connections = this.allConnections.get(worldKey1);
      if (connections != null) {
         connections.remove(worldKey2);
      }
   }

   public boolean isConnected(WaypointWorld world1, WaypointWorld world2) {
      if (world1 == world2) {
         return true;
      } else if (world1 != null && world2 != null) {
         Set<String> connections = this.allConnections.get(world1.getInternalWorldKey());
         return connections == null ? false : connections.contains(world2.getInternalWorldKey());
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

         for (Entry<String, Set<String>> entry : this.allConnections.entrySet()) {
            String worldKey = entry.getKey();

            for (String c : entry.getValue()) {
               String fullConnection = worldKey + ":" + c;
               if (!redundantConnections.contains(fullConnection)) {
                  writer.println("connection:" + fullConnection);
                  redundantConnections.add(c + ":" + worldKey);
               }
            }
         }
      }
   }

   public void swapConnections(WaypointWorld world1, WaypointWorld world2) {
      this.swapConnections(world1.getInternalWorldKey(), world2.getInternalWorldKey());
   }

   private void swapConnections(String worldKey1, String worldKey2) {
      Set<String> connections1 = new HashSet<>(this.allConnections.getOrDefault(worldKey1, new HashSet<>()));
      Set<String> connections2 = new HashSet<>(this.allConnections.getOrDefault(worldKey2, new HashSet<>()));

      for (String c : connections1) {
         this.removeConnection(worldKey1, c);
      }

      for (String c : connections2) {
         this.addConnection(worldKey1, c);
      }

      for (String c : connections2) {
         this.removeConnection(worldKey2, c);
      }

      for (String c : connections1) {
         this.addConnection(worldKey2, c);
      }
   }

   public void renameDimension(String oldName, String newName) {
      for (String worldKey : new HashSet<>(this.allConnections.keySet())) {
         if (worldKey.startsWith(oldName + "/")) {
            String mwPart = worldKey.substring(oldName.length());
            this.swapConnections(worldKey, newName + mwPart);
         }
      }
   }
}
