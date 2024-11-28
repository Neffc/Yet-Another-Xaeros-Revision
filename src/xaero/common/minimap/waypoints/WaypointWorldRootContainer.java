package xaero.common.minimap.waypoints;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.class_1132;
import net.minecraft.class_1937;
import net.minecraft.class_2874;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3218;
import net.minecraft.class_5321;
import net.minecraft.class_638;
import net.minecraft.class_7134;
import net.minecraft.class_7924;
import xaero.common.IXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.XaeroMinimapSession;

public class WaypointWorldRootContainer extends WaypointWorldContainer {
   public boolean configLoaded = false;
   private boolean usingMultiworldDetection = false;
   private boolean ignoreServerLevelId = false;
   private String defaultMultiworldId;
   private boolean teleportationEnabled = true;
   private boolean usingDefaultTeleportCommand = true;
   private String serverTeleportCommandFormat;
   private String serverTeleportCommandRotationFormat;
   private WaypointsSort sortType = WaypointsSort.NONE;
   private boolean sortReversed;
   private boolean ignoreHeightmaps;
   private WaypointWorldConnectionManager subWorldConnections;
   private Map<class_5321<class_1937>, class_2960> dimensionTypeIds;
   private Map<class_5321<class_1937>, class_2874> dimensionTypes;

   public WaypointWorldRootContainer(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, String key) {
      super(modMain, minimapSession, key, null);
      this.updateConnectionsField(minimapSession);
      this.dimensionTypeIds = new HashMap<>();
      this.dimensionTypes = new HashMap<>();
   }

   public void updateConnectionsField(XaeroMinimapSession minimapSession) {
      this.subWorldConnections = minimapSession.getWaypointsManager().isMultiplayer(this.key)
         ? new WaypointWorldConnectionManager()
         : new WaypointWorldConnectionManager() {
            @Override
            public boolean isConnected(WaypointWorld world1, WaypointWorld world2) {
               return true;
            }

            @Override
            public void save(PrintWriter writer) {
            }
         };
   }

   private File getConfigFile() {
      Path directoryPath = this.getDirectory().toPath();

      try {
         if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
         }
      } catch (IOException var3) {
         MinimapLogs.LOGGER.error("suppressed exception", var3);
      }

