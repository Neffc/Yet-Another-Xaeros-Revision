package xaero.common.controls;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.class_1074;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import net.minecraft.class_437;
import net.minecraft.class_3675.class_307;
import org.lwjgl.glfw.GLFW;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.effect.Effects;
import xaero.common.gui.GuiAddWaypoint;
import xaero.common.gui.GuiSlimeSeed;
import xaero.common.gui.GuiWaypoints;
import xaero.common.gui.ScreenBase;
import xaero.common.misc.KeySortableByOther;
import xaero.common.misc.Misc;
import xaero.common.misc.OptimizedMath;
import xaero.common.platform.Services;
import xaero.common.settings.ModOptions;
import xaero.common.settings.ModSettings;
import xaero.hud.HudSession;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;

public class ControlsHandler {
   protected IXaeroMinimap modMain;
   protected HudSession hudSession;

   public ControlsHandler(IXaeroMinimap modMain, HudSession hudSession) {
      this.modMain = modMain;
      this.hudSession = hudSession;
   }

   public void setKeyState(class_304 kb, boolean pressed) {
      class_304.method_1416(Services.PLATFORM.getKeyBindingHelper().getBoundKeyOf(kb), pressed);
   }

   public boolean isDown(class_304 kb) {
      IKeyBindingHelper keyBindingHelper = Services.PLATFORM.getKeyBindingHelper();
      if (keyBindingHelper.getBoundKeyOf(kb).method_1444() == -1) {
         return false;
      } else if (keyBindingHelper.getBoundKeyOf(kb).method_1442() == class_307.field_1672) {
         return GLFW.glfwGetMouseButton(class_310.method_1551().method_22683().method_4490(), keyBindingHelper.getBoundKeyOf(kb).method_1444()) == 1;
      } else {
         return keyBindingHelper.getBoundKeyOf(kb).method_1442() == class_307.field_1668
            ? class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), keyBindingHelper.getBoundKeyOf(kb).method_1444())
            : false;
      }
   }

   public void keyDownPre(class_304 kb) {
   }

   public void keyDownPost(class_304 kb) {
   }

   public void keyDown(class_304 kb, boolean tickEnd, boolean isRepeat) {
      MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
      MinimapWorldManager waypointsManager = session.getWorldManager();
      class_310 mc = class_310.method_1551();
      if (!tickEnd) {
         this.keyDownPre(kb);
         if (kb == ModSettings.newWaypoint && this.modMain.getSettings().waypointsGUI(session)) {
            mc.method_1507(
               new GuiAddWaypoint(
                  (HudMod)this.modMain,
                  session,
                  null,
                  Lists.newArrayList(),
                  session.getWorldState().getCurrentWorldPath().getRoot(),
                  waypointsManager.getCurrentWorld(),
                  true
               )
            );
         }

         if (kb == ModSettings.keyWaypoints && this.modMain.getSettings().waypointsGUI(session)) {
            class_437 current = mc.field_1755;
            class_437 currentEscScreen = current instanceof ScreenBase ? ((ScreenBase)current).escape : null;
            mc.method_1507(new GuiWaypoints((HudMod)this.modMain, session, current, currentEscScreen));
         }

         if (kb == ModSettings.keyLargeMap) {
            session.getProcessor().setEnlargedMap(this.modMain.getSettings().enlargedMinimapAToggle ? !session.getProcessor().isEnlargedMap() : true);
            session.getProcessor().setToResetImage(true);
            session.getProcessor().instantZoom();
         }

         if (kb == ModSettings.keyToggleMap && !Misc.hasEffect(mc.field_1724, Effects.NO_MINIMAP) && !Misc.hasEffect(mc.field_1724, Effects.NO_MINIMAP_HARMFUL)
            )
          {
            this.modMain.getSettings().toggleBooleanOptionValue(ModOptions.MINIMAP);
         }

         if (kb == ModSettings.keyToggleWaypoints) {
            this.modMain.getSettings().toggleBooleanOptionValue(ModOptions.INGAME_WAYPOINTS);
         }

         if (kb == ModSettings.keyToggleMapWaypoints) {
            this.modMain.getSettings().toggleBooleanOptionValue(ModOptions.WAYPOINTS);
         }

         if (kb == ModSettings.keyToggleSlimes) {
            try {
               if (this.modMain.getSettings().customSlimeSeedNeeded(this.hudSession)
                  && this.modMain.getSettings().getBooleanValue(ModOptions.OPEN_SLIME_SETTINGS)) {
                  class_437 current = mc.field_1755;
                  class_437 currentEscScreen = current instanceof ScreenBase ? ((ScreenBase)current).escape : null;
                  class_310.method_1551().method_1507(new GuiSlimeSeed(this.modMain, session, current, currentEscScreen));
               } else {
                  this.modMain.getSettings().slimeChunks = !this.modMain.getSettings().slimeChunks;
                  this.modMain.getSettings().saveSettings();
               }
            } catch (IOException var18) {
               MinimapLogs.LOGGER.error("suppressed exception", var18);
            }
         }

         if (kb == ModSettings.keyToggleGrid) {
            try {
               this.modMain.getSettings().chunkGrid = -this.modMain.getSettings().chunkGrid - 1;
               this.modMain.getSettings().saveSettings();
            } catch (IOException var17) {
               MinimapLogs.LOGGER.error("suppressed exception", var17);
            }
         }

         if (kb == ModSettings.keyInstantWaypoint
            && !Misc.hasEffect(mc.field_1724, Effects.NO_WAYPOINTS)
            && !Misc.hasEffect(mc.field_1724, Effects.NO_WAYPOINTS_HARMFUL)) {
            session.getWaypointSession()
               .getTemporaryHandler()
               .createTemporaryWaypoint(
                  waypointsManager.getCurrentWorld(),
                  OptimizedMath.myFloor(mc.field_1719.method_23317()),
                  OptimizedMath.myFloor(mc.field_1719.method_23318() + 0.0625),
                  OptimizedMath.myFloor(mc.field_1719.method_23321())
               );
         }

         if (kb == ModSettings.keySwitchSet) {
            MinimapWorld currentWorld = waypointsManager.getCurrentWorld();
            if (currentWorld != null) {
               List<KeySortableByOther<String>> keysList = new ArrayList<>();

               for (WaypointSet set : currentWorld.getIterableWaypointSets()) {
                  String key = set.getName();
                  keysList.add(new KeySortableByOther<>(key, class_1074.method_4662(key, new Object[0]).toLowerCase()));
               }

               Collections.sort(keysList);
               boolean foundCurrent = false;
               String firstSetKey = null;

               for (KeySortableByOther<String> sortedSet : keysList) {
                  String setKey = sortedSet.getKey();
                  if (firstSetKey == null) {
                     firstSetKey = setKey;
                  }

                  if (setKey != null && setKey.equals(currentWorld.getCurrentWaypointSetId())) {
                     foundCurrent = true;
                  } else if (foundCurrent) {
                     foundCurrent = false;
                     currentWorld.setCurrentWaypointSetId(setKey);
                     break;
                  }
               }

               if (foundCurrent) {
                  currentWorld.setCurrentWaypointSetId(firstSetKey);
               }

               session.getWorldStateUpdater().update();
               session.getWaypointSession().setSetChangedTime(System.currentTimeMillis());

               try {
                  session.getWorldManagerIO().saveWorld(currentWorld);
               } catch (IOException var16) {
                  MinimapLogs.LOGGER.error("suppressed exception", var16);
               }
            }
         }

         if (kb == ModSettings.keyAllSets) {
            this.modMain.getSettings().toggleBooleanOptionValue(ModOptions.WAYPOINTS_ALL_SETS);
         }

         if (kb == ModSettings.keyLightOverlay) {
            if (this.modMain.getSettings().lightOverlayType == 0) {
               this.modMain.getSettings().lightOverlayType = 1;
            } else {
               this.modMain.getSettings().lightOverlayType *= -1;
            }

            try {
               this.modMain.getSettings().saveSettings();
            } catch (IOException var15) {
               MinimapLogs.LOGGER.error("suppressed exception", var15);
            }
         }

         if (!session.getProcessor().isEnlargedMap() || this.modMain.getSettings().zoomOnEnlarged == 0) {
            int zoomChange = 0;
            if (kb == ModSettings.keyBindZoom) {
               zoomChange = 1;
            }

            if (kb == ModSettings.keyBindZoom1) {
               zoomChange = -1;
            }

            if (zoomChange != 0) {
               this.modMain.getSettings().changeZoom(zoomChange);

               try {
                  this.modMain.getSettings().saveSettings();
               } catch (IOException var14) {
                  MinimapLogs.LOGGER.error("suppressed exception", var14);
               }
            }
         }

         if (kb == ModSettings.keyToggleRadar) {
            this.modMain.getSettings().toggleBooleanOptionValue(ModOptions.RADAR_DISPLAYED);
         } else if (kb == ModSettings.keyManualCaveMode) {
            session.getProcessor().toggleManualCaveMode();
         } else if (kb == ModSettings.keyToggleTrackedPlayers) {
            this.modMain.getSettings().toggleBooleanOptionValue(ModOptions.TRACKED_PLAYERS);
         } else if (kb == ModSettings.keyTogglePacChunkClaims) {
            if (this.modMain.getSupportMods().worldmap() && this.modMain.getSupportMods().shouldUseWorldMapChunks()) {
               this.modMain.getSupportMods().worldmapSupport.toggleChunkClaims();
            } else {
               this.modMain.getSettings().toggleBooleanOptionValue(ModOptions.PAC_CLAIMS);
            }
         }

         this.keyDownPost(kb);
      }
   }

   public void keyUpPre(class_304 kb) {
   }

   public void keyUpPost(class_304 kb) {
   }

   public void keyUp(class_304 kb, boolean tickEnd) {
      MinimapSession minimapSession = BuiltInHudModules.MINIMAP.getCurrentSession();
      if (!tickEnd) {
         this.keyUpPre(kb);
         if (!this.modMain.getSettings().enlargedMinimapAToggle && kb == ModSettings.keyLargeMap) {
            minimapSession.getProcessor().setEnlargedMap(false);
            minimapSession.getProcessor().setToResetImage(true);
            minimapSession.getProcessor().instantZoom();
         }

         this.keyUpPost(kb);
      }
   }
}
