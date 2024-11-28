package xaero.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xaero.common.category.setting.ObjectCategoryDefaultSettingsSetter;
import xaero.common.config.CommonConfig;
import xaero.common.config.CommonConfigIO;
import xaero.common.config.CommonConfigInit;
import xaero.common.controls.ControlsRegister;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.events.ClientEvents;
import xaero.common.events.ClientEventsListener;
import xaero.common.events.CommonEvents;
import xaero.common.events.ModClientEvents;
import xaero.common.events.ModCommonEvents;
import xaero.common.file.SimpleBackup;
import xaero.common.gui.GuiHelper;
import xaero.common.gui.widget.WidgetLoadingHandler;
import xaero.common.gui.widget.WidgetScreenHandler;
import xaero.common.interfaces.InterfaceManager;
import xaero.common.interfaces.render.InterfaceRenderer;
import xaero.common.message.MinimapMessageHandler;
import xaero.common.minimap.MinimapInterface;
import xaero.common.minimap.radar.category.EntityRadarCategoryManager;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.minimap.radar.tracker.PlayerTrackerMinimapElementRenderer;
import xaero.common.minimap.radar.tracker.system.PlayerTrackerSystemManager;
import xaero.common.minimap.radar.tracker.system.impl.SyncedPlayerTrackerSystem;
import xaero.common.misc.Internet;
import xaero.common.mods.SupportMods;
import xaero.common.patreon.Patreon;
import xaero.common.patreon.PatreonMod;
import xaero.common.platform.Services;
import xaero.common.server.XaeroMinimapServer;
import xaero.common.server.core.XaeroMinimapServerCore;
import xaero.common.server.mods.SupportServerMods;
import xaero.common.server.player.ServerPlayerTickHandler;
import xaero.common.settings.ModOptions;
import xaero.common.settings.ModSettings;
import xaero.common.validator.FieldValidatorHolder;
import xaero.common.validator.NumericFieldValidator;
import xaero.common.validator.WaypointCoordinateFieldValidator;
import xaero.hud.Hud;
import xaero.hud.io.HudIO;
import xaero.hud.minimap.Minimap;
import xaero.hud.render.HudRenderer;

public abstract class HudMod implements IXaeroMinimap {
   public static final Logger LOGGER = LogManager.getLogger();
   public static final boolean FAIRPLAY = false;
   public static HudMod INSTANCE;
   protected PlatformContext platformContext;
   protected boolean loadedClient;
   protected boolean loadedServer;
   protected static final String versionID_minecraft = "1.20";
   private String versionID;
   private int newestUpdateID;
   private boolean isOutdated;
   private String latestVersion;
   private String latestVersionMD5;
   private ModSettings settings;
   private String message = "";
   private ControlsRegister controlsRegister;
   protected final ClientEvents events;
   protected final ModClientEvents modClientEvents;
   protected InterfaceRenderer interfaceRenderer;
   protected InterfaceManager interfaces;
   private GuiHelper guiHelper;
   private FieldValidatorHolder fieldValidators;
   private SupportMods supportMods;
   private SupportServerMods supportServerMods;
   private WidgetScreenHandler widgetScreenHandler;
   private WidgetLoadingHandler widgetLoader;
   private EntityRadarCategoryManager entityRadarCategoryManager;
   private ServerPlayerTickHandler serverPlayerTickHandler;
   private PlayerTrackerSystemManager playerTrackerSystemManager;
   private PlayerTrackerMinimapElementRenderer trackedPlayerRenderer;
   private MinimapMessageHandler messageHandler;
   protected final CommonEvents commonEvents;
   protected final ModCommonEvents modCommonEvents;
   private CommonConfigIO commonConfigIO;
   private CommonConfig commonConfig;
   private ClientEventsListener clientEventsListener;
   protected Hud hud;
   protected HudRenderer hudRenderer;
   protected HudIO hudIO;
   private HudClientOnlyBase clientOnlyBase;
   private MinimapInterface minimapInterface;
   private File modJAR = null;
   private File configFile;
   public File waypointsFile;
   public File waypointsFolder;
   private XaeroMinimapServer minimapServer;

   @Override
   public boolean isLoadedClient() {
      return this.loadedClient;
   }

   @Override
   public boolean isLoadedServer() {
      return this.loadedServer;
   }

   protected HudMod() {
      INSTANCE = this;
      this.platformContext = this.createPlatformContext();
      this.playerTrackerSystemManager = new PlayerTrackerSystemManager();
      this.commonEvents = this.platformContext.createCommonEvents(this);
      this.modCommonEvents = this.platformContext.createModCommonEvents(this);
      if (!Services.PLATFORM.isDedicatedServer()) {
         this.events = this.platformContext.createClientEvents(this);
         this.clientEventsListener = this.createForgeEventHandlerListener();
         this.modClientEvents = this.platformContext.createModClientEvents(this);
      } else {
         this.events = null;
         this.modClientEvents = null;
      }

      new CommonConfigInit().init(this, this.getCommonConfigFileName());
   }