      return directoryPath.resolve("config.txt").toFile();
   }

   public void saveConfig() {
      File configFile = this.getConfigFile();
      PrintWriter writer = null;

      try {
         writer = new PrintWriter(new FileWriter(configFile));
         writer.println("//waypoints config options");
         writer.println("usingMultiworldDetection:" + this.usingMultiworldDetection);
         writer.println("ignoreServerLevelId:" + this.ignoreServerLevelId);
         if (this.defaultMultiworldId != null) {
            writer.println("defaultMultiworldId:" + this.defaultMultiworldId);
         }

         writer.println("teleportationEnabled:" + this.teleportationEnabled);
         writer.println("usingDefaultTeleportCommand:" + this.usingDefaultTeleportCommand);
         if (this.serverTeleportCommandFormat != null) {
            writer.println("serverTeleportCommandFormat:" + this.serverTeleportCommandFormat.replace(":", "^col^"));
         }

         if (this.serverTeleportCommandRotationFormat != null) {
            writer.println("serverTeleportCommandRotationFormat:" + this.serverTeleportCommandRotationFormat.replace(":", "^col^"));
         }

         writer.println("sortType:" + this.sortType.name());
         writer.println("sortReversed:" + this.sortReversed);
         writer.println("");
         writer.println("//other config options");
         writer.println("ignoreHeightmaps:" + this.ignoreHeightmaps);
         this.subWorldConnections.save(writer);
         writer.println("");
         writer.println("//dimension types (DO NOT EDIT)");

         for (Entry<class_5321<class_1937>, class_2960> entry : this.dimensionTypeIds.entrySet()) {
            writer.println("dimensionType:" + entry.getKey().method_29177().toString().replace(':', '$') + ":" + entry.getValue().toString().replace(':', '$'));
         }
      } catch (IOException var5) {
         MinimapLogs.LOGGER.error("suppressed exception", var5);
      }

      if (writer != null) {
         writer.close();
      }
   }

   public void loadConfig() {
      this.configLoaded = true;
      File configFile = this.getConfigFile();
      if (!configFile.exists()) {
         this.saveConfig();
      } else {
         BufferedReader reader = null;

         try {
            reader = new BufferedReader(new FileReader(configFile));

            String line;
            while ((line = reader.readLine()) != null) {
               String[] args = line.split(":");
               String valueString = args.length < 2 ? "" : args[1];
               if (args[0].equals("usingMultiworldDetection")) {
                  this.usingMultiworldDetection = valueString.equals("true");
               } else if (args[0].equals("ignoreServerLevelId")) {
                  this.ignoreServerLevelId = valueString.equals("true");
               } else if (args[0].equals("defaultMultiworldId")) {
                  this.defaultMultiworldId = valueString;
               } else if (args[0].equals("teleportationEnabled")) {
                  this.teleportationEnabled = valueString.equals("true");
               } else if (args[0].equals("usingDefaultTeleportCommand")) {
                  this.usingDefaultTeleportCommand = valueString.equals("true");
               } else if (args[0].equals("teleportCommand")) {
                  this.serverTeleportCommandFormat = "/" + valueString.replace("^col^", ":") + " {x} {y} {z}";
                  this.serverTeleportCommandRotationFormat = "/" + valueString.replace("^col^", ":") + " {x} {y} {z} {yaw} ~";
               } else if (args[0].equals("serverTeleportCommand")) {
                  this.serverTeleportCommandFormat = valueString.replace("^col^", ":") + " {x} {y} {z}";
                  this.serverTeleportCommandRotationFormat = valueString.replace("^col^", ":") + " {x} {y} {z} {yaw} ~";
               } else if (args[0].equals("serverTeleportCommandFormat")) {
                  this.serverTeleportCommandFormat = valueString.replace("^col^", ":");
               } else if (args[0].equals("serverTeleportCommandRotationFormat")) {
                  this.serverTeleportCommandRotationFormat = valueString.replace("^col^", ":");
               } else if (args[0].equals("sortType")) {
                  this.sortType = WaypointsSort.valueOf(valueString);
               } else if (args[0].equals("sortReversed")) {
                  this.sortReversed = valueString.equals("true");
               } else if (args[0].equals("ignoreHeightmaps")) {
                  this.ignoreHeightmaps = valueString.equals("true");
               } else if (args[0].equals("connection")) {
                  if (args.length > 2) {
                     String worldKey2 = args[2];
                     this.subWorldConnections.addConnection(valueString, worldKey2);
                  }
               } else if (args[0].equals("dimensionType")) {
                  try {
                     this.dimensionTypeIds
                        .put(
                           class_5321.method_29179(class_7924.field_41223, new class_2960(args[1].replace('$', ':'))),
                           new class_2960(args[2].replace('$', ':'))
                        );
                  } catch (Throwable var9) {
                  }
               }
            }
         } catch (FileNotFoundException var10) {
            MinimapLogs.LOGGER.error("suppressed exception", var10);
         } catch (IOException var11) {
            MinimapLogs.LOGGER.error("suppressed exception", var11);
         }

         if (reader != null) {
            try {
               reader.close();
            } catch (IOException var8) {
               MinimapLogs.LOGGER.error("suppressed exception", var8);
            }
         }
      }
   }

   public boolean isUsingMultiworldDetection() {
      return this.usingMultiworldDetection;
   }

   public void setUsingMultiworldDetection(boolean usingMultiworldDetection) {
      this.usingMultiworldDetection = usingMultiworldDetection;
   }

   public String getDefaultMultiworldId() {
      return this.defaultMultiworldId;
   }

   public void setDefaultMultiworldId(String defaultMultiworldId) {
      this.defaultMultiworldId = defaultMultiworldId;
   }

   public boolean isTeleportationEnabled() {
      return this.teleportationEnabled;
   }

   public void setTeleportationEnabled(boolean teleportation) {
      this.teleportationEnabled = teleportation;
   }

   public boolean isUsingDefaultTeleportCommand() {
      return this.usingDefaultTeleportCommand;
   }

   public void setUsingDefaultTeleportCommand(boolean usingDefaultTeleportCommand) {
      this.usingDefaultTeleportCommand = usingDefaultTeleportCommand;
   }

   public String getServerTeleportCommandFormat() {
      return this.serverTeleportCommandFormat;
   }

   public String getServerTeleportCommandRotationFormat() {
      return this.serverTeleportCommandRotationFormat;
   }

   public void setServerTeleportCommandFormat(String serverTeleportCommandFormat) {
      this.serverTeleportCommandFormat = serverTeleportCommandFormat;
   }

   public void setServerTeleportCommandRotationFormat(String serverTeleportCommandRotationFormat) {
      this.serverTeleportCommandRotationFormat = serverTeleportCommandRotationFormat;
   }

   public WaypointsSort getSortType() {
      return this.sortType;
   }

   public void toggleSortType() {
      this.sortType = WaypointsSort.values()[(this.sortType.ordinal() + 1) % WaypointsSort.values().length];
   }

   public boolean isSortReversed() {
      return this.sortReversed;
   }

   public void toggleSortReversed() {
      this.sortReversed = !this.sortReversed;
   }

   public boolean isIgnoreServerLevelId() {
      return this.ignoreServerLevelId;
   }

   public WaypointWorldConnectionManager getSubWorldConnections() {
      return this.subWorldConnections;
   }

   public boolean isIgnoreHeightmaps() {
      return this.ignoreHeightmaps;
   }

   public void setIgnoreHeightmaps(boolean ignoreHeightmaps) {
      this.ignoreHeightmaps = ignoreHeightmaps;
   }

   public class_2874 getDimensionType(class_5321<class_1937> dimId) {
      class_2874 dimensionType = this.dimensionTypes.get(dimId);
      if (dimensionType == null) {
         class_2960 dimensionTypeId = this.dimensionTypeIds.get(dimId);
         if (dimensionTypeId == null) {
            if (dimId == class_1937.field_25180) {
               dimensionTypeId = class_7134.field_37671;
            } else if (dimId == class_1937.field_25179) {
               dimensionTypeId = class_7134.field_37670;
            } else {
               if (dimId != class_1937.field_25181) {
                  class_1132 integratedServer = class_310.method_1551().method_1576();
                  if (integratedServer == null) {
                     return null;
                  }

                  class_3218 serverLevel = integratedServer.method_3847(dimId);
                  if (serverLevel == null) {
                     return null;
                  }

                  this.dimensionTypes.put(dimId, serverLevel.method_8597());
                  return serverLevel.method_8597();
               }

               dimensionTypeId = class_7134.field_37672;
            }
         }

         dimensionType = (class_2874)class_310.method_1551().field_1687.method_30349().method_30530(class_7924.field_41241).method_10223(dimensionTypeId);
         if (dimensionType != null) {
            this.dimensionTypes.put(dimId, dimensionType);
         }
      }

      return dimensionType;
   }

   public double getDimensionScale(class_5321<class_1937> dimId) {
      class_2874 dimType = this.getDimensionType(dimId);
      return dimType == null ? 1.0 : dimType.comp_646();
   }

   public void updateDimensionType(class_638 level) {
      class_5321<class_1937> dimId = level.method_27983();
      class_5321<class_2874> dimTypeId = level.method_44013();
      class_2874 dimType = level.method_8597();
      if (!Objects.equals(this.dimensionTypeIds.get(dimId), dimTypeId.method_29177())) {
         this.dimensionTypes.put(dimId, dimType);
         this.dimensionTypeIds.put(dimId, dimTypeId.method_29177());
         this.saveConfig();
      }
   }

   @Override
   public WaypointWorldRootContainer getRootContainer() {
      return this;
   }
}
