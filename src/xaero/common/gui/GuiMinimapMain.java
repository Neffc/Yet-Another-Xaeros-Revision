package xaero.common.gui;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_437;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.settings.ModOptions;
import xaero.common.settings.ModSettings;

public class GuiMinimapMain extends GuiMinimapSettings {
   private ISettingEntry[] mainEntries;
   private ISettingEntry[] searchableEntries;

   public GuiMinimapMain(IXaeroMinimap modMain, class_437 par1GuiScreen, class_437 escScreen) {
      super(modMain, class_2561.method_43471("gui.xaero_minimap_settings"), par1GuiScreen, escScreen);
      ScreenSwitchSettingEntry changePositionEntry = new ScreenSwitchSettingEntry(
         "gui.xaero_change_position", (current, escape) -> new GuiEditMode(modMain, current, escape, "gui.xaero_minimap_guide", false), null, true
      );
      ScreenSwitchSettingEntry viewSettingsEntry = new ScreenSwitchSettingEntry(
         "gui.xaero_minimap_view_settings", (current, escape) -> new GuiMinimapViewSettings(modMain, current, escape), null, true
      );
      ScreenSwitchSettingEntry entityRadarSettingsEntry = new ScreenSwitchSettingEntry(
         "gui.xaero_entity_radar_settings", (current, escape) -> new GuiEntityRadarSettings(modMain, current, escape), null, true
      );
      ScreenSwitchSettingEntry blockMapSettingsEntry = new ScreenSwitchSettingEntry(
         "gui.xaero_minimap_block_map_settings", (current, escape) -> new GuiMinimapBlockMapSettings(modMain, current, escape), null, true
      );
      ScreenSwitchSettingEntry overlaySettingsEntry = new ScreenSwitchSettingEntry(
         "gui.xaero_overlay_settings", (current, escape) -> new GuiMinimapOverlaysSettings(modMain, current, escape), null, true
      );
      ScreenSwitchSettingEntry infoSettingsEntry = new ScreenSwitchSettingEntry(
         "gui.xaero_minimap_info_settings", (current, escape) -> new GuiMinimapInfoSettings(modMain, current, escape), null, true
      );
      ScreenSwitchSettingEntry waypointSettingsEntry = new ScreenSwitchSettingEntry(
         "gui.xaero_waypoint_settings", (current, escape) -> new GuiWaypointSettings(modMain, current, escape), null, true
      );
      ScreenSwitchSettingEntry miscSettingsEntry = new ScreenSwitchSettingEntry(
         "gui.xaero_minimap_misc_settings", (current, escape) -> new GuiMinimapMiscSettings(modMain, current, escape), null, true
      );
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      ScreenSwitchSettingEntry waypointsEntry = new ScreenSwitchSettingEntry(
         "gui.xaero_waypoints",
         (current, escape) -> {
            XaeroMinimapSession minimapSession2 = XaeroMinimapSession.getCurrentSession();
            return minimapSession2 != null && modMain.getSettings().waypointsGUI(minimapSession2.getWaypointsManager())
               ? new GuiWaypoints(modMain, minimapSession2, this, escape)
               : null;
         },
         null,
         minimapSession != null && modMain.getSettings().waypointsGUI(minimapSession.getWaypointsManager())
      );
      List<ISettingEntry> mainEntriesBuilder = Lists.newArrayList(
         new ISettingEntry[]{
            new ConfigSettingEntry(ModOptions.MINIMAP),
            changePositionEntry,
            viewSettingsEntry,
            blockMapSettingsEntry,
            entityRadarSettingsEntry,
            overlaySettingsEntry,
            infoSettingsEntry,
            waypointSettingsEntry,
            miscSettingsEntry,
            waypointsEntry
         }
      );
      if (modMain.isStandalone()) {
         mainEntriesBuilder.add(
            new ScreenSwitchSettingEntry(
               "gui.xaero_reset_defaults", (current, escape) -> new GuiReset(this::resetConfirmResult, par1GuiScreen, escape), null, true
            )
         );
      }

      this.mainEntries = mainEntriesBuilder.toArray(new ISettingEntry[0]);
      LinkedHashSet<ISettingEntry> searchableEntriesBuilder = new LinkedHashSet<>();

      for (ISettingEntry entry : this.mainEntries) {
         if (entry instanceof ScreenSwitchSettingEntry) {
            ScreenSwitchSettingEntry screenSwitchEntry = (ScreenSwitchSettingEntry)entry;
            class_437 tempScreen = screenSwitchEntry.getScreenFactory().apply(this, this);
            if (tempScreen instanceof GuiSettings) {
               GuiSettings tempSettingsScreen = (GuiSettings)tempScreen;
               ISettingEntry[] settingsScreenEntries = tempSettingsScreen.getEntriesCopy();
               if (settingsScreenEntries != null) {
                  searchableEntriesBuilder.addAll(Arrays.asList(settingsScreenEntries));
               }
            } else {
               searchableEntriesBuilder.add(entry);
            }
         } else {
            searchableEntriesBuilder.add(entry);
         }
      }

      this.searchableEntries = searchableEntriesBuilder.toArray(new ISettingEntry[0]);
   }

   @Override
   public void method_25426() {
      if (this.entryFilter.isEmpty()) {
         this.entries = this.mainEntries;
      } else {
         this.entries = this.searchableEntries;
      }

      super.method_25426();
      if (ModSettings.serverSettings != ModSettings.defaultSettings) {
         this.screenTitle = class_2561.method_43470("§e" + class_1074.method_4662("gui.xaero_server_disabled", new Object[0]));
      }
   }
}