   protected abstract PlatformContext createPlatformContext();

   protected abstract String getCommonConfigFileName();

   protected abstract ModSettings createModSettings();

   protected abstract ControlsRegister createControlsRegister();

   protected abstract GuiHelper createGuiHelper();

   protected abstract String getOldConfigFileName();

   protected abstract HudClientOnlyBase createClientOnly();

   protected abstract String getModName();

   protected abstract Logger getLogger();

   protected abstract ClientEventsListener createForgeEventHandlerListener();

   protected abstract String getConfigFileName();

   protected void loadClient() throws IOException {
      Logger LOGGER = this.getLogger();
      LOGGER.info("Loading {} - Stage 1/2", this.getModName());
      ModOptions.init(this);
      String modId = this.getModId();
      Path modFile = Services.PLATFORM.getModFile(modId);
      (this.clientOnlyBase = this.createClientOnly()).preInit(modId, this);
      String fileName = modFile.getFileName().toString();
      if (fileName.endsWith(".jar")) {
         this.modJAR = modFile.toFile();
      }

      Path gameDir = Services.PLATFORM.getGameDir();
      Path config = Services.PLATFORM.getConfigDir();
      this.waypointsFile = config.resolve("xaerowaypoints.txt").toFile();
      Path wrongWaypointsFolder3 = config.resolve("XaeroWaypoints");
      Path wrongWaypointsFolder2;
      if (this.modJAR != null) {
         wrongWaypointsFolder2 = this.modJAR.toPath().getParent().resolve("XaeroWaypoints");
      } else {
         wrongWaypointsFolder2 = config.getParent().resolve("mods").resolve("XaeroWaypoints");
      }

      Path wrongWaypointsFolder4 = new File(config.toFile().getCanonicalPath()).toPath().getParent().resolve("XaeroWaypoints");
      Path wrongWaypointsFolder5 = config.getParent().resolve("XaeroWaypoints");
      this.waypointsFolder = gameDir.resolve("XaeroWaypoints").toFile();
      if (wrongWaypointsFile.exists() && !this.waypointsFile.exists()) {
         Files.move(wrongWaypointsFile.toPath(), this.waypointsFile.toPath());
      }

      if (wrongWaypointsFolder.exists() && !this.waypointsFolder.exists()) {
         Files.move(wrongWaypointsFolder.toPath(), this.waypointsFolder.toPath());
      } else if (wrongWaypointsFolder2.toFile().exists() && !this.waypointsFolder.exists()) {
         Files.move(wrongWaypointsFolder2, this.waypointsFolder.toPath());
      } else if (wrongWaypointsFolder3.toFile().exists() && !this.waypointsFolder.exists()) {
         Files.move(wrongWaypointsFolder3, this.waypointsFolder.toPath());
      } else if (wrongWaypointsFolder4.toFile().exists() && !this.waypointsFolder.exists()) {
         Files.move(wrongWaypointsFolder4, this.waypointsFolder.toPath());
      } else if (wrongWaypointsFolder5.toFile().exists() && !this.waypointsFolder.exists()) {
         Files.move(wrongWaypointsFolder5, this.waypointsFolder.toPath());
      }

      Path waypointsFolderBackup062020 = this.waypointsFolder.toPath().resolveSibling(this.waypointsFolder.getName() + "_BACKUP032021");
      if (!Files.exists(waypointsFolderBackup062020) && this.waypointsFolder.exists()) {
         LOGGER.info("Backing up XaeroWaypoints...");
         SimpleBackup.copyDirectoryWithContents(this.waypointsFolder.toPath(), waypointsFolderBackup062020, 32);
         LOGGER.info("Done backing up XaeroWaypoints!");
      }

      this.configFile = config.resolve(this.getConfigFileName()).toFile();
      File oldConfigFile = gameDir.resolve("config").resolve(this.getOldConfigFileName()).toFile();
      if (oldConfigFile.exists() && !this.configFile.exists()) {
         Files.move(oldConfigFile.toPath(), this.configFile.toPath());
      }

      this.widgetScreenHandler = new WidgetScreenHandler();
      this.widgetLoader = new WidgetLoadingHandler(this.widgetScreenHandler);
      this.settings = this.createModSettings();
      this.ensureControlsRegister();
      this.guiHelper = this.createGuiHelper();
      this.fieldValidators = new FieldValidatorHolder(new NumericFieldValidator(), new WaypointCoordinateFieldValidator());
      this.interfaceRenderer = new InterfaceRenderer(this);
      this.interfaces = new InterfaceManager(this);
      this.minimapInterface = new MinimapInterface(this);
      File old_optionsFile = gameDir.resolve(this.getOldConfigFileName()).toFile();
      if (old_optionsFile.exists() && !this.configFile.exists()) {
         this.configFile.getParentFile().mkdirs();
         Files.move(old_optionsFile.toPath(), this.configFile.toPath());
      }

      if (old_waypointsFile.exists() && !this.waypointsFile.exists()) {
         this.waypointsFile.getParentFile().mkdirs();
         Files.move(old_waypointsFile.toPath(), this.waypointsFile.toPath());
      }

      this.trackedPlayerRenderer = PlayerTrackerMinimapElementRenderer.Builder.begin(this).build();
      XaeroMinimapCore.modMain = this;
   }

