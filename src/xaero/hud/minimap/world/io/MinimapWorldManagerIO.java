package xaero.hud.minimap.world.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.minecraft.class_1937;
import net.minecraft.class_5321;
import net.minecraft.class_634;
import xaero.common.HudMod;
import xaero.common.file.SimpleBackup;
import xaero.common.misc.Misc;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.io.WaypointIO;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.minimap.world.container.io.RootConfigIO;
import xaero.hud.path.XaeroPath;

public class MinimapWorldManagerIO {
   private final HudMod modMain;
   private final RootConfigIO rootConfigIO;
   private final WaypointIO waypointIO;
   private final Pattern backupFilePattern;

   public MinimapWorldManagerIO(HudMod modMain) {
      this.modMain = modMain;
      this.rootConfigIO = new RootConfigIO(modMain);
      this.waypointIO = new WaypointIO(modMain);
      this.backupFilePattern = Pattern.compile("^backup-*$");
   }

   public void loadWorldsFromAllSources(MinimapSession session, class_634 connection) throws IOException {
      this.fixOldRootFolder(session);
      boolean shouldResave = this.waypointIO.getOldIO().load(session);
      this.loadAllWorlds(session);
      if (shouldResave) {
         this.saveAllWorlds(session);
      }
   }

   public void loadAllWorlds(MinimapSession session) throws IOException {
      Path minimapFolderPath = this.modMain.getMinimapFolder();
      if (!Files.exists(minimapFolderPath)) {
         Files.createDirectories(minimapFolderPath);
      }

      Path minimapTempToAddFolder = minimapFolderPath.resolve("temp_to_add");
      if (Files.exists(minimapTempToAddFolder)) {
         copyTempFilesBack(minimapTempToAddFolder);
      }

      this.convertWorldDimFilesToFolders();
      this.convertWorldDimFoldersToSingleFolder(session);
      Stream<Path> rootFiles = Files.list(minimapFolderPath);
      if (rootFiles != null) {
         Iterator<Path> rootIterator = rootFiles.iterator();

         while (rootIterator.hasNext()) {
            Path rootFilePath = rootIterator.next();
            if (Files.isDirectory(rootFilePath)) {
               String rootFolderName = rootFilePath.getFileName().toString();
               if (!this.backupFilePattern.matcher(rootFolderName).find()) {
                  this.loadWorldFolder(rootFilePath, rootFolderName, session);
               }
            }
         }

         rootFiles.close();
      }
   }

   private void loadWorldFolder(Path folder, String rootFolderName, MinimapSession session) throws IOException {
      Path tempToAdd = folder.resolve("temp_to_add");
      if (Files.exists(tempToAdd)) {
         copyTempFilesBack(tempToAdd);
      }

      Stream<Path> worldFiles = Files.list(folder);
      if (worldFiles != null) {
         XaeroPath rootPath = XaeroPath.root(rootFolderName);
         Iterator<Path> worldFileIterator = worldFiles.iterator();

         while (worldFileIterator.hasNext()) {
            Path worldFile = worldFileIterator.next();
            String worldFileName = worldFile.getFileName().toString();
            if (!this.backupFilePattern.matcher(worldFileName).find()) {
               if (!Files.isDirectory(worldFile)) {
                  if (worldFileName.contains("_")) {
                     MinimapWorldContainer container = session.getWorldManager().addWorldContainer(rootPath);
                     this.loadWorldFile(container, worldFileName, null);
                  }
               } else {
                  this.loadDimensionFolder(worldFileName, worldFile, rootFolderName, session);
               }
            }
         }

         if (session.getWorldManager().getWorldContainer(rootPath).isEmpty()) {
            session.getWorldManager().removeContainer(rootPath);
         }

         worldFiles.close();
      }
   }

   private void loadDimensionFolder(String dimensionName, Path folder, String rootFolderName, MinimapSession session) throws IOException {
      Path tempToAdd2 = folder.resolve("temp_to_add");
      if (Files.exists(tempToAdd2)) {
         copyTempFilesBack(tempToAdd2);
      }

      String fixedDimensionName = this.waypointIO.getOldIO().fixOldDimensionName(dimensionName);
      XaeroPath containerKey = XaeroPath.root(rootFolderName).resolve(fixedDimensionName);
      MinimapWorldContainer container = session.getWorldManager().addWorldContainer(containerKey);
      Stream<Path> dimensionFiles = Files.list(folder);
      if (dimensionFiles != null) {
         Iterator<Path> dimensionFileIterator = dimensionFiles.iterator();

         while (dimensionFileIterator.hasNext()) {
            Path dimensionFile = dimensionFileIterator.next();
            String fileName = dimensionFile.getFileName().toString();
            this.loadWorldFile(container, fileName, dimensionFile);
         }

         dimensionFiles.close();
      }

      if (container.isEmpty()) {
         session.getWorldManager().removeContainer(containerKey);
      }

      if (!fixedDimensionName.equals(dimensionName)) {
         SimpleBackup.moveToBackup(folder);
         this.saveWorlds(container);
      }
   }

