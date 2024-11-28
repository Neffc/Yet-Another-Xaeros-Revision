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
import xaero.common.AXaeroMinimap;
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

   public WaypointWorldRootContainer(AXaeroMinimap modMain, XaeroMinimapSession minimapSession, String key) {
      super(modMain, minimapSession, key);
      this.updateConnectionsField(minimapSession);
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
      } catch (IOException var4) {
         MinimapLogs.LOGGER.error("suppressed exception", var4);
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
               } else if (args[0].equals("connection") && args.length > 2) {
                  String worldKey2 = args[2];
                  this.subWorldConnections.addConnection(valueString, worldKey2);
               }
            }
         } catch (FileNotFoundException var9) {
            MinimapLogs.LOGGER.error("suppressed exception", var9);
         } catch (IOException var10) {
            MinimapLogs.LOGGER.error("suppressed exception", var10);
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
}
