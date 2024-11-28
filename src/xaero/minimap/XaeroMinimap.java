package xaero.minimap;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModOrigin;
import net.fabricmc.loader.api.metadata.ModOrigin.Kind;
import net.minecraft.class_3264;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.XaeroMinimapCommonBase;
import xaero.common.XaeroMinimapSession;
import xaero.common.category.setting.ObjectCategoryDefaultSettingsSetter;
import xaero.common.config.CommonConfig;
import xaero.common.config.CommonConfigIO;
import xaero.common.config.CommonConfigInit;
import xaero.common.controls.ControlsRegister;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.events.FMLCommonEventHandler;
import xaero.common.events.FMLEventHandler;
import xaero.common.events.ForgeCommonEventHandler;
import xaero.common.events.ForgeEventHandler;
import xaero.common.events.ModEvents;
import xaero.common.file.SimpleBackup;
import xaero.common.gui.GuiHelper;
import xaero.common.gui.widget.WidgetLoadingHandler;
import xaero.common.gui.widget.WidgetScreenHandler;
import xaero.common.interfaces.InterfaceManager;
import xaero.common.interfaces.render.InterfaceRenderer;
import xaero.common.message.MinimapMessageHandler;
import xaero.common.minimap.radar.category.EntityRadarCategoryManager;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.minimap.radar.tracker.PlayerTrackerMinimapElementRenderer;
import xaero.common.minimap.radar.tracker.system.PlayerTrackerSystemManager;
import xaero.common.minimap.radar.tracker.system.impl.SyncedPlayerTrackerSystem;
import xaero.common.misc.Internet;
import xaero.common.mods.SupportMods;
import xaero.common.patreon.Patreon;
import xaero.common.patreon.PatreonMod;
import xaero.common.resource.ShaderResourceReloadListener;
import xaero.common.server.XaeroMinimapServer;
import xaero.common.server.core.XaeroMinimapServerCore;
import xaero.common.server.mods.SupportServerMods;
import xaero.common.server.player.ServerPlayerTickHandler;
import xaero.common.settings.ModOptions;
import xaero.common.settings.ModSettings;
import xaero.common.validator.FieldValidatorHolder;
import xaero.common.validator.NumericFieldValidator;
import xaero.common.validator.WaypointCoordinateFieldValidator;
import xaero.minimap.controls.MinimapControlsRegister;
import xaero.minimap.gui.MinimapGuiHelper;
import xaero.minimap.interfaces.MinimapInterfaceLoader;

public class XaeroMinimap extends AXaeroMinimap implements ClientModInitializer, DedicatedServerModInitializer {
   public static XaeroMinimap instance;
   private static final String versionID_minecraft = "1.20";
   private static final String versionID_mod = "23.8.3";
   private static final boolean versionID_fair = false;
   public static final String versionID = "1.20_23.8.3_fabric";
   private int newestUpdateID;
   private boolean isOutdated;
   private String fileLayoutID = "minimap_fabric";
   private String latestVersion;
   private String latestVersionMD5;
   private ModSettings settings;
   private String message = "";
   private ControlsRegister controlsRegister;
   private ForgeEventHandler events;
   private FMLEventHandler fmlEvents;
   private ModEvents modEvents;
   private InterfaceManager interfaces;
   private InterfaceRenderer interfaceRenderer;
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
   private ForgeCommonEventHandler commonEvents;
   private FMLCommonEventHandler fmlCommonEvents;
   private CommonConfigIO commonConfigIO;
   private CommonConfig commonConfig;
   private File modJAR = null;
   private File configFile;
   public File waypointsFile;
   public File waypointsFolder;
   private boolean loadLaterNeeded;
   private boolean loadLaterDone;
   private XaeroMinimapServer minimapServer;
   private Throwable firstStageError;

   public XaeroMinimap() {
      this.playerTrackerSystemManager = new PlayerTrackerSystemManager();
      new CommonConfigInit().init(this, "xaerominimap-common.txt");
   }

   public void onInitializeClient() {
      INSTANCE = this;
      instance = this;

      try {
         SupportMods.checkForMinimapDuplicates();
         this.loadCommon();
         this.loadClient();
      } catch (Throwable var2) {
         this.firstStageError = var2;
      }
   }

   public void onInitializeServer() {
      INSTANCE = this;
      instance = this;

      try {
         SupportMods.checkForMinimapDuplicates();
         this.loadCommon();
         this.loadServer();
      } catch (Throwable var2) {
         this.firstStageError = var2;
      }
   }

   @Override
   public Path getGameDir() {
      return FabricLoader.getInstance().getGameDir().normalize();
   }

