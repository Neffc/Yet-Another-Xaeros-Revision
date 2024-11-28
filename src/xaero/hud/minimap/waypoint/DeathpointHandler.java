package xaero.hud.minimap.waypoint;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1657;
import xaero.common.HudMod;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.misc.OptimizedMath;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.path.XaeroPath;

public class DeathpointHandler {
   private final HudMod modMain;
   private final MinimapSession session;

   public DeathpointHandler(HudMod modMain, MinimapSession session) {
      this.modMain = modMain;
      this.session = session;
   }

   public void createDeathpoint(class_1657 player) {
      this.session.getWorldStateUpdater().update();
      if (this.modMain.getSettings().switchToAutoOnDeath) {
         this.session.getWorldState().setCustomWorldPath(null);
      }

      boolean worldmap = this.modMain.getSupportMods().worldmap();
      MinimapWorld potentialAutoWorld = null;
      XaeroPath usedAutoWorldPath = this.session.getWorldState().getAutoWorldPath();
      XaeroPath usedAutoContainerPath = usedAutoWorldPath == null ? null : usedAutoWorldPath.getParent();
      XaeroPath potentialAutoContainerPath = this.session.getWorldStateUpdater().getPotentialContainerPath();
      if (!potentialAutoContainerPath.equals(usedAutoContainerPath)) {
         String potentialAutoWorldNode = this.session.getWorldStateUpdater().getPotentialWorldNode(this.session.getMc().field_1687.method_27983(), worldmap);
         if (potentialAutoWorldNode != null) {
            XaeroPath potentialAutoWorldPath = potentialAutoContainerPath.resolve(potentialAutoWorldNode);
            potentialAutoWorld = this.session.getWorldManager().getWorld(potentialAutoWorldPath);
            this.createDeathpoint(player, potentialAutoWorld, false);
         }
      }

      MinimapWorld autoWorld = this.session.getWorldManager().getAutoWorld();
      if (potentialAutoWorld == null && autoWorld != null) {
         this.createDeathpoint(player, autoWorld, false);
      }

      if (worldmap) {
         List<String> allPotentialMWIds = this.modMain.getSupportMods().worldmapSupport.getPotentialMultiworldIds(player.method_37908().method_27983());
         if (allPotentialMWIds != null) {
            for (String mwId : allPotentialMWIds) {
               MinimapWorld potentialWorld = this.session.getWorldManager().getWorld(potentialAutoContainerPath.resolve(mwId));
               if (potentialWorld != autoWorld && potentialWorld != potentialAutoWorld) {
                  this.createDeathpoint(player, potentialWorld, false);
               }
            }
         }
      }
   }

   public void createDeathpoint(class_1657 player, MinimapWorld world, boolean temporary) {
      WaypointSet currentSet = world.getCurrentWaypointSet();
      if (currentSet != null) {
         boolean disabled = false;

         for (WaypointSet set : world.getIterableWaypointSets()) {
            Iterator<Waypoint> waypoints = set.getWaypoints().iterator();

            while (waypoints.hasNext()) {
               Waypoint w = waypoints.next();
               if (w.getPurpose() == WaypointPurpose.DEATH) {
                  if (set == currentSet) {
                     disabled = w.isDisabled();
                  }

                  if (!this.modMain.getSettings().getOldDeathpoints()) {
                     waypoints.remove();
                  } else {
                     w.setPurpose(WaypointPurpose.OLD_DEATH);
                     w.setName("gui.xaero_deathpoint_old");
                  }
                  break;
               }
            }
         }

         double dimDiv = this.session.getDimensionHelper().getDimensionDivision(world);
         if (this.modMain.getSettings().getDeathpoints()) {
            Waypoint deathpoint = new Waypoint(
               OptimizedMath.myFloor((double)OptimizedMath.myFloor(player.method_23317()) * dimDiv),
               OptimizedMath.myFloor(player.method_23318()),
               OptimizedMath.myFloor((double)OptimizedMath.myFloor(player.method_23321()) * dimDiv),
               "gui.xaero_deathpoint",
               "D",
               WaypointColor.BLACK,
               WaypointPurpose.DEATH
            );
            deathpoint.setTemporary(temporary);
            deathpoint.setDisabled(disabled);
            currentSet.add(deathpoint, true);
         }

         try {
            this.session.getWorldManagerIO().saveWorld(world);
         } catch (IOException var10) {
            MinimapLogs.LOGGER.error("suppressed exception", var10);
         }
      }
   }
}
