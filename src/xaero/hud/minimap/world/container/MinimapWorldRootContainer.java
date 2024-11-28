package xaero.hud.minimap.world.container;

import java.io.IOException;
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
import xaero.common.HudMod;
import xaero.common.file.SimpleBackup;
import xaero.common.minimap.waypoints.WaypointWorldContainer;
import xaero.common.minimap.waypoints.WaypointWorldRootContainer;
import xaero.common.minimap.waypoints.WaypointsSort;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointSession;
import xaero.hud.minimap.world.connection.MinimapWorldConnectionManager;
import xaero.hud.minimap.world.container.config.RootConfig;
import xaero.hud.path.XaeroPath;

public class MinimapWorldRootContainer extends WaypointWorldContainer {
   private final RootConfig config;
   private final Map<class_5321<class_1937>, class_2960> dimensionTypeIds;
   private final Map<class_5321<class_1937>, class_2874> dimensionTypes;

   protected MinimapWorldRootContainer(HudMod modMain, MinimapSession session, XaeroPath path) {
      super(modMain, session, path, null);
      this.config = new RootConfig(MinimapWorldContainerUtil.isMultiplayer(path));
      this.dimensionTypeIds = new HashMap<>();
      this.dimensionTypes = new HashMap<>();
   }

   public void updateConnectionsField(WaypointSession session) {
      this.config.resetSubWorldConnections(MinimapWorldContainerUtil.isMultiplayer(this.path));
   }

   public MinimapWorldConnectionManager getSubWorldConnections() {
      return this.config.getSubWorldConnections();
   }

