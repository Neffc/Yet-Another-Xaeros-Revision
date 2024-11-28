package xaero.common.events;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.lang.reflect.Field;
import net.minecraft.class_1074;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2556;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3675;
import net.minecraft.class_429;
import net.minecraft.class_437;
import net.minecraft.class_4398;
import net.minecraft.class_442;
import net.minecraft.class_4439;
import net.minecraft.class_4877;
import net.minecraft.class_500;
import net.minecraft.class_638;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.XaeroMinimapSession;
import xaero.common.anim.OldAnimation;
import xaero.common.effect.Effects;
import xaero.common.gui.GuiAddWaypoint;
import xaero.common.gui.GuiEditMode;
import xaero.common.gui.GuiWaypoints;
import xaero.common.gui.GuiWidgetUpdateAll;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.misc.Misc;
import xaero.common.patreon.Patreon;
import xaero.common.settings.ModSettings;

public class ForgeEventHandler {
   private AXaeroMinimap modMain;
   private class_437 lastGuiOpen;
   private Field realmsTaskField;
   private Field realmsTaskServerField;
   private boolean crosshairDisabledByThisMod = false;
   public static boolean renderCrosshairs = true;
   public class_4877 latestRealm;

   public ForgeEventHandler(AXaeroMinimap modMain) {
      this.modMain = modMain;
   }

   public class_437 handleGuiOpen(class_437 gui) {
      if (gui != null && gui.getClass() == class_429.class) {
         if (!ModSettings.settingsButton) {
            return gui;
         }

         gui = this.modMain.getGuiHelper().getMyOptions();

         try {
            this.modMain.getSettings().saveSettings();
         } catch (IOException var7) {
            MinimapLogs.LOGGER.error("suppressed exception", var7);
         }
      }

      if (gui instanceof class_442 || gui instanceof class_500) {
         this.modMain.getSettings().resetServerSettings();
      }

      class_310 mc = class_310.method_1551();
      if (gui instanceof class_4398) {
         try {
            if (this.realmsTaskField == null) {
               this.realmsTaskField = Misc.getFieldReflection(class_4398.class, "field_19919", "Lnet/minecraft/class_4358;");
               this.realmsTaskField.setAccessible(true);
            }

            if (this.realmsTaskServerField == null) {
               this.realmsTaskServerField = Misc.getFieldReflection(class_4439.class, "field_20224", "Lnet/minecraft/class_4877;");
               this.realmsTaskServerField.setAccessible(true);
            }

            class_4398 realmsTaskScreen = (class_4398)gui;
            if (this.realmsTaskField.get(realmsTaskScreen) instanceof class_4439 realmsTask) {
               class_4877 realm = (class_4877)this.realmsTaskServerField.get(realmsTask);
               if (realm != null && (this.latestRealm == null || realm.field_22599 != this.latestRealm.field_22599)) {
                  this.latestRealm = realm;
               }
            }
         } catch (Exception var8) {
            MinimapLogs.LOGGER.error("suppressed exception", var8);
         }
      } else if ((gui instanceof GuiAddWaypoint || gui instanceof GuiWaypoints)
         && (mc.field_1724.method_6059(Effects.NO_WAYPOINTS) || mc.field_1724.method_6059(Effects.NO_WAYPOINTS_HARMFUL))) {
         gui = null;
      }

      this.lastGuiOpen = gui;
      return gui;
   }

   public boolean handleRenderStatusEffectOverlay(class_332 guiGraphics) {
      return false;
   }

   protected void handleRenderGameOverlayEventPreOverridable(class_332 guiGraphics, float partialTicks) {
      RenderSystem.clear(256, class_310.field_1703);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      if (minimapSession != null) {
         this.modMain.getInterfaceRenderer().renderInterfaces(minimapSession, guiGraphics, partialTicks);
         this.modMain
            .getInterfaces()
            .getMinimapInterface()
            .getWaypointsGuiRenderer()
            .drawSetChange(minimapSession.getWaypointsManager(), guiGraphics, class_310.method_1551().method_22683());
         if (renderCrosshairs && minimapSession.getMinimapProcessor().isEnlargedMap() && this.modMain.getSettings().centeredEnlarged) {
            renderCrosshairs = false;
            this.crosshairDisabledByThisMod = true;
         }
      }

      OldAnimation.tick();
   }

