package xaero.common;

import java.io.File;
import java.io.IOException;
import xaero.common.config.CommonConfig;
import xaero.common.config.CommonConfigIO;
import xaero.common.controls.ControlsRegister;
import xaero.common.events.ClientEvents;
import xaero.common.events.ClientEventsListener;
import xaero.common.events.CommonEvents;
import xaero.common.events.ModClientEvents;
import xaero.common.events.ModCommonEvents;
import xaero.common.gui.GuiHelper;
import xaero.common.gui.widget.WidgetLoadingHandler;
import xaero.common.gui.widget.WidgetScreenHandler;
import xaero.common.interfaces.InterfaceManager;
import xaero.common.interfaces.render.InterfaceRenderer;
import xaero.common.message.MinimapMessageHandler;
import xaero.common.minimap.radar.category.EntityRadarCategoryManager;
import xaero.common.minimap.radar.tracker.PlayerTrackerMinimapElementRenderer;
import xaero.common.minimap.radar.tracker.system.PlayerTrackerSystemManager;
import xaero.common.mods.SupportMods;
import xaero.common.patreon.PatreonMod;
import xaero.common.platform.Services;
import xaero.common.server.mods.SupportServerMods;
import xaero.common.server.player.ServerPlayerTickHandler;
import xaero.common.settings.ModSettings;
import xaero.common.validator.FieldValidatorHolder;
import xaero.hud.Hud;
import xaero.hud.io.HudIO;
import xaero.hud.minimap.Minimap;
import xaero.hud.render.HudRenderer;

public interface IXaeroMinimap {
   File old_waypointsFile = Services.PLATFORM.getGameDir().resolve("xaerowaypoints.txt").toFile();
   File wrongWaypointsFile = Services.PLATFORM.getGameDir().resolve("config").resolve("xaerowaypoints.txt").toFile();
   File wrongWaypointsFolder = Services.PLATFORM.getGameDir().resolve("mods").resolve("XaeroWaypoints").toFile();

   String getVersionID();

   String getFileLayoutID();

   File getConfigFile();

   File getModJAR();

   ModSettings getSettings();

   void setSettings(ModSettings var1);

   boolean isOutdated();

   void setOutdated(boolean var1);

   String getMessage();

   void setMessage(String var1);

   String getLatestVersion();

   String getLatestVersionMD5();

   void setLatestVersion(String var1);

   void setLatestVersionMD5(String var1);

   int getNewestUpdateID();

   void setNewestUpdateID(int var1);

   PatreonMod getPatreon();

   String getVersionsURL();

   void resetSettings() throws IOException;

   String getUpdateLink();

   Object getSettingsKey();

   File getWaypointsFile();

   File getWaypointsFolder();

   WidgetScreenHandler getWidgetScreenHandler();

   WidgetLoadingHandler getWidgetLoader();

   XaeroMinimapSession createSession();

   SupportMods getSupportMods();

   SupportServerMods getSupportServerMods();

   GuiHelper getGuiHelper();

   FieldValidatorHolder getFieldValidators();

   ControlsRegister getControlsRegister();

   ClientEvents getEvents();

   void tryLoadLater();

   void tryLoadLaterServer();

   ModClientEvents getModEvents();

   InterfaceManager getInterfaces();

   InterfaceRenderer getInterfaceRenderer();

   boolean isStandalone();

   EntityRadarCategoryManager getEntityRadarCategoryManager();

   boolean isFairPlay();

   PlayerTrackerMinimapElementRenderer getTrackedPlayerRenderer();

   PlayerTrackerSystemManager getPlayerTrackerSystemManager();

   ServerPlayerTickHandler getServerPlayerTickHandler();

   void setServerPlayerTickHandler(ServerPlayerTickHandler var1);

   MinimapMessageHandler getMessageHandler();

   CommonEvents getCommonEvents();

   void setCommonConfigIO(CommonConfigIO var1);

   void setCommonConfig(CommonConfig var1);

   CommonConfigIO getCommonConfigIO();

   CommonConfig getCommonConfig();

   ClientEventsListener getForgeEventHandlerListener();

   PlatformContext getPlatformContext();

   void ensureControlsRegister();

   ModClientEvents getModClientEvents();

   ModCommonEvents getModCommonEvents();

   String getModId();

   boolean isLoadedClient();

   boolean isLoadedServer();

   Hud getHud();

   HudRenderer getHudRenderer();

   HudIO getHudIO();

   Minimap getMinimap();
}
