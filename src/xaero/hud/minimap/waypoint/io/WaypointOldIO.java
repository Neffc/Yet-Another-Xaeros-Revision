package xaero.hud.minimap.waypoint.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.class_1937;
import net.minecraft.class_5321;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.misc.Misc;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.path.XaeroPath;

public class WaypointOldIO {
   private final Path configFile;
   private final Path oldWaypointsFile;

   public WaypointOldIO(Path configFile, Path oldWaypointsFile) {
      this.configFile = configFile;
      this.oldWaypointsFile = oldWaypointsFile;
   }

   public boolean load(MinimapSession session) throws IOException {
      boolean shouldResave = this.loadFromFile(session, this.configFile);
      return this.loadOldWaypoints(session) || shouldResave;
   }

   public boolean loadOldWaypoints(MinimapSession session) throws IOException {
      if (!Files.exists(this.oldWaypointsFile)) {
         return false;
      } else {
         boolean result = this.loadFromFile(session, this.oldWaypointsFile);
         Misc.quickFileBackupMove(this.oldWaypointsFile);
         return result;
      }
   }

   public boolean checkLine(String[] args, MinimapSession session) {
      if (args.length == 0) {
         return false;
      } else if (!args[0].equalsIgnoreCase("world") && !args[0].equalsIgnoreCase("waypoint")) {
         return false;
      } else {
         if (!args[1].contains("_")) {
            args[1] = args[1] + "_null";
         }

         MinimapWorldContainer container = session.getWorldManager().addWorldContainer(this.convertToNewContainerID(args[1], session));
         MinimapWorld world = container.addWorld("waypoints");
         if (args[0].equalsIgnoreCase("world")) {
            world.setCurrentWaypointSetId(args[2]);

            for (int i = 2; i < args.length; i++) {
               if (world.getWaypointSet(args[i]) == null) {
                  world.addWaypointSet(WaypointSet.Builder.begin().setName(args[i]).build());
               }
            }

            return true;
         } else if (args[0].equalsIgnoreCase("waypoint")) {
            String setName = "gui.xaero_default";
            if (args.length > 10) {
               setName = args[10];
            }

            WaypointSet waypoints = world.getWaypointSet(setName);
            if (waypoints == null) {
               world.addWaypointSet(waypoints = WaypointSet.Builder.begin().setName(setName).build());
            }

            Waypoint loadedWaypoint = new Waypoint(
               Integer.parseInt(args[4]),
               Integer.parseInt(args[5]),
               Integer.parseInt(args[6]),
               args[2].replace("§§", ":"),
               args[3].replace("§§", ":"),
               Integer.parseInt(args[7])
            );
            if (args.length > 8) {
               loadedWaypoint.setDisabled(args[8].equals("true"));
            }

            if (args.length > 9) {
               loadedWaypoint.setType(Integer.parseInt(args[9]));
            }

            if (args.length > 11) {
               loadedWaypoint.setRotation(args[11].equals("true"));
            }

            if (args.length > 12) {
               loadedWaypoint.setYaw(Integer.parseInt(args[12]));
            }

            waypoints.add(loadedWaypoint);
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean loadFromFile(MinimapSession session, Path filePath) throws IOException {
      if (!Files.exists(filePath)) {
         return false;
      } else {
         String s;
         try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            while ((s = reader.readLine()) != null) {
               String[] args = s.split(":");

               try {
                  this.checkLine(args, session);
               } catch (Exception var8) {
                  MinimapLogs.LOGGER.info("Skipping old waypoint line:" + args[0]);
               }
            }
         }

         return true;
      }
   }

   public XaeroPath convertToNewContainerID(String oldID, MinimapSession session) {
      int separatorIndex = oldID.lastIndexOf("_");
      String parentContainer = oldID.substring(0, separatorIndex);
      String dimension = oldID.substring(separatorIndex + 1);
      if (dimension.equals("null")) {
         dimension = "Overworld";
      } else if (dimension.startsWith("DIM")) {
         int dimensionId = Integer.parseInt(dimension.substring(3));
         dimension = "dim%" + dimensionId;
         class_5321<class_1937> dimRegistryKey = session.getDimensionHelper().getDimensionKeyForDirectoryName(dimension);
         if (dimRegistryKey != null) {
            dimension = session.getDimensionHelper().getDimensionDirectoryName(dimRegistryKey);
         }
      }

      return XaeroPath.root(parentContainer).resolve(this.fixOldDimensionName(dimension));
   }

   public String fixOldDimensionName(String savedDimName) {
      if (savedDimName.equals("Overworld")) {
         return "dim%0";
      } else if (savedDimName.equals("Nether")) {
         return "dim%-1";
      } else {
         return savedDimName.equals("The End") ? "dim%1" : savedDimName;
      }
   }
}