   public class_2874 getDimensionType(class_5321<class_1937> dimId) {
      class_2874 dimensionType = this.dimensionTypes.get(dimId);
      if (dimensionType != null) {
         return dimensionType;
      } else {
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

         return dimensionType;
      }
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
         this.session.getWorldManagerIO().getRootConfigIO().save(this);
      }
   }

   public void renameOldContainer(XaeroPath containerPath) {
      if (!this.subContainers.isEmpty()) {
         String dimensionPart = containerPath.getAtIndex(1).getLastNode();
         if (!this.subContainers.containsKey(dimensionPart)) {
            class_5321<class_1937> dimId = this.session.getDimensionHelper().getDimensionKeyForDirectoryName(dimensionPart);
            if (dimId != null) {
               class_2960 dimKey = dimId.method_29177();
               String dimKeyOldValidation = dimKey.method_12832().replaceAll("[^a-zA-Z0-9_]+", "");
               XaeroPath customWorldPath = this.session.getWorldState().getCustomWorldPath();
               MinimapWorldContainer currentCustomContainer = customWorldPath == null
                  ? null
                  : this.session.getWorldManager().getWorld(customWorldPath).getContainer();

               for (Entry<String, MinimapWorldContainer> subContainerEntry : this.subContainers.entrySet()) {
                  String subKey = subContainerEntry.getKey();
                  if (subKey.equals(dimKeyOldValidation)) {
                     MinimapWorldContainer dimContainer = subContainerEntry.getValue();
                     boolean currentlySelected = currentCustomContainer != null && currentCustomContainer.getPath().isSubOf(dimContainer.getPath());
                     this.subContainers.put(dimensionPart, dimContainer);
                     this.subContainers.remove(subKey);
                     SimpleBackup.moveToBackup(dimContainer.getDirectoryPath());
                     dimContainer.setPath(this.path.resolve(dimensionPart));
                     if (currentlySelected) {
                        this.session.getWorldState().setCustomWorldPath(dimContainer.getPath().resolve(customWorldPath.getSubPath(2)));
                     }

                     try {
                        this.session.getWorldManagerIO().saveWorlds(this);
                     } catch (IOException var14) {
                        throw new RuntimeException("Failed to rename a dimension! Can't continue.", var14);
                     }

                     MinimapWorldConnectionManager connections = this.getSubWorldConnections();
                     connections.renameDimension(subKey, dimensionPart);
                     this.session.getWorldManagerIO().getRootConfigIO().save(this);
                     return;
                  }
               }
            }
         }
      }
   }

   public Iterable<Entry<class_5321<class_1937>, class_2960>> getDimensionTypeIds() {
      return this.dimensionTypeIds.entrySet();
   }

   public void setDimensionTypeId(class_5321<class_1937> dim, class_2960 dimType) {
      this.dimensionTypes.remove(dim);
      this.dimensionTypeIds.put(dim, dimType);
   }

   @Override
   public MinimapWorldRootContainer getRoot() {
      return this;
   }

   public boolean isConfigLoaded() {
      return this.config.isLoaded();
   }

   public RootConfig getConfig() {
      return this.config;
   }

   @Deprecated
   public boolean isUsingMultiworldDetection() {
      return this.config.isUsingMultiworldDetection();
   }

   @Deprecated
   public void setUsingMultiworldDetection(boolean usingMultiworldDetection) {
      this.config.setUsingMultiworldDetection(usingMultiworldDetection);
   }

   @Deprecated
   public String getDefaultMultiworldId() {
      return this.config.getDefaultMultiworldId();
   }

   @Deprecated
   public void setDefaultMultiworldId(String defaultMultiworldId) {
      this.config.setDefaultMultiworldId(defaultMultiworldId);
   }

   @Deprecated
   public boolean isTeleportationEnabled() {
      return this.config.isTeleportationEnabled();
   }

   @Deprecated
   public void setTeleportationEnabled(boolean teleportation) {
      this.config.setTeleportationEnabled(teleportation);
   }

   @Deprecated
   public boolean isUsingDefaultTeleportCommand() {
      return this.config.isUsingDefaultTeleportCommand();
   }

   @Deprecated
   public void setUsingDefaultTeleportCommand(boolean usingDefaultTeleportCommand) {
      this.config.setUsingDefaultTeleportCommand(usingDefaultTeleportCommand);
   }

   @Deprecated
   public String getServerTeleportCommandFormat() {
      return this.config.getServerTeleportCommandFormat();
   }

   @Deprecated
   public String getServerTeleportCommandRotationFormat() {
      return this.config.getServerTeleportCommandRotationFormat();
   }

   @Deprecated
   public void setServerTeleportCommandFormat(String serverTeleportCommandFormat) {
      this.config.setServerTeleportCommandFormat(serverTeleportCommandFormat);
   }

   @Deprecated
   public void setServerTeleportCommandRotationFormat(String serverTeleportCommandRotationFormat) {
      this.config.setServerTeleportCommandRotationFormat(serverTeleportCommandRotationFormat);
   }

   @Deprecated
   public WaypointsSort getSortType() {
      return this.config.getSortType();
   }

   @Deprecated
   public void setSortType(WaypointsSort sortType) {
      this.config.setSortType(sortType);
   }

   @Deprecated
   public void toggleSortType() {
      this.config.toggleSortType();
   }

   @Deprecated
   public boolean isSortReversed() {
      return this.config.isSortReversed();
   }

   @Deprecated
   public void setSortReversed(boolean sortReversed) {
      this.config.setSortReversed(sortReversed);
   }

   @Deprecated
   public void toggleSortReversed() {
      this.config.toggleSortReversed();
   }

   @Deprecated
   public boolean isIgnoreServerLevelId() {
      return this.config.isIgnoreServerLevelId();
   }

   @Deprecated
   public void setIgnoreServerLevelId(boolean ignoreServerLevelId) {
      this.config.setIgnoreServerLevelId(ignoreServerLevelId);
   }

   @Deprecated
   public boolean isIgnoreHeightmaps() {
      return this.config.isIgnoreHeightmaps();
   }

   @Deprecated
   public void setIgnoreHeightmaps(boolean ignoreHeightmaps) {
      this.config.setIgnoreHeightmaps(ignoreHeightmaps);
   }

   @Override
   public boolean isEmpty() {
      return super.isEmpty();
   }

   @Override
   public String getName(String worldNode) {
      return super.getName(worldNode);
   }

   @Override
   public void removeName(String worldNode) {
      super.removeName(worldNode);
   }

   @Override
   public String getSubName() {
      return super.getSubName();
   }

   public static final class Builder {
      private HudMod modMain;
      private MinimapSession session;
      private XaeroPath path;

      private Builder() {
      }

      public MinimapWorldRootContainer.Builder setDefault() {
         this.setModMain(null);
         this.setSession(null);
         this.setPath(null);
         return this;
      }

      public MinimapWorldRootContainer.Builder setModMain(HudMod modMain) {
         this.modMain = modMain;
         return this;
      }

      public MinimapWorldRootContainer.Builder setSession(MinimapSession session) {
         this.session = session;
         return this;
      }

      public MinimapWorldRootContainer.Builder setPath(XaeroPath path) {
         this.path = path;
         return this;
      }

      public MinimapWorldRootContainer build() {
         if (this.modMain != null && this.session != null && this.path != null) {
            return new WaypointWorldRootContainer(this.modMain, this.session, this.path);
         } else {
            throw new IllegalStateException();
         }
      }

      public static MinimapWorldRootContainer.Builder begin() {
         return new MinimapWorldRootContainer.Builder().setDefault();
      }
   }
}
