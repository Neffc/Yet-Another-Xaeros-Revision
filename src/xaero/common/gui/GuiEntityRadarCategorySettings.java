package xaero.common.gui;

import java.io.IOException;
import java.nio.file.Files;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_437;
import xaero.common.AXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.category.setting.ObjectCategoryDefaultSettingsSetter;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.GuiCategoryUIEditorDataConverter;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.EntityRadarCategoryManager;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.minimap.radar.category.ui.GuiEntityRadarCategoryUIEditorData;
import xaero.common.minimap.radar.category.ui.GuiEntityRadarCategoryUIEditorDataConverter;
import xaero.common.minimap.radar.category.ui.data.GuiEntityRadarCategoryUIEditorSettingsData;

public class GuiEntityRadarCategorySettings
   extends GuiCategorySettings<EntityRadarCategory, GuiEntityRadarCategoryUIEditorData, EntityRadarCategory.Builder, GuiEntityRadarCategoryUIEditorSettingsData<?>, GuiEntityRadarCategoryUIEditorSettingsData.Builder, GuiEntityRadarCategoryUIEditorData.Builder> {
   private final EntityRadarCategoryManager entityRadarCategoryManager;

   protected GuiEntityRadarCategorySettings(AXaeroMinimap modMain, class_437 parent, class_437 escape) {
      super(
         modMain,
         parent,
         escape,
         class_2561.method_43471("gui.xaero_entity_radar_categories"),
         GuiEntityRadarCategoryUIEditorDataConverter.Builder.getDefault().build()
      );
      this.entityRadarCategoryManager = modMain.getEntityRadarCategoryManager();
   }

   protected GuiEntityRadarCategoryUIEditorData constructEditorData(
      GuiCategoryUIEditorDataConverter<EntityRadarCategory, GuiEntityRadarCategoryUIEditorData, EntityRadarCategory.Builder, GuiEntityRadarCategoryUIEditorSettingsData<?>, GuiEntityRadarCategoryUIEditorSettingsData.Builder, GuiEntityRadarCategoryUIEditorData.Builder> dataConverter
   ) {
      EntityRadarCategory rootCategory = this.modMain.getEntityRadarCategoryManager().getRootCategory();
      return dataConverter.convert(rootCategory, true);
   }

   protected GuiEntityRadarCategoryUIEditorData constructDefaultData(
      GuiCategoryUIEditorDataConverter<EntityRadarCategory, GuiEntityRadarCategoryUIEditorData, EntityRadarCategory.Builder, GuiEntityRadarCategoryUIEditorSettingsData<?>, GuiEntityRadarCategoryUIEditorSettingsData.Builder, GuiEntityRadarCategoryUIEditorData.Builder> dataConverter
   ) {
      this.modMain.getSettings().resetEntityRadarBackwardsCompatibilityConfig();
      EntityRadarCategoryManager manager = this.modMain.getEntityRadarCategoryManager();
      EntityRadarCategory rootCategory;
      if (Files.exists(manager.getSecondaryFilePath())) {
         try {
            rootCategory = manager.getSecondaryFileIO().loadRootCategory();
         } catch (IOException var5) {
            throw new RuntimeException(var5);
         }
      } else {
         rootCategory = manager.getDefaultCategoryConfigurator().setupDefault(this.modMain.getSettings());
      }

      ObjectCategoryDefaultSettingsSetter defaultSettings = ObjectCategoryDefaultSettingsSetter.Builder.getDefault()
         .setSettings(EntityRadarCategorySettings.SETTINGS)
         .build();
      defaultSettings.setDefaultsFor(rootCategory, true);
      return dataConverter.convert(rootCategory, true);
   }

   protected void onConfigConfirmed(EntityRadarCategory confirmedRootCategory) {
      this.entityRadarCategoryManager.setRootCategory(confirmedRootCategory);
      this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().resetEntityIcons();
      this.entityRadarCategoryManager.save();
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      if (minimapSession != null) {
         MinimapProcessor minimapProcessor = minimapSession.getMinimapProcessor();
         MinimapRadar radar = minimapProcessor.getEntityRadar();
         radar.updateRadar(class_310.method_1551().field_1687, class_310.method_1551().field_1724, class_310.method_1551().method_1560(), minimapProcessor);
      }
   }
}