   public void loadLater() {
      Logger LOGGER = this.getLogger();
      LOGGER.info("Loading {} - Stage 2/2", this.getModName());

      try {
         this.clientOnlyBase.preLoadLater(this);
         this.controlsRegister.onStage2();
         this.settings.loadSettings();
         this.entityRadarCategoryManager = EntityRadarCategoryManager.Builder.getDefault().setModMain(this).build();
         this.entityRadarCategoryManager.init();
         Patreon.checkPatreon(this);
         Internet.checkModVersion(this);
         if (this.isOutdated) {
            PatreonMod patreonEntry = this.getPatreon();
            if (patreonEntry != null) {
               patreonEntry.modJar = this.modJAR;
               patreonEntry.currentVersion = this.getVersionID();
               patreonEntry.latestVersion = this.latestVersion;
               patreonEntry.md5 = this.latestVersionMD5;
               patreonEntry.onVersionIgnore = () -> {
                  ModSettings.ignoreUpdate = this.newestUpdateID;

                  try {
                     this.getSettings().saveSettings();
                  } catch (IOException var3x) {
                     LOGGER.error("suppressed exception", var3x);
                  }
               };
               Patreon.addOutdatedMod(patreonEntry);
            }
         }

         this.playerTrackerSystemManager.register("minimap_synced", new SyncedPlayerTrackerSystem());
         this.supportMods = this.platformContext.createSupportMods(this);
         this.getInterfaces().getMinimapInterface().getOverMapRendererHandler().add(this.trackedPlayerRenderer);
      } catch (Throwable var3) {
         LOGGER.error("error", var3);
         this.interfaces.getMinimapInterface().setCrashedWith(var3);
      }

      this.loadedClient = true;
   }

   public void loadServer() {
      Logger LOGGER = this.getLogger();
      LOGGER.info("Loading {} - Stage 1/2 (Server)", this.getModName());
      this.minimapServer = new XaeroMinimapServer(this);
      this.minimapServer.load();
   }

   protected void loadCommon() {
      this.versionID = "1.20_" + this.platformContext.getModInfoVersion();
      XaeroMinimapServerCore.modMain = this;
      this.messageHandler = this.platformContext.createMessageHandler(this);
      this.supportServerMods = new SupportServerMods();
      new HudCommonBase().setup(this);
   }

   protected void loadLaterServer() {
      Logger LOGGER = this.getLogger();
      LOGGER.info("Loading {} - Stage 2/2 (Server)", this.getModName());
      this.minimapServer.loadLater();
      this.loadedServer = true;
   }

   @Override
   public File getConfigFile() {
      return this.configFile;
   }

   @Override
   public File getModJAR() {
      return this.modJAR;
   }

   @Override
   public ModSettings getSettings() {
      return this.settings;
   }

   @Override
   public void setSettings(ModSettings minimapSettings) {
      this.settings = minimapSettings;
   }

   @Override
   public void resetSettings() throws IOException {
      this.settings = this.createModSettings();
      this.settings.loadDefaultSettings();
      this.interfaces.getMinimapInterface().getInfoDisplayManager().reset();
      ObjectCategoryDefaultSettingsSetter defaultSettings = ObjectCategoryDefaultSettingsSetter.Builder.getDefault()
         .setSettings(EntityRadarCategorySettings.SETTINGS)
         .build();
      defaultSettings.setDefaultsFor(this.getEntityRadarCategoryManager().getRootCategory(), false);
      this.getEntityRadarCategoryManager().save();
   }

   @Override
   public boolean isOutdated() {
      return this.isOutdated;
   }

   @Override
   public void setOutdated(boolean value) {
      this.isOutdated = value;
   }

   @Override
   public String getMessage() {
      return this.message;
   }

   @Override
   public void setMessage(String string) {
      this.message = string;
   }

   @Override
   public String getLatestVersion() {
      return this.latestVersion;
   }

