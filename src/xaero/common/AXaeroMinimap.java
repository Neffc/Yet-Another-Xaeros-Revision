package xaero.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import xaero.common.config.CommonConfig;
import xaero.common.config.CommonConfigIO;
import xaero.common.controls.ControlsRegister;
import xaero.common.events.FMLCommonEventHandler;
import xaero.common.events.FMLEventHandler;
import xaero.common.events.ForgeCommonEventHandler;
import xaero.common.events.ForgeEventHandler;
import xaero.common.events.ModEvents;
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
import xaero.common.server.mods.SupportServerMods;
import xaero.common.server.player.ServerPlayerTickHandler;
import xaero.common.settings.ModSettings;
import xaero.common.validator.FieldValidatorHolder;

public abstract class AXaeroMinimap {
   public static AXaeroMinimap INSTANCE;
   protected boolean loaded;
   public static final File old_waypointsFile = FabricLoader.getInstance().getGameDir().resolve("xaerowaypoints.txt").toFile();
   public static final File wrongWaypointsFile = FabricLoader.getInstance().getGameDir().resolve("config").resolve("xaerowaypoints.txt").toFile();
   public static final File wrongWaypointsFolder = FabricLoader.getInstance().getGameDir().resolve("mods").resolve("XaeroWaypoints").toFile();

   public abstract String getVersionID();

   public abstract String getFileLayoutID();

   public abstract File getConfigFile();

   public abstract File getModJAR();

   public abstract ModSettings getSettings();

   public abstract void setSettings(ModSettings var1);

   public abstract boolean isOutdated();

   public abstract void setOutdated(boolean var1);

   public abstract String getMessage();

   public abstract void setMessage(String var1);

   public abstract String getLatestVersion();

   public abstract String getLatestVersionMD5();

   public abstract void setLatestVersion(String var1);

   public abstract void setLatestVersionMD5(String var1);

   public abstract int getNewestUpdateID();

   public abstract void setNewestUpdateID(int var1);

   public abstract PatreonMod getPatreon();

   public abstract String getVersionsURL();

   public abstract void resetSettings() throws IOException;

   public abstract String getUpdateLink();

   public abstract Object getSettingsKey();

   public abstract File getWaypointsFile();

   public abstract File getWaypointsFolder();

   public abstract WidgetScreenHandler getWidgetScreenHandler();

   public abstract WidgetLoadingHandler getWidgetLoader();

   public abstract XaeroMinimapSession createSession();

   public abstract SupportMods getSupportMods();

   public abstract SupportServerMods getSupportServerMods();

   public abstract GuiHelper getGuiHelper();

   public abstract FieldValidatorHolder getFieldValidators();

   public abstract ControlsRegister getControlsRegister();

   public abstract ForgeEventHandler getEvents();

   public abstract FMLEventHandler getFMLEvents();

   public abstract ModEvents getModEvents();

   public abstract InterfaceManager getInterfaces();

   public abstract InterfaceRenderer getInterfaceRenderer();

   public boolean isLoaded() {
      return this.loaded;
   }

   public abstract void tryLoadLater();

   public abstract void tryLoadLaterServer();

   public abstract boolean isStandalone();

   public abstract EntityRadarCategoryManager getEntityRadarCategoryManager();

   public abstract boolean isFairPlay();

   public abstract PlayerTrackerMinimapElementRenderer getTrackedPlayerRenderer();

   public abstract PlayerTrackerSystemManager getPlayerTrackerSystemManager();

   public abstract ServerPlayerTickHandler getServerPlayerTickHandler();

   public abstract void setServerPlayerTickHandler(ServerPlayerTickHandler var1);

   public abstract MinimapMessageHandler getMessageHandler();

   public abstract ForgeCommonEventHandler getCommonEvents();

   public abstract FMLCommonEventHandler getFMLCommonEvents();

   public abstract Path getGameDir();

   public abstract void setCommonConfigIO(CommonConfigIO var1);

   public abstract void setCommonConfig(CommonConfig var1);

   public abstract CommonConfigIO getCommonConfigIO();

   public abstract CommonConfig getCommonConfig();
}
