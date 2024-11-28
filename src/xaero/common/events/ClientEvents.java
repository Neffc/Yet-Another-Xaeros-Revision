package xaero.common.events;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.lang.reflect.Field;
import net.minecraft.class_1041;
import net.minecraft.class_1074;
import net.minecraft.class_1657;
import net.minecraft.class_1936;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_429;
import net.minecraft.class_437;
import net.minecraft.class_4398;
import net.minecraft.class_442;
import net.minecraft.class_4439;
import net.minecraft.class_4877;
import net.minecraft.class_500;
import net.minecraft.class_638;
import net.minecraft.class_8251;
import net.minecraft.class_2556.class_7602;
import org.apache.commons.lang3.StringUtils;
import org.joml.Matrix4f;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.effect.Effects;
import xaero.common.gui.GuiAddWaypoint;
import xaero.common.gui.GuiWaypoints;
import xaero.common.gui.GuiWidgetUpdateAll;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.misc.Misc;
import xaero.common.patreon.Patreon;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.MinimapLogs;

public class ClientEvents {
   protected IXaeroMinimap modMain;
   private class_437 lastGuiOpen;
   private Field realmsTaskField;
   private Field realmsTaskServerField;
   public class_4877 latestRealm;

   public ClientEvents(IXaeroMinimap modMain) {
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
               this.realmsTaskField = Misc.getFieldReflection(class_4398.class, "queuedTasks", "field_19919", "Lnet/minecraft/class_4358;", "f_88773_");
               this.realmsTaskField.setAccessible(true);
            }

            if (this.realmsTaskServerField == null) {
               this.realmsTaskServerField = Misc.getFieldReflection(class_4439.class, "server", "field_20224", "Lnet/minecraft/class_4877;", "f_90327_");
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

   public void handleRenderGameOverlayEventPre(class_332 guiGraphics, float partialTicks) {
      if (!class_310.method_1551().field_1690.field_1842) {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         if (minimapSession != null) {
            class_1041 mainwindow = class_310.method_1551().method_22683();
            Matrix4f projectionMatrixBU = RenderSystem.getProjectionMatrix();
            class_8251 vertexSortingBU = RenderSystem.getVertexSorting();
            Matrix4f ortho = new Matrix4f().setOrtho(0.0F, (float)mainwindow.method_4489(), (float)mainwindow.method_4506(), 0.0F, 1000.0F, 3000.0F);
            RenderSystem.setProjectionMatrix(ortho, class_8251.field_43361);
            RenderSystem.getModelViewStack().method_22903();
            RenderSystem.getModelViewStack().method_34426();
            RenderSystem.applyModelViewMatrix();
            this.modMain
               .getInterfaces()
               .getMinimapInterface()
               .getWaypointsIngameRenderer()
               .render(
                  minimapSession, partialTicks, minimapSession.getMinimapProcessor(), XaeroMinimapCore.waypointsProjection, XaeroMinimapCore.waypointModelView
               );
            RenderSystem.getModelViewStack().method_22909();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.setProjectionMatrix(projectionMatrixBU, vertexSortingBU);
         }
      }
   }

   public void handleRenderGameOverlayEventPost() {
      this.modMain.getHud().getEventHandler().handleRenderGameOverlayEventPost();
   }

   public boolean handleClientSendChatEvent(String message) {
      if (message.startsWith("xaero_waypoint_add:")) {
         String[] args = message.split(":");
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         minimapSession.getWaypointSharing().onWaypointAdd(args);
         return true;
      } else if (message.equals("xaero_tp_anyway")) {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         minimapSession.getWaypointsManager().teleportAnyway();
         return true;
      } else {
         return false;
      }
   }

   public boolean handleClientPlayerChatReceivedEvent(class_7602 chatType, class_2561 component, GameProfile gameProfile) {
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

         String probableName = StringUtils.substringBetween(textString, "<", ">");
         return this.handleChatMessage(
            probableName == null ? class_1074.method_4662("gui.xaero_waypoint_server_shared", new Object[0]) : probableName, component
         );
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

   public void worldUnload(class_1936 world) {
      if (world instanceof class_638) {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         if (minimapSession != null) {
            MinimapProcessor minimap = minimapSession.getMinimapProcessor();
            minimap.getEntityRadar().updateRadar(null, null, null, minimap);
         }
      }
   }

   public void handleClientTickStart() {
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      if (minimapSession != null) {
         MinimapProcessor minimap = minimapSession.getMinimapProcessor();
         minimap.onClientTick();
         if (class_310.method_1551().field_1755 == null) {
            minimapSession.getKeyEventHandler().onKeyInput(class_310.method_1551(), this.modMain, minimapSession);
         }
      }
   }

   public void handlePlayerTickStart(class_1657 player) {
      if (player == class_310.method_1551().field_1724) {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         if (minimapSession != null) {
            try {
               MinimapProcessor minimap = minimapSession.getMinimapProcessor();
               WaypointsManager waypointsManager = minimapSession.getWaypointsManager();
               waypointsManager.updateWorldIds();
               minimap.onPlayerTick();
               waypointsManager.updateWaypoints();
               class_310 mc = class_310.method_1551();
               minimapSession.getKeyEventHandler().handleEvents(mc, minimapSession);
               this.modMain.getForgeEventHandlerListener().playerTickPost(minimapSession);
            } catch (Throwable var6) {
               this.modMain.getInterfaces().getMinimapInterface().setCrashedWith(var6);
            }
         }
      }
   }

   public void handleRenderTickStart() {
      if (class_310.method_1551().field_1724 != null) {
         this.modMain.getInterfaces().getMinimapInterface().checkCrashes();
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         if (minimapSession != null) {
            MinimapProcessor minimap = minimapSession.getMinimapProcessor();
            minimap.getMinimapWriter().onRender();
         }
      }
   }

   public boolean handleRenderStatusEffectOverlay(class_332 guiGraphics) {
      return this.modMain.getForgeEventHandlerListener().handleRenderStatusEffectOverlay(guiGraphics);
   }

   public boolean handleRenderCrosshairOverlay(class_332 guiGraphics) {
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      return minimapSession == null ? false : minimapSession.getMinimapProcessor().isEnlargedMap() && this.modMain.getSettings().centeredEnlarged;
   }
}