   private void loadClient() throws IOException {
      MinimapLogs.LOGGER.info("Loading Xaero's Minimap - Stage 1/2");
      ModOptions.init(this);
      ModContainer modContainer = (ModContainer)FabricLoader.getInstance()
         .getModContainer("1.20_23.8.3_fabric".endsWith("fair") ? "xaerominimapfair" : "xaerominimap")
         .orElse(null);
      if (modContainer == null) {
         modContainer = (ModContainer)FabricLoader.getInstance().getModContainer("xaerodev").orElse(null);
      }

      ModOrigin origin = modContainer.getOrigin();
      Path modFile = origin.getKind() == Kind.PATH ? (Path)origin.getPaths().get(0) : null;
      if (modFile == null) {
         try {
            Class<?> quiltLoaderClass = Class.forName("org.quiltmc.loader.api.QuiltLoader");
            Method quiltGetModContainerMethod = quiltLoaderClass.getDeclaredMethod("getModContainer", String.class);
            Class<?> quiltModContainerAPIClass = Class.forName("org.quiltmc.loader.api.ModContainer");
            Method quiltGetSourcePathsMethod = quiltModContainerAPIClass.getDeclaredMethod("getSourcePaths");
            Object quiltModContainer = ((Optional)quiltGetModContainerMethod.invoke(null, modContainer.getMetadata().getId())).orElse(null);
            List<List<Path>> paths = (List<List<Path>>)quiltGetSourcePathsMethod.invoke(quiltModContainer);
            if (!paths.isEmpty() && !paths.get(0).isEmpty()) {
               modFile = paths.get(0).get(0);
            }
         } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException var14) {
         }
      }

      new XaeroMinimapClient().preInit(modContainer.getMetadata().getId(), this);
      String fileName = modFile.getFileName().toString();
      if (fileName.endsWith(".jar")) {
         this.modJAR = modFile.toFile();
      }