   @Override
   public void setLatestVersion(String string) {
      this.latestVersion = string;
   }

   @Override
   public int getNewestUpdateID() {
      return this.newestUpdateID;
   }

   @Override
   public void setNewestUpdateID(int parseInt) {
      this.newestUpdateID = parseInt;
   }

   @Override
   public PatreonMod getPatreon() {
      return (PatreonMod)Patreon.getMods().get(this.getFileLayoutID());
   }

   @Override
   public File getWaypointsFile() {
      return this.waypointsFile;
   }

   @Override
   public File getWaypointsFolder() {
      return this.waypointsFolder;
   }

   @Override
   public WidgetScreenHandler getWidgetScreenHandler() {
      return this.widgetScreenHandler;
   }

   @Override
   public WidgetLoadingHandler getWidgetLoader() {
      return this.widgetLoader;
   }

   @Override
   public SupportMods getSupportMods() {
      return this.supportMods;
   }

   @Override
   public ModClientEvents getModEvents() {
      return this.modClientEvents;
   }

   @Override
   public GuiHelper getGuiHelper() {
      return this.guiHelper;
   }

   @Override
   public FieldValidatorHolder getFieldValidators() {
      return this.fieldValidators;
   }

   @Override
   public ControlsRegister getControlsRegister() {
      return this.controlsRegister;
   }

   @Override
   public ClientEvents getEvents() {
      return this.events;
   }

   @Override
   public InterfaceManager getInterfaces() {
      return this.interfaces;
   }

   @Override
   public InterfaceRenderer getInterfaceRenderer() {
      return this.interfaceRenderer;
   }

   @Override
   public void setLatestVersionMD5(String string) {
      this.latestVersionMD5 = string;
   }

   @Override
   public String getLatestVersionMD5() {
      return this.latestVersionMD5;
   }

   @Override
   public boolean isStandalone() {
      return false;
   }

   @Override
   public EntityRadarCategoryManager getEntityRadarCategoryManager() {
      return this.entityRadarCategoryManager;
   }

   @Override
   public boolean isFairPlay() {
      if (!this.loadedClient) {
         return false;
      } else {
         XaeroMinimapSession session = XaeroMinimapSession.getCurrentSession();
         return session != null && session.getMinimapProcessor().getForcedFairPlay();
      }
   }

   @Override
   public PlayerTrackerMinimapElementRenderer getTrackedPlayerRenderer() {
      return this.trackedPlayerRenderer;
   }

   @Override
   public PlayerTrackerSystemManager getPlayerTrackerSystemManager() {
      return this.playerTrackerSystemManager;
   }

   @Override
   public ServerPlayerTickHandler getServerPlayerTickHandler() {
      return this.serverPlayerTickHandler;
   }

   @Override
   public void setServerPlayerTickHandler(ServerPlayerTickHandler serverPlayerTickHandler) {
      this.serverPlayerTickHandler = serverPlayerTickHandler;
   }

   @Override
   public MinimapMessageHandler getMessageHandler() {
      return this.messageHandler;
   }

   @Override
   public CommonEvents getCommonEvents() {
      return this.commonEvents;
   }

   @Override
   public SupportServerMods getSupportServerMods() {
      return this.supportServerMods;
   }

   @Override
   public void setCommonConfigIO(CommonConfigIO serverConfigIO) {
      this.commonConfigIO = serverConfigIO;
   }

   @Override
   public void setCommonConfig(CommonConfig serverConfig) {
      this.commonConfig = serverConfig;
   }

   @Override
   public CommonConfigIO getCommonConfigIO() {
      return this.commonConfigIO;
   }

   @Override
   public CommonConfig getCommonConfig() {
      return this.commonConfig;
   }

   @Override
   public ClientEventsListener getForgeEventHandlerListener() {
      return this.clientEventsListener;
   }

   @Override
   public PlatformContext getPlatformContext() {
      return this.platformContext;
   }

   @Override
   public void ensureControlsRegister() {
      if (this.controlsRegister == null) {
         this.controlsRegister = this.createControlsRegister();
      }
   }

   @Override
   public ModClientEvents getModClientEvents() {
      return this.modClientEvents;
   }

   @Override
   public ModCommonEvents getModCommonEvents() {
      return this.modCommonEvents;
   }

   @Override
   public void tryLoadLater() {
   }

   @Override
   public void tryLoadLaterServer() {
   }

   @Override
   public String getVersionID() {
      return this.versionID;
   }

   @Override
   public Hud getHud() {
      return this.hud;
   }

   @Override
   public HudRenderer getHudRenderer() {
      return this.hudRenderer;
   }

   @Override
   public HudIO getHudIO() {
      return this.hudIO;
   }

   @Override
   public Minimap getMinimap() {
      return this.minimapInterface;
   }
}
