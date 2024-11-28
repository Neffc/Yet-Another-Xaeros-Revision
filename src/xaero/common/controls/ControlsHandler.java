package xaero.common.controls;

import com.google.common.collect.Lists;
import java.io.IOException;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import net.minecraft.class_437;
import net.minecraft.class_3675.class_307;
import org.lwjgl.glfw.GLFW;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.XaeroMinimapSession;
import xaero.common.effect.Effects;
import xaero.common.gui.GuiAddWaypoint;
import xaero.common.gui.GuiSlimeSeed;
import xaero.common.gui.GuiWaypoints;
import xaero.common.gui.ScreenBase;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.misc.Misc;
import xaero.common.misc.OptimizedMath;
import xaero.common.settings.ModOptions;
import xaero.common.settings.ModSettings;

public class ControlsHandler {
   protected AXaeroMinimap modMain;
   protected XaeroMinimapSession minimapSession;
   protected WaypointsManager waypointsManager;
   protected MinimapProcessor minimap;

   public ControlsHandler(AXaeroMinimap modMain, XaeroMinimapSession minimapSession) {
      this.modMain = modMain;
      this.minimapSession = minimapSession;
      this.waypointsManager = minimapSession.getWaypointsManager();
      this.minimap = minimapSession.getMinimapProcessor();
   }

   public void setKeyState(class_304 kb, boolean pressed) {
      class_304.method_1416(KeyBindingHelper.getBoundKeyOf(kb), pressed);
   }

   public boolean isDown(class_304 kb) {
      if (KeyBindingHelper.getBoundKeyOf(kb).method_1444() == -1) {
         return false;
      } else if (KeyBindingHelper.getBoundKeyOf(kb).method_1442() == class_307.field_1672) {
         return GLFW.glfwGetMouseButton(class_310.method_1551().method_22683().method_4490(), KeyBindingHelper.getBoundKeyOf(kb).method_1444()) == 1;
      } else {
         return KeyBindingHelper.getBoundKeyOf(kb).method_1442() == class_307.field_1668
            ? class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), KeyBindingHelper.getBoundKeyOf(kb).method_1444())
            : false;
      }
   }

   public void keyDownPre(class_304 kb) {
   }

   public void keyDownPost(class_304 kb) {
   }

   public void keyDown(class_304 kb, boolean tickEnd, boolean isRepeat) {
      class_310 mc = class_310.method_1551();
      if (!tickEnd) {
         this.keyDownPre(kb);
         if (kb == ModSettings.newWaypoint && this.modMain.getSettings().waypointsGUI(this.waypointsManager)) {
            mc.method_1507(
               new GuiAddWaypoint(
                  this.modMain,
                  this.waypointsManager,
                  null,
                  Lists.newArrayList(),
                  this.waypointsManager.getCurrentContainerID().split("/")[0],
                  this.waypointsManager.getCurrentWorld(),
                  true
               )
            );
         }

         if (kb == ModSettings.keyWaypoints && this.modMain.getSettings().waypointsGUI(this.waypointsManager)) {
            class_437 current = mc.field_1755;
            class_437 currentEscScreen = current instanceof ScreenBase ? ((ScreenBase)current).escape : null;
            mc.method_1507(new GuiWaypoints(this.modMain, this.minimapSession, current, currentEscScreen));
         }

         if (kb == ModSettings.keyLargeMap) {
            this.minimapSession
               .getMinimapProcessor()
               .setEnlargedMap(this.modMain.getSettings().enlargedMinimapAToggle ? !this.minimapSession.getMinimapProcessor().isEnlargedMap() : true);
            this.minimap.setToResetImage(true);
            this.minimap.instantZoom();
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
               if (this.modMain.getSettings().customSlimeSeedNeeded(this.minimapSession)
                  && this.modMain.getSettings().getBooleanValue(ModOptions.OPEN_SLIME_SETTINGS)) {
                  class_437 current = mc.field_1755;
                  class_437 currentEscScreen = current instanceof ScreenBase ? ((ScreenBase)current).escape : null;
                  class_310.method_1551().method_1507(new GuiSlimeSeed(this.modMain, this.waypointsManager, current, currentEscScreen));
               } else {
                  this.modMain.getSettings().slimeChunks = !this.modMain.getSettings().slimeChunks;
                  this.modMain.getSettings().saveSettings();
               }
            } catch (IOException var12) {
               MinimapLogs.LOGGER.error("suppressed exception", var12);
            }
         }

         if (kb == ModSettings.keyToggleGrid) {
            try {
               this.modMain.getSettings().chunkGrid = -this.modMain.getSettings().chunkGrid - 1;
               this.modMain.getSettings().saveSettings();
            } catch (IOException var11) {
               MinimapLogs.LOGGER.error("suppressed exception", var11);
            }
         }

         if (kb == ModSettings.keyInstantWaypoint
            && !Misc.hasEffect(mc.field_1724, Effects.NO_WAYPOINTS)
            && !Misc.hasEffect(mc.field_1724, Effects.NO_WAYPOINTS_HARMFUL)) {
            this.waypointsManager
               .createTemporaryWaypoints(
                  this.waypointsManager.getCurrentWorld(),
                  OptimizedMath.myFloor(mc.field_1719.method_23317()),
                  OptimizedMath.myFloor(mc.field_1719.method_23318() + 0.0625),
                  OptimizedMath.myFloor(mc.field_1719.method_23321())
               );
         }

         if (kb == ModSettings.keySwitchSet) {
            WaypointWorld currentWorld = this.waypointsManager.getCurrentWorld();
            if (currentWorld != null) {
               String[] keys = currentWorld.getSets().keySet().toArray(new String[0]);

               for (int i = 0; i < keys.length; i++) {
                  if (keys[i] != null && keys[i].equals(currentWorld.getCurrent())) {
                     currentWorld.setCurrent(keys[(i + 1) % keys.length]);
                     break;
                  }
               }

               this.waypointsManager.updateWaypoints();
               this.waypointsManager.setChanged = System.currentTimeMillis();

               try {
                  this.modMain.getSettings().saveWaypoints(currentWorld);
               } catch (IOException var10) {
                  MinimapLogs.LOGGER.error("suppressed exception", var10);
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
            } catch (IOException var9) {
               MinimapLogs.LOGGER.error("suppressed exception", var9);
            }
         }

         if (!this.minimapSession.getMinimapProcessor().isEnlargedMap() || this.modMain.getSettings().zoomOnEnlarged == 0) {
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
               } catch (IOException var8) {
                  MinimapLogs.LOGGER.error("suppressed exception", var8);
               }
            }
         }

         if (kb == ModSettings.keyToggleRadar) {
            this.modMain.getSettings().toggleBooleanOptionValue(ModOptions.RADAR_DISPLAYED);
         } else if (kb == ModSettings.keyManualCaveMode) {
            this.minimapSession.getMinimapProcessor().toggleManualCaveMode();
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
      if (!tickEnd) {
         this.keyUpPre(kb);
         if (!this.modMain.getSettings().enlargedMinimapAToggle && kb == ModSettings.keyLargeMap) {
            this.minimapSession.getMinimapProcessor().setEnlargedMap(false);
            this.minimap.setToResetImage(true);
            this.minimap.instantZoom();
         }

         this.keyUpPost(kb);
      }
   }
}