      Path gameDir = this.getGameDir();
      Path config = FabricLoader.getInstance().getConfigDir();
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
         MinimapLogs.LOGGER.info("Backing up XaeroWaypoints...");
         SimpleBackup.copyDirectoryWithContents(this.waypointsFolder.toPath(), waypointsFolderBackup062020, 32);
         MinimapLogs.LOGGER.info("Done backing up XaeroWaypoints!");
      }

      this.configFile = config.resolve("xaerominimap.txt").toFile();
      File oldConfigFile = gameDir.resolve("config").resolve("xaerominimap.txt").toFile();
      if (oldConfigFile.exists() && !this.configFile.exists()) {
         Files.move(oldConfigFile.toPath(), this.configFile.toPath());
      }

      this.widgetScreenHandler = new WidgetScreenHandler();
      this.widgetLoader = new WidgetLoadingHandler(this.widgetScreenHandler);
      this.controlsRegister = new MinimapControlsRegister();
      this.guiHelper = new MinimapGuiHelper(this);
      this.fieldValidators = new FieldValidatorHolder(new NumericFieldValidator(), new WaypointCoordinateFieldValidator());
      this.interfaceRenderer = new InterfaceRenderer(this);
      MinimapInterfaceLoader interfaceLoader = new MinimapInterfaceLoader();
      this.interfaces = new InterfaceManager(this, interfaceLoader);
      this.modEvents = new ModEvents(this);
      this.trackedPlayerRenderer = PlayerTrackerMinimapElementRenderer.Builder.begin(this).build();
      XaeroMinimapCore.modMain = this;
      ResourceManagerHelper.get(class_3264.field_14188).registerReloadListener(new ShaderResourceReloadListener());
      this.loadLaterNeeded = true;
   }

   @Override
   public void tryLoadLater() {
      if (!this.loadLaterDone) {
         if (this.firstStageError != null) {
            throw new RuntimeException(this.firstStageError);
         } else if (this.loadLaterNeeded) {
            this.loadLaterDone = true;
            MinimapLogs.LOGGER.info("Loading Xaero's Minimap - Stage 2/2");

            try {
               this.events = new ForgeEventHandler(this);
               this.fmlEvents = new FMLEventHandler(this);
            } catch (Throwable var5) {
               MinimapLogs.LOGGER.error("suppressed exception", var5);
            }

            try {
               Path gameDir = this.getGameDir();
               File old_optionsFile = gameDir.resolve("xaerominimap.txt").toFile();
               if (old_optionsFile.exists() && !this.configFile.exists()) {
                  this.configFile.getParentFile().mkdirs();
                  Files.move(old_optionsFile.toPath(), this.configFile.toPath());
               }

               if (old_waypointsFile.exists() && !this.waypointsFile.exists()) {
                  this.waypointsFile.getParentFile().mkdirs();
                  Files.move(old_waypointsFile.toPath(), this.waypointsFile.toPath());
               }

               this.settings = new ModSettings(this);
               this.settings.loadSettings();
               this.entityRadarCategoryManager = EntityRadarCategoryManager.Builder.getDefault().setModMain(this).build();
               this.entityRadarCategoryManager.init();
               Patreon.checkPatreon(this);
               Internet.checkModVersion(this);
               if (this.isOutdated) {
                  PatreonMod patreonEntry = this.getPatreon();
                  if (patreonEntry != null) {
                     patreonEntry.modJar = this.modJAR;
                     patreonEntry.currentVersion = "1.20_23.8.3_fabric";
                     patreonEntry.latestVersion = this.latestVersion;
                     patreonEntry.md5 = this.latestVersionMD5;
                     patreonEntry.onVersionIgnore = () -> {
                        ModSettings.ignoreUpdate = this.newestUpdateID;

                        try {
                           this.getSettings().saveSettings();
                        } catch (IOException var2x) {
                           MinimapLogs.LOGGER.error("suppressed exception", var2x);
                        }
                     };
                     Patreon.addOutdatedMod(patreonEntry);
                  }
               }

               this.playerTrackerSystemManager.register("minimap_synced", new SyncedPlayerTrackerSystem());
               this.supportMods = new SupportMods(this);
               this.getInterfaces().getMinimapInterface().getOverMapRendererHandler().add(this.trackedPlayerRenderer);
            } catch (Throwable var4) {
               MinimapLogs.LOGGER.error("error", var4);
               this.interfaces.getMinimapInterface().setCrashedWith(var4);
            }

            this.loaded = true;
         }
      }
   }

   public void loadServer() {
      MinimapLogs.LOGGER.info("Loading Xaero's Minimap - Stage 1/2 (Server)");
      this.minimapServer = new XaeroMinimapServer(this);
      this.minimapServer.load();
      this.loadLaterNeeded = true;
   }

   private void loadCommon() {
      XaeroMinimapServerCore.modMain = this;
      this.messageHandler = new MinimapMessageHandler();
      this.commonEvents = new ForgeCommonEventHandler(this);
      this.fmlCommonEvents = new FMLCommonEventHandler(this);
      this.supportServerMods = new SupportServerMods();
      new XaeroMinimapCommonBase().setup(this);
   }

   @Override
   public void tryLoadLaterServer() {
      if (!this.loadLaterDone) {
         if (this.firstStageError != null) {
            throw new RuntimeException(this.firstStageError);
         } else if (this.loadLaterNeeded) {
            MinimapLogs.LOGGER.info("Loading Xaero's Minimap - Stage 2/2 (Server)");
            this.loadLaterDone = true;
            this.minimapServer.loadLater();
         }
      }
   }

   @Override
   public String getVersionsURL() {
      return "http://data.chocolateminecraft.com/Versions_" + Patreon.getKEY_VERSION2() + "/Minimap.dat";
   }

   @Override
   public String getUpdateLink() {
      return "http://chocolateminecraft.com/update/minimap.html";
   }

   @Override
   public String getFileLayoutID() {
      return this.fileLayoutID;
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
      this.settings = new ModSettings(this);
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
      return (PatreonMod)Patreon.getMods().get(this.fileLayoutID);
   }

   @Override
   public String getVersionID() {
      return "1.20_23.8.3_fabric";
   }

   @Override
   public Object getSettingsKey() {
      return MinimapControlsRegister.keyBindSettings;
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
   public XaeroMinimapSession createSession() {
      return new XaeroMinimapStandaloneSession(this);
   }

   @Override
   public SupportMods getSupportMods() {
      return this.supportMods;
   }

   @Override
   public FMLEventHandler getFMLEvents() {
      return this.fmlEvents;
   }

   @Override
   public ModEvents getModEvents() {
      return this.modEvents;
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
   public ForgeEventHandler getEvents() {
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
      return true;
   }

   @Override
   public EntityRadarCategoryManager getEntityRadarCategoryManager() {
      return this.entityRadarCategoryManager;
   }

   @Override
   public boolean isFairPlay() {
      if (this.loaded) {
         XaeroMinimapSession session = XaeroMinimapSession.getCurrentSession();
         if (session != null && session.getMinimapProcessor().getForcedFairPlay()) {
            return true;
         }
      }

      return false;
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
   public ForgeCommonEventHandler getCommonEvents() {
      return this.commonEvents;
   }

   @Override
   public SupportServerMods getSupportServerMods() {
      return this.supportServerMods;
   }

   @Override
   public FMLCommonEventHandler getFMLCommonEvents() {
      return this.fmlCommonEvents;
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
}
