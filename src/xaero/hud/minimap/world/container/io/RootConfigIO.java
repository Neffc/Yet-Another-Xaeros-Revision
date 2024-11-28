package xaero.hud.minimap.world.container.io;

import it.unimi.dsi.fastutil.ints.IntIterator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;
import net.minecraft.class_1937;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import net.minecraft.class_7924;
import xaero.common.HudMod;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsSort;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;
import xaero.hud.path.XaeroPathReader;

public class RootConfigIO {
   private final HudMod modMain;

   public RootConfigIO(HudMod modMain) {
      this.modMain = modMain;
   }

   private File getFile(MinimapWorldRootContainer rootContainer) {
      Path directoryPath = rootContainer.getDirectoryPath();

      try {
         if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
         }
      } catch (IOException var4) {
         MinimapLogs.LOGGER.error("suppressed exception", var4);
      }

      return directoryPath.resolve("config.txt").toFile();
   }

   public void save(MinimapWorldRootContainer rootContainer) {
      File configFile = this.getFile(rootContainer);
      PrintWriter writer = null;

      try {
         writer = new PrintWriter(new FileWriter(configFile));
         writer.println("//waypoints config options");
         writer.println("usingMultiworldDetection:" + rootContainer.isUsingMultiworldDetection());
         writer.println("ignoreServerLevelId:" + rootContainer.isIgnoreServerLevelId());
         if (rootContainer.getDefaultMultiworldId() != null) {
            writer.println("defaultMultiworldId:" + rootContainer.getDefaultMultiworldId());
         }

         writer.println("teleportationEnabled:" + rootContainer.isTeleportationEnabled());
         writer.println("usingDefaultTeleportCommand:" + rootContainer.isUsingDefaultTeleportCommand());
         if (rootContainer.getServerTeleportCommandFormat() != null) {
            writer.println("serverTeleportCommandFormat:" + rootContainer.getServerTeleportCommandFormat().replace(":", "^col^"));
         }

         if (rootContainer.getServerTeleportCommandRotationFormat() != null) {
            writer.println("serverTeleportCommandRotationFormat:" + rootContainer.getServerTeleportCommandRotationFormat().replace(":", "^col^"));
         }

         writer.println("sortType:" + rootContainer.getSortType().name());
         writer.println("sortReversed:" + rootContainer.isSortReversed());
         writer.println("");
         writer.println("//other config options");
         writer.println("ignoreHeightmaps:" + rootContainer.isIgnoreHeightmaps());
         rootContainer.getSubWorldConnections().save(writer);
         writer.println("");
         writer.println("//dimension types (DO NOT EDIT)");

         for (Entry<class_5321<class_1937>, class_2960> entry : rootContainer.getDimensionTypeIds()) {
            writer.println("dimensionType:" + entry.getKey().method_29177().toString().replace(':', '$') + ":" + entry.getValue().toString().replace(':', '$'));
         }

         writer.println("//server waypoints");

         for (MinimapWorldContainer dimContainer : rootContainer.getSubContainers()) {
            IntIterator var6 = dimContainer.getServerWaypointManager().getIds().iterator();

            while (var6.hasNext()) {
               int serverWaypointId = (Integer)var6.next();
               Waypoint serverWaypoint = dimContainer.getServerWaypointManager().getById(serverWaypointId);
               writer.print("server-waypoint");
               writer.print(":");
               writer.print(dimContainer.getLastNode());
               writer.print(":");
               writer.println(serverWaypointId);
               writer.print(":");
               writer.println(serverWaypoint.isDisabled());
            }
         }
      } catch (IOException var9) {
         MinimapLogs.LOGGER.error("suppressed exception", var9);
      }

      if (writer != null) {
         writer.close();
      }
   }

   public void load(MinimapWorldRootContainer rootContainer) {
      rootContainer.confirmConfigLoad();
      File configFile = this.getFile(rootContainer);
      if (!configFile.exists()) {
         this.save(rootContainer);
      } else {
         BufferedReader reader = null;

         try {
            reader = new BufferedReader(new FileReader(configFile));

            String line;
            while ((line = reader.readLine()) != null) {
               String[] args = line.split(":");
               String valueString = args.length < 2 ? "" : args[1];
               if (args[0].equals("usingMultiworldDetection")) {
                  rootContainer.setUsingMultiworldDetection(valueString.equals("true"));
               } else if (args[0].equals("ignoreServerLevelId")) {
                  rootContainer.setIgnoreServerLevelId(valueString.equals("true"));
               } else if (args[0].equals("defaultMultiworldId")) {
                  rootContainer.setDefaultMultiworldId(valueString);
               } else if (args[0].equals("teleportationEnabled")) {
                  rootContainer.setTeleportationEnabled(valueString.equals("true"));
               } else if (args[0].equals("usingDefaultTeleportCommand")) {
                  rootContainer.setUsingDefaultTeleportCommand(valueString.equals("true"));
               } else if (args[0].equals("teleportCommand")) {
                  rootContainer.setServerTeleportCommandFormat("/" + valueString.replace("^col^", ":") + " {x} {y} {z}");
                  rootContainer.setServerTeleportCommandRotationFormat("/" + valueString.replace("^col^", ":") + " {x} {y} {z} {yaw} ~");
               } else if (args[0].equals("serverTeleportCommand")) {
                  rootContainer.setServerTeleportCommandFormat(valueString.replace("^col^", ":") + " {x} {y} {z}");
                  rootContainer.setServerTeleportCommandRotationFormat(valueString.replace("^col^", ":") + " {x} {y} {z} {yaw} ~");
               } else if (args[0].equals("serverTeleportCommandFormat")) {
                  rootContainer.setServerTeleportCommandFormat(valueString.replace("^col^", ":"));
               } else if (args[0].equals("serverTeleportCommandRotationFormat")) {
                  rootContainer.setServerTeleportCommandRotationFormat(valueString.replace("^col^", ":"));
               } else if (args[0].equals("sortType")) {
                  rootContainer.setSortType(WaypointsSort.valueOf(valueString));
               } else if (args[0].equals("sortReversed")) {
                  rootContainer.setSortReversed(valueString.equals("true"));
               } else if (args[0].equals("ignoreHeightmaps")) {
                  rootContainer.setIgnoreHeightmaps(valueString.equals("true"));
               } else if (args[0].equals("connection")) {
                  XaeroPath worldKey1 = new XaeroPathReader().read(valueString);
                  if (args.length > 2) {
                     XaeroPath worldKey2 = new XaeroPathReader().read(args[2]);
                     rootContainer.getSubWorldConnections().addConnection(worldKey1, worldKey2);
                  }
               } else if (args[0].equals("dimensionType")) {
                  try {
                     rootContainer.setDimensionTypeId(
                        class_5321.method_29179(class_7924.field_41223, new class_2960(args[1].replace('$', ':'))), new class_2960(args[2].replace('$', ':'))
                     );
                  } catch (Throwable var11) {
                  }
               } else if (args[0].equals("server-waypoint")) {
                  String dimensionNode = args[1];
                  MinimapWorldContainer dimContainer = rootContainer.addSubContainer(rootContainer.getPath().resolve(dimensionNode));
                  boolean disabled = args[3].equals("true");
                  if (disabled) {
                     dimContainer.getServerWaypointManager().addDisabled(Integer.parseInt(args[2]));
                  }
               }
            }
         } catch (IOException var12) {
            MinimapLogs.LOGGER.error("suppressed exception", var12);
         }

         if (reader != null) {
            try {
               reader.close();
            } catch (IOException var10) {
               MinimapLogs.LOGGER.error("suppressed exception", var10);
            }
         }
      }
   }
}