   public void handleRenderGameOverlayEventPre(class_332 guiGraphics, float partialTicks) {
      if (class_3675.method_15987(class_310.method_1551().method_22683().method_4490(), 256)) {
         GuiEditMode.cancel(this.modMain.getInterfaces());
      }

      this.handleRenderGameOverlayEventPreOverridable(guiGraphics, partialTicks);
   }

   public void handleRenderGameOverlayEventPost() {
      if (this.crosshairDisabledByThisMod) {
         renderCrosshairs = true;
         this.crosshairDisabledByThisMod = false;
      }
   }

   public String handleClientSendChatEvent(String message) {
      if (message.startsWith("xaero_waypoint_add:")) {
         String[] args = message.split(":");
         message = "";
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         minimapSession.getWaypointSharing().onWaypointAdd(args);
      } else if (message.equals("xaero_tp_anyway")) {
         message = "";
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         minimapSession.getWaypointsManager().teleportAnyway();
      }

      return message;
   }

   public boolean handleClientPlayerChatReceivedEvent(class_2556 chatType, class_2561 component, GameProfile gameProfile) {
      return component == null
         ? false
         : this.handleChatMessage(
            gameProfile == null ? class_1074.method_4662("gui.xaero_waypoint_somebody_shared", new Object[0]) : gameProfile.getName(), component
         );
   }

   public boolean handleClientSystemChatReceivedEvent(class_2561 component) {
      if (component == null) {
         return false;
      } else {
         String textString = component.getString();
         if (textString.contains("§r§e§s§e§t§x§a§e§r§o")) {
            XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
            minimapSession.getMinimapProcessor().setNoMinimapMessageReceived(false);
            minimapSession.getMinimapProcessor().setFairPlayOnlyMessageReceived(false);
         }

         if (textString.contains("§n§o§m§i§n§i§m§a§p")) {
            XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
            minimapSession.getMinimapProcessor().setNoMinimapMessageReceived(true);
         }

         if (textString.contains("§f§a§i§r§x§a§e§r§o")) {
            XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
            minimapSession.getMinimapProcessor().setFairPlayOnlyMessageReceived(true);
         }

         return this.handleChatMessage(class_1074.method_4662("gui.xaero_waypoint_server_shared", new Object[0]), component);
      }
   }

   private boolean handleChatMessage(String playerName, class_2561 text) {
      String textString = text.getString();
      if (!textString.contains("xaero_waypoint:") && !textString.contains("xaero-waypoint:")) {
         return false;
      } else {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         minimapSession.getWaypointSharing().onWaypointReceived(playerName, textString);
         return true;
      }
   }

   public void handleDrawScreenEventPost(class_437 gui) {
      if (Patreon.needsNotification() && gui instanceof class_442) {
         class_310.method_1551().method_1507(new GuiWidgetUpdateAll(this.modMain));
      } else if (this.modMain.isOutdated()) {
         this.modMain.setOutdated(false);
      }
   }

   public void handlePlayerSetSpawnEvent(class_2338 newSpawnPoint, class_1937 world) {
      if (world instanceof class_638) {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         if (minimapSession != null) {
            minimapSession.getWaypointsManager().setCurrentSpawn(newSpawnPoint, (class_638)world);
         }
      }
   }

   public Object getLastGuiOpen() {
      return this.lastGuiOpen;
   }

   public void worldUnload(class_638 world) {
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      if (minimapSession != null) {
         MinimapProcessor minimap = minimapSession.getMinimapProcessor();
         minimap.getEntityRadar().updateRadar(null, null, null, minimap);
      }
   }
}