   public boolean loadWorldFile(MinimapWorldContainer container, String fileName, Path filePath) throws IOException {
      if (!fileName.endsWith(".txt")) {
         return false;
      } else {
         String noExtension = fileName.substring(0, fileName.lastIndexOf("."));
         String multiworldId = noExtension;
         if (!noExtension.equals("waypoints")) {
            String[] multiworld = noExtension.split("_");
            if (multiworld.length < 2) {
               return false;
            }

            multiworldId = multiworld[0];
            String multiworldName = multiworld[1].replace("%us%", "_");
            container.setName(multiworldId, multiworldName);
         }

         MinimapWorld world = container.addWorld(multiworldId);
         if (world != null) {
            this.loadWorld(world, filePath);
         }

         return true;
      }
   }

   public void loadWorld(MinimapWorld world, Path filePath) throws IOException {
      if (filePath == null) {
         filePath = this.getWorldFile(world);
      }

      if (Files.exists(filePath)) {
         String s;
         try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.toFile()), "UTF8"))) {
            while ((s = reader.readLine()) != null) {
               String[] args = s.split(":");

               try {
                  this.checkWorldFileLine(args, world);
               } catch (Throwable var8) {
                  MinimapLogs.LOGGER.error("Skipping minimap world file line:" + Arrays.toString((Object[])args), var8);
               }
            }
         }
      }
   }

   public boolean checkWorldFileLine(String[] args, MinimapWorld world) {
      return this.waypointIO.checkLine(args, world);
   }

   public void saveWorlds(MinimapWorldContainer container) throws IOException {
      for (MinimapWorld world : container.getAllWorldsIterable()) {
         this.saveWorld(world);
      }
   }

   public void saveAllWorlds(MinimapSession session) throws IOException {
      for (MinimapWorldRootContainer rootContainer : session.getWorldManager().getRootContainers()) {
         this.saveWorlds(rootContainer);
      }
   }

   public void saveWorld(MinimapWorld wpw) throws IOException {
      this.saveWorld(wpw, true);
   }

   public void saveWorld(MinimapWorld world, boolean overwrite) throws IOException {
      if (world != null) {
         Path worldFilePath = this.getWorldFile(world);
         if (!Files.exists(worldFilePath) || overwrite) {
            Path worldFileTempPath = worldFilePath.getParent().resolve(worldFilePath.getFileName() + ".temp");
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(worldFileTempPath.toFile()));

            try (OutputStreamWriter output = new OutputStreamWriter(bufferedOutput, StandardCharsets.UTF_8)) {
               this.waypointIO.saveWaypoints(world, output);
            } catch (Throwable var12) {
               try {
                  bufferedOutput.close();
               } catch (Throwable var9) {
                  var12.addSuppressed(var9);
               }

               throw var12;
            }

            bufferedOutput.close();
            Misc.safeMoveAndReplace(worldFileTempPath, worldFilePath, true);
            if (world.hasSomethingToRemoveOnSave()) {
               world.cleanupOnSave(worldFilePath);
            }
         }
      }
   }

   public void convertWorldDimFilesToFolders() throws IOException {
      Stream<Path> files = Files.list(this.modMain.getMinimapFolder());
      Path backupFolder = this.modMain.getMinimapFolder().resolve("backup");
      Files.createDirectories(backupFolder);
      if (files != null) {
         Iterator<Path> rootFileIterator = files.iterator();

         while (rootFileIterator.hasNext()) {
            Path rootFilePath = rootFileIterator.next();
            if (!Files.isDirectory(rootFilePath)) {
               String fileName = rootFilePath.getFileName().toString();
               if (fileName.endsWith(".txt") && fileName.contains("_")) {
                  int lastUnderscore = fileName.lastIndexOf("_");
                  if (!fileName.startsWith("Multiplayer_") && !fileName.startsWith("Realms_")) {
                     fileName = fileName.substring(0, lastUnderscore).replace("_", "%us%") + fileName.substring(lastUnderscore);
                  }

                  String noExtension = fileName.substring(0, fileName.lastIndexOf("."));
                  Path folderPath = rootFilePath.getParent().resolve(noExtension);
                  Path fixedFilePath = folderPath.resolve("waypoints.txt");
                  Path backupFilePath = backupFolder.resolve(fileName);
                  if (!Files.exists(folderPath)) {
                     Files.createDirectories(folderPath);
                  }

                  if (!Files.exists(backupFilePath)) {
                     Files.copy(rootFilePath, backupFilePath);
                  }

                  try {
                     Files.move(rootFilePath, fixedFilePath);
                  } catch (FileAlreadyExistsException var12) {
                     if (Files.exists(backupFilePath)) {
                        Files.deleteIfExists(rootFilePath);
                     }
                  }
               }
            }
         }

         files.close();
      }
   }

   public void convertWorldDimFoldersToSingleFolder(MinimapSession session) throws IOException {
      Stream<Path> files = Files.list(this.modMain.getMinimapFolder());
      if (files != null) {
         Iterator<Path> rootFileIterator = files.iterator();

         while (rootFileIterator.hasNext()) {
            Path rootFilePath = rootFileIterator.next();
            if (Files.isDirectory(rootFilePath)) {
               String folderName = rootFilePath.getFileName().toString();
               String[] folderArgs = folderName.split("_");
               if (folderArgs.length > 2 || folderArgs.length == 2 && !folderArgs[0].equals("Multiplayer")) {
                  String lastArg = folderArgs[folderArgs.length - 1];
                  if (lastArg.equals("null") || lastArg.startsWith("DIM") && lastArg.length() != 3) {
                     int dimensionId = lastArg.equals("null") ? 0 : Integer.parseInt(lastArg.substring(3));
                     String dimensionName = "dim%" + dimensionId;
                     class_5321<class_1937> dimRegistryKey = session.getDimensionHelper().getDimensionKeyForDirectoryName(dimensionName);
                     if (dimRegistryKey != null) {
                        dimensionName = session.getDimensionHelper().getDimensionDirectoryName(dimRegistryKey);
                     }

                     Path correctDimensionFolder = rootFilePath.getParent()
                        .resolve(folderName.substring(0, folderName.lastIndexOf("_")))
                        .resolve(dimensionName);
                     if (!Files.exists(correctDimensionFolder)) {
                        Files.createDirectories(correctDimensionFolder);
                     }

                     Stream<Path> dimensionFiles = Files.list(rootFilePath);
                     if (dimensionFiles != null) {
                        Iterator<Path> dimensionFileIterator = dimensionFiles.iterator();

                        while (dimensionFileIterator.hasNext()) {
                           Path dimensionFilePath = dimensionFileIterator.next();
                           if (!Files.isDirectory(dimensionFilePath)) {
                              Path correctFilePath = correctDimensionFolder.resolve(dimensionFilePath.getFileName());
                              Files.move(dimensionFilePath, correctFilePath);
                           }
                        }

                        dimensionFiles.close();
                     }

                     Stream<Path> deleteCheck = Files.list(rootFilePath);
                     if (deleteCheck != null) {
                        boolean oldFolderEmpty = deleteCheck.count() == 0L;
                        deleteCheck.close();
                        if (oldFolderEmpty) {
                           Files.deleteIfExists(rootFilePath);
                        }
                     }
                  }
               }
            }
         }

         files.close();
      }
   }

   public static void copyTempFilesBack(Path folder) throws IOException {
      Stream<Path> tempFiles = Files.list(folder);
      if (tempFiles != null) {
         Iterator<Path> tempFilesIterator = tempFiles.iterator();

         while (tempFilesIterator.hasNext()) {
            Path tempFile = tempFilesIterator.next();
            Path newLocation = folder.getParent().resolve(tempFile.getFileName());
            if (Files.exists(newLocation) && (Files.isDirectory(newLocation) || Files.size(newLocation) != 0L)) {
               SimpleBackup.moveToBackup(folder.getParent(), tempFile);
            } else {
               Misc.safeMoveAndReplace(tempFile, newLocation, false);
            }
         }

         tempFiles.close();
      }

      Files.delete(folder);
   }

   private void fixOldRootFolder(MinimapSession session) throws IOException {
      XaeroPath autoRootContainerPath = session.getWorldState().getAutoRootContainerPath();

      for (int format = 1; format >= 0; format--) {
         this.fixOldRootFolder(autoRootContainerPath, session.getWorldState().getOutdatedAutoRootContainerPath(format));
      }
   }

   private void fixOldRootFolder(XaeroPath path, XaeroPath outdatedPath) throws IOException {
      if (!path.equals(outdatedPath)) {
         Path oldFormatRootFolder = outdatedPath.applyToFilePath(this.modMain.getMinimapFolder());
         if (Files.exists(oldFormatRootFolder)) {
            Path fixedFolder = path.applyToFilePath(this.modMain.getMinimapFolder());
            if (!Files.exists(fixedFolder)) {
               Files.move(oldFormatRootFolder, fixedFolder);
            }
         }
      }
   }

   public void onRootContainerAdded(MinimapWorldRootContainer rootContainer) {
      if (!rootContainer.isConfigLoaded()) {
         this.rootConfigIO.load(rootContainer);
      }
   }

   public Path getWorldFile(MinimapWorld w) throws IOException {
      Path containerFolderPath = w.getContainer().getDirectoryPath();
      if (!Files.exists(containerFolderPath)) {
         Files.createDirectories(containerFolderPath);
      }

      String fileName = w.getNode();
      String worldName = w.getContainer().getName(w.getNode());
      if (worldName != null) {
         fileName = fileName + "_" + worldName.replace("_", "%us%").replace(":", "§§");
      }

      fileName = fileName + ".txt";
      return containerFolderPath.resolve(fileName);
   }

   public RootConfigIO getRootConfigIO() {
      return this.rootConfigIO;
   }
}
