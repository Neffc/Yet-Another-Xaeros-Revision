package xaero.minimap;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xaero.common.HudClientOnlyBase;
import xaero.common.HudMod;
import xaero.common.XaeroMinimapSession;
import xaero.common.controls.ControlsRegister;
import xaero.common.events.ClientEventsListener;
import xaero.common.gui.GuiHelper;
import xaero.common.interfaces.IInterfaceLoader;
import xaero.common.interfaces.InterfaceManager;
import xaero.common.mods.SupportMods;
import xaero.common.patreon.Patreon;
import xaero.common.settings.ModSettings;
import xaero.minimap.controls.MinimapControlsRegister;
import xaero.minimap.gui.MinimapGuiHelper;
import xaero.minimap.interfaces.MinimapInterfaceLoader;

public abstract class XaeroMinimap extends HudMod {
   public static final Logger LOGGER = LogManager.getLogger();
   public static XaeroMinimap instance;
   public static final String MOD_ID = "xaerominimap";

   public XaeroMinimap() {
      instance = this;
   }

   @Override
   protected void loadClient() throws IOException {
      super.loadClient();
   }

   @Override
   protected void loadCommon() {
      SupportMods.checkForMinimapDuplicates("xaero.pvp.BetterPVP");
      super.loadCommon();
   }

   @Override
   protected String getCommonConfigFileName() {
      return "xaerominimap-common.txt";
   }

   @Override
   public String getModId() {
      return "xaerominimap";
   }

   @Override
   protected ModSettings createModSettings() {
      return new ModSettings(this);
   }

   @Override
   protected GuiHelper createGuiHelper() {
      return new MinimapGuiHelper(this);
   }

   @Override
   protected IInterfaceLoader createInterfaceLoader() {
      return new MinimapInterfaceLoader();
   }

   @Override
   protected InterfaceManager createInterfaceManager(IInterfaceLoader loader) throws IOException {
      return new InterfaceManager(this, loader);
   }

   @Override
   protected String getConfigFileName() {
      return "xaerominimap.txt";
   }

   @Override
   protected String getOldConfigFileName() {
      return "xaerominimap.txt";
   }

   @Override
   protected HudClientOnlyBase createClientOnly() {
      return new MinimapClientOnly();
   }

   @Override
   protected String getModName() {
      return "Xaero's Minimap";
   }

   @Override
   protected Logger getLogger() {
      return LOGGER;
   }

   @Override
   protected ClientEventsListener createForgeEventHandlerListener() {
      return new ClientEventsListener();
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
   protected ControlsRegister createControlsRegister() {
      return new MinimapControlsRegister();
   }

   @Override
   public XaeroMinimapSession createSession() {
      return new XaeroMinimapStandaloneSession(this);
   }

   @Override
   public Object getSettingsKey() {
      return MinimapControlsRegister.keyBindSettings;
   }
}
