package xaero.common.minimap.waypoints;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;
import xaero.hud.path.XaeroPathReader;

@Deprecated
public class WaypointWorldContainer extends MinimapWorldContainer {
   private static final XaeroPathReader pathReader = new XaeroPathReader();

   @Deprecated
   public WaypointWorldContainer(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, String key, WaypointWorldRootContainer rootContainer) {
      this((HudMod)modMain, minimapSession.getWaypointsManager(), pathReader.read(key), rootContainer);
   }

   @Deprecated
   public WaypointWorldContainer(HudMod modMain, MinimapSession session, XaeroPath path, MinimapWorldRootContainer rootContainer) {
      super(modMain, session, path, rootContainer);
   }

   @Deprecated
   public void setKey(String key) {
      super.setPath(pathReader.read(key));
   }

   @Deprecated
   public WaypointWorldContainer addSubContainer(String subID) {
      return (WaypointWorldContainer)super.addSubContainer(this.path.resolve(subID));
   }

   @Deprecated
   public boolean containsSub(String subId) {
      return super.containsSubContainer(this.path.resolve(subId));
   }

   @Deprecated
   public void deleteSubContainer(String subId) {
      super.deleteSubContainer(this.path.resolve(subId));
   }

   @Deprecated
   @Override
   public boolean isEmpty() {
      return super.isEmpty();
   }

   @Deprecated
   public WaypointWorld addWorld(String multiworldId) {
      return (WaypointWorld)super.addWorld(multiworldId);
   }

   @Deprecated
   public void addName(String id, String name) {
      super.setName(id, name);
   }

   @Deprecated
   @Override
   public String getName(String id) {
      return super.getName(id);
   }

   @Deprecated
   @Override
   public void removeName(String id) {
      super.removeName(id);
   }

   @Deprecated
   public String getSubId() {
      return super.getLastNode();
   }

   @Deprecated
   @Override
   public String getSubName() {
      return super.getSubName();
   }

   @Deprecated
   public String getFullName(String id, String containerName) {
      return super.getFullWorldName(id, containerName);
   }

   @Deprecated
   public String getKey() {
      return super.getPath().toString();
   }

   @Deprecated
   public WaypointWorld getFirstWorld() {
      return (WaypointWorld)super.getFirstWorld();
   }

   @Deprecated
   public WaypointWorld getFirstWorldConnectedTo(WaypointWorld refWorld) {
      return (WaypointWorld)super.getFirstWorldConnectedTo(refWorld);
   }

   @Deprecated
   public ArrayList<WaypointWorld> getAllWorlds() {
      List<MinimapWorld> allWorlds = this.getWorldsCopy();

      for (MinimapWorldContainer sub : this.subContainers.values()) {
         allWorlds.addAll(((WaypointWorldContainer)sub).getAllWorlds());
      }

      return (ArrayList<WaypointWorld>)allWorlds;
   }

   @Deprecated
   public String getEqualIgnoreCaseSub(String cId) {
      return this.fixPathCharacterCases(pathReader.read(cId)).toString();
   }

   @Deprecated
   public WaypointWorldRootContainer getRootContainer() {
      return (WaypointWorldRootContainer)super.getRoot();
   }

   @Deprecated
   public File getDirectory() {
      return super.getDirectoryPath().toFile();
   }
}
