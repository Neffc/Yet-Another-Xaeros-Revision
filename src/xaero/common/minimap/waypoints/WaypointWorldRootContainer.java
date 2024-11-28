package xaero.common.minimap.waypoints;

import net.minecraft.class_1937;
import net.minecraft.class_2874;
import net.minecraft.class_5321;
import net.minecraft.class_638;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;

@Deprecated
public class WaypointWorldRootContainer extends MinimapWorldRootContainer {
   @Deprecated
   public WaypointWorldRootContainer(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, String key) {
      super((HudMod)modMain, minimapSession.getWaypointsManager(), XaeroPath.root(key));
   }

   @Deprecated
   public WaypointWorldRootContainer(HudMod modMain, MinimapSession session, XaeroPath path) {
      super(modMain, session, path);
   }

   @Deprecated
   public void updateConnectionsField(XaeroMinimapSession minimapSession) {
      super.updateConnectionsField(minimapSession.getWaypointsManager().getWaypointSession());
   }

   @Deprecated
   public void saveConfig() {
      super.getSession().getWorldManagerIO().getRootConfigIO().save(this);
   }

   @Deprecated
   public void loadConfig() {
      super.getSession().getWorldManagerIO().getRootConfigIO().load(this);
   }

   @Deprecated
   @Override
   public boolean isUsingMultiworldDetection() {
      return super.isUsingMultiworldDetection();
   }

   @Deprecated
   @Override
   public void setUsingMultiworldDetection(boolean usingMultiworldDetection) {
      super.setUsingMultiworldDetection(usingMultiworldDetection);
   }

   @Deprecated
   @Override
   public String getDefaultMultiworldId() {
      return super.getDefaultMultiworldId();
   }

   @Deprecated
   @Override
   public void setDefaultMultiworldId(String defaultMultiworldId) {
      super.setDefaultMultiworldId(defaultMultiworldId);
   }

   @Deprecated
   @Override
   public boolean isTeleportationEnabled() {
      return super.isTeleportationEnabled();
   }

   @Deprecated
   @Override
   public void setTeleportationEnabled(boolean teleportation) {
      super.setTeleportationEnabled(teleportation);
   }

   @Deprecated
   @Override
   public boolean isUsingDefaultTeleportCommand() {
      return super.isUsingDefaultTeleportCommand();
   }

   @Deprecated
   @Override
   public void setUsingDefaultTeleportCommand(boolean usingDefaultTeleportCommand) {
      super.setUsingDefaultTeleportCommand(usingDefaultTeleportCommand);
   }

   @Deprecated
   @Override
   public String getServerTeleportCommandFormat() {
      return super.getServerTeleportCommandFormat();
   }

   @Deprecated
   @Override
   public String getServerTeleportCommandRotationFormat() {
      return super.getServerTeleportCommandRotationFormat();
   }

   @Deprecated
   @Override
   public void setServerTeleportCommandFormat(String serverTeleportCommandFormat) {
      super.setServerTeleportCommandFormat(serverTeleportCommandFormat);
   }

   @Deprecated
   @Override
   public void setServerTeleportCommandRotationFormat(String serverTeleportCommandRotationFormat) {
      super.setServerTeleportCommandRotationFormat(serverTeleportCommandRotationFormat);
   }

   @Deprecated
   @Override
   public WaypointsSort getSortType() {
      return super.getSortType();
   }

   @Deprecated
   @Override
   public void toggleSortType() {
      super.toggleSortType();
   }

   @Deprecated
   @Override
   public boolean isSortReversed() {
      return super.isSortReversed();
   }

   @Deprecated
   @Override
   public void toggleSortReversed() {
      super.toggleSortReversed();
   }

   @Deprecated
   @Override
   public boolean isIgnoreServerLevelId() {
      return super.isIgnoreServerLevelId();
   }

   @Deprecated
   public WaypointWorldConnectionManager getSubWorldConnections() {
      return (WaypointWorldConnectionManager)super.getSubWorldConnections();
   }

   @Deprecated
   @Override
   public boolean isIgnoreHeightmaps() {
      return super.isIgnoreHeightmaps();
   }

   @Deprecated
   @Override
   public void setIgnoreHeightmaps(boolean ignoreHeightmaps) {
      super.setIgnoreHeightmaps(ignoreHeightmaps);
   }

   @Deprecated
   @Override
   public class_2874 getDimensionType(class_5321<class_1937> dimId) {
      return super.getDimensionType(dimId);
   }

   @Deprecated
   @Override
   public double getDimensionScale(class_5321<class_1937> dimId) {
      return super.getDimensionScale(dimId);
   }

   @Deprecated
   @Override
   public void updateDimensionType(class_638 level) {
      super.updateDimensionType(level);
   }

   @Deprecated
   @Override
   public WaypointWorldRootContainer getRootContainer() {
      return this;
   }
}
